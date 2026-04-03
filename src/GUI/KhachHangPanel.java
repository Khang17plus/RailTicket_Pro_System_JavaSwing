package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Thư viện để làm nội dung cho bảng
import java.awt.*;

public class KhachHangPanel extends JPanel {

    public KhachHangPanel() {
        // 1. Bố cục chính: Trên (NORTH) là Tiêu đề + Tìm kiếm, Giữa (CENTER) là Cái bảng
        setLayout(new BorderLayout());

        // --- PHẦN TRÊN: TIÊU ĐỀ VÀ THANH TÌM KIẾM ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 1, 5, 5)); // 2 hàng: Hàng 1 Tiêu đề, Hàng 2 Tìm kiếm

        JLabel title = new JLabel("DANH SÁCH KHÁCH HÀNG");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Ô tìm kiếm đơn giản
        JPanel searchPanel = new JPanel(); // Dùng FlowLayout mặc định để nó nằm giữa
        searchPanel.add(new JLabel("Tìm tên khách: "));
        JTextField txtSearch = new JTextField(20); // Độ dài ô nhập là 20
        JButton btnSearch = new JButton("Tìm");
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        topPanel.add(title);
        topPanel.add(searchPanel);
        
        add(topPanel, BorderLayout.NORTH);

        // --- PHẦN GIỮA: CÁI BẢNG (JTable) ---
        // BƯỚC A: Tạo danh sách tên cột
        String[] columnNames = {"Mã KH", "Họ và Tên", "Số điện thoại", "Email", "Loại vé"};

        // BƯỚC B: Tạo dữ liệu giả (Mỗi ngoặc nhọn là 1 hàng)
        Object[][] data = {
            {"KH001", "Nguyễn Văn A", "0901234567", "anv@gmail.com", "Vé tháng"},
            {"KH002", "Trần Thị B", "0908888888", "btt@gmail.com", "Vé lượt"},
            {"KH003", "Lê Văn C", "0907777777", "clv@gmail.com", "Vé tháng"},
            {"KH004", "Phạm Minh D", "0901112223", "dpm@gmail.com", "Vé lượt"}
        };

        // BƯỚC C: Bỏ dữ liệu vào "Ruột" bảng (Model)
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        // BƯỚC D: Tạo bảng từ cái "Ruột" đó
        JTable table = new JTable(model);
        
        // Chỉnh cái bảng cho đẹp tí
        table.setRowHeight(30); // Cho hàng nó cao ra, dễ nhìn
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14)); // Chỉnh chữ ở đầu mục cho đậm

        // BƯỚC E: Bỏ bảng vào JScrollPane (Bắt buộc phải có cái này thì mới hiện tên cột được)
        JScrollPane scrollPane = new JScrollPane(table);
        
        add(scrollPane, BorderLayout.CENTER);
    }
}