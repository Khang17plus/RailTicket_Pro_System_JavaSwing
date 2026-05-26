package GUI;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import Entity.Tau; // Import Entity Tau

public class TauPanel extends JPanel {
    
    private Component component = new Component();
    
    private String[] tauOptions = {
        "Thêm tàu mới",
        "Xóa tàu",
        "Sửa thông tin", 
        "Tra cứu tàu"
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

    public TauPanel() {
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

        // Cập nhật card thống kê cho Tàu
        JPanel cardTongTau = createCardstatistical("img/user2.png", "Tổng số tàu ", 25);
        JPanel cardHoatDong = createCardstatistical("img/user2.png", "Đang hoạt động  ", 20);
        JPanel cardBaoTri = createCardstatistical("img/user2.png", "Đang bảo trì ", 5);

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

        headerR.add(cardBaoTri);
        headerR.add(cardHoatDong);
        headerR.add(cardTongTau);

        JLabel title = new JLabel("Quản lý đoàn tàu");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Quản lý danh sách tàu, loại tàu và trạng thái hoạt động trong hệ thống Metro");
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

        // Format Cột cho Tàu
        String[] columns = {"Mã Tàu", "Tên Tàu", "Loại Tàu", "Trạng Thái"};
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

    public void setData(List<Tau> list) {
        tableModel.setRowCount(0);
        for (Tau t : list) {
            tableModel.addRow(new Object[] {
                t.getMaTau(), t.getTenTau(), t.getLoaiTau(), t.getTrangThai()
            });
        }
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        
        for (String option : tauOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = ((JMenuItem) e.getSource()).getText();
                switch (text) {
                    case "Thêm tàu mới":
                        themTau();
                        break;
                    case "Xóa tàu":
                        xoaTau();
                        break;
                    case "Sửa thông tin":
                        suaTau();
                        break;
                    case "Tra cứu tàu":
                        traCuuTau();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Chọn: " + text);
                }
            });
            submenu.add(it);
        }
        return submenu;
    }

    // Thêm tàu
    private void themTau() {
        String[] labels = {"Mã Tàu", "Tên Tàu", "Loại Tàu (Cao tốc/Thường)", "Trạng Thái (Hoạt động/Bảo trì)"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnSave = new JButton("Lưu Tàu");

        JDialog dialog = component.createDinamicForm(
            "Thêm Tàu Mới", 
            "Nhập Thông Tin Tàu", 
            "Vui lòng điền đầy đủ dữ liệu đoàn tàu", 
            labels, fields, new JButton[]{btnCancel, btnSave}
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            String maTau = fields[0].getText().trim();
            String tenTau = fields[1].getText().trim();
            String loaiTau = fields[2].getText().trim();
            String trangThai = fields[3].getText().trim();

            if (maTau.isEmpty() || tenTau.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mã tàu và Tên tàu không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Mock Data - Thêm thẳng vào bảng không cần DB
            tableModel.addRow(new Object[]{maTau, tenTau, loaiTau, trangThai});
            JOptionPane.showMessageDialog(dialog, "Thêm tàu thành công!");
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    // Xóa tàu
    private void xoaTau() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa đoàn tàu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    // Sửa thông tin tàu
    private void suaTau() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu cần sửa!");
            return;
        }

        // Lấy dữ liệu cũ từ Table
        String maTau = tableModel.getValueAt(selectedRow, 0).toString();
        String tenTau = tableModel.getValueAt(selectedRow, 1).toString();
        String loaiTau = tableModel.getValueAt(selectedRow, 2).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 3).toString();

        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa thông tin tàu");
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());

        JLabel lbMaTau = new JLabel("Mã Tàu:");
        JTextField txtMaTau = new JTextField(maTau, 25);
        txtMaTau.setEditable(false); // Không cho sửa mã gốc

        JLabel lbTenTau = new JLabel("Tên Tàu:");
        JTextField txtTenTau = new JTextField(tenTau, 25);

        JLabel lbLoaiTau = new JLabel("Loại Tàu:");
        JTextField txtLoaiTau = new JTextField(loaiTau, 25);

        JLabel lbTrangThai = new JLabel("Trạng Thái:");
        JTextField txtTrangThai = new JTextField(trangThai, 25);

        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnCancel = new JButton("Hủy");

        dialog.add(lbMaTau);
        dialog.add(txtMaTau);
        dialog.add(lbTenTau);
        dialog.add(txtTenTau);
        dialog.add(lbLoaiTau);
        dialog.add(txtLoaiTau);
        dialog.add(lbTrangThai);
        dialog.add(txtTrangThai);
        
        dialog.add(btnUpdate);
        dialog.add(btnCancel);

        btnUpdate.addActionListener(ev -> {
            tableModel.setValueAt(txtTenTau.getText(), selectedRow, 1);
            tableModel.setValueAt(txtLoaiTau.getText(), selectedRow, 2);
            tableModel.setValueAt(txtTrangThai.getText(), selectedRow, 3);

            JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
            dialog.dispose();
        });

        btnCancel.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Tra cứu tàu
    private void traCuuTau() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Tên Tàu hoặc Mã Tàu cần tìm:");
        JOptionPane.showMessageDialog(table, "Đã ghi nhận từ khóa: " + keyword + " (Chưa kết nối DB)");
    }
}