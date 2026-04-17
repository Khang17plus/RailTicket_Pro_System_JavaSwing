package Entity;
import java.time.LocalDateTime;

public class Thue {
    private String maThue;
    private String tenThue;
    private double phanTram;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private boolean trangThai;

    public Thue() {}

    public Thue(String maThue, String tenThue, double phanTram, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, boolean trangThai) {
        this.maThue = maThue;
        this.tenThue = tenThue;
        this.phanTram = phanTram;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
    }

    public String getMaThue() { return maThue; }
    public void setMaThue(String maThue) { this.maThue = maThue; }

    public String getTenThue() { return tenThue; }
    public void setTenThue(String tenThue) { this.tenThue = tenThue; }

    public double getPhanTram() { return phanTram; }
    public void setPhanTram(double phanTram) { this.phanTram = phanTram; }

    public LocalDateTime getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDateTime ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDateTime getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDateTime ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "Thue{" + "maThue='" + maThue + '\'' + ", tenThue='" + tenThue + '\'' + ", phanTram=" + phanTram + ", ngayBatDau=" + ngayBatDau + ", ngayKetThuc=" + ngayKetThuc + ", trangThai=" + trangThai + '}';
    }
}