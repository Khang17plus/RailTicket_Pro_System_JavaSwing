package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * THÊM MỚI LỊCH TRÌNH DỪNG ĐỖ
     */
    /**
     * THÊM MỚI LỊCH TRÌNH DỪNG ĐỖ (Tự động cộng 1 cho các chặng phía sau nếu chèn vào giữa)
     */
    public boolean themLichTrinh(LichTrinhDungDo lt) {
        // Câu lệnh 1: Đẩy các chặng đứng sau (hoặc bằng) vị trí mới tăng lên 1 đơn vị
        String sqlUpdate = "UPDATE LichTrinhDungDo SET thuTuDung = thuTuDung + 1 WHERE maChuyen = ? AND thuTuDung >= ?";
        
        // Câu lệnh 2: Chèn chặng mới vào vị trí trống đã được tạo ra
        String sqlInsert = "INSERT INTO LichTrinhDungDo (maChuyen, maGa, thuTuDung, thoiGianDen, thoiGianDi) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = ConnectDB.getInstance().getConnection();
            conn.setAutoCommit(false); // Bật Transaction để đảm bảo cả 2 câu lệnh cùng thành công hoặc cùng hủy

            // BƯỚC 1: Cập nhật tăng thứ tự các chặng cũ để nhường chỗ
            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.setString(1, lt.getMaChuyen());
                psUpdate.setInt(2, lt.getThuTuDung());
                psUpdate.executeUpdate();
            }

            // BƯỚC 2: Tiến hành chèn chặng mới vào
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setString(1, lt.getMaChuyen());
                psInsert.setString(2, lt.getMaGa());
                psInsert.setInt(3, lt.getThuTuDung());
                psInsert.setTimestamp(4, lt.getThoiGianDen() != null ? Timestamp.valueOf(lt.getThoiGianDen()) : null);
                psInsert.setTimestamp(5, lt.getThoiGianDi() != null ? Timestamp.valueOf(lt.getThoiGianDi()) : null);
                
                int rowsInserted = psInsert.executeUpdate();
                
                conn.commit(); // Hoàn thành an toàn cả 2 bước, lưu thay đổi vào DB
                return rowsInserted > 0;
            }
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Nếu có bất kỳ lỗi gì xảy ra, hủy bỏ toàn bộ thao tác để tránh lỗi lệch dữ liệu
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Trả lại trạng thái auto-commit mặc định
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * CẬP NHẬT LỊCH TRÌNH DỪNG ĐỖ THÔNG THƯỜNG
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
     * 🔥 HÀM QUAN TRỌNG: Cập nhật lịch trình nâng cao hỗ trợ thay đổi cả Mã Ga
     */
    public boolean capNhatLichTrinhNangCao(LichTrinhDungDo lt, String maGaCu) {
        String sql = "UPDATE LichTrinhDungDo SET maGa = ?, thuTuDung = ?, thoiGianDen = ?, thoiGianDi = ? " +
                     "WHERE maChuyen = ? AND maGa = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lt.getMaGa()); 
            ps.setInt(2, lt.getThuTuDung());
            ps.setTimestamp(3, lt.getThoiGianDen() != null ? Timestamp.valueOf(lt.getThoiGianDen()) : null);
            ps.setTimestamp(4, lt.getThoiGianDi() != null ? Timestamp.valueOf(lt.getThoiGianDi()) : null);
            ps.setString(5, lt.getMaChuyen());
            ps.setString(6, maGaCu); 
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * XÓA CHẶNG DỪNG
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