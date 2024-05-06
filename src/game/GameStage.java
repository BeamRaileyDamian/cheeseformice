package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class GameStage {
	public static final int WINDOW_HEIGHT = 900;
	public static final int WINDOW_WIDTH = 1200;
	private Scene scene;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private Image bg;
	private Player player;
	private Hole hole;
	private Cheese cheese;
	private GameTimer gametimer;

	// Array of platforms
	private Platform[] platforms = new Platform[3];
	// Array of trampolines
	private Trampoline[] trampolines = new Trampoline[3];

	GameStage(MainMenu menu) {
		this.root = new Group();

		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); // canvas of background
		this.gc = canvas.getGraphicsContext2D();

		this.player = new Player(); // declare player
		this.hole = new Hole(5, Mouse.INITIAL_Y); // declare hole
		this.cheese = new Cheese(1000, GameStage.WINDOW_HEIGHT - 850, Mouse.MOUSE_SIZE);
		this.platforms[0] = new Platform(200, GameStage.WINDOW_HEIGHT - 450, 100);
		this.platforms[1] = new Platform(400, GameStage.WINDOW_HEIGHT - 550, 100);
		this.platforms[2] = new Platform(600, GameStage.WINDOW_HEIGHT - 650, 100);
		this.trampolines[0] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 750, 100);
		
		TilePane tile = new TilePane();
		tile.setLayoutX(-60);
		tile.setLayoutY(860); 

        TextField b = new TextField();
        b.setFocusTraversable(false);
        Label l = new Label(this.player.getName()+":");
 
        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
            	b.setText("");
            	root.requestFocus();
            }
        };
 
        // when enter is pressed
        b.setOnAction(event);
        tile.getChildren().add(l);
        tile.getChildren().add(b);

		this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img

		this.root.getChildren().addAll(canvas, tile); // add canvas to the root
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		// Add event handler to handle mouse clicks on the scene
        this.scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event1 -> {
            // Check if the click happened outside of the TextField
            if (!b.getBoundsInParent().contains(event1.getX(), event1.getY())) {
                // Request focus for the root pane to remove focus from the TextField
                root.requestFocus();
            }
        });

		this.gametimer = new GameTimer(this.gc, this.scene, this, menu, this.player, this.hole, this.cheese, this.platforms, this.trampolines);
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
