package GUI;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.formdev.flatlaf.FlatLightLaf;

import Controller.KhachHangController;
import DAO.KhachHangDAO;
import Entity.KhachHang;
//import sun.security.ec.point.ProjectivePoint;


public class KhachHangPanel extends JPanel {
	private KhachHangController controller;
	private Component component = new Component();
	
	 private String[] khachHangOptions = {
		        "Thêm khách hàng",
		        "Xóa khách hàng",
		        "Sửa thông tin", 
		        "Tra cứu khách hàng"
		    };
    private JTable table;
    private DefaultTableModel tableModel;
    
    
   public void setController(KhachHangController controller) {
	   this.controller = controller;
   }
   
    
    public JButton createButtonExcel(String Cmt) {
    	JButton btn = new JButton(Cmt);
    	btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	String style = "arc:12; focusWidth:0; font: bold 13;";
    	  if (Cmt.contains("Nhập")) {
    	        btn.setBackground(new Color(59,130,246)); // xanh dương
    	    }
    	  else if(Cmt.contains("Tìm"))  {
    		  btn.setBackground(Color.gray); // nền trắng
    	        btn.setForeground(Color.BLACK); // chữ đen
    	        
    	        btn.setPreferredSize(new Dimension(60, 36));
    	  }
    	  
    	  else {
    	        btn.setBackground(new Color(34,197,94)); // xanh lá
    	    }
    	  btn.setPreferredSize(new Dimension(140, 36)); // 🔥 CHUẨN CHIỀU CAO
    	    btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    	    btn.setFocusPainted(false);
    	    
//    	    btn.putClientProperty("FlatLaf.style", "arc:10; margin:8,15,8,15");
//    	   
    	    btn.putClientProperty("FlatLaf.style", style + "margin:8,15,8,15");
    	   
    	return btn;
    }
    

    
    public JPanel createCardstatistical( String  IconURL , String title, int value ) {
    	JPanel card = new JPanel(new BorderLayout(15,0));
    	ImageIcon icon = new ImageIcon(IconURL);
        Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));
    	
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(Color.GRAY);
        
        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.BLACK);
        
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        // add vào card
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        
    	
    	card.putClientProperty("FlatLaf.style",  "arc:10; border:10,10,10,10; background:#FFFFFF");
    	
    	return card;
    	
    }
    
    
    
    public KhachHangPanel() {
        
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); 

        // 2. Phần Header (Tiêu đề và Mô tả)
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250)); // Đồng bộ màu nền
        
        JPanel headerL = new JPanel();
        headerL.setLayout(new BoxLayout(headerL, BoxLayout.Y_AXIS));
        headerL.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel headerR = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        
        JPanel cardKH = createCardstatistical("img/user2.png", "Tổng khách hàng ", 125);
      
        JPanel cardKHmoi = createCardstatistical("img/user2.png", "Mới Tháng Này  ", 15);
        JPanel cardKHMua = createCardstatistical("img/user2.png", "Đặt chổ gần đây ", 25);
        
        
        JPanel actionPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(400, 36));
        txtSearch.putClientProperty("FlatLaf.style", "arc:10");
        
        JButton btnSearch = createButtonExcel("Tìm Kiếm");
        
        
        
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));

        
        
        
        JButton imports= createButtonExcel("Nhập file excel");
        JButton export = createButtonExcel("Xuất file excel");
        
        
        
        rightPanel.add(export);
        rightPanel.add(imports);
        
        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(rightPanel, BorderLayout.CENTER);
        
        headerR.add(cardKHMua);
     
        headerR.add(cardKHmoi);
        
        headerR.add(cardKH);
        
        
        
        
        
        JLabel title = new JLabel("Quản lí khách hàng");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Quản lí thông tin khách hàng sử dụng dịch vụ metro");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        headerL.add(title);
        headerL.add(Box.createVerticalStrut(5));
        headerL.add(sub);
        
        header.add(headerL,BorderLayout.WEST);
        header.add(headerR,BorderLayout.EAST);
        header.add(actionPanel,BorderLayout.SOUTH);
        
        add(header, BorderLayout.NORTH);

        // 3. Phần Main chứa Table (Card giao diện)
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(new Color(245, 247, 250));
       main.putClientProperty("FlatLaf.style", "arc:20; border:10,10,10,10");

        // Tạo một Panel bọc cái bảng lại cho giống giao diện "Card" trong Dashboard
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Khởi tạo Model và Bảng (Dựa trên DB: maKH, tenKH, cccd, sdt, email, ngayDangKy)
        String[] columns = {"Mã KH", "Họ và tên", "CCCD", "Số điện thoại", "Email", "Ngày đăng ký"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên ô
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40);
        // Format Style cho Table đồng bộ với hệ thống
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(35); // Chiều cao dòng
        table.setGridColor(new Color(235, 235, 235)); // Màu đường viền bảng
        table.setShowVerticalLines(false); // Ẩn kẻ dọc cho hiện đại
        table.setSelectionBackground(new Color(232, 240, 254)); // Màu khi chọn dòng
        
        // Format Header của Table
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 13));
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(100, 40));
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); // Chỉ có viền dưới
        
        // Cuộn trang
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Bỏ viền mặc định của ScrollPane

        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        tableCard.putClientProperty("FlatLaf.style",
        	    "arc:20; border:12,12,12,12; background:#FFFFFF");
        
        main.add(tableCard, BorderLayout.CENTER);
        
      
        
        
        
        add(main, BorderLayout.CENTER);
        
       
        
        // 4. Load dữ liệu giả để test giao diện
     
    }

    // Hàm thêm dữ liệu mẫu (Sau này bạn xóa hàm này đi và load từ DB lên)

    
    
    
    public void setData(List<KhachHang> list) {
    	tableModel.setRowCount(0);
    	for(KhachHang kh: list) {
    		tableModel.addRow(new Object[] {
    				kh.getMaKH(), kh.getTenKH(), kh.getCccd(),
    	            kh.getSoDienThoai(), kh.getEmail(), kh.getNgayDangKy()
    		});
    		
    	}
    	
    	
    	
    	
    }
   
    
    // Getter để gọi tableModel từ Controller/DAO đổ dữ liệu
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    
    public List<JMenuItem> getMenuOption(){
    	List<JMenuItem> submenu = new ArrayList<>();
    	
    	
    	
    	for (String option : khachHangOptions) {
    		JMenuItem it = new JMenuItem(option);
    		
    		
    		
    		 it.addActionListener(e -> {
    	            String text = ((JMenuItem) e.getSource()).getText();
    	            
    	            switch (text) {
    	                case "Thêm khách hàng":
    	                    themKhachHang();
    	                    break;
    	                case "Xóa khách hàng":
    	                    xoaKhachHang();
    	                    break;
    	                case "Sửa thông tin":
    	                    suaKhachHang();
    	                    break;
    	                case "Tra cứu khách hàng":
    	                    traCuuKhachHang();
    	                    break;
    	                default:
    	                    JOptionPane.showMessageDialog(null, "Chọn: " + text);
    	            }
    	        });
    		submenu.add(it);
    	}
    	
    	
    	return submenu;
    }
    
    
    
    
    
    private void themKhachHang() {
        // 1. Chuẩn bị mảng Label và Field
        // Thêm "Mã Khách Hàng" nếu DB của bạn không tự tăng (Identity). Nếu tự tăng thì bỏ chữ Mã KH đi nhé.
        String[] labels = {"Mã Khách Hàng", "Họ và Tên", "Số CCCD", "Số Điện Thoại", "Địa chỉ Email"};
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
        }

        // 2. Chuẩn bị nút bấm
        JButton btnCancel = new JButton("Hủy bỏ");
        
        JButton btnSave = new JButton("Lưu Khách Hàng");

        // 3. Gọi Component tái sử dụng để tạo Form
        // Lưu ý: Đảm bảo class Component của bạn không gọi setVisible(true) trong Constructor nữa nhé, nếu không nó sẽ tự nhảy ra 1 cái frame test đấy.
        JDialog dialog = component.createDinamicForm(
            "Thêm Khách Hàng Mới", 
            "Nhập Thông Tin", 
            "Vui lòng điền đầy đủ các thông tin bên dưới", 
            labels, fields, new JButton[]{btnCancel, btnSave}
        );

        // 4. Bắt sự kiện Hủy
        btnCancel.addActionListener(e -> dialog.dispose());

        // 5. BẮT SỰ KIỆN LƯU VÀ ĐẨY XUỐNG CONTROLLER
        btnSave.addActionListener(e -> {
            // Lấy dữ liệu từ các ô nhập
            String maKH = fields[0].getText().trim();
            String tenKH = fields[1].getText().trim();
            String cccd = fields[2].getText().trim();
            String sdt = fields[3].getText().trim();
            String email = fields[4].getText().trim();

            // Validate cơ bản: Không được để trống
            if (maKH.isEmpty() || tenKH.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đủ Mã KH, Tên và SĐT!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tạo đối tượng Entity KhachHang (Ngày đăng ký lấy ngày hiện tại)
            // Lưu ý: Constructor này phải khớp với class Entity.KhachHang của bạn
            LocalDateTime ngayHienTai =  LocalDateTime.now();
            KhachHang khNew = new KhachHang(maKH, tenKH, cccd, sdt, email, ngayHienTai);

            // GỌI CONTROLLER ĐỂ XỬ LÝ LƯU (Flow chính nằm ở đây)
            if (controller != null) {
                boolean isSuccess = controller.themKhachHang(khNew); // Phương thức này ta sẽ viết ở Bước 2
                
                if (isSuccess) {
                    JOptionPane.showMessageDialog(dialog, "Thêm khách hàng thành công!");
                    
                    // Cập nhật lại giao diện bảng (Add trực tiếp dòng mới vào bảng cho nhanh, đỡ phải load lại DB)
                    tableModel.addRow(new Object[]{
                        khNew.getMaKH(), khNew.getTenKH(), khNew.getCccd(), 
                        khNew.getSoDienThoai(), khNew.getEmail(), khNew.getNgayDangKy()
                    });
                    
                    dialog.dispose(); // Tắt form
                } else {
                    JOptionPane.showMessageDialog(dialog, "Thêm thất bại! Trùng mã KH hoặc lỗi hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Chưa khởi tạo Controller!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
    
     
 // ----------------------------------------------------
    // Xóa khách hàng
    private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy mã khách hàng từ cột đầu tiên (Cột 0)
        String maKH = tableModel.getValueAt(selectedRow, 0).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng " + maKH + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Đẩy xuống Controller xử lý
            if (controller != null && controller.xoaKhachHang(maKH)) {
                tableModel.removeRow(selectedRow); // Xóa trên giao diện
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Khách hàng có thể đã mua vé.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ----------------------------------------------------
    // Sửa khách hàng
    private void suaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy dữ liệu cũ từ Table
        String maCu = tableModel.getValueAt(selectedRow, 0).toString();
        String tenCu = tableModel.getValueAt(selectedRow, 1).toString();
        String cccdCu = tableModel.getValueAt(selectedRow, 2).toString();
        String sdtCu = tableModel.getValueAt(selectedRow, 3).toString();
        String emailCu = tableModel.getValueAt(selectedRow, 4).toString();
        
        // Dùng lại class Component để tạo Form cho đồng bộ
        String[] labels = {"Họ và Tên", "Số CCCD", "Số Điện Thoại", "Địa chỉ Email"};
        JTextField[] fields = new JTextField[labels.length];
        
        fields[0] = new JTextField(tenCu);
        fields[1] = new JTextField(cccdCu);
        fields[2] = new JTextField(sdtCu);
        fields[3] = new JTextField(emailCu);

        JButton btnCancel = new JButton("Hủy bỏ");
        JButton btnUpdate = new JButton("Cập Nhật");

        JDialog dialog = component.createDinamicForm(
            "Sửa Thông Tin", "Mã KH: " + maCu, 
            "Cập nhật thông tin khách hàng", 
            labels, fields, new JButton[]{btnCancel, btnUpdate}
        );
        
        btnCancel.addActionListener(ev -> dialog.dispose());

        btnUpdate.addActionListener(ev -> {
            String tenMoi = fields[0].getText().trim();
            String cccdMoi = fields[1].getText().trim();
            String sdtMoi = fields[2].getText().trim();
            String emailMoi = fields[3].getText().trim();

            if (tenMoi.isEmpty() || sdtMoi.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tên và SĐT không được để trống!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tạo đối tượng KhachHang với thông tin mới (Ngày ĐK giữ null, DB không đổi)
            KhachHang khUpdate = new KhachHang(maCu, tenMoi, cccdMoi, sdtMoi, emailMoi, null);

            // Đẩy xuống Controller
            if (controller != null && controller.capNhatKhachHang(khUpdate)) {
                // Cập nhật lại Table giao diện
                tableModel.setValueAt(tenMoi, selectedRow, 1);
                tableModel.setValueAt(cccdMoi, selectedRow, 2);
                tableModel.setValueAt(sdtMoi, selectedRow, 3);
                tableModel.setValueAt(emailMoi, selectedRow, 4);
                
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.setVisible(true);
    }

    // ----------------------------------------------------
    // Tra cứu (Tìm kiếm) khách hàng
    private void traCuuKhachHang() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập Tên, CCCD hoặc SĐT cần tìm:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (controller != null) {
                controller.timKiemKhachHang(keyword.trim());
            }
        } else if (keyword != null && keyword.trim().isEmpty()) {
            // Nếu để trống và ấn OK -> Load lại toàn bộ
            if (controller != null) {
                controller.loadDataToTable();
            }
        }
    }
    

   
}