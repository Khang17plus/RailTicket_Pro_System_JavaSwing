package GUI;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import Entity.VeTau; // Import Entity VeTau

public class VeTauPanel extends JPanel {
    
    private Component component = new Component();
    
    private String[] veTauOptions = {
        "Thêm vé tàu",
        "Xóa vé tàu",
        "Sửa thông tin", 
        "Tra cứu vé tàu"
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

    public VeTauPanel() {
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

        // Cập nhật thẻ thống kê cho Vé Tàu
        JPanel cardTongVe = createCardstatistical("img/user2.png", "Tổng số vé ", 1200);
        JPanel cardVeBan = createCardstatistical("img/user2.png", "Vé đã bán  ", 850);
        JPanel cardVeTrong = createCardstatistical("img/user2.png", "Vé khả dụng ", 350);

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

        headerR.add(cardVeTrong);
        headerR.add(cardVeBan);
        headerR.add(cardTongVe);

        JLabel title = new JLabel("Quản lý vé tàu");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Quản lý danh sách vé, chỗ ngồi và trạng thái phát hành trên hệ thống Metro");
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

        // Format Cột cho Vé Tàu
        String[] columns = {"Mã Vé", "Mã Chuyến", "Mã Ghế", "Giá Gốc", "Trạng Thái"};
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

    public void setData(List<VeTau> list) {
        tableModel.setRowCount(0);
        for (VeTau vt : list) {
            tableModel.addRow(new Object[] {
                vt.getMaVe(), vt.getMaChuyen(), vt.getMaGhe(),
                vt.getGiaGoc(), vt.getTrangThai()
            });
        }
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        
        for (String option : veTauOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = ((JMenuItem) e.getSource()).getText();
                switch (text) {
                    case "Thêm vé tàu":
                        themVeTau();
                        break;
                    case "Xóa vé tàu":
                        xoaVeTau();
                        break;
                    case "Sửa thông tin":
                        suaVeTau();
                        break;
                    case "Tra cứu vé tàu":
                        traCuuVeTau();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Chọn: " + text);
                }
            });
            submenu.add(it);
        }
        return submenu;
    }

    // Thêm vé tàu
    private void themVeTau() {
        String[] labels = {"Mã Vé", "Mã Chuyến", "Mã Ghế", "Giá Gốc", "Trạng Thái (Đã bán/Trống)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnSave = new JButton("Lưu Vé Tàu");

        JDialog dialog = component.createDinamicForm(
            "Thêm Vé Tàu Mới", 
            "Nhập Thông Tin Vé", 
            "Vui lòng điền đầy đủ dữ liệu để phát hành vé", 
            labels, fields, new JButton[]{btnCancel, btnSave}
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            String maVe = fields[0].getText().trim();
            String maChuyen = fields[1].getText().trim();
            String maGhe = fields[2].getText().trim();
            String giaGoc = fields[3].getText().trim();
            String trangThai = fields[4].getText().trim();

            if (maVe.isEmpty() || maChuyen.isEmpty() || maGhe.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mã vé, mã chuyến và mã ghế không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mock Data - Thêm thẳng vào bảng không cần DB
            tableModel.addRow(new Object[]{maVe, maChuyen, maGhe, giaGoc, trangThai});
            JOptionPane.showMessageDialog(dialog, "Thêm vé tàu thành công!");
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // Xóa vé tàu
    private void xoaVeTau() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vé tàu cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa vé này khỏi hệ thống?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa vé thành công!");
        }
    }

    // Sửa thông tin vé tàu
    private void suaVeTau() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vé tàu cần sửa!");
            return;
        }

        // Lấy dữ liệu cũ
        String maCu = tableModel.getValueAt(selectedRow, 0).toString();
        String chuyenCu = tableModel.getValueAt(selectedRow, 1).toString();
        String gheCu = tableModel.getValueAt(selectedRow, 2).toString();
        String giaCu = tableModel.getValueAt(selectedRow, 3).toString();
        String trangThaiCu = tableModel.getValueAt(selectedRow, 4).toString();

        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa thông tin vé");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());

        JLabel lbMaVe = new JLabel("Mã Vé:");
        JTextField txtMaVe = new JTextField(maCu, 20);
        txtMaVe.setEditable(false);

        JLabel lbChuyen = new JLabel("Mã Chuyến:");
        JTextField txtChuyen = new JTextField(chuyenCu, 20);

        JLabel lbGhe = new JLabel("Mã Ghế:");
        JTextField txtGhe = new JTextField(gheCu, 20);

        JLabel lbGia = new JLabel("Giá Gốc:");
        JTextField txtGia = new JTextField(giaCu, 20);

        JLabel lbTrangThai = new JLabel("Trạng Thái:");
        JTextField txtTrangThai = new JTextField(trangThaiCu, 20);

        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnCancel = new JButton("Hủy");

        dialog.add(lbMaVe);
        dialog.add(txtMaVe);
        dialog.add(lbChuyen);
        dialog.add(txtChuyen);
        dialog.add(lbGhe);
        dialog.add(txtGhe);
        dialog.add(lbGia);
        dialog.add(txtGia);
        dialog.add(lbTrangThai);
        dialog.add(txtTrangThai);
        dialog.add(btnUpdate);
        dialog.add(btnCancel);

        btnUpdate.addActionListener(ev -> {
            tableModel.setValueAt(txtChuyen.getText(), selectedRow, 1);
            tableModel.setValueAt(txtGhe.getText(), selectedRow, 2);
            tableModel.setValueAt(txtGia.getText(), selectedRow, 3);
            tableModel.setValueAt(txtTrangThai.getText(), selectedRow, 4);

            JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
            dialog.dispose();
        });

        btnCancel.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Tra cứu vé tàu
    private void traCuuVeTau() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Mã Vé hoặc Mã Chuyến cần tìm:");
        JOptionPane.showMessageDialog(table, "Đã ghi nhận từ khóa: " + keyword + " (Chưa kết nối DB)");
    }
}