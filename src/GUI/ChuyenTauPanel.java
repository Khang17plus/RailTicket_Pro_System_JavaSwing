package GUI;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Entity.ChuyenTau;
import Entity.LichTrinhDungDo;
import Controller.ChuyenTauController;

public class ChuyenTauPanel extends JPanel {
    private Component component = new Component();
    private ChuyenTauController controller;
    
    // Bảng Chuyến Tàu (Bên Trái)
    private JTable tableChuyenTau;
    private DefaultTableModel modelChuyenTau;
    
    // Bảng Lịch Trình (Bên Phải)
    private JTable tableLichTrinh;
    private DefaultTableModel modelLichTrinh;
    private JLabel lblTitleLichTrinh; 
    
    // Định dạng thời gian dùng chung
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void setController(ChuyenTauController controller) {
        this.controller = controller;
    }

    public ChuyenTauPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); 

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250)); 
        
        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setBackground(new Color(245, 247, 250));
        
        JLabel title = new JLabel("Quản lý chuyến tàu & Lịch trình");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        JLabel sub = new JLabel("Tổ chức chuyến đi, ga xuất phát, ga đến và các điểm dừng đỗ");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);
        
        JPanel headerR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerR.setBackground(new Color(245, 247, 250));
        headerR.add(createCardstatistical("img/user2.png", "Tổng Chuyến Tàu", 42));
        headerR.add(createCardstatistical("img/user2.png", "Chuyến Hôm Nay", 8));
        headerR.add(createCardstatistical("img/user2.png", "Điểm Dừng Đỗ", 156));
        
        header.add(headerL, BorderLayout.WEST);
        header.add(headerR, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ================= MAIN BỤC CHỨA 2 BẢNG =================
        JPanel main = new JPanel(new GridLayout(1, 2, 15, 0)); 
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // ----- CARD TRÁI: DANH SÁCH CHUYẾN TÀU -----
        JPanel pnlChuyenTau = createTableCard(
            "Danh Sách Chuyến Tàu", 
            new String[]{"Mã Chuyến", "Mã Tàu", "Ga Đi", "Ga Đến", "Thời Gian Đi", "Trạng Thái"}
        );
        tableChuyenTau = (JTable) ((JScrollPane) pnlChuyenTau.getClientProperty("scrollPane")).getViewport().getView();
        modelChuyenTau = (DefaultTableModel) tableChuyenTau.getModel();

        JPanel actionChuyen = createActionPanel(true);
        pnlChuyenTau.add(actionChuyen, BorderLayout.NORTH);

        // ----- CARD PHẢI: LỊCH TRÌNH DỪNG ĐỖ -----
        lblTitleLichTrinh = new JLabel("Danh Sách Chặng Tàu (Chọn 1 chuyến để xem)");
        lblTitleLichTrinh.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitleLichTrinh.setForeground(new Color(59, 130, 246));

        JPanel pnlLichTrinh = createTableCard("", new String[]{"Thứ Tự", "Ga Dừng", "Thời Gian Đến", "Thời Gian Đi"});
        tableLichTrinh = (JTable) ((JScrollPane) pnlLichTrinh.getClientProperty("scrollPane")).getViewport().getView();
        modelLichTrinh = (DefaultTableModel) tableLichTrinh.getModel();

        JPanel pnlRightHeader = new JPanel(new BorderLayout());
        pnlRightHeader.setBackground(Color.WHITE);
        pnlRightHeader.add(lblTitleLichTrinh, BorderLayout.WEST);
        pnlRightHeader.add(createActionPanel(false), BorderLayout.EAST); 

        pnlLichTrinh.add(pnlRightHeader, BorderLayout.NORTH);

        main.add(pnlChuyenTau);
        main.add(pnlLichTrinh);
        add(main, BorderLayout.CENTER);

        // ================= SỰ KIỆN CLICK BẢNG CHUYẾN TÀU =================
        tableChuyenTau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableChuyenTau.getSelectedRow();
                if (row != -1) {
                    String maChuyen = modelChuyenTau.getValueAt(row, 0).toString();
                    lblTitleLichTrinh.setText("Danh Sách Chặng Tàu cho Chuyến: " + maChuyen);
                    
                    if (controller != null) {
                        controller.loadLichTrinhByMaChuyen(maChuyen);
                    }
                }
            }
        });
    }

    // ================= HÀM TIỆN ÍCH TẠO BỘ CHỌN THỜI GIAN JCOMBOBOX =================
    private JPanel createTimePickerPanel(JComboBox<String> cbxNgay, JComboBox<String> cbxGio, JComboBox<String> cbxPhut) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);

        LocalDate today = LocalDate.now();
        for (int i = -5; i < 30; i++) { 
            cbxNgay.addItem(today.plusDays(i).toString());
        }
        cbxNgay.setSelectedItem(today.toString()); 

        for (int i = 0; i < 24; i++) {
            cbxGio.addItem(String.format("%02d", i));
        }

        for (int i = 0; i < 60; i++) {
            cbxPhut.addItem(String.format("%02d", i));
        }

        cbxNgay.setPreferredSize(new Dimension(130, 38));
        cbxGio.setPreferredSize(new Dimension(60, 38));
        cbxPhut.setPreferredSize(new Dimension(60, 38));

        panel.add(cbxNgay);
        panel.add(new JLabel(" "));
        panel.add(cbxGio);
        panel.add(new JLabel(":"));
        panel.add(cbxPhut);

        return panel;
    }

    // ================= CÁC HÀM ĐỔ DỮ LIỆU TỪ CONTROLLER =================
    public void setChuyenTauData(List<ChuyenTau> list) {
        modelChuyenTau.setRowCount(0);
        for(ChuyenTau ct : list) {
            String tgDi = ct.getThoiGianDi() != null ? ct.getThoiGianDi().format(formatter) : "";
            modelChuyenTau.addRow(new Object[] {
                ct.getMaChuyen(), ct.getMaTau(), ct.getMaGaDi(), ct.getMaGaDen(), tgDi, ct.getTrangThai()
            });
        }
    }

    public void setLichTrinhData(List<LichTrinhDungDo> list) {
        modelLichTrinh.setRowCount(0);
        for(LichTrinhDungDo lt : list) {
            String tgDen = lt.getThoiGianDen() != null ? lt.getThoiGianDen().format(formatter) : "";
            String tgDi = lt.getThoiGianDi() != null ? lt.getThoiGianDi().format(formatter) : "";
            modelLichTrinh.addRow(new Object[] {
                lt.getThuTuDung(), lt.getMaGa(), tgDen, tgDi
            });
        }
    }

    // ================= GIAO DIỆN COMPONENT DÙNG CHUNG =================
    public JButton createCustomButton(String text, String type) {
        JButton btn = new JButton(text);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 12;";
        switch (type) {
            case "blue": btn.setBackground(new Color(59, 130, 246)); btn.setForeground(Color.WHITE); break;
            case "green": btn.setBackground(new Color(34, 197, 94)); btn.setForeground(Color.WHITE); break;
            case "red": btn.setBackground(new Color(239, 68, 68)); btn.setForeground(Color.WHITE); break;
        }
        btn.setPreferredSize(new Dimension(80, 32));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", style + "margin:5,10,5,10");
        return btn;
    }

    public JPanel createCardstatistical(String IconURL, String title, int value) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        ImageIcon icon = new ImageIcon(IconURL);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        card.add(new JLabel(new ImageIcon(img)), BorderLayout.WEST);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);
        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);
        card.add(textPanel, BorderLayout.CENTER);
        card.putClientProperty("FlatLaf.style", "arc:10; border:10,10,10,10; background:#FFFFFF");
        return card;
    }

    private JPanel createTableCard(String title, String[] columns) {
        JPanel tableCard = new JPanel(new BorderLayout(0, 10));
        tableCard.setBackground(Color.WHITE);
        tableCard.putClientProperty("FlatLaf.style", "arc:20; border:12,12,12,12; background:#FFFFFF");
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(235, 235, 235));
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(232, 240, 254));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tableCard.add(scrollPane, BorderLayout.CENTER);
        tableCard.putClientProperty("scrollPane", scrollPane);
        return tableCard;
    }

    private JPanel createActionPanel(boolean isChuyenTau) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(Color.WHITE);
        
        JButton btnAdd = createCustomButton("Thêm", "green");
        JButton btnEdit = createCustomButton("Sửa", "blue");
        JButton btnDel = createCustomButton("Xóa", "red");
        
        btnAdd.addActionListener(e -> { if (isChuyenTau) formThemChuyenTau(); else formThemLichTrinh(); });
        btnEdit.addActionListener(e -> { if (isChuyenTau) formSuaChuyenTau(); else formSuaLichTrinh(); });
        btnDel.addActionListener(e -> { if (isChuyenTau) xoaChuyenTau(); else xoaLichTrinh(); });

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDel);
        return panel;
    }

    // ================= LOGIC XỬ LÝ CHUYẾN TÀU =================
    private void formThemChuyenTau() {
        String[] labels = {"Mã Chuyến", "Mã Tàu", "Ga Đi", "Ga Đến", "Thời Gian Đi", "Thời Gian Đến", "Trạng Thái"};
        JComponent[] fields = new JComponent[labels.length];
        
        String maMoi = "CH01";
        if (controller != null) {
            maMoi = controller.layMaChuyenMoi();
        }
        JTextField txtMaChuyen = new JTextField(maMoi);
        txtMaChuyen.setEditable(false); 
        txtMaChuyen.setFocusable(false);
        fields[0] = txtMaChuyen;
        
        JComboBox<String> cbxTau = new JComboBox<>();
        if (controller != null) {
            List<String> dsTauTuDB = controller.layDanhSachTauFormat();
            for (String t : dsTauTuDB) cbxTau.addItem(t);
        }
        fields[1] = cbxTau;
        
        JComboBox<String> cbxGaDi = new JComboBox<>();
        JComboBox<String> cbxGaDen = new JComboBox<>();
        if (controller != null) {
            List<String> dsGaTuDB = controller.layDanhSachGaFormat();
            for (String ga : dsGaTuDB) {
                cbxGaDi.addItem(ga);
                cbxGaDen.addItem(ga);
            }
        }
        fields[2] = cbxGaDi;
        fields[3] = cbxGaDen;
        
        JComboBox<String> cbxNgayDi = new JComboBox<>();
        JComboBox<String> cbxGioDi = new JComboBox<>();
        JComboBox<String> cbxPhutDi = new JComboBox<>();
        fields[4] = createTimePickerPanel(cbxNgayDi, cbxGioDi, cbxPhutDi);
        
        JComboBox<String> cbxNgayDen = new JComboBox<>();
        JComboBox<String> cbxGioDen = new JComboBox<>();
        JComboBox<String> cbxPhutDen = new JComboBox<>();
        fields[5] = createTimePickerPanel(cbxNgayDen, cbxGioDen, cbxPhutDen);
        
        fields[6] = new JTextField("Sẵn sàng");

        JButton btnCancel = new JButton("Hủy");
        JButton btnSave = new JButton("Lưu");

        JDialog dialog = component.createDinamicForm("Thêm Chuyến Tàu", "Thông tin tuyến", "Nhập lịch trình chuyến mới", labels, fields, new JButton[]{btnCancel, btnSave});

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                String maTau = cbxTau.getSelectedItem() != null ? cbxTau.getSelectedItem().toString().split(" - ")[0] : "";
                String maGaDi = cbxGaDi.getSelectedItem() != null ? cbxGaDi.getSelectedItem().toString().split(" - ")[0] : "";
                String maGaDen = cbxGaDen.getSelectedItem() != null ? cbxGaDen.getSelectedItem().toString().split(" - ")[0] : "";
                
                if(maGaDi.equals(maGaDen)) {
                    JOptionPane.showMessageDialog(dialog, "Ga đi và Ga đến không được trùng nhau!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String strThoiGianDi = cbxNgayDi.getSelectedItem() + " " + cbxGioDi.getSelectedItem() + ":" + cbxPhutDi.getSelectedItem();
                String strThoiGianDen = cbxNgayDen.getSelectedItem() + " " + cbxGioDen.getSelectedItem() + ":" + cbxPhutDen.getSelectedItem();

                ChuyenTau ct = new ChuyenTau();
                ct.setMaChuyen(((JTextField)fields[0]).getText().trim());
                ct.setMaTau(maTau);
                ct.setMaGaDi(maGaDi);
                ct.setMaGaDen(maGaDen);
                
                ct.setThoiGianDi(LocalDateTime.parse(strThoiGianDi, formatter));
                ct.setThoiGianDen(LocalDateTime.parse(strThoiGianDen, formatter));
                ct.setTrangThai(((JTextField)fields[6]).getText().trim());

                if (controller != null && controller.themChuyenTau(ct)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
                    controller.loadDanhSachChuyenTau();
                    dialog.dispose();
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi định dạng thời gian!");
            }
        });
        dialog.setVisible(true);
    }

    private void formSuaChuyenTau() {
        int row = tableChuyenTau.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Chọn 1 chuyến tàu để sửa!"); return; }

        String maChuyen = modelChuyenTau.getValueAt(row, 0).toString();
        String[] labels = {"Mã Tàu", "Ga Đi", "Ga Đến", "Thời Gian Đi", "Thời Gian Đến", "Trạng Thái"};
        JComponent[] fields = new JComponent[labels.length];
        
        JComboBox<String> cbxTau = new JComboBox<>();
        String oldMaTau = modelChuyenTau.getValueAt(row, 1).toString();
        if (controller != null) {
            List<String> dsTauTuDB = controller.layDanhSachTauFormat();
            for (String t : dsTauTuDB) {
                cbxTau.addItem(t);
                if (t.startsWith(oldMaTau)) cbxTau.setSelectedItem(t);
            }
        }
        fields[0] = cbxTau;
        
        JComboBox<String> cbxGaDi = new JComboBox<>();
        JComboBox<String> cbxGaDen = new JComboBox<>();
        String oldGaDi = modelChuyenTau.getValueAt(row, 2).toString();
        String oldGaDen = modelChuyenTau.getValueAt(row, 3).toString();
        
        if (controller != null) {
            List<String> dsGaTuDB = controller.layDanhSachGaFormat();
            for (String ga : dsGaTuDB) {
                cbxGaDi.addItem(ga);
                cbxGaDen.addItem(ga);
                if (ga.startsWith(oldGaDi)) cbxGaDi.setSelectedItem(ga);
                if (ga.startsWith(oldGaDen)) cbxGaDen.setSelectedItem(ga);
            }
        }
        fields[1] = cbxGaDi;
        fields[2] = cbxGaDen;
        
        JComboBox<String> cbxNgayDi = new JComboBox<>();
        JComboBox<String> cbxGioDi = new JComboBox<>();
        JComboBox<String> cbxPhutDi = new JComboBox<>();
        fields[3] = createTimePickerPanel(cbxNgayDi, cbxGioDi, cbxPhutDi);
        
        JComboBox<String> cbxNgayDen = new JComboBox<>();
        JComboBox<String> cbxGioDen = new JComboBox<>();
        JComboBox<String> cbxPhutDen = new JComboBox<>();
        fields[4] = createTimePickerPanel(cbxNgayDen, cbxGioDen, cbxPhutDen);
        
        String oldTgDiStr = modelChuyenTau.getValueAt(row, 4).toString();
        if (!oldTgDiStr.isEmpty() && oldTgDiStr.contains(" ")) {
            String[] parts = oldTgDiStr.split(" ");
            cbxNgayDi.setSelectedItem(parts[0]);
            if (parts[1].contains(":")) {
                String[] timeParts = parts[1].split(":");
                cbxGioDi.setSelectedItem(timeParts[0]);
                cbxPhutDi.setSelectedItem(timeParts[1]);
            }
        }

        fields[5] = new JTextField(modelChuyenTau.getValueAt(row, 5).toString());

        JButton btnCancel = new JButton("Hủy");
        JButton btnSave = new JButton("Cập Nhật");

        JDialog dialog = component.createDinamicForm("Sửa Chuyến Tàu", "Mã chuyến: " + maChuyen, "Cập nhật thông tin", labels, fields, new JButton[]{btnCancel, btnSave});

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                String maTau = cbxTau.getSelectedItem() != null ? cbxTau.getSelectedItem().toString().split(" - ")[0] : "";
                String maGaDi = cbxGaDi.getSelectedItem() != null ? cbxGaDi.getSelectedItem().toString().split(" - ")[0] : "";
                String maGaDen = cbxGaDen.getSelectedItem() != null ? cbxGaDen.getSelectedItem().toString().split(" - ")[0] : "";
                
                if(maGaDi.equals(maGaDen)) {
                    JOptionPane.showMessageDialog(dialog, "Ga đi và Ga đến không được trùng nhau!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String strThoiGianDi = cbxNgayDi.getSelectedItem() + " " + cbxGioDi.getSelectedItem() + ":" + cbxPhutDi.getSelectedItem();
                String strThoiGianDen = cbxNgayDen.getSelectedItem() + " " + cbxGioDen.getSelectedItem() + ":" + cbxPhutDen.getSelectedItem();

                ChuyenTau ct = new ChuyenTau();
                ct.setMaChuyen(maChuyen); 
                ct.setMaTau(maTau);
                ct.setMaGaDi(maGaDi);
                ct.setMaGaDen(maGaDen);
                
                ct.setThoiGianDi(LocalDateTime.parse(strThoiGianDi, formatter));
                ct.setThoiGianDen(LocalDateTime.parse(strThoiGianDen, formatter));
                ct.setTrangThai(((JTextField)fields[5]).getText().trim());

                if (controller != null && controller.capNhatChuyenTau(ct)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    controller.loadDanhSachChuyenTau();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi cập nhật thời gian!");
            }
        });
        dialog.setVisible(true);
    }

    private void xoaChuyenTau() {
        int row = tableChuyenTau.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Chọn 1 chuyến tàu để xóa!"); return; }
        String maChuyen = modelChuyenTau.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa chuyến tàu " + maChuyen + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller != null && controller.xoaChuyenTau(maChuyen)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                controller.loadDanhSachChuyenTau();
                modelLichTrinh.setRowCount(0);
                lblTitleLichTrinh.setText("Danh Sách Chặng Tàu (Chọn 1 chuyến để xem)");
            }
        }
    }

    // ================= LOGIC XỬ LÝ LỊCH TRÌNH DỪNG ĐỖ (ĐÃ ĐỒNG BỘ THÔNG MINH) =================
    private void formThemLichTrinh() {
        int rowCt = tableChuyenTau.getSelectedRow();
        if (rowCt == -1) { JOptionPane.showMessageDialog(this, "Chọn một Chuyến Tàu ở bảng bên trái trước!"); return; }
        String maChuyen = modelChuyenTau.getValueAt(rowCt, 0).toString();

        String[] labels = {"Ga Dừng", "Thứ Tự Dừng", "Thời Gian Đến", "Thời Gian Đi"};
        JComponent[] fields = new JComponent[labels.length];
        
        JComboBox<String> cbxGaDung = new JComboBox<>();
        if (controller != null) {
            List<String> dsGaTuDB = controller.layDanhSachGaFormat();
            for (String ga : dsGaTuDB) cbxGaDung.addItem(ga);
        }
        fields[0] = cbxGaDung;
        fields[1] = new JTextField();
        
        // 🌟 NÂNG CẤP: Trạm dừng trung gian luôn bắt buộc nhập đủ cả 2 mốc thời gian
        JComboBox<String> cbxNgayDen = new JComboBox<>();
        JComboBox<String> cbxGioDen = new JComboBox<>();
        JComboBox<String> cbxPhutDen = new JComboBox<>();
        fields[2] = createTimePickerPanel(cbxNgayDen, cbxGioDen, cbxPhutDen);
        
        JComboBox<String> cbxNgayDi = new JComboBox<>();
        JComboBox<String> cbxGioDi = new JComboBox<>();
        JComboBox<String> cbxPhutDi = new JComboBox<>();
        fields[3] = createTimePickerPanel(cbxNgayDi, cbxGioDi, cbxPhutDi);

        JButton btnCancel = new JButton("Hủy");
        JButton btnSave = new JButton("Lưu");

        JDialog dialog = component.createDinamicForm("Thêm Chặng Dừng", "Chuyến: " + maChuyen, "Nhập thông tin chặng dừng mới", labels, fields, new JButton[]{btnCancel, btnSave});

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                String maGaDung = cbxGaDung.getSelectedItem() != null ? cbxGaDung.getSelectedItem().toString().split(" - ")[0] : "";
                
                LichTrinhDungDo lt = new LichTrinhDungDo();
                lt.setMaChuyen(maChuyen);
                lt.setMaGa(maGaDung);
                lt.setThuTuDung(Integer.parseInt(((JTextField)fields[1]).getText().trim()));
                
                // Mặc định ép đọc và parse đầy đủ thời gian
                String strDen = cbxNgayDen.getSelectedItem() + " " + cbxGioDen.getSelectedItem() + ":" + cbxPhutDen.getSelectedItem();
                lt.setThoiGianDen(LocalDateTime.parse(strDen, formatter));

                String strDi = cbxNgayDi.getSelectedItem() + " " + cbxGioDi.getSelectedItem() + ":" + cbxPhutDi.getSelectedItem();
                lt.setThoiGianDi(LocalDateTime.parse(strDi, formatter));

                if (controller != null && controller.themLichTrinh(lt)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm chặng thành công!");
                    controller.loadLichTrinhByMaChuyen(maChuyen);
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi dữ liệu! Vui lòng điền đầy đủ và đúng định dạng mốc thời gian trung gian.");
            }
        });
        dialog.setVisible(true);
    }

    private void formSuaLichTrinh() {
        int rowLt = tableLichTrinh.getSelectedRow();
        int rowCt = tableChuyenTau.getSelectedRow();
        if (rowLt == -1 || rowCt == -1) { JOptionPane.showMessageDialog(this, "Chọn 1 chặng dừng để sửa!"); return; }

        String maChuyen = modelChuyenTau.getValueAt(rowCt, 0).toString();
        String maGaCu = modelLichTrinh.getValueAt(rowLt, 1).toString(); 
        
        String[] labels = {"Ga Dừng", "Thứ Tự Dừng", "Thời Gian Đến", "Thời Gian Đi"};
        JComponent[] fields = new JComponent[labels.length];
        
        JComboBox<String> cbxGaDung = new JComboBox<>();
        if (controller != null) {
            List<String> dsGaTuDB = controller.layDanhSachGaFormat();
            for (String ga : dsGaTuDB) {
                cbxGaDung.addItem(ga);
                if (ga.startsWith(maGaCu)) cbxGaDung.setSelectedItem(ga);
            }
        }
        fields[0] = cbxGaDung;
        fields[1] = new JTextField(modelLichTrinh.getValueAt(rowLt, 0).toString());
        
        JComboBox<String> cbxNgayDen = new JComboBox<>();
        JComboBox<String> cbxGioDen = new JComboBox<>();
        JComboBox<String> cbxPhutDen = new JComboBox<>();
        fields[2] = createTimePickerPanel(cbxNgayDen, cbxGioDen, cbxPhutDen);
        
        JComboBox<String> cbxNgayDi = new JComboBox<>();
        JComboBox<String> cbxGioDi = new JComboBox<>();
        JComboBox<String> cbxPhutDi = new JComboBox<>();
        fields[3] = createTimePickerPanel(cbxNgayDi, cbxGioDi, cbxPhutDi);
        
        // 🌟 NÂNG CẤP THÔNG MINH: Nhận diện tự động chặn để khóa cứng đầu/cuối hành trình
        boolean isGaDau = (rowLt == 0);
        boolean isGaCuoi = (rowLt == tableLichTrinh.getRowCount() - 1);

        if (isGaDau) {
            // Ga Đầu hành trình $\rightarrow$ Khóa cứng thời gian Đến
            cbxNgayDen.setEnabled(false); cbxGioDen.setEnabled(false); cbxPhutDen.setEnabled(false);
        } else {
            String oldTgDenStr = modelLichTrinh.getValueAt(rowLt, 2).toString();
            if (!oldTgDenStr.isEmpty() && oldTgDenStr.contains(" ")) {
                String[] parts = oldTgDenStr.split(" ");
                cbxNgayDen.setSelectedItem(parts[0]);
                if (parts[1].contains(":")) {
                    String[] timeParts = parts[1].split(":");
                    cbxGioDen.setSelectedItem(timeParts[0]);
                    cbxPhutDen.setSelectedItem(timeParts[1]);
                }
            }
        }
        
        if (isGaCuoi) {
            // Ga Cuối hành trình $\rightarrow$ Khóa cứng thời gian Đi
            cbxNgayDi.setEnabled(false); cbxGioDi.setEnabled(false); cbxPhutDi.setEnabled(false);
        } else {
            String oldTgDiStr = modelLichTrinh.getValueAt(rowLt, 3).toString();
            if (!oldTgDiStr.isEmpty() && oldTgDiStr.contains(" ")) {
                String[] parts = oldTgDiStr.split(" ");
                cbxNgayDi.setSelectedItem(parts[0]);
                if (parts[1].contains(":")) {
                    String[] timeParts = parts[1].split(":");
                    cbxGioDi.setSelectedItem(timeParts[0]);
                    cbxPhutDi.setSelectedItem(timeParts[1]);
                }
            }
        }

        JButton btnCancel = new JButton("Hủy");
        JButton btnSave = new JButton("Cập Nhật");

        JDialog dialog = component.createDinamicForm("Sửa Chặng Dừng", "Ga gốc: " + maGaCu, "Chuyến: " + maChuyen, labels, fields, new JButton[]{btnCancel, btnSave});

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                String maGaMoi = cbxGaDung.getSelectedItem() != null ? cbxGaDung.getSelectedItem().toString().split(" - ")[0] : "";
                LichTrinhDungDo lt = new LichTrinhDungDo();
                lt.setMaChuyen(maChuyen);
                lt.setMaGa(maGaMoi);
                lt.setThuTuDung(Integer.parseInt(((JTextField)fields[1]).getText().trim()));
                
                // Lưu logic ép NULL chuẩn xác vào DB
                if (isGaDau) {
                    lt.setThoiGianDen(null);
                } else {
                    String strDen = cbxNgayDen.getSelectedItem() + " " + cbxGioDen.getSelectedItem() + ":" + cbxPhutDen.getSelectedItem();
                    lt.setThoiGianDen(LocalDateTime.parse(strDen, formatter));
                }

                if (isGaCuoi) {
                    lt.setThoiGianDi(null);
                } else {
                    String strDi = cbxNgayDi.getSelectedItem() + " " + cbxGioDi.getSelectedItem() + ":" + cbxPhutDi.getSelectedItem();
                    lt.setThoiGianDi(LocalDateTime.parse(strDi, formatter));
                }

                if (controller != null) {
                    boolean check = controller.capNhatLichTrinhNangCao(lt, maGaCu);
                    if (check) {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                        controller.loadLichTrinhByMaChuyen(maChuyen);
                        dialog.dispose();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi cập nhật dữ liệu!");
            }
        });
        dialog.setVisible(true);
    }

    private void xoaLichTrinh() {
        int rowLt = tableLichTrinh.getSelectedRow();
        int rowCt = tableChuyenTau.getSelectedRow();
        if (rowLt == -1 || rowCt == -1) { JOptionPane.showMessageDialog(this, "Chọn 1 chặng dừng để xóa!"); return; }
        
        String maChuyen = modelChuyenTau.getValueAt(rowCt, 0).toString();
        String maGa = modelLichTrinh.getValueAt(rowLt, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa ga " + maGa + " khỏi chuyến này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller != null && controller.xoaLichTrinh(maChuyen, maGa)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                controller.loadLichTrinhByMaChuyen(maChuyen);
            }
        }
    }
}