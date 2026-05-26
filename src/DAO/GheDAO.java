package DAO;

import ConnectDB.ConnectDB;
import Entity.Ghe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GheDAO {

    // Sinh mã ghế tự động theo toa, dạng {maToa}_Gxxx
    public String generateNextMaGhe(String maToa) {
        String sql = "SELECT TOP 1 maGhe FROM Ghe WHERE maToa = ? AND maGhe LIKE ? " +
                     "ORDER BY LEN(maGhe) DESC, " +
                     "CAST(SUBSTRING(maGhe, CHARINDEX('_G', maGhe) + 2, LEN(maGhe)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maToa);
            ps.setString(2, maToa + "_G%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maxMa = rs.getString("maGhe");
                int gIndex = maxMa.lastIndexOf("_G");
                if (gIndex != -1) {
                    String numberPart = maxMa.substring(gIndex + 2);
                    int nextNumber = Integer.parseInt(numberPart) + 1;
                    return maToa + "_G" + String.format("%03d", nextNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maToa + "_G001";
    }

    // Lấy tất cả ghế
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

    // Lấy ghế theo mã toa
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

    // Tìm theo mã ghế
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

    // Thêm ghế
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

    // Cập nhật ghế
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

    // Xoá ghế theo mã ghế
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

    // ==================== CÁC METHOD BỔ SUNG CHO CONTROLLER ====================

    // Xóa tất cả ghế theo mã toa
    public boolean deleteByMaToa(String maToa) {
        String sql = "DELETE FROM Ghe WHERE maToa = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maToa);
            return ps.executeUpdate() >= 0; // >=0 vì có thể không có ghế nào
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra tồn tại ghế theo toa và số ghế
    public boolean existsByMaToaAndSoGhe(String maToa, int soGhe) {
        String sql = "SELECT COUNT(*) FROM Ghe WHERE maToa = ? AND soGhe = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maToa);
            ps.setInt(2, soGhe);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đếm số ghế hiện có của toa
    public int countByMaToa(String maToa) {
        String sql = "SELECT COUNT(*) FROM Ghe WHERE maToa = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maToa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Thêm nhiều ghế (batch)
    public boolean insertBatch(List<Ghe> list) {
        if (list == null || list.isEmpty()) return true;
        String sql = "INSERT INTO Ghe(maGhe, maToa, soGhe, loaiGhe) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Ghe g : list) {
                ps.setString(1, g.getMaGhe());
                ps.setString(2, g.getMaToa());
                ps.setInt(3, g.getSoGhe());
                ps.setString(4, g.getLoaiGhe());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            for (int r : results) {
                if (r == Statement.EXECUTE_FAILED) return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // map ResultSet → Ghe
    private Ghe mapResultSet(ResultSet rs) throws SQLException {
        Ghe ghe = new Ghe();
        ghe.setMaGhe(rs.getString("maGhe"));
        ghe.setMaToa(rs.getString("maToa"));
        ghe.setSoGhe(rs.getInt("soGhe"));
        ghe.setLoaiGhe(rs.getString("loaiGhe"));
        return ghe;
    }
}