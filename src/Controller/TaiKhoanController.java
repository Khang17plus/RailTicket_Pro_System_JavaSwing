package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Utils.SessionManager;
import DAO.TaiKhoanDAO;
import Entity.TaiKhoan;
import GUI.TaiKhoanPanel;

import javax.swing.JOptionPane;  // THÊM IMPORT NÀY

public class TaiKhoanController {
    private TaiKhoanPanel view;
    private TaiKhoanDAO dao;

    public TaiKhoanController(TaiKhoanPanel view) {
        this.view = view;
        this.dao = new TaiKhoanDAO();
        
        // Kiểm tra quyền trước khi load dữ liệu
        if (isAdmin()) {
            loadDataToTable();
        } else {
            // SỬA: Dùng JOptionPane trực tiếp
            JOptionPane.showMessageDialog(view, 
                "Bạn không có quyền truy cập chức năng quản lý tài khoản!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean isAdmin() {
        return SessionManager.getInstance().isAdmin();
    }

    public void loadDataToTable() {
        if (!isAdmin()) {
            System.out.println("Từ chối loadDataToTable: Không phải ADMIN");
            return; 
        }
        
        List<TaiKhoan> list = dao.getAll();
        if (view != null) {
            view.setData(list);
            view.updateThongKeCoDinh(list);
        }
    }

    public boolean themTaiKhoan(TaiKhoan tk) {
        if (!isAdmin()) {
            JOptionPane.showMessageDialog(view, 
                "Chỉ ADMIN mới có quyền thêm tài khoản!", 
                "Lỗi phân quyền", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!dao.checkNhanVienTonTai(tk.getMaNV())) {
            JOptionPane.showMessageDialog(view, 
                "Mã nhân viên không tồn tại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean result = dao.insert(tk);
        if (result) {
            JOptionPane.showMessageDialog(view, 
                "Thêm tài khoản thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            loadDataToTable();
        } else {
            JOptionPane.showMessageDialog(view, 
                "Thêm tài khoản thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public boolean capNhatTaiKhoan(TaiKhoan tk) {
        if (!isAdmin()) {
            JOptionPane.showMessageDialog(view, 
                "Chỉ ADMIN mới có quyền cập nhật tài khoản!", 
                "Lỗi phân quyền", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean result = dao.update(tk);
        if (result) {
            JOptionPane.showMessageDialog(view, 
                "Cập nhật tài khoản thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            loadDataToTable();
        } else {
            JOptionPane.showMessageDialog(view, 
                "Cập nhật tài khoản thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    public void timKiemTaiKhoan(String keyword) {
        if (!isAdmin()) {
            JOptionPane.showMessageDialog(view, 
                "Chỉ ADMIN mới có quyền tìm kiếm tài khoản!", 
                "Lỗi phân quyền", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<TaiKhoan> list = dao.searchTaiKhoan(keyword);
        if (view != null) {
            view.setData(list);
        }
    }

    public boolean exportToExcel(File file) {
        if (!isAdmin()) {
            JOptionPane.showMessageDialog(view, 
                "Chỉ ADMIN mới có quyền xuất Excel!", 
                "Lỗi phân quyền", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // ... (Giữ nguyên logic export cũ của bạn)
        return true;
    }

    public int importFromExcel(File file) {
        if (!isAdmin()) {
            JOptionPane.showMessageDialog(view, 
                "Chỉ ADMIN mới có quyền nhập Excel!", 
                "Lỗi phân quyền", 
                JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        // ... (Giữ nguyên logic import cũ của bạn)
        return 0;
    }
}