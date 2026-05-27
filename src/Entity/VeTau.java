package Entity;

public class VeTau {
    private String maVe;
    private String maChuyen;
    private String maGhe;
    private double giaGoc;
    private String trangThai;
    
    // Fields bổ sung từ JOIN
    private int soGhe;
    private String loaiGhe;
    private String maToa;
    
    // Fields mới thêm vào database
    private String tenHanhKhach;
    private String soCCCD;
    private String loaiVe;
    
    // Constructor mặc định
    public VeTau() {}
    
    // Constructor cơ bản
    public VeTau(String maVe, String maChuyen, String maGhe, double giaGoc, String trangThai) {
        this.maVe = maVe;
        this.maChuyen = maChuyen;
        this.maGhe = maGhe;
        this.giaGoc = giaGoc;
        this.trangThai = trangThai;
    }
    
    // Getters và Setters cơ bản
    public String getMaVe() {
        return maVe;
    }
    
    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }
    
    public String getMaChuyen() {
        return maChuyen;
    }
    
    public void setMaChuyen(String maChuyen) {
        this.maChuyen = maChuyen;
    }
    
    public String getMaGhe() {
        return maGhe;
    }
    
    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }
    
    public double getGiaGoc() {
        return giaGoc;
    }
    
    public void setGiaGoc(double giaGoc) {
        this.giaGoc = giaGoc;
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    // Getters và Setters cho fields bổ sung
    public int getSoGhe() {
        return soGhe;
    }
    
    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }
    
    public String getLoaiGhe() {
        return loaiGhe;
    }
    
    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }
    
    public String getMaToa() {
        return maToa;
    }
    
    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }
    
    // Getters và Setters cho fields mới
    public String getTenHanhKhach() {
        return tenHanhKhach;
    }
    
    public void setTenHanhKhach(String tenHanhKhach) {
        this.tenHanhKhach = tenHanhKhach;
    }
    
    public String getSoCCCD() {
        return soCCCD;
    }
    
    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }
    
    public String getLoaiVe() {
        return loaiVe;
    }
    
    public void setLoaiVe(String loaiVe) {
        this.loaiVe = loaiVe;
    }
    
    @Override
    public String toString() {
        return "VeTau{" +
                "maVe='" + maVe + '\'' +
                ", maChuyen='" + maChuyen + '\'' +
                ", maGhe='" + maGhe + '\'' +
                ", soGhe=" + soGhe +
                ", loaiGhe='" + loaiGhe + '\'' +
                ", tenHanhKhach='" + tenHanhKhach + '\'' +
                ", soCCCD='" + soCCCD + '\'' +
                ", loaiVe='" + loaiVe + '\'' +
                ", giaGoc=" + giaGoc +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}