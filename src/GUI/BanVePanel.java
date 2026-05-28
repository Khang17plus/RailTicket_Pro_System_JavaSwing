// ======================== BanVePanel.java (Đã sửa lỗi sơ đồ ghế & đồng bộ) ========================
package GUI;

import Controller.BanVeController;
import Entity.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
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

    public BanVePanel() { initUI(); }

    public void setController(BanVeController controller) {
        this.controller = controller;
        if (this.controller != null) this.controller.loadDanhSachGa();
    }

    private void initUI() {
        setLayout(new BorderLayout()); setBackground(Color.WHITE);
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        JLabel lblTitle = new JLabel("BÁN VÉ TÀU HỎA");
        lblTitle.setFont(new Font("Segoe UI",Font.BOLD,24));
        lblTitle.setForeground(new Color(30,64,175));
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT,15,10));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createTitledBorder("Tìm kiếm chuyến tàu"));
        cbGaDi = new JComboBox<>(); cbGaDen = new JComboBox<>();
        cbGaDi.setPreferredSize(new Dimension(250,30)); cbGaDen.setPreferredSize(new Dimension(250,30));
        dateChooser = new JDateChooser();
        Calendar cal = Calendar.getInstance(); cal.add(Calendar.DAY_OF_MONTH, 1);
        dateChooser.setDate(cal.getTime()); dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(150,30));
        JButton btnTim = createButton("🔍 Tìm chuyến", new Color(59,130,246));
        btnTim.addActionListener(e -> timChuyen());
        pnlSearch.add(new JLabel("Ga đi:")); pnlSearch.add(cbGaDi);
        pnlSearch.add(new JLabel("Ga đến:")); pnlSearch.add(cbGaDen);
        pnlSearch.add(new JLabel("Ngày đi:")); pnlSearch.add(dateChooser); pnlSearch.add(btnTim);

        JPanel pnlCenter = new JPanel(new BorderLayout()); pnlCenter.add(pnlSearch, BorderLayout.NORTH);
        JSplitPane splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); splitPaneMain.setDividerLocation(400);
        modelChuyen = new DefaultTableModel(new String[]{"Mã Chuyến","Tên Tàu","Ga Đi","Ga Đến","Giờ Đi","Giờ Đến"},0){
            @Override public boolean isCellEditable(int r,int c){return false;}};
        tblChuyen = new JTable(modelChuyen); tblChuyen.setRowHeight(35);
        tblChuyen.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting() && tblChuyen.getSelectedRow()!=-1){
                currentChuyen = listChuyenTemp.get(tblChuyen.getSelectedRow());
                if(controller!=null) controller.loadToaByChuyen(currentChuyen);
            }
        });
        JScrollPane scrollChuyen = new JScrollPane(tblChuyen);
        scrollChuyen.setBorder(BorderFactory.createTitledBorder("Danh sách chuyến"));
        splitPaneMain.setLeftComponent(scrollChuyen);

        JSplitPane splitRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); splitRight.setDividerLocation(200);
        pnlToa = new JPanel(); pnlToa.setLayout(new BoxLayout(pnlToa, BoxLayout.Y_AXIS)); pnlToa.setBackground(Color.WHITE);
        JScrollPane scrollToa = new JScrollPane(pnlToa); scrollToa.setBorder(BorderFactory.createTitledBorder("Danh sách Toa"));
        splitRight.setLeftComponent(scrollToa);
        
        // 🔥 SỬA ĐỔI: Chuyển sang GridLayout để sơ đồ nút ghế tự động xuống dòng ngay hàng thẳng lối đẹp mắt
        pnlGhe = new JPanel(new GridLayout(0, 5, 10, 10)); pnlGhe.setBackground(Color.WHITE);
        JScrollPane scrollGhe = new JScrollPane(pnlGhe); scrollGhe.setBorder(BorderFactory.createTitledBorder("Sơ đồ ghế"));
        splitRight.setRightComponent(scrollGhe);
        splitPaneMain.setRightComponent(splitRight);
        pnlCenter.add(splitPaneMain, BorderLayout.CENTER); add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBorder(BorderFactory.createTitledBorder("Giỏ hàng & Thanh toán")); pnlFooter.setPreferredSize(new Dimension(0,180));
        txtCart = new JTextArea(); txtCart.setEditable(false); txtCart.setFont(new Font("Monospaced",Font.PLAIN,13));
        pnlFooter.add(new JScrollPane(txtCart), BorderLayout.CENTER);
        JPanel pnlPay = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,10));
        lblTotal = new JLabel("Tổng tiền: 0 VND"); lblTotal.setFont(new Font("Segoe UI",Font.BOLD,18)); lblTotal.setForeground(Color.RED);
        JButton btnPay = createButton("💳 Thanh toán", new Color(34,197,94));
        btnPay.addActionListener(e -> thanhToanVoiThongTinHanhKhach());
        pnlPay.add(lblTotal); pnlPay.add(btnPay);
        pnlFooter.add(pnlPay, BorderLayout.SOUTH); add(pnlFooter, BorderLayout.SOUTH);
    }

    public void loadGaComboBox(List<GaTau> dsGa) {
        cbGaDi.removeAllItems(); cbGaDen.removeAllItems();
        for(GaTau ga : dsGa){ String item = ga.getMaGa()+" - "+ga.getTenGa(); cbGaDi.addItem(item); cbGaDen.addItem(item); }
        if(!dsGa.isEmpty()){ cbGaDi.setSelectedIndex(0); cbGaDen.setSelectedIndex(dsGa.size()-1); }
    }

    private void timChuyen() {
        if(controller==null) return;
        if(cbGaDi.getSelectedItem() == null || cbGaDen.getSelectedItem() == null) return;
        String maGaDi = ((String)cbGaDi.getSelectedItem()).split(" - ")[0].trim();
        String maGaDen = ((String)cbGaDen.getSelectedItem()).split(" - ")[0].trim();
        java.time.LocalDate ngayDi = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        controller.timKiemChuyen(maGaDi, maGaDen, ngayDi);
    }

    public void hienThiDanhSachChuyen(List<ChuyenTau> ds) {
        SwingUtilities.invokeLater(()->{
            listChuyenTemp = ds; modelChuyen.setRowCount(0);
            for(ChuyenTau c : ds) modelChuyen.addRow(new Object[]{c.getMaChuyen(),c.getTenTau(),c.getTenGaDi(),c.getTenGaDen(),c.getThoiGianDi().toString().replace("T"," "),c.getThoiGianDen().toString().replace("T"," ")});
            pnlToa.removeAll(); pnlGhe.removeAll(); pnlToa.revalidate(); pnlToa.repaint(); pnlGhe.revalidate(); pnlGhe.repaint(); resetGioHang();
        });
    }

    public void hienThiDanhSachToa(List<ToaTau> ds) {
        SwingUtilities.invokeLater(()->{
            pnlToa.removeAll();
            for(ToaTau t : ds){
                JButton btn = new JButton(String.format("<html><center><b>%s</b><br><small>%s - %d chỗ</small></center></html>", t.getTenToa(),t.getLoaiToa(),t.getSucChua()));
                btn.setMaximumSize(new Dimension(180,55)); btn.setBackground(new Color(240,245,255)); btn.setForeground(new Color(30,64,175));
                btn.addActionListener(e -> { currentMaToa = t.getMaToa(); controller.loadGheByToa(currentMaToa, currentChuyen.getMaChuyen()); });
                pnlToa.add(Box.createRigidArea(new Dimension(0,5))); pnlToa.add(btn);
            }
            pnlToa.revalidate(); pnlToa.repaint(); pnlGhe.removeAll(); pnlGhe.revalidate(); pnlGhe.repaint();
        });
    }

    public void hienThiSoDoGhe(List<VeTau> dsVe, String maChuyen, String maToa) {
        SwingUtilities.invokeLater(()->{
            pnlGhe.removeAll();
            for(VeTau v : dsVe){
                JButton btnGhe = new JButton(String.format("<html><center><b>%d</b><br><small>%s<br>%,.0fđ</small></center></html>", v.getSoGhe(), v.getLoaiGhe(), v.getGiaGoc()));
                btnGhe.setPreferredSize(new Dimension(95,75)); btnGhe.setFont(new Font("Segoe UI",Font.BOLD,11));
                boolean inCart = gioHang.stream().anyMatch(ve->ve.getMaGhe().equals(v.getMaGhe()));
                if("Đã bán".equals(v.getTrangThai())){ btnGhe.setBackground(new Color(239,68,68)); btnGhe.setEnabled(false); }
                else if(inCart) btnGhe.setBackground(new Color(249,115,22));
                else btnGhe.setBackground(new Color(34,197,94));
                btnGhe.setForeground(Color.WHITE);
                final VeTau vt = v;
                btnGhe.addActionListener(e -> {
                    boolean selected = gioHang.stream().anyMatch(ve->ve.getMaGhe().equals(vt.getMaGhe()));
                    capNhatGioHang(vt, !selected);
                    // 🔥 SỬA ĐỔI QUAN TRỌNG: Gọi reloadGhe() thông qua Controller để nạp lại danh sách mới từ DB, tránh lỗi tràn tầng lặp vô hạn
                    reloadGhe(); 
                });
                pnlGhe.add(btnGhe);
            }
            pnlGhe.revalidate(); pnlGhe.repaint();
        });
    }

    public void capNhatGioHang(VeTau ve, boolean add){
        if(add) gioHang.add(ve); else gioHang.removeIf(v->v.getMaGhe().equals(ve.getMaGhe()));
        StringBuilder sb = new StringBuilder(); double total = 0;
        sb.append(String.format("%-8s %-12s %-15s %-15s\n","Số Ghế","Loại Ghế","Giá (VND)","Loại Vé"));
        sb.append("─".repeat(55)).append("\n");
        for(VeTau v : gioHang){ sb.append(String.format("%-8d %-12s %,-15.0f %-15s\n", v.getSoGhe(), v.getLoaiGhe(), v.getGiaGoc(),"")); total+=v.getGiaGoc(); }
        sb.append("─".repeat(55)).append("\n").append(String.format("Tổng: %d vé, %,.0f VND\n", gioHang.size(), total));
        txtCart.setText(sb.toString()); lblTotal.setText(String.format("Tổng tiền: %,.0f VND", total));
    }

    public void resetGioHang(){ gioHang.clear(); txtCart.setText(""); lblTotal.setText("Tổng tiền: 0 VND"); }
    public void reloadGhe(){ if(currentChuyen!=null && currentMaToa!=null) controller.loadGheByToa(currentMaToa, currentChuyen.getMaChuyen()); }

    private void thanhToanVoiThongTinHanhKhach(){
        if(gioHang.isEmpty()) return;
        List<KhuyenMai> dsKM = controller.getActiveKhuyenMai();
        List<Thue> dsThue = controller.getActiveThue();

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),"Đặt vé & Thanh toán",Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());

        JPanel main = new JPanel(new GridBagLayout()); main.setBorder(BorderFactory.createEmptyBorder(15,15,15,15)); main.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.fill=GridBagConstraints.HORIZONTAL; gbc.insets=new Insets(5,5,5,5); gbc.gridx=0; gbc.weightx=1.0;

        // Khách hàng
        JTextField txtCCCD=new JTextField(15), txtTen=new JTextField(15), txtSDT=new JTextField(15), txtEmail=new JTextField(15);
        JLabel lblMaKH = new JLabel("(Tự động tạo mới)"); lblMaKH.setForeground(Color.GRAY);
        JButton btnTimKH = new JButton("🔍 Tìm KH"); btnTimKH.setBackground(new Color(59,130,246)); btnTimKH.setForeground(Color.WHITE);
        JPanel pnlKH = new JPanel(new GridBagLayout()); pnlKH.setBorder(createTitleBorder("Thông tin khách hàng")); pnlKH.setBackground(new Color(250,250,255));
        GridBagConstraints gbcKH = new GridBagConstraints(); gbcKH.insets=new Insets(3,3,3,3); gbcKH.fill=GridBagConstraints.HORIZONTAL;
        
        gbcKH.gridx=0; gbcKH.gridy=0; pnlKH.add(new JLabel("CCCD (*):"),gbcKH); 
        gbcKH.gridx=1; 
        JPanel pnlCCCD=new JPanel(new BorderLayout(5,0)); pnlCCCD.setOpaque(false); 
        pnlCCCD.add(txtCCCD,BorderLayout.CENTER); pnlCCCD.add(btnTimKH,BorderLayout.EAST); 
        pnlKH.add(pnlCCCD,gbcKH);
        
        gbcKH.gridx=0;gbcKH.gridy=1; pnlKH.add(new JLabel("Họ tên (*):"),gbcKH); gbcKH.gridx=1; pnlKH.add(txtTen,gbcKH);
        gbcKH.gridx=0;gbcKH.gridy=2; pnlKH.add(new JLabel("SĐT:"),gbcKH); gbcKH.gridx=1; pnlKH.add(txtSDT,gbcKH);
        gbcKH.gridx=0;gbcKH.gridy=3; pnlKH.add(new JLabel("Email:"),gbcKH); gbcKH.gridx=1; pnlKH.add(txtEmail,gbcKH);
        gbcKH.gridx=0;gbcKH.gridy=4; pnlKH.add(new JLabel("Mã KH:"),gbcKH); gbcKH.gridx=1; pnlKH.add(lblMaKH,gbcKH);
        gbc.gridy=0; main.add(pnlKH,gbc);

        // Hành khách vé
        JPanel pnlHanhKhach = new JPanel(); pnlHanhKhach.setLayout(new BoxLayout(pnlHanhKhach,BoxLayout.Y_AXIS));
        pnlHanhKhach.setBorder(createTitleBorder("Thông tin hành khách từng vé")); pnlHanhKhach.setBackground(new Color(250,250,255));
        List<JTextField> listTenHK=new ArrayList<>(), listCCCDHK=new ArrayList<>();
        List<JComboBox<String>> listLoaiVe=new ArrayList<>();
        String[] loaiArr = {"Người lớn","Trẻ em","Sinh viên","Người cao tuổi"};
        for(VeTau ve : gioHang){
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5)); row.setBackground(new Color(245,248,255));
            row.add(new JLabel("Ghế "+ve.getSoGhe()+" ("+ve.getLoaiGhe()+"):"));
            JTextField ten=new JTextField(12); listTenHK.add(ten); row.add(new JLabel("Tên:")); row.add(ten);
            JTextField cccd=new JTextField(10); listCCCDHK.add(cccd); row.add(new JLabel("CCCD:")); row.add(cccd);
            JComboBox<String> loai = new JComboBox<>(loaiArr); listLoaiVe.add(loai); row.add(new JLabel("Loại:")); row.add(loai);
            pnlHanhKhach.add(row);
        }
        gbc.gridy=1; main.add(pnlHanhKhach,gbc);

        // KM & Thuế
        JComboBox<String> cbKM = new JComboBox<>(); cbKM.addItem("Không áp dụng");
        dsKM.forEach(km->cbKM.addItem(km.getMaKM()+" - "+km.getTenKM()+" ("+(km.getLoaiKM().equals("PERCENT")?km.getGiaTri()+"%":String.format("%,.0fđ",km.getGiaTri()))+")"));
        JComboBox<String> cbThue = new JComboBox<>(); cbThue.addItem("Không áp dụng");
        dsThue.forEach(t->cbThue.addItem(t.getMaThue()+" - "+t.getTenThue()+" ("+String.format("%.1f%%",t.getPhanTram())+")"));
        JPanel pnlKMT = new JPanel(new GridBagLayout()); pnlKMT.setBorder(createTitleBorder("Khuyến mãi & Thuế")); pnlKMT.setBackground(new Color(250,250,255));
        GridBagConstraints gbK = new GridBagConstraints(); gbK.insets=new Insets(5,5,5,5); gbK.fill=GridBagConstraints.HORIZONTAL;
        gbK.gridy=0; pnlKMT.add(new JLabel("Khuyến mãi:"),gbK); gbK.gridx=1; pnlKMT.add(cbKM,gbK);
        gbK.gridx=0;gbK.gridy=1; pnlKMT.add(new JLabel("Thuế/VAT:"),gbK); gbK.gridx=1; pnlKMT.add(cbThue,gbK);
        gbc.gridy=2; main.add(pnlKMT,gbc);

        // Tổng tiền
        JLabel lblGoc=new JLabel(), lblGiam=new JLabel(), lblThue=new JLabel(), lblTong=new JLabel();
        lblTong.setFont(new Font("Segoe UI",Font.BOLD,16)); lblTong.setForeground(new Color(200,30,30));
        JPanel pnlTien = new JPanel(new GridLayout(4,1,5,5)); pnlTien.setOpaque(false);
        pnlTien.add(lblGoc); pnlTien.add(lblGiam); pnlTien.add(lblThue); pnlTien.add(lblTong);
        gbc.gridy=3; main.add(pnlTien,gbc);

        Runnable capNhat = ()->{
            double tongGoc = gioHang.stream().mapToDouble(VeTau::getGiaGoc).sum();
            double giam=0, thue=0;
            int idxKM = cbKM.getSelectedIndex(); 
            if(idxKM>0){ KhuyenMai km = dsKM.get(idxKM-1); giam = km.getLoaiKM().equals("FIXED")? tongGoc -km.getGiaTri(): tongGoc*km.getGiaTri()/100; }
            double sauGiam = tongGoc - giam;
            int idxT = cbThue.getSelectedIndex();
            if(idxT>0){ Thue t = dsThue.get(idxT-1); thue = sauGiam * t.getPhanTram()/100; }
            double tongCuoi = sauGiam + thue;
            lblGoc.setText(String.format("Tổng giá gốc: %,.0f VND", tongGoc));
            lblGiam.setText(String.format("Giảm giá: -%,.0f VND", giam));
            lblThue.setText(String.format("Thuế: +%,.0f VND", thue));
            lblTong.setText(String.format("Thành tiền: %,.0f VND", tongCuoi));
        };
        cbKM.addActionListener(e->capNhat.run()); cbThue.addActionListener(e->capNhat.run()); capNhat.run();

        // Phương thức TT
        JPanel pnlPT = new JPanel(new FlowLayout(FlowLayout.LEFT)); pnlPT.setBorder(createTitleBorder("Phương thức thanh toán"));
        String[] ptOpt = {"Tiền mặt","Chuyển khoản","Thẻ"}; JComboBox<String> cbPT = new JComboBox<>(ptOpt);
        pnlPT.add(new JLabel("Phương thức:")); pnlPT.add(cbPT);
        gbc.gridy=4; main.add(pnlPT,gbc);

        JScrollPane scroll = new JScrollPane(main); scroll.setBorder(null); dialog.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10)); btnPanel.setBackground(new Color(240,240,245));
        JButton btnOK = new JButton("✅ Xác nhận đặt vé"); btnOK.setBackground(new Color(34,197,94)); btnOK.setForeground(Color.WHITE);
        JButton btnHuy = new JButton("❌ Hủy"); btnHuy.setBackground(Color.GRAY); btnHuy.setForeground(Color.WHITE);
        btnPanel.add(btnHuy); btnPanel.add(btnOK); dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setSize(680, Math.min(500+gioHang.size()*60, 700)); dialog.setLocationRelativeTo(this); dialog.setResizable(false);

        btnTimKH.addActionListener(e->{
            String cccd = txtCCCD.getText().trim();
            KhachHang kh = controller.timKhachHangTheoCCCD(cccd);
            if(kh!=null){ txtTen.setText(kh.getTenKH()); txtSDT.setText(kh.getSoDienThoai()); txtEmail.setText(kh.getEmail()); lblMaKH.setText(kh.getMaKH()); lblMaKH.setForeground(new Color(34,197,94)); }
            else { txtTen.setText(""); txtSDT.setText(""); txtEmail.setText(""); lblMaKH.setText("(Khách hàng mới)"); lblMaKH.setForeground(Color.ORANGE); }
        });
        
        btnOK.addActionListener(e->{
            String cccd=txtCCCD.getText().trim(), ten=txtTen.getText().trim();
            String maKH=lblMaKH.getText();
            if(maKH.contains("mới")){ maKH=controller.taoKhachHangMoi(ten,cccd,txtSDT.getText().trim(),txtEmail.getText().trim()); }
            
            for(int i=0;i<gioHang.size();i++){ 
                VeTau v=gioHang.get(i); 
                v.setTenHanhKhach(listTenHK.get(i).getText().trim()); 
                v.setSoCCCD(listCCCDHK.get(i).getText().trim()); 
                v.setLoaiVe((String)listLoaiVe.get(i).getSelectedItem()); 
            }
            
            double giam=0,thue=0; String maKM=null, maThue=null;
            int iKM=cbKM.getSelectedIndex(); 
            if(iKM>0){ 
                KhuyenMai km=dsKM.get(iKM-1); maKM=km.getMaKM(); 
                double goc=gioHang.stream().mapToDouble(VeTau::getGiaGoc).sum(); 
                giam=km.getLoaiKM().equals("FIXED")?km.getGiaTri():goc*km.getGiaTri()/100; 
            }
            int iT=cbThue.getSelectedIndex(); 
            if(iT>0){ 
                Thue t=dsThue.get(iT-1); maThue=t.getMaThue(); 
                thue=(gioHang.stream().mapToDouble(VeTau::getGiaGoc).sum()-giam)*t.getPhanTram()/100; 
            }
            
            // 🔥 ĐỒNG BỘ: Đã map chuẩn 8 tham số khớp khít với hàm thanh toán bên BanVeController
            controller.thanhToan(gioHang, maKH, "1", (String)cbPT.getSelectedItem(), maKM, maThue, giam, thue);
            dialog.dispose();
        });
        btnHuy.addActionListener(e->dialog.dispose());
        dialog.setVisible(true);
    }

    private JButton createButton(String text, Color bg){
        JButton b = new JButton(text); b.setFont(new Font("Segoe UI",Font.BOLD,13)); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR)); b.setBorder(BorderFactory.createEmptyBorder(8,15,8,15)); return b;
    }
    private TitledBorder createTitleBorder(String title){
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200,200,200),1,true), title, TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI",Font.BOLD,13), new Color(30,64,175));
    }
}