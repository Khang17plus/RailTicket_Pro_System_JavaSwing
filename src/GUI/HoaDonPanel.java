package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HoaDonPanel extends JPanel {

    public HoaDonPanel() {
        // 1. Layout chính: Trên là tiêu đề, Giữa là bảng, Dưới là nút bấm
        setLayout(new BorderLayout());

        // --- PHẦN 1: TIÊU ĐỀ (NORTH) ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        
        JLabel title = new JLabel("QUẢN LÝ HÓA ĐƠN");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Ô lọc hóa đơn theo ngày hoặc mã
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Nhập mã hóa đơn: "));
        JTextField txtSearch = new JTextField(15);
        JButton btnFilter = new JButton("Lọc dữ liệu");
        filterPanel.add(txtSearch);
        filterPanel.add(btnFilter);

        topPanel.add(title);
        topPanel.add(filterPanel);
        add(topPanel, BorderLayout.NORTH);

        // --- PHẦN 2: CÁI BẢNG HÓA ĐƠN (CENTER) ---
        // Tên các cột cho Hóa đơn
        String[] columnNames = {"Mã HĐ", "Ngày Lập", "Nhân Viên", "Khách Hàng", "Tổng Tiền (VNĐ)"};

        // Dữ liệu giả cho Hóa đơn
        Object[][] data = {
            {"HD001", "30/03/2026", "Nguyễn Văn A", "Trần Văn Tú", "150.000"},
            {"HD002", "30/03/2026", "Lê Thị B", "Nguyễn Minh C", "45.000"},
            {"HD003", "31/03/2026", "Nguyễn Văn A", "Phạm Hoàng D", "220.000"},
            {"HD004", "31/03/2026", "Trần Văn C", "Lý Thanh E", "90.000"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        
        // Chỉnh bảng tí cho thoáng
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- PHẦN 3: CÁC NÚT CHỨC NĂNG (SOUTH) ---
        JPanel bottomPanel = new JPanel(); // Mặc định là FlowLayout (nằm giữa)
        
        JButton btnAdd = new JButton("Tạo Hóa Đơn Mới");
        JButton btnPrint = new JButton("In Hóa Đơn (PDF)");
        JButton btnDelete = new JButton("Xóa Hóa Đơn");

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnPrint);
        bottomPanel.add(btnDelete);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}