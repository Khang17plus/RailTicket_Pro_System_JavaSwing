package Controller;

import DAO.TauDAO;
import DAO.ToaTauDAO;
import Entity.Tau;
import Entity.ToaTau; // Đã đổi sang ToaTau của bạn
import GUI.TauVaToaPanel;

import java.util.List;

public class TauVaToaController {
    
    private TauVaToaPanel view;
    private TauDAO tauDAO;
    private ToaTauDAO toaTauDAO;

    // Khởi tạo Controller và móc nối với View
    public TauVaToaController(TauVaToaPanel view) {
        this.view = view;
        this.tauDAO = new TauDAO();
        this.toaTauDAO = new ToaTauDAO();
    }

    // 1. Load toàn bộ danh sách tàu lên JList (Gọi khi khởi tạo)
    public void loadDanhSachTau() {
        List<Tau> listTau = tauDAO.getAll(); // Gọi thẳng xuống DB
        view.hienThiDanhSachTau(listTau);
    }

    // 2. Sự kiện khi người dùng click vào 1 dòng Tàu bên Panel
    public void chonTau(String maTau) {
        // Lấy chi tiết chiếc Tàu từ DB
        Tau selectedTau = tauDAO.findById(maTau);
        
        if (selectedTau != null) {
            // Cập nhật Form bên View
            view.hienThiChiTietTau(selectedTau);
            
            // Lấy danh sách Toa thuộc về Tàu này từ DB
            List<ToaTau> danhSachToa = toaTauDAO.findByMaTau(maTau);
            view.hienThiDanhSachToa(danhSachToa);
        }
    }

    // =================================================================
    // CÁC HÀM XỬ LÝ THÊM/SỬA/XÓA TÀU (Để bạn gọi khi bấm nút trên giao diện)
    // =================================================================

    public boolean themTau(Tau tau) {
        boolean success = tauDAO.insert(tau);
        if (success) {
            loadDanhSachTau(); // Load lại list sau khi thêm thành công
        }
        return success;
    }

    public boolean suaTau(Tau tau) {
        boolean success = tauDAO.update(tau);
        if (success) {
            loadDanhSachTau(); 
        }
        return success;
    }

    public boolean xoaTau(String maTau) {
        // Lưu ý: Nếu Tàu đang có Toa thì DB có thể chặn xóa (Ràng buộc khóa ngoại)
        // Bạn nên check và xóa Toa trước nếu cần.
        boolean success = tauDAO.delete(maTau);
        if (success) {
            loadDanhSachTau();
        }
        return success;
    }
}