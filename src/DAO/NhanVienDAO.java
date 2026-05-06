package DAO;

import ConnectDB.ConnectDB;
import Entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    // 1. Lấy danh sách toàn bộ nhân viên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
             
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getString("chucVu"),
                    rs.getString("soDienThoai"),
                    rs.getBoolean("trangThai")
                );
                list.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy 1 nhân viên theo Mã (Dùng cho Session lúc đăng nhập)
    public NhanVien getNhanVienTheoMa(String maNV) {
        NhanVien nv = null;
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, maNV);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("chucVu"),
                        rs.getString("soDienThoai"),
                        rs.getBoolean("trangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nv;
    }

    // 3. Thêm nhân viên mới
    public boolean addNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNV, tenNV, chucVu, soDienThoai, trangThai) VALUES (?, ?, ?, ?, ?)";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, nv.getMaNV());
            pst.setString(2, nv.getTenNV());
            pst.setString(3, nv.getChucVu());
            pst.setString(4, nv.getSoDienThoai());
            pst.setBoolean(5, nv.isTrangThai()); // true = Đang làm, false = Nghỉ việc
            
            n = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // 4. Cập nhật thông tin nhân viên
    public boolean updateNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET tenNV = ?, chucVu = ?, soDienThoai = ?, trangThai = ? WHERE maNV = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, nv.getTenNV());
            pst.setString(2, nv.getChucVu());
            pst.setString(3, nv.getSoDienThoai());
            pst.setBoolean(4, nv.isTrangThai());
            pst.setString(5, nv.getMaNV());
            
            n = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // 5. Xóa nhân viên (Xóa cứng khỏi Database)
    // Lưu ý: Nếu nhân viên đã lập hóa đơn, SQL sẽ báo lỗi Foreign Key. 
    // Trong thực tế người ta thường dùng Cập nhật trangThai = 0 (Xóa mềm) thay vì xóa cứng.
    public boolean deleteNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNV = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            pst.setString(1, maNV);
            n = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // 6. Tìm kiếm nhân viên theo Mã hoặc Tên
    public List<NhanVien> searchNhanVien(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE maNV LIKE ? OR tenNV LIKE ?";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("chucVu"),
                        rs.getString("soDienThoai"),
                        rs.getBoolean("trangThai")
                    );
                    list.add(nv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}