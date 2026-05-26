package Entity;

public class GaTau {
    private String maGa;
    private String tenGa;
    private String diaChi;
    private String soDienThoai;
    private String trangThai;

    public GaTau() {}

    public GaTau(String maGa, String tenGa, String diaChi, String soDienThoai, String trangThai) {
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.trangThai = trangThai;
    }

    public String getMaGa() { 
        return maGa; 
    }
    
    public void setMaGa(String maGa) { 
        this.maGa = maGa; 
    }

    public String getTenGa() { 
        return tenGa; 
    }
    
    public void setTenGa(String tenGa) { 
        this.tenGa = tenGa; 
    }

    public String getDiaChi() { 
        return diaChi; 
    }
    
    public void setDiaChi(String diaChi) { 
        this.diaChi = diaChi; 
    }

    public String getSoDienThoai() { 
        return soDienThoai; 
    }
    
    public void setSoDienThoai(String soDienThoai) { 
        this.soDienThoai = soDienThoai; 
    }

    public String getTrangThai() { 
        return trangThai; 
    }
    
    public void setTrangThai(String trangThai) { 
        this.trangThai = trangThai; 
    }

    @Override
    public String toString() {
        return "GaTau{" + 
                "maGa='" + maGa + '\'' + 
                ", tenGa='" + tenGa + '\'' + 
                ", diaChi='" + diaChi + '\'' + 
                ", soDienThoai='" + soDienThoai + '\'' + 
                ", trangThai='" + trangThai + '\'' + 
                '}';
    }
}