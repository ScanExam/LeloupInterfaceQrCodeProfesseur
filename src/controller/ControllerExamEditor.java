package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.google.zxing.WriterException;

import javafx.scene.image.Image;
import model.ExamPage;
import controller.ExamPrinter;
import model.PDFfile;
import view.javaFX.JFXViewController;

public class ControllerExamEditor {

	public PDFfile pdf;
	private JFXViewController jfxView;

	private TreeMap<String, ExamPage> examPages;
	
	private String currentPage="";
	
	Boolean test = true;

	/**
	 * 
	 * @param jfxView is the JFXView
	 */
	public ControllerExamEditor(JFXViewController jfxView){
		this.jfxView = jfxView;
    }
	
	/**
	 * Load a PDF file from system
	 * @param file is the file chosen
	 */
	public void loadPDFExam(File file) {
		//If no file has been chosen
		if (file == null) {
			System.out.println("Aucun fichier selectionne");
        }
		else{
			this.examPages= new TreeMap<>();
			
			String fileName = file.getName();
		    int index = fileName.lastIndexOf('.');

		    //Check if the file  is a PDF file
		    if(index > 0 && fileName.substring(index + 1).equals("pdf")) {
		    	System.out.println("Fichier "+fileName+" chargé");
		    	pdf= new PDFfile(file);
		    	
		    	
		    	try {
		    		
		    		//Set current page to 0 (the first page) and load it
		    		this.currentPage="0";
					jfxView.setImageExam(this.pdf.getBufferedImageFromPDF(0));
					
					//Create new page settings for every pages
					for(int i=0;i<pdf.getNumberOfPages();i++) {
						this.examPages.put(""+i, new ExamPage(i,this.pdf.getBufferedImageFromPDF(i)));
			    	}
					
					jfxView.setCoords(examPages.get(this.currentPage).getQrCodeX()+"", examPages.get(currentPage).getQrCodeY()+"", examPages.get(currentPage).getQrCodeSize()+"");
					jfxView.setPdfPreviewListView2(this.examPages);
					jfxView.initQrCode(examPages.get(this.currentPage).getQrCodeSize(),0,0,ImageIO.read(new File("src/ressources/qrcodetest.png")));
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    else{
	        	System.out.println("Format non pris en charge");	
	        }
		}  
	}
	
	

	
	/**
	 * Set a new Exam page
	 * @param page is the desired page ("0","1",...)
	 */
	public void setExamPage(String page) {
		System.out.println("Set current page to :"+page);
		
		//Update current page
		this.currentPage=page;
		
		//Update View
		jfxView.setImageExam(this.examPages.get(page).getPageImage());
		jfxView.setCoords(examPages.get(currentPage).getQrCodeX()+"", examPages.get(currentPage).getQrCodeY()+"",examPages.get(currentPage).getQrCodeSize()+"");
	}
	
	/**
	 * Erase the coordinates written in the view
	 */
	public void resetCoords() {
		jfxView.setCoords(examPages.get(currentPage).getQrCodeX()+"", examPages.get(currentPage).getQrCodeY()+"",examPages.get(currentPage).getQrCodeSize()+"");
	}
	
	
	/**
	 * Update QR Code Image position in view
	 * @param imageWidth
	 * @param imageHeight
	 */
	public void updateQRCode(double imageWidth, double imageHeight) {
		
		int newCoordX = (int) ((double)examPages.get(currentPage).getQrCodeX()/100*imageWidth);
		int newCoordY = (int) ((double)examPages.get(currentPage).getQrCodeY()/100*imageHeight);
		
		int newSize = (int) ((double)examPages.get(currentPage).getQrCodeSize()/100*imageWidth);
		//Update QrCode in View
		jfxView.updateQrCode(newSize,newCoordX,newCoordY);
		jfxView.setCoords(examPages.get(currentPage).getQrCodeX()+"", examPages.get(currentPage).getQrCodeY()+"", examPages.get(currentPage).getQrCodeSize()+"");
	}

	
	/**
	 * Set a new position for the QrCode
	 * @param percentPositionX is the desired X position of the qr code in percent 
	 * @param percentPositionY is the desired Y position of the qr code in percent
	 * @param size is the desired size of the qr code in percent in relation to the width
	 * @param imageWidth it is a benchmark to know the size of the Qr Code in relation to the Width of the PDf page.
	 * @param imageHeight it is a benchmark to know the size of the Qr Code in relation to the Height of the PDf page.
	 */
	public void setQrCode(String percentPositionX, String percentPositionY, String size, double imageWidth, double imageHeight) {

	  //Extract Integer from input fields
	  int intXPercent=Integer.parseInt("0"+percentPositionX.replaceAll("[^0-9]", "")); 
	  int intYPercent=Integer.parseInt("0"+percentPositionY.replaceAll("[^0-9]", "")); 
	  int intClearSize=Integer.parseInt("0"+size.replaceAll("[^0-9]", "")); 

	  this.setQrCode(intXPercent, intYPercent, imageWidth, imageHeight, intClearSize);
	  
	  //BUG :
	  //If 15.5 is set in textField, after treatment 15.5 -> 155, but user want 15.5
	  //So /10
	  //Find method???
	}
	
	/**
	 * Set a new position for the QrCode
	 * @param percentPositionX is the desired X position of the qr code in percent 
	 * @param percentPositionY is the desired Y position of the qr code in percent
	 * @param imageWidth it is a benchmark to know the size of the Qr Code in relation to the Width of the PDf page.
	 * @param imageHeight it is a benchmark to know the size of the Qr Code in relation to the Height of the PDf page.
	 */
	public void setQrCode(int percentPositionX, int percentPositionY, double imageWidth, double imageHeight) {
	  this.setQrCode(percentPositionX, percentPositionY, imageWidth, imageHeight, examPages.get(currentPage).getQrCodeSize());
	}
	
	/**
	 * Set a new position for the QrCode
	 * @param percentPositionX is the desired X position of the qr code in percent 
	 * @param percentPositionY is the desired Y position of the qr code in percent
	 * @param imageWidth it is a benchmark to know the size of the Qr Code in relation to the Width of the PDf page.
	 * @param imageHeight it is a benchmark to know the size of the Qr Code in relation to the Height of the PDf page.
	 * @param size is the desired size of the qr code in percent in relation to the Width of the PDf page.
	 */
	public void setQrCode(int percentPositionX, int percentPositionY, double imageWidth, double imageHeight, float size) {

		//Check if a file is loaded
		if(this.currentPage=="") {
			jfxView.setCoords("0", "0", "0");
			return;
		}
	  
		  //Update coords and size in representation
		  examPages.get(this.currentPage).setQrCodeXY(percentPositionX, percentPositionY);
		  examPages.get(this.currentPage).setQrCodeSize(size);
		  
		  this.updateQRCode(imageWidth, imageHeight);

	  
	}
	
	/**
	 * Update QrCode size when user scroll
	 * @param zoom True if zoom, False if dezoom
	 * @param imageWidth it is a benchmark to know the size of the Qr Code in relation to the Width of the PDf page.
	 * @param imageHeight it is a benchmark to know the size of the Qr Code in relation to the Height of the PDf page.
	 */
	public void zoom(Boolean zoom, double imageWidth, double imageHeight) {
		if(zoom) {
			examPages.get(currentPage).setQrCodeSize((float) (examPages.get(currentPage).getQrCodeSize()+0.5));
		}
		else {
			examPages.get(currentPage).setQrCodeSize((float) (examPages.get(currentPage).getQrCodeSize()-0.5));
		}
		this.updateQRCode(imageWidth,imageHeight);
		jfxView.setCoords(examPages.get(currentPage).getQrCodeX()+"", examPages.get(currentPage).getQrCodeY()+"", examPages.get(currentPage).getQrCodeSize()+"");
	}
	
	
	/**
	 * Allows the export of the file
	 * @param mode, 0 -> save as PDf File, 1 -> classic print, 2 -> advanced print
	 * @throws WriterException
	 * @throws Exception
	 */
	public void examExportation(int mode) throws WriterException, Exception {
		if(this.currentPage=="") {
			System.out.println("Load an exam to print it");
			return;
		}
		
		ExamPrinter printer = new ExamPrinter(examPages, pdf, "test", mode);
		
		if(printer.print()) {
			System.out.println("Impression Succes");
		}
		else {
			System.out.println("Impression Erreur");
		}
	}
	
	
	
}
