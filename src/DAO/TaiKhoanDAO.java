package DAO;

import ConnectDB.ConnectDB;
import Entity.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    // 🔹 Lấy tất cả tài khoản hệ thống (Sắp xếp theo mã nhân viên)
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        // Cắt chữ "NV" để ép kiểu số nguyên, giúp sắp xếp JTable mượt mà không lỗi Alphabetical
        String sql = "SELECT * FROM TaiKhoan ORDER BY CAST(SUBSTRING(maNV, 3, LEN(maNV)) AS INT) ASC";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Tìm tài khoản theo mã nhân viên (maNV chính là Username)
    public TaiKhoan findById(String maNV) {
        String sql = "SELECT * FROM TaiKhoan WHERE maNV = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm tài khoản mới (Cấp tài khoản cho nhân viên)
    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan(maNV, matKhau, vaiTro) VALUES (?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getMaNV());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getVaiTro()); // ADMIN hoặc STAFF

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật tài khoản (Đổi mật khẩu hoặc thay đổi quyền hạn)
    public boolean update(TaiKhoan tk) {
        // Nếu mật khẩu truyền vào trống/null -> Chỉ cập nhật Vai Trò (Tránh mất pass cũ)
        String sql;
        boolean updatePassword = tk.getMatKhau() != null && !tk.getMatKhau().trim().isEmpty();

        if (updatePassword) {
            sql = "UPDATE TaiKhoan SET matKhau=?, vaiTro=? WHERE maNV=?";
        } else {
            sql = "UPDATE TaiKhoan SET vaiTro=? WHERE maNV=?";
        }

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (updatePassword) {
                ps.setString(1, tk.getMatKhau());
                ps.setString(2, tk.getVaiTro());
                ps.setString(3, tk.getMaNV());
            } else {
                ps.setString(1, tk.getVaiTro());
                ps.setString(2, tk.getMaNV());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa tài khoản hệ thống
    public boolean delete(String maNV) {
        String sql = "DELETE FROM TaiKhoan WHERE maNV = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Tìm kiếm tài khoản (Theo mã nhân viên hoặc vai trò)
    public List<TaiKhoan> searchTaiKhoan(String keyword) {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE maNV LIKE ? OR vaiTro LIKE ? ORDER BY CAST(SUBSTRING(maNV, 3, LEN(maNV)) AS INT) ASC";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern); 
            ps.setString(2, searchPattern); 
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs)); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================================
    // 🔥 THÀNH PHẦN BỔ SUNG: PHỤC VỤ LUỒNG KIỂM TRA ĐIỀU KIỆN & IMPORT EXCEL
    // =========================================================================
    
    /**
     * Kiểm tra xem mã nhân viên nhập vào đã tồn tại trong danh mục NhanVien hay chưa
     * (Ràng buộc khóa ngoại bắt buộc trước khi cấp tài khoản)
     */
    public boolean checkNhanVienTonTai(String maNV) {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE maNV = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Đã tồn tại nhân viên gốc -> Hợp lệ để cấp account
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra xem mã nhân viên này đã được cấp tài khoản trước đó hay chưa (Chặn trùng PK)
     */
    public boolean checkTrungTaiKhoan(String maNV) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE maNV = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Trả về true nếu nhân viên này đã có tài khoản rồi
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Kiểm tra thông tin tài khoản và mật khẩu dưới Database phục vụ chức năng Đăng Nhập
     * @return Đối tượng TaiKhoan nếu khớp dữ liệu, ngược lại trả về null
     */
    public TaiKhoan checkLogin(String username, String password) {
        String sql = "SELECT * FROM TaiKhoan WHERE maNV = ? AND matKhau = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaNV(rs.getString("maNV"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    tk.setVaiTro(rs.getString("vaiTro"));
                    return tk;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔥 Hàm duy nhất ánh xạ ResultSet → Object TaiKhoan
    private TaiKhoan mapResultSet(ResultSet rs) throws SQLException {
        TaiKhoan tk = new TaiKhoan();
        tk.setMaNV(rs.getString("maNV"));
        tk.setMatKhau(rs.getString("matKhau"));
        tk.setVaiTro(rs.getString("vaiTro"));
        return tk;
    }
}