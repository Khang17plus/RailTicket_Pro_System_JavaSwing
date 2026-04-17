package Entity;
import java.time.LocalDateTime;

public class HoaDon {
    private String maHoaDon;
    private String maKH;
    private String maNV;
    private LocalDateTime ngayLap;
    private double tongTienHang;
    private double tongThue;
    private double tongGiamGia;
    private double tongThanhToan;
    private String phuongThucThanhToan;

    public HoaDon() {}

    public HoaDon(String maHoaDon, String maKH, String maNV, LocalDateTime ngayLap, double tongTienHang, double tongThue, double tongGiamGia, double tongThanhToan, String phuongThucThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLap = ngayLap;
        this.tongTienHang = tongTienHang;
        this.tongThue = tongThue;
        this.tongGiamGia = tongGiamGia;
        this.tongThanhToan = tongThanhToan;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }

    public double getTongTienHang() { return tongTienHang; }
    public void setTongTienHang(double tongTienHang) { this.tongTienHang = tongTienHang; }

    public double getTongThue() { return tongThue; }
    public void setTongThue(double tongThue) { this.tongThue = tongThue; }

    public double getTongGiamGia() { return tongGiamGia; }
    public void setTongGiamGia(double tongGiamGia) { this.tongGiamGia = tongGiamGia; }

    public double getTongThanhToan() { return tongThanhToan; }
    public void setTongThanhToan(double tongThanhToan) { this.tongThanhToan = tongThanhToan; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    @Override
    public String toString() {
        return "HoaDon{" + "maHoaDon='" + maHoaDon + '\'' + ", maKH='" + maKH + '\'' + ", maNV='" + maNV + '\'' + ", ngayLap=" + ngayLap + ", tongTienHang=" + tongTienHang + ", tongThue=" + tongThue + ", tongGiamGia=" + tongGiamGia + ", tongThanhToan=" + tongThanhToan + ", phuongThucThanhToan='" + phuongThucThanhToan + '\'' + '}';
    }
}