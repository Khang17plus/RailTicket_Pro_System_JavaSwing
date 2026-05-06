package GUI;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Entity.HoaDon; // Import Entity HoaDon

public class HoaDonPanel extends JPanel {
    
    private Component component = new Component();
    
    private String[] hoaDonOptions = {
        "Thêm hóa đơn",
        "Xóa hóa đơn",
        "Sửa thông tin", 
        "Tra cứu hóa đơn"
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

    public HoaDonPanel() {
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

        // Cập nhật card thống kê cho Hóa Đơn
        JPanel cardTongHD = createCardstatistical("img/user2.png", "Tổng số hóa đơn ", 1542);
        JPanel cardHDMoi = createCardstatistical("img/user2.png", "Hóa đơn hôm nay  ", 24);
        JPanel cardDoanhThu = createCardstatistical("img/user2.png", "Doanh thu (Triệu VNĐ) ", 320);

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

        headerR.add(cardDoanhThu);
        headerR.add(cardHDMoi);
        headerR.add(cardTongHD);

        JLabel title = new JLabel("Quản lý hóa đơn");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Quản lý thông tin giao dịch, thanh toán và doanh thu hệ thống");
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

        // Format Cột cho Hóa Đơn
        String[] columns = {"Mã HĐ", "Mã KH", "Mã NV", "Ngày Lập", "Tổng Hàng", "Thuế", "Giảm Giá", "Tổng TT", "PT Thanh Toán"};
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

    public void setData(List<HoaDon> list) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (HoaDon hd : list) {
            String ngayLapStr = (hd.getNgayLap() != null) ? hd.getNgayLap().format(formatter) : "";
            tableModel.addRow(new Object[] {
                hd.getMaHoaDon(), hd.getMaKH(), hd.getMaNV(),
                ngayLapStr, hd.getTongTienHang(), hd.getTongThue(), 
                hd.getTongGiamGia(), hd.getTongThanhToan(), hd.getPhuongThucThanhToan()
            });
        }
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        
        for (String option : hoaDonOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = ((JMenuItem) e.getSource()).getText();
                switch (text) {
                    case "Thêm hóa đơn":
                        themHoaDon();
                        break;
                    case "Xóa hóa đơn":
                        xoaHoaDon();
                        break;
                    case "Sửa thông tin":
                        suaHoaDon();
                        break;
                    case "Tra cứu hóa đơn":
                        traCuuHoaDon();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Chọn: " + text);
                }
            });
            submenu.add(it);
        }
        return submenu;
    }

    // Thêm hóa đơn
    private void themHoaDon() {
        // Không đưa "Ngày Lập" vào form vì sẽ lấy thời gian hệ thống tự động
        String[] labels = {
            "Mã Hóa Đơn", "Mã Khách Hàng", "Mã Nhân Viên", 
            "Tổng Tiền Hàng", "Tổng Thuế", "Tổng Giảm Giá", "Tổng Thanh Toán", "Phương Thức TT"
        };
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnSave = new JButton("Lưu Hóa Đơn");

        JDialog dialog = component.createDinamicForm(
            "Thêm Hóa Đơn Mới", 
            "Nhập Thông Tin Giao Dịch", 
            "Vui lòng điền đầy đủ dữ liệu thanh toán", 
            labels, fields, new JButton[]{btnCancel, btnSave}
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            String maHD = fields[0].getText().trim();
            String maKH = fields[1].getText().trim();
            String maNV = fields[2].getText().trim();
            String tienHang = fields[3].getText().trim();
            String thue = fields[4].getText().trim();
            String giamGia = fields[5].getText().trim();
            String tongTT = fields[6].getText().trim();
            String ptThanhToan = fields[7].getText().trim();

            if (maHD.isEmpty() || maKH.isEmpty() || maNV.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mã HĐ, Mã KH và Mã NV không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy thời gian hiện tại làm Ngày Lập
            String ngayLap = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            // Mock Data - Thêm thẳng vào bảng không cần DB
            tableModel.addRow(new Object[]{maHD, maKH, maNV, ngayLap, tienHang, thue, giamGia, tongTT, ptThanhToan});
            JOptionPane.showMessageDialog(dialog, "Thêm hóa đơn thành công!");
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // Xóa hóa đơn
    private void xoaHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa hóa đơn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    // Sửa thông tin hóa đơn
    private void suaHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần sửa!");
            return;
        }

        // Lấy dữ liệu cũ từ Table
        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        String maKH = tableModel.getValueAt(selectedRow, 1).toString();
        String maNV = tableModel.getValueAt(selectedRow, 2).toString();
        String tienHang = tableModel.getValueAt(selectedRow, 4).toString();
        String thue = tableModel.getValueAt(selectedRow, 5).toString();
        String giamGia = tableModel.getValueAt(selectedRow, 6).toString();
        String tongTT = tableModel.getValueAt(selectedRow, 7).toString();
        String ptThanhToan = tableModel.getValueAt(selectedRow, 8).toString();

        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa thông tin hóa đơn");
        dialog.setSize(420, 500); // Tăng kích thước vì nhiều field
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());

        JLabel lbMaHD = new JLabel("Mã HĐ:");
        JTextField txtMaHD = new JTextField(maHD, 25);
        txtMaHD.setEditable(false);

        JLabel lbMaKH = new JLabel("Mã KH:");
        JTextField txtMaKH = new JTextField(maKH, 25);

        JLabel lbMaNV = new JLabel("Mã NV:");
        JTextField txtMaNV = new JTextField(maNV, 25);

        JLabel lbTienHang = new JLabel("Tiền hàng:");
        JTextField txtTienHang = new JTextField(tienHang, 25);

        JLabel lbThue = new JLabel("Thuế:");
        JTextField txtThue = new JTextField(thue, 25);

        JLabel lbGiamGia = new JLabel("Giảm giá:");
        JTextField txtGiamGia = new JTextField(giamGia, 25);
        
        JLabel lbTongTT = new JLabel("Tổng TT:");
        JTextField txtTongTT = new JTextField(tongTT, 25);
        
        JLabel lbPTThanhToan = new JLabel("Phương thức TT:");
        JTextField txtPTThanhToan = new JTextField(ptThanhToan, 25);

        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnCancel = new JButton("Hủy");

        dialog.add(lbMaHD);
        dialog.add(txtMaHD);
        dialog.add(lbMaKH);
        dialog.add(txtMaKH);
        dialog.add(lbMaNV);
        dialog.add(txtMaNV);
        dialog.add(lbTienHang);
        dialog.add(txtTienHang);
        dialog.add(lbThue);
        dialog.add(txtThue);
        dialog.add(lbGiamGia);
        dialog.add(txtGiamGia);
        dialog.add(lbTongTT);
        dialog.add(txtTongTT);
        dialog.add(lbPTThanhToan);
        dialog.add(txtPTThanhToan);
        
        dialog.add(btnUpdate);
        dialog.add(btnCancel);

        btnUpdate.addActionListener(ev -> {
            tableModel.setValueAt(txtMaKH.getText(), selectedRow, 1);
            tableModel.setValueAt(txtMaNV.getText(), selectedRow, 2);
            tableModel.setValueAt(txtTienHang.getText(), selectedRow, 4);
            tableModel.setValueAt(txtThue.getText(), selectedRow, 5);
            tableModel.setValueAt(txtGiamGia.getText(), selectedRow, 6);
            tableModel.setValueAt(txtTongTT.getText(), selectedRow, 7);
            tableModel.setValueAt(txtPTThanhToan.getText(), selectedRow, 8);

            JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
            dialog.dispose();
        });

        btnCancel.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Tra cứu hóa đơn
    private void traCuuHoaDon() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Mã Hóa Đơn hoặc Mã KH cần tìm:");
        JOptionPane.showMessageDialog(table, "Đã ghi nhận từ khóa: " + keyword + " (Chưa kết nối DB)");
    }
}