package GUI;

import javax.swing.*;

import org.jfree.chart.ChartPanel;

import java.awt.*;

public class DashboardPanel extends JPanel {

private JPanel createCard(String title , String value ) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(220,220,220)),
            BorderFactory.createEmptyBorder(10,10,10,10)
			));
	
    JLabel t = new JLabel(title);
    t.setForeground(Color.GRAY);
    
    JLabel v = new JLabel(value);

    v.setFont(new Font("Arial", Font.BOLD, 20));

    card.add(t, BorderLayout.NORTH);
    card.add(v, BorderLayout.CENTER);
    
    
    
	return card;
}

private JPanel createCard(String title , String value, Icon icon  , Color iconBgColor) {
	
    JPanel card = new JPanel(new GridBagLayout());
    card.setBackground(Color.white);
    
    card.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createLineBorder(new Color(235, 235, 235), 1, true),
    		
    		 BorderFactory.createEmptyBorder(15, 15, 15, 15)
    		));
    
    
    GridBagConstraints gbc = new GridBagConstraints()
    		;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    JLabel iconLabel = new JLabel(icon);
    iconLabel.setOpaque(true);
    iconLabel.setBackground(iconBgColor);
    iconLabel.setPreferredSize(new Dimension(45, 45));
    iconLabel.setHorizontalAlignment(SwingConstants.LEFT);
    iconLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    gbc.gridx = 0; gbc.gridy =0;gbc.weightx = 0.5;
    card.add(iconLabel,gbc);
    
    
    JLabel lblTitle = new JLabel(title);
    lblTitle.setForeground(Color.GRAY);
    lblTitle.setFont(new Font("Arial", Font.PLAIN, 13));
    gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
    gbc.insets = new Insets(15, 0, 5, 0);
    card.add(lblTitle, gbc);
    
    JLabel lblValue = new JLabel(value);
    lblValue.setFont(new Font("Arial", Font.BOLD, 24));
    gbc.gridy = 2;
    gbc.insets = new Insets(0, 0, 5, 0);
    card.add(lblValue, gbc);
    JLabel lblSub = new JLabel("Hôm nay");
    
    lblSub.setForeground(Color.LIGHT_GRAY);
    lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
    
    gbc.gridy = 3;
    card.add(lblSub, gbc);
    
    
	return card;
}


	
private JPanel createChartCard(String title, ChartPanel chart) {

    // Panel ngoài
    JPanel panel = new JPanel(new BorderLayout());

    // Nền trắng
    panel.setBackground(Color.WHITE);

    // Border giống card
    panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,220)),
            BorderFactory.createEmptyBorder(10,10,10,10)
    ));

    // Title chart
    JLabel t = new JLabel(title);
    t.setFont(new Font("Arial", Font.BOLD, 14));

    // Gắn title lên trên
    panel.add(t, BorderLayout.NORTH);

    // Gắn chart vào giữa
    panel.add(chart, BorderLayout.CENTER);

    return panel;

}
	
	public DashboardPanel() {
		setLayout(new BorderLayout());
		setBackground(new Color(245, 247, 250));
		
		
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		header.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		

        JLabel title = new JLabel("Trang chủ");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        
        JLabel sub = new JLabel("Tổng quan hệ thống bán vé tàu");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(sub);
        add(header, BorderLayout.NORTH);

        
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 15, 0));
        statsRow.setOpaque(false);
        statsRow.add(createCard(
        	    "Vé đã bán",
        	    "1847",
        	    new ImageIcon("img/ticket-thin (1).png"),
        	    Color.white
        	    
        	));
        statsRow.add(createCard("Doanh thu", "73.5M", new ImageIcon("img/currency-dollar-thin.png"),Color.white));
        statsRow.add(createCard("Chuyến tàu", "156",new ImageIcon("img/train-thin.png"),Color.white));
        statsRow.add(createCard("Khách hàng", "12456",new ImageIcon("img/users-thin (3).png"),Color.white));
        main.add(statsRow);
        main.add(Box.createVerticalStrut(20));

        
        JPanel chartRow = new JPanel(new GridLayout(1, 2, 15, 0));
        chartRow.setOpaque(false);

        
        chartRow.add(createChartCard("Doanh thu tuần", ChartUtil.createBarChart()));

        // Chart phải (line chart)
        chartRow.add(createChartCard("Số vé bán ra", ChartUtil.createLineChart()));

        main.add(chartRow);
        add(main, BorderLayout.CENTER);
        
	}
	
	
	
	
	
	
	
	
	
}