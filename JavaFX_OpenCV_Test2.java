//package application;

import java.io.ByteArrayInputStream;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;

import javafx.geometry.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;


public class JavaFX_OpenCV_Test2 extends Application {
	
	VideoCapture camvideo;
	Image image;
	Image snapShot;
	
	Canvas canvas;
	GraphicsContext g2d;

	Canvas canvas_tmp;
	GraphicsContext g2d_tmp;

	AnimationTimer timer;
	
	@Override
	public void start(Stage primaryStage) {
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		opeCVInit();
		
		/* Canvas */
		this.canvas = new Canvas(700, 700);
		this.g2d = canvas.getGraphicsContext2D();
		
		/* Tmp Canvas */
		this.canvas_tmp = new Canvas(700, 700);
		this.g2d_tmp = canvas_tmp.getGraphicsContext2D();

		/* Two BUttons and HBox*/
		Button setSceneButton = new Button();
		setSceneButton.setText("Set a Scene");
		setSceneButton.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("Set a Scene");
	                timer.start();
	            }
	        });

		Button takeSceneButton = new Button();
		takeSceneButton.setText("Take a Scene");
		takeSceneButton.setOnAction(new EventHandler<ActionEvent>() {
	 
	            @Override
	            public void handle(ActionEvent event) {
	                System.out.println("Take a Scene");
	                takeSnap();
	                timer.stop();
	            }
	        });

		 HBox hbox = new HBox(8);
	     hbox.getChildren().addAll(setSceneButton, takeSceneButton);
	     
	     	/* a Pane and a Scene */
	        BorderPane root = new BorderPane();
	        root.setTop(hbox);
	        root.setMargin(hbox, new Insets(10, 20, 10, 50));
	        
	        root.setCenter(this.canvas);
	        root.setAlignment(this.canvas, Pos.TOP_LEFT);

	        root.setRight(this.canvas_tmp);

	        Scene scene = new Scene(root, 1400, 700);
	         
	        primaryStage.setTitle("Hello World!");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        
	        
	        timer = new AnimationTimer() {

	            Mat mat = new Mat();

	            @Override
	            public void handle(long now) {

	            	camvideo.read(mat);
	                image = mat2Image(mat);
	                g2d.drawImage(image, 0, 0);
	            }
	        };
	        
	        
/*	        
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
*/
	}
	
	private void takeSnap() {
		
		Mat mat = new Mat();
		
		camvideo.read(mat);
		snapShot = mat2Image(mat);
		g2d_tmp.drawImage(snapShot, 0, 0);
	}
	
	private Image mat2Image(Mat mat) {
		
	    
	    	MatOfByte byteMat = new MatOfByte();
	    	Imgcodecs.imencode(".png", mat, byteMat);
	    	return new Image(new ByteArrayInputStream(byteMat.toArray()));
	    
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
	private void opeCVInit() {
		
	    this.camvideo = new VideoCapture(0);
	    
	    if( !this.camvideo.isOpened() ) {
	    	this.camvideo.open(0);
	    }
	    if( !this.camvideo.isOpened() ) {
	    	System.out.printf("Failed to open Video%n");
	    }	
	}




}
