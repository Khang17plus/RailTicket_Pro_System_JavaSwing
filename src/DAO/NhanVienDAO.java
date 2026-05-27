package DAO;

import ConnectDB.ConnectDB;
import Entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {

    // 🔹 Lấy tất cả nhân viên
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

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

    // 🔹 Tìm theo mã NV
    public NhanVien findById(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";

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

    public NhanVien getNhanVienTheoMa(String maNV) {
        return findById(maNV);
    }

    // 🔹 Thêm mới nhân viên
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien(maNV, tenNV, chucVu, soDienThoai, trangThai) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getChucVu()); 
            ps.setString(4, nv.getSoDienThoai());
            ps.setBoolean(5, nv.isTrangThai()); 

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Lỗi Insert DAO: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật thông tin nhân viên
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET tenNV=?, chucVu=?, soDienThoai=?, trangThai=? WHERE maNV=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getChucVu());
            ps.setString(3, nv.getSoDienThoai());
            ps.setBoolean(4, nv.isTrangThai()); 
            ps.setString(5, nv.getMaNV());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Tìm kiếm nhân viên theo mã hoặc số điện thoại
    public List<NhanVien> searchNhanVien(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE maNV LIKE ? OR soDienThoai LIKE ?";

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

    // 🔥 Hàm duy nhất map ResultSet → Object NhanVien
    private NhanVien mapResultSet(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNV(rs.getString("maNV"));
        nv.setTenNV(rs.getString("tenNV"));
        nv.setChucVu(rs.getString("chucVu"));
        nv.setSoDienThoai(rs.getString("soDienThoai"));
        nv.setTrangThai(rs.getBoolean("trangThai"));
        return nv;
    }

    // 🔹 Hàm đếm số lượng phục vụ hiển thị thẻ thống kê
    public int countNhanVien(String type) {
        String sql = "SELECT COUNT(*) FROM NhanVien";
        if (type.equals("1")) {
            sql += " WHERE trangThai = 1"; 
        } else if (type.equals("0")) {
            sql += " WHERE trangThai = 0"; 
        }

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // =========================================================================
    // 🔥 LẤY MÃ LỚN NHẤT ĐÃ ĐƯỢC TỐI ƯU SẮP XẾP SỐ (Dành cho SQL Server)
    // =========================================================================
    public String getMaxMaNhanVien() {
        String sql = "SELECT TOP 1 maNV FROM NhanVien ORDER BY CAST(SUBSTRING(maNV, 3, LEN(maNV)) AS INT) DESC";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("maNV");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}