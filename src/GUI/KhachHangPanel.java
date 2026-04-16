package GUI;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class KhachHangPanel extends JPanel {
	 private String[] khachHangOptions = {
		        "Thêm khách hàng",
		        "Xóa khách hàng",
		        "Sửa thông tin", 
		        "Tra cứu khách hàng"
		    };
    private JTable table;
    private DefaultTableModel tableModel;

    public KhachHangPanel() {
        // 1. Setup Layout tổng thể y hệt DashboardPanel
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Màu nền tổng thể xám xanh nhạt

        // 2. Phần Header (Tiêu đề và Mô tả)
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setBackground(new Color(245, 247, 250)); // Đồng bộ màu nền

        JLabel title = new JLabel("Quản lí khách hàng");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Quản lí thông tin khách hàng sử dụng dịch vụ metro");
        sub.setForeground(Color.GRAY);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(sub);
        
        add(header, BorderLayout.NORTH);

        // 3. Phần Main chứa Table (Card giao diện)
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        main.add(tableCard, BorderLayout.CENTER);
        
        JPanel t = new JPanel();
        
        
        List<JMenuItem> listit = getMenuOption();
        for(JMenuItem item: listit) {
        	t.add(item);
        }
        
        
        
        add(main, BorderLayout.CENTER);
        add(t, BorderLayout.WEST);
        
       
        
        // 4. Load dữ liệu giả để test giao diện
        loadMockData();
    }

    // Hàm thêm dữ liệu mẫu (Sau này bạn xóa hàm này đi và load từ DB lên)
    private void loadMockData() {
        tableModel.addRow(new Object[]{"KH001", "Nguyễn Văn A", "079090123456", "0901234567", "nguyenvana@email.com", "2024-05-01"});
        tableModel.addRow(new Object[]{"KH002", "Trần Thị B", "079090123457", "0912345678", "tranthib@email.com", "2024-05-02"});
        tableModel.addRow(new Object[]{"KH003", "Lê Văn C", "079090123458", "0923456789", "levanc@email.com", "2024-05-03"});
        tableModel.addRow(new Object[]{"KH004", "Phạm Thị D", "079090123459", "0934567890", "phamthid@email.com", "2024-05-04"});
        tableModel.addRow(new Object[]{"KH005", "Hoàng Văn E", "079090123460", "0945678901", "hoangvane@email.com", "2024-05-05"});
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
    
    
 // Thêm khách hàng
    private void themKhachHang() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm khách hàng");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());
        
        JLabel lbMa = new JLabel("Mã KH:");
        JTextField txtMa = new JTextField(20);
        
        JLabel lbTen = new JLabel("Họ tên:");
        JTextField txtTen = new JTextField(20);
        
        JLabel lbCccd = new JLabel("CCCD:");
        JTextField txtCccd = new JTextField(20);
        
        JLabel lbSdt = new JLabel("SĐT:");
        JTextField txtSdt = new JTextField(20);
        
        JLabel lbEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField(20);
        
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        
        dialog.add(lbMa);
        dialog.add(txtMa);
        dialog.add(lbTen);
        dialog.add(txtTen);
        dialog.add(lbCccd);
        dialog.add(txtCccd);
        dialog.add(lbSdt);
        dialog.add(txtSdt);
        dialog.add(lbEmail);
        dialog.add(txtEmail);
        dialog.add(btnSave);
        dialog.add(btnCancel);
        
        btnSave.addActionListener(ev -> {
            String ma = txtMa.getText();
            String ten = txtTen.getText();
            String cccd = txtCccd.getText();
            String sdt = txtSdt.getText();
            String email = txtEmail.getText();
            String ngayDK = java.time.LocalDate.now().toString();
            
            tableModel.addRow(new Object[]{ma, ten, cccd, sdt, email, ngayDK});
            JOptionPane.showMessageDialog(dialog, "Thêm thành công!");
            dialog.dispose();
        });
        
        btnCancel.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Xóa khách hàng
    private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    // Sửa khách hàng
    private void suaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        
        // Lấy dữ liệu cũ
        String maCu = tableModel.getValueAt(selectedRow, 0).toString();
        String tenCu = tableModel.getValueAt(selectedRow, 1).toString();
        String cccdCu = tableModel.getValueAt(selectedRow, 2).toString();
        String sdtCu = tableModel.getValueAt(selectedRow, 3).toString();
        String emailCu = tableModel.getValueAt(selectedRow, 4).toString();
        
        // Tạo dialog sửa
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa khách hàng");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());
        
        JLabel lbMa = new JLabel("Mã KH:");
        JTextField txtMa = new JTextField(maCu, 20);
        txtMa.setEditable(false);
        
        JLabel lbTen = new JLabel("Họ tên:");
        JTextField txtTen = new JTextField(tenCu, 20);
        
        JLabel lbCccd = new JLabel("CCCD:");
        JTextField txtCccd = new JTextField(cccdCu, 20);
        
        JLabel lbSdt = new JLabel("SĐT:");
        JTextField txtSdt = new JTextField(sdtCu, 20);
        
        JLabel lbEmail = new JLabel("Email:");
        JTextField txtEmail = new JTextField(emailCu, 20);
        
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnCancel = new JButton("Hủy");
        
        dialog.add(lbMa);
        dialog.add(txtMa);
        dialog.add(lbTen);
        dialog.add(txtTen);
        dialog.add(lbCccd);
        dialog.add(txtCccd);
        dialog.add(lbSdt);
        dialog.add(txtSdt);
        dialog.add(lbEmail);
        dialog.add(txtEmail);
        dialog.add(btnUpdate);
        dialog.add(btnCancel);
        
        btnUpdate.addActionListener(ev -> {
            tableModel.setValueAt(txtTen.getText(), selectedRow, 1);
            tableModel.setValueAt(txtCccd.getText(), selectedRow, 2);
            tableModel.setValueAt(txtSdt.getText(), selectedRow, 3);
            tableModel.setValueAt(txtEmail.getText(), selectedRow, 4);
            
            JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
            dialog.dispose();
        });
        
        btnCancel.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Tra cứu khách hàng
    private void traCuuKhachHang() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập tên hoặc SĐT cần tìm:");
        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }
        
        // Xóa hết dữ liệu cũ
        tableModel.setRowCount(0);
        
        // Tìm kiếm và hiển thị kết quả
        // TODO: Gọi database tìm kiếm
        // Tạm thời tìm trong mock data
        if (keyword.contains("A") || keyword.contains("1")) {
            loadMockData(); // Load lại toàn bộ nếu tìm thấy
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
            loadMockData(); // Load lại toàn bộ
        }
    }
}