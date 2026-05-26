package DAO;

import ConnectDB.ConnectDB;
import Entity.KhuyenMai;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    // 🔹 Lấy tất cả chương trình khuyến mãi
    public List<KhuyenMai> getAll() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";

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

    // 🔹 Tìm theo mã KM
    public KhuyenMai findById(String maKM) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKM);
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

    // 🔹 Cập nhật thông tin khuyến mãi
    public boolean update(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKM=?, giaTri=?, loaiKM=?, ngayBatDau=?, ngayKetThuc=?, trangThai=? WHERE maKM=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getTenKM());
            ps.setDouble(2, km.getGiaTri());
            ps.setString(3, km.getLoaiKM());
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(km.getNgayBatDau()));
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(km.getNgayKetThuc()));
            ps.setBoolean(6, km.isTrangThai());
            ps.setString(7, km.getMaKM());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Thêm mới khuyến mãi
    public boolean insert(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai(maKM, tenKM, giaTri, loaiKM, ngayBatDau, ngayKetThuc, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setDouble(3, km.getGiaTri()); 
            ps.setString(4, km.getLoaiKM()); 
            
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(km.getNgayBatDau()));
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(km.getNgayKetThuc()));
            ps.setBoolean(7, km.isTrangThai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Lỗi Insert DAO: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Tìm kiếm khuyến mãi theo mã hoặc tên
    public List<KhuyenMai> searchKhuyenMai(String keyword) {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE maKM LIKE ? OR tenKM LIKE ?";

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

    // 🔹 Xoá khuyến mãi
    public boolean delete(String maKM) {
        String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 Hàm map ResultSet → Object KhuyenMai chuẩn nhất
    private KhuyenMai mapResultSet(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setMaKM(rs.getString("maKM"));
        km.setTenKM(rs.getString("tenKM"));
        km.setLoaiKM(rs.getString("loaiKM")); 
        km.setGiaTri(rs.getDouble("giaTri")); 
        
        Timestamp start = rs.getTimestamp("ngayBatDau");
        if (start != null) {
            km.setNgayBatDau(start.toLocalDateTime());
        }
        
        Timestamp end = rs.getTimestamp("ngayKetThuc");
        if (end != null) {
            km.setNgayKetThuc(end.toLocalDateTime());
        }
        
        km.setTrangThai(rs.getBoolean("trangThai"));
        return km;
    }

    // =========================================================================
    // 🔥 THÀNH PHẦN BỔ SUNG: LẤY MÃ KHUYẾN MÃI LỚN NHẤT HIỆN TẠI
    // =========================================================================
    /**
     * Truy vấn mã Khuyến mãi lớn nhất (Ví dụ: KM005) trong DB phục vụ phát sinh mã tự động
     */
    public String getMaxMaKhuyenMai() {
        String maxMa = "";
        String sql = "SELECT MAX(maKM) FROM KhuyenMai";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                maxMa = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxMa;
    }
}