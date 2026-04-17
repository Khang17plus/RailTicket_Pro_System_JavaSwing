package Entity;
import java.time.LocalDateTime;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private String loaiKM;
    private double giaTri;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private boolean trangThai;

    public KhuyenMai() {}

    public KhuyenMai(String maKM, String tenKM, String loaiKM, double giaTri, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, boolean trangThai) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.loaiKM = loaiKM;
        this.giaTri = giaTri;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    // Các getter setter tương tự...
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }

    public String getLoaiKM() { return loaiKM; }
    public void setLoaiKM(String loaiKM) { this.loaiKM = loaiKM; }

    public double getGiaTri() { return giaTri; }
    public void setGiaTri(double giaTri) { this.giaTri = giaTri; }

    public LocalDateTime getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDateTime ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDateTime getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDateTime ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "KhuyenMai{" + "maKM='" + maKM + '\'' + ", tenKM='" + tenKM + '\'' + ", loaiKM='" + loaiKM + '\'' + ", giaTri=" + giaTri + ", ngayBatDau=" + ngayBatDau + ", ngayKetThuc=" + ngayKetThuc + ", trangThai=" + trangThai + '}';
    }
}