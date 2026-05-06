package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JFrame {

    // Khai báo biến ở đây để Controller có thể lấy dữ liệu
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginPanel() {
        setTitle("Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Panel chính
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        txtUser = new JTextField(15);
        panel.add(txtUser, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        txtPass = new JPasswordField(15);
        panel.add(txtPass, gbc);

        // Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;

        btnLogin = new JButton("Login");
        panel.add(btnLogin, gbc);

        add(panel);
    }

    // =========================================
    // THÊM 3 HÀM GETTER NÀY CHO CONTROLLER
    // =========================================
    public JTextField getTxtUser() { return txtUser; }
    public JPasswordField getTxtPass() { return txtPass; }
    public JButton getBtnLogin() { return btnLogin; }
}