package Entity;

import java.util.Objects;

/**
 * Thực thể Tài Khoản (TaiKhoan) đại diện cho bảng TaiKhoan trong cơ sở dữ liệu RailWayTicketDB.
 * Khóa chính maNV đồng thời là Khóa ngoại tham chiếu đến bảng NhanVien.
 */
public class TaiKhoan {
    private String maNV;
    private String matKhau;
    private String vaiTro; // Nhận một trong hai giá trị chuẩn: "ADMIN" hoặc "STAFF"

    // =========================================================================
    // 🔹 CONSTRUCTORS (HÀM KHỞI TẠO)
    // =========================================================================
    
    /**
     * Hàm khởi tạo mặc định (No-Args Constructor)
     */
    public TaiKhoan() {
    }

    /**
     * Hàm khởi tạo đầy đủ tham số (All-Args Constructor)
     * * @param maNV    Mã nhân viên (Đồng thời là Username đăng nhập)
     * @param matKhau Mật khẩu mã hóa hoặc chuỗi gốc hệ thống
     * @param vaiTro  Vai trò phân quyền ('ADMIN' hoặc 'STAFF')
     */
    public TaiKhoan(String maNV, String matKhau, String vaiTro) {
        this.maNV = maNV;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro != null ? vaiTro.toUpperCase().trim() : null;
    }

    // =========================================================================
    // 🔹 GETTERS AND SETTERS (CÁC HÀM TRUY XUẤT DỮ LIỆU)
    // =========================================================================

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro != null ? vaiTro.toUpperCase().trim() : null;
    }

    // =========================================================================
    // 🔹 ĐỒNG BỘ PHƯƠNG THỨC HỖ TRỢ (TOSTRING, EQUALS, HASHCODE)
    // =========================================================================

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maNV='" + maNV + '\'' +
                ", matKhau='********'" + // Ẩn mật khẩu khi log dữ liệu test hệ thống
                ", vaiTro='" + vaiTro + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return Objects.equals(maNV, taiKhoan.maNV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNV);
    }
}