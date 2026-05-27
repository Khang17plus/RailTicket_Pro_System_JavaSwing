package GUI;

import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;

import com.formdev.flatlaf.FlatLightLaf;

import Controller.KhuyenMaiController;
import Entity.KhuyenMai;


public class KhuyenMaiPanel extends JPanel {
    private KhuyenMaiController controller;
    private Component component = new Component(); // Class Component dùng chung của bạn
    
    // Thêm 3 biến này vào thuộc tính lớp để quản lý các số thống kê
    private JLabel lblDangApDung;
    private JLabel lblSapDienRa;
    private JLabel lblTongChienDich;
    private JLabel lblHetHan;

    private String[] khuyenMaiOptions = {
        "Thêm khuyến mãi",
        "Xóa khuyến mãi",
        "Sửa thông tin"
    };

    private JTable table;
    private DefaultTableModel tableModel;

    // --- THÀNH PHẦN BỔ SUNG: Khai báo các nút bấm để gắn sự kiện Excel ---
    private JButton btnExportExcel;
    private JButton btnImportExcel;

    public void setController(KhuyenMaiController controller) {
        this.controller = controller;
    }

    // Tái sử dụng style Button từ KhachHangPanel
    public JButton createButtonExcel(String Cmt) {
        JButton btn = new JButton(Cmt);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 13;";
        
        if (Cmt.contains("Nhập")) {
            btn.setBackground(new Color(59, 130, 246)); // Blue
        } else if (Cmt.contains("Tìm")) {
            btn.setBackground(Color.gray);
            btn.setForeground(Color.BLACK);
            btn.setPreferredSize(new Dimension(60, 36));
        } else {
            btn.setBackground(new Color(34, 197, 94)); // Green
        }
        
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", style + "margin:8,15,8,15");
        return btn;
    }

    // Tái sử dụng style Card thống kê
    public JPanel createCardstatistical(String IconURL, String title, String value) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        
        // --- LÀM Y HỆT KHÁCH HÀNG: Đọc ảnh và scale kích thước ---
        ImageIcon icon = new ImageIcon(IconURL);
        Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);

        if (title.equals("Đang áp dụng")) {
            lblDangApDung = valueLabel;
        } else if (title.equals("Sắp diễn ra")) {
            lblSapDienRa = valueLabel;
        } else if (title.equals("Tổng chiến dịch")) {
            lblTongChienDich = valueLabel;
        } else if (title.equals("Hết hạn")) { 
            lblHetHan = valueLabel;
        }

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        card.putClientProperty("FlatLaf.style", "arc:10; border:10,10,10,10; background:#FFFFFF");
        return card;
    }

    public KhuyenMaiPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- HEADER SECTION ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250));

        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setOpaque(false);
        
        JLabel title = new JLabel("Quản lý chương trình khuyến mãi");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel sub = new JLabel("Tạo và quản lý các mã giảm giá, sự kiện ưu đãi metro");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        
        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);

        JPanel headerR = new JPanel(new GridLayout(1, 4, 15, 0));
        headerR.setOpaque(false);
        headerR.add(createCardstatistical("img/expired.png", "Hết hạn", "00"));      
        headerR.add(createCardstatistical("img/valid.png", "Đang áp dụng", "00"));
        headerR.add(createCardstatistical("img/time.png", "Sắp diễn ra", "00"));
        headerR.add(createCardstatistical("img/equal.png", "Tổng chiến dịch", "00"));

        // --- ACTION PANEL (Search & Excel) ---
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(350, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập mã KM hoặc tên chương trình...");
        JButton btnSearch = createButtonExcel("Tìm Kiếm");
        
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // --- THÀNH PHẦN SỬA ĐỔI: Khởi tạo biến rõ ràng cho nút Excel ---
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        btnExportExcel = createButtonExcel("Xuất file excel");
        btnImportExcel = createButtonExcel("Nhập file excel");
        
        rightPanel.add(btnExportExcel);
        rightPanel.add(btnImportExcel);

        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.CENTER);

        header.add(headerL, BorderLayout.WEST);
        header.add(headerR, BorderLayout.EAST);
        header.add(actionPanel, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // --- MAIN TABLE SECTION ---
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.putClientProperty("FlatLaf.style", "arc:20; border:12,12,12,12; background:#FFFFFF");

        String[] columns = {"Mã KM", "Tên chương trình",  "Giá trị", "Loại", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(232, 240, 254));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 40));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        main.add(tableCard, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
        
        // --- ĐĂNG KÝ CÁC SỰ KIỆN LẮNG NGHE (LISTENERS) ---
        
        // 1. Sự kiện tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (controller != null) {
                controller.timKiemKhuyenMai(keyword);
            }
        });

        // 2. THÀNH PHẦN BỔ SUNG: Sự kiện xuất file Excel
        btnExportExcel.addActionListener(e -> {
            if (controller == null) return;
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file Excel Khuyến Mãi");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                
                // Ép đuôi file luôn là .xlsx
                if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                    fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".xlsx");
                }
                
                if (controller.exportToExcel(fileToSave)) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel khuyến mãi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi ghi file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 3. THÀNH PHẦN BỔ SUNG: Sự kiện nhập file Excel
        btnImportExcel.addActionListener(e -> {
            if (controller == null) return;
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel Khuyến Mãi để nạp");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToOpen = fileChooser.getSelectedFile();
                
                // Gọi sang hàm xử lý import từ Controller
                int rowsImported = controller.importFromExcel(fileToOpen);
                
                if (rowsImported > 0) {
                    JOptionPane.showMessageDialog(this, "Nhập thành công " + rowsImported + " chương trình mới từ Excel!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu mới nào được thêm vào hệ thống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // 🔥 ĐÃ SỬA ĐỔI: Đồng bộ hóa logic hiển thị bảng JTable (Gom Tạm Ngưng về Hết Hạn)
    public void setData(List<KhuyenMai> list) {
        tableModel.setRowCount(0);
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        java.time.LocalDateTime bayGio = java.time.LocalDateTime.now();

        for (KhuyenMai km : list) {
            String trangThaiHienThi = ""; 
            
            // Nếu bị tắt trạng thái HOẶC đã quá ngày kết thúc -> Gom hết về Hết hạn
            if (!km.isTrangThai() || (km.getNgayKetThuc() != null && bayGio.isAfter(km.getNgayKetThuc()))) {
                trangThaiHienThi = "Hết hạn";
            } 
            // Nếu bật trạng thái nhưng chưa tới ngày chạy -> Sắp diễn ra
            else if (km.getNgayBatDau() != null && bayGio.isBefore(km.getNgayBatDau())) {
                trangThaiHienThi = "Sắp diễn ra";
            } 
            // Thỏa mãn mọi điều kiện -> Đang áp dụng
            else {
                trangThaiHienThi = "Đang áp dụng";
            }

            tableModel.addRow(new Object[]{
                km.getMaKM(),               
                km.getTenKM(),              
                km.getGiaTri(),             
                km.getLoaiKM(),              
                km.getNgayBatDau() != null ? km.getNgayBatDau().format(dtf) : "", 
                km.getNgayKetThuc() != null ? km.getNgayKetThuc().format(dtf) : "", 
                trangThaiHienThi
            });
        }
    }

    public void updateThongKeCoDinh(List<KhuyenMai> allList) {
        capNhatThongKeCoDinh(allList);
    }

    // 🔥 ĐÃ SỬA ĐỔI: Đồng bộ hóa logic đếm trên các Card CardStatistical khớp 100% với bảng
    public void capNhatThongKeCoDinh(List<KhuyenMai> allList) {
        int dangApDung = 0;
        int sapDienRa = 0;
        int hetHan = 0; 
        int tongChienDich = allList.size();
        
        java.time.LocalDateTime bayGio = java.time.LocalDateTime.now();

        for (KhuyenMai km : allList) {
            // Logic đếm bê nguyên từ hàm hiển thị bảng xuống để cam đoan khớp số liệu
            if (!km.isTrangThai() || (km.getNgayKetThuc() != null && bayGio.isAfter(km.getNgayKetThuc()))) {
                hetHan++;
            } else if (km.getNgayBatDau() != null && bayGio.isBefore(km.getNgayBatDau())) {
                sapDienRa++;
            } else {
                dangApDung++;
            }
        }

        if (lblHetHan != null) lblHetHan.setText(String.format("%02d", hetHan)); 
        if (lblDangApDung != null) lblDangApDung.setText(String.format("%02d", dangApDung));
        if (lblSapDienRa != null) lblSapDienRa.setText(String.format("%02d", sapDienRa));
        if (lblTongChienDich != null) lblTongChienDich.setText(String.format("%02d", tongChienDich));
    }

    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        for (String option : khuyenMaiOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = it.getText();
                switch (text) {
                    case "Thêm khuyến mãi": themKhuyenMai(); break;
                    case "Xóa khuyến mãi": xoaKhuyenMai(); break;
                    case "Sửa thông tin": suaKhuyenMai(); break;
                }
            });
            submenu.add(it);
        }
        return submenu;
    }
    
    private void themKhuyenMai() {
        String[] labels = {"Mã KM", "Tên KM", "Giá trị (%)", "Loại KM", "Ngày BĐ (dd/MM/yyyy HH:mm)", "Ngày KT (dd/MM/yyyy HH:mm)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) fields[i] = new JTextField();

        if (controller != null) {
            String maTuDong = controller.phatSinhMaTuDong();
            fields[0].setText(maTuDong); 
        }
        fields[0].setEditable(false); 
        fields[0].setBackground(new Color(240, 240, 240)); 

        JButton btnCancel = new JButton("Hủy");
        JButton btnSave = new JButton("Lưu");

        JDialog dialog = component.createDinamicForm("Thêm Khuyến Mãi", "Nhập Thông Tin", 
                "Thiết lập chương trình ưu đãi mới", labels, fields, new JButton[]{btnCancel, btnSave});

        btnCancel.addActionListener(e -> dialog.dispose());
        
        btnSave.addActionListener(e -> {
            try {
                String ma = fields[0].getText().trim();
                String ten = fields[1].getText().trim();
                double giaTriNhap = Double.parseDouble(fields[2].getText().trim()); 
                String loaiNhap = fields[3].getText().trim(); 

                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                java.time.LocalDateTime bd = java.time.LocalDateTime.parse(fields[4].getText().trim(), formatter);
                java.time.LocalDateTime kt = java.time.LocalDateTime.parse(fields[5].getText().trim(), formatter);
                
                KhuyenMai km = new KhuyenMai(ma, ten, loaiNhap, giaTriNhap, bd, kt, true);

                if (controller != null && controller.themKhuyenMai(km)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
                    controller.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi lưu vào CSDL! Kiểm tra lại file DAO.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: Ô Giá trị phải nhập số!");
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: Sai định dạng ngày (dd/MM/yyyy HH:mm)!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    private void xoaKhuyenMai() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chương trình khuyến mãi trong bảng để xóa!");
            return;
        }

        String maKM = tableModel.getValueAt(row, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn xóa mã khuyến mãi: " + maKM + " không?\nLưu ý: Hành động này không thể hoàn tác!", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller != null && controller.xoaKhuyenMai(maKM)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công mã: " + maKM);
                controller.loadDataToTable(); 
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Có thể mã này đang được sử dụng ở hóa đơn khác.");
            }
        }
    }

    private void suaKhuyenMai() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chương trình để sửa!");
            return;
        }

        String[] labels = {"Mã KM (Không sửa)", "Tên KM", "Giá trị (%)", "Loại KM", "Ngày BĐ (dd/MM/yyyy HH:mm)", "Ngày KT (dd/MM/yyyy HH:mm)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
            fields[i].setText(tableModel.getValueAt(row, i).toString());
        }
        
        fields[0].setEditable(false);
        fields[0].setBackground(new Color(240, 240, 240));

        JButton btnCancel = new JButton("Hủy");
        JButton btnUpdate = new JButton("Cập nhật");

        JDialog dialog = component.createDinamicForm("Sửa Khuyến Mãi", "Chỉnh Sửa Thông Tin", 
                "Cập nhật lại thông tin chương trình ưu đãi", labels, fields, new JButton[]{btnCancel, btnUpdate});

        btnCancel.addActionListener(e -> dialog.dispose());

        btnUpdate.addActionListener(e -> {
            try {
                String ma = fields[0].getText().trim();
                String ten = fields[1].getText().trim();
                double giaTri = Double.parseDouble(fields[2].getText().trim());
                String loai = fields[3].getText().trim();

                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                java.time.LocalDateTime bd = java.time.LocalDateTime.parse(fields[4].getText().trim(), formatter);
                java.time.LocalDateTime kt = java.time.LocalDateTime.parse(fields[5].getText().trim(), formatter);

                KhuyenMai km = new KhuyenMai(ma, ten, loai, giaTri, bd, kt, true);

                if (controller != null && controller.updateKhuyenMai(km)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    controller.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật dữ liệu!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi định dạng: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }
}