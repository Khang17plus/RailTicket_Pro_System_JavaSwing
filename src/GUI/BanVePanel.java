package GUI;

import Controller.BanVeController;
import Entity.ChuyenTau;
import Entity.GaTau;
import Entity.KhachHang;
import Entity.ToaTau;
import Entity.VeTau;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<GaTau> listGaTemp = new ArrayList<>();

    public BanVePanel() {
        initUI();
    }

    public void setController(BanVeController controller) {
        this.controller = controller;
        // Load danh sách ga từ database khi controller được set
        if (this.controller != null) {
            this.controller.loadDanhSachGa();
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Header ---
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

        // --- Khu vực tìm kiếm ---
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm chuyến tàu"));

        // ComboBox ga đi và ga đến (sẽ load từ DB)
        cbGaDi = new JComboBox<>();
        cbGaDen = new JComboBox<>();
        cbGaDi.setPreferredSize(new Dimension(250, 30));
        cbGaDen.setPreferredSize(new Dimension(250, 30));
        
        // DateChooser - mặc định là ngày mai (để có dữ liệu mẫu)
        dateChooser = new JDateChooser();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1); // Ngày mai
        dateChooser.setDate(cal.getTime());
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(150, 30));

        JButton btnTim = createButton("🔍 Tìm chuyến", new Color(59, 130, 246));
        btnTim.addActionListener(e -> timChuyen());

        pnlSearch.add(new JLabel("Ga đi:"));
        pnlSearch.add(cbGaDi);
        pnlSearch.add(new JLabel("Ga đến:"));
        pnlSearch.add(cbGaDen);
        pnlSearch.add(new JLabel("Ngày đi:"));
        pnlSearch.add(dateChooser);
        pnlSearch.add(btnTim);

        // --- Khu vực hiển thị kết quả ---
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        JSplitPane splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneMain.setDividerLocation(400);

        // Cột trái: Danh sách chuyến
        String[] cols = {"Mã Chuyến", "Tên Tàu", "Ga Đi", "Ga Đến", "Giờ Đi", "Giờ Đến"};
        modelChuyen = new DefaultTableModel(cols, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblChuyen = new JTable(modelChuyen);
        tblChuyen.setRowHeight(35);
        tblChuyen.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblChuyen.getSelectedRow() != -1) {
                currentChuyen = listChuyenTemp.get(tblChuyen.getSelectedRow());
                if (controller != null) controller.loadToaByChuyen(currentChuyen);
            }
        });
        JScrollPane scrollChuyen = new JScrollPane(tblChuyen);
        scrollChuyen.setBorder(BorderFactory.createTitledBorder("Danh sách chuyến"));
        splitPaneMain.setLeftComponent(scrollChuyen);

        // Cột phải: Toa & Ghế
        JSplitPane splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitRight.setDividerLocation(200);

        // Panel Toa
        pnlToa = new JPanel();
        pnlToa.setLayout(new BoxLayout(pnlToa, BoxLayout.Y_AXIS));
        pnlToa.setBackground(Color.WHITE);
        JScrollPane scrollToa = new JScrollPane(pnlToa);
        scrollToa.setBorder(BorderFactory.createTitledBorder("Danh sách Toa"));
        splitRight.setLeftComponent(scrollToa);

        // Panel Ghế
        pnlGhe = new JPanel(new GridLayout(0, 5, 10, 10));
        pnlGhe.setBackground(Color.WHITE);
        JScrollPane scrollGhe = new JScrollPane(pnlGhe);
        scrollGhe.setBorder(BorderFactory.createTitledBorder("Sơ đồ ghế"));
        splitRight.setRightComponent(scrollGhe);

        splitPaneMain.setRightComponent(splitRight);
        pnlCenter.add(splitPaneMain, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- Footer: Giỏ hàng & Thanh toán ---
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createTitledBorder("Giỏ hàng & Thanh toán"));
        pnlFooter.setPreferredSize(new Dimension(0, 180));

        txtCart = new JTextArea();
        txtCart.setEditable(false);
        txtCart.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollCart = new JScrollPane(txtCart);
        pnlFooter.add(scrollCart, BorderLayout.CENTER);

        JPanel pnlPay = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        lblTotal = new JLabel("Tổng tiền: 0 VND");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(Color.RED);
        
        JButton btnPay = createButton("💳 Thanh toán", new Color(34, 197, 94));
        btnPay.addActionListener(e -> thanhToanVoiThongTinHanhKhach());

        pnlPay.add(lblTotal);
        pnlPay.add(btnPay);
        pnlFooter.add(pnlPay, BorderLayout.SOUTH);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    // ==================== LOAD DỮ LIỆU TỪ DATABASE ====================
    
    /**
     * Load danh sách ga từ database vào ComboBox
     */
    public void loadGaComboBox(List<GaTau> dsGa) {
        listGaTemp = dsGa;
        cbGaDi.removeAllItems();
        cbGaDen.removeAllItems();
        
        for (GaTau ga : dsGa) {
            String item = ga.getMaGa() + " - " + ga.getTenGa();
            cbGaDi.addItem(item);
            cbGaDen.addItem(item);
        }
        
        // Mặc định: Ga đầu là Hà Nội, Ga cuối là Sài Gòn
        if (dsGa.size() > 0) {
            cbGaDi.setSelectedIndex(0); // Ga đầu tiên (Hà Nội - GA009)
            cbGaDen.setSelectedIndex(dsGa.size() - 1); // Ga cuối (Sài Gòn - GA047)
        }
    }

    // ==================== CHỨC NĂNG TÌM KIẾM ====================
    
    /**
     * Tìm kiếm chuyến tàu theo ga đi, ga đến và ngày đi
     */
    private void timChuyen() {
        if (controller == null) {
            JOptionPane.showMessageDialog(this, "Controller chưa được khởi tạo!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String selectedDi = (String) cbGaDi.getSelectedItem();
        String selectedDen = (String) cbGaDen.getSelectedItem();
        
        if (selectedDi == null || selectedDen == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ga đi và ga đến!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy mã ga từ chuỗi "GA009 - Ga Hà Nội"
        String maGaDi = selectedDi.split(" - ")[0].trim();
        String maGaDen = selectedDen.split(" - ")[0].trim();
        
        // Lấy ngày đi từ JDateChooser
        java.time.LocalDate ngayDi = null;
        if (dateChooser.getDate() != null) {
            ngayDi = dateChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày đi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Gọi controller tìm kiếm
        controller.timKiemChuyen(maGaDi, maGaDen, ngayDi);
    }

    // ==================== HIỂN THỊ DỮ LIỆU ====================
    
    /**
     * Hiển thị danh sách chuyến tàu tìm được
     */
    public void hienThiDanhSachChuyen(List<ChuyenTau> ds) {
        SwingUtilities.invokeLater(() -> {
            listChuyenTemp = ds;
            modelChuyen.setRowCount(0);
            
            if (ds.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy chuyến tàu nào phù hợp!", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            
            for (ChuyenTau c : ds) {
                modelChuyen.addRow(new Object[]{
                    c.getMaChuyen(), 
                    c.getTenTau(),
                    c.getTenGaDi(),
                    c.getTenGaDen(),
                    c.getThoiGianDi().toString().replace("T", " "), 
                    c.getThoiGianDen().toString().replace("T", " ")
                });
            }
            
            // Clear panel toa và ghế
            pnlToa.removeAll(); 
            pnlGhe.removeAll();
            pnlToa.revalidate(); 
            pnlToa.repaint();
            pnlGhe.revalidate(); 
            pnlGhe.repaint();
            resetGioHang();
        });
    }

    /**
     * Hiển thị danh sách toa của chuyến đã chọn
     */
    public void hienThiDanhSachToa(List<ToaTau> ds) {
        SwingUtilities.invokeLater(() -> {
            pnlToa.removeAll();
            
            for (ToaTau t : ds) {
                JButton btn = new JButton(
                    String.format("<html><center><b>%s</b><br><small>%s - %d chỗ</small></center></html>", 
                        t.getTenToa(), t.getLoaiToa(), t.getSucChua())
                );
                btn.setMaximumSize(new Dimension(180, 55));
                btn.setBackground(new Color(240, 245, 255));
                btn.setForeground(new Color(30, 64, 175));
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btn.addActionListener(e -> {
                    currentMaToa = t.getMaToa();
                    if (controller != null && currentChuyen != null) {
                        controller.loadGheByToa(currentMaToa, currentChuyen.getMaChuyen());
                    }
                });
                
                pnlToa.add(Box.createRigidArea(new Dimension(0, 5)));
                pnlToa.add(btn);
            }
            
            pnlToa.add(Box.createVerticalGlue());
            pnlToa.revalidate();
            pnlToa.repaint();
            
            // Clear panel ghế
            pnlGhe.removeAll(); 
            pnlGhe.revalidate(); 
            pnlGhe.repaint();
        });
    }

    /**
     * Hiển thị sơ đồ ghế của toa đã chọn
     */
    public void hienThiSoDoGhe(List<VeTau> dsVe, String maChuyen, String maToa) {
        SwingUtilities.invokeLater(() -> {
            pnlGhe.removeAll();
            
            for (VeTau v : dsVe) {
                String loaiGheHienThi = v.getLoaiGhe() != null ? v.getLoaiGhe() : "Ghế ngồi";
                String giaHienThi = String.format("%,.0fđ", v.getGiaGoc());
                
                JButton btnGhe = new JButton(
                    String.format("<html><center><b>%d</b><br><small>%s<br>%s</small></center></html>", 
                        v.getSoGhe(), loaiGheHienThi, giaHienThi)
                );
                btnGhe.setPreferredSize(new Dimension(95, 75));
                btnGhe.setFont(new Font("Segoe UI", Font.BOLD, 11));
                btnGhe.setFocusPainted(false);
                btnGhe.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                // Kiểm tra ghế có trong giỏ hàng không
                boolean isInCart = gioHang.stream()
                    .anyMatch(ve -> ve.getMaGhe().equals(v.getMaGhe()));

                // Set màu sắc theo trạng thái
                if (v.getTrangThai() != null && v.getTrangThai().equalsIgnoreCase("Đã bán")) {
                    btnGhe.setBackground(new Color(239, 68, 68)); // Đỏ
                    btnGhe.setForeground(Color.WHITE);
                    btnGhe.setEnabled(false);
                    btnGhe.setToolTipText("Đã bán");
                } else if (isInCart) {
                    btnGhe.setBackground(new Color(249, 115, 22)); // Cam
                    btnGhe.setForeground(Color.WHITE);
                    btnGhe.setToolTipText("Đã chọn - Click để bỏ chọn");
                } else {
                    btnGhe.setBackground(new Color(34, 197, 94)); // Xanh lá
                    btnGhe.setForeground(Color.WHITE);
                    btnGhe.setToolTipText("Còn trống - Click để chọn");
                }

                final VeTau veTau = v;
                btnGhe.addActionListener(e -> {
                    // Kiểm tra trạng thái hiện tại của ghế
                    boolean isSelected = gioHang.stream()
                        .anyMatch(ve -> ve.getMaGhe().equals(veTau.getMaGhe()));
                    
                    if (isSelected) {
                        // Bỏ chọn ghế
                        capNhatGioHang(veTau, false);
                        // Cập nhật màu nút
                        SwingUtilities.invokeLater(() -> {
                            btnGhe.setBackground(new Color(34, 197, 94)); // Xanh lá
                            btnGhe.setToolTipText("Còn trống - Click để chọn");
                        });
                    } else {
                        // Thêm vào giỏ hàng
                        capNhatGioHang(veTau, true);
                        // Cập nhật màu nút
                        SwingUtilities.invokeLater(() -> {
                            btnGhe.setBackground(new Color(249, 115, 22)); // Cam
                            btnGhe.setToolTipText("Đã chọn - Click để bỏ chọn");
                        });
                    }
                });
                
                pnlGhe.add(btnGhe);
            }
            
            pnlGhe.revalidate();
            pnlGhe.repaint();
        });
    }

    /**
     * Cập nhật giỏ hàng khi chọn/bỏ chọn ghế
     */
    public void capNhatGioHang(VeTau ve, boolean isAdd) {
        if (isAdd) {
            gioHang.add(ve);
        } else {
            gioHang.removeIf(v -> v.getMaGhe().equals(ve.getMaGhe()));
        }
        
        // Cập nhật hiển thị giỏ hàng
        StringBuilder sb = new StringBuilder();
        double total = 0;
        sb.append(String.format("%-8s %-12s %-15s %-15s\n", "Số Ghế", "Loại Ghế", "Giá (VND)", "Loại Vé"));
        sb.append("─".repeat(55)).append("\n");
        
        for (int i = 0; i < gioHang.size(); i++) {
            VeTau v = gioHang.get(i);
            sb.append(String.format("%-8d %-12s %,-15.0f %-15s\n", 
                v.getSoGhe(), 
                v.getLoaiGhe() != null ? v.getLoaiGhe() : "",
                v.getGiaGoc(),
                ""));
            total += v.getGiaGoc();
        }
        
        sb.append("─".repeat(55)).append("\n");
        sb.append(String.format("Tổng cộng: %d vé, Thành tiền: %,.0f VND\n", gioHang.size(), total));
        
        txtCart.setText(sb.toString());
        lblTotal.setText(String.format("Tổng tiền: %,.0f VND", total));
    }

    /**
     * Reset giỏ hàng
     */
    public void resetGioHang() {
        gioHang.clear();
        txtCart.setText("");
        lblTotal.setText("Tổng tiền: 0 VND");
    }

    /**
     * Reload sơ đồ ghế
     */
    public void reloadGhe() {
        if (controller != null && currentChuyen != null && currentMaToa != null) {
            controller.loadGheByToa(currentMaToa, currentChuyen.getMaChuyen());
        }
    }

    // ==================== CHỨC NĂNG THANH TOÁN ====================
    
    /**
     * Form thanh toán với thông tin hành khách
     */
    private void thanhToanVoiThongTinHanhKhach() {
        if (gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (currentChuyen == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chuyến tàu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Tạo dialog thanh toán
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), 
            "Thông tin đặt vé & Thanh toán", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(550, 550);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // === Panel thông tin khách hàng ===
        JPanel pnlKhachHang = new JPanel(new GridLayout(0, 2, 10, 10));
        pnlKhachHang.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        
        JTextField txtCCCD = new JTextField();
        JTextField txtTenKH = new JTextField();
        JTextField txtSDT = new JTextField();
        JTextField txtEmail = new JTextField();
        JLabel lblMaKH = new JLabel("(Tự động tạo mới)");
        
        JButton btnTimKH = new JButton("🔍 Tìm KH");
        btnTimKH.setBackground(new Color(59, 130, 246));
        btnTimKH.setForeground(Color.WHITE);
        
        pnlKhachHang.add(new JLabel("CCCD (*):"));
        JPanel pnlCCCD = new JPanel(new BorderLayout(5, 0));
        pnlCCCD.add(txtCCCD, BorderLayout.CENTER);
        pnlCCCD.add(btnTimKH, BorderLayout.EAST);
        pnlKhachHang.add(pnlCCCD);
        pnlKhachHang.add(new JLabel("Họ tên (*):"));
        pnlKhachHang.add(txtTenKH);
        pnlKhachHang.add(new JLabel("Số điện thoại:"));
        pnlKhachHang.add(txtSDT);
        pnlKhachHang.add(new JLabel("Email:"));
        pnlKhachHang.add(txtEmail);
        pnlKhachHang.add(new JLabel("Mã KH:"));
        pnlKhachHang.add(lblMaKH);
        
        // === Panel thông tin hành khách cho từng vé ===
        JPanel pnlHanhKhach = new JPanel();
        pnlHanhKhach.setLayout(new BoxLayout(pnlHanhKhach, BoxLayout.Y_AXIS));
        pnlHanhKhach.setBorder(BorderFactory.createTitledBorder("Thông tin hành khách cho từng vé"));
        
        List<JTextField> listTenHK = new ArrayList<>();
        List<JTextField> listCCCDHK = new ArrayList<>();
        List<JComboBox<String>> listLoaiVe = new ArrayList<>();
        
        String[] loaiVeOptions = {"Người lớn", "Trẻ em", "Sinh viên", "Người cao tuổi"};
        
        for (int i = 0; i < gioHang.size(); i++) {
            VeTau ve = gioHang.get(i);
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row.setBorder(BorderFactory.createEtchedBorder());
            
            row.add(new JLabel("Ghế " + ve.getSoGhe() + " (" + ve.getLoaiGhe() + "):"));
            
            JTextField txtTen = new JTextField(12);
            listTenHK.add(txtTen);
            row.add(new JLabel("Tên:"));
            row.add(txtTen);
            
            JTextField txtCCCDHK = new JTextField(10);
            listCCCDHK.add(txtCCCDHK);
            row.add(new JLabel("CCCD:"));
            row.add(txtCCCDHK);
            
            JComboBox<String> cbLoai = new JComboBox<>(loaiVeOptions);
            listLoaiVe.add(cbLoai);
            row.add(new JLabel("Loại vé:"));
            row.add(cbLoai);
            
            pnlHanhKhach.add(row);
        }
        
        // === Panel phương thức thanh toán ===
        JPanel pnlThanhToan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlThanhToan.setBorder(BorderFactory.createTitledBorder("Phương thức thanh toán"));
        String[] ptOptions = {"Tiền mặt", "Chuyển khoản", "Thẻ"};
        JComboBox<String> cbPT = new JComboBox<>(ptOptions);
        pnlThanhToan.add(new JLabel("Phương thức:"));
        pnlThanhToan.add(cbPT);
        
        // === Tổng tiền ===
        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblTongTienDialog = new JLabel(String.format("Tổng thanh toán: %,.0f VND", 
            gioHang.stream().mapToDouble(VeTau::getGiaGoc).sum()));
        lblTongTienDialog.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTienDialog.setForeground(Color.RED);
        pnlTongTien.add(lblTongTienDialog);
        
        // Thêm vào mainPanel
        mainPanel.add(pnlKhachHang);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(pnlHanhKhach);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(pnlThanhToan);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(pnlTongTien);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // === Buttons ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnXacNhan = new JButton("✅ Xác nhận đặt vé");
        btnXacNhan.setBackground(new Color(34, 197, 94));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JButton btnHuy = new JButton("❌ Hủy");
        btnHuy.setBackground(Color.GRAY);
        btnHuy.setForeground(Color.WHITE);
        
        btnPanel.add(btnHuy);
        btnPanel.add(btnXacNhan);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        // === Xử lý sự kiện ===
        
        // Tìm khách hàng theo CCCD
        btnTimKH.addActionListener(e -> {
            String cccd = txtCCCD.getText().trim();
            if (cccd.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập CCCD để tìm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (controller != null) {
                KhachHang kh = controller.timKhachHangTheoCCCD(cccd);
                if (kh != null) {
                    txtTenKH.setText(kh.getTenKH());
                    txtSDT.setText(kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "");
                    txtEmail.setText(kh.getEmail() != null ? kh.getEmail() : "");
                    lblMaKH.setText(kh.getMaKH());
                    lblMaKH.setForeground(new Color(34, 197, 94));
                    JOptionPane.showMessageDialog(dialog, "Đã tìm thấy khách hàng: " + kh.getTenKH());
                } else {
                    txtTenKH.setText("");
                    txtSDT.setText("");
                    txtEmail.setText("");
                    lblMaKH.setText("(Khách hàng mới)");
                    lblMaKH.setForeground(Color.ORANGE);
                    JOptionPane.showMessageDialog(dialog, "Không tìm thấy khách hàng. Sẽ tạo mới khi đặt vé.");
                }
            }
        });
        
        // Xác nhận đặt vé
        btnXacNhan.addActionListener(e -> {
            String cccd = txtCCCD.getText().trim();
            String tenKH = txtTenKH.getText().trim();
            String sdt = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();
            
            // Validate
            if (cccd.isEmpty() || tenKH.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập CCCD và Họ tên khách hàng!", 
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Kiểm tra thông tin hành khách cho từng vé
            for (int i = 0; i < gioHang.size(); i++) {
                String tenHK = listTenHK.get(i).getText().trim();
                String cccdHK = listCCCDHK.get(i).getText().trim();
                
                if (tenHK.isEmpty() || cccdHK.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Vui lòng nhập đầy đủ Tên và CCCD cho ghế " + gioHang.get(i).getSoGhe() + "!", 
                        "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Cập nhật thông tin hành khách vào vé
                VeTau ve = gioHang.get(i);
                ve.setTenHanhKhach(tenHK);
                ve.setSoCCCD(cccdHK);
                ve.setLoaiVe((String) listLoaiVe.get(i).getSelectedItem());
            }
            
            // Xác định mã khách hàng
            String maKH = lblMaKH.getText();
            if (maKH.equals("(Tự động tạo mới)") || maKH.equals("(Khách hàng mới)")) {
                if (controller != null) {
                    maKH = controller.taoKhachHangMoi(tenKH, cccd, sdt, email);
                    if (maKH == null) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi tạo khách hàng mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            
            // Lấy phương thức thanh toán
            String pt = (String) cbPT.getSelectedItem();
            
            // Gọi controller thanh toán
            if (controller != null) {
                controller.thanhToan(gioHang, maKH, "1", pt); // Mã NV mặc định là "1"
            }
            
            dialog.dispose();
        });
        
        // Hủy
        btnHuy.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }

    // ==================== UTILITY ====================
    
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }
}