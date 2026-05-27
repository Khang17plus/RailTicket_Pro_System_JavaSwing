package DAO;

import ConnectDB.ConnectDB;
import Entity.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BanVeDAO {

    // ==================== LẤY DANH SÁCH GA TÀU ĐANG CHẠY ====================
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

    // ==================== TÌM CHUYẾN TÀU (QUÉT MỞ RỘNG TRẠNG THÁI DỮ LIỆU) ====================
    public List<ChuyenTau> timChuyenTheoGaVaNgay(String maGaDi, String maGaDen, LocalDate ngayDi) {
        List<ChuyenTau> list = new ArrayList<>();
        String sql = "SELECT c.*, t.tenTau, g1.tenGa as tenGaDi, g2.tenGa as tenGaDen " +
                     "FROM ChuyenTau c " +
                     "JOIN Tau t ON c.maTau = t.maTau " +
                     "JOIN GaTau g1 ON c.maGaDi = g1.maGa " +
                     "JOIN GaTau g2 ON c.maGaDen = g2.maGa " +
                     "WHERE c.maGaDi = ? AND c.maGaDen = ? AND CAST(c.thoiGianDi AS DATE) = ? " +
                     "AND (c.trangThai = N'Sắp khởi hành' OR c.trangThai = N'Sẵn sàng' OR c.trangThai = N'Đang vận hành')";
        
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

    // ==================== LẤY TOA TÀU THEO MÃ TÀU ====================
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

    // ==================== LẤY DANH SÁCH GHẾ THEO TOA & CHUYẾN CHỌN ====================
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

    // ==================== PHÁT SINH MÃ TỰ ĐỘNG KHÁCH HÀNG ====================
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

    // ==================== TRANSACTION ĐẶT VÉ ĐA BẢNG AN TOÀN ====================
    public boolean datVe(String maChuyen, List<VeTau> dsVe, String maKH, String maNV, String pt,
                          String maKM, String maThue, double giam, double thue, HoaDon hdOut) {
        Connection conn = null;
        try {
            conn = getNewConnection();
            conn.setAutoCommit(false); // Bật chế độ quản lý Transaction thủ công

            String maHD = generateMaHoaDon();
            double tongGoc = dsVe.stream().mapToDouble(VeTau::getGiaGoc).sum();
            double tongTT = tongGoc - giam + thue;

            // 1. Ghi nhận thông tin bảng HoaDon chính
            String sqlHD = "INSERT INTO HoaDon(maHoaDon, maKH, maNV, ngayLap, tongTienHang, tongThue, tongGiamGia, tongThanhToan, phuongThucThanhToan) " +
                           "VALUES (?, ?, ?, GETDATE(), ?, ?, ?, ?, ?)";
            try (PreparedStatement psHD = conn.prepareStatement(sqlHD)) {
                psHD.setString(1, maHD);
                psHD.setString(2, maKH);
                psHD.setString(3, maNV);
                psHD.setDouble(4, tongGoc);
                psHD.setDouble(5, thue);
                psHD.setDouble(6, giam);
                psHD.setDouble(7, tongTT);
                psHD.setString(8, pt);
                psHD.executeUpdate();
            }

            // 2. Chèn dữ liệu tuần tự các vé và chi tiết chặng hóa đơn phụ tương ứng
            String sqlVe = "INSERT INTO VeTau(maVe, maChuyen, maGhe, giaGoc, trangThai, tenHanhKhach, soCCCD, loaiVe) " +
                           "VALUES (?, ?, ?, ?, N'Đã bán', ?, ?, ?)";
            String sqlCT = "INSERT INTO CT_HoaDon(maHoaDon, maVe, maThue, maKM, giaBanThucTe, tienThue, tienGiamGia, thanhTien) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            double soVe = dsVe.size();
            double giamVe = giam / soVe;
            double thueVe = thue / soVe;

            String currentMaVeStr = generateMaVe();
            int currentMaVeNum = Integer.parseInt(currentMaVeStr.substring(2));

            try (PreparedStatement psVe = conn.prepareStatement(sqlVe);
                 PreparedStatement psCT = conn.prepareStatement(sqlCT)) {

                for (VeTau ve : dsVe) {
                    String maVe = String.format("VE%05d", currentMaVeNum++);
                    ve.setMaVe(maVe);

                    // Insert dữ liệu VeTau
                    psVe.setString(1, maVe);
                    psVe.setString(2, maChuyen);
                    psVe.setString(3, ve.getMaGhe());
                    psVe.setDouble(4, ve.getGiaGoc());
                    psVe.setString(5, ve.getTenHanhKhach() != null ? ve.getTenHanhKhach() : "Khách");
                    psVe.setString(6, ve.getSoCCCD() != null ? ve.getSoCCCD() : "000000000000");
                    psVe.setString(7, ve.getLoaiVe() != null ? ve.getLoaiVe() : "Người lớn");
                    psVe.executeUpdate();

                    // Insert dữ liệu CT_HoaDon tương ứng khóa ngoại mã vé trên
                    psCT.setString(1, maHD);
                    psCT.setString(2, maVe);
                    psCT.setString(3, maThue);
                    psCT.setString(4, maKM);
                    psCT.setDouble(5, ve.getGiaGoc());
                    psCT.setDouble(6, thueVe);
                    psCT.setDouble(7, giamVe);
                    psCT.setDouble(8, ve.getGiaGoc() - giamVe + thueVe);
                    psCT.executeUpdate();
                }
            }

            // Commit xác nhận thành công lưu trữ vĩnh viễn
            conn.commit();

            // Đóng gói trả thực thể đầu ra phục vụ in ấn hóa đơn giao diện
            hdOut.setMaHoaDon(maHD);
            hdOut.setMaKH(maKH);
            hdOut.setMaNV(maNV);
            hdOut.setTongTienHang(tongGoc);
            hdOut.setTongThue(thue);
            hdOut.setTongGiamGia(giam);
            hdOut.setTongThanhToan(tongTT);
            hdOut.setPhuongThucThanhToan(pt);
            
            return true;

        } catch (Exception e) {
            System.err.println("❌ Lỗi xảy ra trong quá trình đặt vé: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác dữ liệu bảo vệ hệ thống khi dính lỗi nửa chừng
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    // Kết nối Driver độc lập
    private Connection getNewConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=RailWayTicketDB;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "1"; 
        return DriverManager.getConnection(url, user, password);
    }

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