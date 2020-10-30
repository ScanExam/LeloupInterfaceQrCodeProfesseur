package model;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;


public final class Print{
	
    /**
     * print From File
     * @param file
     * @param withAttributesPrint
     * @throws IOException
     * @throws PrinterException
     */
    public void printFromFile(File file, boolean withAttributesPrint) throws IOException, PrinterException {
    	PDDocument document = PDDocument.load(file);
    	if(withAttributesPrint) {
    		printWithDialog(document);
    	}
    	else {
    		printWithDialogAndAttributes(document);
    	}
    	document.close();
    }
    
    /**
     * print From PDDocument
     * @param document
     * @param withAttributesPrint
     * @throws IOException
     * @throws PrinterException
     */
    public void printFromPDDocument(PDDocument document, boolean withAttributesPrint) throws IOException, PrinterException {
    	
    	if(withAttributesPrint) {
    		printWithDialog(document);
    	}
    	else {
    		printWithDialogAndAttributes(document);
    	}
    }




    /**
     * Prints with a print preview dialog.
     * @param document
     * @throws IOException
     * @throws PrinterException
     */
    private static void printWithDialog(PDDocument document) throws IOException, PrinterException{
        
    	PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        if (job.printDialog()){
            job.print();
        }
    }

   

    /**
     * Prints with a print preview dialog and custom PrintRequestAttribute values.
     * @param document
     * @throws IOException
     * @throws PrinterException
     */
    private static void printWithDialogAndAttributes(PDDocument document) throws IOException, PrinterException{
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();

        if (job.printDialog(attr)){
            job.print(attr);
        }
    }
}