//package test;
//
//import com.github.sarxos.webcam.Webcam;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.NotFoundException;
//import com.google.zxing.Result;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//import com.google.zxing.common.HybridBinarizer;
//
//import java.awt.image.BufferedImage;
//
//public class QRScannerTest {
//
//    public static void main(String[] args) {
//
//        try {
//
//            // Lấy webcam mặc định
//            Webcam webcam = Webcam.getDefault();
//
//            if (webcam == null) {
//                System.out.println("Không tìm thấy webcam!");
//                return;
//            }
//
//            webcam.open();
//
//            System.out.println("=== ĐANG QUÉT QR ===");
//
//            while (true) {
//
//                BufferedImage image = webcam.getImage();
//
//                if (image == null) {
//                    continue;
//                }
//
//                BinaryBitmap bitmap = new BinaryBitmap(
//                        new HybridBinarizer(
//                                new BufferedImageLuminanceSource(image)
//                        )
//                );
//
//                try {
//
//                    Result result = new MultiFormatReader().decode(bitmap);
//
//                    System.out.println("Đã quét được QR:");
//                    System.out.println(result.getText());
//
//                    break;
//
//                } catch (NotFoundException e) {
//
//                    // chưa thấy QR
//                }
//
//                Thread.sleep(100);
//            }
//
//            webcam.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}