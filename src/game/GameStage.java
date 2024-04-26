package game;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GameStage {
	public static final int WINDOW_HEIGHT = 750;
	public static final int WINDOW_WIDTH = 1000;
	private Scene scene;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private Image bg;
	private Player player;
	private Hole hole;
	private Cheese cheese;
	private GameTimer gametimer;
	
	GameStage(MainMenu menu) {
		this.root = new Group();

		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); // canvas of background
		this.gc = canvas.getGraphicsContext2D();

		this.player = new Player(); // declare player
		this.hole = new Hole(); // declare hole
		this.cheese = new Cheese(GameStage.WINDOW_WIDTH/4*3, GameStage.WINDOW_HEIGHT - 230, Mouse.MOUSE_SIZE);
		
		this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, false, true); // declare the background img
		
		this.root.getChildren().addAll(canvas); // add canvas to the root
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		
		this.gametimer = new GameTimer(this.gc, this.scene, this, menu, this.player, this.hole, this.cheese);
		this.gametimer.start();
	}
	
	// getters
	Scene getScene() {
		return this.scene;
	}

	Player getPlayer() {
		return this.player;
	}

	Image getBG() {
		return this.bg;
	}
}
