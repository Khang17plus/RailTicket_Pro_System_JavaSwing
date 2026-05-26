package Controller;

import DAO.GaTauDAO;
import Entity.GaTau;
import GUI.GaTauPanel;

import java.util.List;

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
}