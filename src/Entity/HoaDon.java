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
    
    // Constructor
    public HoaDon() {}
    
    // Getters và Setters
    public String getMaHoaDon() {
        return maHoaDon;
    }
    
    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }
    
    public String getMaNV() {
        return maNV;
    }
    
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    
    public LocalDateTime getNgayLap() {
        return ngayLap;
    }
    
    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }
    
    public double getTongTienHang() {
        return tongTienHang;
    }
    
    public void setTongTienHang(double tongTienHang) {
        this.tongTienHang = tongTienHang;
    }
    
    public double getTongThue() {
        return tongThue;
    }
    
    public void setTongThue(double tongThue) {
        this.tongThue = tongThue;
    }
    
    public double getTongGiamGia() {
        return tongGiamGia;
    }
    
    public void setTongGiamGia(double tongGiamGia) {
        this.tongGiamGia = tongGiamGia;
    }
    
    public double getTongThanhToan() {
        return tongThanhToan;
    }
    
    public void setTongThanhToan(double tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }
    
    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }
    
    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }
}