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
import javafx.stage.Stage;

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
	private Stage stage;
	private int currentLevel;

	// NETWORKING
	private boolean isServer = true;
	private TextArea messages = new TextArea();
	private NetworkConnection connection = isServer ? createServer() : createClient();

	// Array of GamePlatforms
	private GamePlatform[] platforms = new GamePlatform[5];
	// Array of trampolines
	private Trampoline[] trampolines = new Trampoline[6];

	private LargeBox largeBox;

	GameStage(MainMenu menu, int level, Stage stage) {
		this.root = new Group();
		this.stage = stage;

		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); // canvas of background
		this.gc = canvas.getGraphicsContext2D();

		if (level == 1) {
			this.currentLevel = 1;

			this.player = new Player(); // declare player
			this.hole = new Hole(5, Mouse.INITIAL_Y); // declare hole
			this.cheese = new Cheese(1000, GameStage.WINDOW_HEIGHT - 850, Mouse.MOUSE_SIZE);
			this.platforms[0] = new GamePlatform(200, GameStage.WINDOW_HEIGHT - 450, 100);
			this.platforms[1] = new GamePlatform(400, GameStage.WINDOW_HEIGHT - 550, 100);
			this.platforms[2] = new GamePlatform(600, GameStage.WINDOW_HEIGHT - 650, 100);
			this.platforms[3] = new GamePlatform(600, GameStage.WINDOW_HEIGHT - 650, 100);
			this.platforms[4] = new GamePlatform(600, GameStage.WINDOW_HEIGHT - 650, 100);

			for (int i = 0; i < trampolines.length; i++) {
				this.trampolines[i] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 750, 100);
			}

			this.largeBox = new LargeBox(0, GameStage.WINDOW_HEIGHT - 0, 0);

			this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img
		} else if (level == 2) {
			this.currentLevel = 2;

			this.player = new Player(); // declare player
			this.hole = new Hole(5, Mouse.INITIAL_Y); // declare hole
			this.cheese = new Cheese(1000, GameStage.WINDOW_HEIGHT - 850, Mouse.MOUSE_SIZE);
			for (int i = 0; i < platforms.length; i++) {
				this.platforms[i] = new GamePlatform(0, GameStage.WINDOW_HEIGHT, 0);
			}
			this.trampolines[0] = new Trampoline(200, GameStage.WINDOW_HEIGHT - 450, 100);
			this.trampolines[1] = new Trampoline(400, GameStage.WINDOW_HEIGHT - 550, 100);
			this.trampolines[2] = new Trampoline(600, GameStage.WINDOW_HEIGHT - 650, 100);
			this.trampolines[3] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 750, 100);
			this.trampolines[4] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 750, 100);
			this.trampolines[5] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 750, 100);

			this.largeBox = new LargeBox(0, GameStage.WINDOW_HEIGHT - 0, 0);

			this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img
		} else if (level == 3) {
			this.currentLevel = 3;

			this.player = new Player(); // declare player
			this.hole = new Hole(5, Mouse.INITIAL_Y); // declare hole
			this.cheese = new Cheese(1000, GameStage.WINDOW_HEIGHT - 400, Mouse.MOUSE_SIZE);
			for (int i = 0; i < platforms.length; i++) {
				this.platforms[i] = new GamePlatform(0, GameStage.WINDOW_HEIGHT, 0);
			}
			this.trampolines[0] = new Trampoline(300, GameStage.WINDOW_HEIGHT - 450, 100);
			this.trampolines[1] = new Trampoline(100, GameStage.WINDOW_HEIGHT - 550, 100);
			this.trampolines[2] = new Trampoline(300, GameStage.WINDOW_HEIGHT - 650, 100);
			this.trampolines[3] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 450, 100);
			this.trampolines[4] = new Trampoline(1000, GameStage.WINDOW_HEIGHT - 550, 100);
			this.trampolines[5] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 650, 100);

			this.largeBox = new LargeBox(400, GameStage.WINDOW_HEIGHT - 750, 400);

			this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img
		}

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

		this.gametimer = new GameTimer(this.gc, this.scene, this, menu, this.player, this.hole, this.cheese, this.platforms, this.trampolines, this.largeBox, this.stage, this.currentLevel, this.connection);
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
