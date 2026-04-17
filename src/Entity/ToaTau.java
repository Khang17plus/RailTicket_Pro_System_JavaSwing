package Entity;

public class ToaTau {
    private String maToa;
    private String maTau;
    private String tenToa;
    private String loaiToa;
    private int sucChua;

    public ToaTau() {}

    public ToaTau(String maToa, String maTau, String tenToa, String loaiToa, int sucChua) {
        this.maToa = maToa;
        this.maTau = maTau;
        this.tenToa = tenToa;
        this.loaiToa = loaiToa;
        this.sucChua = sucChua;
    }

    public String getMaToa() { return maToa; }
    public void setMaToa(String maToa) { this.maToa = maToa; }

    public String getMaTau() { return maTau; }
    public void setMaTau(String maTau) { this.maTau = maTau; }

    public String getTenToa() { return tenToa; }
    public void setTenToa(String tenToa) { this.tenToa = tenToa; }

    public String getLoaiToa() { return loaiToa; }
    public void setLoaiToa(String loaiToa) { this.loaiToa = loaiToa; }

    public int getSucChua() { return sucChua; }
    public void setSucChua(int sucChua) { this.sucChua = sucChua; }

    @Override
    public String toString() {
        return "ToaTau{" + "maToa='" + maToa + '\'' + ", maTau='" + maTau + '\'' + ", tenToa='" + tenToa + '\'' + ", loaiToa='" + loaiToa + '\'' + ", sucChua=" + sucChua + '}';
    }
}