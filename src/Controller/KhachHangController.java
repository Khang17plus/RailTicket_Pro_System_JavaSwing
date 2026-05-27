package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import DAO.KhachHangDAO;
import Entity.KhachHang;
import GUI.KhachHangPanel;

public class KhachHangController {
    
    private KhachHangPanel view;
    private KhachHangDAO dao;
    
    public KhachHangController(KhachHangPanel view) {
        this.view = view;
        this.dao = new KhachHangDAO();
        
        // Tự động đổ dữ liệu lên bảng khi khởi tạo
        loadDataToTable();
    }
    
    /**
     * Lấy toàn bộ danh sách khách hàng từ DB và hiển thị lên giao diện
     */
    public void loadDataToTable() {
        List<KhachHang> list = dao.getAll();
        view.setData(list); // Gọi hàm setData bên KhachHangPanel
        
        // Cập nhật các ô thẻ (Card) thống kê số lượng trên giao diện nếu có
        if (view != null) {
            view.updateThongKeCoDinh(list);
        }
    }
    
    /**
     * Thêm mới một khách hàng
     */
    public boolean themKhachHang(KhachHang kh) {
        boolean result = dao.insert(kh);
        if (result) {
            System.out.println("Thêm khách hàng thành công");
        }
        return result;
    }
    
    /**
     * Cập nhật thông tin khách hàng đã tồn tại
     */
    public boolean capNhatKhachHang(KhachHang kh) {
        return dao.update(kh);
    }
    
    /**
     * 🔥 TỰ ĐỘNG PHÁT SINH MÃ KHÁCH HÀNG TIẾP THEO
     * Định dạng mã sinh ra: KHxxx (Ví dụ: KH001, KH002, KH012,...)
     */
    public String phatSinhMaTuDong() {
        String maxMa = dao.getMaxMaKhachHang();
        
        // Nếu Database trống chưa có khách hàng nào
        if (maxMa == null || maxMa.trim().isEmpty()) {
            return "KH001";
        }
        
        try {
            // Cắt chuỗi bỏ đi chữ "KH" để lấy phần số đằng sau
            String phanSoStr = maxMa.substring(2).trim();
            int phanSo = Integer.parseInt(phanSoStr);
            
            // Tăng mã số lên 1 đơn vị
            phanSo++;
            
            // Định dạng lại chuỗi dạng KH kèm 3 chữ số, tự bù số 0 ở trước (ví dụ: KH002)
            return String.format("KH%03d", phanSo);
        } catch (Exception e) {
            e.printStackTrace();
            return "KH" + (System.currentTimeMillis() % 1000);
        }
    }
    
    /**
     * Tìm kiếm khách hàng theo từ khóa (CCCD hoặc Số điện thoại)
     */
    public void timKiemKhachHang(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable(); // Nếu ô tìm kiếm trống thì hiển thị lại toàn bộ
        } else {
            List<KhachHang> list = dao.searchKhachHang(keyword);
            view.setData(list); // Cập nhật lại bảng với dữ liệu tìm được
        }
    }

    // =========================================================================
    // 📊 XỬ LÝ EXCEL: XUẤT FILE EXCEL (EXPORT)
    // =========================================================================
    public boolean exportToExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook(); 
             FileOutputStream fileOut = new FileOutputStream(file)) {
            
            Sheet sheet = workbook.createSheet("Danh Sách Khách Hàng");
            
            // 1. Tạo Font và Style cho Tiêu đề (Header Row)
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // 2. Định nghĩa danh sách các Cột Tiêu Đề
            String[] columns = {"Mã KH", "Tên khách hàng", "Số CCCD/Hộ chiếu", "Số điện thoại", "Email", "Ngày đăng ký"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }
            
            // 3. Đổ dữ liệu từ Database vào các dòng tiếp theo
            List<KhachHang> list = dao.getAll();
            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            int rowNum = 1;
            
            for (KhachHang kh : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(kh.getMaKH());
                row.createCell(1).setCellValue(kh.getTenKH());
                row.createCell(2).setCellValue(kh.getCccd());
                row.createCell(3).setCellValue(kh.getSoDienThoai());
                row.createCell(4).setCellValue(kh.getEmail());
                
                String ngayDKStr = (kh.getNgayDangKy() != null) ? kh.getNgayDangKy().format(dtf) : "";
                row.createCell(5).setCellValue(ngayDKStr);
            }
            
            // 4. Tự động co giãn độ rộng cột cho vừa vặn chữ
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(fileOut);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // 📥 XỬ LÝ EXCEL: NHẬP FILE EXCEL (IMPORT)
    // =========================================================================
    public int importFromExcel(File file) {
        int successfulRows = 0;
        
        try (FileInputStream fileIn = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fileIn)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            java.util.Iterator<Row> rowIterator = sheet.iterator();
            
            // Bỏ qua dòng tiêu đề thứ nhất
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            
            // Đọc tuần tự từng dòng dữ liệu còn lại
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                
                // Kiểm tra nếu ô Tên Khách Hàng trống thì bỏ qua dòng đó
                Cell cellTen = row.getCell(1);
                if (cellTen == null || cellTen.getCellType() == CellType.BLANK) {
                    continue; 
                }
                
                try {
                    // 1. Tự động sinh mã mới cuốn chiếu liên tục (không lo trùng khóa chính)
                    String maKHStr = phatSinhMaTuDong();
                    
                    // 2. Đọc và làm sạch chuỗi văn bản
                    String tenKHStr = cellTen.getStringCellValue().trim();
                    
                    // Đọc CCCD (bọc lót trường hợp ô định dạng số hoặc chuỗi)
                    String cccdStr = getCellValueAsString(row.getCell(2));
                    String sdtStr = getCellValueAsString(row.getCell(3));
                    String emailStr = getCellValueAsString(row.getCell(4));
                    
                    // 3. Xử lý cột ngày đăng ký
                    java.time.LocalDateTime ngayDK = java.time.LocalDateTime.now(); // Mặc định là hôm nay
                    Cell cellNgay = row.getCell(5);
                    if (cellNgay != null && cellNgay.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellNgay)) {
                        ngayDK = cellNgay.getLocalDateTimeCellValue();
                    }
                    
                    // Chặn trùng lặp thực tế: Nếu số CCCD hoặc Số điện thoại đã tồn tại ở DB -> Bỏ qua ga này!
                    if (dao.checkTrungCCCD(cccdStr) || dao.checkTrungSDT(sdtStr)) {
                        System.out.println("Bỏ qua khách hàng trùng CCCD/SĐT: " + tenKHStr);
                        continue;
                    }
                    
                    // 4. Khởi tạo thực thể Khách Hàng và lưu xuống DB
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(maKHStr);
                    kh.setTenKH(tenKHStr);
                    kh.setCccd(cccdStr);
                    kh.setSoDienThoai(sdtStr);
                    kh.setEmail(emailStr);
                    kh.setNgayDangKy(ngayDK);
                    
                    if (dao.insert(kh)) {
                        successfulRows++;
                        // Tránh lag giao diện, dữ liệu thật được nạp gối đầu liên tục lên DB
                    }
                } catch (Exception rowEx) {
                    System.err.println("Lỗi phân tích tại dòng " + row.getRowNum() + ": " + rowEx.getMessage());
                }
            }
            
            // Làm mới giao diện sau khi import xong toàn bộ file
            if (successfulRows > 0) {
                loadDataToTable();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return successfulRows;
    }
    
    /**
     * Hàm phụ trợ giải quyết triệt để vấn đề Excel biến đổi Số điện thoại / CCCD thành định dạng E+ (Scientific)
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Nếu là số, ép định dạng số nguyên chuỗi gốc, chống lỗi rớt mất số 0 đầu tiên của SĐT
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell).replaceAll("\\s+", "");
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}