package cn.edu.sjtu.se.dclab.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
 * 2016-01-23 10:40 AM
 *
 * @author changyi yuan
 */
public class QRCoder {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private int width = 300;
    private int height = 300;
    private String imgFormat = "jpg";
    private String encodeChar = "utf-8";

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImgFormat() {
        return imgFormat;
    }

    public void setImgFormat(String imgFormat) {
        this.imgFormat = imgFormat;
    }

    public String getEncodeChar() {
        return encodeChar;
    }

    public void setEncodeChar(String encodeChar) {
        this.encodeChar = encodeChar;
    }

    private BufferedImage toBufferedImage(String content) throws WriterException, UnsupportedEncodingException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, encodeChar);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
            }
        }

        return image;
    }

    public void writeToFile(String content, String imgFile) throws WriterException, IOException {
        File outputFile = new File(imgFile);
        BufferedImage image = toBufferedImage(content);
        ImageIO.write(image, imgFormat, outputFile);
    }

    public void writeToStream(String content, OutputStream stream) throws IOException, WriterException {
        BufferedImage image = toBufferedImage(content);
        ImageIO.write(image, imgFormat, stream);
    }

    public String readFromFile(String imgFile) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(new File(imgFile));
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, encodeChar);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    public static void main(String[] args) throws IOException, WriterException, NotFoundException {
        QRCoder coder = new QRCoder();
        coder.setWidth(600);
        coder.setHeight(600);
        String imgPath = "C:\\Users\\Public.dclab-PC\\Desktop\\odi\\QRCodeDemo\\test.jpg";
        coder.writeToFile("Hi~这是测试 http://baidu.com", imgPath);
        String result = coder.readFromFile(imgPath);
        System.out.println(result);
    }
}
