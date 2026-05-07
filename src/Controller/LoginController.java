package Controller;

import DAO.NhanVienDAO;
import DAO.TaiKhoanDAO;
import Entity.NhanVien;
import Entity.TaiKhoan;
import GUI.LoginPanel;
import Utils.SessionManager;
import App.Main; 

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginPanel view;
    private TaiKhoanDAO dao;

    public LoginController(LoginPanel view) {
        this.view = view;
        this.dao = new TaiKhoanDAO();

        // Lắng nghe sự kiện click nút Login sữa ở đây để debug
        this.view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thucHienDangNhap();
            }
        	
        });

        // Bắt thêm sự kiện ấn phím Enter ở ô Password
        this.view.getTxtPass().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thucHienDangNhap();
            }
        });
    }

    private void thucHienDangNhap() {
        String username = view.getTxtUser().getText().trim();
        String password = new String(view.getTxtPass().getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ Username và Password!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gọi DAO kiểm tra trong CSDL
        TaiKhoan tk = dao.checkLogin(username, password);
        

        if (tk != null) {
        	NhanVienDAO nvDao = new NhanVienDAO();
            NhanVien nv = nvDao.getNhanVienTheoMa(tk.getMaNV());
            SessionManager.getInstance().login(tk, nv);
            // 1. Tắt giao diện Đăng nhập
            view.dispose(); 

            // 2. Mở giao diện Main
            Main mainApp = new Main();
            mainApp.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }
}