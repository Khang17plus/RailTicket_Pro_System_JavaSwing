package App;
import GUI.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.ui.FlatPopupMenuSeparatorUI;

import Controller.GaTauController;
import Controller.KhachHangController;
import Controller.KhuyenMaiController;
import Controller.TauVaToaController;
import Controller.ThongKeController;
import Controller.BanVeController;
import Controller.ChuyenTauController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
public class Main extends JFrame {
	private KhachHangPanel khp ;
	private KhachHangController khController;
	private NhanVienPanel nvp;
	 
	private TauVaToaPanel tauvatoa;
	private TauVaToaController tauvatoacontroller;
	 
	private GaTauPanel gatau;
	private GaTauController gataucontroller;
	 
	private CardLayout cardLayout;
	private JPanel content;
	
	private ChuyenTauPanel chuyenTauPanel;
    private ChuyenTauController chuyenTauController;
    
    private KhuyenMaiPanel khuyenMaiPanel;
    private KhuyenMaiController khuyenMaiController;
    
    private ThongKePanel tkp;
    private ThongKeController tkController;
    private BanVePanel banVePanel;
    private BanVeController banVeController;
    
    private HoTroPanel hoTroPanel;
    
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

		String[] nhanVienOptions = null;

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
	
		private JButton createNavButton(String text, String iconURL, List<JMenuItem> subItems, String cardName) {
			JButton btn = new JButton(text);
		try {
				
				java.io.File imgFile = new java.io.File(iconURL);
				if (imgFile.exists()) {
					btn.setIcon(new ImageIcon(imgFile.getAbsolutePath()));
					btn.setIconTextGap(15);
				}
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
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
		    
		  
			   
			   List<JMenuItem> listIT = subItems;
			   for(JMenuItem item : listIT) {
				   item.addActionListener(e -> show(cardName));
				
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
		
	private JButton createNavButton(String text, String iconURL, String[] subItems) {
		JButton btn = new JButton(text);
	try {
			
			java.io.File imgFile = new java.io.File(iconURL);
			if (imgFile.exists()) {
				btn.setIcon(new ImageIcon(imgFile.getAbsolutePath()));
				btn.setIconTextGap(15);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
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
		  // 🔥 TẠO OBJECT
	    khp = new KhachHangPanel();
	    khController = new KhachHangController(khp);

	    // 🔥 NỐI MVC
	    khp.setController(khController);
	    nvp = new NhanVienPanel();
	    
	    
	    tauvatoa = new TauVaToaPanel();
	    tauvatoacontroller = new TauVaToaController(tauvatoa);
	    tauvatoa.setController(tauvatoacontroller);
	    
	    
	    
	    gatau = new GaTauPanel();
	    gataucontroller = new GaTauController(gatau);
	    gatau.setController(gataucontroller);;
		
	    chuyenTauPanel = new ChuyenTauPanel();
	    chuyenTauController = new ChuyenTauController(chuyenTauPanel);
	    chuyenTauPanel.setController(chuyenTauController);
	    
	    tkp = new ThongKePanel();
        tkController = new ThongKeController(tkp);
	    
	    khuyenMaiPanel = new KhuyenMaiPanel();
	    khuyenMaiController = new KhuyenMaiController(khuyenMaiPanel);
	    khuyenMaiPanel.setController(khuyenMaiController);
	    
	    banVePanel = new BanVePanel();
	    banVeController = new BanVeController(banVePanel);
	    banVePanel.setController(banVeController);
	    
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
		
        ImageIcon ic = new ImageIcon();
        
        JButton btnDashboard = createNavButton("Trang chủ", "img/home-2-fill.png");
        JButton btnKhachHang = createNavButton("Quản lí khách hàng", "img/group-fill.png", khachHangOptions);
        JButton btnVe = createNavButton("Quản lí vé", "img/ticket-fill.png", veOptions);
        JButton btnHoaDon = createNavButton("Hóa đơn", "img/bill-line.png", hoaDonOptions);
        JButton btnThue = createNavButton("Quản lí Thuế", "img/seo-fill.png", traCuuOptions);
        JButton btnNhanVien = createNavButton("Quản lý nhân viên", "img/id-card-line.png", nhanVienOptions);
        JButton btnTauVaToa = createNavButton("Quản lí Tàu Và Toa", "img/subway.png",tauvatoa.getMenuOption(),"tauvatoa" );
        JButton btnGaTau  =  createNavButton("Quản Lý Ga", "img/subway.png",gatau.getMenuOption(), "gatau");
        JButton btnChuyenTau = createNavButton("Quản lí chuyến tàu", "img/subway.png", chuyenTauOptions);
        JButton btnKhuyenMai = createNavButton("Quản lí khuyến mãi", "img/discount.png", khuyenMaiPanel.getMenuOption(), "khuyenmai");
        JButton btnThongKe = createNavButton("Thống kê", "img/bar-chart-box-line.png", thongKeOptions);
        
     // 1. Khởi tạo panel Hỗ trợ trước
        hoTroPanel = new HoTroPanel();

        // 2. Tạo danh sách các Menu Item con và gán sự kiện click cho từng cái
        List<JMenuItem> hoTroMenuItems = new ArrayList<>();

        JMenuItem mniHuongDan = new JMenuItem("Hướng dẫn");
        mniHuongDan.addActionListener(e -> {
            show("hotro"); // Gọi hàm show của Main để hiển thị cục bự HoTroPanel
            hoTroPanel.setSelectTab(0); // Gọi hàm chuyển tab nội bộ
        });
        hoTroMenuItems.add(mniHuongDan);

        JMenuItem mniLienHe = new JMenuItem("Liên hệ");
        mniLienHe.addActionListener(e -> {
            show("hotro");
            hoTroPanel.setSelectTab(1);
        });
        hoTroMenuItems.add(mniLienHe);

        JMenuItem mniPhanHoi = new JMenuItem("Phản hồi");
        mniPhanHoi.addActionListener(e -> {
            show("hotro");
            hoTroPanel.setSelectTab(2);
        });
        hoTroMenuItems.add(mniPhanHoi);

        // 3. Truyền list này vào cái hàm createNavButton (phiên bản dùng List) của bạn
        JButton btnHoTro = createNavButton("Hỗ trợ", "img/customer-service-line.png", hoTroMenuItems, "hotro");
        
        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnKhachHang);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnVe);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnHoaDon);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnThue);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnNhanVien);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnTauVaToa);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnGaTau);
        menuPanel.add(Box.createVerticalStrut(5));   
        menuPanel.add(btnChuyenTau);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnKhuyenMai);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnThongKe);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnHoTro);
        
        JButton btnLogout = createNavButton("Đăng xuất", "img/logout-box-line.png");
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
        content.add(nvp,"nhanvien");
        content.add(new VeTauPanel(),"banve");
        content.add(new HoaDonPanel(),"hoadon");
        content.add(khuyenMaiPanel, "khuyenmai");
        content.add(tauvatoa, "tauvatoa");
        content.add(gatau, "gatau");
        content.add(chuyenTauPanel, "chuyentau");
        content.add(new ThuePanel(),"thue");
        content.add(hoTroPanel, "hotro");
        content.add(tkp, "thongke");
        content.add(banVePanel, "panel_banve"); // Đặt tên card là "panel_banve"
        // ===== EVENT =====
        btnDashboard.addActionListener(e -> show("dashboard"));
        btnKhachHang.addActionListener(e -> show("khachhang"));
        btnNhanVien.addActionListener(e-> show("nhanvien"));
        btnVe.addActionListener(e-> show("panel_banve"));
        btnHoaDon.addActionListener(e-> show("hoadon"));
        btnKhuyenMai.addActionListener(e-> show("khuyenmai"));
        btnChuyenTau.addActionListener(e->show("chuyentau"));
        btnThue.addActionListener(e->show("thue"));
        btnTauVaToa.addActionListener(e->show("tauvatoa"));
        btnGaTau.addActionListener(e->show("gatau"));
        btnHoTro.addActionListener(e->show("hotro"));
        btnThongKe.addActionListener(e -> {
            show("thongke"); // Chuyển sang màn hình thống kê
            if (tkController != null) {
                tkController.loadStatistics(); // Tự động load dữ liệu/vẽ biểu đồ ngay khi bấm
            }
        });
        add(content, BorderLayout.CENTER);
        show("dashboard");
	}
	
	private JButton createNavButton(String text, String iconURL) {

        JButton btn = new JButton(text);
		
		
		try {
			
			java.io.File imgFile = new java.io.File(iconURL);
			if (imgFile.exists()) {
				btn.setIcon(new ImageIcon(imgFile.getAbsolutePath()));
				btn.setIconTextGap(15);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
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


	
	
}
