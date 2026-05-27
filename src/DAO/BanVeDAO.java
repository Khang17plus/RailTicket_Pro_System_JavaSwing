package DAO;

import ConnectDB.ConnectDB;
import Entity.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BanVeDAO {

    // ==================== LẤY DANH SÁCH GA ====================
    public List<GaTau> getAllGa() {
        List<GaTau> list = new ArrayList<>();
        String sql = "SELECT maGa, tenGa, diaChi, soDienThoai, trangThai FROM GaTau WHERE trangThai = N'Đang hoạt động' ORDER BY maGa";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                GaTau ga = new GaTau();
                ga.setMaGa(rs.getString("maGa"));
                ga.setTenGa(rs.getString("tenGa"));
                ga.setDiaChi(rs.getString("diaChi"));
                ga.setSoDienThoai(rs.getString("soDienThoai"));
                ga.setTrangThai(rs.getString("trangThai"));
                list.add(ga);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==================== TÌM CHUYẾN TÀU ====================
    public List<ChuyenTau> timChuyenTheoGaVaNgay(String maGaDi, String maGaDen, LocalDate ngayDi) {
        List<ChuyenTau> list = new ArrayList<>();
        String sql = "SELECT c.*, t.tenTau, g1.tenGa as tenGaDi, g2.tenGa as tenGaDen " +
                     "FROM ChuyenTau c " +
                     "JOIN Tau t ON c.maTau = t.maTau " +
                     "JOIN GaTau g1 ON c.maGaDi = g1.maGa " +
                     "JOIN GaTau g2 ON c.maGaDen = g2.maGa " +
                     "WHERE c.maGaDi = ? AND c.maGaDen = ? AND CAST(c.thoiGianDi AS DATE) = ? AND c.trangThai = N'Sắp khởi hành'";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==================== LẤY DANH SÁCH TOA ====================
    public List<ToaTau> getToaByMaTau(String maTau) {
        List<ToaTau> list = new ArrayList<>();
        String sql = "SELECT * FROM ToaTau WHERE maTau = ?";
        
        try (Connection conn = ConnectDB.getInstance().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ToaTau(
                        rs.getString("maToa"), 
                        rs.getString("maTau"), 
                        rs.getString("tenToa"), 
                        rs.getString("loaiToa"), 
                        rs.getInt("sucChua")
                    ));
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // ==================== LẤY DANH SÁCH GHẾ (VÉ) ====================
    public List<VeTau> getVeByChuyenVaToa(String maChuyen, String maToa) {
        List<VeTau> list = new ArrayList<>();
        String sql = "SELECT g.maGhe, g.soGhe, g.loaiGhe, " +
                     "ISNULL(v.maVe, '') as maVe, " +
                     "ISNULL(v.giaGoc, 200000) as giaGoc, " +
                     "ISNULL(v.trangThai, N'Trống') as trangThai, " +
                     "ISNULL(v.tenHanhKhach, '') as tenHanhKhach, " +
                     "ISNULL(v.soCCCD, '') as soCCCD, " +
                     "ISNULL(v.loaiVe, '') as loaiVe " +
                     "FROM Ghe g " +
                     "LEFT JOIN VeTau v ON g.maGhe = v.maGhe AND v.maChuyen = ? " +
                     "WHERE g.maToa = ? ORDER BY g.soGhe ASC";
        
        try (Connection conn = ConnectDB.getInstance().getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maChuyen);
            ps.setString(2, maToa);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VeTau ve = new VeTau();
                    ve.setMaChuyen(maChuyen);
                    ve.setMaGhe(rs.getString("maGhe"));
                    ve.setSoGhe(rs.getInt("soGhe"));
                    ve.setLoaiGhe(rs.getString("loaiGhe"));
                    ve.setMaVe(rs.getString("maVe").isEmpty() ? null : rs.getString("maVe"));
                    ve.setGiaGoc(rs.getDouble("giaGoc"));
                    ve.setTrangThai(rs.getString("trangThai"));
                    ve.setTenHanhKhach(rs.getString("tenHanhKhach").isEmpty() ? null : rs.getString("tenHanhKhach"));
                    ve.setSoCCCD(rs.getString("soCCCD").isEmpty() ? null : rs.getString("soCCCD"));
                    ve.setLoaiVe(rs.getString("loaiVe").isEmpty() ? null : rs.getString("loaiVe"));
                    list.add(ve);
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // ==================== TÌM KHÁCH HÀNG THEO CCCD ====================
    public KhachHang findKhachHangByCCCD(String cccd) {
        String sql = "SELECT * FROM KhachHang WHERE cccd = ?";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==================== PHÁT SINH MÃ KHÁCH HÀNG TỰ ĐỘNG ====================
    /**
     * Phát sinh mã khách hàng mới theo format KHxxx (KH001, KH002,...)
     * GIỐNG HỆT cách làm bên KhachHangDAO
     */
    public String generateMaKhachHang() {
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY CAST(SUBSTRING(maKH, 3, LEN(maKH)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String maxMa = rs.getString("maKH");
                if (maxMa != null && !maxMa.isEmpty()) {
                    try {
                        String phanSoStr = maxMa.substring(2).trim();
                        int phanSo = Integer.parseInt(phanSoStr);
                        phanSo++;
                        return String.format("KH%03d", phanSo);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Nếu DB trống hoặc lỗi
        return "KH001";
    }

    // ==================== TẠO KHÁCH HÀNG MỚI ====================
    public String insertKhachHang(String tenKH, String cccd, String soDienThoai, String email) {
        // Phát sinh mã KH tự động
        String maKH = generateMaKhachHang();
        
        String sql = "INSERT INTO KhachHang(maKH, tenKH, cccd, soDienThoai, email, ngayDangKy) VALUES (?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            ps.setString(2, tenKH);
            ps.setString(3, cccd);
            ps.setString(4, soDienThoai);
            ps.setString(5, email);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                return maKH;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==================== PHÁT SINH MÃ HÓA ĐƠN TỰ ĐỘNG ====================
    /**
     * Phát sinh mã hóa đơn theo format HDxxxxx (HD00001, HD00002,...)
     */
    private String generateMaHoaDon() {
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY CAST(SUBSTRING(maHoaDon, 3, LEN(maHoaDon)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String maxMa = rs.getString("maHoaDon");
                if (maxMa != null && !maxMa.isEmpty()) {
                    try {
                        String phanSoStr = maxMa.substring(2).trim();
                        int phanSo = Integer.parseInt(phanSoStr);
                        phanSo++;
                        return String.format("HD%05d", phanSo);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "HD00001";
    }

    // ==================== PHÁT SINH MÃ VÉ TỰ ĐỘNG ====================
    /**
     * Phát sinh mã vé theo format VExxxxx (VE00001, VE00002,...)
     */
    private String generateMaVe() {
        String sql = "SELECT TOP 1 maVe FROM VeTau ORDER BY CAST(SUBSTRING(maVe, 3, LEN(maVe)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String maxMa = rs.getString("maVe");
                if (maxMa != null && !maxMa.isEmpty()) {
                    try {
                        String phanSoStr = maxMa.substring(2).trim();
                        int phanSo = Integer.parseInt(phanSoStr);
                        phanSo++;
                        return String.format("VE%05d", phanSo);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "VE00001";
    }

    // ==================== ĐẶT VÉ (THANH TOÁN) ====================
    public boolean datVe(String maChuyen, List<VeTau> dsVeChon, String maKH, String maNV, 
                         String phuongThucThanhToan, HoaDon hdOut) {
        String insertHD = "INSERT INTO HoaDon(maHoaDon, maKH, maNV, phuongThucThanhToan, tongTienHang, tongThanhToan, ngayLap) " +
                         "VALUES (?, ?, ?, ?, ?, ?, GETDATE())";
        String insertVe = "INSERT INTO VeTau(maVe, maChuyen, maGhe, giaGoc, trangThai, tenHanhKhach, soCCCD, loaiVe) " +
                         "VALUES (?, ?, ?, ?, N'Đã bán', ?, ?, ?)";
        String insertCTHD = "INSERT INTO CT_HoaDon(maHoaDon, maVe, giaBanThucTe, thanhTien) VALUES (?, ?, ?, ?)";
        
        Connection con = null;
        try {
            con = ConnectDB.getInstance().getConnection();
            con.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Phát sinh mã hóa đơn tự động
            String maHD = generateMaHoaDon();
            double tongTien = dsVeChon.stream().mapToDouble(VeTau::getGiaGoc).sum();
            
            // 2. Insert HoaDon
            try (PreparedStatement psHD = con.prepareStatement(insertHD)) {
                psHD.setString(1, maHD);
                psHD.setString(2, maKH);
                psHD.setString(3, maNV);
                psHD.setString(4, phuongThucThanhToan);
                psHD.setDouble(5, tongTien);
                psHD.setDouble(6, tongTien);
                psHD.executeUpdate();
            }

            // 3. Insert VeTau & CT_HoaDon cho từng ghế
            try (PreparedStatement psVe = con.prepareStatement(insertVe);
                 PreparedStatement psCTHD = con.prepareStatement(insertCTHD)) {
                
                for (VeTau ve : dsVeChon) {
                    // Phát sinh mã vé tự động
                    String maVeMoi = generateMaVe();
                    ve.setMaVe(maVeMoi);
                    
                    // Insert vé với thông tin hành khách
                    psVe.setString(1, maVeMoi);
                    psVe.setString(2, maChuyen);
                    psVe.setString(3, ve.getMaGhe());
                    psVe.setDouble(4, ve.getGiaGoc());
                    psVe.setString(5, ve.getTenHanhKhach());
                    psVe.setString(6, ve.getSoCCCD());
                    psVe.setString(7, ve.getLoaiVe());
                    psVe.executeUpdate();
                    
                    // Insert chi tiết hóa đơn
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
            hdOut.setMaNV(maNV);
            hdOut.setTongTienHang(tongTien);
            hdOut.setTongThanhToan(tongTien);
            hdOut.setPhuongThucThanhToan(phuongThucThanhToan);
            
            return true;
            
        } catch (SQLException e) {
            if (con != null) {
                try { 
                    con.rollback(); 
                } catch (SQLException ex) { 
                    ex.printStackTrace(); 
                }
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

    // ==================== MAPPER ====================
    
    /**
     * Map ResultSet → KhachHang (giống hệt bên KhachHangDAO)
     */
    private KhachHang mapKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("maKH"));
        kh.setTenKH(rs.getString("tenKH"));
        kh.setCccd(rs.getString("cccd"));
        kh.setSoDienThoai(rs.getString("soDienThoai"));
        kh.setEmail(rs.getString("email"));
        
        Timestamp ts = rs.getTimestamp("ngayDangKy");
        if (ts != null) {
            kh.setNgayDangKy(ts.toLocalDateTime());
        }
        
        return kh;
    }
}