package Entity;

public class Tau {
    private String maTau;
    private String tenTau;
    private String loaiTau;
    private String trangThai;

    public Tau() {}

    public Tau(String maTau, String tenTau, String loaiTau, String trangThai) {
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.loaiTau = loaiTau;
        this.trangThai = trangThai;
    }

    public String getMaTau() { return maTau; }
    public void setMaTau(String maTau) { this.maTau = maTau; }

    public String getTenTau() { return tenTau; }
    public void setTenTau(String tenTau) { this.tenTau = tenTau; }

    public String getLoaiTau() { return loaiTau; }
    public void setLoaiTau(String loaiTau) { this.loaiTau = loaiTau; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "Tau{" + "maTau='" + maTau + '\'' + ", tenTau='" + tenTau + '\'' + ", loaiTau='" + loaiTau + '\'' + ", trangThai='" + trangThai + '\'' + '}';
    }
}