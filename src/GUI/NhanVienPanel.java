package GUI;

import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import Controller.NhanVienController;
import Entity.NhanVien;

public class NhanVienPanel extends JPanel {
    private NhanVienController controller;
    private Component component = new Component(); // Sử dụng class Component dùng chung
    
    // Quản lý các số thống kê nhân viên
    private JLabel lblDangLamViec;
    private JLabel lblNghieViec;
    private JLabel lblTongNhanVien;

    // 🔥 Đã loại bỏ lựa chọn "Xóa nhân viên"
    private String[] nhanVienOptions = {
        "Thêm nhân viên",
        "Sửa thông tin"
    };

    private JTable table;
    private DefaultTableModel tableModel;

    public void setController(NhanVienController controller) {
        this.controller = controller;
    }

    // Tái sử dụng style Button
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

    // Tái sử dụng style Card thống kê cho Nhân Viên
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

        if (title.equals("Đang làm việc")) {
            lblDangLamViec = valueLabel;
        } else if (title.equals("Nghỉ việc")) {
            lblNghieViec = valueLabel;
        } else if (title.equals("Tổng nhân viên")) {
            lblTongNhanVien = valueLabel;
        }

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        card.putClientProperty("FlatLaf.style", "arc:10; border:10,10,10,10; background:#FFFFFF");
        return card;
    }

    public NhanVienPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- HEADER SECTION ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250));

        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setOpaque(false);
        
        JLabel title = new JLabel("Quản lý nhân viên hệ thống");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel sub = new JLabel("Tạo, phân quyền và điều hành hồ sơ nhân viên nghiệp vụ đường sắt");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        
        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);

        // Grid 1 hàng 3 cột cho gọn gàng và cân đối giao diện nhân viên
        JPanel headerR = new JPanel(new GridLayout(1, 3, 15, 0));
        headerR.setOpaque(false);
        headerR.add(createCardstatistical("img/expired.png", "Nghỉ việc", "00"));      
        headerR.add(createCardstatistical("img/valid.png", "Đang làm việc", "00"));
        headerR.add(createCardstatistical("img/equal.png", "Tổng nhân viên", "00"));

        // --- ACTION PANEL (Search & Excel) ---
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(350, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập mã NV hoặc số điện thoại...");
        JButton btnSearch = createButtonExcel("Tìm Kiếm");
        
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(createButtonExcel("Xuất file excel"));
        rightPanel.add(createButtonExcel("Nhập file excel"));

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

        String[] columns = {"Mã NV", "Tên nhân viên", "Chức vụ", "Số điện thoại", "Trạng thái"};
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
        
        // Sự kiện tìm kiếm nhân viên
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (controller != null) {
                controller.timKiemNhanVien(keyword);
            }
        });
    }

    // Đổ dữ liệu Nhân viên lên JTable
    public void setData(List<NhanVien> list) {
        tableModel.setRowCount(0);
        for (NhanVien nv : list) {
            String trangThaiHienThi = nv.isTrangThai() ? "Đang làm việc" : "Nghỉ việc";

            tableModel.addRow(new Object[]{
                nv.getMaNV(),               
                nv.getTenNV(),              
                nv.getChucVu(),             
                nv.getSoDienThoai(),              
                trangThaiHienThi
            });
        }
    }

    // Cập nhật số lượng lên các Card thống kê
    public void capNhatThongKeCoDinh(List<NhanVien> allList) {
        int dangLamViec = 0;
        int nghieViec = 0;
        int tongNhanVien = allList.size();
        
        for (NhanVien nv : allList) {
            if (nv.isTrangThai()) {
                dangLamViec++;
            } else {
                nghieViec++;
            }
        }

        if (lblNghieViec != null) lblNghieViec.setText(String.format("%02d", nghieViec));
        if (lblDangLamViec != null) lblDangLamViec.setText(String.format("%02d", dangLamViec));
        if (lblTongNhanVien != null) lblTongNhanVien.setText(String.format("%02d", tongNhanVien));
    }

    // Menu chuột phải / Menu Option
    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        for (String option : nhanVienOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = it.getText();
                switch (text) {
                    case "Thêm nhân viên": themNhanVien(); break;
                    case "Sửa thông tin": suaNhanVien(); break;
                }
            });
            submenu.add(it);
        }
        return submenu;
    }

    // 🔥 ĐÃ ĐỒNG BỘ: Tự phát sinh mã tự động và khóa Edit ô Mã NV
    private void themNhanVien() {
        if (controller == null) {
            JOptionPane.showMessageDialog(this, "Hệ thống chưa kết nối dữ liệu bộ điều khiển!");
            return;
        }

        String[] labels = {"Mã NV", "Tên NV", "Chức vụ", "Số điện thoại", "Trạng thái (1:Đi làm, 0:Nghỉ)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) fields[i] = new JTextField();

        // Tự phát sinh mã thông qua Controller và đưa vào textfield
        String maTuSinh = controller.phatSinhMaTuDong();
        fields[0].setText(maTuSinh);
        fields[0].setEditable(false); // Khóa lại không cho chỉnh sửa mã
        fields[0].setBackground(new Color(240, 240, 240));

        // Mặc định ô trạng thái nhập là 1 (Đang đi làm) khi thêm mới
        fields[4].setText("1");

        JButton btnCancel = new JButton("Hủy");
        JButton btnSave = new JButton("Lưu");

        JDialog dialog = component.createDinamicForm("Thêm Nhân Viên", "Nhập Thông Tin", 
                "Thiết lập hồ sơ nhân viên mới vào hệ thống", labels, fields, new JButton[]{btnCancel, btnSave});

        btnCancel.addActionListener(e -> dialog.dispose());
        
        btnSave.addActionListener(e -> {
            try {
                String ma = fields[0].getText().trim();
                String ten = fields[1].getText().trim();
                String chucVu = fields[2].getText().trim();
                String sdt = fields[3].getText().trim();
                boolean trangThai = fields[4].getText().trim().equals("1");

                if (ten.isEmpty() || chucVu.isEmpty() || sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                NhanVien nv = new NhanVien(ma, ten, chucVu, sdt, trangThai);

                if (controller.themNhanVien(nv)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!");
                    controller.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm vào CSDL! Vui lòng thử lại.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    private void suaNhanVien() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
            return;
        }

        String[] labels = {"Mã NV (Không sửa)", "Tên NV", "Chức vụ", "Số điện thoại", "Trạng thái (1:Đi làm, 0:Nghỉ)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
            if (i < 4) {
                fields[i].setText(tableModel.getValueAt(row, i).toString());
            }
        }
        
        // Đổ trạng thái hiện tại từ text hiển thị sang số "1" hoặc "0" để sửa
        String currentStatusText = tableModel.getValueAt(row, 4).toString();
        fields[4].setText(currentStatusText.equals("Đang làm việc") ? "1" : "0");
        
        fields[0].setEditable(false);
        fields[0].setBackground(new Color(240, 240, 240));

        JButton btnCancel = new JButton("Hủy");
        JButton btnUpdate = new JButton("Cập nhật");

        JDialog dialog = component.createDinamicForm("Sửa Nhân Viên", "Chỉnh Sửa Thông Tin", 
                "Cập nhật lại hồ sơ nhân sự (Đặt về 0 để thiết lập nghỉ việc)", labels, fields, new JButton[]{btnCancel, btnUpdate});

        btnCancel.addActionListener(e -> dialog.dispose());

        btnUpdate.addActionListener(e -> {
            try {
                String ma = fields[0].getText().trim();
                String ten = fields[1].getText().trim();
                String chucVu = fields[2].getText().trim();
                String sdt = fields[3].getText().trim();
                boolean trangThai = fields[4].getText().trim().equals("1");

                if (ten.isEmpty() || chucVu.isEmpty() || sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Thông tin không được bỏ trống!");
                    return;
                }

                NhanVien nv = new NhanVien(ma, ten, chucVu, sdt, trangThai);

                if (controller != null && controller.updateNhanVien(nv)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật hồ sơ thành công!");
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