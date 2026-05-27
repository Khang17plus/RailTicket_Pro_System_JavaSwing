package DAO;

import ConnectDB.ConnectDB;
import Entity.HoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {

    // 🔹 Lấy tất cả hóa đơn (Để load lên bảng HoaDonPanel)
    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY ngayLap DESC";

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
    public List<HoaDon> searchHoaDon(String keyword) {

        List<HoaDon> list = new ArrayList<>();

        String sql = "SELECT * FROM HoaDon " +
                     "WHERE maHoaDon LIKE ? " +
                     "OR maKH LIKE ? " +
                     "OR maNV LIKE ? " +
                     "ORDER BY ngayLap DESC";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchValue = "%" + keyword + "%";

            ps.setString(1, searchValue);
            ps.setString(2, searchValue);
            ps.setString(3, searchValue);

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

    // 🔹 Thêm hóa đơn mới
    public boolean insert(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (maHoaDon, maKH, maNV, ngayLap, tongTienHang, tongThue, tongGiamGia, tongThanhToan, phuongThucThanhToan) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHoaDon());
            ps.setString(2, hd.getMaKH());
            ps.setString(3, hd.getMaNV());
            // Sử dụng thời gian hiện tại nếu ngày lập bị null
            ps.setTimestamp(4, Timestamp.valueOf(hd.getNgayLap() != null ? hd.getNgayLap() : java.time.LocalDateTime.now()));
            ps.setDouble(5, hd.getTongTienHang());
            ps.setDouble(6, hd.getTongThue());
            ps.setDouble(7, hd.getTongGiamGia());
            ps.setDouble(8, hd.getTongThanhToan());
            ps.setString(9, hd.getPhuongThucThanhToan());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Tìm hóa đơn theo mã (Dùng để in vé QR)
    public HoaDon findById(String maHD) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔥 Hàm map ResultSet → Object (Đặc sản để code sạch)
    private HoaDon mapResultSet(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(rs.getString("maHoaDon"));
        hd.setMaKH(rs.getString("maKH"));
        hd.setMaNV(rs.getString("maNV"));
        
        // Xử lý LocalDateTime cho MacBook
        Timestamp ts = rs.getTimestamp("ngayLap");
        if (ts != null) {
            hd.setNgayLap(ts.toLocalDateTime());
        }

        hd.setTongTienHang(rs.getDouble("tongTienHang"));
        hd.setTongThue(rs.getDouble("tongThue"));
        hd.setTongGiamGia(rs.getDouble("tongGiamGia"));
        hd.setTongThanhToan(rs.getDouble("tongThanhToan"));
        hd.setPhuongThucThanhToan(rs.getString("phuongThucThanhToan"));

        return hd;
    }
}