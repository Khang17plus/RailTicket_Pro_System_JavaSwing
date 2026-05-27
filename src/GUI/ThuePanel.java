package GUI;

import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import com.toedter.calendar.JDateChooser;

import Controller.ThueController;
import Entity.Thue;

public class ThuePanel extends JPanel {
    
    private Component component = new Component(); 
    private ThueController controller; 
    
    private String[] thueOptions = {
        "Thêm loại thuế",
        "Xóa loại thuế",
        "Sửa thông tin", 
        "Tra cứu thuế"
    };
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnExportExcel;
    private JButton btnImportExcel;
    private JLabel lblTongThue;
    private JLabel lblDangApDung;
    private JLabel lblNgungApDung;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setController(ThueController controller) {
        this.controller = controller;
    }

    public JButton createButtonExcel(String Cmt) {
        JButton btn = new JButton(Cmt);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String style = "arc:12; focusWidth:0; font: bold 13;";
        if (Cmt.contains("Nhập")) {
            btn.setBackground(new Color(59, 130, 246)); 
            btn.setForeground(Color.WHITE);
        } else if (Cmt.contains("Tìm")) {
            btn.setBackground(Color.gray); 
            btn.setForeground(Color.BLACK); 
            btn.setPreferredSize(new Dimension(60, 36));
        } else {
            btn.setBackground(new Color(34, 197, 94)); 
            btn.setForeground(Color.WHITE);
        }
        btn.setPreferredSize(new Dimension(140, 36)); 
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", style + "margin:8,15,8,15");

        return btn;
    }

    public JPanel createCardstatistical(String IconURL, String title, String initialValue) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        ImageIcon icon = new ImageIcon(IconURL);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(initialValue);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);

        if (title.contains("Tổng")) lblTongThue = valueLabel;
        else if (title.contains("Đang áp dụng")) lblDangApDung = valueLabel;
        else if (title.contains("Ngừng áp dụng")) lblNgungApDung = valueLabel;

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        card.setPreferredSize(new Dimension(165, 55));
        card.putClientProperty("FlatLaf.style", "arc:10; border:10,10,10,10; background:#FFFFFF");

        return card;
    }

    public ThuePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // --- Header Section ---
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250)); 

        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerL.setOpaque(false);
        
        JPanel headerR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        headerR.setOpaque(false);

        JPanel cardNgungApDung = createCardstatistical("img/expired.png", "Ngừng áp dụng ", "00");
        JPanel cardDangApDung = createCardstatistical("img/valid.png", "Đang áp dụng  ", "00");
        JPanel cardTongThue = createCardstatistical("img/equal.png", "Tổng loại thuế ", "00");

        headerR.add(cardNgungApDung);
        headerR.add(cardDangApDung);
        headerR.add(cardTongThue);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchPanel.setOpaque(false);
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(400, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập mã hoặc tên loại thuế...");
        JButton btnSearch = createButtonExcel("Tìm Kiếm");

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        btnExportExcel = createButtonExcel("Xuất file excel");
        btnImportExcel = createButtonExcel("Nhập file excel");

        rightPanel.add(btnExportExcel);
        rightPanel.add(btnImportExcel);

        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.CENTER);

        JLabel title = new JLabel("Quản lý thuế");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Thiết lập và quản lý các loại thuế phí áp dụng trên hệ thống");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);

        header.add(headerL, BorderLayout.WEST);
        header.add(headerR, BorderLayout.EAST);
        header.add(actionPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // --- Main Section (Table) ---
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);

        String[] columns = {"Mã Thuế", "Tên Thuế", "Mức Thuế", "Ngày Bắt Đầu", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(35); 
        table.setGridColor(new Color(235, 235, 235)); 
        table.setShowVerticalLines(false); 
        table.setSelectionBackground(new Color(232, 240, 254)); 

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 40));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); 

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 

        tableCard.add(scrollPane, BorderLayout.CENTER);
        tableCard.putClientProperty("FlatLaf.style", "arc:20; border:12,12,12,12; background:#FFFFFF");

        main.add(tableCard, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> {
            if (controller != null) {
                controller.timKiemThue(txtSearch.getText().trim());
            }
        });
        btnExportExcel.addActionListener(e -> {
            if (controller == null) return;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file Excel Khuyến Mãi");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                    fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".xlsx");
                }
                if (controller.exportToExcel(fileToSave)) {
                    JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi ghi file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnImportExcel.addActionListener(e -> {
            if (controller == null) return;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel Khuyến Mãi để nạp");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx"));
            
            int userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToOpen = fileChooser.getSelectedFile();
                int rowsImported = controller.importFromExcel(fileToOpen);
                if (rowsImported > 0) {
                    JOptionPane.showMessageDialog(this, "Nhập thành công " + rowsImported + " chương trình mới!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu mới nào được thêm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void setData(List<Thue> list) {
        tableModel.setRowCount(0);
        LocalDateTime bayGio = LocalDateTime.now();
        
        for (Thue t : list) {
            String ngayBD = (t.getNgayBatDau() != null) ? t.getNgayBatDau().format(formatter) : "";
            
            String trangThaiStr = "Ngừng áp dụng";
            if (t.isTrangThai()) {
                if (t.getNgayBatDau() != null && bayGio.isBefore(t.getNgayBatDau())) {
                    trangThaiStr = "Sắp diễn ra";
                } else {
                    trangThaiStr = "Đang áp dụng";
                }
            }
            
            tableModel.addRow(new Object[] {
                t.getMaThue(), 
                t.getTenThue(), 
                t.getPhanTram() + "%",
                ngayBD, 
                trangThaiStr
            });
        }
        capNhatThongKeCoDinh(list);
    }

    public void capNhatThongKeCoDinh(List<Thue> allList) {
        int dangApDung = 0;
        int ngungApDung = 0;
        LocalDateTime bayGio = LocalDateTime.now();

        for (Thue t : allList) {
            if (t.isTrangThai()) {
                if (t.getNgayBatDau() != null && bayGio.isBefore(t.getNgayBatDau())) {
                    // Sắp diễn ra
                } else {
                    dangApDung++;
                }
            } else {
                ngungApDung++;
            }
        }

        if (lblDangApDung != null) lblDangApDung.setText(String.format("%02d", dangApDung));
        if (lblNgungApDung != null) lblNgungApDung.setText(String.format("%02d", ngungApDung));
        if (lblTongThue != null) lblTongThue.setText(String.format("%02d", allList.size()));
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public List<JMenuItem> getMenuOption() {
        List<JMenuItem> submenu = new ArrayList<>();
        for (String option : thueOptions) {
            JMenuItem it = new JMenuItem(option);
            it.addActionListener(e -> {
                String text = ((JMenuItem) e.getSource()).getText();
                switch (text) {
                    case "Thêm loại thuế": themThue(); break;
                    case "Xóa loại thuế": xoaThue(); break;
                    case "Sửa thông tin": suaThue(); break;
                    case "Tra cứu thuế": traCuuThue(); break;
                }
            });
            submenu.add(it);
        }
        return submenu;
    }

    // 🔥 CẢI TIẾN: Thay đổi từ nhúng Trailing Component sang Panel độc lập để dập tắt lỗi FlatLaf hoàn toàn
    private JPanel createDatePickerPanel(JTextField field) {
        field.setEditable(false);
        field.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel container = new JPanel(new BorderLayout(2, 0));
        container.setOpaque(false);
        
        JButton btnCalendar = new JButton("📅");
        btnCalendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalendar.setFocusPainted(false);
        btnCalendar.putClientProperty("FlatLaf.style", "arc:6; focusWidth:0; margin:2,5,2,5;");
        
        btnCalendar.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(container);
            ThueDatePickerDialog picker = new ThueDatePickerDialog(owner, field.getText());
            picker.setVisible(true);
            if (picker.isConfirmed()) {
                field.setText(picker.getSelectedDateTimeString());
            }
        });
        
        field.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btnCalendar.doClick();
            }
        });

        container.add(field, BorderLayout.CENTER);
        container.add(btnCalendar, BorderLayout.EAST);
        return container;
    }

    private void themThue() {
        String[] labels = {
            "Mã Thuế", "Tên Loại Thuế", "Mức Thuế (%)",
            "Ngày Bắt Đầu", "Trạng Thái (1: Áp dụng, 0: Ngừng)"
        };

        // Đổi mảng từ JTextField[] sang JComponent[] để chứa được cả JPanel lịch
        JComponent[] fields = new JComponent[labels.length];
        
        JTextField txtMa = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtMucThue = new JTextField();
        JTextField txtNgayBD = new JTextField();
        JTextField txtTrangThai = new JTextField("1"); // Để sẵn mặc định là áp dụng

        // 🌟 ĐOẠN PHÁT SINH MÃ TỰ ĐỘNG GIỐNG KHUYẾN MÃI
        if (controller != null) {
            txtMa.setText(controller.phatSinhMaTuDong()); 
        }
        txtMa.setEditable(false); 
        txtMa.setBackground(new Color(240, 240, 240)); 

        // Tự động điền ngày hiện tại làm mẫu
        txtNgayBD.setText(LocalDateTime.now().format(formatter));
        
        fields[0] = txtMa;
        fields[1] = txtTen;
        fields[2] = txtMucThue;
        
        // Bọc ô nhập ngày vào Panel chứa nút lịch an toàn
        fields[3] = createDatePickerPanel(txtNgayBD);
        fields[4] = txtTrangThai;

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnSave = new JButton("Lưu Thiết Lập");

        JDialog dialog = component.createDinamicForm(
            "Thêm Loại Thuế",
            "Thiết Lập Thuế",
            "Vui lòng điền thông tin và mức thuế áp dụng",
            labels,
            fields,
            new JButton[]{btnCancel, btnSave}
        );

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            try {
                String maThue = txtMa.getText().trim();
                String tenThue = txtTen.getText().trim();
                
                if (txtNgayBD.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn ngày bắt đầu!");
                    return;
                }
                
                double phanTram = Double.parseDouble(txtMucThue.getText().trim());
                LocalDateTime ngayBD = LocalDateTime.parse(txtNgayBD.getText().trim(), formatter);
                boolean trangThai = txtTrangThai.getText().trim().equals("1");

                if (maThue.isEmpty() || tenThue.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Không được để trống thông tin!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Thue newThue = new Thue(maThue, tenThue, phanTram, ngayBD, trangThai);

                if (controller != null && controller.themThue(newThue)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm thiết lập thuế thành công!");
                    controller.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm vào CSDL!", "Thất bại", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Mức thuế phải là số số hợp lệ!", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
    
    

    private void xoaThue() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại thuế cần xóa!");
            return;
        }

        String maThue = tableModel.getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn ngừng áp dụng cấu hình thuế " + maThue + " không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION && controller != null) {
            if (controller.xoaThue(maThue)) {
                JOptionPane.showMessageDialog(this, "Xóa (Ngừng áp dụng) thành công!");
                controller.loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xử lý dòng thuế này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    

    private void suaThue() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cấu hình thuế cần sửa!");
            return;
        }

        String maThue = tableModel.getValueAt(selectedRow, 0).toString();
        String tenThue = tableModel.getValueAt(selectedRow, 1).toString();
        String phanTramRaw = tableModel.getValueAt(selectedRow, 2).toString().replace("%", "");
        String ngayBD = tableModel.getValueAt(selectedRow, 3).toString();
        String trangThaiRaw = tableModel.getValueAt(selectedRow, 4).toString();

        String[] labels = {
            "Mã Thuế (Không sửa)",
            "Tên Loại Thuế",
            "Mức Thuế (%)",
            "Ngày Bắt Đầu",
            "Trạng Thái (1: Áp dụng, 0: Ngừng)"
        };

        JComponent[] fields = new JComponent[labels.length];
        
        JTextField txtMa = new JTextField(maThue);
        JTextField txtTen = new JTextField(tenThue);
        JTextField txtMucThue = new JTextField(phanTramRaw);
        JTextField txtNgayBD = new JTextField(ngayBD);
        JTextField txtTrangThai = new JTextField(trangThaiRaw.contains("Đang") || trangThaiRaw.contains("Sắp") ? "1" : "0");
        
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(240, 240, 240));

        fields[0] = txtMa;
        fields[1] = txtTen;
        fields[2] = txtMucThue;
        
        // Tái sử dụng Panel bọc nút lịch cho form sửa
        fields[3] = createDatePickerPanel(txtNgayBD);
        fields[4] = txtTrangThai;

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnUpdate = new JButton("Cập nhật");

        JDialog dialog = component.createDinamicForm(
            "Sửa Cấu Hình Thuế",
            "Mã số thuế: " + maThue,
            "Vui lòng chỉnh sửa các thông số cần thiết",
            labels,
            fields,
            new JButton[]{btnCancel, btnUpdate}
        );

        btnCancel.addActionListener(ev -> dialog.dispose());

        btnUpdate.addActionListener(ev -> {
            try {
                Thue updatedThue = new Thue(
                    maThue,
                    txtTen.getText().trim(),
                    Double.parseDouble(txtMucThue.getText().trim()),
                    LocalDateTime.parse(txtNgayBD.getText().trim(), formatter),
                    txtTrangThai.getText().trim().equals("1")
                );

                if (controller != null && controller.updateThue(updatedThue)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                    controller.loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Mức thuế phải là số hợp lệ!", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi định dạng thời gian hoặc dữ liệu!", "Lỗi cấu trúc", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void traCuuThue() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Tên hoặc Mã Thuế cần tìm:");
        if (keyword != null && controller != null) {
            controller.timKiemThue(keyword.trim());
        }
    }
}

// ==========================================
// THÀNH PHẦN ĐƯỢC TÁCH BIỆT: BẢNG CHỌN NGÀY GIỜ CHUYÊN BIỆT CHO THUẾ
// ==========================================
class ThueDatePickerDialog extends JDialog {
    private JDateChooser dateChooser;
    private JSpinner timeSpinner;
    private boolean confirmed = false;
    private LocalDateTime resultDateTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ThueDatePickerDialog(Window owner, String initialValue) {
        super(owner, "Chọn Thời Gian Áp Dụng", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(10, 10));
        setSize(new Dimension(360, 160));
        setLocationRelativeTo(owner);
        setResizable(false);
        
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.getCalendarButton().setCursor(new Cursor(Cursor.HAND_CURSOR));
        dateChooser.setPreferredSize(new Dimension(140, 30));
        
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setPreferredSize(new Dimension(75, 30));

        if (initialValue != null && !initialValue.trim().isEmpty()) {
            try {
                LocalDateTime existingLdt = LocalDateTime.parse(initialValue.trim(), formatter);
                Date existingDate = Date.from(existingLdt.atZone(ZoneId.systemDefault()).toInstant());
                dateChooser.setDate(existingDate);
                timeSpinner.setValue(existingDate);
            } catch (Exception e) {
                dateChooser.setDate(new Date());
            }
        } else {
            dateChooser.setDate(new Date());
        }

        gbc.gridx = 0; gbc.gridy = 0;
        mainContent.add(new JLabel("Chọn Ngày:"), gbc);
        gbc.gridx = 1;
        mainContent.add(dateChooser, gbc);
        
        gbc.gridx = 2;
        mainContent.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3;
        mainContent.add(timeSpinner, gbc);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionRow.setBackground(new Color(245, 245, 245));
        actionRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        JButton btnOk = new JButton("Xác nhận");
        JButton btnCancel = new JButton("Đóng");
        
        btnOk.putClientProperty("FlatLaf.style", "background:#22c55e; foreground:#ffffff; arc:8; focusWidth:0;");
        btnCancel.putClientProperty("FlatLaf.style", "arc:8; focusWidth:0;");

        btnOk.addActionListener(e -> {
            if (dateChooser.getDate() != null) {
                LocalDate datePart = dateChooser.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                    
                Date timeValue = (Date) timeSpinner.getValue();
                java.time.LocalTime timePart = timeValue.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalTime();
                
                resultDateTime = datePart.atTime(timePart);
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dispose());
        
        actionRow.add(btnCancel);
        actionRow.add(btnOk);

        add(mainContent, BorderLayout.CENTER);
        add(actionRow, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedDateTimeString() {
        return resultDateTime != null ? resultDateTime.format(formatter) : "";
    }
}