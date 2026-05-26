package DAO;

import ConnectDB.ConnectDB;
import Entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    // 🔹 Lấy tất cả khách hàng
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

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

    // 🔹 Tìm theo mã KH
    public KhachHang findById(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
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

    // 🔹 Tìm theo Số điện thoại (Thường dùng cho luồng tra cứu nhanh khi bán vé)
    public KhachHang findBySoDienThoai(String sdt) {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sdt);
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

    // 🔹 Thêm khách hàng
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(maKH, tenKH, cccd, soDienThoai, email, ngayDangKy) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getCccd());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getEmail());
            // Nếu ngayDangKy null thì lấy thời gian hiện tại của hệ thống
            ps.setTimestamp(6, kh.getNgayDangKy() != null ? Timestamp.valueOf(kh.getNgayDangKy()) : new Timestamp(System.currentTimeMillis()));

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật khách hàng
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH=?, cccd=?, soDienThoai=?, email=? WHERE maKH=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getCccd());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getMaKH());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Tìm kiếm khách hàng (Theo CCCD hoặc SĐT)
    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE cccd LIKE ? OR soDienThoai LIKE ?";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern); // cho cccd
            ps.setString(2, searchPattern); // cho soDienThoai
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs)); // Sử dụng lại hàm map chuẩn, tránh lặp code
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================================================
    // 🔥 LẤY MÃ KHÁCH HÀNG LỚN NHẤT HIỆN TẠI (ĐỂ TỰ SINH MÃ TỰ ĐỘNG KH0xx)
    // =========================================================================
    public String getMaxMaKhachHang() {
        String maxMa = "";
        String sql = "SELECT MAX(maKH) FROM KhachHang";

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

    // 🔥 Hàm duy nhất map ResultSet → Object KhachHang
    private KhachHang mapResultSet(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("maKH"));
        kh.setTenKH(rs.getString("tenKH"));
        kh.setCccd(rs.getString("cccd"));
        kh.setSoDienThoai(rs.getString("soDienThoai"));
        kh.setEmail(rs.getString("email"));

        Timestamp ts = rs.getTimestamp("ngayDangKy");
        if (ts != null) {
            kh.setNgayDangKy(ts.toLocalDateTime());
        }

        return kh;
    }
}