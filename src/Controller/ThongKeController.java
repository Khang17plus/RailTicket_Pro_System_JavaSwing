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
        
        // Dòng này rất quan trọng để nạp Controller vào Panel giúp nút Lọc bấm được
        this.panel.setController(this); 
    }

    // Hàm gọi khi mới mở form (Mặc định hiển thị theo tháng)
    public void loadStatistics() {
        loadThongKeDong("Theo tháng", ""); 
    }

    // 🔥 Hàm MỚI: Xử lý động dựa trên tiêu chí chọn
    public void loadThongKeDong(String tieuChi, String tuKhoa) {
        List<Object[]> data = null;
        
        // Điều hướng truy vấn dựa vào ComboBox
        switch (tieuChi) {
            case "Theo tháng":
                data = dao.getDoanhThuTheoThang(); // Hàm cũ của bạn
                break;
            case "Theo ngày":
                data = dao.getDoanhThuTheoNgay(tuKhoa);
                break;
            case "Theo nhân viên":
                data = dao.getDoanhThuTheoNhanVien(tuKhoa);
                break;
        }

        if (data != null) {
            panel.setDataTable(data);    // Đổ vào bảng
            panel.veBieuDo(data);        // Vẽ lại biểu đồ
            
            double tongDoanhThu = 0;
            int tongVe = 0;
            for (Object[] row : data) {
                try {
                    if (row[1] != null) tongVe += ((Number) row[1]).intValue();
                    if (row[3] != null) tongDoanhThu += ((Number) row[3]).doubleValue();
                } catch (Exception ex) { ex.printStackTrace(); }
            }
            panel.updateCards(tongDoanhThu, tongVe);
        }
    }
}