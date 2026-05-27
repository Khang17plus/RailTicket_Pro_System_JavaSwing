package GUI;

import javax.swing.*;
import org.jfree.chart.ChartPanel;
import java.awt.*;
import java.awt.event.ComponentAdapter; // Đã thêm
import java.awt.event.ComponentEvent;   // Đã thêm
import java.util.Map;
import Controller.TrangChuController; // Import Controller quản lý dữ liệu trang chủ

public class DashboardPanel extends JPanel {

    // 🔥 Biến các nhãn hiển thị số thành thuộc tính để có thể cập nhật dữ liệu từ DB
    private JLabel lblVeValue = new JLabel("0");
    private JLabel lblDoanhThuValue = new JLabel("0đ");
    private JLabel lblChuyenValue = new JLabel("0");
    private JLabel lblKhachValue = new JLabel("0");
    
    // Vùng chứa biểu đồ động
    private JPanel chartRow;
    private TrangChuController controller;

    private JPanel createCard(String title , String value ) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(10,10,10,10)
                ));
        
        JLabel t = new JLabel(title);
        t.setForeground(Color.GRAY);
        
        JLabel v = new JLabel(value);
        v.setFont(new Font("Arial", Font.BOLD, 20));

        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        card.putClientProperty("FlatLaf.style", "arc:20; border:12,12,12,12; background:#FFFFFF");
        
        return card;
    }

    // 🔥 NÂNG CẤP: Thay thế tham số chuỗi 'value' bằng chính đối tượng 'JLabel' để quản lý dữ liệu động
    private JPanel createCard(String title , JLabel lblValue, Icon icon , Color iconBgColor) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.white);
        
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 235, 235), 1, true),
                 BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(iconBgColor);
        iconLabel.setPreferredSize(new Dimension(45, 45));
        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        card.add(iconLabel, gbc);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 5, 0);
        card.add(lblTitle, gbc);
        
        // Cấu hình font chữ cho nhãn giá trị truyền vào
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(lblValue, gbc);
        
        JLabel lblSub = new JLabel("Hôm nay");
        lblSub.setForeground(Color.LIGHT_GRAY);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
        
        gbc.gridy = 3;
        card.add(lblSub, gbc);
        
        card.putClientProperty("FlatLaf.style", "arc:20; border:12,12,12,12; background:#FFFFFF");
        return card;
    }
        
    private JPanel createChartCard(String title, ChartPanel chart) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(t, BorderLayout.NORTH);
        panel.add(chart, BorderLayout.CENTER);
        panel.putClientProperty("FlatLaf.style", "arc:20; border:12,12,12,12; background:#FFFFFF");
        return panel;
    }
        
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        header.setOpaque(false); // Đảm bảo tệp màu nền đồng bộ

        JLabel title = new JLabel("Trang chủ");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        
        JLabel sub = new JLabel("Tổng quan hệ thống bán vé tàu");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(sub);
        add(header, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Row 1: Thống kê tổng quan thẻ KPI
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 15, 0));
        statsRow.setOpaque(false);
        
        // 🔥 Gắn các thuộc tính JLabel động vào các thẻ KPI tương ứng
        statsRow.add(createCard("Vé đã bán", lblVeValue, new ImageIcon("img/ticket-thin (1).png"), Color.white));
        statsRow.add(createCard("Doanh thu", lblDoanhThuValue, new ImageIcon("img/currency-dollar-thin.png"), Color.white));
        statsRow.add(createCard("Chuyến tàu", lblChuyenValue, new ImageIcon("img/train-thin.png"), Color.white));
        statsRow.add(createCard("Khách hàng", lblKhachValue, new ImageIcon("img/users-thin (3).png"), Color.white));
        
        main.add(statsRow);
        main.add(Box.createVerticalStrut(20));

        // Row 2: Vùng chứa biểu đồ tuần tuần
        chartRow = new JPanel(new GridLayout(1, 2, 15, 0));
        chartRow.setOpaque(false);
        main.add(chartRow);
        
        add(main, BorderLayout.CENTER);
        
        // 🔥 KÍCH HOẠT CONTROLLER: Tự động kết nối DB quét dữ liệu thật đổ lên giao diện khi Panel khởi chạy
        this.controller = new TrangChuController(this);

        // =========================================================
        // 🔥 BỔ SUNG: TỰ ĐỘNG LOAD LẠI DỮ LIỆU KHI CHUYỂN TAB VÀO ĐÂY
        // =========================================================
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (controller != null) {
                    controller.loadThongKeHeThong();
                }
            }
        });
    }
    
    /**
     * 🔥 HÀM MỚI: Cập nhật dữ liệu chữ thực tế cho các thẻ KPI thông số
     */
    public void updateKPICards(int veBan, double doanhThu, int chuyenTau, int khachHang) {
        lblVeValue.setText(String.valueOf(veBan));
        lblChuyenValue.setText(String.valueOf(chuyenTau));
        lblKhachValue.setText(String.valueOf(khachHang));
        
        // Định dạng rút gọn doanh thu thông minh (Ví dụ: 73500000đ -> 73.5M)
        if (doanhThu >= 1000000) {
            lblDoanhThuValue.setText(String.format("%.1fM", doanhThu / 1000000.0));
        } else {
            lblDoanhThuValue.setText(String.format("%,.0fđ", doanhThu));
        }
    }

    /**
     * 🔥 HÀM MỚI: Nhận dữ liệu Map từ DB để vẽ lại đồ thị động JFreeChart sạch sẽ, không lo đè nén component
     */
    public void updateCharts(Map<String, Double> dataDoanhThu, Map<String, Integer> dataVe) {
        chartRow.removeAll(); // Xóa bỏ khung biểu đồ rỗng/cũ cũ

        // Tạo và add biểu đồ thật dựa vào nạp dữ liệu Map truyền từ Controller xuống
        chartRow.add(createChartCard("Doanh thu tuần", ChartUtil.createBarChart(dataDoanhThu)));
        chartRow.add(createChartCard("Số vé bán ra", ChartUtil.createLineChart(dataVe)));

        chartRow.revalidate();
        chartRow.repaint();
    }
}