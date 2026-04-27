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
	
	public void loadData() {
		List<KhachHang> list = dao.getAll();
		view.setData(list);
	}
	
	
	
	

}
