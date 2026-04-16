package GUI;

import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

public class ChartUtil {

    // ===== BAR CHART =====
    public static ChartPanel createBarChart() {

        // Dataset chứa dữ liệu
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Thêm dữ liệu (có thể thay bằng DB)
        dataset.addValue(3, "Doanh thu", "T2");
        dataset.addValue(5, "Doanh thu", "T3");
        dataset.addValue(4, "Doanh thu", "T4");
        dataset.addValue(7, "Doanh thu", "T5");

        // Tạo biểu đồ cột
        JFreeChart chart = ChartFactory.createBarChart(
                "", "", "", dataset
        );

        style(chart);

        return new ChartPanel(chart);
    }

    // ===== LINE CHART =====
    public static ChartPanel createLineChart() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1200, "Vé", "T2");
        dataset.addValue(1500, "Vé", "T3");
        dataset.addValue(1300, "Vé", "T4");
        dataset.addValue(1700, "Vé", "T5");

        // Tạo biểu đồ đường
        JFreeChart chart = ChartFactory.createLineChart(
                "", "", "", dataset
        );

        style(chart);

        return new ChartPanel(chart);
    }

    // ===== STYLE CHUNG =====
    private static void style(JFreeChart chart) {

        // Nền trắng
        chart.setBackgroundPaint(Color.WHITE);

        // Lấy vùng vẽ
        CategoryPlot plot = chart.getCategoryPlot();

        // Nền trắng
        plot.setBackgroundPaint(Color.WHITE);

        // Gridline xám nhạt
        plot.setRangeGridlinePaint(new Color(220,220,220));
    }
}