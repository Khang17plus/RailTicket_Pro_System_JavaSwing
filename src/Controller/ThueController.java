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
        loadDataToTable();
    }

    public void loadDataToTable() {
        List<Thue> list = dao.getAll();
        view.setData(list);
        view.capNhatThongKeCoDinh(list);
    }

    public boolean themThue(Thue t) {
        return dao.insert(t);
    }

    public boolean updateThue(Thue t) {
        return dao.update(t);
    }

    public boolean xoaThue(String maThue) {
        return dao.delete(maThue);
    }

    public void timKiemThue(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable();
        } else {
            List<Thue> listKetQua = dao.searchThue(keyword);
            view.setData(listKetQua);
            view.capNhatThongKeCoDinh(dao.getAll()); // Thống kê giữ nguyên theo tổng DB
        }
    }
}