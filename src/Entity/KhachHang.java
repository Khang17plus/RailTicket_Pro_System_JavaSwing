package Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String cccd;
    private String soDienThoai;
    private String email;
    private LocalDateTime ngayDangKy;
   
    public KhachHang() {
       
    }

    // Constructor đầy đủ
    public KhachHang(String maKH, String tenKH, String cccd, String soDienThoai, String email, LocalDateTime ngayDangKy) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.cccd = cccd;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngayDangKy = ngayDangKy;
    }

    // Getter & Setter
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDateTime ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    // toString (debug cho dễ)
    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", cccd='" + cccd + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", ngayDangKy=" + ngayDangKy +
                '}';
    }
}