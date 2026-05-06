package App;

import Controller.LoginController;
import GUI.LoginPanel;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class AppRunner {
    public static void main(String[] args) {
        
        // 1. Cài đặt giao diện FlatLaf cho toàn bộ hệ thống
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            // Custom góc bo tròn
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Chạy luồng khởi động
        SwingUtilities.invokeLater(() -> {
            // Tạo View đăng nhập
            LoginPanel loginView = new LoginPanel();
            
            // Gắn Controller điều khiển View
            new LoginController(loginView);
            
            // Mở màn hình Login đầu tiên
            loginView.setVisible(true);
        });
    }
}