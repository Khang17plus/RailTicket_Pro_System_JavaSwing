package DAO;

import ConnectDB.ConnectDB;
import Entity.CT_HoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CT_HoaDonDAO {

    // 🔹 Lấy danh sách chi tiết của một hóa đơn (Dùng để in danh sách vé trong 1 HĐ)
    public List<CT_HoaDon> getByMaHoaDon(String maHD) {
        List<CT_HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM CT_HoaDon WHERE maHoaDon = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
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

    // 🔹 Thêm mới chi tiết hóa đơn
    public boolean insert(CT_HoaDon ct) {
        String sql = "INSERT INTO CT_HoaDon (maHoaDon, maVe, giaBanThucTe, thanhTien) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaHoaDon());
            ps.setString(2, ct.getMaVe());
            ps.setDouble(3, ct.getGiaBanThucTe());
            ps.setDouble(4, ct.getThanhTien());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa chi tiết theo mã hóa đơn (Dùng khi hủy HĐ)
    public boolean deleteByMaHD(String maHD) {
        String sql = "DELETE FROM CT_HoaDon WHERE maHoaDon = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔥 Hàm map ResultSet → Object (Đảm bảo đồng bộ với Entity)
    private CT_HoaDon mapResultSet(ResultSet rs) throws SQLException {
        CT_HoaDon ct = new CT_HoaDon();
        ct.setMaHoaDon(rs.getString("maHoaDon"));
        ct.setMaVe(rs.getString("maVe"));
        ct.setGiaBanThucTe(rs.getDouble("giaBanThucTe"));
        ct.setThanhTien(rs.getDouble("thanhTien"));
        return ct;
    }
}