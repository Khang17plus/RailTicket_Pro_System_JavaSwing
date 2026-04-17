package Entity;

public class PhanCong {
    private String maPhanCong;
    private String maNV;
    private String maChuyen;
    private String vaiTro;

    public PhanCong() {}

    public PhanCong(String maPhanCong, String maNV, String maChuyen, String vaiTro) {
        this.maPhanCong = maPhanCong;
        this.maNV = maNV;
        this.maChuyen = maChuyen;
        this.vaiTro = vaiTro;
    }

    public String getMaPhanCong() { return maPhanCong; }
    public void setMaPhanCong(String maPhanCong) { this.maPhanCong = maPhanCong; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMaChuyen() { return maChuyen; }
    public void setMaChuyen(String maChuyen) { this.maChuyen = maChuyen; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    @Override
    public String toString() {
        return "PhanCong{" + "maPhanCong='" + maPhanCong + '\'' + ", maNV='" + maNV + '\'' + ", maChuyen='" + maChuyen + '\'' + ", vaiTro='" + vaiTro + '\'' + '}';
    }
}