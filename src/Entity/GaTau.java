package Entity;

public class GaTau {
    private String maGa;
    private String tenGa;
    private String diaChi;

    public GaTau() {}

    public GaTau(String maGa, String tenGa, String diaChi) {
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.diaChi = diaChi;
    }

    public String getMaGa() { return maGa; }
    public void setMaGa(String maGa) { this.maGa = maGa; }

    public String getTenGa() { return tenGa; }
    public void setTenGa(String tenGa) { this.tenGa = tenGa; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    @Override
    public String toString() {
        return "GaTau{" + "maGa='" + maGa + '\'' + ", tenGa='" + tenGa + '\'' + ", diaChi='" + diaChi + '\'' + '}';
    }
}