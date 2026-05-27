package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JFrame {

    // Giữ nguyên 100% tên biến cũ để Controller không bị lỗi
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginPanel() {
        // Cấu hình Frame chính chống răng cưa và bo tròn nhẹ cửa sổ
        setTitle("RailTicket Pro - Đăng Nhập");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // Panel nền chính (Gradient mượt từ xanh đen tối sang xám đen)
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42), 0, getHeight(), new Color(30, 41, 59));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 45, 40, 45));

        // --- 1. KHU VỰC TIÊU ĐỀ & LOGO THƯƠNG HIỆU ---
     // --- 1. KHU VỰC TIÊU ĐỀ & LOGO THƯƠNG HIỆU ---
        ImageIcon originalIcon = new ImageIcon("img/logo_taulua.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(160, 80, Image.SCALE_SMOOTH);

        JLabel lblLogo = new JLabel(new ImageIcon(scaledImage));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("RailTicket Pro");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Hệ thống quản lý bán vé đường sắt");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(148, 163, 184)); // Màu xám nhẹ nhã nhặn
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- 2. KHU VỰC FORM NHẬP LIỆU ---
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridLayout(4, 1, 0, 8)); // 4 hàng dọc khoảng cách thoáng
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        // Label + Ô nhập Username
        JLabel lblUser = new JLabel("Tài khoản người dùng");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(203, 213, 225));
        
        txtUser = new JTextField();
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUser.putClientProperty("JTextField.placeholderText", "Nhập mã nhân viên hoặc username...");
        txtUser.putClientProperty("JTextField.padding", new Insets(8, 12, 8, 12));
        txtUser.setBackground(new Color(51, 65, 85));
        txtUser.setForeground(Color.WHITE);
        txtUser.setCaretColor(Color.WHITE);

        // Label + Ô nhập Password
        JLabel lblPass = new JLabel("Mật khẩu bảo mật");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(203, 213, 225));
        
        txtPass = new JPasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.putClientProperty("JTextField.placeholderText", "••••••••");
        txtPass.putClientProperty("JTextField.padding", new Insets(8, 12, 8, 12));
        txtPass.putClientProperty("JPasswordField.showRevealButton", true); // Hiện con mắt ẩn/hiển thị pass (FlatLaf)
        txtPass.setBackground(new Color(51, 65, 85));
        txtPass.setForeground(Color.WHITE);
        txtPass.setCaretColor(Color.WHITE);

        formPanel.add(lblUser);
        formPanel.add(txtUser);
        formPanel.add(lblPass);
        formPanel.add(txtPass);

        // --- 3. NÚT ĐĂNG NHẬP (HOVER EFFECT) ---
        btnLogin = new JButton("ĐĂNG NHẬP HỆ THỐNG");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(37, 99, 235)); // Màu xanh Royal công nghệ
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Hiệu ứng đổi màu mượt khi Hover chuột vào nút bấm
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(29, 78, 216));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(37, 99, 235));
            }
        });

        // --- 4. SẮP XẾP VÀ NẠP CÁC THÀNH PHẦN VÀO KHUNG CHÍNH ---
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(lblLogo);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(lblSubtitle);
        mainPanel.add(Box.createVerticalStrut(40)); // Khoảng cách giữa tiêu đề và form
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(btnLogin);

        add(mainPanel);
    }

    // =========================================================================
    // GIỮ NGUYÊN TOÀN BỘ GETTER ĐỂ ĐẢM BẢO KẾT NỐI KHÔNG LỖI VỚI LOGINCONTROLLER
    // =========================================================================
    public JTextField getTxtUser() { return txtUser; }
    public JPasswordField getTxtPass() { return txtPass; }
    public JButton getBtnLogin() { return btnLogin; }
}