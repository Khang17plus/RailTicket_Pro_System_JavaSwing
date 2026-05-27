package Controller;

import java.util.List;
import DAO.ThueDAO; 
import Entity.Thue; 
import GUI.ThuePanel;

public class ThueController {
    
    private ThuePanel view;
    private ThueDAO dao;
    
    public ThueController(ThuePanel view) {
        this.view = view;
        this.dao = new ThueDAO();
        
        // Tự động đổ dữ liệu khi khởi tạo
        loadDataToTable();
    }
    
    /**
     * Lấy toàn bộ danh sách thuế từ DB và hiển thị lên bảng
     */
    public void loadDataToTable() {
        List<Thue> list = dao.getAll();
        view.setData(list);
        view.capNhatThongKeCoDinh(list); // Gọi tính toán dựa trên full danh sách gốc
    }
    
    /**
     * Thêm mới một cấu hình thuế
     */
    public boolean themThue(Thue t) {
        return dao.insert(t); 
    }
    
    /**
     * Cập nhật thông tin thuế đã tồn tại
     */
    public boolean updateThue(Thue t) {
        return dao.update(t);
    }
    
    /**
     * Xóa cấu hình thuế theo mã (Xóa mềm)
     */
    public boolean xoaThue(String maThue) {
        return dao.delete(maThue);
    }
    
    /**
     * Tìm kiếm thuế theo từ khóa (Mã hoặc Tên thuế)
     */
    public void timKiemThue(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable();
        } else {
            List<Thue> listKetQuaTimKiem = dao.searchThue(keyword);
            view.setData(listKetQuaTimKiem); // Chỉ đổi dữ liệu hiển thị trên bảng table
            
            // Giữ số tổng luôn khớp với toàn bộ database mới nhất
            view.capNhatThongKeCoDinh(dao.getAll());
        }
    }

    /**
     * Tự động phát sinh mã Thuế tiếp theo dạng THUExxx (THUE001, THUE002,...)
     * Đã được tối ưu để phối hợp với câu lệnh SQL ép kiểu số ở DAO
     */
    public String phatSinhMaTuDong() {
        // Gọi DAO lấy về mã Thuế lớn nhất hiện tại (Ví dụ: "THUE003" hoặc "THUE012")
        String maxMa = dao.getMaxMaThue();
        
        // Nếu database trống trơn hoặc gặp lỗi, trả về mã đầu tiên
        if (maxMa == null || maxMa.trim().isEmpty() || maxMa.trim().equals("")) {
            return "THUE001";
        }
        
        try {
            // Cắt chuỗi lấy phần số, bỏ chữ "THUE" ở đầu (Ví dụ: "THUE003" -> lấy từ index 4 được "003")
            String phanSoStr = maxMa.substring(4).trim();
            
            // Chuyển chuỗi số thành kiểu int để cộng dồn (Ví dụ: 3 + 1 = 4)
            int phanSo = Integer.parseInt(phanSoStr);
            phanSo++; 
            
            // Định dạng trả về chuỗi có 3 chữ số cố định (THUE001 -> THUE099 -> THUE100):
            return String.format("THUE%03d", phanSo);
            
        } catch (Exception e) {
            e.printStackTrace();
            return "THUE001"; // Trả về mã an toàn đầu tiên nếu lỗi logic cắt chuỗi
        }
    }

    // =========================================================================
    // THÀNH PHẦN BỔ SUNG: XỬ LÝ NHẬP XUẤT EXCEL THUẾ (GIỐNG KHUYẾN MÃI)
    // =========================================================================
    
    /**
     * Xuất toàn bộ danh sách cấu hình thuế hiện tại ra file Excel (.xlsx)
     */
    public boolean exportToExcel(java.io.File file) {
        List<Thue> list = dao.getAll();
        try (org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Danh Sách Cấu Hình Thuế");
            
            // 1. Tạo hàng tiêu đề (Header Row)
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] columns = {"Mã Thuế", "Tên Loại Thuế", "Mức Thuế (%)", "Ngày Bắt Đầu", "Trạng Thái"};
            
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
            
            for (Thue t : list) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(t.getMaThue());
                row.createCell(1).setCellValue(t.getTenThue());
                row.createCell(2).setCellValue(t.getPhanTram());
                
                row.createCell(3).setCellValue(t.getNgayBatDau() != null ? t.getNgayBatDau().format(formatter) : "");
                
                // Trạng thái hiển thị văn bản trực quan giống bên khuyến mãi
                row.createCell(4).setCellValue(t.isTrangThai() ? "Đang hoạt động" : "Tạm ngưng");
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
     * Đọc file Excel dữ liệu Thuế và nạp dữ liệu mới vào cơ sở dữ liệu
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
                String tenThue = row.getCell(1).getStringCellValue().trim();
                double phanTram = row.getCell(2) != null ? row.getCell(2).getNumericCellValue() : 0.0;
                String ngayBDStr = row.getCell(3) != null ? row.getCell(3).getStringCellValue().trim() : "";
                String trangThaiStr = row.getCell(4) != null ? row.getCell(4).getStringCellValue().trim() : "Đang hoạt động";
                
                // Khởi tạo đối tượng Entity mới
                Thue t = new Thue();
                
                // 🔥 SỬ DỤNG MÃ TỰ SINH THÔNG MINH ĐỂ KHÔNG BỊ TRÙNG LẶP KHI NẠP BẢNG
                String maMoi = phatSinhMaTuDong();
                t.setMaThue(maMoi);
                t.setTenThue(tenThue);
                t.setPhanTram(phanTram);
                
                // Ép kiểu String từ ô văn bản Excel về lại dữ liệu LocalDateTime
                try {
                    if (!ngayBDStr.isEmpty()) {
                        t.setNgayBatDau(java.time.LocalDateTime.parse(ngayBDStr, formatter));
                    } else {
                        t.setNgayBatDau(java.time.LocalDateTime.now());
                    }
                } catch (Exception dateEx) {
                    // Nếu lỗi định dạng thời gian từ file, gán mặc định thời gian hiện tại để không sập luồng
                    t.setNgayBatDau(java.time.LocalDateTime.now());
                }
                
                t.setTrangThai(trangThaiStr.contains("Đang hoạt động") || trangThaiStr.equalsIgnoreCase("true") || trangThaiStr.equals("1"));
                
                // Thực hiện đẩy xuống SQL Database
                if (dao.insert(t)) {
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