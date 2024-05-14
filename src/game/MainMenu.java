package game;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {
	private Scene scene;
	private Stage stage;
	private MainMenu menu;

	public MainMenu(Stage stage) {
		this.menu = this; // for use inside convenience method
		this.stage = stage;

		stage.setTitle("Cheeseformice"); // set title, icon, make unresizable

		Image icon = new Image("assets/cheese.png");
		stage.getIcons().add(icon);

		stage.setResizable(false);

		// declares the background img
		Image bg = new Image("assets/menu_bg.png", GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT, false, false);
		ImageView viewbg = new ImageView();
		viewbg.setImage(bg);

		StackPane root = new StackPane(); // root

		VBox vbox = new VBox(); // vbox for buttons
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15);

        // sets the button (play) background
        Image img = new Image("assets/play.png");
        ImageView view = new ImageView(img);
        // sets the button (play) size
        view.setFitHeight(100);
        view.setPreserveRatio(true);
        // creates button (play)
        Button play = new Button();
        // position of the button (play)
        play.setTranslateX(0);
        play.setTranslateY(20);
        play.setBackground(null);
        play.setGraphic(view);
        this.setMouseHandler(play, 1);

		vbox.getChildren().addAll(play);
		root.getChildren().addAll(viewbg, vbox);
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
	}

	private void setMouseHandler(Button b, int num) { // set event handler
		b.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				switch(num) { // based on num, branch to a different scene
				case 1: // new game
					GameStage theGameStage = new GameStage(menu, 3, stage);
                	stage.setScene(theGameStage.getScene());
                	break;
				}
			}
		});
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
