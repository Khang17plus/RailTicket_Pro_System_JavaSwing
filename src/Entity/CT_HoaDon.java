package Entity;

public class CT_HoaDon {
    private String maHoaDon;
    private String maVe;
    private String maThue;
    private String maKM;
    private double giaBanThucTe;
    private double tienThue;
    private double tienGiamGia;
    private double thanhTien;

    public CT_HoaDon() {}

    public CT_HoaDon(String maHoaDon, String maVe, String maThue, String maKM, double giaBanThucTe, double tienThue, double tienGiamGia, double thanhTien) {
        this.maHoaDon = maHoaDon;
        this.maVe = maVe;
        this.maThue = maThue;
        this.maKM = maKM;
        this.giaBanThucTe = giaBanThucTe;
        this.tienThue = tienThue;
        this.tienGiamGia = tienGiamGia;
        this.thanhTien = thanhTien;
    }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }

    public String getMaThue() { return maThue; }
    public void setMaThue(String maThue) { this.maThue = maThue; }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public double getGiaBanThucTe() { return giaBanThucTe; }
    public void setGiaBanThucTe(double giaBanThucTe) { this.giaBanThucTe = giaBanThucTe; }

    public double getTienThue() { return tienThue; }
    public void setTienThue(double tienThue) { this.tienThue = tienThue; }

    public double getTienGiamGia() { return tienGiamGia; }
    public void setTienGiamGia(double tienGiamGia) { this.tienGiamGia = tienGiamGia; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }

    @Override
    public String toString() {
        return "CT_HoaDon{" + "maHoaDon='" + maHoaDon + '\'' + ", maVe='" + maVe + '\'' + ", maThue='" + maThue + '\'' + ", maKM='" + maKM + '\'' + ", giaBanThucTe=" + giaBanThucTe + ", tienThue=" + tienThue + ", tienGiamGia=" + tienGiamGia + ", thanhTien=" + thanhTien + '}';
    }
}