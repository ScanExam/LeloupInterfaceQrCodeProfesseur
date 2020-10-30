package controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.zxing.WriterException;

import model.ExamPage;
import model.MyQrCodegenerator;
import model.PDFfile;


public class ExamPrinter {
	
	private TreeMap<String, ExamPage> examPages;
	private MyQrCodegenerator qrCodeGenerator;
	private PDFfile pdf;
	private String qrCodeData;
	private int mode;

	/**
	 * Define new ExamPrinter
	 * @param exam is a TreeMap which contains the list of the settings for pages
	 * @param pdf is a class that represents the original PDF file as well as all the methods used to create it.
	 * @param qrCodeData is the data to put in the QrCode
	 * @param mode 0=save as PDf, 1=print, 2=Print with more settings
	 */
	public ExamPrinter(TreeMap<String, ExamPage> exam, PDFfile pdf, String qrCodeData, int mode) {
		this.examPages=exam;
		this.qrCodeGenerator = new MyQrCodegenerator();
		this.pdf=pdf;
		this.qrCodeData = qrCodeData;
		this.mode = mode;
	}
	
	
	/**
	 * Check all settings
	 * @return True if settings are valid, else False
	 */
	private Boolean isValid() {
		
		Set<String> keys = examPages.keySet();
		for(String key: keys){
			if(!examPages.get(key).isValid())
			return false;
		}
		return true;
	}
	
	
	/**
	 * This method adds the qr codes to the pages of an exam according to the position desired by the user.
	 * @return if the method worked
	 * @throws WriterException
	 * @throws Exception
	 */
	public Boolean print() throws WriterException, Exception {
		if(!isValid()) {
			return false;
		}
		
		
		//Add QRCodes to pages
		//We go through the desired parameters for each page
		Set<String> keys = examPages.keySet();
		for(String key: keys){
			
			//Get settings
			int percentX = examPages.get(key).getQrCodeX();
			int percentY = examPages.get(key).getQrCodeY();
			float percentSize = examPages.get(key).getQrCodeSize();
			float pdfPageWidth = pdf.getPageWidth(examPages.get(key).getNumeroPage());
			float pdfPageHeight = pdf.getPageHeight(examPages.get(key).getNumeroPage());
			
			//Convert settings in percents to pixel value for the size of the PDF page
			float x = pdfPageWidth/100*(float)percentX;
			float y = pdfPageHeight- (pdfPageHeight/100*(float)percentY);
			float s = pdfPageWidth/100*percentSize;
	
			//Add the QrCode to the page
			pdf.addImageToPDF(examPages.get(key).getNumeroPage(), (int)x, (int)y, qrCodeGenerator.generateQRCodeBufferedImage(qrCodeData, (int)s, (int)s));
			
		}
		
		
		return true;
		
	}
	
	
}
