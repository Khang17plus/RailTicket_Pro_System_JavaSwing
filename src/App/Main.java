package App;

import GUI.*;
import Controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {

    // ==========================================
    // 1. KHAI BÁO CÁC THÀNH PHẦN MVC
    // ==========================================
    private KhachHangPanel khp;
    private KhachHangController khController;
    
    private NhanVienPanel nvp;
    private NhanVienController nvController;
    
    private TaiKhoanPanel taiKhoanPanel;
    private TaiKhoanController taiKhoanController;
    
    private TauVaToaPanel tauvatoa;
    private TauVaToaController tauvatoacontroller;
    
    private GaTauPanel gatau;
    private GaTauController gataucontroller;
    
    private ChuyenTauPanel chuyenTauPanel;
    private ChuyenTauController chuyenTauController;
    
    private KhuyenMaiPanel khuyenMaiPanel;
    private KhuyenMaiController khuyenMaiController;
    
    private ThongKePanel tkp;
    private ThongKeController tkController;
    
    private BanVePanel banVePanel;
    private BanVeController banVeController;
    
    private ThuePanel thuePanel;
    private HoTroPanel hoTroPanel;

    // Giao diện chính
    private CardLayout cardLayout;
    private JPanel content;

    // ==========================================
    // 2. KHAI BÁO CÁC SUB-MENU DẠNG CHUỖI MẶC ĐỊNH
    // ==========================================
    private final String[] veOptions = { "Thêm vé", "Xóa vé", "Sửa vé", "Tra cứu vé", "Bán vé" };
    private final String[] hoaDonOptions = { "Tạo hóa đơn", "Xóa hóa đơn", "Tra cứu hóa đơn" };
    private final String[] chuyenTauOptions = { "Thêm chuyến", "Xóa chuyến", "Sửa chuyến", "Tra cứu chuyến" };
    private final String[] thongKeOptions = { "Doanh thu", "Vé bán", "Khách hàng" };

    // ==========================================
    // CONSTRUCTOR: KHỞI TẠO HỆ THỐNG
    // ==========================================
    public Main() {
        initMVC();
        initFrame();
        
        // Cấu trúc Layout chính: Trái là Sidebar, Trên là Header, Giữa là Content
        setLayout(new BorderLayout());
        
        add(new Header(), BorderLayout.NORTH); // Giả sử class Header của bạn là một JPanel độc lập
        add(createSidebar(), BorderLayout.WEST);
        add(createContentPanel(), BorderLayout.CENTER);
        
        // Hiển thị trang chủ mặc định
        showCard("dashboard");

        // Chạy ngầm việc nạp dữ liệu ban đầu để tránh lag giao diện lúc khởi động
        SwingUtilities.invokeLater(() -> {
            if (chuyenTauController != null) {
                chuyenTauController.loadDanhSachChuyenTau();
            }
        });
    }

    // ==========================================
    // HÀM KHỞI TẠO MVC (Ráp nối View và Controller)
    // ==========================================
    private void initMVC() {
        khp = new KhachHangPanel();
        khController = new KhachHangController(khp);
        khp.setController(khController);

        nvp = new NhanVienPanel();
        nvController = new NhanVienController(nvp); 
        nvp.setController(nvController);

        taiKhoanPanel = new TaiKhoanPanel();
        taiKhoanController = new TaiKhoanController(taiKhoanPanel);
        taiKhoanPanel.setController(taiKhoanController);
        
        tauvatoa = new TauVaToaPanel();
        tauvatoacontroller = new TauVaToaController(tauvatoa);
        tauvatoa.setController(tauvatoacontroller);
        
        gatau = new GaTauPanel();
        gataucontroller = new GaTauController(gatau);
        gatau.setController(gataucontroller);
        
        chuyenTauPanel = new ChuyenTauPanel();
        chuyenTauController = new ChuyenTauController(chuyenTauPanel);
        chuyenTauPanel.setController(chuyenTauController);
        
        khuyenMaiPanel = new KhuyenMaiPanel();
        khuyenMaiController = new KhuyenMaiController(khuyenMaiPanel);
        khuyenMaiPanel.setController(khuyenMaiController);
        
        banVePanel = new BanVePanel();
        banVeController = new BanVeController(banVePanel);
        banVePanel.setController(banVeController);
        
        tkp = new ThongKePanel();
        tkController = new ThongKeController(tkp);
        
        thuePanel = new ThuePanel();
        hoTroPanel = new HoTroPanel();
    }

    // ==========================================
    // HÀM CẤU HÌNH FRAME CHÍNH
    // ==========================================
    private void initFrame() {
        setTitle("RailTicket Pro - Hệ Thống Quản Lý Bán Vé Tàu Hỏa");
        setSize(1400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // ==========================================
    // HÀM TẠO SIDEBAR (MENU BÊN TRÁI)
    // ==========================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(new Color(51, 65, 85));
        
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // -- Menu không có popup --
        menuPanel.add(createNavButton("Trang chủ", "img/home-2-fill.png"));
        menuPanel.add(Box.createVerticalStrut(5));
        
        // -- Menu dùng List<JMenuItem> động từ Panel --
        menuPanel.add(createNavButton("Quản lý tài khoản", "img/group-fill.png", taiKhoanPanel.getMenuOption(), "taikhoan"));
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(createNavButton("Quản lí khách hàng", "img/group-fill.png", khp.getMenuOption(), "khachhang"));
        menuPanel.add(Box.createVerticalStrut(5));
        
        // -- Menu dùng mảng String mặc định --
        menuPanel.add(createNavButton("Quản lí vé", "img/ticket-fill.png", veOptions));
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(createNavButton("Hóa đơn", "img/bill-line.png", hoaDonOptions));
        menuPanel.add(Box.createVerticalStrut(5));
        
        // -- Tiếp tục List động --
        menuPanel.add(createNavButton("Quản lí Thuế", "img/seo-fill.png", thuePanel.getMenuOption(), "thue"));
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(createNavButton("Quản lý nhân viên", "img/id-card-line.png", nvp.getMenuOption(), "nhanvien"));
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(createNavButton("Quản lí Tàu Và Toa", "img/subway.png", tauvatoa.getMenuOption(), "tauvatoa"));
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(createNavButton("Quản Lý Ga", "img/subway.png", gatau.getMenuOption(), "gatau"));
        menuPanel.add(Box.createVerticalStrut(5));
        
        // -- Trở lại mảng String --
        menuPanel.add(createNavButton("Quản lí chuyến tàu", "img/subway.png", chuyenTauOptions));
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(createNavButton("Quản lí khuyến mãi", "img/discount.png", khuyenMaiPanel.getMenuOption(), "khuyenmai"));
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(createNavButton("Thống kê", "img/bar-chart-box-line.png", thongKeOptions));
        menuPanel.add(Box.createVerticalStrut(5));
        
        // -- Menu Hỗ Trợ (Xử lý chuyển tab nội bộ) --
        List<JMenuItem> hoTroMenuItems = new ArrayList<>();
        hoTroMenuItems.add(createHoTroMenuItem("Hướng dẫn", 0));
        hoTroMenuItems.add(createHoTroMenuItem("Liên hệ", 1));
        hoTroMenuItems.add(createHoTroMenuItem("Phản hồi", 2));
        menuPanel.add(createNavButton("Hỗ trợ", "img/customer-service-line.png", hoTroMenuItems, "hotro"));
        
        // Nút Đăng xuất ở dưới cùng
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 20, 15));
        logoutPanel.add(createNavButton("Đăng xuất", "img/logout-box-line.png"), BorderLayout.SOUTH);
        
        sidebar.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);
        
        return sidebar;
    }

    // ==========================================
    // HÀM TẠO CONTENT (CARD LAYOUT)
    // ==========================================
    private JPanel createContentPanel() {
        cardLayout = new CardLayout();
        content = new JPanel(cardLayout);
        
        content.add(new DashboardPanel(), "dashboard");
        content.add(khp, "khachhang");
        content.add(nvp, "nhanvien");
        content.add(taiKhoanPanel, "taikhoan");
        content.add(new VeTauPanel(), "banve");
        content.add(new HoaDonPanel(), "hoadon");
        content.add(khuyenMaiPanel, "khuyenmai");
        content.add(tauvatoa, "tauvatoa");
        content.add(gatau, "gatau");
        content.add(chuyenTauPanel, "chuyentau");
        content.add(thuePanel, "thue");
        content.add(hoTroPanel, "hotro");
        content.add(tkp, "thongke");
        content.add(banVePanel, "panel_banve");
        
        return content;
    }

    // ==========================================
    // ROUTING & ĐIỀU HƯỚNG
    // ==========================================
    private void showCard(String name) {
        cardLayout.show(content, name);
    }

    private void triggerControllerRefresh(String cardName) {
        if ("chuyentau".equals(cardName) && chuyenTauController != null) {
            chuyenTauController.loadDanhSachChuyenTau();
        } else if ("thongke".equals(cardName) && tkController != null) {
            tkController.loadStatistics();
        } else if ("khachhang".equals(cardName) && khController != null) {
            khController.loadDataToTable();
        }
    }

    private String getCardNameByButtonText(String text) {
        switch (text) {
            case "Quản lí khách hàng": return "khachhang";
            case "Quản lí vé": return "panel_banve"; 
            case "Hóa đơn": return "hoadon";
            case "Quản lí chuyến tàu": return "chuyentau";
            case "Thống kê": return "thongke";
            case "Quản lý tài khoản": return "taikhoan";
            case "Quản lý nhân viên": return "nhanvien";
            case "Quản lí Tàu Và Toa": return "tauvatoa";
            case "Quản Lý Ga": return "gatau";
            case "Quản lí khuyến mãi": return "khuyenmai";
            case "Quản lí Thuế": return "thue";
            case "Hỗ trợ": return "hotro";
            default: return "dashboard";
        }
    }

    // ==========================================
    // BỘ 3 HÀM OVERLOADING: TẠO NÚT NAVIGATION TỐI ƯU
    // ==========================================

    // LOẠI 1: Nút đơn giản, không có menu thả xuống (Ví dụ: Trang chủ, Đăng xuất)
    private JButton createNavButton(String text, String iconURL) {
        JButton btn = createBaseButton(text, iconURL);
        btn.addActionListener(e -> {
            String targetCard = getCardNameByButtonText(text);
            showCard(targetCard);
            triggerControllerRefresh(targetCard);
        });
        return btn;
    }

    // LOẠI 2: Nút có menu thả xuống, truyền vào MẢNG STRING
    private JButton createNavButton(String text, String iconURL, String[] subItems) {
        JButton btn = createBaseButton(text, iconURL);
        String targetCard = getCardNameByButtonText(text);
        
        List<JMenuItem> menuItems = new ArrayList<>();
        if (subItems != null) {
            for (String itemName : subItems) {
                JMenuItem item = new JMenuItem(itemName);
                item.addActionListener(e -> {
                    showCard(targetCard);
                    triggerControllerRefresh(targetCard);
                });
                menuItems.add(item);
            }
        }
        attachHoverPopup(btn, menuItems, targetCard);
        return btn;
    }

    // LOẠI 3: Nút có menu thả xuống, truyền vào LIST JMENUITEM ĐỘNG TỪ PANEL (Có custom action)
    private JButton createNavButton(String text, String iconURL, List<JMenuItem> subItems, String targetCard) {
        JButton btn = createBaseButton(text, iconURL);
        if (subItems != null) {
            for (JMenuItem item : subItems) {
                // Thêm tính năng tự động nhảy card cho các item tùy chỉnh
                item.addActionListener(e -> {
                    showCard(targetCard);
                    triggerControllerRefresh(targetCard);
                });
            }
        }
        attachHoverPopup(btn, subItems, targetCard);
        return btn;
    }

    // --- CÁC HÀM TIỆN ÍCH DÙNG CHUNG CHO GIAO DIỆN NÚT ---

    private JButton createBaseButton(String text, String iconURL) {
        JButton btn = new JButton(text);
        try {
            File imgFile = new File(iconURL);
            if (imgFile.exists()) {
                btn.setIcon(new ImageIcon(imgFile.getAbsolutePath()));
                btn.setIconTextGap(15);
            }
        } catch (Exception ignored) {}
        
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setContentAreaFilled(true);
                btn.setBackground(new Color(51, 65, 85));
            }
            public void mouseExited(MouseEvent e) {
                btn.setContentAreaFilled(false);
            }
        });
        return btn;
    }

    private void attachHoverPopup(JButton btn, List<JMenuItem> subItems, String targetCard) {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(new Color(30, 41, 59, 200));
        
        final boolean[] isHoverPopup = {false};
        Timer hideTimer = new Timer(100, e -> {
            if (!isHoverPopup[0]) popup.setVisible(false);
        });
        hideTimer.setRepeats(false);
        
        if (subItems != null) {
            for (JMenuItem item : subItems) {
                item.setForeground(Color.WHITE);
                item.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        isHoverPopup[0] = true;
                        hideTimer.stop();
                    }
                    public void mouseExited(MouseEvent e) {
                        isHoverPopup[0] = false;
                        hideTimer.restart();
                    }
                });
                popup.add(item);
            }
        }
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hideTimer.stop();
                if (!popup.isVisible() && popup.getComponentCount() > 0) {
                    popup.show(btn, btn.getWidth() + 15, 0);
                    btn.setContentAreaFilled(true);
                    btn.setBackground(new Color(51, 65, 85));
                }
            }
            public void mouseExited(MouseEvent e) {
                hideTimer.restart();
                btn.setContentAreaFilled(false);
            }
        });

        // Bấm trực tiếp vào nút cha (Nút điều hướng bự) cũng nhảy màn hình
        btn.addActionListener(e -> {
            showCard(targetCard);
            triggerControllerRefresh(targetCard);
        });
    }

    private JMenuItem createHoTroMenuItem(String text, int tabIndex) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(e -> {
            showCard("hotro");
            hoTroPanel.setSelectTab(tabIndex);
        });
        return item;
    }
}