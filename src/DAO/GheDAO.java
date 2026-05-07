package DAO;

import ConnectDB.ConnectDB;
import Entity.Ghe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GheDAO {

    // 🔹 Lấy tất cả ghế
    public List<Ghe> getAll() {
        List<Ghe> list = new ArrayList<>();
        String sql = "SELECT * FROM Ghe";

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

    // 🔹 Lấy ghế theo mã toa (QUAN TRỌNG)
    public List<Ghe> findByMaToa(String maToa) {
        List<Ghe> list = new ArrayList<>();
        String sql = "SELECT * FROM Ghe WHERE maToa = ? ORDER BY soGhe";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maToa);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Tìm theo mã ghế
    public Ghe findById(String maGhe) {
        String sql = "SELECT * FROM Ghe WHERE maGhe = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGhe);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm ghế
    public boolean insert(Ghe ghe) {
        String sql = "INSERT INTO Ghe(maGhe, maToa, soGhe, loaiGhe) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ghe.getMaGhe());
            ps.setString(2, ghe.getMaToa());
            ps.setInt(3, ghe.getSoGhe());
            ps.setString(4, ghe.getLoaiGhe());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật ghế
    public boolean update(Ghe ghe) {
        String sql = "UPDATE Ghe SET maToa=?, soGhe=?, loaiGhe=? WHERE maGhe=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ghe.getMaToa());
            ps.setInt(2, ghe.getSoGhe());
            ps.setString(3, ghe.getLoaiGhe());
            ps.setString(4, ghe.getMaGhe());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xoá ghế
    public boolean delete(String maGhe) {
        String sql = "DELETE FROM Ghe WHERE maGhe = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGhe);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 map ResultSet → Ghe
    private Ghe mapResultSet(ResultSet rs) throws SQLException {
        Ghe ghe = new Ghe();
        ghe.setMaGhe(rs.getString("maGhe"));
        ghe.setMaToa(rs.getString("maToa"));
        ghe.setSoGhe(rs.getInt("soGhe"));
        ghe.setLoaiGhe(rs.getString("loaiGhe"));
        return ghe;
    }
}