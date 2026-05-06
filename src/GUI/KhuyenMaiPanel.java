package GUI;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Entity.KhuyenMai; // Import Entity KhuyenMai

public class KhuyenMaiPanel extends JPanel {
    
    private Component component = new Component();
    
    private String[] khuyenMaiOptions = {
        "Thêm khuyến mãi",
        "Xóa khuyến mãi",
        "Sửa thông tin", 
        "Tra cứu khuyến mãi"
    };
    
    private JTable table;
    private DefaultTableModel tableModel;

    public JButton createButtonExcel(String Cmt) {
        JButton btn = new JButton(Cmt);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 13;";
        if (Cmt.contains("Nhập")) {
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

    public JPanel createCardstatistical(String IconURL, String title, int value) {
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

        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);

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

        // 2. Phần Header (Tiêu đề và Mô tả)
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250)); 

        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel headerR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));

        // Cập nhật card thống kê cho Khuyến Mãi
        JPanel cardTongKM = createCardstatistical("img/user2.png", "Tổng số chương trình ", 45);
        JPanel cardDangDienRa = createCardstatistical("img/user2.png", "Đang diễn ra  ", 12);
        JPanel cardSapHetHan = createCardstatistical("img/user2.png", "Sắp hết hạn ", 5);

        JPanel actionPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(400, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        JButton btnSearch = createButtonExcel("Tìm Kiếm");

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton imports = createButtonExcel("Nhập file excel");
        JButton export = createButtonExcel("Xuất file excel");

        rightPanel.add(export);
        rightPanel.add(imports);

        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.CENTER);

        headerR.add(cardSapHetHan);
        headerR.add(cardDangDienRa);
        headerR.add(cardTongKM);

        JLabel title = new JLabel("Quản lý khuyến mãi");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Quản lý các chương trình ưu đãi, giảm giá và chiến dịch marketing");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);

        header.add(headerL, BorderLayout.WEST);
        header.add(headerR, BorderLayout.EAST);
        header.add(actionPanel, BorderLayout.SOUTH);

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

        // Format Cột cho Khuyến Mãi
        String[] columns = {"Mã KM", "Tên Chương Trình", "Loại KM", "Giá Trị", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(35); 
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

    public void setData(List<KhuyenMai> list) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (KhuyenMai km : list) {
            String ngayBD = (km.getNgayBatDau() != null) ? km.getNgayBatDau().format(formatter) : "";
            String ngayKT = (km.getNgayKetThuc() != null) ? km.getNgayKetThuc().format(formatter) : "";
            String trangThaiStr = km.isTrangThai() ? "Đang áp dụng" : "Hết hạn/Hủy";
            
            tableModel.addRow(new Object[] {
                km.getMaKM(), km.getTenKM(), km.getLoaiKM(),
                km.getGiaTri(), ngayBD, ngayKT, trangThaiStr
            });
        }
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        
        for (String option : khuyenMaiOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = ((JMenuItem) e.getSource()).getText();
                switch (text) {
                    case "Thêm khuyến mãi":
                        themKhuyenMai();
                        break;
                    case "Xóa khuyến mãi":
                        xoaKhuyenMai();
                        break;
                    case "Sửa thông tin":
                        suaKhuyenMai();
                        break;
                    case "Tra cứu khuyến mãi":
                        traCuuKhuyenMai();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Chọn: " + text);
                }
            });
            submenu.add(it);
        }
        return submenu;
    }

    // Thêm khuyến mãi
    private void themKhuyenMai() {
        String[] labels = {
            "Mã KM", "Tên Chương Trình", "Loại KM (%, VNĐ)", 
            "Giá Trị", "Ngày Bắt Đầu (dd/MM/yyyy)", "Ngày Kết Thúc (dd/MM/yyyy)", "Trạng Thái (Đang áp dụng/Hết hạn)"
        };
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnSave = new JButton("Lưu Khuyến Mãi");

        JDialog dialog = component.createDinamicForm(
            "Thêm Khuyến Mới", 
            "Nhập Thông Tin Ưu Đãi", 
            "Vui lòng điền đầy đủ dữ liệu chương trình", 
            labels, fields, new JButton[]{btnCancel, btnSave}
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            String maKM = fields[0].getText().trim();
            String tenKM = fields[1].getText().trim();
            String loaiKM = fields[2].getText().trim();
            String giaTri = fields[3].getText().trim();
            String ngayBD = fields[4].getText().trim();
            String ngayKT = fields[5].getText().trim();
            String trangThai = fields[6].getText().trim();

            if (maKM.isEmpty() || tenKM.isEmpty() || giaTri.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mã KM, Tên và Giá trị không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mock Data - Thêm thẳng vào bảng không cần DB
            tableModel.addRow(new Object[]{maKM, tenKM, loaiKM, giaTri, ngayBD, ngayKT, trangThai});
            JOptionPane.showMessageDialog(dialog, "Thêm chương trình khuyến mãi thành công!");
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // Xóa khuyến mãi
    private void xoaKhuyenMai() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chương trình khuyến mãi cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    // Sửa thông tin khuyến mãi
    private void suaKhuyenMai() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần sửa!");
            return;
        }

        // Lấy dữ liệu cũ từ Table
        String maKM = tableModel.getValueAt(selectedRow, 0).toString();
        String tenKM = tableModel.getValueAt(selectedRow, 1).toString();
        String loaiKM = tableModel.getValueAt(selectedRow, 2).toString();
        String giaTri = tableModel.getValueAt(selectedRow, 3).toString();
        String ngayBD = tableModel.getValueAt(selectedRow, 4).toString();
        String ngayKT = tableModel.getValueAt(selectedRow, 5).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 6).toString();

        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa chương trình khuyến mãi");
        dialog.setSize(420, 480);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());

        JLabel lbMaKM = new JLabel("Mã KM:");
        JTextField txtMaKM = new JTextField(maKM, 25);
        txtMaKM.setEditable(false);

        JLabel lbTenKM = new JLabel("Tên CT:");
        JTextField txtTenKM = new JTextField(tenKM, 25);

        JLabel lbLoaiKM = new JLabel("Loại KM:");
        JTextField txtLoaiKM = new JTextField(loaiKM, 25);

        JLabel lbGiaTri = new JLabel("Giá Trị:");
        JTextField txtGiaTri = new JTextField(giaTri, 25);

        JLabel lbNgayBD = new JLabel("Ngày Bắt Đầu:");
        JTextField txtNgayBD = new JTextField(ngayBD, 25);

        JLabel lbNgayKT = new JLabel("Ngày Kết Thúc:");
        JTextField txtNgayKT = new JTextField(ngayKT, 25);
        
        JLabel lbTrangThai = new JLabel("Trạng Thái:");
        JTextField txtTrangThai = new JTextField(trangThai, 25);

        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnCancel = new JButton("Hủy");

        dialog.add(lbMaKM);
        dialog.add(txtMaKM);
        dialog.add(lbTenKM);
        dialog.add(txtTenKM);
        dialog.add(lbLoaiKM);
        dialog.add(txtLoaiKM);
        dialog.add(lbGiaTri);
        dialog.add(txtGiaTri);
        dialog.add(lbNgayBD);
        dialog.add(txtNgayBD);
        dialog.add(lbNgayKT);
        dialog.add(txtNgayKT);
        dialog.add(lbTrangThai);
        dialog.add(txtTrangThai);
        
        dialog.add(btnUpdate);
        dialog.add(btnCancel);

        btnUpdate.addActionListener(ev -> {
            tableModel.setValueAt(txtTenKM.getText(), selectedRow, 1);
            tableModel.setValueAt(txtLoaiKM.getText(), selectedRow, 2);
            tableModel.setValueAt(txtGiaTri.getText(), selectedRow, 3);
            tableModel.setValueAt(txtNgayBD.getText(), selectedRow, 4);
            tableModel.setValueAt(txtNgayKT.getText(), selectedRow, 5);
            tableModel.setValueAt(txtTrangThai.getText(), selectedRow, 6);

            JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
            dialog.dispose();
        });

        btnCancel.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Tra cứu khuyến mãi
    private void traCuuKhuyenMai() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Tên hoặc Mã KM cần tìm:");
        JOptionPane.showMessageDialog(table, "Đã ghi nhận từ khóa: " + keyword + " (Chưa kết nối DB)");
    }
}