package game;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
	private String playerName;
	private int portNum;

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
		
        ComboBox<String> comboBox = new ComboBox<>();

        // Add items to the ComboBox
        comboBox.getItems().addAll(
            "Client",
            "Server"
        );

        // Set default value
        comboBox.setValue("Client");
        comboBox.setTranslateX(530);
        comboBox.setTranslateY(-500);
        comboBox.setStyle("-fx-font-size: 14px; -fx-border-color: lightblue; -fx-border-width: 2px; -fx-background-color: white;");

		// add input field for player name
		TextField playerName = new TextField();
		playerName.setPromptText("Enter your name");
		playerName.setMaxWidth(200);
		// position of the input field
		playerName.setTranslateX(0);
		playerName.setTranslateY(20);

		// on input of player name, set the player name
		playerName.setOnKeyReleased(e -> {
			this.playerName = playerName.getText();
		});
		
		// add input field for player name
		TextField port = new TextField();
		port.setPromptText("Port");
		port.setMaxWidth(100);
		// position of the input field
		port.setTranslateX(400);
		port.setTranslateY(-405);
		port.setStyle("-fx-font-size: 14px; -fx-border-color: lightblue; -fx-border-width: 2px; -fx-background-color: white;");

		// on input of player name, set the player name
		port.setOnKeyReleased(e -> {
			this.portNum = Integer.parseInt(port.getText());
		});

		this.setMouseHandler(play, 1, comboBox);
		// add button to vbox
		vbox.getChildren().addAll(play, port, playerName, comboBox);
		root.getChildren().addAll(viewbg, vbox);
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
	}

	private void setMouseHandler(Button b, int num, ComboBox<String> comboBox) { // set event handler
		b.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				switch(num) { // based on num, branch to a different scene
				case 1: // new game
					boolean isServer = comboBox.getValue() == "Server" ? true : false;
					GameStage theGameStage = new GameStage(menu, 1, stage, playerName, isServer);
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
