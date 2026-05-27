package GUI;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;

import Controller.GaTauController;
import Entity.GaTau;

public class GaTauPanel extends JPanel {
    private GaTauController controller;
    private Component component = new Component();
    
    private String[] gaTauOptions = {
        "Thêm ga tàu",
        "Xóa ga tàu",
        "Sửa thông tin", 
        "Tra cứu ga tàu"
    };
    private JTable table;
    private DefaultTableModel tableModel;
    
    // 🔥 BIẾN THÊM MỚI: Quản lý các Label KPI toàn cục để cập nhật số liệu theo Database thật
    private JLabel lblTongGa;
    private JLabel lblHoatDong;
    private JLabel lblBaoTri;
    
    public void setController(GaTauController controller) {
        this.controller = controller;
    }
   
    public JButton createButtonExcel(String Cmt) {
        JButton btn = new JButton(Cmt);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 13;";
        if (Cmt.contains("Nhập")) {
            btn.setBackground(new Color(59,130,246)); // xanh dương
            btn.setForeground(Color.WHITE);
        } else if(Cmt.contains("Tìm"))  {
            btn.setBackground(Color.gray); // nền xám
            btn.setForeground(Color.BLACK); // chữ đen
            btn.setPreferredSize(new Dimension(80, 36));
        } else {
            btn.setBackground(new Color(34,197,94)); // xanh lá
            btn.setForeground(Color.WHITE);
        }
        btn.setPreferredSize(new Dimension(140, 36)); 
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", style + "margin:8,15,8,15");
        return btn;
    }
    
    // 🔥 SỬA ĐỔI: Nhận đối tượng JLabel từ ngoài truyền vào để thay đổi số liệu động
    public JPanel createCardstatistical(String IconURL, String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(15,0));
        ImageIcon icon = new ImageIcon(IconURL);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);
        
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        card.putClientProperty("FlatLaf.style",  "arc:10; border:10,10,10,10; background:#FFFFFF");
        return card;
    }
    
    public GaTauPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); 

        // 2. Phần Header
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250)); 
        
        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerL.setBackground(new Color(245, 247, 250));
        JPanel headerR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        headerR.setBackground(new Color(245, 247, 250));
        
        // 🔥 KHỞI TẠO ĐỘNG: Tạo các nhãn động với giá trị ban đầu là "0"
        lblBaoTri = new JLabel("0");
        lblHoatDong = new JLabel("0");
        lblTongGa = new JLabel("0");
        
        // Thống kê liên kết trực tiếp với các Label toàn cục
        JPanel cardBaoTri = createCardstatistical("img/user2.png", "Đang bảo trì ", lblBaoTri);
        JPanel cardHoatDong = createCardstatistical("img/user2.png", "Đang hoạt động ", lblHoatDong);
        JPanel cardTongGa = createCardstatistical("img/user2.png", "Tổng số nhà ga ", lblTongGa);
        
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(new Color(245, 247, 250));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        searchPanel.setBackground(new Color(245, 247, 250));
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(400, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập tên ga hoặc địa chỉ...");
        JButton btnSearch = createButtonExcel("Tìm Kiếm");
        
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (controller != null) {
                if (!keyword.isEmpty()) {
                    controller.timKiemGaTau(keyword);
                } else {
                    controller.loadDataToTable();
                }
            }
        });
        
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        rightPanel.setBackground(new Color(245, 247, 250));

        JButton imports= createButtonExcel("Nhập file excel");
        JButton export = createButtonExcel("Xuất file excel");
        imports.addActionListener(e->importExcel());
        export.addActionListener(e->exportExcel());
        rightPanel.add(export);
        rightPanel.add(imports);
        
        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.CENTER);
        
        headerR.add(cardBaoTri);
        headerR.add(cardHoatDong);
        headerR.add(cardTongGa);
        
        JLabel title = new JLabel("Quản lý ga tàu");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Quản lý thông tin và trạng thái các nhà ga thuộc hệ thống");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);
        
        header.add(headerL,BorderLayout.WEST);
        header.add(headerR,BorderLayout.EAST);
        header.add(actionPanel,BorderLayout.SOUTH);
        
        add(header, BorderLayout.NORTH);

        // 3. Phần Main chứa Table 
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(new Color(245, 247, 250));
        main.putClientProperty("FlatLaf.style", "arc:20; border:10,10,10,10");

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Format Cột cho Ga Tàu
        String[] columns = {"Mã Ga", "Tên Ga", "Địa Chỉ", "Số Điện Thoại", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35); 
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setGridColor(new Color(235, 235, 235)); 
        table.setShowVerticalLines(false); 
        table.setSelectionBackground(new Color(232, 240, 254)); 
        
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 40));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); 
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 

        tableCard.add(scrollPane, BorderLayout.CENTER);
        tableCard.putClientProperty("FlatLaf.style", "arc:20; border:12,12,12,12; background:#FFFFFF");
        
        main.add(tableCard, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    // 🔥 SỬA ĐỔI CHÍNH: Tính toán số lượng thực tế từ Database đẩy lên
    public void setData(List<GaTau> list) {
        tableModel.setRowCount(0);
        
        int tongGa = 0;
        int dangHoatDong = 0;
        int dangBaoTri = 0;
        
        for(GaTau ga: list) {
            tableModel.addRow(new Object[] {
                ga.getMaGa(), ga.getTenGa(), ga.getDiaChi(),
                ga.getSoDienThoai(), ga.getTrangThai()
            });
            
            // Đếm số lượng dựa trên trạng thái thực tế trong CSDL
            tongGa++;
            if (ga.getTrangThai() != null) {
                String status = ga.getTrangThai().trim();
                if (status.equalsIgnoreCase("Đang hoạt động")) {
                    dangHoatDong++;
                } else if (status.equalsIgnoreCase("Đang bảo trì") || status.equalsIgnoreCase("Bảo trì tạm thời")) {
                    dangBaoTri++;
                }
            }
        }
        
        // 🔥 Cập nhật con số thực tế lên giao diện Cards
        lblTongGa.setText(String.valueOf(tongGa));
        lblHoatDong.setText(String.valueOf(dangHoatDong));
        lblBaoTri.setText(String.valueOf(dangBaoTri));
    }
   
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public List<JMenuItem> getMenuOption(){
        List<JMenuItem> submenu = new ArrayList<>();
        
        for (String option : gaTauOptions) {
            JMenuItem it = new JMenuItem(option);
             it.addActionListener(e -> {
                 String text = ((JMenuItem) e.getSource()).getText();
                 switch (text) {
                     case "Thêm ga tàu":
                         themGaTau();
                         break;
                     case "Xóa ga tàu":
                         xoaGaTau();
                         break;
                     case "Sửa thông tin":
                         suaGaTau();
                         break;
                     case "Tra cứu ga tàu":
                         traCuuGaTau();
                         break;
                     default:
                         JOptionPane.showMessageDialog(null, "Chọn: " + text);
                 }
             });
            submenu.add(it);
        }
        return submenu;
    }
    
    private void exportExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("DanhSachGa.xlsx"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files (.xlsx)", "xlsx"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".xlsx")) path += ".xlsx";
            List<GaTau> currentData = getDataFromTable();
            boolean success = controller.exportToExcel(path, currentData);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xuất file thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Xuất file thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public List<GaTau> getDataFromTable() {
        List<GaTau> list = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            GaTau ga = new GaTau();
            ga.setMaGa((String) model.getValueAt(i, 0));
            ga.setTenGa((String) model.getValueAt(i, 1));
            ga.setDiaChi((String) model.getValueAt(i, 2));
            ga.setSoDienThoai((String) model.getValueAt(i, 3));
            ga.setTrangThai((String) model.getValueAt(i, 4));
            list.add(ga);
        }
        return list;
    }

    private void themGaTau() {
        String[] labels = {"Mã Ga", "Tên Ga", "Địa Chỉ", "Số Điện Thoại", "Trạng Thái"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        fields[0].setEditable(false);
        fields[0].setBackground(new Color(240, 240, 240)); 
        if (controller != null) {
            fields[0].setText(controller.generateNextMaGa()); 
        } else {
            fields[0].setText("GA001"); 
        }

        fields[4].setEditable(false);
        fields[4].setBackground(new Color(240, 240, 240));
        fields[4].setText("Đang hoạt động");
        
        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnSave = new JButton("Lưu Ga Tàu");

        JDialog dialog = component.createDinamicForm(
            "Thêm Ga Tàu Mới", 
            "Nhập Thông Tin", 
            "Vui lòng điền đầy đủ các thông tin bên dưới", 
            labels, fields, new JButton[]{btnCancel, btnSave}
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            String maGa = fields[0].getText().trim();
            String tenGa = fields[1].getText().trim();
            String diaChi = fields[2].getText().trim();
            String sdt = fields[3].getText().trim();
            String trangThai = fields[4].getText().trim();

            if (tenGa.isEmpty() || diaChi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đủ Tên và Địa chỉ ga!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            GaTau gaNew = new GaTau(maGa, tenGa, diaChi, sdt, trangThai);

            if (controller != null) {
                boolean isSuccess = controller.themGaTau(gaNew); 
                
                if (isSuccess) {
                    JOptionPane.showMessageDialog(dialog, "Thêm ga tàu thành công!");
                    // Kích hoạt load lại dữ liệu để các thẻ Card tự cập nhật tăng số lượng
                    controller.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại! Lỗi hệ thống hoặc trùng mã.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Chưa khởi tạo Controller!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void xoaGaTau() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ga tàu cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maGa = tableModel.getValueAt(selectedRow, 0).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa ga tàu " + maGa + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller != null && controller.xoaGaTau(maGa)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                controller.loadDataToTable(); // Refresh để cập nhật lại Card giảm số lượng
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Ga tàu có thể đang có lịch trình chuyến tàu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importExcel() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Excel files (.xls, .xlsx)", "xls", "xlsx"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            String result = controller.importExcel(path);
            JOptionPane.showMessageDialog(this, result);
            controller.loadDataToTable(); 
        }
    }

    private void suaGaTau() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ga tàu cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maCu = tableModel.getValueAt(selectedRow, 0).toString();
        String tenCu = tableModel.getValueAt(selectedRow, 1).toString();
        String diaChiCu = tableModel.getValueAt(selectedRow, 2).toString();
        String sdtCu = tableModel.getValueAt(selectedRow, 3).toString();
        String trangThaiCu = tableModel.getValueAt(selectedRow, 4).toString();
        
        String[] labels = {"Tên Ga", "Địa Chỉ", "Số Điện Thoại", "Trạng Thái"};
        JTextField[] fields = new JTextField[labels.length];
        
        fields[0] = new JTextField(tenCu);
        fields[1] = new JTextField(diaChiCu);
        fields[2] = new JTextField(sdtCu);
        fields[3] = new JTextField(trangThaiCu);

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnUpdate = new JButton("Cập Nhật");

        JDialog dialog = component.createDinamicForm(
            "Sửa Thông Tin Ga", "Mã Ga: " + maCu, 
            "Cập nhật thông tin ga tàu", 
            labels, fields, new JButton[]{btnCancel, btnUpdate}
        );
        
        btnCancel.addActionListener(ev -> dialog.dispose());

        btnUpdate.addActionListener(ev -> {
            String tenMoi = fields[0].getText().trim();
            String diaChiMoi = fields[1].getText().trim();
            String sdtMoi = fields[2].getText().trim();
            String trangThaiMoi = fields[3].getText().trim();

            if (tenMoi.isEmpty() || diaChiMoi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tên và Địa chỉ không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            GaTau gaUpdate = new GaTau(maCu, tenMoi, diaChiMoi, sdtMoi, trangThaiMoi);

            if (controller != null && controller.capNhatGaTau(gaUpdate)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                controller.loadDataToTable(); // Gọi lại để đồng bộ cả Card nếu trạng thái ga bị đổi
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }

    private void traCuuGaTau() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Tên hoặc Địa chỉ ga cần tìm:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (controller != null) {
                controller.timKiemGaTau(keyword.trim());
            }
        } else if (keyword != null && keyword.trim().isEmpty()) {
            if (controller != null) {
                controller.loadDataToTable();
            }
        }
    }
}