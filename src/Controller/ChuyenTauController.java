package Controller;

import java.util.List;
import DAO.ChuyenTauDAO;
import DAO.LichTrinhDungDoDAO;
import Entity.ChuyenTau;
import Entity.LichTrinhDungDo;
import GUI.ChuyenTauPanel;

public class ChuyenTauController {
    private ChuyenTauPanel view;
    private ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private LichTrinhDungDoDAO lichTrinhDAO = new LichTrinhDungDoDAO();

    public ChuyenTauController(ChuyenTauPanel view) {
        this.view = view;
        // Tự động nạp dữ liệu chuyến tàu lên bảng bên trái ngay khi ứng dụng khởi chạy
        loadDanhSachChuyenTau();
    }

    /**
     * Tải toàn bộ danh sách chuyến tàu từ database và đẩy lên bảng bên trái (tableChuyenTau)
     */
    public void loadDanhSachChuyenTau() {
        List<ChuyenTau> list = chuyenTauDAO.getAllChuyenTau();
        if (view != null) {
            view.setChuyenTauData(list);
        }
    }

    /**
     * Tải danh sách chặng dừng của một chuyến cụ thể và đẩy lên bảng bên phải (tableLichTrinh)
     * Hàm này kích hoạt khi người dùng click chọn 1 dòng trên bảng chuyến tàu
     */
    public void loadLichTrinhByMaChuyen(String maChuyen) {
        // 1. Lấy danh sách chặng dừng từ Database thông qua lớp DAO
        List<LichTrinhDungDo> list = lichTrinhDAO.getLichTrinhByMaChuyen(maChuyen);
        
        // 2. Kiểm tra và đẩy dữ liệu sang giao diện để vẽ lại bảng bên phải
        if (view != null) {
            view.setLichTrinhData(list);
        }
    }

    /**
     * Gọi sang DAO lấy mã chuyến tàu tự sinh mới nhất kế tiếp (CH01, CH02,...)
     */
    public String layMaChuyenMoi() {
        return chuyenTauDAO.getNextMaChuyen();
    }

    // =========================================================================
    // PHẦN 1: CÁC HÀM XỬ LÝ CHUYẾN TÀU (BẢNG BÊN TRÁI)
    // =========================================================================
    
    /**
     * Xử lý thêm mới một chuyến tàu
     */
    public boolean themChuyenTau(ChuyenTau ct) {
        return chuyenTauDAO.themChuyenTau(ct);
    }

    /**
     * Xử lý cập nhật thông tin chuyến tàu
     */
    public boolean capNhatChuyenTau(ChuyenTau ct) {
        return chuyenTauDAO.capNhatChuyenTau(ct);
    }

    /**
     * Xử lý xóa một chuyến tàu dựa trên mã
     */
    public boolean xoaChuyenTau(String maChuyen) {
        return chuyenTauDAO.xoaChuyenTau(maChuyen);
    }

    // =========================================================================
    // PHẦN 2: CÁC HÀM XỬ LÝ LỊCH TRÌNH DỪNG ĐỖ (BẢNG BÊN PHẢI)
    // =========================================================================
    
    /**
     * Xử lý thêm mới một chặng dừng cho chuyến tàu
     */
    public boolean themLichTrinh(LichTrinhDungDo lt) {
        // Gọi sang hàm thêm của LichTrinhDungDoDAO (Bạn nhớ bổ sung hàm này trong DAO nếu chưa có nhé)
        return lichTrinhDAO.themLichTrinh(lt);
    }

    /**
     * Xử lý sửa thông tin chặng dừng
     */
    public boolean capNhatLichTrinh(LichTrinhDungDo lt) {
        // Gọi sang hàm cập nhật của LichTrinhDungDoDAO
        return lichTrinhDAO.capNhatLichTrinh(lt);
    }

    /**
     * Xử lý xóa một chặng dừng cụ thể của một chuyến tàu
     */
    public boolean xoaLichTrinh(String maChuyen, String maGa) {
        // Gọi sang hàm xóa kết hợp 2 khóa chính (maChuyen, maGa) trong LichTrinhDungDoDAO
        return lichTrinhDAO.xoaLichTrinh(maChuyen, maGa);
    }
}