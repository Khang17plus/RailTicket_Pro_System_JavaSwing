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
    private TauDAO tauDAO = new TauDAO(); // ĐÃ THÊM

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

    /**
     * Gọi sang GaTauDAO lấy danh sách Ga động từ DB
     */
    public List<String> layDanhSachGaFormat() {
        return gaTauDAO.getDanhSachGaFormat();
    }

    /**
     * Gọi sang TauDAO lấy danh sách Tàu động từ DB
     */
    public List<String> layDanhSachTauFormat() {
        return tauDAO.getDanhSachTauFormat();
    }

    // =========================================================================
    // CÁC HÀM CRUD CHUYẾN TÀU
    // =========================================================================
    public boolean themChuyenTau(ChuyenTau ct) {
        return chuyenTauDAO.themChuyenTau(ct);
    }

    public boolean capNhatChuyenTau(ChuyenTau ct) {
        return chuyenTauDAO.capNhatChuyenTau(ct);
    }

    public boolean xoaChuyenTau(String maChuyen) {
        return chuyenTauDAO.xoaChuyenTau(maChuyen);
    }

    // =========================================================================
    // CÁC HÀM CRUD LỊCH TRÌNH DỪNG ĐỖ
    // =========================================================================
    public boolean themLichTrinh(LichTrinhDungDo lt) {
        return lichTrinhDAO.themLichTrinh(lt);
    }

    public boolean capNhatLichTrinh(LichTrinhDungDo lt) {
        return lichTrinhDAO.capNhatLichTrinh(lt);
    }

    public boolean xoaLichTrinh(String maChuyen, String maGa) {
        return lichTrinhDAO.xoaLichTrinh(maChuyen, maGa);
    }
}