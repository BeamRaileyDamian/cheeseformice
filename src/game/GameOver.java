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
	private TextArea scoreboard = new TextArea();
	private StackPane root = new StackPane();

  public GameOver (Stage stage, MainMenu menu){
    this.stage = stage;
    this.menu = menu;

    Image bg = new Image("assets/gameover.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, false, false);
    ImageView viewbg = new ImageView();
    viewbg.setImage(bg);

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.setSpacing(15);

    // add the background image to the root
    this.root.getChildren().add(viewbg);

    // set the root  as root of the scene
    this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);

  }

  // method to add the list of mouse and their scores to the game over screen
  public void addMice(Mouse player){
    	// Scoreboard
		this.scoreboard.setPrefHeight(550);
		this.scoreboard.setEditable(false);

		VBox vbox = new VBox(5, this.scoreboard);
		vbox.setPrefSize(590, 185);
		vbox.setLayoutY(713);
		vbox.setLayoutX(610);

		this.root.getChildren().addAll(vbox); // add canvas to the root

		// clone the mice hashmap
		HashMap<String, Mouse> miceClone = new HashMap<String, Mouse>(GameStage.mice);

		// add to the hashmap clone the player
		miceClone.put(player.getName(), player);

		// sort the hashmap by points
		miceClone.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().points - entry1.getValue().points)
				.forEach(entry -> {
					scoreboard.appendText(entry.getValue().getName() + ": " + entry.getValue().points + "\n");
				});

  }

  	//setter
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