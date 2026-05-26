package Controller;

import java.util.List;
import DAO.KhachHangDAO;
import Entity.KhachHang;
import GUI.KhachHangPanel;

public class KhachHangController {
    
    private KhachHangPanel view;
    private KhachHangDAO dao;
    
    public KhachHangController(KhachHangPanel view) {
        this.view = view;
        this.dao = new KhachHangDAO();
        
        // Tự động đổ dữ liệu lên bảng khi khởi tạo
        loadDataToTable();
    }
    
    /**
     * Lấy toàn bộ danh sách khách hàng từ DB và hiển thị lên giao diện
     */
    public void loadDataToTable() {
        List<KhachHang> list = dao.getAll();
        view.setData(list); // Gọi hàm setData bên KhachHangPanel
    }
    
    /**
     * Thêm mới một khách hàng
     */
    public boolean themKhachHang(KhachHang kh) {
        boolean result = dao.insert(kh);
        if (result) {
            System.out.println("Thêm khách hàng thành công");
        }
        return result;
    }
    
    /**
     * Cập nhật thông tin khách hàng đã tồn tại
     */
    public boolean capNhatKhachHang(KhachHang kh) {
        return dao.update(kh);
    }
    
    /**
     * 🔥 THÀNH PHẦN MỚI: TỰ ĐỘNG PHÁT SINH MÃ KHÁCH HÀNG TIẾP THEO
     * Định dạng mã sinh ra: KHxxx (Ví dụ: KH001, KH002, KH012,...)
     */
    public String phatSinhMaTuDong() {
        String maxMa = dao.getMaxMaKhachHang();
        
        // Nếu Database trống chưa có khách hàng nào
        if (maxMa == null || maxMa.trim().isEmpty()) {
            return "KH001";
        }
        
        try {
            // Cắt chuỗi bỏ đi chữ "KH" để lấy phần số đằng sau
            String phanSoStr = maxMa.substring(2).trim();
            int phanSo = Integer.parseInt(phanSoStr);
            
            // Tăng mã số lên 1 đơn vị
            phanSo++;
            
            // Định dạng lại chuỗi dạng KH kèm 3 chữ số, tự bù số 0 ở trước (ví dụ: KH002)
            return String.format("KH%03d", phanSo);
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi bất ngờ, sinh mã tạm dựa trên timestamp
            return "KH" + (System.currentTimeMillis() % 1000);
        }
    }
    
    /**
     * Tìm kiếm khách hàng theo từ khóa (CCCD hoặc Số điện thoại)
     */
    public void timKiemKhachHang(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable(); // Nếu ô tìm kiếm trống thì hiển thị lại toàn bộ
        } else {
            List<KhachHang> list = dao.searchKhachHang(keyword);
            view.setData(list); // Cập nhật lại bảng với dữ liệu tìm được
        }
    }
}