package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import DAO.TaiKhoanDAO;
import Entity.TaiKhoan;
import GUI.TaiKhoanPanel;

public class TaiKhoanController {
    
    private TaiKhoanPanel view;
    private TaiKhoanDAO dao;
    
    public TaiKhoanController(TaiKhoanPanel view) {
        this.view = view;
        this.dao = new TaiKhoanDAO();
        
        // Tự động đổ dữ liệu lên bảng khi khởi tạo phân hệ tài khoản
        loadDataToTable();
    }
    
    /**
     * Lấy toàn bộ danh sách tài khoản từ DB và hiển thị lên giao diện
     */
    public void loadDataToTable() {
        List<TaiKhoan> list = dao.getAll();
        view.setData(list); // Gọi hàm setData bên TaiKhoanPanel
        
        // Cập nhật các ô thẻ (Card) thống kê số lượng phân quyền theo thời gian thực
        if (view != null) {
            view.updateThongKeCoDinh(list);
        }
    }
    
    /**
     * Cấp mới một tài khoản nhân viên
     */
    public boolean themTaiKhoan(TaiKhoan tk) {
        // Chặn điều kiện: Mã nhân viên phải tồn tại ở bảng Nhân Viên thì mới được tạo account
        if (!dao.checkNhanVienTonTai(tk.getMaNV())) {
            System.out.println("Lỗi: Mã nhân viên không tồn tại trong hệ thống đường sắt!");
            return false;
        }
        
        boolean result = dao.insert(tk);
        if (result) {
            System.out.println("Cấp tài khoản cho nhân viên " + tk.getMaNV() + " thành công");
        }
        return result;
    }
    
    /**
     * Cập nhật thông tin tài khoản (Mật khẩu hoặc Vai trò)
     */
    public boolean capNhatTaiKhoan(TaiKhoan tk) {
        return dao.update(tk);
    }
    
    /**
     * Tìm kiếm tài khoản theo từ khóa (Mã nhân viên hoặc Vai trò)
     */
    public void timKiemTaiKhoan(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable(); // Nếu ô tìm kiếm trống thì hiển thị lại toàn bộ
        } else {
            List<TaiKhoan> list = dao.searchTaiKhoan(keyword);
            view.setData(list); // Cập nhật lại bảng với dữ liệu tìm được
        }
    }

    // =========================================================================
    // 📊 XỬ LÝ EXCEL: XUẤT FILE EXCEL (EXPORT)
    // =========================================================================
    public boolean exportToExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook(); 
             FileOutputStream fileOut = new FileOutputStream(file)) {
            
            Sheet sheet = workbook.createSheet("Danh Sách Tài Khoản");
            
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
            
            // 2. Định nghĩa danh sách các Cột Tiêu Đề theo chuẩn phân hệ bảo mật
            String[] columns = {"Mã Nhân Viên", "Mật Khẩu Hệ Thống", "Vai Trò"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }
            
            // 3. Đổ dữ liệu từ Database vào các dòng tiếp theo
            List<TaiKhoan> list = dao.getAll();
            int rowNum = 1;
            
            for (TaiKhoan tk : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(tk.getMaNV());
                // Xuất mật khẩu gốc ra file Excel phục vụ việc backup của ADMIN
                row.createCell(1).setCellValue(tk.getMatKhau());
                row.createCell(2).setCellValue(tk.getVaiTro());
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
            
            // Đọc tuần tự từng dòng dữ liệu account nhân viên
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                
                // Kiểm tra nếu ô Mã Nhân Viên trống thì bỏ qua dòng đó
                Cell cellMaNV = row.getCell(0);
                if (cellMaNV == null || cellMaNV.getCellType() == CellType.BLANK) {
                    continue; 
                }
                
                try {
                    // 1. Đọc dữ liệu thô và loại bỏ khoảng trắng thừa
                    String maNVStr = getCellValueAsString(cellMaNV).toUpperCase();
                    String matKhauStr = getCellValueAsString(row.getCell(1));
                    String vaiTroStr = getCellValueAsString(row.getCell(2)).toUpperCase();
                    
                    // 2. Chặn điều kiện logic nghiêm ngặt từ Database:
                    // - Điều kiện 1: Nhân viên phải tồn tại ở bảng cha NhanVien (Khóa ngoại)
                    // - Điều kiện 2: Nhân viên này CHƯA có tài khoản (Khóa chính duy nhất)
                    // - Điều kiện 3: Vai trò phải ghi đúng từ khóa 'ADMIN' hoặc 'STAFF'
                    if (!dao.checkNhanVienTonTai(maNVStr)) {
                        System.out.println("Bỏ qua dòng: Nhân viên " + maNVStr + " không tồn tại ở hệ thống.");
                        continue;
                    }
                    if (dao.checkTrungTaiKhoan(maNVStr)) {
                        System.out.println("Bỏ qua dòng: Nhân viên " + maNVStr + " đã được cấp tài khoản rồi.");
                        continue;
                    }
                    if (!"ADMIN".equals(vaiTroStr) && !"STAFF".equals(vaiTroStr)) {
                        System.out.println("Bỏ qua dòng: Vai trò '" + vaiTroStr + "' không hợp lệ (Phải là ADMIN/STAFF).");
                        continue;
                    }
                    
                    // 3. Khởi tạo thực thể và đẩy dữ liệu xuống DB
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaNV(maNVStr);
                    tk.setMatKhau(matKhauStr.isEmpty() ? "123456" : matKhauStr); // Mật khẩu mặc định nếu ô Excel trống
                    tk.setVaiTro(vaiTroStr);
                    
                    if (dao.insert(tk)) {
                        successfulRows++;
                    }
                } catch (Exception rowEx) {
                    System.err.println("Lỗi phân tích tài khoản tại dòng " + row.getRowNum() + ": " + rowEx.getMessage());
                }
            }
            
            // Làm mới giao diện bảng điều khiển sau khi nạp xong toàn bộ file Excel
            if (successfulRows > 0) {
                loadDataToTable();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return successfulRows;
    }
    
    /**
     * Hàm phụ trợ định dạng dữ liệu chuỗi chống lỗi định dạng ô Excel
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                DataFormatter formatter = new DataFormatter();
                return formatter.formatCellValue(cell).replaceAll("\\s+", "");
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}