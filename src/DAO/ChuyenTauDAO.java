package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.ChuyenTau;

public class ChuyenTauDAO {

    /**
     * Lấy toàn bộ danh sách chuyến tàu từ DB
     */
    public List<ChuyenTau> getAllChuyenTau() {
        List<ChuyenTau> list = new ArrayList<>();
        String sql = "SELECT maChuyen, maTau, maGaDi, maGaDen, thoiGianDi, thoiGianDen, trangThai FROM ChuyenTau";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ChuyenTau ct = new ChuyenTau();
                ct.setMaChuyen(rs.getString("maChuyen"));
                ct.setMaTau(rs.getString("maTau"));
                ct.setMaGaDi(rs.getString("maGaDi"));
                ct.setMaGaDen(rs.getString("maGaDen"));
                
                Timestamp tgDi = rs.getTimestamp("thoiGianDi");
                if (tgDi != null) ct.setThoiGianDi(tgDi.toLocalDateTime());
                
                Timestamp tgDen = rs.getTimestamp("thoiGianDen");
                if (tgDen != null) ct.setThoiGianDen(tgDen.toLocalDateTime());
                
                ct.setTrangThai(rs.getString("trangThai"));
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Hàm tự động sinh mã chuyến tiếp theo (CH01, CH02,...)
     */
    public String getNextMaChuyen() {
        String sql = "SELECT MAX(maChuyen) FROM ChuyenTau";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String maxMa = rs.getString(1);
                if (maxMa != null && maxMa.startsWith("CH")) {
                    int num = Integer.parseInt(maxMa.substring(2)); 
                    return String.format("CH%02d", num + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "CH01"; // Mặc định nếu DB trống
    }

    /**
     * Thêm mới một chuyến tàu vào database
     */
    public boolean themChuyenTau(ChuyenTau ct) {
        String sql = "INSERT INTO ChuyenTau (maChuyen, maTau, maGaDi, maGaDen, thoiGianDi, thoiGianDen, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ct.getMaChuyen());
            ps.setString(2, ct.getMaTau());
            ps.setString(3, ct.getMaGaDi());
            ps.setString(4, ct.getMaGaDen());
            
            ps.setTimestamp(5, ct.getThoiGianDi() != null ? Timestamp.valueOf(ct.getThoiGianDi()) : null);
            ps.setTimestamp(6, ct.getThoiGianDen() != null ? Timestamp.valueOf(ct.getThoiGianDen()) : null);
            ps.setString(7, ct.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin chuyến tàu
     */
    public boolean capNhatChuyenTau(ChuyenTau ct) {
        String sql = "UPDATE ChuyenTau SET maTau = ?, maGaDi = ?, maGaDen = ?, thoiGianDi = ?, thoiGianDen = ?, trangThai = ? WHERE maChuyen = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ct.getMaTau());
            ps.setString(2, ct.getMaGaDi());
            ps.setString(3, ct.getMaGaDen());
            ps.setTimestamp(4, ct.getThoiGianDi() != null ? Timestamp.valueOf(ct.getThoiGianDi()) : null);
            ps.setTimestamp(5, ct.getThoiGianDen() != null ? Timestamp.valueOf(ct.getThoiGianDen()) : null);
            ps.setString(6, ct.getTrangThai());
            ps.setString(7, ct.getMaChuyen());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa một chuyến tàu khỏi hệ thống dựa vào mã
     */
    public boolean xoaChuyenTau(String maChuyen) {
        String sql = "DELETE FROM ChuyenTau WHERE maChuyen = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maChuyen);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}