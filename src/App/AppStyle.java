package App;

import java.awt.Color;
import java.awt.Font;

public class AppStyle {

    // ================= FONT =================
    private static final String FONT_NAME = "Segoe UI";

    public static final Font HEADER = new Font(FONT_NAME, Font.BOLD, 26);
    public static final Font SUB_HEADER = new Font(FONT_NAME, Font.BOLD, 18);
    public static final Font MENU = new Font(FONT_NAME, Font.PLAIN, 15);
    public static final Font DATA_NUMBER = new Font(FONT_NAME, Font.BOLD, 22);
    public static final Font TABLE_TEXT = new Font(FONT_NAME, Font.PLAIN, 14);
    public static final Font CAPTION = new Font(FONT_NAME, Font.PLAIN, 12);

    // ================= COLOR =================

    // 🎯 Màu chính (Primary - dùng cho button, highlight)
    public static final Color PRIMARY = new Color(59, 130, 246);   // #3B82F6

    // ✅ Thành công (Export, trạng thái OK)
    public static final Color SUCCESS = new Color(34, 197, 94);    // #22C55E

    // ⚠️ Cảnh báo
    public static final Color WARNING = new Color(245, 158, 11);   // #F59E0B

    // ❌ Lỗi
    public static final Color DANGER = new Color(239, 68, 68);     // #EF4444

    // 🧱 Background tổng
    public static final Color BACKGROUND = new Color(245, 247, 250); // #F5F7FA

    // 📦 Card nền trắng
    public static final Color CARD_BG = Color.WHITE;

    // 🧩 Viền card nhẹ
    public static final Color CARD_BORDER = new Color(229, 231, 235); // #E5E7EB

    // 🔤 Text chính
    public static final Color TEXT_PRIMARY = new Color(30, 41, 59);   // #1E293B

    // 🔤 Text phụ
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // #64748B

    // 📊 Table
    public static final Color TABLE_HEADER_BG = Color.WHITE;
    public static final Color TABLE_GRID = new Color(235, 235, 235);

    // 🎯 Selected row
    public static final Color TABLE_SELECTION_BG = PRIMARY;
    public static final Color TABLE_SELECTION_TEXT = Color.WHITE;
}