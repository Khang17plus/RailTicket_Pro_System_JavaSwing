package GUI;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        // 1. Dùng BorderLayout cho gọn: Tiêu đề ở TRÊN, Nội dung ở GIỮA
        setLayout(new BorderLayout());

        // 2. Tạo cái Tiêu đề
        JLabel title = new JLabel("TRANG CHỦ - HỆ THỐNG TÀU HỎA");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa chữ
        add(title, BorderLayout.NORTH);

        // 3. Tạo một cái Panel lớn để chứa mọi thứ ở giữa
        JPanel centerPanel = new JPanel();
        // GridLayout(số hàng, số cột, khoảng cách ngang, khoảng cách dọc)
        centerPanel.setLayout(new GridLayout(2, 1, 10, 10)); 

        // --- PHẦN 1: 4 CÁI Ô THỐNG KÊ ---
        JPanel cardArea = new JPanel(new GridLayout(1, 4, 10, 10));
        cardArea.add(taoOThongKe("VÉ ĐÃ BÁN", "1.847 cái"));
        cardArea.add(taoOThongKe("DOANH THU", "73 Triệu"));
        cardArea.add(taoOThongKe("CHUYẾN TÀU", "156 Chuyến"));
        cardArea.add(taoOThongKe("KHÁCH HÀNG", "12.456 Người"));

        // --- PHẦN 2: 2 CÁI Ô BIỂU ĐỒ (LÀM GIẢ BẰNG CHỮ) ---
        JPanel chartArea = new JPanel(new GridLayout(1, 2, 10, 10));
        chartArea.add(taoOChartGia("BIỂU ĐỒ DOANH THU TUẦN"));
        chartArea.add(taoOChartGia("BIỂU ĐỒ LƯỢNG VÉ BÁN RA"));

        // Add 2 phần này vào centerPanel
        centerPanel.add(cardArea);
        centerPanel.add(chartArea);

        add(centerPanel, BorderLayout.CENTER);
    }

    // Hàm tạo 1 cái ô thống kê (Dễ đọc cực kỳ)
    private JPanel taoOThongKe(String ten, String soLieu) {
        JPanel p = new JPanel();
        // Tạo cái viền đen mỏng cho dễ nhìn
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        p.setLayout(new GridLayout(2, 1)); // Chia làm 2 dòng: dòng trên tên, dòng dưới số

        JLabel lblTen = new JLabel(ten);
        lblTen.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSo = new JLabel(soLieu);
        lblSo.setFont(new Font("Arial", Font.BOLD, 18)); // Cho số to lên
        lblSo.setForeground(Color.BLUE); // Cho số màu xanh cho nổi
        lblSo.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(lblTen);
        p.add(lblSo);

        return p;
    }

    // Hàm tạo 1 cái ô biểu đồ giả
    private JPanel taoOChartGia(String tenBieuDo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(tenBieuDo)); // Tạo cái viền có tên ở trên

        
        
        
        return p;
    }
}