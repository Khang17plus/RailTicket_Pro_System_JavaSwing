package Controller;

import java.util.List;
import DAO.NhanVienDAO;
import Entity.NhanVien;
import GUI.NhanVienPanel;

public class NhanVienController {
    
    private NhanVienPanel view;
    private NhanVienDAO dao;
    
    public NhanVienController(NhanVienPanel view) {
        this.view = view;
        this.dao = new NhanVienDAO();
        
        // Tự động đổ dữ liệu khi khởi tạo
        loadDataToTable();
    }
    
    /**
     * Lấy toàn bộ danh sách nhân viên từ DB và hiển thị lên bảng
     */
    public void loadDataToTable() {
        List<NhanVien> list = dao.getAll();
        view.setData(list);
        view.capNhatThongKeCoDinh(list); // Gọi tính toán số lượng dựa trên full danh sách gốc
    }
    
    /**
     * Thêm mới một nhân viên
     */
    public boolean themNhanVien(NhanVien nv) {
        return dao.insert(nv);
    }
    
    /**
     * Cập nhật thông tin nhân viên đã tồn tại (bao gồm cả trạng thái thôi việc)
     */
    public boolean updateNhanVien(NhanVien nv) {
        return dao.update(nv);
    }
    
    /**
     * THÀNH PHẦN MỚI: TỰ ĐỘNG PHÁT SINH MÃ NHÂN VIÊN TIẾP THEO
     * Định dạng mã sinh ra: NVxxx (Ví dụ: NV001, NV002, NV015,...)
     */
    public String phatSinhMaTuDong() {
        String maxMa = dao.getMaxMaNhanVien();
        
        // Trường hợp Database chưa có nhân viên nào
        if (maxMa == null || maxMa.trim().isEmpty()) {
            return "NV001";
        }
        
        try {
            // Cắt chuỗi bỏ đi chữ "NV" để lấy phần số đằng sau
            String phanSoStr = maxMa.substring(2).trim();
            int phanSo = Integer.parseInt(phanSoStr);
            
            // Tăng mã số lên 1 đơn vị
            phanSo++;
            
            // Định dạng lại chuỗi dạng NV kèm 3 chữ số, tự bù số 0 ở trước (ví dụ: NV004)
            return String.format("NV%03d", phanSo);
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi bất ngờ (sai định dạng chuỗi), sinh mã tạm dựa trên timestamp để không nghẽn hệ thống
            return "NV" + (System.currentTimeMillis() % 1000);
        }
    }
    
    /**
     * Tìm kiếm nhân viên theo từ khóa (Mã hoặc Số điện thoại)
     */
    public void timKiemNhanVien(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable();
        } else {
            List<NhanVien> listKetQuaTimKiem = dao.searchNhanVien(keyword);
            view.setData(listKetQuaTimKiem); // Chỉ đổi dữ liệu hiển thị trên bảng table
            
            // Giữ nguyên số thống kê tổng/đang làm/nghỉ việc cũ từ full DB!
            view.capNhatThongKeCoDinh(dao.getAll());
        }
    }	
}