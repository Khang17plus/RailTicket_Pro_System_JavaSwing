package DAO;

import ConnectDB.ConnectDB;
import Entity.Tau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TauDAO {

    // 🔹 Lấy tất cả tàu
    public List<Tau> getAll() {
        List<Tau> list = new ArrayList<>();
        String sql = "SELECT * FROM Tau";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tau tau = mapResultSet(rs);
                list.add(tau);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Tìm theo mã tàu
    public Tau findById(String maTau) {
        String sql = "SELECT * FROM Tau WHERE maTau = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTau);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 🔹 Lấy danh sách tàu định dạng "Mã - Tên" để đổ vào ComboBox (MỚI THÊM)
    public List<String> getDanhSachTauFormat() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT maTau, tenTau FROM Tau ORDER BY maTau ASC";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String tauFormat = rs.getString("maTau") + " - " + rs.getString("tenTau");
                list.add(tauFormat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Thêm tàu
    public boolean insert(Tau tau) {
        String sql = "INSERT INTO Tau(maTau, tenTau, loaiTau, trangThai) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tau.getMaTau());
            ps.setString(2, tau.getTenTau());
            ps.setString(3, tau.getLoaiTau());
            ps.setString(4, tau.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật tàu
    public boolean update(Tau tau) {
        String sql = "UPDATE Tau SET tenTau=?, loaiTau=?, trangThai=? WHERE maTau=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tau.getTenTau());
            ps.setString(2, tau.getLoaiTau());
            ps.setString(3, tau.getTrangThai());
            ps.setString(4, tau.getMaTau());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xoá tàu
    public boolean delete(String maTau) {
        String sql = "DELETE FROM Tau WHERE maTau = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTau);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 map ResultSet → Tau
    private Tau mapResultSet(ResultSet rs) throws SQLException {
        Tau tau = new Tau();
        tau.setMaTau(rs.getString("maTau"));
        tau.setTenTau(rs.getString("tenTau"));
        tau.setLoaiTau(rs.getString("loaiTau"));
        tau.setTrangThai(rs.getString("trangThai"));
        return tau;
    }
    
    // getMaTaumax 
    public String getMaxMaTau() {
        String sql = "SELECT TOP 1 maTau FROM Tau " +
                     "WHERE maTau LIKE 'T%' " +
                     "ORDER BY LEN(maTau) DESC, " +
                     "CAST(SUBSTRING(maTau, 2, LEN(maTau)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("maTau");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}