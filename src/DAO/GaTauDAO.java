package DAO;

import ConnectDB.ConnectDB;
import Entity.GaTau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GaTauDAO {

    // 🔹 Lấy tất cả ga tàu
    public List<GaTau> getAll() {
        List<GaTau> list = new ArrayList<>();
        String sql = "SELECT * FROM GaTau";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                GaTau ga = mapResultSet(rs);
                list.add(ga);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Tìm ga tàu theo mã
    public GaTau findById(String maGa) {
        String sql = "SELECT * FROM GaTau WHERE maGa = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 Thêm ga tàu mới
    public boolean insert(GaTau ga) {
        // Giả định bảng có các cột: maGa, tenGa, diaChi, soDienThoai, trangThai
        String sql = "INSERT INTO GaTau(maGa, tenGa, diaChi, soDienThoai, trangThai) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ga.getMaGa());
            ps.setString(2, ga.getTenGa());
            ps.setString(3, ga.getDiaChi());
            ps.setString(4, ga.getSoDienThoai());
            ps.setString(5, ga.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
 // 🔹 Lấy mã ga lớn nhất hiện tại để phục vụ phát sinh mã tự động
    public String getMaxMaGa() {
        String sql = "SELECT MAX(maGa) FROM GaTau";
        String maxMa = null;

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                maxMa = rs.getString(1); // Lấy giá trị đầu tiên của kết quả (MAX(maGa))
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return maxMa;
    }

    // 🔹 Cập nhật ga tàu
    public boolean update(GaTau ga) {
        String sql = "UPDATE GaTau SET tenGa=?, diaChi=?, soDienThoai=?, trangThai=? WHERE maGa=?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ga.getTenGa());
            ps.setString(2, ga.getDiaChi());
            ps.setString(3, ga.getSoDienThoai());
            ps.setString(4, ga.getTrangThai());
            ps.setString(5, ga.getMaGa());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 Xóa ga tàu
    public boolean delete(String maGa) {
        String sql = "DELETE FROM GaTau WHERE maGa = ?";

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGa);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 🔹 Tìm kiếm ga tàu (Ví dụ: theo Tên ga hoặc Địa chỉ)
    public List<GaTau> searchGaTau(String keyword) {
        List<GaTau> list = new ArrayList<>();
        String sql = "SELECT * FROM GaTau WHERE tenGa LIKE ? OR diaChi LIKE ?";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
             
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern); 
            pst.setString(2, searchPattern); 
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔥 Hàm map ResultSet → Object
    private GaTau mapResultSet(ResultSet rs) throws SQLException {
        GaTau ga = new GaTau();
        ga.setMaGa(rs.getString("maGa"));
        
        // Giả định bạn có cột tenGa trong database
        ga.setTenGa(rs.getString("tenGa")); 
        
        ga.setDiaChi(rs.getString("diaChi"));
        ga.setSoDienThoai(rs.getString("soDienThoai"));
        ga.setTrangThai(rs.getString("trangThai"));

        return ga;
    }
}