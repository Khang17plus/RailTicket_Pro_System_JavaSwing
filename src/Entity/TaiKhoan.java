package Entity;

public class TaiKhoan {
    private String maNV;
    private String matKhau;
    private String vaiTro;

    public TaiKhoan() {}

    public TaiKhoan(String maNV, String matKhau, String vaiTro) {
        this.maNV = maNV;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    @Override
    public String toString() {
        return "TaiKhoan{" + "maNV='" + maNV + '\'' + ", matKhau='" + matKhau + '\'' + ", vaiTro='" + vaiTro + '\'' + '}';
    }
}