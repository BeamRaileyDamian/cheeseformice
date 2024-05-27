package game;


import java.util.HashMap;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class GameOver {
	private Scene scene;
	private Stage stage;
	private MainMenu menu;
	private HashMap<String, Mouse> mice = new HashMap<String, Mouse>();
	private StackPane root = new StackPane();
	private int numOfMicePrinted =  0;

	public GameOver(Stage stage, MainMenu menu) {
		this.stage = stage;
		this.menu = menu;

		Image bg = new Image("assets/gameover.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, false, false);
		ImageView viewbg = new ImageView();
		viewbg.setImage(bg);

		// Add the background image to the root
		this.root.getChildren().addAll(viewbg);

		// Create a button to go back to the main menu
		Image backImg = new Image("assets/menu_button.png");
		ImageView backImgView = new ImageView(backImg);
		backImgView.setFitHeight(100);
		backImgView.setPreserveRatio(true);
		Button back = new Button();
		back.setTranslateX(0);
		back.setTranslateY(350);
		back.setBackground(null);
		back.setGraphic(backImgView);

		// set the action of the button
		back.setOnAction(e -> {
			this.menu.setStage();
		});

		this.root.getChildren().add(back);


		// Set the root as the root of the scene
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
	}

	// Method to add the list of mouse and their scores to the game over screen
	public void addMice(Mouse player) {

		HashMap<String, Mouse> miceClone = new HashMap<String, Mouse>(GameStage.mice);

		miceClone.put(player.getName(), player);

		// Sort the hashmap by points
		miceClone.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().points - entry1.getValue().points)
				.forEach(entry -> {
					// Create a label with the mouse name and points
					Label label = new Label(entry.getKey() + " : " + entry.getValue().points);
					label.setStyle("-fx-font-size: 35px; -fx-text-fill: black; -fx-font-weight: bold;");

					label.setAlignment(Pos.CENTER);
					label.setTranslateY(50 + this.numOfMicePrinted * 40);

					this.numOfMicePrinted += 1;

					this.root.getChildren().add(label);
				});
	}

	// Setter
	void setStage() {
		this.stage.setScene(this.scene);
	}

	// Getter
	public Scene getScene() {
		return this.scene;
	}

	// Getter
	Stage getStage() {
		return this.stage;
	}
}
