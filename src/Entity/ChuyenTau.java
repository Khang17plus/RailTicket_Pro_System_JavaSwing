package Entity;
import java.time.LocalDateTime;

public class ChuyenTau {
    private String maChuyen;
    private String maTau;
    private String maGaDi;
    private String maGaDen;
    private LocalDateTime thoiGianDi;
    private LocalDateTime thoiGianDen;
    private String trangThai;

    public ChuyenTau() {}

    public ChuyenTau(String maChuyen, String maTau, String maGaDi, String maGaDen, LocalDateTime thoiGianDi, LocalDateTime thoiGianDen, String trangThai) {
        this.maChuyen = maChuyen;
        this.maTau = maTau;
        this.maGaDi = maGaDi;
        this.maGaDen = maGaDen;
        this.thoiGianDi = thoiGianDi;
        this.thoiGianDen = thoiGianDen;
        this.trangThai = trangThai;
    }

    public String getMaChuyen() { return maChuyen; }
    public void setMaChuyen(String maChuyen) { this.maChuyen = maChuyen; }

    public String getMaTau() { return maTau; }
    public void setMaTau(String maTau) { this.maTau = maTau; }

    public String getMaGaDi() { return maGaDi; }
    public void setMaGaDi(String maGaDi) { this.maGaDi = maGaDi; }

    public String getMaGaDen() { return maGaDen; }
    public void setMaGaDen(String maGaDen) { this.maGaDen = maGaDen; }

    public LocalDateTime getThoiGianDi() { return thoiGianDi; }
    public void setThoiGianDi(LocalDateTime thoiGianDi) { this.thoiGianDi = thoiGianDi; }

    public LocalDateTime getThoiGianDen() { return thoiGianDen; }
    public void setThoiGianDen(LocalDateTime thoiGianDen) { this.thoiGianDen = thoiGianDen; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "ChuyenTau{" + "maChuyen='" + maChuyen + '\'' + ", maTau='" + maTau + '\'' + ", maGaDi='" + maGaDi + '\'' + ", maGaDen='" + maGaDen + '\'' + ", thoiGianDi=" + thoiGianDi + ", thoiGianDen=" + thoiGianDen + ", trangThai='" + trangThai + '\'' + '}';
    }
}