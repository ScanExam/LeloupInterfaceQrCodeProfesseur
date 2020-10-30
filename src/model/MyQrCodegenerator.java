package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.google.zxing.EncodeHintType;

public class MyQrCodegenerator {

	/**
	 * Generate a Qr Code
	 * @param text to put in
	 * @param width of the QrCode
	 * @param height of the QrCode
	 * @return the BufferedImage of the QrCode
	 * @throws WriterException
	 * @throws IOException
	 */
	public BufferedImage generateQRCodeBufferedImage(String text, int width, int height) throws WriterException, IOException {
        
		
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(1);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, 0);
		
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        
        
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
        

    }

}
