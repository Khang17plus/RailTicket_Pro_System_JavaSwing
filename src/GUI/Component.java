package GUI;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class Component extends JFrame {

//    public Component() {
//        // --- THÊM DÒNG NÀY: Bật tính năng bo góc và đổ bóng cho cửa sổ ---
//        JFrame.setDefaultLookAndFeelDecorated(true);
//        JDialog.setDefaultLookAndFeelDecorated(true);
//        
//        // Cài đặt giao diện FlatLaf
//        FlatLightLaf.setup();
//
//        JPanel container = new JPanel();
//        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
//        // Thêm padding cho container để nút không dính sát mép
//        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        
//        
//        JButton btnOpen = new JButton("Thêm Khách Hàng Mới");
//       
//        
//        
//        btnOpen.addActionListener(e -> {
//            // 1. Định nghĩa các nhãn (Labels) - Muốn thêm ô gì thì cứ viết thêm vào đây
//            String[] labels = {"Họ và Tên", "Số CCCD", "Số Điện Thoại", "Địa Chỉ Email", "Ghi Chú"};
//            
//            // 2. Khởi tạo mảng JTextField tương ứng với số lượng nhãn
//            JTextField[] fields = new JTextField[labels.length];
//            for (int i = 0; i < fields.length; i++) {
//                fields[i] = new JTextField();
//            }
//
//            // 3. Tạo các nút bấm cho Footer
//            JButton btnCancel = new JButton("Hủy bỏ");
//            JButton btnSave = new JButton("Lưu Khách Hàng");
//
//            // 4. Gọi hàm createDinamicForm với đầy đủ tham số
//            JDialog form = createDinamicForm(
//                "Hệ thống quản lý",               // titleDialog
//                "Thêm Khách Hàng Mới",            // headerTitle
//                "Vui lòng điền chính xác thông tin", // subtitle
//                labels,                          // mảng labels
//                fields,                          // mảng fields
//                new JButton[]{btnCancel, btnSave} // mảng buttons
//            );
//
//            // 5. Thêm sự kiện đóng form cho nút Hủy
//            btnCancel.addActionListener(ex -> form.dispose());
//
//            // 6. Hiển thị form
//            form.setVisible(true);
//        });
//
//        container.add(btnOpen);
//
//        JScrollPane scroll = new JScrollPane(container);
//        scroll.getVerticalScrollBar().setUnitIncrement(16);
//        add(scroll);
//
//        setTitle("test Component");
//        setSize(1280, 800);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setVisible(true);
//    }

      
    
	public JDialog createDinamicForm(String titleDialog, String headerTitle, String subtitle, String labels[], JComponent[] fields, JButton[] buttons) {
        JDialog form = new JDialog();
        form.setTitle(titleDialog);
        form.setModal(true);
        form.setResizable(false);
        form.setLayout(new BorderLayout());
        
        // Panel chính với Padding lớn tạo không gian thoáng
        JPanel container = new JPanel(new BorderLayout(0, 25));
        container.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        container.setBackground(UIManager.getColor("Panel.background"));

        // --- 1. HEADER (Dùng Label Style của FlatLaf) ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);

        JLabel lbTitle = new JLabel(headerTitle);
        lbTitle.putClientProperty("FlatLaf.style", "font: $h2.font; foreground: $AccentColor");

        JLabel lbSub = new JLabel(subtitle);
        lbSub.putClientProperty("FlatLaf.style", "font: $small.font; foreground: #808080");

        headerPanel.add(lbTitle);
        headerPanel.add(lbSub);

        // --- 2. BODY (Duyệt mảng để tạo các Input Group) ---
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);

        for (int i = 0; i < labels.length; i++) {
            JPanel group = new JPanel(new BorderLayout(0, 8));
            group.setOpaque(false);
            group.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

            JLabel lbl = new JLabel(labels[i]);
            lbl.putClientProperty("FlatLaf.style", "font: 13; foreground: #404040");

            // Styling chung cho JComponent (cả TextField và ComboBox)
            fields[i].setPreferredSize(new Dimension(380, 38));
            fields[i].putClientProperty("FlatLaf.style", "arc: 10; focusWidth: 2");

            // Kiểm tra nếu là JTextField thì mới set placeholder
            if (fields[i] instanceof JTextField) {
                ((JTextField) fields[i]).putClientProperty("JTextField.placeholderText", "Nhập " + labels[i].toLowerCase() + "...");
            }

            group.add(lbl, BorderLayout.NORTH);
            group.add(fields[i], BorderLayout.CENTER);
            bodyPanel.add(group);
        }

        // --- 3. FOOTER (Các nút bấm) ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        footerPanel.setOpaque(false);

        for (JButton btn : buttons) {
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(120, 38));
            
            // Nếu là nút chính (thường là nút cuối cùng), cho nó nổi bật
            if (btn == buttons[buttons.length - 1]) {
                btn.putClientProperty("JButton.buttonType", "accent"); // Màu chủ đạo (Xanh)
                btn.putClientProperty("FlatLaf.style", "arc: 10;");
            } else {
                btn.putClientProperty("FlatLaf.style", "arc: 10;");
            }
            footerPanel.add(btn);
        }

        // Ráp vào container
        container.add(headerPanel, BorderLayout.NORTH);
        container.add(bodyPanel, BorderLayout.CENTER);
        container.add(footerPanel, BorderLayout.SOUTH);

        form.add(container);
        form.pack(); // Tự động co giãn theo nội dung
        form.setLocationRelativeTo(null);
        
        return form;
    }
//    public static void main(String[] args) {
//        // Chạy ứng dụng
//        SwingUtilities.invokeLater(() -> new Component());
//    }
}