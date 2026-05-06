package Controller;
import GUI.KhachHangPanel;


import java.util.ArrayList;
import java.util.List;

import DAO.KhachHangDAO;
import Entity.KhachHang;

public class KhachHangController {
	
	private KhachHangPanel view;
	private KhachHangDAO dao;
	
	public KhachHangController(KhachHangPanel view ) {
		this.view = view;
        this.dao = new KhachHangDAO();
		
        loadData();
	}
	public void loadDataToTable() {
        List<KhachHang> list = dao.getAll();
        view.setData(list); // Gọi hàm setData bên KhachHangPanel
    }
	public void loadData() {
		List<KhachHang> list = dao.getAll();
		view.setData(list);
	}
	
	public boolean themKhachHang(KhachHang kh) {
		boolean result = dao.insert(kh);
		if (result) {
			System.out.println("them thanh cong");
		}
		return result;
		
	}
	
	public boolean capNhatKhachHang(KhachHang kh) {
        return dao.update(kh);
    }
	
	public boolean xoaKhachHang(String maKH) {
        return dao.delete(maKH);
    }
	
	public void timKiemKhachHang(String keyword) {
        List<KhachHang> list = dao.searchKhachHang(keyword);
        view.setData(list); // Cập nhật lại bảng với dữ liệu tìm được
    }

	
}
