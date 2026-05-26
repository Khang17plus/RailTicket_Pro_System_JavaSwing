package Controller;

import DAO.TauDAO;
import DAO.ToaTauDAO;
import Entity.Ghe;
import Entity.Tau;
import Entity.ToaTau; // Đã đổi sang ToaTau của bạn
import GUI.TauVaToaPanel;
import DAO.GheDAO;

import java.util.ArrayList;
import java.util.List;
public class TauVaToaController {
    
    private TauVaToaPanel view;
    private TauDAO tauDAO;
    private ToaTauDAO toaTauDAO;
    private GheDAO gheDAO;

    // Khởi tạo Controller và móc nối với View
    public TauVaToaController(TauVaToaPanel view) {
        this.view = view;
        this.tauDAO = new TauDAO();
        this.toaTauDAO = new ToaTauDAO();
        this.gheDAO = new GheDAO(); 
    }
    public boolean capNhatTrangThai(String maTau, String trangThaiMoi) {
        Tau tau = tauDAO.findById(maTau);
        if (tau != null) {
            tau.setTrangThai(trangThaiMoi);
            return tauDAO.update(tau);
        }
        return false;
    }
    
 // Sinh mã toa tự động (dạng TOA001, TOA002,...)
    public String generateNextMaToa(String maTau) {
        String maxMa = toaTauDAO.getMaxMaToa(maTau);
        int nextNumber = 1;
        if (maxMa != null && !maxMa.isEmpty()) {
            int toaIndex = maxMa.indexOf("_TOA");
            if (toaIndex != -1) {
                String numberPart = maxMa.substring(toaIndex + 4);
                try {
                    nextNumber = Integer.parseInt(numberPart) + 1;
                } catch (NumberFormatException e) {
                    nextNumber = 1;
                }
            }
        }
        return maTau + "_TOA" + String.format("%03d", nextNumber);
    }

    // Thêm toa mới
    public boolean themToa(ToaTau toa) {
        boolean success = toaTauDAO.insert(toa);
        if (success) {
            // Sau khi thêm, refresh lại danh sách toa của tàu đang chọn
            if (view != null && toa.getMaTau() != null) {
                chonTau(toa.getMaTau());
            }
        }
        return success;
    }
    
    public String generateNextMaTau() {
        // Giả sử bạn có một method trong DAO: String getMaxMaTau()
        String maxMa = tauDAO.getMaxMaTau();  // trả về "T123" hoặc null nếu chưa có tàu nào

        if (maxMa == null || maxMa.isEmpty()) {
            return "T001";
        }

        // Lấy phần số sau chữ 'T'
        String numberPart = maxMa.substring(1);  // bỏ ký tự 'T'
        int currentNumber;
        try {
            currentNumber = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            // Nếu mã không đúng định dạng, xử lý mặc định
            currentNumber = 0;
        }

        int nextNumber = currentNumber + 1;
        // Tuỳ chọn format: giữ 3 chữ số cho đến 999, sau đó chuyển sang không padding
        if (nextNumber < 1000) {
            return String.format("T%03d", nextNumber);
        } else {
        	
        	
            return "T" + nextNumber;
        }
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
    
    public boolean phatSinhGheChoToa(String maToa) {
        ToaTau toa = toaTauDAO.findById(maToa);
        if (toa == null) return false;
        int sucChua = toa.getSucChua();

        // Xóa ghế cũ
        gheDAO.deleteByMaToa(maToa);

        List<Ghe> dsGheMoi = new ArrayList<>();
        for (int i = 1; i <= sucChua; i++) {
            Ghe g = new Ghe();
            g.setMaGhe(maToa + "_G" + String.format("%03d", i));
            g.setMaToa(maToa);
            g.setSoGhe(i);
            g.setLoaiGhe("Ghế ngồi");
            dsGheMoi.add(g);
        }
        return gheDAO.insertBatch(dsGheMoi);
    }
    public boolean themGheLe(String maToa, int soGhe, String loaiGhe) {
        if (gheDAO.existsByMaToaAndSoGhe(maToa, soGhe)) return false;

        ToaTau toa = toaTauDAO.findById(maToa);
        if (toa != null && gheDAO.countByMaToa(maToa) >= toa.getSucChua()) return false;

        Ghe ghe = new Ghe();
        String maGhe = gheDAO.generateNextMaGhe(maToa);
        ghe.setMaGhe(maGhe);
        ghe.setMaToa(maToa);
        ghe.setSoGhe(soGhe);
        ghe.setLoaiGhe(loaiGhe != null && !loaiGhe.isEmpty() ? loaiGhe : "Ghế ngồi");
        return gheDAO.insert(ghe);
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