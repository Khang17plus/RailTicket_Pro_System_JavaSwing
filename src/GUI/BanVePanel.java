package GUI;

import Controller.BanVeController;
import Entity.ChuyenTau;
import Entity.ToaTau;
import Entity.VeTau;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BanVePanel extends JPanel {
    private BanVeController controller;
    private JComboBox<String> cbGaDi, cbGaDen;
    private JDateChooser dateChooser;
    private JTable tblChuyen;
    private DefaultTableModel modelChuyen;
    private JPanel pnlToa, pnlGhe;
    private JTextArea txtCart;
    private JLabel lblTotal;
    
    private List<VeTau> gioHang = new ArrayList<>();
    private ChuyenTau currentChuyen;
    private String currentMaToa;
    private List<ChuyenTau> listChuyenTemp = new ArrayList<>();

    public BanVePanel() {
        initUI();
    }

    public void setController(BanVeController controller) {
        this.controller = controller;
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1.1 Header ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblTitle = new JLabel("BÁN VÉ TÀU HỎA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(30, 64, 175));
        JLabel lblDesc = new JLabel("Chọn chuyến – chọn chỗ – thanh toán nhanh chóng");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JPanel pnlTitleInfo = new JPanel(new GridLayout(2, 1));
        pnlTitleInfo.setBackground(Color.WHITE);
        pnlTitleInfo.add(lblTitle);
        pnlTitleInfo.add(lblDesc);
        pnlHeader.add(pnlTitleInfo, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // --- 1.2 Khu vực tìm kiếm ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm chuyến tàu"));

        // Giả lập dữ liệu Ga (bạn có thể thay bằng DAO gọi db)
        String[] dsGa = {"G01 - Ga Hà Nội", "G06 - Ga Vinh", "G09 - Ga Đà Nẵng", "G12 - Ga Nha Trang", "G15 - Ga Sài Gòn"};
        cbGaDi = new JComboBox<>(dsGa);
        cbGaDen = new JComboBox<>(dsGa);
        cbGaDen.setSelectedIndex(4); // Default SG
        
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date()); // Today
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(150, 30));

        JButton btnTim = createButton("Tìm chuyến", new Color(59, 130, 246));
        btnTim.addActionListener(e -> {
            if (controller != null) {
                String maGaDi = cbGaDi.getSelectedItem().toString().split(" - ")[0];
                String maGaDen = cbGaDen.getSelectedItem().toString().split(" - ")[0];
                java.time.LocalDate ngayDi = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                controller.timKiemChuyen(maGaDi, maGaDen, ngayDi);
            }
        });

        pnlSearch.add(new JLabel("Ga đi:"));
        pnlSearch.add(cbGaDi);
        pnlSearch.add(new JLabel("Ga đến:"));
        pnlSearch.add(cbGaDen);
        pnlSearch.add(new JLabel("Ngày đi:"));
        pnlSearch.add(dateChooser);
        pnlSearch.add(btnTim);

        // --- 1.3 Khu vực hiển thị kết quả (GridBagLayout/JSplitPane) ---
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        JSplitPane splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneMain.setDividerLocation(350);

        // Cột trái: DS Chuyến
        modelChuyen = new DefaultTableModel(new String[]{"Mã", "Tên Tàu", "Giờ đi", "Giờ đến"}, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblChuyen = new JTable(modelChuyen);
        tblChuyen.setRowHeight(30);
        tblChuyen.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblChuyen.getSelectedRow() != -1) {
                currentChuyen = listChuyenTemp.get(tblChuyen.getSelectedRow());
                if (controller != null) controller.loadToaByChuyen(currentChuyen);
            }
        });
        JScrollPane scrollChuyen = new JScrollPane(tblChuyen);
        scrollChuyen.setBorder(BorderFactory.createTitledBorder("Danh sách chuyến"));
        splitPaneMain.setLeftComponent(scrollChuyen);

        // Cột giữa (Toa) & Phải (Ghế)
        JSplitPane splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitRight.setDividerLocation(150);

        pnlToa = new JPanel();
        pnlToa.setLayout(new BoxLayout(pnlToa, BoxLayout.Y_AXIS));
        JScrollPane scrollToa = new JScrollPane(pnlToa);
        scrollToa.setBorder(BorderFactory.createTitledBorder("Danh sách Toa"));
        splitRight.setLeftComponent(scrollToa);

        pnlGhe = new JPanel(new GridLayout(0, 4, 10, 10)); // Lưới ghế 4 cột
        pnlGhe.setBackground(Color.WHITE);
        JScrollPane scrollGhe = new JScrollPane(pnlGhe);
        scrollGhe.setBorder(BorderFactory.createTitledBorder("Sơ đồ ghế"));
        splitRight.setRightComponent(scrollGhe);

        splitPaneMain.setRightComponent(splitRight);
        pnlCenter.add(splitPaneMain, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- 1.4 Khu vực thanh toán (Footer) ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createTitledBorder("Giỏ hàng & Thanh toán"));
        pnlFooter.setPreferredSize(new Dimension(0, 150));

        txtCart = new JTextArea();
        txtCart.setEditable(false);
        JScrollPane scrollCart = new JScrollPane(txtCart);
        pnlFooter.add(scrollCart, BorderLayout.CENTER);

        JPanel pnlPay = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        lblTotal = new JLabel("Tổng tiền: 0 VND");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(Color.RED);
        
        JButton btnPay = createButton("Thanh toán", new Color(34, 197, 94));
        btnPay.addActionListener(e -> {
            if (gioHang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ghế trước khi thanh toán!");
                return;
            }
            // Dialog đơn giản nhập thông tin thanh toán
            String maKH = JOptionPane.showInputDialog(this, "Nhập mã Khách Hàng (VD: KH01):", "KH01");
            if(maKH == null || maKH.trim().isEmpty()) return;
            
            String[] phuongThuc = {"Tiền mặt", "Chuyển khoản", "Thẻ"};
            String pt = (String) JOptionPane.showInputDialog(this, "Chọn phương thức TT:", "Thanh toán", 
                    JOptionPane.QUESTION_MESSAGE, null, phuongThuc, phuongThuc[0]);
            
            if(pt != null && controller != null){
                controller.thanhToan(gioHang, maKH, "NV01", pt); // Tạm gán mã NV01 (Admin)
            }
        });

        pnlPay.add(lblTotal);
        pnlPay.add(btnPay);
        pnlFooter.add(pnlPay, BorderLayout.SOUTH);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    // --- CÁC HÀM CẬP NHẬT GIAO DIỆN ---
    public void hienThiDanhSachChuyen(List<ChuyenTau> ds) {
        SwingUtilities.invokeLater(() -> {
            listChuyenTemp = ds;
            modelChuyen.setRowCount(0);
            for (ChuyenTau c : ds) {
                modelChuyen.addRow(new Object[]{c.getMaChuyen(), c.getTenTau(), 
                    c.getThoiGianDi().toString().replace("T", " "), 
                    c.getThoiGianDen().toString().replace("T", " ")});
            }
            pnlToa.removeAll(); pnlGhe.removeAll();
            pnlToa.revalidate(); pnlToa.repaint();
            pnlGhe.revalidate(); pnlGhe.repaint();
            resetGioHang();
        });
    }

    public void hienThiDanhSachToa(List<ToaTau> ds) {
        SwingUtilities.invokeLater(() -> {
            pnlToa.removeAll();
            for (ToaTau t : ds) {
                JButton btn = createButton(t.getTenToa(), Color.LIGHT_GRAY);
                btn.setMaximumSize(new Dimension(130, 40));
                btn.addActionListener(e -> {
                    currentMaToa = t.getMaToa();
                    if (controller != null && currentChuyen != null) {
                        controller.loadGheByToa(currentMaToa, currentChuyen.getMaChuyen());
                    }
                });
                pnlToa.add(Box.createRigidArea(new Dimension(0, 5)));
                pnlToa.add(btn);
            }
            pnlToa.revalidate();
            pnlToa.repaint();
            pnlGhe.removeAll(); pnlGhe.revalidate(); pnlGhe.repaint();
        });
    }

    public void hienThiSoDoGhe(List<VeTau> dsVe, String maChuyen, String maToa) {
        SwingUtilities.invokeLater(() -> {
            pnlGhe.removeAll();
            for (VeTau v : dsVe) {
                JButton btnGhe = new JButton("Ghế " + v.getSoGhe());
                btnGhe.setPreferredSize(new Dimension(80, 60));
                btnGhe.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnGhe.putClientProperty("FlatLaf.style", "arc:10"); // Style bo góc FlatLaf
                
                // Kiem tra gio hang (neu dang duoc chon)
                boolean isInCart = gioHang.stream().anyMatch(ve -> ve.getMaGhe().equals(v.getMaGhe()));

                if (v.getTrangThai().equalsIgnoreCase("Đã bán")) {
                    btnGhe.setBackground(new Color(239, 68, 68)); // Đỏ
                    btnGhe.setForeground(Color.WHITE);
                    btnGhe.setEnabled(false);
                } else if (isInCart) {
                    btnGhe.setBackground(new Color(249, 115, 22)); // Cam
                    btnGhe.setForeground(Color.WHITE);
                } else {
                    btnGhe.setBackground(new Color(34, 197, 94)); // Xanh lá
                    btnGhe.setForeground(Color.WHITE);
                }

                btnGhe.addActionListener(e -> {
                    boolean isSelected = btnGhe.getBackground().equals(new Color(249, 115, 22));
                    if (isSelected) {
                        btnGhe.setBackground(new Color(34, 197, 94)); // Về trống
                        capNhatGioHang(v, false);
                    } else {
                        btnGhe.setBackground(new Color(249, 115, 22)); // Thành chọn
                        capNhatGioHang(v, true);
                    }
                });
                pnlGhe.add(btnGhe);
            }
            pnlGhe.revalidate();
            pnlGhe.repaint();
        });
    }

    public void capNhatGioHang(VeTau ve, boolean isAdd) {
        if (isAdd) {
            gioHang.add(ve);
        } else {
            gioHang.removeIf(v -> v.getMaGhe().equals(ve.getMaGhe()));
        }
        
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (VeTau v : gioHang) {
            sb.append(String.format("Ghế %d (Toa: %s) - %.0f VND\n", v.getSoGhe(), currentMaToa, v.getGiaGoc()));
            total += v.getGiaGoc();
        }
        txtCart.setText(sb.toString());
        lblTotal.setText(String.format("Tổng tiền: %,.0f VND", total));
    }

    public void resetGioHang() {
        gioHang.clear();
        txtCart.setText("");
        lblTotal.setText("Tổng tiền: 0 VND");
    }

    public void reloadGhe() {
        if (controller != null && currentChuyen != null && currentMaToa != null) {
            controller.loadGheByToa(currentMaToa, currentChuyen.getMaChuyen());
        }
    }

    // Tiện ích tạo button bo góc chuẩn FlatLaf style
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", "arc: 15;  margin: 0,0,0,0; borderWidth: 0; focusWidth: 0;");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}