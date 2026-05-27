package GUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.Map;

public class ChartUtil {

    /**
     * Tạo biểu đồ Cột Doanh Thu nhận dữ liệu thật từ Map
     */
    public static ChartPanel createBarChart(Map<String, Double> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Đổ dữ liệu từ Map vào Dataset của JFreeChart
        if (data != null) {
            data.forEach((thu, doanhThu) -> {
                dataset.addValue(doanhThu, "Doanh thu", thu);
            });
        }

        // Tạo biểu đồ cột đứng (Vertical Bar Chart)
        JFreeChart barChart = ChartFactory.createBarChart(
                null,                   // Tiêu đề (Bỏ trống vì đã có ở Card Title)
                null,                   // Trục X
                null,                   // Trục Y
                dataset,                // Dữ liệu
                PlotOrientation.VERTICAL,
                false,                  // Hiện chú thích (Legend) -> Tắt cho đỡ chật
                true,                   // Tooltips
                false                   // URLs
        );

        // --- CUSTOM GIAO DIỆN FLAT MODERN ---
        barChart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(240, 240, 240)); // Đường kẻ ngang mờ
        plot.setOutlineVisible(false);

        // Đổi màu cột thành màu đỏ san hô giống như hình mẫu của bạn
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(248, 113, 113)); 
        renderer.setShadowVisible(false); // Tắt bóng đổ phía sau cột

        // 🔥 SỬA LỖI: Thay thế hàm sai bằng cách chỉnh Margin trên trục X
        // Chỉ số 0.3 nghĩa là khoảng trống chiếm 30%, giúp các cột thon gọn và tinh tế hơn
        plot.getDomainAxis().setCategoryMargin(0.3); 

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 260));
        chartPanel.setBackground(Color.WHITE);
        return chartPanel;
    }

    /**
     * Tạo biểu đồ Đường Số Vé nhận dữ liệu thật từ Map
     */
    public static ChartPanel createLineChart(Map<String, Integer> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Đổ dữ liệu số vé từ Map vào Dataset
        if (data != null) {
            data.forEach((thu, soVe) -> {
                dataset.addValue(soVe, "Số vé", thu);
            });
        }

        // Tạo biểu đồ đường (Line Chart)
        JFreeChart lineChart = ChartFactory.createLineChart(
                null, 
                null, 
                null, 
                dataset, 
                PlotOrientation.VERTICAL, 
                false, 
                true, 
                false
        );

        // --- CUSTOM GIAO DIỆN FLAT MODERN ---
        lineChart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(240, 240, 240));
        plot.setOutlineVisible(false);

        // Đổi màu nét vẽ sang màu đỏ thanh mảnh, sắc nét
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(239, 68, 68)); 
        renderer.setSeriesStroke(0, new BasicStroke(2.0f)); // Độ dày đường line
        renderer.setSeriesShapesVisible(0, true); // Hiện các chấm tròn tại các mốc ngày

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(400, 260));
        chartPanel.setBackground(Color.WHITE);
        return chartPanel;
    }
}