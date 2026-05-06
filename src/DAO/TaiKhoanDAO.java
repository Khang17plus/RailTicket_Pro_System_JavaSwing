package DAO;

import Entity.TaiKhoan;
import ConnectDB.ConnectDB; // Import class ConnectDB của bạn
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {
    
    public TaiKhoan checkLogin(String maNV, String matKhau) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TaiKhoan WHERE maNV = ? AND matKhau = ?";
        
        // Gọi đến ConnectDB.getInstance().getConnection() theo đúng cấu trúc của bạn
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            if (con != null) {
                pst.setString(1, maNV);
                pst.setString(2, matKhau);
                
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        tk = new TaiKhoan(
                            rs.getString("maNV"),
                            rs.getString("matKhau"),
                            rs.getString("vaiTro")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi truy vấn đăng nhập!");
        }
        
        return tk;
    }
}