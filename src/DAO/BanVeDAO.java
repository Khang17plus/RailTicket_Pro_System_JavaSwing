package DAO;

import ConnectDB.ConnectDB;
import Entity.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanVeDAO {

    public List<ChuyenTau> timChuyenTheoGaVaNgay(String maGaDi, String maGaDen, LocalDate ngayDi) {
        List<ChuyenTau> list = new ArrayList<>();
        String sql = "SELECT c.*, t.tenTau, g1.tenGa as tenGaDi, g2.tenGa as tenGaDen " +
                     "FROM ChuyenTau c " +
                     "JOIN Tau t ON c.maTau = t.maTau " +
                     "JOIN GaTau g1 ON c.maGaDi = g1.maGa " +
                     "JOIN GaTau g2 ON c.maGaDen = g2.maGa " +
                     "WHERE c.maGaDi = ? AND c.maGaDen = ? AND CAST(c.thoiGianDi AS DATE) = ? AND c.trangThai = N'Sẵn sàng'";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maGaDi);
            ps.setString(2, maGaDen);
            ps.setDate(3, Date.valueOf(ngayDi));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChuyenTau ct = new ChuyenTau();
                    ct.setMaChuyen(rs.getString("maChuyen"));
                    ct.setMaTau(rs.getString("maTau"));
                    ct.setTenTau(rs.getString("tenTau"));
                    ct.setMaGaDi(rs.getString("maGaDi"));
                    ct.setTenGaDi(rs.getString("tenGaDi"));
                    ct.setMaGaDen(rs.getString("maGaDen"));
                    ct.setTenGaDen(rs.getString("tenGaDen"));
                    ct.setThoiGianDi(rs.getTimestamp("thoiGianDi").toLocalDateTime());
                    ct.setThoiGianDen(rs.getTimestamp("thoiGianDen").toLocalDateTime());
                    ct.setTrangThai(rs.getString("trangThai"));
                    list.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ToaTau> getToaByMaTau(String maTau) {
        List<ToaTau> list = new ArrayList<>();
        String sql = "SELECT * FROM ToaTau WHERE maTau = ?";
        
        try (Connection con = ConnectDB.getInstance().getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ToaTau(rs.getString("maToa"), rs.getString("maTau"), 
                            rs.getString("tenToa"), rs.getString("loaiToa"), rs.getInt("sucChua")));
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public List<VeTau> getVeByChuyenVaToa(String maChuyen, String maToa) {
        List<VeTau> list = new ArrayList<>();
        String sql = "SELECT g.maGhe, g.soGhe, " +
                     "ISNULL(v.maVe, '') as maVe, " +
                     "ISNULL(v.giaGoc, 500000) as giaGoc, " +
                     "ISNULL(v.trangThai, N'Trống') as trangThai " +
                     "FROM Ghe g " +
                     "LEFT JOIN VeTau v ON g.maGhe = v.maGhe AND v.maChuyen = ? " +
                     "WHERE g.maToa = ? ORDER BY g.soGhe ASC";
        
        try (Connection con = ConnectDB.getInstance().getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maChuyen);
            ps.setString(2, maToa);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VeTau ve = new VeTau();
                    ve.setMaChuyen(maChuyen);
                    ve.setMaGhe(rs.getString("maGhe"));
                    ve.setSoGhe(rs.getInt("soGhe"));
                    ve.setMaVe(rs.getString("maVe").isEmpty() ? null : rs.getString("maVe"));
                    ve.setGiaGoc(rs.getDouble("giaGoc"));
                    ve.setTrangThai(rs.getString("trangThai"));
                    list.add(ve);
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    public boolean datVe(String maChuyen, List<VeTau> dsVeChon, String maKH, String maNV, String phuongThucThanhToan, HoaDon hdOut) {
        String insertHD = "INSERT INTO HoaDon(maHoaDon, maKH, maNV, phuongThucThanhToan, tongTienHang, tongThanhToan, ngayLap) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertVe = "INSERT INTO VeTau(maVe, maChuyen, maGhe, giaGoc, trangThai) VALUES (?, ?, ?, ?, N'Đã bán')";
        String insertCTHD = "INSERT INTO CT_HoaDon(maHoaDon, maVe, giaBanThucTe, thanhTien) VALUES (?, ?, ?, ?)";
        
        Connection con = null;
        try {
            con = ConnectDB.getInstance().getConnection();
            con.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Tạo mã HD và lưu thông tin chung
            String maHD = "HD" + System.currentTimeMillis();
            double tongTien = dsVeChon.stream().mapToDouble(VeTau::getGiaGoc).sum();
            
            try (PreparedStatement psHD = con.prepareStatement(insertHD)) {
                psHD.setString(1, maHD);
                psHD.setString(2, maKH);
                psHD.setString(3, maNV);
                psHD.setString(4, phuongThucThanhToan);
                psHD.setDouble(5, tongTien);
                psHD.setDouble(6, tongTien);
                psHD.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
                psHD.executeUpdate();
            }

            // 2. Insert VeTau & CT_HoaDon
            try (PreparedStatement psVe = con.prepareStatement(insertVe);
                 PreparedStatement psCTHD = con.prepareStatement(insertCTHD)) {
                
                for (VeTau ve : dsVeChon) {
                    String maVeMoi = "VE" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    ve.setMaVe(maVeMoi);
                    
                    psVe.setString(1, maVeMoi);
                    psVe.setString(2, maChuyen);
                    psVe.setString(3, ve.getMaGhe());
                    psVe.setDouble(4, ve.getGiaGoc());
                    psVe.executeUpdate();
                    
                    psCTHD.setString(1, maHD);
                    psCTHD.setString(2, maVeMoi);
                    psCTHD.setDouble(3, ve.getGiaGoc());
                    psCTHD.setDouble(4, ve.getGiaGoc());
                    psCTHD.executeUpdate();
                }
            }

            con.commit(); // Hoàn tất giao dịch
            
            // Cập nhật dữ liệu trả về cho đối tượng hdOut
            hdOut.setMaHoaDon(maHD);
            hdOut.setMaKH(maKH);
            hdOut.setTongThanhToan(tongTien);
            
            return true;
        } catch (Exception e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { 
                    con.setAutoCommit(true); 
                    con.close(); 
                } catch (SQLException ex) { 
                    ex.printStackTrace(); 
                }
            }
        }
    }
}