package DAO;

import ConnectDB.ConnectDB;
import Entity.LichTrinhDungDo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichTrinhDungDoDAO {

    // 🔹 Lấy tất cả lịch trình
    public List<LichTrinhDungDo> getAll() {
        List<LichTrinhDungDo> list = new ArrayList<>();
        String sql = "SELECT * FROM LichTrinhDungDo";

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

    // 🔹 Lấy lịch trình dừng đỗ của MỘT CHUYẾN TÀU (Sắp xếp theo thứ tự dừng)
    public List<LichTrinhDungDo> getByMaChuyen(String maChuyen) {
        List<LichTrinhDungDo> list = new ArrayList<>();
        String sql = "SELECT * FROM LichTrinhDungDo WHERE maChuyen = ? ORDER BY thuTuDung ASC";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyen);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Tìm 1 điểm dừng cụ thể (Dựa vào Khóa chính kép)
    public LichTrinhDungDo findById(String maChuyen, String maGa) {
        String sql = "SELECT * FROM LichTrinhDungDo WHERE maChuyen = ? AND maGa = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyen);
            ps.setString(2, maGa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm điểm dừng đỗ
    public boolean insert(LichTrinhDungDo lt) {
        String sql = "INSERT INTO LichTrinhDungDo(maChuyen, maGa, thuTuDung, thoiGianDen, thoiGianDi) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lt.getMaChuyen());
            ps.setString(2, lt.getMaGa());
            ps.setInt(3, lt.getThuTuDung());
            
            ps.setTimestamp(4, lt.getThoiGianDen() != null ? Timestamp.valueOf(lt.getThoiGianDen()) : null);
            ps.setTimestamp(5, lt.getThoiGianDi() != null ? Timestamp.valueOf(lt.getThoiGianDi()) : null);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Cập nhật điểm dừng đỗ (Cập nhật thời gian hoặc thứ tự dựa trên mã chuyến & mã ga)
    public boolean update(LichTrinhDungDo lt) {
        String sql = "UPDATE LichTrinhDungDo SET thuTuDung=?, thoiGianDen=?, thoiGianDi=? WHERE maChuyen=? AND maGa=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lt.getThuTuDung());
            ps.setTimestamp(2, lt.getThoiGianDen() != null ? Timestamp.valueOf(lt.getThoiGianDen()) : null);
            ps.setTimestamp(3, lt.getThoiGianDi() != null ? Timestamp.valueOf(lt.getThoiGianDi()) : null);
            
            // Điều kiện WHERE
            ps.setString(4, lt.getMaChuyen());
            ps.setString(5, lt.getMaGa());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa điểm dừng đỗ (Xóa 1 ga cụ thể trong chuyến)
    public boolean delete(String maChuyen, String maGa) {
        String sql = "DELETE FROM LichTrinhDungDo WHERE maChuyen = ? AND maGa = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChuyen);
            ps.setString(2, maGa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 Hàm map ResultSet → Object
    private LichTrinhDungDo mapResultSet(ResultSet rs) throws SQLException {
        LichTrinhDungDo lt = new LichTrinhDungDo();
        lt.setMaChuyen(rs.getString("maChuyen"));
        lt.setMaGa(rs.getString("maGa"));
        lt.setThuTuDung(rs.getInt("thuTuDung"));

        Timestamp tsDen = rs.getTimestamp("thoiGianDen");
        if (tsDen != null) lt.setThoiGianDen(tsDen.toLocalDateTime());

        Timestamp tsDi = rs.getTimestamp("thoiGianDi");
        if (tsDi != null) lt.setThoiGianDi(tsDi.toLocalDateTime());

        return lt;
    }
}