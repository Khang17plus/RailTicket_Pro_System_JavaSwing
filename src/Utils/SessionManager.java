package Utils;

import Entity.NhanVien;
import Entity.TaiKhoan;

public class SessionManager {
    private static SessionManager instance;
    private TaiKhoan taiKhoanDangNhap;
    private NhanVien nhanVienHienTai;
    
    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    // Đăng nhập (đã có)
    public void login(TaiKhoan tk, NhanVien nv) {
        this.taiKhoanDangNhap = tk;
        this.nhanVienHienTai = nv;
    }
    
    // === THÊM CÁC METHOD SAU ĐÂY ===
    
    // Kiểm tra đã đăng nhập chưa
    public boolean isLoggedIn() {
        return taiKhoanDangNhap != null;
    }
    
    // Kiểm tra có phải ADMIN không
    public boolean isAdmin() {
        return taiKhoanDangNhap != null && "ADMIN".equals(taiKhoanDangNhap.getVaiTro());
    }
    
    // Lấy tài khoản đang đăng nhập
    public TaiKhoan getCurrentTaiKhoan() {
        return taiKhoanDangNhap;
    }
    
    // Lấy nhân viên hiện tại
    public NhanVien getCurrentNhanVien() {
        return nhanVienHienTai;
    }
    
    // Đăng xuất (đã có, nhưng đổi tên cho thống nhất)
    public void logout() {
        this.taiKhoanDangNhap = null;
        this.nhanVienHienTai = null;
    }
    
    // Giữ method cũ cho tương thích (nếu cần)
    public void dangXuat() {
        logout();
    }
    
    public TaiKhoan getTaiKhoanDangNhap() {
        return taiKhoanDangNhap;
    }
    
    public void setTaiKhoanDangNhap(TaiKhoan tk) {
        this.taiKhoanDangNhap = tk;
    }
    
    public NhanVien getNhanVien() { 
        return nhanVienHienTai; 
    }
}