package Entity;

public class Ghe {
    private String maGhe;
    private String maToa;
    private int soGhe;
    private String loaiGhe;

    public Ghe() {}

    public Ghe(String maGhe, String maToa, int soGhe, String loaiGhe) {
        this.maGhe = maGhe;
        this.maToa = maToa;
        this.soGhe = soGhe;
        this.loaiGhe = loaiGhe;
    }

    public String getMaGhe() { return maGhe; }
    public void setMaGhe(String maGhe) { this.maGhe = maGhe; }

    public String getMaToa() { return maToa; }
    public void setMaToa(String maToa) { this.maToa = maToa; }

    public int getSoGhe() { return soGhe; }
    public void setSoGhe(int soGhe) { this.soGhe = soGhe; }

    public String getLoaiGhe() { return loaiGhe; }
    public void setLoaiGhe(String loaiGhe) { this.loaiGhe = loaiGhe; }

    @Override
    public String toString() {
        return "Ghe{" + "maGhe='" + maGhe + '\'' + ", maToa='" + maToa + '\'' + ", soGhe=" + soGhe + ", loaiGhe='" + loaiGhe + '\'' + '}';
    }
}