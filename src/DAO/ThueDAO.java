package DAO;

import ConnectDB.ConnectDB;
import Entity.Thue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp; // Cần cái này cho biến ts
import java.time.LocalDateTime; // Cần cái này cho .toLocalDateTime()
import Entity.Thue; // Cần cái này cho class Thue

public class ThueDAO {

    // 1. LẤY TOÀN BỘ DỮ LIỆU TỪ DB SQL (Có sắp xếp để dễ nhìn)
    public List<Thue> getAll() {
        List<Thue> list = new ArrayList<>();
        // Thêm ORDER BY để mã thuế mới nhất lên đầu bảng
        String sql = "SELECT maThue, tenThue, phanTram, ngayBatDau, trangThai FROM Thue ORDER BY maThue DESC";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("ngayBatDau");
                list.add(new Thue(
                    rs.getString("maThue"),
                    rs.getString("tenThue"),
                    rs.getDouble("phanTram"),
                    ts != null ? ts.toLocalDateTime() : null,
                    rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) { 
            System.err.println("Lỗi getAll() trong ThueDAO: " + e.getMessage());
        }
        return list;
    }

    // 2. THÊM DỮ LIỆU VÀO DB SQL
    public boolean insert(Thue t) {
        String sql = "INSERT INTO Thue(maThue, tenThue, phanTram, ngayBatDau, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, t.getMaThue());
            ps.setString(2, t.getTenThue());
            ps.setDouble(3, t.getPhanTram());
            ps.setTimestamp(4, t.getNgayBatDau() != null ? Timestamp.valueOf(t.getNgayBatDau()) : null);
            ps.setBoolean(5, t.isTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println("Lỗi insert() trong ThueDAO: " + e.getMessage());
        }
        return false;
    }

    // 3. SỬA DỮ LIỆU TRONG DB SQL
    public boolean update(Thue t) {
        String sql = "UPDATE Thue SET tenThue=?, phanTram=?, ngayBatDau=?, trangThai=? WHERE maThue=?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, t.getTenThue());
            ps.setDouble(2, t.getPhanTram());
            ps.setTimestamp(3, t.getNgayBatDau() != null ? Timestamp.valueOf(t.getNgayBatDau()) : null);
            ps.setBoolean(4, t.isTrangThai());
            ps.setString(5, t.getMaThue());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println("Lỗi update() trong ThueDAO: " + e.getMessage());
        }
        return false;
    }

    // 4. XÓA MỀM (SOFT DELETE) - Chuyển trạng thái về ngừng áp dụng để an toàn cho Hóa đơn
    public boolean delete(String maThue) {
        String sql = "UPDATE Thue SET trangThai = 0 WHERE maThue = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maThue);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            System.err.println("Lỗi delete() trong ThueDAO: " + e.getMessage());
        }
        return false;
    }

    // 5. TÌM KIẾM DỮ LIỆU TRONG DB SQL
    public List<Thue> searchThue(String keyword) {
        List<Thue> list = new ArrayList<>();
        String sql = "SELECT maThue, tenThue, phanTram, ngayBatDau, trangThai FROM Thue WHERE maThue LIKE ? OR tenThue LIKE ? ORDER BY maThue DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String p = "%" + keyword + "%";
            ps.setString(1, p); 
            ps.setString(2, p);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("ngayBatDau");
                    list.add(new Thue(
                        rs.getString("maThue"),
                        rs.getString("tenThue"),
                        rs.getDouble("phanTram"),
                        ts != null ? ts.toLocalDateTime() : null,
                        rs.getBoolean("trangThai")
                    ));
                }
            }
        } catch (SQLException e) { 
            System.err.println("Lỗi searchThue() trong ThueDAO: " + e.getMessage());
        }
        return list;
    }
}