package Controller;

import java.util.Map;
import DAO.ThongKeDAO;
import GUI.DashboardPanel; // Đã đổi từ TrangChuPanel sang DashboardPanel

public class TrangChuController {
    private DashboardPanel view; // Đã đổi kiểu dữ liệu tương thích với giao diện
    private ThongKeDAO thongKeDAO = new ThongKeDAO();

    public TrangChuController(DashboardPanel view) {
        this.view = view;
        loadThongKeHeThong();
    }

    /**
     * Hàm lấy toàn bộ dữ liệu thật từ DB và bắn lên UI
     */
    public void loadThongKeHeThong() {
        if (view == null) return;
        
        // 1. Lấy dữ liệu thật cho 4 thẻ KPI từ DAO
        int veBan = thongKeDAO.getSoVeBanHomNay();
        double doanhThu = thongKeDAO.getDoanhThuHomNay();
        int chuyenTau = thongKeDAO.getSoChuyenTauHomNay();
        int khachHang = thongKeDAO.getTongSoKhachHang();
        
        // Đẩy lên các nhãn thông số trên giao diện DashboardPanel
        view.updateKPICards(veBan, doanhThu, chuyenTau, khachHang);

        // 2. Lấy dữ liệu đồ thị tuần từ DAO
        Map<String, Double> dsDoanhThuTuan = thongKeDAO.getDoanhThuTheoTuan();
        Map<String, Integer> dsVeTuan = thongKeDAO.getSoVeBanTheoTuan();
        
        // Đẩy dữ liệu vào hàm vẽ đồ thị thật trên giao diện
        view.updateCharts(dsDoanhThuTuan, dsVeTuan);
    }
}