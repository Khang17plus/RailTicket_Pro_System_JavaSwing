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

    private JButton btnThemMoi;
    private JButton btnDoiTrangThai;
    private JButton btnThemToa;
    private JButton btnPhatSinhGhe;
    private JButton btnThemGheLe;
    
    private String currentSelectedToa; // lưu mã toa đang chọn trên bảng (dùng cho phát sinh ghế sau)

    
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
        
//        
//        JPanel leftContent = new JPanel(new GridLayout(2,1 ,0 ,15));
//        leftContent.setOpaque(false);
        
        
        

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

        JPanel formBody = new JPanel(new GridLayout(0, 1, 0, 6));
        formBody.setOpaque(false);
        
        // Đưa các JTextField đã khai báo vào hàm
        formBody.add(createInputGroup("Mã Tàu:", txtMaTau));
        formBody.add(createInputGroup("Tên Tàu:", txtTenTau));
        formBody.add(createInputGroup("Loại Tàu:", txtLoaiTau));
        
        formBody.add(lblStatus); // Add Label trạng thái

        JPanel btnGroup = new JPanel(new GridLayout(1, 2, 10, 0));
        
        btnThemMoi = createStyledButton(
        	    "Thêm Mới",
        	    new Color(16, 185, 129),
        	    Color.WHITE
        	);
        btnThemMoi.addActionListener(e -> themTauMoi());
        
        btnDoiTrangThai = createStyledButton("Đổi Trạng Thái", Color.WHITE, Color.BLACK);
        btnGroup.add(btnThemMoi);
        
        btnGroup.add(btnDoiTrangThai);
        
        btnThemMoi.addActionListener(e -> themTauMoi());
        btnDoiTrangThai.addActionListener(e -> doiTrangThai());
        formBody.add(btnGroup);

        formWrapper.add(formBody, BorderLayout.NORTH);
        formCard.add(formWrapper, BorderLayout.CENTER);

//     
//         --- TẠO SPLIT PANE ---
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
     // Footer buttons cho table
        JPanel footerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footerButtons.setOpaque(false);
        btnThemToa = createStyledButton("Thêm Toa", Color.WHITE, Color.BLACK);
        btnPhatSinhGhe = createStyledButton("Phát Sinh Ghế", Color.WHITE, Color.BLACK);
//        btnThemGheLe = createStyledButton("Thêm Ghế Lẻ", Color.WHITE, Color.BLACK);
        footerButtons.add(btnThemToa);
        footerButtons.add(btnPhatSinhGhe);
//        footerButtons.add(btnThemGheLe);

        rightPanel.add(footerButtons, BorderLayout.SOUTH);

        // Gán sự kiện
        btnThemToa.addActionListener(e -> themToaMoi());
        btnPhatSinhGhe.addActionListener(e -> phatSinhGheTuDong()); // sẽ viết sau
//        btnThemGheLe.addActionListener(e -> themGheLe()); // sẽ viết sau
        
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    // =========================================================
    // CÁC HÀM CẬP NHẬT GIAO DIỆN (Controller sẽ gọi mấy hàm này)
    // =========================================================

    
    private void themToaMoi() {
        // Kiểm tra đã chọn tàu chưa
        String maTau = txtMaTau.getText().trim();
        if (maTau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu trước khi thêm toa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo dialog nhập liệu
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm toa mới", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(450, 320);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã toa (tự sinh)
        JLabel lblMaToa = new JLabel("Mã toa:");
        JTextField txtMaToa = new JTextField();
        txtMaToa.setEditable(false);
        txtMaToa.setBackground(new Color(240, 240, 240));
        String nextMaToa = controller.generateNextMaToa(maTau);
        txtMaToa.setText(nextMaToa);

        // Tên toa
        JLabel lblTenToa = new JLabel("Tên toa:*");
        JTextField txtTenToa = new JTextField(15);

        // Loại toa (comboBox)
        JLabel lblLoaiToa = new JLabel("Loại toa:");
        String[] loaiToaArr = {"Ngồi mềm", "Ngồi cứng", "Giường nằm", "VIP"};
        JComboBox<String> cbLoaiToa = new JComboBox<>(loaiToaArr);

        // Sức chứa (tự động theo loại)
        JLabel lblSucChua = new JLabel("Sức chứa:");
        JTextField txtSucChua = new JTextField(5);
        // Map sức chứa mặc định
        java.util.Map<String, Integer> sucChuaMap = new java.util.HashMap<>();
        sucChuaMap.put("Ngồi mềm", 56);
        sucChuaMap.put("Ngồi cứng", 80);
        sucChuaMap.put("Giường nằm", 36);
        sucChuaMap.put("VIP", 24);

        // Cập nhật sức chứa khi chọn loại toa
        cbLoaiToa.addActionListener(e -> {
            String selected = (String) cbLoaiToa.getSelectedItem();
            txtSucChua.setText(String.valueOf(sucChuaMap.getOrDefault(selected, 56)));
        });
        cbLoaiToa.setSelectedIndex(0); // kích hoạt lần đầu

        // Layout các thành phần
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblMaToa, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtMaToa, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblTenToa, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtTenToa, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblLoaiToa, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(cbLoaiToa, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblSucChua, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(txtSucChua, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // Xử lý lưu
        btnSave.addActionListener(e -> {
            String tenToa = txtTenToa.getText().trim();
            String loaiToa = (String) cbLoaiToa.getSelectedItem();
            int sucChua;
            try {
                sucChua = Integer.parseInt(txtSucChua.getText().trim());
                if (sucChua <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Sức chứa phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tenToa.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập tên toa!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tạo đối tượng ToaTau
            ToaTau toa = new ToaTau();
            toa.setMaToa(txtMaToa.getText().trim());
            toa.setMaTau(maTau);
            toa.setTenToa(tenToa);
            toa.setLoaiToa(loaiToa);
            toa.setSucChua(sucChua);

            boolean success = controller.themToa(toa);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Thêm toa thành công!");
                dialog.dispose();
                // Refresh lại danh sách toa của tàu hiện tại
                controller.chonTau(maTau);
            } else {
                JOptionPane.showMessageDialog(dialog, "Thêm toa thất bại!\nCó thể mã toa bị trùng hoặc lỗi DB.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }
    
    
   private void themTauMoi() {
    // Tạo dialog nhập liệu
    JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm tàu mới", Dialog.ModalityType.APPLICATION_MODAL);
    dialog.setSize(400, 250);
    dialog.setLayout(new BorderLayout());
    dialog.setLocationRelativeTo(this);

    // Panel nhập liệu
    JPanel inputPanel = new JPanel(new GridBagLayout());
    inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Mã tàu (tự sinh, chỉ hiển thị)
    JLabel lblMa = new JLabel("Mã tàu:");
    JTextField txtMa = new JTextField();
    txtMa.setEditable(false);
    txtMa.setBackground(new Color(240, 240, 240));
    String nextMa = controller.generateNextMaTau();  // gọi Controller sinh mã
    txtMa.setText(nextMa);

    // Tên tàu
    JLabel lblTen = new JLabel("Tên tàu:*");
    JTextField txtTen = new JTextField(15);

    // Loại tàu
    JLabel lblLoai = new JLabel("Loại tàu:*");
    JTextField txtLoai = new JTextField(15);

    // Layout
    gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(lblMa, gbc);
    gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(txtMa, gbc);
    gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(lblTen, gbc);
    gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(txtTen, gbc);
    gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(lblLoai, gbc);
    gbc.gridx = 1; gbc.gridy = 2; inputPanel.add(txtLoai, gbc);

    // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSave = new JButton("Lưu");
    JButton btnCancel = new JButton("Hủy");
    buttonPanel.add(btnSave);
    buttonPanel.add(btnCancel);

    dialog.add(inputPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    // Xử lý sự kiện
    btnSave.addActionListener(e -> {
        String tenTau = txtTen.getText().trim();
        String loaiTau = txtLoai.getText().trim();

        if (tenTau.isEmpty() || loaiTau.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ Tên tàu và Loại tàu!", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo đối tượng Tàu
        Tau tau = new Tau();
        tau.setMaTau(txtMa.getText().trim());
        tau.setTenTau(tenTau);
        tau.setLoaiTau(loaiTau);
        tau.setTrangThai("Sẵn sàng");  // hoặc "Đang hoạt động" tuỳ logic

        boolean success = controller.themTau(tau);
        if (success) {
            JOptionPane.showMessageDialog(dialog, "Thêm tàu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            controller.loadDanhSachTau();   // reload danh sách tàu
            clearForm();                    // xoá các trường bên panel chính
        } else {
            JOptionPane.showMessageDialog(dialog, "Thêm tàu thất bại!\nCó thể mã tàu đã tồn tại hoặc lỗi DB.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnCancel.addActionListener(e -> dialog.dispose());
    dialog.setVisible(true);
}
   
// Phát sinh ghế tự động cho toa đang chọn
private void phatSinhGheTuDong() {
    if (currentSelectedToa == null || currentSelectedToa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn toa cần phát sinh ghế!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(this,
            "Phát sinh ghế sẽ xóa toàn bộ ghế cũ của toa này và tạo mới theo sức chứa.\nBạn có chắc chắn?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    boolean success = controller.phatSinhGheChoToa(currentSelectedToa);
    if (success) {
        JOptionPane.showMessageDialog(this, "Phát sinh ghế thành công!");
        // Refresh lại danh sách toa (có thể hiển thị số ghế đã tạo, tuỳ ý)
        // Ở đây chỉ cần reload lại toa đang chọn để cập nhật thông tin (nếu cần)
        String maTau = txtMaTau.getText().trim();
        if (!maTau.isEmpty()) controller.chonTau(maTau);
    } else {
        JOptionPane.showMessageDialog(this, "Phát sinh ghế thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}

// Thêm một ghế lẻ vào toa đang chọn
private void themGheLe() {
    if (currentSelectedToa == null || currentSelectedToa.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn toa cần thêm ghế!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Dialog nhập số ghế và loại ghế
    JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    JTextField txtSoGhe = new JTextField();
    JTextField txtLoaiGhe = new JTextField();
    panel.add(new JLabel("Số ghế:"));
    panel.add(txtSoGhe);
    panel.add(new JLabel("Loại ghế:"));
    panel.add(txtLoaiGhe);

    int option = JOptionPane.showConfirmDialog(this, panel, "Thêm ghế lẻ",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (option == JOptionPane.OK_OPTION) {
        try {
            int soGhe = Integer.parseInt(txtSoGhe.getText().trim());
            String loaiGhe = txtLoaiGhe.getText().trim();
            if (soGhe <= 0) throw new NumberFormatException();
            if (loaiGhe.isEmpty()) loaiGhe = "Ghế ngồi";

            boolean success = controller.themGheLe(currentSelectedToa, soGhe, loaiGhe);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm ghế thành công!");
                // Refresh lại toa để cập nhật (không cần thiết lắm, nhưng gọi lại để đồng bộ)
                String maTau = txtMaTau.getText().trim();
                if (!maTau.isEmpty()) controller.chonTau(maTau);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm ghế thất bại!\nCó thể số ghế đã tồn tại trong toa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số ghế phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
   
   private void doiTrangThai() {
	    String maTau = txtMaTau.getText().trim();
	    if (maTau.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu cần đổi trạng thái!", "Thông báo", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    String trangThaiHienTai = lblStatus.getText().replace("Trạng thái: ", "");
	    String[] dsTrangThai = {"Sẵn sàng", "Đang vận hành", "Đang bảo trì", "Ngừng hoạt động"};
	    JComboBox<String> cbTrangThai = new JComboBox<>(dsTrangThai);
	    cbTrangThai.setSelectedItem(trangThaiHienTai);
	    
	    int option = JOptionPane.showConfirmDialog(this, cbTrangThai, "Chọn trạng thái mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (option == JOptionPane.OK_OPTION) {
	        String trangThaiMoi = (String) cbTrangThai.getSelectedItem();
	        boolean success = controller.capNhatTrangThai(maTau, trangThaiMoi);
	        if (success) {
	            JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công!");
	            controller.loadDanhSachTau(); // reload danh sách
	            // Cập nhật lại thông tin tàu hiện tại
	            controller.chonTau(maTau);
	        } else {
	            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
    
   private void clearForm() {
	    txtMaTau.setText("");
	    txtTenTau.setText("");
	    txtLoaiTau.setText("");
	    lblStatus.setText("Trạng thái: N/A");
	    lblStatus.setForeground(Color.GRAY);
	}
   
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
        modelToa.setRowCount(0);
        for (ToaTau t : list) {
            modelToa.addRow(new Object[]{
                t.getMaToa(), 
                t.getMaTau(), 
                t.getTenToa(), 
                t.getLoaiToa(), 
                t.getSucChua() 
            });
        }
        // Bắt sự kiện chọn dòng để lưu mã toa đang chọn
        tableToa.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableToa.getSelectedRow();
                if (row != -1) {
                    currentSelectedToa = (String) tableToa.getValueAt(row, 0);
                }
            }
        });
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
        
        txt.putClientProperty(FlatClientProperties.STYLE, "arc: 8;  margin: 2,8,2,8");
        
        group.add(lbl, BorderLayout.NORTH);
        group.add(txt, BorderLayout.CENTER);
        return group;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 38));
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