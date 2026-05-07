package GUI;

import com.formdev.flatlaf.FlatClientProperties;
import Controller.TauVaToaController;
import Entity.Tau;
import Entity.ToaTau;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TauVaToaPanel extends JPanel {

    private TauVaToaController controller;

    private String[] options = {
        "Quản lý Tàu",
        "Quản lý Toa",
        "Thêm Ghế Lẻ",
        "Phát sinh Ghế tự động",
        "Bảo trì đoàn tàu"
    };

    // 1. Khai báo các biến giao diện ra ngoài để Controller có thể đổ dữ liệu vào
    private JTable tableToa;
    private DefaultTableModel modelToa;
    private JList<String> listTau;
    private DefaultListModel<String> modelListTau;
    
    private JTextField txtMaTau;
    private JTextField txtTenTau;
    private JTextField txtLoaiTau;
    private JTextField txtLanSuaChua;
    private JLabel lblStatus;

    // Hàm nhận Controller từ Main
    public void setController(TauVaToaController controller) {
        this.controller = controller;
        // Bảo controller load danh sách tàu từ DB lên ngay khi vừa bật
        if (this.controller != null) {
            this.controller.loadDanhSachTau();
        }
    }

    public TauVaToaPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // Khởi tạo các ô nhập liệu (Text Fields)
        txtMaTau = new JTextField(); 
        txtMaTau.setEditable(false); // Mã tàu không cho sửa
        txtTenTau = new JTextField();
        txtLoaiTau = new JTextField();
        txtLanSuaChua = new JTextField("0"); // Mặc định là 0
        lblStatus = new JLabel("Trạng thái: N/A", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // --- 1. HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250)); 
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("QUẢN LÝ TÀU & TOA TÀU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.black);
        
        JLabel subLabel = new JLabel("Quản lý danh sách tàu, toa tàu và ghế ngồi trong hệ thống");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLabel.setForeground(Color.black);

        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 0));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel);
        titleContainer.add(subLabel);
        headerPanel.add(titleContainer, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // --- 2. CENTER CONTENT ---
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 2a. LEFT PANEL (Sử dụng JSplitPane)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(320, 0));
        leftPanel.setOpaque(false);

        // --- Card Danh sách tàu ---
        JPanel listCard = createStyledCard("Danh sách tàu");
        modelListTau = new DefaultListModel<>(); // Dùng Model động để dễ cập nhật
        listTau = new JList<>(modelListTau);
        listTau.setFixedCellHeight(35);
        listTau.setSelectionBackground(new Color(232, 240, 254));
        
        // BẮT SỰ KIỆN CLICK VÀO TÀU -> Gọi Controller
        listTau.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listTau.getSelectedValue() != null) {
                String selectedItem = listTau.getSelectedValue();
                String maTau = selectedItem.split(" - ")[0].trim(); // Cắt lấy Mã Tàu
                if (controller != null) {
                    controller.chonTau(maTau); // Truyền xuống Controller
                }
            }
        });

        JScrollPane scrollTau = new JScrollPane(listTau);
        listCard.add(scrollTau, BorderLayout.CENTER);

        // --- Card Thông tin chi tiết ---
        JPanel formCard = createStyledCard("Thông tin tàu đã chọn");
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setOpaque(false);

        JPanel formBody = new JPanel(new GridLayout(0, 1, 0, 10));
        formBody.setOpaque(false);
        
        // Đưa các JTextField đã khai báo vào hàm
        formBody.add(createInputGroup("Mã Tàu:", txtMaTau));
        formBody.add(createInputGroup("Tên Tàu:", txtTenTau));
        formBody.add(createInputGroup("Loại Tàu:", txtLoaiTau));
        formBody.add(createInputGroup("Số lần sửa chữa:", txtLanSuaChua));
        formBody.add(lblStatus); // Add Label trạng thái

        JPanel btnGroup = new JPanel(new GridLayout(1, 2, 10, 0));
        btnGroup.add(createStyledButton("Thêm Mới", new Color(16, 185, 129), Color.WHITE));
        btnGroup.add(createStyledButton("Đổi Trạng Thái", Color.WHITE, Color.BLACK));
        formBody.add(btnGroup);

        formWrapper.add(formBody, BorderLayout.NORTH);
        formCard.add(formWrapper, BorderLayout.CENTER);

        // --- TẠO SPLIT PANE ---
        JSplitPane splitLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listCard, formCard);
        splitLeft.setContinuousLayout(true);
        splitLeft.setDividerSize(8); 
        splitLeft.setResizeWeight(0.5); 
        splitLeft.setBorder(null);
        splitLeft.setOpaque(false);

        leftPanel.add(splitLeft, BorderLayout.CENTER);

        // 2b. RIGHT PANEL (Bảng danh sách toa)
        JPanel rightPanel = createStyledCard("Chọn một tàu để xem danh sách toa");
        
        String[] cols = {"Mã Toa", "Mã Tàu", "Tên Toa", "Loại Toa", "Sức Chứa"};
        modelToa = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };
        tableToa = new JTable(modelToa);
        tableToa.setRowHeight(35);
        
        JTableHeader tableHeader = tableToa.getTableHeader();
        tableHeader.setBackground(new Color(10, 61, 98));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollTable = new JScrollPane(tableToa);
        rightPanel.add(scrollTable, BorderLayout.CENTER);

        // Footer buttons cho table
        JPanel footerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerButtons.setOpaque(false);
        footerButtons.add(createStyledButton("Thêm Toa", Color.WHITE, Color.BLACK));
        footerButtons.add(createStyledButton("Phát Sinh Ghế", Color.WHITE, Color.BLACK));
        footerButtons.add(createStyledButton("Thêm Ghế Lẻ", Color.WHITE, Color.BLACK));
        
        rightPanel.add(footerButtons, BorderLayout.SOUTH);

        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    // =========================================================
    // CÁC HÀM CẬP NHẬT GIAO DIỆN (Controller sẽ gọi mấy hàm này)
    // =========================================================

    // 1. Load danh sách tàu lên JList bên trái
    public void hienThiDanhSachTau(List<Tau> list) {
        modelListTau.clear(); // Xóa list cũ
        for (Tau t : list) {
            modelListTau.addElement(t.getMaTau() + " - " + t.getTenTau());
        }
    }

    // 2. Hiển thị chi tiết Tàu lên Form
    public void hienThiChiTietTau(Tau t) {
        txtMaTau.setText(t.getMaTau());
        txtTenTau.setText(t.getTenTau());
        txtLoaiTau.setText(t.getLoaiTau());
        
        String trangThai = t.getTrangThai() != null ? t.getTrangThai() : "Không xác định";
        lblStatus.setText("Trạng thái: " + trangThai);
        
        if (trangThai.equalsIgnoreCase("Đang hoạt động") || trangThai.equalsIgnoreCase("Hoạt động")) {
            lblStatus.setForeground(new Color(16, 185, 129)); // Xanh lá
        } else {
            lblStatus.setForeground(Color.RED); // Đỏ
        }
    }

    // 3. Đổ danh sách Toa của tàu đó lên Bảng bên phải
    public void hienThiDanhSachToa(List<ToaTau> list) { 
        modelToa.setRowCount(0); // Xóa bảng cũ
        for (ToaTau t : list) {
            modelToa.addRow(new Object[]{
                t.getMaToa(), 
                t.getMaTau(), 
                t.getTenToa(), 
                t.getLoaiToa(), 
                t.getSucChua() 
            });
        }
    }


    // =========================================================
    // HELPER METHODS (Dùng để tạo giao diện)
    // =========================================================

    private JPanel createStyledCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 15");

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(new Color(10, 61, 98));
        card.add(lblTitle, BorderLayout.NORTH);

        return card;
    }

    // Nhận trực tiếp JTextField thay vì String
    private JPanel createInputGroup(String label, JTextField txt) {
        JPanel group = new JPanel(new BorderLayout(0, 5));
        group.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.GRAY);
        
        txt.putClientProperty(FlatClientProperties.STYLE, "arc: 8; margin: 5,10,5,10");
        
        group.add(lbl, BorderLayout.NORTH);
        group.add(txt, BorderLayout.CENTER);
        return group;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, 38));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; focusWidth: 0");
        return btn;
    }

    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        for (String option : options) {
            JMenuItem it = new JMenuItem(option);
            it.setForeground(Color.WHITE);
            it.setBackground(new Color(30, 41, 59));
            it.addActionListener(e -> {
                String text = ((JMenuItem) e.getSource()).getText();
                JOptionPane.showMessageDialog(this, "Tính năng: " + text);
            });
            submenu.add(it);
        }
        return submenu;
    }
}