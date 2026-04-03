package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BanVePanel extends JPanel {

    public BanVePanel() {
        // 1. Layout chính: Trên là Tiêu đề & Lọc, Giữa là Bảng vé, Dưới là Nút bấm
        setLayout(new BorderLayout());

        // --- PHẦN 1: TIÊU ĐỀ & BỘ LỌC (NORTH) ---
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ VÉ TÀU");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Thanh công cụ lọc vé
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Loại vé: "));
        String[] loaiVe = {"Tất cả", "Vé lượt", "Vé tháng", "Vé VIP"};
        JComboBox<String> cbLoaiVe = new JComboBox<>(loaiVe); // Ô chọn sổ xuống
        
        filterPanel.add(cbLoaiVe);
        filterPanel.add(new JLabel("  Mã vé: "));
        JTextField txtMaVe = new JTextField(10);
        JButton btnTim = new JButton("Tìm kiếm");
        
        filterPanel.add(txtMaVe);
        filterPanel.add(btnTim);

        topPanel.add(title);
        topPanel.add(filterPanel);
        add(topPanel, BorderLayout.NORTH);

        // --- PHẦN 2: BẢNG DANH SÁCH VÉ (CENTER) ---
        String[] columnNames = {"Mã Vé", "Loại Vé", "Tên Khách", "Số Ghế", "Giá Tiền", "Trạng Thái"};

        Object[][] data = {
            {"VE001", "Vé lượt", "Nguyễn Văn A", "A-12", "50.000", "Đã thanh toán"},
            {"VE002", "Vé tháng", "Trần Thị B", "B-05", "500.000", "Đã thanh toán"},
            {"VE003", "Vé lượt", "Lê Văn C", "C-01", "50.000", "Chờ xử lý"},
            {"VE004", "Vé VIP", "Phạm Minh D", "V-01", "150.000", "Đã thanh toán"},
            {"VE005", "Vé lượt", "Hoàng Văn E", "A-20", "50.000", "Đã hủy"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setRowHeight(30); // Cho hàng cao ra nhìn cho sướng mắt

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- PHẦN 3: CÁC NÚT CHỨC NĂNG (SOUTH) ---
        JPanel bottomPanel = new JPanel();
        
        JButton btnAdd = new JButton("Đặt Vé Mới");
        JButton btnEdit = new JButton("Sửa Thông Tin");
        JButton btnCancel = new JButton("Hủy Vé");
        JButton btnReload = new JButton("Làm Mới");

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnCancel);
        bottomPanel.add(btnReload);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}