package Controller;

import java.util.List;
import DAO.ChuyenTauDAO;
import DAO.LichTrinhDungDoDAO;
import DAO.GaTauDAO;
import DAO.TauDAO;
import Entity.ChuyenTau;
import Entity.LichTrinhDungDo;
import GUI.ChuyenTauPanel;

public class ChuyenTauController {
    private ChuyenTauPanel view;
    private ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private LichTrinhDungDoDAO lichTrinhDAO = new LichTrinhDungDoDAO();
    private GaTauDAO gaTauDAO = new GaTauDAO();
    private TauDAO tauDAO = new TauDAO();

    public ChuyenTauController(ChuyenTauPanel view) {
        this.view = view;
        loadDanhSachChuyenTau();
    }

    public void loadDanhSachChuyenTau() {
        List<ChuyenTau> list = chuyenTauDAO.getAllChuyenTau();
        if (view != null) {
            view.setChuyenTauData(list);
        }
    }

    public void loadLichTrinhByMaChuyen(String maChuyen) {
        List<LichTrinhDungDo> list = lichTrinhDAO.getLichTrinhByMaChuyen(maChuyen);
        if (view != null) {
            view.setLichTrinhData(list);
        }
    }

    public String layMaChuyenMoi() {
        return chuyenTauDAO.getNextMaChuyen();
    }

    public List<String> layDanhSachGaFormat() {
        return gaTauDAO.getDanhSachGaFormat();
    }

    public List<String> layDanhSachTauFormat() {
        return tauDAO.getDanhSachTauFormat();
    }

    // =========================================================================
    // CÁC HÀM CRUD CHUYẾN TÀU (BẢNG BÊN TRÁI)
    // =========================================================================
    
    /**
     * 🔥 THÊM MỚI CHUYẾN TÀU VÀ TỰ ĐỘNG KHỞI TẠO GA ĐẦU / GA CUỐI VÀO BẢNG LỊCH TRÌNH
     */
    public boolean themChuyenTau(ChuyenTau ct) {
        // 1. Thực hiện thêm thông tin chuyến tàu vào bảng ChuyenTau trước
        boolean isInsertChuyenSuccess = chuyenTauDAO.themChuyenTau(ct);
        if (!isInsertChuyenSuccess) {
            return false;
        }

        // 2. Tự động thêm Ga Đi vào lịch trình dừng đỗ (Thứ tự 1, TG Đến = NULL, TG Đi = TG Đi của chuyến)
        LichTrinhDungDo gaDau = new LichTrinhDungDo();
        gaDau.setMaChuyen(ct.getMaChuyen());
        gaDau.setMaGa(ct.getMaGaDi());
        gaDau.setThuTuDung(1);
        gaDau.setThoiGianDen(null); // xuất phát nên không có thời gian đến
        gaDau.setThoiGianDi(ct.getThoiGianDi());
        lichTrinhDAO.themLichTrinh(gaDau);

        // 3. Tự động thêm Ga Đến vào lịch trình dừng đỗ (Thứ tự 2, TG Đến = TG Đến của chuyến, TG Đi = NULL)
        LichTrinhDungDo gaCuoi = new LichTrinhDungDo();
        gaCuoi.setMaChuyen(ct.getMaChuyen());
        gaCuoi.setMaGa(ct.getMaGaDen());
        gaCuoi.setThuTuDung(2);
        gaCuoi.setThoiGianDen(ct.getThoiGianDen());
        gaCuoi.setThoiGianDi(null); // kết thúc hành trình nên không có thời gian đi tiếp
        lichTrinhDAO.themLichTrinh(gaCuoi);

        return true;
    }

    public boolean capNhatChuyenTau(ChuyenTau ct) {
        return chuyenTauDAO.capNhatChuyenTau(ct);
    }

    public boolean xoaChuyenTau(String maChuyen) {
        return chuyenTauDAO.xoaChuyenTau(maChuyen);
    }

    // =========================================================================
    // CÁC HÀM CRUD LỊCH TRÌNH DỪNG ĐỖ (BẢNG BÊN PHẢI)
    // =========================================================================
    public boolean themLichTrinh(LichTrinhDungDo lt) {
        return lichTrinhDAO.themLichTrinh(lt);
    }

    public boolean capNhatLichTrinh(LichTrinhDungDo lt) {
        return lichTrinhDAO.capNhatLichTrinh(lt);
    }

    public boolean capNhatLichTrinhNangCao(LichTrinhDungDo lt, String maGaCu) {
        return lichTrinhDAO.capNhatLichTrinhNangCao(lt, maGaCu);
    }

    public boolean xoaLichTrinh(String maChuyen, String maGa) {
        return lichTrinhDAO.xoaLichTrinh(maChuyen, maGa);
    }
}