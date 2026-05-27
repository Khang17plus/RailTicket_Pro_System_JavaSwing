package Controller;

import DAO.*;
import Entity.*;
import GUI.BanVePanel;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class BanVeController {
    private BanVePanel view;
    private BanVeDAO dao = new BanVeDAO();
    private KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
    private ThueDAO thueDAO = new ThueDAO();

    public BanVeController(BanVePanel view) {
        this.view = view;
        view.setController(this);
    }

    public void loadDanhSachGa() {
        view.loadGaComboBox(dao.getAllGa());
    }

    public void timKiemChuyen(String gaDi, String gaDen, LocalDate date) {
        view.hienThiDanhSachChuyen(dao.timChuyenTheoGaVaNgay(gaDi, gaDen, date));
    }

    public void loadToaByChuyen(ChuyenTau ct) {
        view.hienThiDanhSachToa(dao.getToaByMaTau(ct.getMaTau()));
    }

    public void loadGheByToa(String maToa, String maChuyen) {
        view.hienThiSoDoGhe(dao.getVeByChuyenVaToa(maChuyen, maToa), maChuyen, maToa);
    }

    public KhachHang timKhachHangTheoCCCD(String cccd) {
        return dao.findKhachHangByCCCD(cccd);
    }

    public String taoKhachHangMoi(String ten, String cccd, String sdt, String email) {
        return dao.insertKhachHang(ten, cccd, sdt, email);
    }

    public List<KhuyenMai> getActiveKhuyenMai() {
        List<KhuyenMai> all = kmDAO.getAll();
        List<KhuyenMai> active = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (KhuyenMai km : all) {
            // Kiểm tra điều kiện thời gian của chiến dịch khuyến mãi
            if (km.isTrangThai() && km.getNgayBatDau() != null && km.getNgayKetThuc() != null
                    && !now.isBefore(km.getNgayBatDau()) && !now.isAfter(km.getNgayKetThuc())) {
                active.add(km);
            }
        }
        return active;
    }

    public List<Thue> getActiveThue() {
        List<Thue> all = thueDAO.getAll();
        List<Thue> active = new ArrayList<>();
        for (Thue t : all) {
            if (t.isTrangThai()) {
                active.add(t);
            }
        }
        return active;
    }

    // Hàm nhận xử lý thanh toán 8 tham số đồng bộ trực tiếp từ BanVePanel
    public void thanhToan(List<VeTau> dsVe, String maKH, String maNV, String pt,
                          String maKM, String maThue, double giam, double thue) {
        if (dsVe.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Giỏ hàng trống!");
            return;
        }
        
        HoaDon hd = new HoaDon();
        boolean success = dao.datVe(dsVe.get(0).getMaChuyen(), dsVe, maKH, maNV, pt,
                maKM, maThue, giam, thue, hd);
                
        if (success) {
            JOptionPane.showMessageDialog(view, "✅ Đặt vé & Thanh toán thành công!\nMã hóa đơn: " + hd.getMaHoaDon());
            xuatHoaDonTxt(hd, dsVe);
            view.resetGioHang();
            view.reloadGhe();
        } else {
            JOptionPane.showMessageDialog(view, "❌ Thanh toán thất bại! Ghế có thể vừa bị giữ chỗ.", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm xuất tệp tin hóa đơn văn bản dạng .txt để đối chiếu dữ liệu nhanh
    private void xuatHoaDonTxt(HoaDon hd, List<VeTau> dsVe) {
        String fileName = "hoadon_" + hd.getMaHoaDon() + ".txt";
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write("========== HÓA ĐƠN BÁN VÉ TÀU ==========\n");
            fw.write("Mã HD: " + hd.getMaHoaDon() + "\n");
            fw.write("Khách hàng (Mã): " + hd.getMaKH() + "\n");
            fw.write("Ngày lập: " + LocalDateTime.now() + "\n");
            fw.write("Chuyến tàu: " + dsVe.get(0).getMaChuyen() + "\n");
            fw.write("Tổng tiền hàng: " + String.format("%,.0f VND\n", hd.getTongTienHang()));
            fw.write("Giảm giá: " + String.format("%,.0f VND\n", hd.getTongGiamGia()));
            fw.write("Thuế: " + String.format("%,.0f VND\n", hd.getTongThue()));
            fw.write("Tổng thanh toán: " + String.format("%,.0f VND\n", hd.getTongThanhToan()));
            fw.write("----------------------------------------\n");
            fw.write(String.format("%-10s %-10s %-20s %-15s %-15s\n", "Số ghế", "Loại ghế", "Hành khách", "CCCD", "Giá"));
            for (VeTau v : dsVe) {
                fw.write(String.format("%-10d %-10s %-20s %-15s %-15.0f\n",
                        v.getSoGhe(),
                        v.getLoaiGhe() != null ? v.getLoaiGhe() : "Ghế",
                        v.getTenHanhKhach() != null ? v.getTenHanhKhach() : "",
                        v.getSoCCCD() != null ? v.getSoCCCD() : "",
                        v.getGiaGoc()));
            }
            fw.write("----------------------------------------\n");
            fw.write("Phương thức: " + hd.getPhuongThucThanhToan() + "\n");
            fw.write("Cảm ơn quý khách đã tin dùng dịch vụ!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}