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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ThongKePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblDoanhThu, lblSoVe;
    private JPanel chartPanelWrapper; // Nơi chứa biểu đồ cột

    public ThongKePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- 1. HEADER: Thẻ Card thống kê ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        header.setBackground(new Color(245, 247, 250));
        
        lblDoanhThu = new JLabel("0 VNĐ");
        lblSoVe = new JLabel("0");
        header.add(createCard("Tổng doanh thu", lblDoanhThu, new Color(34, 197, 94)));
        header.add(createCard("Vé đã bán", lblSoVe, new Color(59, 130, 246)));
        add(header, BorderLayout.NORTH);

        // --- 2. CENTER: Chia đôi màn hình cho Biểu đồ và Bảng ---
        JPanel mainContent = new JPanel(new GridLayout(2, 1, 0, 20));
        mainContent.setBackground(new Color(245, 247, 250));
        mainContent.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        // Vùng chứa biểu đồ
        chartPanelWrapper = new JPanel(new BorderLayout());
        chartPanelWrapper.setBackground(Color.WHITE);
        chartPanelWrapper.putClientProperty("FlatLaf.style", "arc:15");

        // Vùng chứa bảng
        String[] columns = {"Tháng/Năm", "Số lượng vé", "Doanh thu gốc", "Thực thu (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.putClientProperty("FlatLaf.style", "arc:15; border:0,0,0,0");

        mainContent.add(chartPanelWrapper);
        mainContent.add(scrollPane);
        add(mainContent, BorderLayout.CENTER);
    }

    // 🔥 HÀM VẼ BIỂU ĐỒ CỘT
    public void veBieuDo(List<Object[]> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Đổ dữ liệu từ SQL vào Dataset của biểu đồ
        for (Object[] row : data) {
            String thang = (String) row[0];
            Double doanhThu = (Double) row[3];
            dataset.addValue(doanhThu, "Doanh thu", thang);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "THỐNG KÊ DOANH THU THEO THÁNG",
                "Tháng", "Số tiền (VNĐ)",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        // Tùy chỉnh hiển thị trên Mac cho đẹp
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.WHITE);

        chartPanelWrapper.removeAll();
        chartPanelWrapper.add(chartPanel, BorderLayout.CENTER);
        chartPanelWrapper.validate();
    }

    // --- CÁC HÀM CẬP NHẬT DỮ LIỆU ---
    public void setDataTable(List<Object[]> data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(new Object[]{
                row[0], row[1], 
                String.format("%,.0f", row[2]), 
                String.format("%,.0f VNĐ", row[3])
            });
        }
    }

    public void updateCards(double doanhThu, int soVe) {
        lblDoanhThu.setText(String.format("%,.0f VNĐ", doanhThu));
        lblSoVe.setText(String.valueOf(soVe));
    }

    private JPanel createCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setPreferredSize(new Dimension(200, 75));
        card.setBackground(Color.WHITE);
        card.putClientProperty("FlatLaf.style", "arc:12; border:10,10,10,10");
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        valueLabel.setForeground(color);
        card.add(lblTitle); card.add(valueLabel);
        return card;
    }
}