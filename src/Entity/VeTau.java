package Entity;

public class VeTau {
    private String maVe;
    private String maChuyen;
    private String maGhe;
    private double giaGoc;
    private String trangThai;

    public VeTau() {}

    public VeTau(String maVe, String maChuyen, String maGhe, double giaGoc, String trangThai) {
        this.maVe = maVe;
        this.maChuyen = maChuyen;
        this.maGhe = maGhe;
        this.giaGoc = giaGoc;
        this.trangThai = trangThai;
    }

    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }

    public String getMaChuyen() { return maChuyen; }
    public void setMaChuyen(String maChuyen) { this.maChuyen = maChuyen; }

    public String getMaGhe() { return maGhe; }
    public void setMaGhe(String maGhe) { this.maGhe = maGhe; }

    public double getGiaGoc() { return giaGoc; }
    public void setGiaGoc(double giaGoc) { this.giaGoc = giaGoc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return "VeTau{" + "maVe='" + maVe + '\'' + ", maChuyen='" + maChuyen + '\'' + ", maGhe='" + maGhe + '\'' + ", giaGoc=" + giaGoc + ", trangThai='" + trangThai + '\'' + '}';
    }
}