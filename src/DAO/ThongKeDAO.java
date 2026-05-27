package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import ConnectDB.ConnectDB;

public class ThongKeDAO {

    // ==================== DÀNH CHO TRANG CHỦ (DASHBOARD) ====================

    // 1. Thống kê số vé bán ra hôm nay (Join qua CT_HoaDon)
    public int getSoVeBanHomNay() {
        String sql = "SELECT COUNT(ct.maVe) FROM CT_HoaDon ct JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
                     "WHERE CAST(hd.ngayLap AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 2. Thống kê tổng doanh thu hôm nay (Dùng cột tongThanhToan)
    public double getDoanhThuHomNay() {
        String sql = "SELECT COALESCE(SUM(tongThanhToan), 0) FROM HoaDon " +
                     "WHERE CAST(ngayLap AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0.0;
    }

    // 3. Thống kê tổng số chuyến tàu chạy hôm nay
    public int getSoChuyenTauHomNay() {
        String sql = "SELECT COUNT(*) FROM ChuyenTau WHERE CAST(thoiGianDi AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 4. Thống kê tổng số khách hàng trong hệ thống
    public int getTongSoKhachHang() {
        String sql = "SELECT COUNT(*) FROM KhachHang";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 5. Biểu đồ: Doanh thu tuần hiện tại
    public Map<String, Double> getDoanhThuTheoTuan() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("T2", 0.0); map.put("T3", 0.0); map.put("T4", 0.0);
        map.put("T5", 0.0); map.put("T6", 0.0); map.put("T7", 0.0); map.put("CN", 0.0);

        String sql = "SET DATEFIRST 1; " + 
                     "SELECT DATEPART(WEEKDAY, ngayLap) AS Thu, SUM(tongThanhToan) AS DoanhThu " +
                     "FROM HoaDon WHERE ngayLap >= DATEADD(wk, DATEDIFF(wk, 6, GETDATE()), 6) " +
                     "GROUP BY DATEPART(WEEKDAY, ngayLap)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int thu = rs.getInt("Thu");
                double doanhThu = rs.getDouble("DoanhThu");
                switch (thu) {
                    case 1: map.put("T2", doanhThu); break;
                    case 2: map.put("T3", doanhThu); break;
                    case 3: map.put("T4", doanhThu); break;
                    case 4: map.put("T5", doanhThu); break;
                    case 5: map.put("T6", doanhThu); break;
                    case 6: map.put("T7", doanhThu); break;
                    case 7: map.put("CN", doanhThu); break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    // 6. Biểu đồ: Số vé tuần hiện tại
    public Map<String, Integer> getSoVeBanTheoTuan() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("T2", 0); map.put("T3", 0); map.put("T4", 0);
        map.put("T5", 0); map.put("T6", 0); map.put("T7", 0); map.put("CN", 0);

        String sql = "SET DATEFIRST 1; " +
                     "SELECT DATEPART(WEEKDAY, hd.ngayLap) AS Thu, COUNT(ct.maVe) AS SoVe " +
                     "FROM CT_HoaDon ct JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
                     "WHERE hd.ngayLap >= DATEADD(wk, DATEDIFF(wk, 6, GETDATE()), 6) " +
                     "GROUP BY DATEPART(WEEKDAY, hd.ngayLap)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int thu = rs.getInt("Thu");
                int soVe = rs.getInt("SoVe");
                switch (thu) {
                    case 1: map.put("T2", soVe); break;
                    case 2: map.put("T3", soVe); break;
                    case 3: map.put("T4", soVe); break;
                    case 4: map.put("T5", soVe); break;
                    case 5: map.put("T6", soVe); break;
                    case 6: map.put("T7", soVe); break;
                    case 7: map.put("CN", soVe); break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    // ==================== DÀNH CHO TRANG THỐNG KÊ (ĐỘNG) ====================

    // 7. Thống kê theo Tháng
    public List<Object[]> getDoanhThuTheoThang() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT MONTH(hd.ngayLap) AS Thang, " +
                     "COUNT(ct.maVe) AS SoVe, " +
                     "COUNT(DISTINCT hd.maHoaDon) AS SoHoaDon, " +
                     "SUM(hd.tongThanhToan) AS DoanhThu " +
                     "FROM HoaDon hd LEFT JOIN CT_HoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                     "WHERE YEAR(hd.ngayLap) = YEAR(GETDATE()) " +
                     "GROUP BY MONTH(hd.ngayLap) ORDER BY Thang ASC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{ "Tháng " + rs.getInt("Thang"), rs.getInt("SoVe"), rs.getInt("SoHoaDon"), rs.getDouble("DoanhThu") });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 8. Thống kê theo Ngày (Tìm kiếm LIKE theo định dạng Ngày)
    public List<Object[]> getDoanhThuTheoNgay(String tuKhoa) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT CAST(hd.ngayLap AS DATE) AS Ngay, " +
                     "COUNT(ct.maVe) AS SoVe, " +
                     "COUNT(DISTINCT hd.maHoaDon) AS SoHoaDon, " +
                     "SUM(hd.tongThanhToan) AS DoanhThu " +
                     "FROM HoaDon hd LEFT JOIN CT_HoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                     "WHERE CONVERT(varchar, hd.ngayLap, 23) LIKE ? " +
                     "GROUP BY CAST(hd.ngayLap AS DATE) ORDER BY Ngay DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps =prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{ rs.getString("Ngay"), rs.getInt("SoVe"), rs.getInt("SoHoaDon"), rs.getDouble("DoanhThu") });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 9. Thống kê theo Nhân Viên
    public List<Object[]> getDoanhThuTheoNhanVien(String tuKhoa) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT nv.tenNV AS TenNV, " +
                     "COUNT(ct.maVe) AS SoVe, " +
                     "COUNT(DISTINCT hd.maHoaDon) AS SoHoaDon, " +
                     "SUM(hd.tongThanhToan) AS DoanhThu " +
                     "FROM HoaDon hd LEFT JOIN CT_HoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                     "JOIN NhanVien nv ON hd.maNV = nv.maNV " +
                     "WHERE nv.tenNV LIKE ? OR nv.maNV LIKE ? " +
                     "GROUP BY nv.tenNV ORDER BY DoanhThu DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ps.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{ rs.getString("TenNV"), rs.getInt("SoVe"), rs.getInt("SoHoaDon"), rs.getDouble("DoanhThu") });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private PreparedStatement prepareStatement(String sql) throws Exception {
        return ConnectDB.getInstance().getConnection().prepareStatement(sql);
    }
}