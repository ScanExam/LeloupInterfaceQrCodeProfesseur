package view.javaFX;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.event.ChangeListener;

import com.google.zxing.WriterException;

import controller.ControllerExamEditor;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ExamPage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.Node;

public class JFXViewController extends Application {
	public ControllerExamEditor controller = new ControllerExamEditor(this);


	private FileChooser fileChooser = new FileChooser();
	public Stage examEditionStage;
	
	@FXML
	public ImageView imageExam;
	@FXML
	public Pane imagePane;
	@FXML
	public ListView pdfPagesPreviews;
	@FXML
	public TextField textFieldQrCodeX;
	@FXML
	public TextField textFieldQrCodeY;
	@FXML
	public TextField textFieldQrCodeSize;
	@FXML
	public Button coordButtonValidation;
	@FXML
	public ImageView qrCodeDemo = new ImageView();;
	
	

	

	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(Stage examEditionStage) {
		try {
			
			this.examEditionStage = examEditionStage;
			Parent root = FXMLLoader.load(getClass().getResource("ExamEditorDesign.fxml"));

			Scene scene = new Scene(root, 1280, 720);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			examEditionStage.setMaximized(true);
			examEditionStage.setScene(scene);
			examEditionStage.show();
			

		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	

	
	@FXML
	public void addFile(Event event) {
		System.out.println("Open file");
        this.controller.loadPDFExam(fileChooser.showOpenDialog(examEditionStage));
		//this.controller.loadPDFExam(new File("C:\\Users\\alexl\\Documents\\FAC\\M1\\Projet\\PDF\\bacS.pdf"));
	}
	
	//Set Image of the exam
	@FXML
	public void setImageExam(BufferedImage img) {
		
		imageExam.fitWidthProperty().bind(imagePane.widthProperty());
		imageExam.fitHeightProperty().bind(imagePane.heightProperty());

		
		imageExam.setImage(bufferedImageToImage(img));

	}
	


	
	
	//Set pictures of pages in LisstView after file loading
		@FXML
		public void setPdfPreviewListView2(TreeMap<String, ExamPage> listOfPagesWithImg) {
			ObservableList<String> items = FXCollections.observableArrayList ();
		    
			
			//Numeros de page
			for (Map.Entry mapentry : listOfPagesWithImg.entrySet()) {
				items.add(mapentry.getKey().toString());
		    }

		    
		    pdfPagesPreviews.setItems(items);
		    
		    pdfPagesPreviews.setCellFactory(param -> new ListCell<String>() {
	            private ImageView imageView = new ImageView();
	            @Override
	            public void updateItem(String name, boolean empty) {
	                super.updateItem(name, empty);
	                if (empty) {
	                    setText(null);
	                    setGraphic(null);
	                } else {
	                    imageView.setImage(bufferedImageToImage(listOfPagesWithImg.get(name).getPageImage()));
	                    imageView.fitWidthProperty().bind(pdfPagesPreviews.widthProperty());
	                    imageView.fitHeightProperty().bind(pdfPagesPreviews.heightProperty());
	                    imageView.setPreserveRatio(true);
	                    
	                    setText(name);
	                    
	                    setGraphic(imageView);
	                }
	            }
	        });
	        
		}
		
		
	
	
		
	@FXML
	public void initQrCode(float size, int x, int y,BufferedImage qrCodeImage) {
		qrCodeDemo.setImage(this.bufferedImageToImage(qrCodeImage));
		imagePane.getChildren().addAll(qrCodeDemo);


		float newSize = (float) (size/100*this.getImageViewWidth());
		qrCodeDemo.setFitHeight(newSize);
		qrCodeDemo.setFitWidth(newSize);
		
		qrCodeDemo.setX(x);
		qrCodeDemo.setY(y);
		qrCodeDemo.setPickOnBounds(true);
		qrCodeDemo.setPreserveRatio(true);

	    

	}
	
	
	@FXML
	public void updateQrCode(float size, int x, int y) {

		qrCodeDemo.setFitHeight(size);
		qrCodeDemo.setFitWidth(size);
		qrCodeDemo.setX(x);
		qrCodeDemo.setY(y);

	}
	
	//Get page clicked in ListView
	@FXML
    public void mousePressedListView(MouseEvent event) {
		System.out.println("clicked on " + pdfPagesPreviews.getSelectionModel().getSelectedItem());
		controller.setExamPage(pdfPagesPreviews.getSelectionModel().getSelectedItem().toString());
		controller.updateQRCode(this.getImageViewWidth(),this.getImageViewHeight());
		
		
    }
	
	@FXML
	public void mousePressedExamImage(MouseEvent e) {

		double aspectRatio = imageExam.getImage().getWidth() / imageExam.getImage().getHeight();
		double realWidth = Math.min(imageExam.getFitWidth(), imageExam.getFitHeight() * aspectRatio);
		double realHeight = Math.min(imageExam.getFitHeight(), imageExam.getFitWidth() / aspectRatio);
		
		
		int percentX = (int) (100*e.getX()/realWidth);
		int percentY = (int) (100*e.getY()/realHeight);
		
		System.out.println("\n JAVAFX clic imageExam :"+ e.getX()+"x - "+e.getY()+"y");
		System.out.println("\n JAVAFX "+realHeight);
		System.out.println("\n JAVAFX "+realWidth);
		
		
		System.out.println("\n JAVAFX "+percentX+" % en X ET "+ percentY+" % en Y");
		
		
		controller.setQrCode(percentX, percentY, this.getImageViewWidth(), this.getImageViewHeight());
		
		
	}
	
	
	@FXML
	public void validCoords(Event event) {
		System.out.println("JAVAFX clic on button for coord change");
		
		controller.setQrCode(textFieldQrCodeX.getText(),textFieldQrCodeY.getText(),textFieldQrCodeSize.getText(), this.getImageViewWidth(), this.getImageViewHeight());
	}
	
	@FXML
	public void setCoords(String x, String y, String size) {
		System.out.println("JAVAFX set coords "+x+" X and "+y+" Y, with size :"+size);
		textFieldQrCodeX.setText(x+"%");
		textFieldQrCodeY.setText(y+"%");
		textFieldQrCodeSize.setText(size+"%");
	}
	
	@FXML
	public void resetCoords(Event event) {
		System.out.println("JAVAFX reset coords");
		controller.resetCoords();
	}
        
	

	private Image bufferedImageToImage(BufferedImage img) {
		return SwingFXUtils.toFXImage(img, null);
	}
	
	@FXML
	public double getImageViewWidth() {
		//Check if an Image is set to avoid bug
		if(imageExam.getImage()==null) {
			return 0;
		}
		else {
			double aspectRatio = imageExam.getImage().getWidth() / imageExam.getImage().getHeight();
			return Math.min(imageExam.getFitWidth(), imageExam.getFitHeight() * aspectRatio);
		}
		
	}
	
	@FXML
	public double getImageViewHeight() {
		//Check if an Image is set to avoid bug
		if(imageExam.getImage()==null) {
			return 0;
		}
		else {
			double aspectRatio = imageExam.getImage().getWidth() / imageExam.getImage().getHeight();
			return Math.min(imageExam.getFitHeight(), imageExam.getFitWidth() / aspectRatio);
		}
		
	}
	
	@FXML
	public void zoomOnImage(Event event) {
		if (event instanceof ScrollEvent) {
			ScrollEvent scrollevent = (ScrollEvent) event;
			Object source = scrollevent.getSource();
			double dirY = scrollevent.getDeltaY();
			Node node = (Node) source;
			if (dirY > 0) {
				System.out.println("JAVAFX zoom on ImageExam");
				controller.zoom(true, this.getImageViewWidth(), this.getImageViewHeight());
			} else {
				System.out.println("JAVAFX dezoom on ImageExam");
				controller.zoom(false, this.getImageViewWidth(), this.getImageViewHeight());
			}
		}
	}
	
	@FXML
	public void saveAsPDF() {
		System.out.println("JAVAFX save exam as PDF");
		try {
			controller.examExportation(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@FXML
	public void print() {
		System.out.println("JAVAFX Print");
		try {
			controller.examExportation(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@FXML
	public void advancedPrint() {
		System.out.println("JAVAFX Advanced Print");
		try {
			controller.examExportation(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
}
	
	


