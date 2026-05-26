package Controller;

import DAO.ChuyenTauDAO;
import DAO.LichTrinhDungDoDAO;
import Entity.ChuyenTau;
import Entity.LichTrinhDungDo;
import GUI.ChuyenTauPanel;

import java.util.List;

public class ChuyenTauController {
    
    private ChuyenTauPanel view;
    private ChuyenTauDAO chuyenTauDAO;
    private LichTrinhDungDoDAO lichTrinhDAO;
    
    public ChuyenTauController(ChuyenTauPanel view) {
        this.view = view;
        this.chuyenTauDAO = new ChuyenTauDAO();
        this.lichTrinhDAO = new LichTrinhDungDoDAO();
        
        // Gắn controller vào view để view có thể gọi ngược lại các sự kiện
        this.view.setController(this);
        
        // Tải danh sách chuyến tàu lên bảng bên trái ngay khi mở form
        loadDanhSachChuyenTau();
    }

    // ================= XỬ LÝ CHUYẾN TÀU (BẢNG TRÁI) =================

    public void loadDanhSachChuyenTau() {
        List<ChuyenTau> list = chuyenTauDAO.getAll();
        view.setChuyenTauData(list); 
    }
    
    public boolean themChuyenTau(ChuyenTau ct) {
        boolean result = chuyenTauDAO.insert(ct);
        if (result) {
            System.out.println("Thêm chuyến tàu thành công: " + ct.getMaChuyen());
        }
        return result;
    }
    
    public boolean capNhatChuyenTau(ChuyenTau ct) {
        return chuyenTauDAO.update(ct);
    }
    
    public boolean xoaChuyenTau(String maChuyen) {
        return chuyenTauDAO.delete(maChuyen);
    }

    // ================= XỬ LÝ LỊCH TRÌNH DỪNG ĐỖ (BẢNG PHẢI) =================

    // Hàm này sẽ được view gọi khi người dùng click vào 1 dòng ở bảng bên trái
    public void loadLichTrinhByMaChuyen(String maChuyen) {
        List<LichTrinhDungDo> list = lichTrinhDAO.getByMaChuyen(maChuyen);
        view.setLichTrinhData(list); 
    }

    public boolean themLichTrinh(LichTrinhDungDo lt) {
        boolean result = lichTrinhDAO.insert(lt);
        if (result) {
            System.out.println("Thêm lịch trình dừng đỗ thành công!");
        }
        return result;
    }

    public boolean capNhatLichTrinh(LichTrinhDungDo lt) {
        return lichTrinhDAO.update(lt);
    }

    public boolean xoaLichTrinh(String maChuyen, String maGa) {
        return lichTrinhDAO.delete(maChuyen, maGa);
    }
}