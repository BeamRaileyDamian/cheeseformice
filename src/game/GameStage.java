package game;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class GameStage{
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
	
	// NETWORKING
	private boolean isServer = true;
	private TextArea messages = new TextArea();
	private NetworkConnection connection = isServer ? createServer() : createClient();
	
	// Array of GamePlatforms
	private GamePlatform[] platforms = new GamePlatform[3];
	// Array of trampolines
	private Trampoline[] trampolines = new Trampoline[3];

	GameStage(MainMenu menu) {
		this.root = new Group();

		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); // canvas of background
		this.gc = canvas.getGraphicsContext2D();

		this.player = new Player(); // declare player
		this.hole = new Hole(5, Mouse.INITIAL_Y); // declare hole
		this.cheese = new Cheese(1000, GameStage.WINDOW_HEIGHT - 850, Mouse.MOUSE_SIZE);
		this.platforms[0] = new GamePlatform(200, GameStage.WINDOW_HEIGHT - 450, 100);
		this.platforms[1] = new GamePlatform(400, GameStage.WINDOW_HEIGHT - 550, 100);
		this.platforms[2] = new GamePlatform(600, GameStage.WINDOW_HEIGHT - 650, 100);
		this.trampolines[0] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 750, 100);  

		this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img

		messages.setPrefHeight(550);
		messages.setEditable(false);
		TextField input = new TextField();
		input.setOnAction(event -> {
			String message = this.player.getName() + ": ";
			message += input.getText();
			input.clear();
			root.requestFocus();
			
			messages.appendText(message + "\n");
			
			try {
				connection.send(message);
			} catch (Exception e) {
				messages.appendText("Failed to send\n");
			}
		});
		
		VBox vbox = new VBox(5, messages, input);
		vbox.setPrefSize(600, 185);
		vbox.setLayoutY(713);
		
		this.root.getChildren().addAll(canvas, vbox); // add canvas to the root
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
        this.scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event1 -> {
            // Check if the click happened outside of the TextField
            if (!vbox.getBoundsInParent().contains(event1.getX(), event1.getY())) {
                // Request focus for the root pane to remove focus from the TextField
                root.requestFocus();
            }
        });

		this.gametimer = new GameTimer(this.gc, this.scene, this, menu, this.player, this.hole, this.cheese, this.platforms, this.trampolines, this.connection);
		this.gametimer.start();
		try {
			connection.startConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////
	private Server createServer() {
		return new Server(55555, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + "\n");
			});
		});
	}
	
	private Client createClient() {
		return new Client("127.0.0.1", 55555, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + "\n");
			});
		});
	}
	/////////////////////////////////////////////////////////////

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
