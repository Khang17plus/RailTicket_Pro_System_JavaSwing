package GUI;

import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser; 

import Controller.KhuyenMaiController;
import Entity.KhuyenMai;

public class KhuyenMaiPanel extends JPanel {
    private KhuyenMaiController controller;
    private Component component = new Component(); 
    
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

    private JButton btnExportExcel;
    private JButton btnImportExcel;

    public void setController(KhuyenMaiController controller) {
        this.controller = controller;
    }

    public JButton createButtonExcel(String Cmt) {
        JButton btn = new JButton(Cmt);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 13;";
        
        if (Cmt.contains("Nhập")) {
            btn.setBackground(new Color(59, 130, 246)); 
            btn.setForeground(Color.WHITE);
        } else if (Cmt.contains("Tìm")) {
            btn.setBackground(Color.gray);
            btn.setForeground(Color.BLACK);
            btn.setPreferredSize(new Dimension(60, 36));
        } else {
            btn.setBackground(new Color(34, 197, 94)); 
            btn.setForeground(Color.WHITE);
        }
        
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", style + "margin:8,15,8,15");
        return btn;
    }

    public JPanel createCardstatistical(String IconURL, String title, String value) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        
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
        JLabel sub = new JLabel("Tạo và quản lý các mã giảm giá, sự kiện ưu đãi");
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

        // --- ACTION PANEL ---
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

        String[] columns = {"Mã KM", "Tên chương trình", "Giá trị", "Loại", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"};
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
        
        // Listeners
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (controller != null) {
                controller.timKiemKhuyenMai(keyword);
            }
        });

        btnExportExcel.addActionListener(e -> {
            if (controller == null) return;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file Excel Khuyến Mãi");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                    fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".xlsx");
                }
                if (controller.exportToExcel(fileToSave)) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi ghi file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnImportExcel.addActionListener(e -> {
            if (controller == null) return;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel Khuyến Mãi để nạp");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToOpen = fileChooser.getSelectedFile();
                int rowsImported = controller.importFromExcel(fileToOpen);
                if (rowsImported > 0) {
                    JOptionPane.showMessageDialog(this, "Nhập thành công " + rowsImported + " chương trình mới!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu mới nào được thêm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void setData(List<KhuyenMai> list) {
        tableModel.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime bayGio = LocalDateTime.now();

        for (KhuyenMai km : list) {
            String trangThaiHienThi = ""; 
            if (!km.isTrangThai() || (km.getNgayKetThuc() != null && bayGio.isAfter(km.getNgayKetThuc()))) {
                trangThaiHienThi = "Hết hạn";
            } else if (km.getNgayBatDau() != null && bayGio.isBefore(km.getNgayBatDau())) {
                trangThaiHienThi = "Sắp diễn ra";
            } else {
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

    public void capNhatThongKeCoDinh(List<KhuyenMai> allList) {
        int dangApDung = 0;
        int sapDienRa = 0;
        int hetHan = 0; 
        int tongChienDich = allList.size();
        LocalDateTime bayGio = LocalDateTime.now();

        for (KhuyenMai km : allList) {
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
    
    // 🔥 CẢI TIẾN: Hàm nhúng nút lịch trực tiếp vào đuôi ô JTextField bằng FlatLaf Trailing Component
    private void setupCalendarButton(JTextField field) {
        field.setEditable(false);
        field.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnCalendar = new JButton("📅");
        btnCalendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalendar.setContentAreaFilled(false);
        btnCalendar.setFocusPainted(false);
        
        // 1. Chỉ định nút này thuộc nhóm nút trong JTextField
        btnCalendar.putClientProperty("FlatLaf.styleClass", "textFieldButton");
        
        // 2. 🔥 CÁCH FIX TRIỆT ĐỂ: Ép FlatLaf sử dụng một chuỗi style trống, xóa sạch các thuộc tính thừa (như toolbar.margin)
        btnCalendar.putClientProperty("FlatLaf.style", "margin:0,0,0,0; border:0,0,0,0; focusWidth:0");
        
        // 3. Đảm bảo xóa sạch Border kiểu cũ để tránh xung đột layout bên trong ô nhập liệu
        btnCalendar.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        
        // Sự kiện click mở bảng lịch
        btnCalendar.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(field);
            DatePickerDialog picker = new DatePickerDialog(owner, field.getText());
            picker.setVisible(true);
            if (picker.isConfirmed()) {
                field.setText(picker.getSelectedDateTimeString());
            }
        });
        
        field.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnCalendar.doClick();
            }
        });

        field.putClientProperty("JTextField.trailingComponent", btnCalendar);
    }

    private void themKhuyenMai() {
        String[] labels = {"Mã KM", "Tên KM", "Giá trị (%)", "Loại KM", "Ngày BĐ (dd/MM/yyyy HH:mm)", "Ngày KT (dd/MM/yyyy HH:mm)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) fields[i] = new JTextField();

        if (controller != null) {
            fields[0].setText(controller.phatSinhMaTuDong()); 
        }
        fields[0].setEditable(false); 
        fields[0].setBackground(new Color(240, 240, 240)); 

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime hienTai = LocalDateTime.now();
        fields[4].setText(hienTai.format(formatter));
        fields[5].setText(hienTai.plusDays(7).format(formatter));

        // Gọi hàm cấu hình nhúng nút lịch vào ô số 4 và 5
        setupCalendarButton(fields[4]);
        setupCalendarButton(fields[5]);

        JButton btnCancel = new JButton("Hủy");
        JButton btnSave = new JButton("Lưu");

        JDialog dialog = component.createDinamicForm("Thêm Khuyến Mãi", "Nhập Thông Tin", 
                "Thiết lập chương trình ưu đãi mới", labels, fields, new JButton[]{btnCancel, btnSave});

        btnCancel.addActionListener(e -> dialog.dispose());
        
        btnSave.addActionListener(e -> {
            try {
                String ma = fields[0].getText().trim();
                String ten = fields[1].getText().trim();
                
                if(fields[4].getText().isEmpty() || fields[5].getText().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!");
                    return;
                }
                
                double giaTriNhap = Double.parseDouble(fields[2].getText().trim()); 
                String loaiNhap = fields[3].getText().trim(); 

                LocalDateTime bd = LocalDateTime.parse(fields[4].getText().trim(), formatter);
                LocalDateTime kt = LocalDateTime.parse(fields[5].getText().trim(), formatter);
                
                if (bd.isAfter(kt)) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi: Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                    return;
                }

                KhuyenMai km = new KhuyenMai(ma, ten, loaiNhap, giaTriNhap, bd, kt, true);

                if (controller != null && controller.themKhuyenMai(km)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
                    controller.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi lưu vào CSDL!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: Ô Giá trị phải nhập số!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    private void xoaKhuyenMai() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chương trình để xóa!");
            return;
        }

        String maKM = tableModel.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn xóa mã khuyến mãi: " + maKM + " không?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller != null && controller.xoaKhuyenMai(maKM)) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công mã: " + maKM);
                controller.loadDataToTable(); 
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
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

        // Đồng bộ nhúng nút chọn lịch vào form sửa thông tin luôn
        setupCalendarButton(fields[4]);
        setupCalendarButton(fields[5]);

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

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime bd = LocalDateTime.parse(fields[4].getText().trim(), formatter);
                LocalDateTime kt = LocalDateTime.parse(fields[5].getText().trim(), formatter);

                if (bd.isAfter(kt)) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi: Ngày bắt đầu không được lớn hơn ngày kết thúc!");
                    return;
                }

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

// ==========================================
// THÀNH PHẦN ĐƯỢC TÁCH BIỆT: BẢNG CHỌN NGÀY GIỜ CHUYÊN BIỆT
// ==========================================
class DatePickerDialog extends JDialog {
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private boolean confirmed = false;
    private LocalDateTime resultDateTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DatePickerDialog(Window owner, String initialValue) {
        super(owner, "Chọn Thời Gian", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(10, 10));
        setSize(new Dimension(360, 160));
        setLocationRelativeTo(owner);
        setResizable(false);
        
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.getCalendarButton().setCursor(new Cursor(Cursor.HAND_CURSOR));
        dateChooser.setPreferredSize(new Dimension(140, 30));
        
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setPreferredSize(new Dimension(75, 30));

        if (initialValue != null && !initialValue.trim().isEmpty()) {
            try {
                LocalDateTime existingLdt = LocalDateTime.parse(initialValue.trim(), formatter);
                Date existingDate = Date.from(existingLdt.atZone(ZoneId.systemDefault()).toInstant());
                dateChooser.setDate(existingDate);
                timeSpinner.setValue(existingDate);
            } catch (Exception e) {
                dateChooser.setDate(new Date());
            }
        } else {
            dateChooser.setDate(new Date());
        }

        gbc.gridx = 0; gbc.gridy = 0;
        mainContent.add(new JLabel("Chọn Ngày:"), gbc);
        gbc.gridx = 1;
        mainContent.add(dateChooser, gbc);
        
        gbc.gridx = 2;
        mainContent.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3;
        mainContent.add(timeSpinner, gbc);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionRow.setBackground(new Color(245, 245, 245));
        actionRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        JButton btnOk = new JButton("Xác nhận");
        JButton btnCancel = new JButton("Đóng");
        
        btnOk.putClientProperty("FlatLaf.style", "background:#22c55e; foreground:#ffffff; arc:8; focusWidth:0;");
        btnCancel.putClientProperty("FlatLaf.style", "arc:8; focusWidth:0;");

        btnOk.addActionListener(e -> {
            if (dateChooser.getDate() != null) {
                LocalDate datePart = dateChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                    
                Date timeValue = (Date) timeSpinner.getValue();
                java.time.LocalTime timePart = timeValue.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalTime();
                
                resultDateTime = datePart.atTime(timePart);
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        actionRow.add(btnCancel);
        actionRow.add(btnOk);

        add(mainContent, BorderLayout.CENTER);
        add(actionRow, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedDateTimeString() {
        return resultDateTime != null ? resultDateTime.format(formatter) : "";
    }
}