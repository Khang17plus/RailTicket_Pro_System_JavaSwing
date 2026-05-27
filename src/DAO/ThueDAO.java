package DAO;

import ConnectDB.ConnectDB;
import Entity.Thue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp; 
import java.time.LocalDateTime; 

public class ThueDAO {

    // 1. LẤY TOÀN BỘ DỮ LIỆU TỪ DB SQL (Sắp xếp tăng dần theo số)
    public List<Thue> getAll() {
        List<Thue> list = new ArrayList<>();
        String sql = "SELECT maThue, tenThue, phanTram, ngayBatDau, trangThai FROM Thue " +
                     "ORDER BY CAST(SUBSTRING(maThue, 5, LEN(maThue)) AS INT) ASC";
        
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

    // 4. XÓA MỀM (SOFT DELETE)
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
        String sql = "SELECT maThue, tenThue, phanTram, ngayBatDau, trangThai FROM Thue " +
                     "WHERE maThue LIKE ? OR tenThue LIKE ? " +
                     "ORDER BY CAST(SUBSTRING(maThue, 5, LEN(maThue)) AS INT) ASC";
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

    // =========================================================================
    // 🔥 ĐÃ SỬA: LẤY MÃ THUẾ LỚN NHẤT TRẢ VỀ NULL NẾU DB TRỐNG
    // =========================================================================
    public String getMaxMaThue() {
        String maxMa = null; // Sửa đổi từ "" sang null để đồng bộ
        String sql = "SELECT TOP 1 maThue FROM Thue ORDER BY CAST(SUBSTRING(maThue, 5, LEN(maThue)) AS INT) DESC";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                maxMa = rs.getString(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi getMaxMaThue() trong ThueDAO: " + e.getMessage());
        }
        return maxMa;
    }

    // 7. PHÁT SINH MÃ TỰ ĐỘNG NGAY TRONG DAO (ĐÃ CHUẨN HÓA CHECK TRỐNG)
    public String phatSinhMaTuDong() {
        String maxMa = getMaxMaThue();
        
        // Kiểm tra chặt chẽ, chỉ cần dính một trong các điều kiện trống là gán mã đầu tiên ngay
        if (maxMa == null || maxMa.trim().isEmpty() || maxMa.trim().equals("")) {
            return "THUE001";
        }
        
        try {
            // Cắt chữ "THUE" để lấy phần số ("THUE001" -> "001")
            String phanSoStr = maxMa.substring(4).trim();
            int phanSo = Integer.parseInt(phanSoStr);
            phanSo++; 
            
            return String.format("THUE%03d", phanSo);
            
        } catch (Exception e) {
            System.err.println("Lỗi thuật toán phatSinhMaTuDong() trong ThueDAO: " + e.getMessage());
            return "THUE001"; // Gặp lỗi định dạng chuỗi bất thường thì trả về mã an toàn
        }
    }
}