package DAO;

import java.sql.*;
import java.util.*;

import ConnectDB.ConnectDB;


public class ThongKeDAO {
    
    // ĐÃ FIX: Sửa tên hàm từ "getDoanh List<Object[]> ThuTheoThang" thành "getDoanhThuTheoThang"
    public List<Object[]> getDoanhThuTheoThang() {
        List<Object[]> data = new ArrayList<>();
     // Cách này dùng CONCAT và MONTH/YEAR để tạo chuỗi MM-yyyy mà không cần CLR
        String sql = "SELECT " +
                     "    RIGHT('0' + CAST(MONTH(hd.ngayLap) AS VARCHAR), 2) + '-' + CAST(YEAR(hd.ngayLap) AS VARCHAR) as ThoiGian, " +
                     "    COUNT(ct.maVe) as SoVe, " +
                     "    SUM(hd.tongTienHang) as DoanhThu, " +
                     "    SUM(hd.tongThanhToan) as ThucThu " +
                     "FROM HoaDon hd " +
                     "JOIN CT_HoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
                     "GROUP BY MONTH(hd.ngayLap), YEAR(hd.ngayLap) " +
                     "ORDER BY YEAR(hd.ngayLap) DESC, MONTH(hd.ngayLap) DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new Object[]{
                    rs.getString(1), 
                    rs.getInt(2),    
                    rs.getDouble(3), 
                    rs.getDouble(4)  
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}