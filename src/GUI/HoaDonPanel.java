package GUI;

import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import ConnectDB.ConnectDB;

import java.awt.*;
import java.io.FileOutputStream;
import java.sql.*;
import Entity.HoaDon;

public class HoaDonPanel extends JPanel {

    private Component component = new Component();
    private JTable table;
    private DefaultTableModel tableModel;
    
    // 🔥 BIẾN THÊM MỚI: Quản lý 2 Label KPI toàn cục để cập nhật số liệu thực tế
    private JLabel lblValueDoanhThu;
    private JLabel lblValueTongHD;

    public JButton createButtonExcel(String Cmt) {
        JButton btn = new JButton(Cmt);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 13;";
        if (Cmt.contains("In")) {
            btn.setBackground(new Color(59, 130, 246));
            btn.setForeground(Color.WHITE);
        } else if (Cmt.contains("Tìm")) {
            btn.setBackground(Color.gray);
            btn.setForeground(Color.BLACK);
            btn.setPreferredSize(new Dimension(80, 36));
        } else if (Cmt.contains("Chi tiết")) {
            btn.setBackground(new Color(255, 193, 7)); // Màu vàng cho nút Chi tiết
            btn.setForeground(Color.BLACK);
        } else {
            btn.setBackground(new Color(34, 197, 94)); // Xanh lá cho Excel
            btn.setForeground(Color.WHITE);
        }
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", style + "margin:8,15,8,15");
        return btn;
    }

    // 🔥 SỬA ĐỔI: Nhận trực tiếp JLabel từ ngoài truyền vào để không bị cố định giá trị cứng
    public JPanel createCardstatistical(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        JLabel iconLabel = new JLabel("📄"); 
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.putClientProperty("FlatLaf.style", "arc:10; border:10,10,10,10; background:#FFFFFF");
        return card;
    }

    public HoaDonPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250));

        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setBackground(new Color(245, 247, 250));
        
        JLabel title = new JLabel("Quản lý hóa đơn");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel sub = new JLabel("Hệ thống quản lý và in vé tàu RailWay_Pro");
        sub.setForeground(Color.GRAY);
        headerL.add(title);
        headerL.add(sub);

        // 🔥 KHỞI TẠO ĐỘNG: Tạo 2 Label động thay vì truyền chuỗi cứng cố định
        lblValueDoanhThu = new JLabel("0 VNĐ");
        lblValueTongHD = new JLabel("0");

        JPanel headerR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        headerR.setBackground(new Color(245, 247, 250));
        headerR.add(createCardstatistical("Doanh thu", lblValueDoanhThu));
        headerR.add(createCardstatistical("Tổng hóa đơn", lblValueTongHD));

        // --- ACTION PANEL ---
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(new Color(245, 247, 250));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(new Color(245, 247, 250));
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(350, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        txtSearch.putClientProperty("PlaceholderText", "Nhập mã hóa đơn cần tìm...");
        
        JButton btnSearch = createButtonExcel("Tìm Kiếm");
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(245, 247, 250));
        
        // Thêm các nút chức năng bên phải
        JButton btnDetail = createButtonExcel("Chi tiết");
        JButton btnInVe = createButtonExcel("In vé QR");
        JButton btnExcel = createButtonExcel("Xuất Excel");
        
        btnDetail.addActionListener(e -> hienThiChiTiet());
        btnInVe.addActionListener(e -> handleInVe());
        // btnExcel.addActionListener(e -> xuatFileExcel()); // Thịnh viết hàm Excel sau nhé
        
        rightPanel.add(btnDetail);
        rightPanel.add(btnInVe);
        rightPanel.add(btnExcel);

        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.EAST);

        header.add(headerL, BorderLayout.WEST);
        header.add(headerR, BorderLayout.EAST);
        header.add(actionPanel, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // --- MAIN TABLE ---
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        main.setBackground(new Color(245, 247, 250));

        String[] columns = {"Mã HĐ", "Ngày Lập", "Khách Hàng", "Thuế", "Giảm Giá", "Tổng Thanh Toán"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(40);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.putClientProperty("FlatLaf.style", "arc:20");
        main.add(scrollPane, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);

        // --- LẮNG NGHE SỰ KIỆN DOUBLE-CLICK TRÊN BẢNG ---
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Nhấp đúp chuột liên tiếp
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String maHD = tableModel.getValueAt(row, 0).toString();
                        // 🔥 Thay đổi lệnh thông báo cũ thành gọi cửa sổ hiển thị dữ liệu thật
                        hienThiDialogChiTiet(maHD);
                    }
                }
            }
        });

        // --- GỌI HÀM LOAD DỮ LIỆU TẠI ĐÂY ---
        loadDataFromDatabase(); 
    }

    private void hienThiChiTiet() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xem chi tiết!");
            return;
        }
        String maHD = tableModel.getValueAt(row, 0).toString();
        // 🔥 Gọi Dialog hiển thị dữ liệu thật thay vì hiển thị thông báo thông thường
        hienThiDialogChiTiet(maHD);
    }

    private void handleInVe() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return;
        }
        String maHD = tableModel.getValueAt(row, 0).toString();
        xuatVeQR(maHD);
    }

    // 🔥 CHỨC NĂNG MỚI THÊM: HIỂN THỊ CỬA SỔ DIALOG CHI TIẾT VÉ THUỘC HÓA ĐƠN
    private void hienThiDialogChiTiet(String maHD) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn: " + maHD, true);
        dialog.setSize(750, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Tiêu đề của Dialog phụ
        JLabel lblTitle = new JLabel("DANH SÁCH VÉ THUỘC HÓA ĐƠN: " + maHD, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        dialog.add(lblTitle, BorderLayout.NORTH);

        // Khởi tạo bảng danh sách vé chi tiết bên trong dialog
        String[] columnNames = {"Mã Vé", "Tên Hành Khách", "Số CCCD", "Loại Vé", "Giá Bán Thực Tế", "Thành Tiền"};
        DefaultTableModel detailModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Câu truy vấn lấy danh sách chi tiết các vé của mã hóa đơn này
        String sql = "SELECT ct.maVe, v.tenHanhKhach, v.soCCCD, v.loaiVe, ct.giaBanThucTe, ct.thanhTien " +
                     "FROM CT_HoaDon ct " +
                     "JOIN VeTau v ON ct.maVe = v.maVe " +
                     "WHERE ct.maHoaDon = ?";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detailModel.addRow(new Object[]{
                        rs.getString("maVe"),
                        rs.getNString("tenHanhKhach"),
                        rs.getString("soCCCD"),
                        rs.getNString("loaiVe"),
                        String.format("%,.0f VNĐ", rs.getDouble("giaBanThucTe")),
                        String.format("%,.0f VNĐ", rs.getDouble("thanhTien"))
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage());
        }

        // Trường hợp khẩn cấp nếu hóa đơn chưa có dữ liệu chi tiết vé
        if (detailModel.getRowCount() == 0) {
            detailModel.addRow(new Object[]{"Không tìm thấy dữ liệu vé chi tiết", "-", "-", "-", "-", "-"});
        }

        dialog.setVisible(true);
    }

    public void xuatVeQR(String maHD) {
        String sql = "SELECT v.maVe, kh.tenKH, kh.cccd, t.tenTau, gDi.tenGa, gDen.tenGa, ct.thoiGianDi, tt.tenToa, g.soGhe " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.maKH = kh.maKH " +
                     "JOIN CT_HoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
                     "JOIN VeTau v ON cthd.maVe = v.maVe " +
                     "JOIN ChuyenTau ct ON v.maChuyen = ct.maChuyen " +
                     "JOIN Tau t ON ct.maTau = t.maTau " +
                     "JOIN GaTau gDi ON ct.maGaDi = gDi.maGa " +
                     "JOIN GaTau gDen ON ct.maGaDen = gDen.maGa " +
                     "JOIN Ghe g ON v.maGhe = g.maGhe " +
                     "JOIN ToaTau tt ON g.maToa = tt.maToa " +
                     "WHERE hd.maHoaDon = ?";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A6);
            String path = "VeTau_" + maHD + ".pdf";
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            com.itextpdf.text.pdf.BaseFont bf = com.itextpdf.text.pdf.BaseFont.createFont("src/font/Arial Bold.ttf", com.itextpdf.text.pdf.BaseFont.IDENTITY_H, com.itextpdf.text.pdf.BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 10, com.itextpdf.text.Font.NORMAL);

            while (rs.next()) {
                document.add(new com.itextpdf.text.Paragraph("THẺ LÊN TÀU HỎA", fontBold));
                
                String maVe = rs.getString(1);
                com.itextpdf.text.pdf.BarcodeQRCode qr = new com.itextpdf.text.pdf.BarcodeQRCode(maVe, 1, 1, null);
                com.itextpdf.text.Image img = qr.getImage();
                img.scaleAbsolute(100, 100);
                img.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(img);

                document.add(new com.itextpdf.text.Paragraph("Họ tên: " + rs.getNString(2), fontNormal));
                document.add(new com.itextpdf.text.Paragraph("Số CCCD: " + rs.getString(3), fontNormal)); 
                document.add(new com.itextpdf.text.Paragraph("Tàu: " + rs.getString(4) + " - Ghế: " + rs.getInt(9), fontNormal));
                document.add(new com.itextpdf.text.Paragraph("Lộ trình: " + rs.getString(5) + " -> " + rs.getString(6), fontNormal));
                
                String thoiGianDi = rs.getTimestamp(7).toString().substring(0, 16); 
                document.add(new com.itextpdf.text.Paragraph("Ngày đi: " + thoiGianDi, fontNormal)); 
                
                document.add(new com.itextpdf.text.Paragraph("------------------------------------------", fontNormal));
                document.newPage(); 
            }
            document.close();
            Runtime.getRuntime().exec("cmd /c start \"\" \"" + path + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // 🔥 SỬA ĐỔI: Tự động cộng dồn doanh thu và số hóa đơn thực tế từ Database quét được
    public void loadDataFromDatabase() {
        String sql = "SELECT hd.maHoaDon, hd.ngayLap, kh.tenKH, hd.tongThue, hd.tongGiamGia, hd.tongThanhToan " +
                     "FROM HoaDon hd " +
                     "JOIN KhachHang kh ON hd.maKH = kh.maKH";

        tableModel.setRowCount(0); 
        
        double tongDoanhThuThucTe = 0;
        int tongSoHoaDonThucTe = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double thanhToan = rs.getDouble("tongThanhToan");
                tongDoanhThuThucTe += thanhToan;
                tongSoHoaDonThucTe++;

                tableModel.addRow(new Object[]{
                    rs.getString("maHoaDon"),
                    rs.getTimestamp("ngayLap"),
                    rs.getNString("tenKH"),
                    String.format("%,.0f", rs.getDouble("tongThue")),      
                    String.format("%,.0f", rs.getDouble("tongGiamGia")),   
                    String.format("%,.0f VNĐ", thanhToan) 
                });
            }
            
            // 🔥 Đồng bộ hiển thị số liệu thật lên 2 thẻ Card góc trên bên phải màn hình
            lblValueDoanhThu.setText(String.format("%,.0f VNĐ", tongDoanhThuThucTe));
            lblValueTongHD.setText(String.valueOf(tongSoHoaDonThucTe));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData(List<HoaDon> ds) {
        // 1. Xóa toàn bộ dữ liệu cũ trên bảng trước khi đổ mới
        tableModel.setRowCount(0);

        double tongDoanhThuThucTe = 0;
        int tongSoHoaDonThucTe = 0;

        // 2. Duyệt qua danh sách hóa đơn từ Database/DAO truyền vào
        for (HoaDon hd : ds) {
            tongDoanhThuThucTe += hd.getTongThanhToan();
            tongSoHoaDonThucTe++;

            // Định dạng số tiền để hiển thị đẹp mắt (ví dụ: 1,000,000 VNĐ)
            String thueStr = String.format("%,.0f", hd.getTongThue());
            String giamGiaStr = String.format("%,.0f", hd.getTongGiamGia());
            String tongTienStr = String.format("%,.0f VNĐ", hd.getTongThanhToan());

            // 3. Thêm dòng mới vào tableModel theo đúng thứ tự các cột:
            tableModel.addRow(new Object[] {
                hd.getMaHoaDon(),               // Cột Mã HĐ
                hd.getNgayLap(),                // Cột Ngày Lập
                hd.getMaKH(),                   // Cột Khách Hàng
                thueStr,                        // Cột Thuế
                giamGiaStr,                     // Cột Giảm Giá
                tongTienStr                     // Cột Tổng Thanh Toán
            });
        }
        
        // Cập nhật lại KPI nếu gọi qua hàm setData
        if (lblValueDoanhThu != null && lblValueTongHD != null) {
            lblValueDoanhThu.setText(String.format("%,.0f VNĐ", tongDoanhThuThucTe));
            lblValueTongHD.setText(String.valueOf(tongSoHoaDonThucTe));
        }
    }
}