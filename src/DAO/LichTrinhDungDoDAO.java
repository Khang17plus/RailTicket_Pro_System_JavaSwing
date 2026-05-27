package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.ConnectDB;
import Entity.LichTrinhDungDo;

public class LichTrinhDungDoDAO {

    /**
     * Lấy danh sách chặng dừng dựa vào Mã Chuyến Tàu (Sắp xếp theo thứ tự đi)
     */
    public List<LichTrinhDungDo> getLichTrinhByMaChuyen(String maChuyen) {
        List<LichTrinhDungDo> list = new ArrayList<>();
        String sql = "SELECT maChuyen, maGa, thuTuDung, thoiGianDen, thoiGianDi FROM LichTrinhDungDo WHERE maChuyen = ? ORDER BY thuTuDung ASC";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maChuyen);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichTrinhDungDo lt = new LichTrinhDungDo();
                    lt.setMaChuyen(rs.getString("maChuyen"));
                    lt.setMaGa(rs.getString("maGa"));
                    lt.setThuTuDung(rs.getInt("thuTuDung"));
                    
                    Timestamp tgDen = rs.getTimestamp("thoiGianDen");
                    if (tgDen != null) lt.setThoiGianDen(tgDen.toLocalDateTime());
                    
                    Timestamp tgDi = rs.getTimestamp("thoiGianDi");
                    if (tgDi != null) lt.setThoiGianDi(tgDi.toLocalDateTime());
                    
                    list.add(lt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 🔥 THÊM MỚI LỊCH TRÌNH DỪNG ĐỖ
     */
    public boolean themLichTrinh(LichTrinhDungDo lt) {
        String sql = "INSERT INTO LichTrinhDungDo (maChuyen, maGa, thuTuDung, thoiGianDen, thoiGianDi) VALUES (?, ?, ?, ?, ?)";
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
            return false;
        }
    }

    /**
     * 🔥 CẬP NHẬT LỊCH TRÌNH DỪNG ĐỖ
     */
    public boolean capNhatLichTrinh(LichTrinhDungDo lt) {
        String sql = "UPDATE LichTrinhDungDo SET thuTuDung = ?, thoiGianDen = ?, thoiGianDi = ? WHERE maChuyen = ? AND maGa = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, lt.getThuTuDung());
            ps.setTimestamp(2, lt.getThoiGianDen() != null ? Timestamp.valueOf(lt.getThoiGianDen()) : null);
            ps.setTimestamp(3, lt.getThoiGianDi() != null ? Timestamp.valueOf(lt.getThoiGianDi()) : null);
            ps.setString(4, lt.getMaChuyen());
            ps.setString(5, lt.getMaGa());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 🔥 XÓA CHẶNG DỪNG (Dựa vào cặp khóa chính: maChuyen và maGa)
     */
    public boolean xoaLichTrinh(String maChuyen, String maGa) {
        String sql = "DELETE FROM LichTrinhDungDo WHERE maChuyen = ? AND maGa = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maChuyen);
            ps.setString(2, maGa);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}