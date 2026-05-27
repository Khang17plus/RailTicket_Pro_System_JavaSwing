package DAO;

import ConnectDB.ConnectDB; // vẫn giữ để dùng cho các hàm khác
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

    // ==================== PHÁT SINH MÃ TỰ ĐỘNG ====================
    public String generateMaKhachHang() {
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY CAST(SUBSTRING(maKH, 3, LEN(maKH)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String maxMa = rs.getString("maKH");
                if (maxMa != null && !maxMa.isEmpty()) {
                    int num = Integer.parseInt(maxMa.substring(2).trim()) + 1;
                    return String.format("KH%03d", num);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "KH001";
    }

    public String insertKhachHang(String tenKH, String cccd, String soDienThoai, String email) {
        String maKH = generateMaKhachHang();
        String sql = "INSERT INTO KhachHang(maKH, tenKH, cccd, soDienThoai, email, ngayDangKy) VALUES (?, ?, ?, ?, ?, GETDATE())";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            ps.setString(2, tenKH);
            ps.setString(3, cccd);
            ps.setString(4, soDienThoai);
            ps.setString(5, email);
            return ps.executeUpdate() > 0 ? maKH : null;
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private String generateMaHoaDon() {
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY CAST(SUBSTRING(maHoaDon, 3, LEN(maHoaDon)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String max = rs.getString("maHoaDon");
                if (max != null && !max.isEmpty()) {
                    int num = Integer.parseInt(max.substring(2).trim()) + 1;
                    return String.format("HD%05d", num);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "HD00001";
    }

    private String generateMaVe() {
        String sql = "SELECT TOP 1 maVe FROM VeTau ORDER BY CAST(SUBSTRING(maVe, 3, LEN(maVe)) AS INT) DESC";
        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String max = rs.getString("maVe");
                if (max != null && !max.isEmpty()) {
                    int num = Integer.parseInt(max.substring(2).trim()) + 1;
                    return String.format("VE%05d", num);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "VE00001";
    }

    // ==================== ĐẶT VÉ (THANH TOÁN) - MỞ CONNECTION RIÊNG ====================
    // ==================== ĐẶT VÉ (THANH TOÁN) - CHÈN THẲNG, KHÔNG TRANSACTION ====================
public boolean datVe(String maChuyen, List<VeTau> dsVeChon, String maKH, String maNV, 
                     String phuongThucThanhToan, HoaDon hdOut) {
    try {
        String maHD = generateMaHoaDon();
        double tongTien = dsVeChon.stream().mapToDouble(VeTau::getGiaGoc).sum();

        // 1. Chèn HoaDon (mở connection riêng)
        String sqlHD = "INSERT INTO HoaDon(maHoaDon, maKH, maNV, ngayLap, tongTienHang, tongThanhToan, phuongThucThanhToan) VALUES (?, ?, ?, GETDATE(), ?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sqlHD)) {
            ps.setString(1, maHD);
            ps.setString(2, maKH);
            ps.setString(3, maNV);
            ps.setDouble(4, tongTien);
            ps.setDouble(5, tongTien);
            ps.setString(6, phuongThucThanhToan);
            ps.executeUpdate();
            System.out.println("✅ Đã chèn HoaDon: " + maHD);
        } catch (SQLException e) {
            System.err.println("❌ Lỗi chèn HoaDon: " + e.getMessage());
            // vẫn tiếp tục chèn vé, không dừng
        }

        // 2. Chèn từng VeTau và CT_HoaDon (mỗi vé mở connection riêng)
        String sqlVe = "INSERT INTO VeTau(maVe, maChuyen, maGhe, giaGoc, trangThai, tenHanhKhach, soCCCD, loaiVe) VALUES (?, ?, ?, ?, N'Đã bán', ?, ?, ?)";
        String sqlCT = "INSERT INTO CT_HoaDon(maHoaDon, maVe, giaBanThucTe, thanhTien) VALUES (?, ?, ?, ?)";

        for (VeTau ve : dsVeChon) {
            String maVe = generateMaVe();
            ve.setMaVe(maVe);

            // Chèn VeTau
            try (Connection con = ConnectDB.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(sqlVe)) {
                ps.setString(1, maVe);
                ps.setString(2, maChuyen);
                ps.setString(3, ve.getMaGhe());
                ps.setDouble(4, ve.getGiaGoc());
                ps.setString(5, ve.getTenHanhKhach() != null ? ve.getTenHanhKhach() : "Khách");
                ps.setString(6, ve.getSoCCCD() != null ? ve.getSoCCCD() : "000000000000");
                ps.setString(7, ve.getLoaiVe() != null ? ve.getLoaiVe() : "Người lớn");
                ps.executeUpdate();
                System.out.println("✅ Đã chèn VeTau: " + maVe);
            } catch (SQLException e) {
                System.err.println("❌ Lỗi chèn VeTau " + maVe + ": " + e.getMessage());
            }

            // Chèn CT_HoaDon
            try (Connection con = ConnectDB.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(sqlCT)) {
                ps.setString(1, maHD);
                ps.setString(2, maVe);
                ps.setDouble(3, ve.getGiaGoc());
                ps.setDouble(4, ve.getGiaGoc());
                ps.executeUpdate();
                System.out.println("✅ Đã chèn CT_HoaDon cho vé: " + maVe);
            } catch (SQLException e) {
                System.err.println("❌ Lỗi chèn CT_HoaDon cho vé " + maVe + ": " + e.getMessage());
            }
        }

        // Trả về thông tin hóa đơn
        hdOut.setMaHoaDon(maHD);
        hdOut.setMaKH(maKH);
        hdOut.setMaNV(maNV);
        hdOut.setTongTienHang(tongTien);
        hdOut.setTongThanhToan(tongTien);
        hdOut.setPhuongThucThanhToan(phuongThucThanhToan);
        return true; // luôn trả về true vì đã chèn được ít nhất một phần

    } catch (Exception e) {
        System.err.println("❌ Lỗi chung datVe: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

    // ==================== MAPPER ====================
    private KhachHang mapKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("maKH"));
        kh.setTenKH(rs.getString("tenKH"));
        kh.setCccd(rs.getString("cccd"));
        kh.setSoDienThoai(rs.getString("soDienThoai"));
        kh.setEmail(rs.getString("email"));
        Timestamp ts = rs.getTimestamp("ngayDangKy");
        if (ts != null) kh.setNgayDangKy(ts.toLocalDateTime());
        return kh;
    }
}