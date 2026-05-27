package Controller;

import java.util.List;
import DAO.KhuyenMaiDAO; 
import Entity.KhuyenMai; 
import GUI.KhuyenMaiPanel;

public class KhuyenMaiController {
    
    private KhuyenMaiPanel view;
    private KhuyenMaiDAO dao;
    
    public KhuyenMaiController(KhuyenMaiPanel view) {
        this.view = view;
        this.dao = new KhuyenMaiDAO();
        
        // Tự động đổ dữ liệu khi khởi tạo
        loadDataToTable();
    }
    
    /**
     * Lấy toàn bộ danh sách khuyến mãi từ DB và hiển thị lên bảng
     */
    public void loadDataToTable() {
        List<KhuyenMai> list = dao.getAll();
        view.setData(list);
        view.capNhatThongKeCoDinh(list); // Gọi tính toán dựa trên full danh sách gốc
    }
    
    /**
     * Thêm mới một chương trình khuyến mãi
     */
    public boolean themKhuyenMai(KhuyenMai km) {
        return dao.insert(km); 
    }
    
    /**
     * Cập nhật thông tin khuyến mãi đã tồn tại
     */
    public boolean updateKhuyenMai(KhuyenMai km) {
        return dao.update(km);
    }
    
    /**
     * Xóa khuyến mãi theo mã
     */
    public boolean xoaKhuyenMai(String maKM) {
        return dao.delete(maKM);
    }
    
    /**
     * Tìm kiếm khuyến mãi theo từ khóa (Mã hoặc Tên)
     */
    public void timKiemKhuyenMai(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable();
        } else {
            List<KhuyenMai> listKetQuaTimKiem = dao.searchKhuyenMai(keyword);
            view.setData(listKetQuaTimKiem); // Chỉ đổi dữ liệu hiển thị trên bảng table
            
            // Giữ số tổng luôn khớp với toàn bộ database mới nhất
            view.capNhatThongKeCoDinh(dao.getAll());
        }
    }

    /**
     * Tự động phát sinh mã Khuyến Mãi tiếp theo dạng KMxxx (KM001, KM002,...)
     * Đã được tối ưu để phối hợp với câu lệnh SQL ép kiểu số ở DAO
     */
    public String phatSinhMaTuDong() {
        // Gọi DAO lấy về mã KM lớn nhất hiện tại (Ví dụ: "KM014" hoặc "KM105")
        String maxMa = dao.getMaxMaKhuyenMai();
        
        // Nếu database trống trơn, chưa có chương trình nào thì trả về mã đầu tiên
        if (maxMa == null || maxMa.trim().isEmpty()) {
            return "KM001";
        }
        
        try {
            // Cắt chuỗi lấy phần số, bỏ chữ "KM" ở đầu (Ví dụ: "KM014" -> lấy từ index 2 được "014")
            String phanSoStr = maxMa.substring(2).trim();
            
            // Chuyển chuỗi số thành kiểu int để cộng dồn (Ví dụ: 14 + 1 = 15)
            int phanSo = Integer.parseInt(phanSoStr);
            phanSo++; 
            
            // Định dạng trả về:
            // Nếu bạn muốn giữ chuẩn 3 chữ số cố định (KM001 -> KM099 -> KM100):
            return String.format("KM%03d", phanSo);
            
            // LƯU Ý: Nếu sau này muốn đổi thành 2 chữ số gọn nhẹ (KM01 -> KM99 -> KM100), hãy dùng block này:
            /*
            if (phanSo < 100) {
                return String.format("KM%02d", phanSo);
            } else {
                return "KM" + phanSo;
            }
            */
            
        } catch (Exception e) {
            e.printStackTrace();
            return "KM" + System.currentTimeMillis(); 
        }
    }

    // =========================================================================
    // THÀNH PHẦN BỔ SUNG: XỬ LÝ NHẬP XUẤT EXCEL KHUYẾN MÃI
    // =========================================================================
    
    /**
     * Xuất toàn bộ danh sách khuyến mãi hiện tại ra file Excel (.xlsx)
     */
    public boolean exportToExcel(java.io.File file) {
        List<KhuyenMai> list = dao.getAll();
        try (org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Danh Sách Khuyến Mãi");
            
            // 1. Tạo hàng tiêu đề (Header Row)
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã KM", "Tên Chương Trình", "Giá Trị", "Loại", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Trạng Thái"};
            
            // Thiết lập font đậm cho header
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            
            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 2. Ghi dữ liệu từ database vào các dòng tiếp theo
            int rowNum = 1;
            // Định dạng chuỗi ngày để ghi ra ô Excel hiển thị tường minh
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            for (KhuyenMai km : list) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(km.getMaKM());
                row.createCell(1).setCellValue(km.getTenKM());
                row.createCell(2).setCellValue(km.getGiaTri());
                row.createCell(3).setCellValue(km.getLoaiKM()); // 'PERCENT' hoặc 'FIXED'
                
                row.createCell(4).setCellValue(km.getNgayBatDau() != null ? km.getNgayBatDau().format(formatter) : "");
                row.createCell(5).setCellValue(km.getNgayKetThuc() != null ? km.getNgayKetThuc().format(formatter) : "");
                
                // Trạng thái hiển thị văn bản trực quan thay vì giá trị bit/boolean thô
                row.createCell(6).setCellValue(km.isTrangThai() ? "Đang hoạt động" : "Tạm ngưng");
            }
            
            // Tự động giãn cột theo nội dung chữ
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 3. Ghi luồng dữ liệu xuống file
            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(file)) {
                workbook.write(fileOut);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đọc file Excel dữ liệu Khuyến Mãi và nạp dữ liệu mới vào cơ sở dữ liệu
     */
    public int importFromExcel(java.io.File file) {
        int countSuccess = 0;
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        try (java.io.FileInputStream fileIn = new java.io.FileInputStream(file);
             org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(fileIn)) {
            
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            java.util.Iterator<org.apache.poi.ss.usermodel.Row> rows = sheet.iterator();
            
            // Bỏ qua dòng tiêu đề (Dòng index = 0)
            if (rows.hasNext()) rows.next();
            
            while (rows.hasNext()) {
                org.apache.poi.ss.usermodel.Row row = rows.next();
                
                // Kiểm tra dòng trống hoặc ô thông tin bắt buộc trống thì bỏ qua
                if (row.getCell(1) == null || row.getCell(1).getStringCellValue().trim().isEmpty()) {
                    continue;
                }
                
                // Đọc dữ liệu từ các cột tương ứng
                String tenKM = row.getCell(1).getStringCellValue().trim();
                double giaTri = row.getCell(2) != null ? row.getCell(2).getNumericCellValue() : 0.0;
                String loaiKM = row.getCell(3) != null ? row.getCell(3).getStringCellValue().trim() : "PERCENT";
                
                String ngayBDStr = row.getCell(4) != null ? row.getCell(4).getStringCellValue().trim() : "";
                String ngayKTStr = row.getCell(5) != null ? row.getCell(5).getStringCellValue().trim() : "";
                String trangThaiStr = row.getCell(6) != null ? row.getCell(6).getStringCellValue().trim() : "Đang hoạt động";
                
                // Khởi tạo đối tượng Entity mới
                KhuyenMai km = new KhuyenMai();
                
                // 🔥 SỬ DỤNG MÃ TỰ SINH THÔNG MINH ĐỂ KHÔNG BỊ TRÙNG LẶP KHI NẠP BẢNG
                String maMoi = phatSinhMaTuDong();
                km.setMaKM(maMoi);
                km.setTenKM(tenKM);
                km.setGiaTri(giaTri);
                km.setLoaiKM(loaiKM.toUpperCase()); // Đưa về định dạng chuẩn 'PERCENT' / 'FIXED'
                
                // Ép kiểu String từ ô văn bản Excel về lại dữ liệu LocalDateTime
                try {
                    if (!ngayBDStr.isEmpty()) km.setNgayBatDau(java.time.LocalDateTime.parse(ngayBDStr, formatter));
                    if (!ngayKTStr.isEmpty()) km.setNgayKetThuc(java.time.LocalDateTime.parse(ngayKTStr, formatter));
                } catch (Exception dateEx) {
                    // Nếu lỗi định dạng thời gian từ file, gán mặc định thời gian hiện tại để không sập luồng
                    km.setNgayBatDau(java.time.LocalDateTime.now());
                    km.setNgayKetThuc(java.time.LocalDateTime.now().plusMonths(1));
                }
                
                km.setTrangThai(trangThaiStr.contains("Đang hoạt động") || trangThaiStr.equalsIgnoreCase("true"));
                
                // Thực hiện đẩy xuống SQL Database
                if (dao.insert(km)) {
                    countSuccess++;
                }
            }
            
            // Làm mới lại bảng hiển thị và làm mới lại các Card số liệu thống kê ở góc trên màn hình
            loadDataToTable();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countSuccess;
    }
}