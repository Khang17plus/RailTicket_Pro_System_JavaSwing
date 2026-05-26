package Controller;

import DAO.BanVeDAO;
import Entity.*;
import GUI.BanVePanel;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

public class BanVeController {
    private BanVePanel view;
    private BanVeDAO dao;

    public BanVeController(BanVePanel view) {
        this.view = view;
        this.dao = new BanVeDAO();
        this.view.setController(this);
    }

    public void timKiemChuyen(String maGaDi, String maGaDen, LocalDate ngayDi) {
        if (maGaDi.equals(maGaDen)) {
            JOptionPane.showMessageDialog(view, "Ga đi và ga đến không được trùng nhau!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<ChuyenTau> dsChuyen = dao.timChuyenTheoGaVaNgay(maGaDi, maGaDen, ngayDi);
        view.hienThiDanhSachChuyen(dsChuyen);
    }

    public void loadToaByChuyen(ChuyenTau chuyen) {
        List<ToaTau> dsToa = dao.getToaByMaTau(chuyen.getMaTau());
        view.hienThiDanhSachToa(dsToa);
    }

    public void loadGheByToa(String maToa, String maChuyen) {
        List<VeTau> dsVe = dao.getVeByChuyenVaToa(maChuyen, maToa);
        view.hienThiSoDoGhe(dsVe, maChuyen, maToa);
    }

    public void thanhToan(List<VeTau> dsVeChon, String maKH, String maNV, String phuongThuc) {
        if (dsVeChon.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng trống!");
            return;
        }
        HoaDon hd = new HoaDon();
        boolean success = dao.datVe(dsVeChon.get(0).getMaChuyen(), dsVeChon, maKH, maNV, phuongThuc, hd);
        
        if (success) {
            JOptionPane.showMessageDialog(view, "Thanh toán thành công! Mã HD: " + hd.getMaHoaDon());
            xuatHoaDon(hd, dsVeChon);
            view.resetGioHang();
            view.reloadGhe();
        } else {
            JOptionPane.showMessageDialog(view, "Thanh toán thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuatHoaDon(HoaDon hd, List<VeTau> dsVe) {
        String fileName = "hoadon_" + hd.getMaHoaDon() + ".txt";
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write("========== HÓA ĐƠN BÁN VÉ TÀU ==========\n");
            fw.write("Mã HD: " + hd.getMaHoaDon() + "\n");
            fw.write("Khách hàng: " + hd.getMaKH() + "\n");
            fw.write("Chuyến tàu: " + dsVe.get(0).getMaChuyen() + "\n");
            fw.write("----------------------------------------\n");
            fw.write(String.format("%-10s %-10s %-15s\n", "Mã ghế", "Số ghế", "Giá"));
            for (VeTau v : dsVe) {
                fw.write(String.format("%-10s %-10d %-15.0f\n", v.getMaGhe(), v.getSoGhe(), v.getGiaGoc()));
            }
            fw.write("----------------------------------------\n");
            fw.write("Tổng thanh toán: " + hd.getTongThanhToan() + " VND\n");
            fw.write("Cảm ơn quý khách đã sử dụng dịch vụ!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}