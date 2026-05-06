package Utils;

import Entity.NhanVien;
import Entity.TaiKhoan;

public class SessionManager {
    private static SessionManager instance;
    private TaiKhoan taiKhoanDangNhap; // Biến lưu trữ tài khoản hiện tại
    private NhanVien nhanVienHienTai; // Lưu toàn bộ thông tin nhân viên
    // Private constructor để ngăn tạo đối tượng mới bằng từ khóa 'new'
    private SessionManager() {}

    // Lấy instance duy nhất của class này
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    public void login(TaiKhoan tk, NhanVien nv) {
        this.taiKhoanDangNhap = tk;
        this.nhanVienHienTai = nv;
    }
    public NhanVien getNhanVien() { return nhanVienHienTai; }	

    // Lưu thông tin khi đăng nhập thành công
    public void setTaiKhoanDangNhap(TaiKhoan tk) {
        this.taiKhoanDangNhap = tk;
    }

    // Lấy thông tin tài khoản đang đăng nhập để sử dụng
    public TaiKhoan getTaiKhoanDangNhap() {
        return taiKhoanDangNhap;
    }

    // Xóa thông tin khi đăng xuất
    public void dangXuat() {
    	this.taiKhoanDangNhap = null;
        this.nhanVienHienTai = null;
    }
}