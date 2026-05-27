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

    // Load danh sách ga từ database
    public void loadDanhSachGa() {
        List<GaTau> dsGa = dao.getAllGa();
        view.loadGaComboBox(dsGa);
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

    // Tìm khách hàng theo CCCD
    public KhachHang timKhachHangTheoCCCD(String cccd) {
        return dao.findKhachHangByCCCD(cccd);
    }

    // Tạo khách hàng mới
    public String taoKhachHangMoi(String tenKH, String cccd, String soDienThoai, String email) {
        return dao.insertKhachHang(tenKH, cccd, soDienThoai, email);
    }

    // Thanh toán với thông tin hành khách
 // Thanh toán với thông tin hành khách
    public void thanhToan(List<VeTau> dsVeChon, String maKH, String maNV, String phuongThuc) {
        if (dsVeChon.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng trống!");
            return;
        }
        
        System.out.println("=== BẮT ĐẦU THANH TOÁN ===");
        System.out.println("Số vé: " + dsVeChon.size());
        System.out.println("Mã KH: " + maKH);
        System.out.println("Mã NV: " + maNV);
        
        // Kiểm tra thông tin vé trước khi thanh toán
        for (VeTau ve : dsVeChon) {
            System.out.println("Vé: Ghế=" + ve.getSoGhe() + ", Giá=" + ve.getGiaGoc() + 
                              ", HK=" + ve.getTenHanhKhach() + ", CCCD=" + ve.getSoCCCD());
        }
        
        HoaDon hd = new HoaDon();
        boolean success = dao.datVe(dsVeChon.get(0).getMaChuyen(), dsVeChon, maKH, maNV, phuongThuc, hd);
        
        if (success) {
            System.out.println("Thanh toán thành công! Mã HD: " + hd.getMaHoaDon());
            JOptionPane.showMessageDialog(view, 
                "✅ Thanh toán thành công!\nMã HD: " + hd.getMaHoaDon() + 
                "\nTổng tiền: " + String.format("%,.0f VND", hd.getTongThanhToan()),
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            xuatHoaDon(hd, dsVeChon);
            view.resetGioHang();
            view.reloadGhe();
        } else {
            System.err.println("Thanh toán thất bại!");
            JOptionPane.showMessageDialog(view, 
                "❌ Thanh toán thất bại!\nVui lòng kiểm tra lại dữ liệu hoặc thử lại sau.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuatHoaDon(HoaDon hd, List<VeTau> dsVe) {
        String fileName = "hoadon_" + hd.getMaHoaDon() + ".txt";
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write("========== HÓA ĐƠN BÁN VÉ TÀU ==========\n");
            fw.write("Mã HD: " + hd.getMaHoaDon() + "\n");
            fw.write("Khách hàng: " + hd.getMaKH() + "\n");
            fw.write("Ngày lập: " + java.time.LocalDateTime.now() + "\n");
            fw.write("Chuyến tàu: " + dsVe.get(0).getMaChuyen() + "\n");
            fw.write("----------------------------------------\n");
            fw.write(String.format("%-10s %-10s %-20s %-15s %-15s\n", "Số ghế", "Loại ghế", "Hành khách", "CCCD", "Giá"));
            for (VeTau v : dsVe) {
                fw.write(String.format("%-10d %-10s %-20s %-15s %-15.0f\n", 
                    v.getSoGhe(), 
                    v.getLoaiGhe(), 
                    v.getTenHanhKhach() != null ? v.getTenHanhKhach() : "",
                    v.getSoCCCD() != null ? v.getSoCCCD() : "",
                    v.getGiaGoc()));
            }
            fw.write("----------------------------------------\n");
            fw.write("Tổng thanh toán: " + hd.getTongThanhToan() + " VND\n");
            fw.write("Cảm ơn quý khách đã sử dụng dịch vụ!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}