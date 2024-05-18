package game;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
	private String ip;
	private String thisIp;
	private About aboutStage;

	public MainMenu(Stage stage) {
		this.menu = this; // for use inside convenience method
		this.stage = stage;
		this.aboutStage = new About(this.stage, this);
		
		try {
            thisIp = InetAddress.getLocalHost().getHostAddress();
            // System.out.println("Your IP address: " + thisIp.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

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
		Image playImg = new Image("assets/play.png");
		ImageView playImgView = new ImageView(playImg);
		playImgView.setFitHeight(100);
		playImgView.setPreserveRatio(true);
		Button play = new Button();
		play.setTranslateX(0);
		play.setTranslateY(240);
		play.setBackground(null);
		play.setGraphic(playImgView);
		
		// sets the button (play) background
		Image aboutImg = new Image("assets/about.png");
		ImageView aboutImgView = new ImageView(aboutImg);
		aboutImgView.setFitHeight(100);
		aboutImgView.setPreserveRatio(true);
		Button about = new Button();
		about.setTranslateX(0);
		about.setTranslateY(240);
		about.setBackground(null);
		about.setGraphic(aboutImgView);
		
		// sets the button (play) background
		Image exitImg = new Image("assets/exit.png");
		ImageView exitImgView = new ImageView(exitImg);
		exitImgView.setFitHeight(100);
		exitImgView.setPreserveRatio(true);
		Button exit = new Button();
		exit.setTranslateX(0);
		exit.setTranslateY(240);
		exit.setBackground(null);
		exit.setGraphic(exitImgView);
		
        ComboBox<String> comboBox = new ComboBox<>();

        // Add items to the ComboBox
        comboBox.getItems().addAll(
            "Client",
            "Server"
        );

        // Set default value
        comboBox.setValue("Client");
        comboBox.setTranslateX(350);
        comboBox.setTranslateY(-630);
        comboBox.setStyle("-fx-font-size: 14px; -fx-border-color: lightblue; -fx-border-width: 2px; -fx-background-color: white;");

		// add input field for player name
		TextField playerName = new TextField();
		playerName.setPromptText("Player Name");
		playerName.setMaxWidth(200);
		playerName.setTranslateX(-478);
		playerName.setTranslateY(-580);
		playerName.setStyle("-fx-font-size: 14px; -fx-border-color: lightblue; -fx-border-width: 2px; -fx-background-color: white;");

		// on input of player name, set the player name
		playerName.setOnKeyReleased(e -> {
			this.playerName = playerName.getText();
		});
		
		// add input field for player name
		TextField ipAddress = new TextField();
		ipAddress.setPromptText("IP Address of Server:");
		ipAddress.setMaxWidth(150);
		// position of the input field
		ipAddress.setTranslateX(500);
		ipAddress.setTranslateY(-531);
		ipAddress.setStyle("-fx-font-size: 14px; -fx-border-color: lightblue; -fx-border-width: 2px; -fx-background-color: white;");

		ipAddress.setOnKeyReleased(e -> {
			ip = ipAddress.getText();
		});
		
		comboBox.setOnAction(event -> {
            String selectedValue = comboBox.getValue();
            if (selectedValue.equals("Server")) {
            	ipAddress.setText(thisIp);
            	ipAddress.setEditable(false);
            } else if (selectedValue.equals("Client")) {
            	ipAddress.setEditable(true);
            	ipAddress.clear();
            }
        });

		this.setMouseHandler(play, 1, comboBox, ip);
		this.setMouseHandler(about, 2, comboBox, ip);
		this.setMouseHandler(exit, 3, comboBox, ip);
		vbox.getChildren().addAll(play, about, exit, ipAddress, playerName, comboBox);
		root.getChildren().addAll(viewbg, vbox);
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
	}

	private void setMouseHandler(Button b, int num, ComboBox<String> comboBox, String ipAddress) { // set event handler
		b.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				switch(num) { // based on num, branch to a different scene
				case 1: // new game
					boolean isServer = comboBox.getValue() == "Server" ? true : false;
					GameStage theGameStage = new GameStage(menu, stage, playerName, isServer, ipAddress);
                	stage.setScene(theGameStage.getScene());
                	break;
				case 2:
					stage.setScene(aboutStage.getScene());
					break;
				case 3: // exit
					System.exit(0);
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
