package GUI;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Controller.ThongKeController;

// Import thư viện biểu đồ
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class ThongKePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblDoanhThu, lblSoVe;
    private JPanel chartPanelWrapper; 
    
    // --- Các Component mới cho thanh tìm kiếm ---
    private JComboBox<String> cbxTieuChi;
    private JTextField txtTimKiem;
    private JButton btnLoc;
    private ThongKeController controller;

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- 1. TOP PANEL: Chứa Thanh tìm kiếm (Trái) và Thẻ KPI (Phải) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));

        // 1a. Thanh bộ lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        filterPanel.setOpaque(false);
        
        cbxTieuChi = new JComboBox<>(new String[]{"Theo tháng", "Theo ngày", "Theo nhân viên"});
        cbxTieuChi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        txtTimKiem = new JTextField(15);
        txtTimKiem.setPreferredSize(new Dimension(150, 30));
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Nhập từ khóa...");
        
        btnLoc = new JButton("Lọc dữ liệu");
        btnLoc.setBackground(new Color(59, 130, 246));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.putClientProperty("FlatLaf.style", "arc:8; font: bold 12");

        filterPanel.add(new JLabel("Thống kê:"));
        filterPanel.add(cbxTieuChi);
        filterPanel.add(new JLabel("Tìm kiếm:"));
        filterPanel.add(txtTimKiem);
        filterPanel.add(btnLoc);

        // Bắt sự kiện nút Lọc
        btnLoc.addActionListener(e -> {
            if (controller != null) {
                String tieuChi = cbxTieuChi.getSelectedItem().toString();
                String tuKhoa = txtTimKiem.getText().trim();
                controller.loadThongKeDong(tieuChi, tuKhoa);
            }
        });

        // 1b. Thẻ Card thống kê KPI
        JPanel headerKPI = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerKPI.setOpaque(false);
        
        lblDoanhThu = new JLabel("0 VNĐ");
        lblSoVe = new JLabel("0");
        headerKPI.add(createCard("Tổng doanh thu", lblDoanhThu, new Color(34, 197, 94)));
        headerKPI.add(createCard("Vé đã bán", lblSoVe, new Color(59, 130, 246)));

        topPanel.add(filterPanel, BorderLayout.WEST);
        topPanel.add(headerKPI, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- 2. CENTER: Chia đôi màn hình cho Biểu đồ và Bảng ---
        JPanel mainContent = new JPanel(new GridLayout(2, 1, 0, 20));
        mainContent.setBackground(new Color(245, 247, 250));
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Vùng chứa biểu đồ
        chartPanelWrapper = new JPanel(new BorderLayout());
        chartPanelWrapper.setBackground(Color.WHITE);
        chartPanelWrapper.putClientProperty("FlatLaf.style", "arc:15");

        // Vùng chứa bảng (Đổi tên cột cho linh hoạt với Ngày/Tháng/Nhân viên)
        String[] columns = {"Mốc thời gian / Tên NV", "Số lượng vé", "Số hóa đơn", "Tổng doanh thu (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.putClientProperty("FlatLaf.style", "arc:15; border:0,0,0,0");
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainContent.add(chartPanelWrapper);
        mainContent.add(scrollPane);
        add(mainContent, BorderLayout.CENTER);
    }

    // Liên kết với Controller
    public void setController(ThongKeController controller) {
        this.controller = controller;
    }

    // 🔥 HÀM VẼ BIỂU ĐỒ CỘT (Đã chống lỗi ép kiểu)
    public void veBieuDo(List<Object[]> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Đổ dữ liệu từ SQL vào Dataset của biểu đồ
        for (Object[] row : data) {
            String nhan = String.valueOf(row[0]);
            // Ép kiểu an toàn bằng Number
            double doanhThu = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
            dataset.addValue(doanhThu, "Doanh thu", nhan);
        }

        // Tự động lấy tiêu đề động dựa trên ComboBox
        String tieuChi = cbxTieuChi.getSelectedItem().toString().toUpperCase();
        
        JFreeChart barChart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU " + tieuChi,
                "Đối tượng", "Số tiền (VNĐ)",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        // Tùy chỉnh hiển thị cho đẹp
        barChart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(59, 130, 246)); // Cột màu xanh dương
        renderer.setShadowVisible(false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.WHITE);

        chartPanelWrapper.removeAll();
        chartPanelWrapper.add(chartPanel, BorderLayout.CENTER);
        chartPanelWrapper.revalidate();
        chartPanelWrapper.repaint();
    }

    // --- CÁC HÀM CẬP NHẬT DỮ LIỆU BẢNG & CARD ---
    public void setDataTable(List<Object[]> data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            // Ép kiểu an toàn từ SQL qua Number thay vì ép thẳng Integer
            int soVe = row[1] != null ? ((Number) row[1]).intValue() : 0;
            int soHoaDon = row[2] != null ? ((Number) row[2]).intValue() : 0;
            double doanhThu = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
            
            tableModel.addRow(new Object[]{
                row[0], 
                soVe, 
                soHoaDon, 
                String.format("%,.0f", doanhThu)
            });
        }
    }

    public void updateCards(double doanhThu, int soVe) {
        lblDoanhThu.setText(String.format("%,.0f VNĐ", doanhThu));
        lblSoVe.setText(String.valueOf(soVe));
    }

    private JPanel createCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setPreferredSize(new Dimension(220, 75));
        card.setBackground(Color.WHITE);
        card.putClientProperty("FlatLaf.style", "arc:12; border:10,15,10,15");
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);
        
        card.add(lblTitle); 
        card.add(valueLabel);
        return card;
    }
}