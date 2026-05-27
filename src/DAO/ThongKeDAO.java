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

    // 1. Thống kê số vé bán ra hôm nay
    public int getSoVeBanHomNay() {
        String sql = "SELECT COUNT(*) FROM Ve v JOIN HoaDon hd ON v.maHoaDon = hd.maHoaDon " +
                     "WHERE CAST(hd.ngayLap AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 2. Thống kê tổng doanh thu hôm nay
    public double getDoanhThuHomNay() {
        String sql = "SELECT COALESCE(SUM(tongTien), 0) FROM HoaDon " +
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

    // 5. Lấy dữ liệu Doanh thu trong tuần hiện tại (Từ Thứ 2 đến Chủ Nhật) để vẽ biểu đồ Cột
    public Map<String, Double> getDoanhThuTheoTuan() {
        Map<String, Double> map = new LinkedHashMap<>();
        // Khởi tạo mặc định các thứ bằng 0
        map.put("T2", 0.0); map.put("T3", 0.0); map.put("T4", 0.0);
        map.put("T5", 0.0); map.put("T6", 0.0); map.put("T7", 0.0); map.put("CN", 0.0);

        String sql = "SET DATEFIRST 1; " + 
                     "SELECT DATEPART(WEEKDAY, ngayLap) AS Thu, SUM(tongTien) AS DoanhThu " +
                     "FROM HoaDon WHERE ngayLap >= DATEADD(wk, DATEDIFF(wk, 6, GETDATE()), 6) " +
                     "GROUP BY DATEPART(WEEKDAY, ngayLap)";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int thu = rs.getInt("Thu");
                double bieuDoDoanhThu = rs.getDouble("DoanhThu");
                switch (thu) {
                    case 1: map.put("T2", bieuDoDoanhThu); break;
                    case 2: map.put("T3", bieuDoDoanhThu); break;
                    case 3: map.put("T4", bieuDoDoanhThu); break;
                    case 4: map.put("T5", bieuDoDoanhThu); break;
                    case 5: map.put("T6", bieuDoDoanhThu); break;
                    case 6: map.put("T7", bieuDoDoanhThu); break;
                    case 7: map.put("CN", bieuDoDoanhThu); break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    // 6. Lấy số lượng Vé bán ra trong tuần hiện tại để vẽ biểu đồ Đường
    public Map<String, Integer> getSoVeBanTheoTuan() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("T2", 0); map.put("T3", 0); map.put("T4", 0);
        map.put("T5", 0); map.put("T6", 0); map.put("T7", 0); map.put("CN", 0);

        String sql = "SET DATEFIRST 1; " +
                     "SELECT DATEPART(WEEKDAY, hd.ngayLap) AS Thu, COUNT(v.maVe) AS SoVe " +
                     "FROM Ve v JOIN HoaDon hd ON v.maHoaDon = hd.maHoaDon " +
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

    /**
     * 🔥 HÀM MỚI BỔ SUNG: Lấy dữ liệu thống kê doanh thu và số vé theo từng tháng của năm hiện tại
     * Trả về danh sách mảng Object phối hợp: [Tên Tháng, Số Vé, Số Hóa Đơn, Doanh Thu]
     */
    public List<Object[]> getDoanhThuTheoThang() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT " +
                     "    MONTH(hd.ngayLap) AS Thang, " +
                     "    COUNT(v.maVe) AS SoVe, " +
                     "    COUNT(DISTINCT hd.maHoaDon) AS SoHoaDon, " +
                     "    SUM(hd.tongTien) AS DoanhThu " +
                     "FROM HoaDon hd " +
                     "LEFT JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                     "WHERE YEAR(hd.ngayLap) = YEAR(GETDATE()) " +
                     "GROUP BY MONTH(hd.ngayLap) " +
                     "ORDER BY Thang ASC";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = "Tháng " + rs.getInt("Thang"); // Cột 0: Tên hiển thị trên bảng/đồ thị
                row[1] = rs.getInt("SoVe");            // Cột 1: Số vé (Dùng cho tongVe += trong Controller)
                row[2] = rs.getInt("SoHoaDon");        // Cột 2: Số hóa đơn bán được
                row[3] = rs.getDouble("DoanhThu");      // Cột 3: Tổng tiền (Dùng cho tongDoanhThu += trong Controller)
                
                list.add(row);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy dữ liệu thống kê doanh thu theo tháng!");
            e.printStackTrace();
        }
        return list;
    }
    /**
     * THỐNG KÊ THEO NGÀY (Có hỗ trợ tìm kiếm theo chuỗi ngày yyyy-MM-dd)
     */
    public List<Object[]> getDoanhThuTheoNgay(String tuKhoa) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT CAST(hd.ngayLap AS DATE) AS Ngay, " +
                     "COUNT(v.maVe) AS SoVe, " +
                     "COUNT(DISTINCT hd.maHoaDon) AS SoHoaDon, " +
                     "SUM(hd.tongTien) AS DoanhThu " +
                     "FROM HoaDon hd LEFT JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                     "WHERE CAST(hd.ngayLap AS DATE) LIKE ? " +
                     "GROUP BY CAST(hd.ngayLap AS DATE) ORDER BY Ngay DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{ rs.getString("Ngay"), rs.getInt("SoVe"), rs.getInt("SoHoaDon"), rs.getDouble("DoanhThu") });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /**
     * THỐNG KÊ THEO NHÂN VIÊN (Có hỗ trợ tìm tên hoặc mã NV)
     * (Lưu ý: Thay đổi tên bảng/cột NhanVien cho đúng với DB của bạn nếu cần)
     */
    public List<Object[]> getDoanhThuTheoNhanVien(String tuKhoa) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT nv.tenNhanVien AS TenNV, " +
                     "COUNT(v.maVe) AS SoVe, " +
                     "COUNT(DISTINCT hd.maHoaDon) AS SoHoaDon, " +
                     "SUM(hd.tongTien) AS DoanhThu " +
                     "FROM HoaDon hd " +
                     "LEFT JOIN Ve v ON hd.maHoaDon = v.maHoaDon " +
                     "JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " + // Đảm bảo bảng NhanVien của bạn có cột maNhanVien và tenNhanVien
                     "WHERE nv.tenNhanVien LIKE ? OR nv.maNhanVien LIKE ? " +
                     "GROUP BY nv.tenNhanVien ORDER BY DoanhThu DESC";
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
}