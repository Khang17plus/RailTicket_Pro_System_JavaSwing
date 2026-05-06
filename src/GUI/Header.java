package GUI;

import javax.swing.*;

import Utils.SessionManager;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import Entity.TaiKhoan;

public class Header extends JPanel {
	 String UserName ="Nguyễn Hoàng A";
	 int typeUser  = 1;
	 private String TypeUserToString(int type) {
		 return (type ==1 ) ? "Quản Lý" :"Nhân viên" ;
	 };
	 
	 
    public Header() {
    	
    	TaiKhoan tk = SessionManager.getInstance().getTaiKhoanDangNhap();
    	
    	// panel 
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 70));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        
     // left panel
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,5) );
        leftPanel.setOpaque(false);
        JLabel logo = new JLabel();
        ImageIcon logoIcon = new ImageIcon("img/logo_taulua.png  ");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(130, 40,Image.SCALE_SMOOTH );
        logo.setIcon(new ImageIcon(scaledLogo) );
        
        leftPanel.add(logo);
        
        // right Panel 
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        rightPanel.setOpaque(false);
        // rinht user 
        
        ImageIcon iconNotify = new ImageIcon("img/bell.png");
        Image imgBell = iconNotify.getImage().getScaledInstance(24,24, Image.SCALE_SMOOTH);
        JLabel labelNotify = new JLabel( new ImageIcon(imgBell));
        
        
        JLabel lblUserInfo = new JLabel("<html><div style='text-align: right;'>"
                + "<b style='color:#2f3542;'>" +UserName+"</b><br>"
                + "<span style='font-size:9px; color:gray;'>"+TypeUserToString(typeUser)+"</span>"
                + "</div></html>");
        
        
        
        
        ImageIcon userIcon = new ImageIcon("img/avata_natra.png");
        Image avataUser = userIcon.getImage();
        // avatar 
        JPanel avatar = new JPanel() { 
        @Override 
	        protected void paintComponent(Graphics g) {
	         super.paintComponent(g);
	         	Graphics2D g2 = (Graphics2D) g.create();
	         	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	         	g2.setClip(new java.awt.geom.Ellipse2D.Double(0,0,35,35));
	         	g2.drawImage(avataUser, 0, 0, 35,35, null);
	         	g2.dispose();
	        }
        	
        	
        }; 
        

        
        
        
        avatar.setPreferredSize(new Dimension(35,35));
        avatar.setOpaque(false);
        rightPanel.add(labelNotify);
        rightPanel.add(lblUserInfo);
        rightPanel.add(avatar);
        
        
        
        
        
        
        
        add(leftPanel,BorderLayout.WEST);
        add(rightPanel,BorderLayout.EAST);
    }
}