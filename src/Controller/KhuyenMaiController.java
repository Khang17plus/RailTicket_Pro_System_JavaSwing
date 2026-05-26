package Controller;

import java.util.List;
import DAO.KhuyenMaiDAO; 
import Entity.KhuyenMai; 
import GUI.KhuyenMaiPanel;

public class KhuyenMaiController {
    
    private KhuyenMaiPanel view;
    private KhuyenMaiDAO dao;
    
    public KhuyenMaiController(KhuyenMaiPanel view) {
        this.view = view;
        this.dao = new KhuyenMaiDAO();
        
        // Tự động đổ dữ liệu khi khởi tạo
        loadDataToTable();
    }
    
    /**
     * Lấy toàn bộ danh sách khuyến mãi từ DB và hiển thị lên bảng
     */
    public void loadDataToTable() {
        List<KhuyenMai> list = dao.getAll();
        view.setData(list);
        view.capNhatThongKeCoDinh(list); // Gọi tính toán dựa trên full danh sách gốc
    }
    
    /**
     * Thêm mới một chương trình khuyến mãi
     */
    public boolean themKhuyenMai(KhuyenMai km) {
        return dao.insert(km); 
    }
    
    /**
     * Cập nhật thông tin khuyến mãi đã tồn tại
     */
    public boolean updateKhuyenMai(KhuyenMai km) {
        return dao.update(km);
    }
    
    /**
     * Xóa khuyến mãi theo mã
     */
    public boolean xoaKhuyenMai(String maKM) {
        return dao.delete(maKM);
    }
    
    /**
     * Tìm kiếm khuyến mãi theo từ khóa (Mã hoặc Tên)
     */
    public void timKiemKhuyenMai(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable();
        } else {
            List<KhuyenMai> listKetQuaTimKiem = dao.searchKhuyenMai(keyword);
            view.setData(listKetQuaTimKiem); // Chỉ đổi dữ liệu hiển thị trên bảng table
            
            // 🔥 KHÔNG cập nhật lại số thống kê ở đây, giữ nguyên số tổng cũ từ DB!
            // Hoặc nếu muốn chắc chắn số tổng luôn khớp database mới nhất, bạn có thể gọi:
            view.capNhatThongKeCoDinh(dao.getAll());
        }
    }

    // =========================================================================
    // THÀNH PHẦN SỬA ĐỔI: HÀM PHÁT SINH MÃ KHUYẾN MÃI TỰ ĐỘNG
    // =========================================================================
    /**
     * Tự động phát sinh mã Khuyến Mãi tiếp theo dạng KMxxx (KM001, KM002,...)
     */
    public String phatSinhMaTuDong() {
        // Gọi DAO lấy về mã KM lớn nhất hiện tại (Ví dụ: "KM014")
        String maxMa = dao.getMaxMaKhuyenMai();
        
        // Nếu database trống trơn, chưa có chương trình nào thì trả về mã đầu tiên
        if (maxMa == null || maxMa.trim().isEmpty()) {
            return "KM001";
        }
        
        try {
            // Cắt chuỗi lấy phần số, bỏ chữ "KM" ở đầu (Ví dụ: "KM014" -> lấy từ index 2 được "014")
            String phanSoStr = maxMa.substring(2).trim();
            
            // Chuyển chuỗi số thành kiểu int để cộng dồn (Ví dụ: 14 + 1 = 15)
            int phanSo = Integer.parseInt(phanSoStr);
            phanSo++; 
            
            // Định dạng lại chuỗi trả về đủ 3 chữ số (Ví dụ: số 15 thành "015")
            return String.format("KM%03d", phanSo);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về mã ngẫu nhiên theo thời gian thực nếu định dạng chuỗi mã bị lỗi ép kiểu
            return "KM" + System.currentTimeMillis(); 
        }
    }
}