package Controller;

import java.util.List;
import DAO.ThongKeDAO;
import GUI.ThongKePanel;

public class ThongKeController {
    private ThongKePanel panel;
    private ThongKeDAO dao;

    public ThongKeController(ThongKePanel panel) {
        this.panel = panel;
        this.dao = new ThongKeDAO();
    }

    public void loadStatistics() {
        List<Object[]> data = dao.getDoanhThuTheoThang();
        
        panel.setDataTable(data);    // Đổ vào bảng
        panel.veBieuDo(data);        // Vẽ biểu đồ cột 🔥
        
        double tongDoanhThu = 0;
        int tongVe = 0;
        for (Object[] row : data) {
            tongVe += (int) row[1];
            tongDoanhThu += (double) row[3];
        }
        panel.updateCards(tongDoanhThu, tongVe);
    }
}