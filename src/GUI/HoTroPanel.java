package GUI;

import javax.swing.*;
import java.awt.*;
import Utils.SessionManager;
import Entity.NhanVien;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HoTroPanel extends JPanel {
    private CardLayout internalCard;
    private JPanel container;

    public HoTroPanel() {
        setLayout(new BorderLayout());
        internalCard = new CardLayout();
        container = new JPanel(internalCard);

        // Đăng ký 3 màn hình riêng biệt vào CardLayout nội bộ
        container.add(createHuongDanPanel(), "huongdan");
        container.add(createLienHePanel(), "lienhe");
        container.add(createPhanHoiPanel(), "phanhoi");

        add(container, BorderLayout.CENTER);
    }
    public void setSelectTab(int index) {
        if (index == 0) {
            internalCard.show(container, "huongdan");
        } else if (index == 1) {
            internalCard.show(container, "lienhe");
        } else if (index == 2) {
            internalCard.show(container, "phanhoi");
        }
        
        // Hai dòng quan trọng để ép Swing vẽ lại giao diện ngay lập tức
        container.revalidate();
        container.repaint();
    }
    
    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> list = new java.util.ArrayList<>();

        JMenuItem huongDan = new JMenuItem("Hướng dẫn");
        huongDan.addActionListener(e -> setSelectTab(0));

        JMenuItem lienHe = new JMenuItem("Liên hệ");
        lienHe.addActionListener(e -> setSelectTab(1));

        JMenuItem phanHoi = new JMenuItem("Phản hồi");
        phanHoi.addActionListener(e -> setSelectTab(2));

        list.add(huongDan);
        list.add(lienHe);
        list.add(phanHoi);

        return list;
    }

    // =========================================================================
    // 1. MÀN HÌNH HƯỚNG DẪN (Nâng cấp theo nghiệp vụ Đối tác / Doanh nghiệp)
    // =========================================================================
    private JPanel createHuongDanPanel() {
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);
        main.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // --- MỤC 1: QUY TRÌNH ĐẶT VÉ ĐOÀN / VÉ LẺ ---
        main.add(createHeaderLabel("1. QUY TRÌNH ĐẶT VÉ LÊ & ĐẶT NHIỀU VÉ CÙNG LÚC"));
        String[] stepTexts = {
            "Đặt vé lẻ / đặt hộ: Thao tác trực tiếp trên hệ thống, nhập thông tin khách hàng (CCCD/Hộ chiếu bắt buộc).",
            "Đặt nhiều vé cùng lúc: Sử dụng tính năng nhập danh sách từ file Excel để giữ chỗ hàng loạt.",
            "Xác nhận thông tin: Kiểm tra kỹ danh sách đi tàu và thông tin công ty trước khi chuyển sang bước thanh toán."
        };
        for (String s : stepTexts) main.add(createBulletLabel(s));
        main.add(Box.createVerticalStrut(20));

        // --- MỤC 2: CHÍNH SÁCH DOANH NGHIỆP & VAT (Dùng JTable) ---
        main.add(createHeaderLabel("2. CHÍNH SÁCH THANH TOÁN & HẠN MỨC DOANH NGHIỆP"));

        String[] columns = {"Đối tượng đối tác", "Chính sách thanh toán", "Quy định xuất hóa đơn VAT"};
        Object[][] data = {
            {"Doanh nghiệp thành viên", "Sử dụng hạn mức doanh nghiệp", "Tự động xuất hóa đơn điện tử"},
            {"Đại lý liên kết", "Trừ trực tiếp vào tài khoản ký quỹ", "Xuất theo gói/tuần hoặc theo yêu cầu"},
            {"Doanh nghiệp đặt theo đoàn", "Thanh toán trả sau / Chuyển khoản", "Xuất hóa đơn sau 24h hoàn thành chuyến"}
        };

        JTable table = new JTable(data, columns);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setBackground(Color.WHITE);

        table.setFillsViewportHeight(true);
        table.setEnabled(false); 

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(table, BorderLayout.CENTER);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, table.getRowHeight() * (data.length + 1))); 
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        main.add(tablePanel);
        main.add(Box.createVerticalStrut(20));

        // --- MỤC 3: QUY ĐỊNH ĐỔI TRẢ DOANH NGHIỆP ---
        main.add(createHeaderLabel("3. QUY ĐỊNH ĐỔI - TRẢ VÉ THEO CHÍNH SÁCH DOANH NGHIỆP"));
        main.add(createBoldLabel("Thời gian quy định:"));
        main.add(createBulletLabel("Hủy vé cá nhân / đặt hộ: Phải thực hiện trước giờ tàu chạy ít nhất 4 giờ."));
        main.add(createBulletLabel("Hủy vé đoàn / tập thể: Phải thực hiện trước giờ tàu chạy ít nhất 24 giờ để hoàn lại hạn mức."));
        
        main.add(Box.createVerticalStrut(10));
        main.add(createBoldLabel("Mức phí khấu trừ hoàn tiền:"));
        main.add(createBulletLabel("Thực hiện trước 24h: Khấu trừ theo tỷ lệ hợp đồng doanh nghiệp (Thường từ 5% - 10%)."));
        main.add(createBulletLabel("Sát giờ tàu chạy (Dưới 4h): Không hỗ trợ hoàn tiền hoặc áp dụng chính sách bảo lưu đặc biệt."));
        main.add(Box.createVerticalStrut(20));

        // --- MỤC 4: TRA CỨU BÁO CÁO KẾ TOÁN ---
        main.add(createHeaderLabel("4. TRA CỨU LỊCH SỬ ĐẶT VÉ & XUẤT BÁO CÁO CHI TIÊU"));
        main.add(createNormalLabel("- Đối soát kế toán: Vào mục Lịch sử giao dịch -> Chọn khoảng thời gian -> Xuất báo cáo báo cáo chi tiêu dạng Excel."));
        main.add(createNormalLabel("- Phân quyền tài khoản: Người đặt vé có thể thực hiện thao tác độc lập, tài khoản kế toán/quản trị sẽ duyệt chi cuối tháng."));

        JScrollPane mainScroll = new JScrollPane(main);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(20);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(mainScroll);
        return wrapper;
    }

    // --- CÁC HÀM PHỤ ĐỂ ĐỊNH DẠNG CHỮ ---
    private JLabel createHeaderLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l.setForeground(new Color(37, 99, 235)); 
        l.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel createBulletLabel(String text) {
        JLabel l = new JLabel("  • " + text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setBorder(BorderFactory.createEmptyBorder(2, 20, 2, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel createBoldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel createNormalLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    // =========================================================================
    // 2. MÀN HÌNH LIÊN HỆ (Bổ sung kênh hỗ trợ riêng biệt cho Đối tác/Doanh nghiệp)
    // =========================================================================
    private JPanel createLienHePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Phần tiêu đề giới thiệu trên cùng
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        
        JLabel lblTitle = new JLabel("TRUNG TÂM HỖ TRỢ ĐỐI TÁC & DOANH NGHIỆP", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(37, 99, 235));
        
        JLabel lblSub = new JLabel("Kết nối trực tiếp với bộ phận hỗ trợ riêng, xử lý các vấn đề phát sinh ngoài quy trình tự động", JLabel.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblSub.setForeground(Color.GRAY);
        
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Container chứa các Card thông tin liên hệ bằng FlowLayout
        JPanel cardsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 40));
        cardsContainer.setBackground(Color.WHITE);

        cardsContainer.add(createContactCard("img/phone_icon.png", "Hotline Ưu Tiên Doanh Nghiệp", "1800 6789 (Số riêng B2B)", "Hỗ trợ khẩn cấp các lỗi đặt vé, xác nhận chỗ đoàn."));
        cardsContainer.add(createContactCard("img/zalo_icon.png", "Zalo Doanh Nghiệp / Chat", "Zalo OA: Railway Support", "Hỗ trợ nhanh lỗi đặt chỗ, xử lý booking trực tuyến."));
        cardsContainer.add(createContactCard("img/email_icon.png", "Email Hỗ Trợ Kinh Doanh", "business@tauxe.com", "Gửi đề nghị báo giá, hợp đồng vận chuyển định kỳ."));
        cardsContainer.add(createContactCard("img/loc_icon.png", "Form Đặt Vé Số Lượng Lớn", "Yêu cầu xe riêng / Toa riêng", "Gửi trực tiếp yêu cầu báo giá đoàn đặc biệt qua form."));

        mainPanel.add(cardsContainer, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createContactCard(String iconPath, String title, String info, String subInfo) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(280, 200));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        card.putClientProperty("FlatLaf.style", "arc:20"); 

        JLabel lblCardTitle = new JLabel(title);
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCardTitle.setForeground(new Color(30, 41, 59));
        lblCardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCardInfo = new JLabel(info);
        lblCardInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCardInfo.setForeground(new Color(37, 99, 235));
        lblCardInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea txtSubInfo = new JTextArea(subInfo);
        txtSubInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSubInfo.setForeground(Color.GRAY);
        txtSubInfo.setLineWrap(true);
        txtSubInfo.setWrapStyleWord(true);
        txtSubInfo.setEditable(false);
        txtSubInfo.setOpaque(false);
        txtSubInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblCardTitle); card.add(Box.createVerticalStrut(15));
        card.add(lblCardInfo); card.add(Box.createVerticalStrut(10));
        card.add(txtSubInfo);
        
        return card;
    }

    // =========================================================================
    // 3. MÀN HÌNH PHẢN HỒI (Giữ nguyên bản gốc ban đầu 100%)
    // =========================================================================
    private JPanel createPhanHoiPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(245, 247, 250));
        
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        form.putClientProperty("FlatLaf.style", "arc:30"); // Panel thì dùng arc thoải mái

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Gửi phản hồi cho chúng tôi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        form.add(title, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        form.add(new JLabel("Chủ đề:"), gbc);
        JComboBox<String> cb = new JComboBox<>(new String[]{"Góp ý dịch vụ", "Báo lỗi phần mềm", "Khác"});
        cb.putClientProperty("JComponent.roundRect", true); // Dùng cái này cho ComboBox
        gbc.gridx = 1;
        form.add(cb, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Nội dung:"), gbc);
        JTextArea area = new JTextArea(6, 30);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        // Bỏ dòng gây lỗi Unknown style 'arc' ở đây
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.putClientProperty("FlatLaf.style", "arc:15"); // Bo góc ở ScrollPane bao quanh
        gbc.gridx = 1;
        form.add(scroll, gbc);

        JButton btn = new JButton("Gửi ngay");
        btn.setBackground(new Color(37, 99, 235));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.putClientProperty("JButton.arc", 15); // Dùng JButton.arc cho chắc chắn
        gbc.gridy = 3;
        form.add(btn, gbc);

        p.add(form);
        return p;
    }
    
}