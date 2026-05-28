package GUI;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import Controller.TaiKhoanController; // Giả định package điều khiển của bạn
import Entity.TaiKhoan;            // Giả định class Entity của bạn

public class TaiKhoanPanel extends JPanel {
    private TaiKhoanController controller;
    private Component component = new Component(); // Form động giống KhachHang
    
    // Quản lý các nhãn số liệu thống kê thời gian thực
    private JLabel lblTongTK;
    private JLabel lblAdminCount; 
    private JLabel lblStaffCount;
    
    private String[] taiKhoanOptions = {
        "Cấp tài khoản mới",
        "Đổi mật khẩu / Quyền"
    };
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    public void setController(TaiKhoanController controller) {
        this.controller = controller;
    }
   
    public JButton createButtonExcel(String Cmt) {
        JButton btn = new JButton(Cmt);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 13;";
        if (Cmt.contains("Nhập") || Cmt.contains("Cấp")) {
            btn.setBackground(new Color(59, 130, 246)); // xanh dương
        } else if (Cmt.contains("Tìm")) {
            btn.setBackground(Color.gray); // nền xám
            btn.setForeground(Color.BLACK); // chữ đen
            btn.setPreferredSize(new Dimension(60, 36));
        } else {
            btn.setBackground(new Color(34, 197, 94)); // xanh lá
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
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
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
        
        // Gắn biến toàn cục dựa theo tiêu đề card tài khoản
        if (title.contains("Tổng tài khoản")) {
            lblTongTK = valueLabel;
        } else if (title.contains("Quản trị viên")) {
            lblAdminCount = valueLabel;
        } else if (title.contains("Nhân viên bán vé")) {
            lblStaffCount = valueLabel;
        }
        
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        card.putClientProperty("FlatLaf.style", "arc:10; border:10,10,10,10; background:#FFFFFF");
        
        return card;
    }
    
    public TaiKhoanPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); 

        // --- HEADER SECTION ---
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250)); 
        
        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel headerR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        
        // Thống kê phân hệ tài khoản
        JPanel cardStaff = createCardstatistical("img/user2.png", "Nhân viên bán vé ", "00");
        JPanel cardAdmin = createCardstatistical("img/user2.png", "Quản trị viên (ADMIN) ", "00");
        JPanel cardTK = createCardstatistical("img/user2.png", "Tổng tài khoản ", "00");
        
        JPanel actionPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(400, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập mã nhân viên hoặc vai trò...");
        JButton btnSearch = createButtonExcel("Tìm Kiếm");
        
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (controller != null) {
                if (!keyword.isEmpty()) {
                    controller.timKiemTaiKhoan(keyword); 
                } else {
                    controller.loadDataToTable(); 
                }
            }
        });
        
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        
        
        
        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.CENTER);
        
        headerR.add(cardStaff);
        headerR.add(cardAdmin);
        headerR.add(cardTK);
        
        JLabel title = new JLabel("Quản lý tài khoản hệ thống");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Phân quyền bảo mật và cấp tài khoản cho cán bộ công nhân viên đường sắt");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);
        
        header.add(headerL, BorderLayout.WEST);
        header.add(headerR, BorderLayout.EAST);
        header.add(actionPanel, BorderLayout.SOUTH);
        
        add(header, BorderLayout.NORTH);

        // --- MAIN TABLE SECTION ---
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

        // Khớp hoàn toàn với cấu trúc Table TaiKhoan trong database
        String[] columns = {"Mã Nhân Viên", "Mật Khẩu hệ thống", "Vai Trò"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40);
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

    public void setData(List<TaiKhoan> list) {
        tableModel.setRowCount(0);
        for (TaiKhoan tk : list) {
            tableModel.addRow(new Object[] {
                tk.getMaNV(),
                tk.getMatKhau(), // 🌟 ĐÃ SỬA: Lấy mật khẩu thực tế thay vì viết cứng "********"
                tk.getVaiTro()
            });
        }
    }
    
    // Đếm số lượng phân quyền trực tiếp từ Database
    public void updateThongKeCoDinh(List<TaiKhoan> allList) {
        int tongTK = allList.size();
        int adminCount = 0;
        int staffCount = 0;
        
        for (TaiKhoan tk : allList) {
            if ("ADMIN".equalsIgnoreCase(tk.getVaiTro())) {
                adminCount++;
            } else if ("STAFF".equalsIgnoreCase(tk.getVaiTro())) {
                staffCount++;
            }
        }
        
        if (lblTongTK != null) lblTongTK.setText(String.format("%02d", tongTK));
        if (lblAdminCount != null) lblAdminCount.setText(String.format("%02d", adminCount));
        if (lblStaffCount != null) lblStaffCount.setText(String.format("%02d", staffCount));
    }
   
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public List<JMenuItem> getMenuOption(){
        List<JMenuItem> submenu = new ArrayList<>();
        for (String option : taiKhoanOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = ((JMenuItem) e.getSource()).getText();
                switch (text) {
                    case "Cấp tài khoản mới":
                        capTaiKhoan();
                        break;
                    case "Đổi mật khẩu / Quyền":
                        suaTaiKhoan();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Chọn: " + text);
                }
            });
            submenu.add(it);
        }
        return submenu;
    }
    
    private void capTaiKhoan() {
        if (controller == null) {
            JOptionPane.showMessageDialog(this, "Hệ thống chưa kết nối bộ điều khiển!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] labels = {"Mã Nhân Viên (Chưa có TK)", "Mật Khẩu Ban Đầu", "Vai Trò (ADMIN/STAFF)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnSave = new JButton("Cấp Tài Khoản");

        JDialog dialog = component.createDinamicForm(
            "Cấp Tài Khoản Mới", 
            "Thông tin tài khoản công nhân viên", 
            "Lưu ý: Mã nhân viên nhập vào phải đã tồn tại trong danh mục Nhân Viên.", 
            labels, fields, new JButton[]{btnCancel, btnSave}
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            String maNV = fields[0].getText().trim();
            String matKhau = fields[1].getText().trim();
            String vaiTro = fields[2].getText().trim().toUpperCase();

            if (maNV.isEmpty() || matKhau.isEmpty() || vaiTro.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ các trường thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!"ADMIN".equals(vaiTro) && !"STAFF".equals(vaiTro)) {
                JOptionPane.showMessageDialog(dialog, "Vai trò bắt buộc phải là 'ADMIN' hoặc 'STAFF'!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaiKhoan tkNew = new TaiKhoan(maNV, matKhau, vaiTro);

            if (controller.themTaiKhoan(tkNew)) {
                JOptionPane.showMessageDialog(dialog, "Cấp tài khoản thành công!");
                controller.loadDataToTable(); 
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thất bại! Có thể mã NV sai hoặc đã có tài khoản rồi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
    
    private void suaTaiKhoan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản từ danh sách cần chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maNV = tableModel.getValueAt(selectedRow, 0).toString();
        String vaiTroCu = tableModel.getValueAt(selectedRow, 2).toString();
        
        String[] labels = {"Mật Khẩu Mới", "Vai Trò (ADMIN/STAFF)"};
        JTextField[] fields = new JTextField[labels.length];
        
        fields[0] = new JTextField(); // Để trống tránh lộ mật khẩu cũ
        fields[1] = new JTextField(vaiTroCu);

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnUpdate = new JButton("Cập Nhật");

        JDialog dialog = component.createDinamicForm(
            "Chỉnh Sửa Quyền & Mật Khẩu", "Mã Nhân Viên: " + maNV, 
            "Nếu giữ nguyên mật khẩu cũ, hãy để trống ô Mật khẩu mới.", 
            labels, fields, new JButton[]{btnCancel, btnUpdate}
        );
        
        btnCancel.addActionListener(ev -> dialog.dispose());

        btnUpdate.addActionListener(ev -> {
            String matKhauMoi = fields[0].getText().trim();
            String vaiTroMoi = fields[1].getText().trim().toUpperCase();

            if (vaiTroMoi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vai trò không được phép bỏ trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!"ADMIN".equals(vaiTroMoi) && !"STAFF".equals(vaiTroMoi)) {
                JOptionPane.showMessageDialog(dialog, "Vai trò bắt buộc phải định dạng: 'ADMIN' hoặc 'STAFF'!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TaiKhoan tkUpdate = new TaiKhoan(maNV, matKhauMoi, vaiTroMoi);

            if (controller != null && controller.capNhatTaiKhoan(tkUpdate)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật tài khoản hệ thống thành công!");
                controller.loadDataToTable(); 
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Gặp lỗi trong quá trình cập nhật cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }
}