package DAO;

import ConnectDB.ConnectDB;
import Entity.ToaTau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToaTauDAO {

    // 🔹 Lấy tất cả toa
    public List<ToaTau> getAll() {
        List<ToaTau> list = new ArrayList<>();
        String sql = "SELECT * FROM ToaTau";

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
    
 // 🔹 Tìm toa theo mã toa
    public ToaTau findById(String maToa) {
        String sql = "SELECT * FROM ToaTau WHERE maToa = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maToa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getMaxMaToa(String maTau) {
        String sql = 
            "SELECT TOP 1 maToa FROM ToaTau WHERE maTau = ? " +
            "ORDER BY CAST(SUBSTRING(maToa, CHARINDEX('_TOA', maToa) + 4, LEN(maToa)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTau);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("maToa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Lấy toa theo mã tàu (QUAN TRỌNG)
    public List<ToaTau> findByMaTau(String maTau) {
        List<ToaTau> list = new ArrayList<>();
        String sql = "SELECT * FROM ToaTau WHERE maTau = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTau);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Thêm toa
    public boolean insert(ToaTau toa) {
        String sql = "INSERT INTO ToaTau(maToa, maTau, tenToa, loaiToa, sucChua) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, toa.getMaToa());
            ps.setString(2, toa.getMaTau());
            ps.setString(3, toa.getTenToa());
            ps.setString(4, toa.getLoaiToa());
            ps.setInt(5, toa.getSucChua());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật toa
    public boolean update(ToaTau toa) {
        String sql = "UPDATE ToaTau SET tenToa=?, loaiToa=?, sucChua=? WHERE maToa=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, toa.getTenToa());
            ps.setString(2, toa.getLoaiToa());
            ps.setInt(3, toa.getSucChua());
            ps.setString(4, toa.getMaToa());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xoá toa
    public boolean delete(String maToa) {
        String sql = "DELETE FROM ToaTau WHERE maToa = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maToa);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 map ResultSet → ToaTau
    private ToaTau mapResultSet(ResultSet rs) throws SQLException {
        ToaTau toa = new ToaTau();
        toa.setMaToa(rs.getString("maToa"));
        toa.setMaTau(rs.getString("maTau"));
        toa.setTenToa(rs.getString("tenToa"));
        toa.setLoaiToa(rs.getString("loaiToa"));
        toa.setSucChua(rs.getInt("sucChua"));
        return toa;
    }
}