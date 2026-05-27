package Controller;

import java.util.List;
import DAO.NhanVienDAO;
import Entity.NhanVien;
import GUI.NhanVienPanel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class NhanVienController {
    
    private NhanVienPanel view;
    private NhanVienDAO dao;
    
    public NhanVienController(NhanVienPanel view) {
        this.view = view;
        this.dao = new NhanVienDAO();
        this.view.setController(this);
        loadDataToTable();
    }
    
    public void loadDataToTable() {
        List<NhanVien> list = dao.getAll();
        view.setData(list);
        view.capNhatThongKeCoDinh(list); 
    }
    
    public boolean themNhanVien(NhanVien nv) {
        return dao.insert(nv);
    }
    
    public boolean updateNhanVien(NhanVien nv) {
        return dao.update(nv);
    }
    public String phatSinhMaTuDong() {
    String maxMa = dao.getMaxMaNhanVien();
    
    // 1. Kiểm tra rỗng
    if (maxMa == null || maxMa.trim().isEmpty()) {
        return "NV001";
    }
    
    try {
        // 2. Dùng Regex để loại bỏ toàn bộ chữ cái, CHỈ giữ lại số (Cực kỳ an toàn)
        // Ví dụ: "NV015" -> "015", "NVABC" -> "", "123" -> "123"
        String numericPart = maxMa.replaceAll("[^0-9]", ""); 
        
        // Nếu bóc tách xong mà không có số nào thì reset về NV001
        if (numericPart.isEmpty()) {
            return "NV001";
        }
        
        // 3. Ép kiểu và cộng thêm 1
        int phanSo = Integer.parseInt(numericPart);
        phanSo++;
        
        // 4. Định dạng chuẩn 3 chữ số (NV001, NV002,... NV999)
        return String.format("NV%03d", phanSo); 
        
    } catch (Exception e) {
        e.printStackTrace();
        // Backup cuối cùng nếu lỗi hệ thống cực nặng
        return "NV" + (System.currentTimeMillis() % 1000);
    }
}
    public void timKiemNhanVien(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable();
        } else {
            List<NhanVien> listKetQuaTimKiem = dao.searchNhanVien(keyword);
            view.setData(listKetQuaTimKiem); 
            view.capNhatThongKeCoDinh(dao.getAll());
        }
    }   

    /**
     * IMPORT FILE EXCEL (Đồng bộ cấu trúc DB Nhân viên: Mã NV, Tên NV, Chức vụ, SĐT, Trạng thái)
     */
    public String importExcel(String filePath) {
        int inserted = 0;
        int skipped = 0;
        StringBuilder errors = new StringBuilder();

        List<NhanVien> currentList = dao.getAll();
        java.util.Set<String> existingPhones = new java.util.HashSet<>();
        for (NhanVien nv : currentList) {
            if (nv.getSoDienThoai() != null) {
                existingPhones.add(nv.getSoDienThoai().trim());
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook wb = filePath.endsWith(".xlsx") ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheetAt(0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Bỏ qua dòng tiêu đề

                String soDienThoai = getCellString(row.getCell(3)); // Cột 3: Số điện thoại
                if (soDienThoai == null || soDienThoai.trim().isEmpty()) continue;

                if (existingPhones.contains(soDienThoai.trim())) {
                    skipped++;
                    continue;
                }
                
                String tenNV     = getCellString(row.getCell(1)); // Cột 1: Tên NV
                String chucVu    = getCellString(row.getCell(2)); // Cột 2: Chức vụ
                String statusStr = getCellString(row.getCell(4)); // Cột 4: Trạng thái text

                // Đổi trạng thái từ chữ sang kiểu boolean
                boolean trangThai = true; 
                if (statusStr != null && (statusStr.contains("Nghỉ") || statusStr.contains("Thôi"))) {
                    trangThai = false;
                }

                NhanVien nv = new NhanVien();
                nv.setMaNV(phatSinhMaTuDong()); 
                nv.setTenNV(tenNV != null ? tenNV : "Chưa đặt tên");
                nv.setChucVu(chucVu != null ? chucVu : "Nhân viên");
                nv.setSoDienThoai(soDienThoai);
                nv.setTrangThai(trangThai);

                if (dao.insert(nv)) {
                    inserted++;
                    existingPhones.add(soDienThoai.trim()); 
                } else {
                    errors.append(tenNV).append("; ");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi đọc file: " + e.getMessage();
        }

        loadDataToTable();

        return String.format("Hoàn tất! Đã thêm %d nhân viên mới, bỏ qua %d người (trùng SĐT).%s",
                inserted, skipped, errors.length() > 0 ? "\nLỗi không lưu được: " + errors : "");
    }

    /**
     * EXPORT FILE EXCEL NHÂN VIÊN
     */
    public boolean exportToExcel(String filePath, List<NhanVien> data) {
        if (data == null) data = dao.getAll();
        
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {
            
            Sheet sheet = workbook.createSheet("DanhSachNhanVien");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Mã NV", "Tên Nhân Viên", "Chức Vụ", "Số Điện Thoại", "Trạng Thái"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            int rowIdx = 1;
            for (NhanVien nv : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(nv.getMaNV());
                row.createCell(1).setCellValue(nv.getTenNV());
                row.createCell(2).setCellValue(nv.getChucVu());
                row.createCell(3).setCellValue(nv.getSoDienThoai());
                
                // Hiển thị trạng thái text thân thiện trên file Excel
                row.createCell(4).setCellValue(nv.isTrangThai() ? "Đang làm việc" : "Đã nghỉ việc");
            }
            
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getCellString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            default:      return null;
        }
    }
}