package model;

import java.awt.image.BufferedImage;

public class ExamPage {
	private int numeroPage;
	private BufferedImage pageImage;
	private BufferedImage qrCodeImage;
	private int qrCodeX;
	private int qrCodeY;
	private float qrCodeSize;
	private float imageRatio;
	
	
	public ExamPage(int numeroPage, BufferedImage pageImage) {
		this.numeroPage=numeroPage;
		this.pageImage=pageImage;
		this.qrCodeSize=7;
		this.qrCodeX=0;
		this.qrCodeY=0;
		this.imageRatio = Math.min((float) ( (float)pageImage.getWidth() / (float) pageImage.getHeight()), (float) ( (float) pageImage.getHeight() / (float) pageImage.getWidth()));

	}
	
	public int getNumeroPage() {
		return numeroPage;
	}
	
	public void setNumeroPage(int numeroPage) {
		this.numeroPage = numeroPage;
	}
	
	public BufferedImage getPageImage() {
		return pageImage;
	}
	public void setPageImage(BufferedImage pageImage) {
		this.pageImage = pageImage;
	}
	
	public BufferedImage getQrCodeImage() {
		return qrCodeImage;
	}
	
	public void setQrCodeImage(BufferedImage qrCodeImage) {
		this.qrCodeImage = qrCodeImage;
	}
	public int getQrCodeX() {
		return qrCodeX;
	}
	
	public int getQrCodeY() {
		return qrCodeY;
	}
	
	public float getQrCodeSize() {
		return qrCodeSize;
	}
	
	public void setQrCodeX(int qrCodeX) {
		//Min 0% of page Width
		if(qrCodeX<0) {
			this.qrCodeX = 0;
		}
		//Max 100% of page Width
		else if(qrCodeX>(100-this.qrCodeSize)){
			this.qrCodeX = (int) (100-this.qrCodeSize);
		}
		else{
			this.qrCodeX = qrCodeX;
		}
		
	}
	
	
	public void setQrCodeY(int qrCodeY) {
		//Min 0% of page Width
		if(qrCodeY<0) {
			this.qrCodeY = 0;
		}
		//Max 100% of page Width
		else if(qrCodeY>(100-(int) ((float) this.qrCodeSize * this.imageRatio))){
			
			this.qrCodeY = (100-(int) ((float) this.qrCodeSize * this.imageRatio));
		}
		else {
			this.qrCodeY = qrCodeY;
		}
		
	}
	
	public void setQrCodeXY(int qrCodeX,int qrCodeY) {
		this.setQrCodeX(qrCodeX);
		this.setQrCodeY(qrCodeY);
	}
	
	
	public void setQrCodeSize(float qrCodeSize) {
		//Min 5% of page Width
		if(qrCodeSize<7) {
			this.qrCodeSize = 7;
		}
		//Max 15% of page Width
		else if(qrCodeSize>15){
			this.qrCodeSize = 15;
		}
		else {
			this.qrCodeSize = qrCodeSize;
		}
	}
	
	public Boolean isValid() {
		
		//Min 7% of page Width
		if(this.qrCodeSize<7) {
			return false;
		}
		//Max 15% of page Width
		else if(this.qrCodeSize>15){
			return false;
		}
		if(this.qrCodeX<0) {
			return false;
		}
		//Max 100% of page Width - QrCode Size
		else if(qrCodeX>(100-this.qrCodeSize)){
			return false;
		}
		if(qrCodeY<0) {
			this.qrCodeY = 0;
		}
		//Max 100% of page Height - QrCode Size
		else if(qrCodeY>(100-this.qrCodeSize)){
			return false;
		}
		return true;

	}
	
	
	
}
