package game;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class About {
	private Scene scene;
	private Stage stage;
	private About menu;

	public About(Stage stage, MainMenu menu) {
		this.menu = this; // for use inside convenience method
		this.stage = stage;

		stage.setTitle("About"); // set title, icon, make unresizable

		Image icon = new Image("assets/cheese.png");
		stage.getIcons().add(icon);
		stage.setResizable(false);

		// declares the background img
		Image bg = new Image("assets/about_bg.png", GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT, false, false);
		ImageView viewbg = new ImageView();
		viewbg.setImage(bg);

		StackPane root = new StackPane(); // root
		VBox vbox = new VBox(); // vbox for buttons

		// sets the button (play) background
		Image menuImg = new Image("assets/mainmenu.png");
		ImageView menuImgView = new ImageView(menuImg);
		menuImgView.setFitHeight(75);
		menuImgView.setPreserveRatio(true);
		Button menuButton = new Button();
		menuButton.setTranslateX(0);
		menuButton.setTranslateY(0);
		menuButton.setBackground(null);
		menuButton.setGraphic(menuImgView);
		
		menuButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
	           @Override
	            public void handle(MouseEvent e) {
	        	   menu.setStage();
	            }
	    	}
		);
		
		vbox.getChildren().addAll(menuButton);
		root.getChildren().addAll(viewbg, vbox);
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
	}

	// setter
	void setStage() {
		this.stage.setScene(this.scene);
	}

	// getter
	public Scene getScene() {
		return this.scene;
	}

	// getter
	Stage getStage() {
		return this.stage;
	}
}
