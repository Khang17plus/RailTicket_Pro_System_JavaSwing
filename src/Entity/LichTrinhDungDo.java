package Entity;
import java.time.LocalDateTime;

public class LichTrinhDungDo {
    private String maChuyen;
    private String maGa;
    private int thuTuDung;
    private LocalDateTime thoiGianDen;
    private LocalDateTime thoiGianDi;

    public LichTrinhDungDo() {}

    public LichTrinhDungDo(String maChuyen, String maGa, int thuTuDung, LocalDateTime thoiGianDen, LocalDateTime thoiGianDi) {
        this.maChuyen = maChuyen;
        this.maGa = maGa;
        this.thuTuDung = thuTuDung;
        this.thoiGianDen = thoiGianDen;
        this.thoiGianDi = thoiGianDi;
    }

    public String getMaChuyen() { return maChuyen; }
    public void setMaChuyen(String maChuyen) { this.maChuyen = maChuyen; }

    public String getMaGa() { return maGa; }
    public void setMaGa(String maGa) { this.maGa = maGa; }

    public int getThuTuDung() { return thuTuDung; }
    public void setThuTuDung(int thuTuDung) { this.thuTuDung = thuTuDung; }

    public LocalDateTime getThoiGianDen() { return thoiGianDen; }
    public void setThoiGianDen(LocalDateTime thoiGianDen) { this.thoiGianDen = thoiGianDen; }

    public LocalDateTime getThoiGianDi() { return thoiGianDi; }
    public void setThoiGianDi(LocalDateTime thoiGianDi) { this.thoiGianDi = thoiGianDi; }

    @Override
    public String toString() {
        return "LichTrinhDungDo{" + "maChuyen='" + maChuyen + '\'' + ", maGa='" + maGa + '\'' + ", thuTuDung=" + thuTuDung + ", thoiGianDen=" + thoiGianDen + ", thoiGianDi=" + thoiGianDi + '}';
    }
}