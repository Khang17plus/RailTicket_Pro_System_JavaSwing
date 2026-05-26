package Entity;

import java.time.LocalDateTime;

public class ChuyenTau {
    private String maChuyen;
    private String maTau;
    private String tenTau;
    private String maGaDi;
    private String tenGaDi;
    private String maGaDen;
    private String tenGaDen;
    private LocalDateTime thoiGianDi;
    private LocalDateTime thoiGianDen;
    private String trangThai;

    public ChuyenTau() {}

    public ChuyenTau(String maChuyen, String maTau, String tenTau, String maGaDi, String tenGaDi, String maGaDen, String tenGaDen, LocalDateTime thoiGianDi, LocalDateTime thoiGianDen, String trangThai) {
        this.maChuyen = maChuyen;
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.maGaDi = maGaDi;
        this.tenGaDi = tenGaDi;
        this.maGaDen = maGaDen;
        this.tenGaDen = tenGaDen;
        this.thoiGianDi = thoiGianDi;
        this.thoiGianDen = thoiGianDen;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaChuyen() { return maChuyen; }
    public void setMaChuyen(String maChuyen) { this.maChuyen = maChuyen; }
    public String getMaTau() { return maTau; }
    public void setMaTau(String maTau) { this.maTau = maTau; }
    public String getTenTau() { return tenTau; }
    public void setTenTau(String tenTau) { this.tenTau = tenTau; }
    public String getMaGaDi() { return maGaDi; }
    public void setMaGaDi(String maGaDi) { this.maGaDi = maGaDi; }
    public String getTenGaDi() { return tenGaDi; }
    public void setTenGaDi(String tenGaDi) { this.tenGaDi = tenGaDi; }
    public String getMaGaDen() { return maGaDen; }
    public void setMaGaDen(String maGaDen) { this.maGaDen = maGaDen; }
    public String getTenGaDen() { return tenGaDen; }
    public void setTenGaDen(String tenGaDen) { this.tenGaDen = tenGaDen; }
    public LocalDateTime getThoiGianDi() { return thoiGianDi; }
    public void setThoiGianDi(LocalDateTime thoiGianDi) { this.thoiGianDi = thoiGianDi; }
    public LocalDateTime getThoiGianDen() { return thoiGianDen; }
    public void setThoiGianDen(LocalDateTime thoiGianDen) { this.thoiGianDen = thoiGianDen; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}