package game;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	private int currentLevel = 1;
	private int ground = GameStage.WINDOW_HEIGHT - 425;
	private String playerName;

	// NETWORKING
	private boolean isServer;
	private String serverIp;
	private TextArea messages = new TextArea();
	private NetworkConnection connection;

	// Array of GamePlatforms
	private GamePlatform[] platforms = new GamePlatform[5];
	// Array of trampolines
	private Trampoline[] trampolines = new Trampoline[6];
	private Land[] lands = new Land[5];

	private LargeBox largeBox;

	GameStage(MainMenu menu, Stage stage, String playerName, Boolean isServer, String serverIp) {
		this.root = new Group();
		this.stage = stage;
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); // canvas of background
		this.gc = canvas.getGraphicsContext2D();
		this.playerName = playerName;
		this.isServer = isServer;
		this.connection = isServer ? createServer() : createClient();
		this.serverIp = serverIp;

		this.player = new Player(Mouse.INITIAL_X, this.ground, this.playerName); // declare player
		this.hole = new Hole(5, Mouse.INITIAL_Y); // declare hole
		this.cheese = new Cheese(1000, GameStage.WINDOW_HEIGHT - 850, Cheese.CHEESE_SIZE);
		//this.cheese = new Cheese(10, GameStage.WINDOW_HEIGHT/2, Cheese.CHEESE_SIZE);
		this.platforms[0] = new GamePlatform(200, GameStage.WINDOW_HEIGHT - 450, 100, 20);
		this.platforms[1] = new GamePlatform(400, GameStage.WINDOW_HEIGHT - 550, 100, 20);
		this.platforms[2] = new GamePlatform(600, GameStage.WINDOW_HEIGHT - 650, 100, 20);
		this.platforms[3] = new GamePlatform(600, GameStage.WINDOW_HEIGHT - 650, 100, 20);
		this.platforms[4] = new GamePlatform(600, GameStage.WINDOW_HEIGHT - 650, 100, 20);

		for (int i = 0; i < trampolines.length; i++) {
			this.trampolines[i] = new Trampoline(800, GameStage.WINDOW_HEIGHT - 750, 100, 20);
		}

		this.lands[0] = new Land(0, GameStage.WINDOW_HEIGHT, 200, 370);
		this.lands[1] = new Land(0, GameStage.WINDOW_HEIGHT, 10, 370);
		this.lands[2] = new Land(0, GameStage.WINDOW_HEIGHT, 10, 370);
		this.lands[3] = new Land(0, GameStage.WINDOW_HEIGHT, 10, 370);
		this.lands[4] = new Land(0, GameStage.WINDOW_HEIGHT, 10, 370);

		this.largeBox = new LargeBox(0, GameStage.WINDOW_HEIGHT, 0);
		this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img

		initChat();
		this.gametimer = new GameTimer(this.gc, this.scene, this, menu, this.player, this.hole, this.cheese, this.platforms, this.trampolines, this.largeBox, this.lands, this.stage, this.currentLevel, this.connection, this.isServer, this.serverIp);
		this.gametimer.start();
	}

	public void setLevel(int level) {
		this.currentLevel = level;
//		if (this.currentLevel == 2) {
//			for (int i = 0; i < platforms.length; i++) {
//				platforms[i].setDimensions(0, 0);
//				platforms[i].setXY(0, GameStage.WINDOW_HEIGHT);
//			}
//
//			this.trampolines[0].setXY(200, GameStage.WINDOW_HEIGHT - 450);
//			this.trampolines[1].setXY(400, GameStage.WINDOW_HEIGHT - 550);
//			this.trampolines[2].setXY(600, GameStage.WINDOW_HEIGHT - 650);
//			this.trampolines[3].setXY(800, GameStage.WINDOW_HEIGHT - 750);
//			this.trampolines[4].setXY(800, GameStage.WINDOW_HEIGHT - 750);
//			this.trampolines[5].setXY(800, GameStage.WINDOW_HEIGHT - 750);
//		}

		if (this.currentLevel == 2) {
			this.bg = new Image("assets/bg_map_1.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img
			this.cheese.setXY(1000, GameStage.WINDOW_HEIGHT - 400);
			this.largeBox.setXY(400, GameStage.WINDOW_HEIGHT - 750);
			this.largeBox.setDimensions(400, 400);

			for (int i = 0; i < platforms.length; i++) {
				platforms[i].setXY(-100, -100);
			}

			this.trampolines[0].setXY(300, GameStage.WINDOW_HEIGHT - 450);
			this.trampolines[1].setXY(100, GameStage.WINDOW_HEIGHT - 550);
			this.trampolines[2].setXY(300, GameStage.WINDOW_HEIGHT - 650);
			this.trampolines[3].setXY(800, GameStage.WINDOW_HEIGHT - 450);
			this.trampolines[4].setXY(1000, GameStage.WINDOW_HEIGHT - 550);
			this.trampolines[5].setXY(800, GameStage.WINDOW_HEIGHT - 650);
		}

		else if (this.currentLevel == 3) {
			this.bg = new Image("assets/bg_map_3.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true); // declare the background img
			this.largeBox.setXY(-1000, -1000);
			this.largeBox.setDimensions(0, 0);

			for (int i = 0; i < platforms.length; i++) {
				platforms[i].setXY(-100, -100);
			}

			this.trampolines[0].setXY(1070, GameStage.WINDOW_HEIGHT - 220);
			for (int i = 1; i < trampolines.length; i++) {
				this.trampolines[i].setXY(-100, -100);
			}

			this.player.setXY(30, WINDOW_HEIGHT/3 - Mouse.MOUSE_SIZE/2 + 5);
			this.ground = 2000;
			this.hole.setXY(30, WINDOW_HEIGHT/3);
			this.cheese.setXY(1070, WINDOW_HEIGHT/3 + 200);

			this.lands[0].setXY(0, WINDOW_HEIGHT - 560);
			this.lands[1].setXY(400, WINDOW_HEIGHT - 560);
			this.lands[2].setXY(600, WINDOW_HEIGHT - 560);
			this.lands[3].setXY(800, WINDOW_HEIGHT - 560);
			this.lands[4].setXY(1000, WINDOW_HEIGHT - 560);
		}
	}

	public int getLevel() {
		return this.currentLevel;
	}

	/////////////////////////////////////////////////////////////
	private void initChat() {
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

        this.scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.C) {
                input.requestFocus();
                Platform.runLater(() -> input.clear());
            }
        });

        try {
			if (isServer) connection.startServer(connection.getPort(), (Server)connection);
			else connection.startClient(connection.getIP(), connection.getPort());

		} catch (Exception e) {
			e.printStackTrace();
		}

		Platform.runLater(() -> root.requestFocus());
	}

	private Server createServer() {
		return new Server(55555, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + "\n");
			});
		});
	}

	private Client createClient() {
		return new Client(this.serverIp, 55555, data -> {
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

	int getGround() {
		return this.ground;
	}

}
