package Controller;

import DAO.GaTauDAO;
import Entity.GaTau;
import GUI.GaTauPanel;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GaTauController {
	
	private GaTauPanel view;
	private GaTauDAO dao;
	
	public GaTauController(GaTauPanel view) {
		this.view = view;
		this.dao = new GaTauDAO();
		
		// Tự động gán controller này cho view để view có thể gọi ngược lại các hàm xử lý
		this.view.setController(this);
		
		// Tải dữ liệu lên bảng ngay khi khởi tạo
		loadDataToTable();
	}

	// 🔹 Tải toàn bộ danh sách ga tàu lên giao diện
	public void loadDataToTable() {
		List<GaTau> list = dao.getAll();
		view.setData(list); 
	}
	
	public String importExcel(String filePath) {
	    int inserted = 0;
	    int skipped = 0;
	    StringBuilder errors = new StringBuilder();

	    try (FileInputStream fis = new FileInputStream(filePath);
	         Workbook wb = filePath.endsWith(".xlsx") ? new XSSFWorkbook(fis) : new HSSFWorkbook(fis)) {

	        Sheet sheet = wb.getSheetAt(0);
	        // Giả sử các cột: 0: Tên ga, 1: Địa chỉ, 2: Số điện thoại, 3: Trạng thái
	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) continue; // bỏ qua tiêu đề

	            String diaChi = getCellString(row.getCell(2));
	            if (diaChi == null || diaChi.trim().isEmpty()) continue;

	            // Kiểm tra địa chỉ đã tồn tại chưa
	            if (dao.findByDiaChi(diaChi) != null) {
	                skipped++;
	                continue;
	            }
	            
	            String tenGa       = getCellString(row.getCell(1)); // Cột B
	            // Cột C
	            String soDienThoai = getCellString(row.getCell(3)); // Cột D
	            String trangThai   = getCellString(row.getCell(4));

//	            String tenGa = getCellString(row.getCell(1));
//	            String soDienThoai = getCellString(row.getCell(2));
//	            String trangThai = getCellString(row.getCell(3));
//	            if (trangThai == null || trangThai.isEmpty()) trangThai = "Đang hoạt động";

	            GaTau ga = new GaTau();
	            ga.setMaGa(generateNextMaGa());   // tự sinh mã GAxxx
	            ga.setTenGa(tenGa != null ? tenGa : "Chưa đặt tên");
	            ga.setDiaChi(diaChi);
	            ga.setSoDienThoai(soDienThoai);
	            ga.setTrangThai(trangThai);

	            if (dao.insert(ga)) inserted++;
	            else errors.append(tenGa).append(" - ").append(diaChi).append("; ");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Lỗi đọc file: " + e.getMessage();
	    }

	    return String.format("Hoàn tất! Đã thêm %d ga mới, bỏ qua %d ga (địa chỉ trùng).%s",
	            inserted, skipped, errors.length() > 0 ? "\nLỗi: " + errors : "");
	}

	// Helper đọc cell
	private String getCellString(Cell cell) {
	    if (cell == null) return null;
	    switch (cell.getCellType()) {
	        case STRING:  return cell.getStringCellValue().trim();
	        case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
	        default:      return null;
	    }
	}
	
	// 🔹 Xử lý thêm ga tàu
	public boolean themGaTau(GaTau ga) {
		boolean result = dao.insert(ga);
		if (result) {
			System.out.println("Thêm ga tàu thành công: " + ga.getMaGa());
		} else {
			System.out.println("Thêm ga tàu thất bại!");
		}
		return result;
	}
	
	// 🔹 Xử lý cập nhật thông tin
	public boolean capNhatGaTau(GaTau ga) {
		boolean result = dao.update(ga);
		if (result) {
			System.out.println("Cập nhật thành công ga: " + ga.getMaGa());
		}
		return result;
	}
	public String generateNextMaGa() {
        // Giả sử gaTauDAO có hàm getMaxMaGa() trả về mã lớn nhất (vd: "GA015")
        String maxMa = dao.getMaxMaGa(); 

        if (maxMa == null || maxMa.isEmpty()) {
            return "GA001";
        }

        // Cắt bỏ chữ "GA" để lấy phần số
        String numberPart = maxMa.substring(2); 
        int currentNumber;
        try {
            currentNumber = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            currentNumber = 0;
        }

        int nextNumber = currentNumber + 1;
        return String.format("GA%03d", nextNumber);
    }
	
	// 🔹 Xử lý xóa ga tàu
	public boolean xoaGaTau(String maGa) {
		boolean result = dao.delete(maGa);
		if (result) {
			System.out.println("Xóa thành công ga: " + maGa);
		}
		return result;
	}
	
	// 🔹 Xử lý tìm kiếm
	public void timKiemGaTau(String keyword) {
		List<GaTau> list = dao.searchGaTau(keyword);
		view.setData(list); 
	}
	
	// Trong GaTauController
	public boolean exportToExcel(String filePath, List<GaTau> data) {
	    if (data == null) data = dao.getAll(); // nếu không truyền data thì lấy tất cả từ DB
	    try (Workbook workbook = new XSSFWorkbook(); // tạo file .xlsx
	         FileOutputStream fos = new FileOutputStream(filePath)) {
	        Sheet sheet = workbook.createSheet("DanhSachGa");
	        // Tạo header
	        Row headerRow = sheet.createRow(0);
	        String[] headers = {"Mã Ga", "Tên Ga", "Địa chỉ", "Số điện thoại", "Trạng thái"};
	        CellStyle headerStyle = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	        }
	        // Đổ dữ liệu
	        int rowIdx = 1;
	        for (GaTau ga : data) {
	            Row row = sheet.createRow(rowIdx++);
	            row.createCell(0).setCellValue(ga.getMaGa());
	            row.createCell(1).setCellValue(ga.getTenGa());
	            row.createCell(2).setCellValue(ga.getDiaChi());
	            row.createCell(3).setCellValue(ga.getSoDienThoai());
	            row.createCell(4).setCellValue(ga.getTrangThai());
	        }
	        // Tự động resize cột (tuỳ chọn)
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
}