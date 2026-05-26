package DAO;

import ConnectDB.ConnectDB;
import Entity.ChuyenTau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChuyenTauDAO {

    // 🔹 Lấy tất cả chuyến tàu
    public List<ChuyenTau> getAll() {
        List<ChuyenTau> list = new ArrayList<>();
        String sql = "SELECT * FROM ChuyenTau";

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

    // 🔹 Tìm chuyến tàu theo mã chuyến
    public ChuyenTau findById(String maChuyen) {
        String sql = "SELECT * FROM ChuyenTau WHERE maChuyen = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyen);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm chuyến tàu mới
    public boolean insert(ChuyenTau ct) {
        String sql = "INSERT INTO ChuyenTau(maChuyen, maTau, maGaDi, maGaDen, thoiGianDi, thoiGianDen, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
        }
        return false;
    }

    // 🔹 Cập nhật thông tin chuyến tàu
    public boolean update(ChuyenTau ct) {
        String sql = "UPDATE ChuyenTau SET maTau=?, maGaDi=?, maGaDen=?, thoiGianDi=?, thoiGianDen=?, trangThai=? WHERE maChuyen=?";

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
        }
        return false;
    }

    // 🔹 Xóa chuyến tàu
    public boolean delete(String maChuyen) {
        String sql = "DELETE FROM ChuyenTau WHERE maChuyen = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyen);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 Hàm map ResultSet → Object
    private ChuyenTau mapResultSet(ResultSet rs) throws SQLException {
        ChuyenTau ct = new ChuyenTau();
        ct.setMaChuyen(rs.getString("maChuyen"));
        ct.setMaTau(rs.getString("maTau"));
        ct.setMaGaDi(rs.getString("maGaDi"));
        ct.setMaGaDen(rs.getString("maGaDen"));

        Timestamp tsDi = rs.getTimestamp("thoiGianDi");
        if (tsDi != null) ct.setThoiGianDi(tsDi.toLocalDateTime());

        Timestamp tsDen = rs.getTimestamp("thoiGianDen");
        if (tsDen != null) ct.setThoiGianDen(tsDen.toLocalDateTime());

        ct.setTrangThai(rs.getString("trangThai"));
        return ct;
    }
}