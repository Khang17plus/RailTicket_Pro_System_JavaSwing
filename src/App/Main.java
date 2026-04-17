package App;
import GUI.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.ui.FlatPopupMenuSeparatorUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.util.List;
public class Main extends JFrame {
	 KhachHangPanel khp = new KhachHangPanel();
	private CardLayout cardLayout;
	private JPanel content;
	String[] khachHangOptions = null;

		String[] veOptions = {
		    "Thêm vé",
		    "Xóa vé",
		    "Sửa vé",
		    "Tra cứu vé",
		    "Bán vé"
		};

		String[] hoaDonOptions = {
		    "Tạo hóa đơn",
		    "Xóa hóa đơn",
		    "Tra cứu hóa đơn"
		};

		String[] traCuuOptions = {
		    "Tra cứu vé",
		    "Tra cứu chuyến tàu",
		    "Tra cứu khách hàng"
		};

		String[] nhanVienOptions = {
		    "Thêm nhân viên",
		    "Xóa nhân viên",
		    "Sửa thông tin",
		    "Phân quyền"
		};

		String[] chuyenTauOptions = {
		    "Thêm chuyến",
		    "Xóa chuyến",
		    "Sửa chuyến",
		    "Tra cứu chuyến"
		};

		String[] khuyenMaiOptions = {
		    "Tạo khuyến mãi",
		    "Xóa khuyến mãi",
		    "Sửa khuyến mãi"
		};

		String[] thongKeOptions = {
		    "Doanh thu",
		    "Vé bán",
		    "Khách hàng"
		};

		String[] hoTroOptions = {
		    "Hướng dẫn",
		    "Liên hệ",
		    "Phản hồi"
		};
	
	
	private JButton createNavButton(String text, String icon, String[] subItems) {
		JButton btn = new JButton("  " + icon + "   " + text);
	    btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    btn.setContentAreaFilled(false);
	    btn.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));
	    btn.setHorizontalAlignment(SwingConstants.LEFT);
	    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    btn.setForeground(Color.WHITE);
	    
	    
	   
	    
	    

	    JPopupMenu popup = new JPopupMenu();
	    popup.setBackground(new Color(30,41,59,200));
	    
	    final boolean[] isHoverPopup = {false};
	    
	    Timer hideTimer = new Timer(100, e -> {
			  if (!isHoverPopup[0]) {
				  popup.setVisible(false);
			  }
			   
			   
		   });
		   
		   hideTimer.setRepeats(false);
	    
	   if(subItems != null) {
		   for ( String item : subItems) {
		    	JMenuItem menuItem = new JMenuItem(item);
		    	
		    	menuItem.addMouseListener(new MouseAdapter() {
				
		    		public void mouseEntered(MouseEvent e) {
		                isHoverPopup[0] = true;
		                hideTimer.stop();
		            };
		            public void mouseExited(MouseEvent e) {
		                isHoverPopup[0] = false;
		                hideTimer.restart();
		            }
		    	
		    	});
		    	
		    	
		    	menuItem.setForeground(Color.WHITE);
		    	popup.add(menuItem);
		    }
	   } else {
		   
		   List<JMenuItem> listIT = khp.getMenuOption();
		   for(JMenuItem item : listIT) {
			   item.addActionListener(e -> show("khachhang"));
			
			   item.addMouseListener(new MouseAdapter() {
					
		    		public void mouseEntered(MouseEvent e) {
		                isHoverPopup[0] = true;
		                hideTimer.stop();
		            };
		            public void mouseExited(MouseEvent e) {
		                isHoverPopup[0] = false;
		                hideTimer.restart();
		            }
		           
		    	
		    	});
			   item.setForeground(Color.WHITE);
			   
			   popup.add(item);
		   }
	   }
	    
	   
	  
	    
	    btn.addMouseListener(new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			hideTimer.stop();;
			if(!popup.isVisible()){
	    	popup.show(btn,btn.getWidth()+15,0);
	    	 	btn.setContentAreaFilled(true);
	            btn.setBackground(new Color(51, 65, 85));
	    }
		}
		
		public void mouseExited(MouseEvent e ) {
			hideTimer.restart();
			btn.setContentAreaFilled(false);
		}
	    
	    
	    }
	    );
	    

	    
		
		
		
	    return btn;
	}
	
	
	
	
	
	public Main()
	{
		setTitle("RailTicket Pro");
		setSize(1400,800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		// === thanh ben 
		
		JPanel sidebar = new JPanel();
		sidebar.setPreferredSize(new Dimension(230, 0));
		
		
		sidebar.setLayout(new BorderLayout());
		sidebar.setBackground(new Color(51, 65, 85));
		
		JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15)); // Tạo padding lề
		
        
        JButton btnDashboard = createNavButton("Trang chủ", "🏠");
        JButton btnKhachHang = createNavButton("Quản lí khách hàng", "👥", khachHangOptions);
        JButton btnVe = createNavButton("Quản lí vé", "🎫", veOptions);
        JButton btnHoaDon = createNavButton("Hóa đơn", "🧾", hoaDonOptions);
        JButton btnTraCuu = createNavButton("Tra cứu thông tin", "🔍", traCuuOptions);
        JButton btnNhanVien = createNavButton("Quản lý nhân viên", "👤", nhanVienOptions);
        JButton btnChuyenTau = createNavButton("Quản lí chuyến tàu", "🚆", chuyenTauOptions);
        JButton btnKhuyenMai = createNavButton("Quản lí khuyến mãi", "🏷️", khuyenMaiOptions);
        JButton btnThongKe = createNavButton("Thống kê", "📊", thongKeOptions);
        JButton btnHoTro = createNavButton("Hỗ trợ", "❓", hoTroOptions);
        
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
        content.add(khp, "khachhang");
        content.add(new BanVePanel(),"banve");
        content.add(new HoaDonPanel(),"hoadon");
        
        
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
    	try {
    		UIManager.setLookAndFeel(new FlatMacLightLaf());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
        new Main().setVisible(true);
    }
	
	
	
}
