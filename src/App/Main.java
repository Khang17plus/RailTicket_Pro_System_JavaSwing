package App;
import GUI.*;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
	private CardLayout cardLayout;
	private JPanel content;
	
	
	
	public Main()
	{
		setTitle("RailTicket Pro");
		setSize(1400,800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		// === thanh ben 
		
		JPanel sidebar = new JPanel();
		sidebar.setPreferredSize(new Dimension(250, 0));
		
		
		sidebar.setLayout(new BorderLayout());
		sidebar.setBackground(new Color(30,41,59));
		
		JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15)); // Tạo padding lề
		
        
        JButton btnDashboard = createNavButton("Trang chủ", "🏠");
        JButton btnKhachHang = createNavButton("Quản lí khách hàng", "👥");
        JButton btnVe = createNavButton("Quản lí vé", "🎫");
        JButton btnHoaDon = createNavButton("Hóa đơn", "🧾");
        JButton btnTraCuu = createNavButton("Tra cứu thông tin", "🔍");
        JButton btnNhanVien = createNavButton("Quản lí nhân viên", "👤");
        JButton btnChuyenTau = createNavButton("Quản lí chuyến tàu", "🚆");
        JButton btnKhuyenMai = createNavButton("Quản lí khuyến mãi", "🏷️");
        JButton btnThongKe = createNavButton("Thống kê", "📊");
        JButton btnHoTro = createNavButton("Hỗ trợ", "❓");
        
        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnKhachHang);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnVe);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnHoaDon);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnTraCuu);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNhanVien);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnChuyenTau);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnKhuyenMai);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnThongKe);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnHoTro);
        
        JButton btnLogout = createNavButton("Đăng xuất", "🚪");
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 20, 15));
        logoutPanel.add(btnLogout, BorderLayout.SOUTH);
        
   
        sidebar.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);
        
        
        
        
        
        
        
        add(sidebar, BorderLayout.WEST);
        
        //=== phan dau
        
        Header header = new Header();
        add(header, BorderLayout.NORTH);
        
        
        // ==content
        
        // ===== CONTENT =====
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);

        content.add(new DashboardPanel(), "dashboard");
        content.add(new KhachHangPanel(), "khachhang");
        content.add(new BanVePanel(),"banve");
        content.add(new HoaDonPanel(),"hoadon");
        
        add(content, BorderLayout.CENTER);

        // ===== EVENT =====
        btnDashboard.addActionListener(e -> show("dashboard"));
        btnKhachHang.addActionListener(e -> show("khachhang"));
        btnVe.addActionListener(e-> show("banve"));
        btnHoaDon.addActionListener(e-> show("hoadon"));
        add(content, BorderLayout.CENTER);
        show("dashboard");
	}
	
	private JButton createNavButton(String text, String icon) {
        JButton btn = new JButton("  " + icon + "   " + text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Nút dài hết lề
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); // Làm nút trong suốt để tự vẽ nền khi hover
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        // Hiệu ứng khi di chuột vào (Hover)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setContentAreaFilled(true);
                btn.setBackground(new Color(51, 65, 85)); // Màu sáng hơn tí khi hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setContentAreaFilled(false);
            }
        });
        return btn;
    }
	
	private void show(String name) {
        cardLayout.show(content, name);
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
	
	
	
}
