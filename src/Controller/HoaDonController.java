package Controller;

import DAO.HoaDonDAO;
import Entity.HoaDon;
import GUI.HoaDonPanel;
import java.util.List;

public class HoaDonController {
    private HoaDonPanel view;
    private HoaDonDAO model;

    public HoaDonController(HoaDonPanel view) {
        this.view = view;
        this.model = new HoaDonDAO();
        lamMoiDuLieu();
    }

    // Cập nhật bảng hóa đơn và các Card thống kê
    public void lamMoiDuLieu() {
        List<HoaDon> ds = model.getAll();
        view.setData(ds);
    }

    // Logic xử lý khi nhấn nút In Vé QR
    public void xuatHoaDonPDF(String maHD) {
        HoaDon hd = model.findById(maHD);
        if (hd != null) {
            // Gọi hàm xuất PDF đã viết trong View hoặc tách ra lớp Utils
            view.xuatVeQR(maHD);
        }
    }
}