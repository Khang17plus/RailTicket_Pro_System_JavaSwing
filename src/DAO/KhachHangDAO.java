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
                KhachHang kh = mapResultSet(rs);
                list.add(kh);
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
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
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
            ps.setTimestamp(6, Timestamp.valueOf(kh.getNgayDangKy()));

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

    // 🔹 Xoá khách hàng
    public boolean delete(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 Hàm map ResultSet → Object
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
 // Tìm kiếm khách hàng (Chỉ theo CCCD hoặc SĐT)
    public List<KhachHang> searchKhachHang(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        
        // Câu SQL chỉ sử dụng điều kiện cho cccd và soDienThoai
        String sql = "SELECT * FROM KhachHang WHERE cccd LIKE ? OR soDienThoai LIKE ?";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            String searchPattern = "%" + keyword + "%";
            
            // Bây giờ chỉ còn 2 dấu ? nên ta chỉ setString 2 lần
            pst.setString(1, searchPattern); // cho cccd
            pst.setString(2, searchPattern); // cho soDienThoai
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    java.time.LocalDateTime ngayDK = (rs.getTimestamp("ngayDangKy") != null) 
                            ? rs.getTimestamp("ngayDangKy").toLocalDateTime() : null;
                            
                    KhachHang kh = new KhachHang(
                        rs.getString("maKH"), 
                        rs.getString("tenKH"), 
                        rs.getString("cccd"),
                        rs.getString("soDienThoai"), 
                        rs.getString("email"), 
                        ngayDK
                    );
                    list.add(kh);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}