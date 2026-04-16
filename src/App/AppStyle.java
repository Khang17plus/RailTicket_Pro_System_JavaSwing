package App;

import java.awt.Font;

public class AppStyle {
	// Tên font nên dùng: "Segoe UI" (Mặc định Windows đẹp) hoặc "Inter" (Hiện đại)
    private static final String FONT_NAME = "Segoe UI";

    // 1. Tiêu đề lớn (H1)
    public static final Font HEADER = new Font(FONT_NAME, Font.BOLD, 26);
    
    // 2. Tiêu đề phụ/nhỏ (H2)
    public static final Font SUB_HEADER = new Font(FONT_NAME, Font.BOLD, 18);
    
    // 3. Chữ trong Menu Sidebar
    public static final Font MENU = new Font(FONT_NAME, Font.PLAIN, 15);
    
    // 4. Con số thống kê (Thường dùng Bold để nổi bật)
    public static final Font DATA_NUMBER = new Font(FONT_NAME, Font.BOLD, 22);
    
    // 5. Chữ trong bảng (Table Content)
    public static final Font TABLE_TEXT = new Font(FONT_NAME, Font.PLAIN, 14);
    
    // 6. Chú thích nhỏ (Caption)
    public static final Font CAPTION = new Font(FONT_NAME, Font.PLAIN, 12);
	
	
	

}
