package util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Utility để sinh QR Code dưới dạng BufferedImage.
 */
public class QRCodeGenerator {

    /**
     * Sinh QR code từ text cho trước.
     *
     * @param text Nội dung mã QR (ví dụ: "nganhang://STK=0123456789;amount=10000")
     * @param size Kích thước hình (chiều rộng = chiều cao, px)
     * @return BufferedImage chứa mã QR
     * @throws WriterException nếu ZXing không thể tạo được QR
     */
    public static BufferedImage generate(String text, int size) throws WriterException {
        // Các hint: charset UTF-8, không viền nhiều (margin = 1)
        Map<EncodeHintType, Object> hints = Map.of(
            EncodeHintType.CHARACTER_SET, "UTF-8",
            EncodeHintType.MARGIN, 1
        );

        // Tạo BitMatrix (ma trận đen trắng)
        BitMatrix matrix = new MultiFormatWriter()
            .encode(text, BarcodeFormat.QR_CODE, size, size, hints);

        // Chuyển BitMatrix thành BufferedImage
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}

