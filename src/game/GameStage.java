package game;

import java.io.IOException;
import java.io.Serializable;

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
import java.util.HashMap;
import java.util.function.Consumer;

public class GameStage{
	public static final int WINDOW_HEIGHT = 900;
	public static final int WINDOW_WIDTH = 1200;
	private Scene scene;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private Image bg;
	private Mouse player;
	private Hole hole;
	private Cheese cheese;
	private GameTimer gametimer;
	private Stage stage;
	private int currentLevel = 1;
	private int ground = GameStage.WINDOW_HEIGHT - 425;
	private String playerName;
	private GameOver gameOver;

	// NETWORKING
	private boolean isServer;
	private String serverIp;
	public static TextArea messages = new TextArea();
	public static TextArea scoreboard = new TextArea();
	private NetworkConnection connection;

	// Array of GamePlatforms
	private GamePlatform[] platforms = new GamePlatform[5];
	// Array of trampolines
	private Trampoline[] trampolines = new Trampoline[6];
	private Land[] lands = new Land[5];

	public static HashMap<String, Mouse> mice = new HashMap<String, Mouse>();

	private LargeBox largeBox;

	GameStage(MainMenu menu, Stage stage, String playerName, Boolean isServer, String serverIp) {
		this.root = new Group();
		this.stage = stage;
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT); // canvas of background
		this.gc = canvas.getGraphicsContext2D();
		this.playerName = playerName;
		this.isServer = isServer;
		this.serverIp = serverIp;
		this.connection = isServer ? createServer() : createClient();
		this.gameOver = new GameOver(this.stage, menu);

		this.player = new Mouse(Mouse.INITIAL_X, this.ground, this.playerName); // declare player
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
		initScoreBoard();
		updateScoreBoard();
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

		// clear all acquired cheese
		GameTimer.acquiredCheese.clear();
		GameTimer.RemainingPoints = 100;
		for (Mouse m : mice.values()) {
			m.setWithCheese(false);
			m.setIsVisible(true);
		}

		this.player.setIsVisible(true);
		this.player.setWithCheese(false);

		if (this.currentLevel == 2) {

			this.player.setXY(Mouse.INITIAL_X, this.ground);

			for (Mouse m : mice.values()){
				m.setXY(Mouse.INITIAL_X, this.ground);
				m.setImgDirection("r");
			}

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

			for (Mouse m : mice.values()){
				m.setXY(30, WINDOW_HEIGHT/3 - Mouse.MOUSE_SIZE/2 + 5);
				m.setImgDirection("r");
			}

			this.lands[0].setXY(0, WINDOW_HEIGHT - 560);
			this.lands[1].setXY(400, WINDOW_HEIGHT - 560);
			this.lands[2].setXY(600, WINDOW_HEIGHT - 560);
			this.lands[3].setXY(800, WINDOW_HEIGHT - 560);
			this.lands[4].setXY(1000, WINDOW_HEIGHT - 560);
		}

		else if (this.currentLevel == 4) {
			this.gameIsOver();
		}
	}

	public int getLevel() {
		return this.currentLevel;
	}

	private void gameIsOver(){

		try {
			connection.send("GAMEOVER");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// close the connection
		try {
			if (this.isServer){
				this.connection.closeConnections();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.gameOver.addMice(player);
		this.stage.setScene(gameOver.getScene());

		this.gametimer.stop();
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
				messages.appendText("Failed to send");
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
			if (isServer) connection.startServer(connection.getPort(), (Server)connection, this.playerName);
			else connection.startClient(connection.getIP(), connection.getPort(), this.playerName);

		} catch (Exception e) {
			e.printStackTrace();
		}


		Platform.runLater(() -> root.requestFocus());
	}

	private void initScoreBoard() {
		// Scoreboard
		scoreboard.setPrefHeight(550);
		scoreboard.setEditable(false);

		VBox vbox = new VBox(5, scoreboard);
		vbox.setPrefSize(590, 185);
		vbox.setLayoutY(713);
		vbox.setLayoutX(610);

		this.root.getChildren().addAll(vbox); // add canvas to the root

		// add a header to the scoreboard
		scoreboard.appendText("Scoreboard\n");
		scoreboard.setStyle("-fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");
	}

	// method to update the scoreboard
	public void updateScoreBoard() {
		scoreboard.clear();
		scoreboard.appendText("Scoreboard\n");
		scoreboard.setStyle("-fx-font-size: 20px; -fx-font-family: Arial; -fx-font-weight: bold;");

		// clone the mice hashmap
		HashMap<String, Mouse> miceClone = new HashMap<String, Mouse>(GameStage.mice);

		// add to the hashmap clone the player
		miceClone.put(this.player.getName(), this.player);

		// sort the hashmap by points
		miceClone.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().points - entry1.getValue().points)
				.forEach(entry -> {
					scoreboard.appendText(entry.getValue().getName() + ": " + entry.getValue().points + "\n");
				});
	}

	private void processData(String string_data) {
		if(string_data.startsWith("COORD")) {
			String[] parts = string_data.split(" ");
			String name = parts[1];
			int x = Integer.parseInt(parts[2]);
			int y = Integer.parseInt(parts[3]);
			int imgNum = Integer.parseInt(parts[4]);
			String direction = (parts[5]);

			if (mice.containsKey(name)) {
				mice.get(name).setXY(x, y);
				mice.get(name).setImgNum(imgNum);
				mice.get(name).setImgDirection(direction);

			} else {
				Mouse newMouse = new Mouse(x, y, name);
				newMouse.setXY(x, y);
				newMouse.setImgNum(imgNum);
				newMouse.setImgDirection(direction);
				mice.put(name, newMouse);
				updateScoreBoard();
			}
		}

		else if (string_data.startsWith("CHEESE")){
			String[] parts = string_data.split(" ");
			String name = parts[1];

			Cheese newCheese = new Cheese(0, 0 + Mouse.MOUSE_SIZE/2, Cheese.CHEESE_SIZE/2);
			newCheese.setPlayer(mice.get(name));
			GameTimer.acquiredCheese.add(newCheese);
		}

		else if (string_data.startsWith("HOLE")) {
			String[] parts = string_data.split(" ");
			String name = parts[1];

			// System.out.println(name);

			mice.get(name).setWithoutCheese();
			mice.get(name).setIsVisible(false);
			mice.get(name).addPoints(GameTimer.RemainingPoints);
			updateScoreBoard();
			for (int i = 0; i < GameTimer.acquiredCheese.size(); i++) {
				Cheese c = GameTimer.acquiredCheese.get(i);

				if (c.getPlayer().getName().equals(name)) {
					GameTimer.acquiredCheese.remove(i);
				}
			}
			GameTimer.RemainingPoints -= 25;

			boolean stageDone = !this.player.getIsVisible();

			for (Mouse m : GameStage.mice.values()) {

				System.out.println(m.getName());
				System.out.println(m.getIsVisible());
				if (m.getIsVisible()){
					// System.out.println("VISIBLE PA");
					stageDone = false;
					break;
				}
			}

			if (stageDone) {
				this.setLevel(this.getLevel()+1);
				try {
					messages.appendText("Stage is Done\n");
					connection.send("DONE");
					// connection.send("Stage is Done\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		else if (string_data.equals("DONE")){
			messages.appendText("Stage is Done\n");
			this.setLevel(this.getLevel()+1);
		}

		else if (string_data.equals("GAMEOVER")){
			// this.gameIsOver();
		}

		else {
			messages.appendText(string_data + "\n");
		}
	}

	private Server createServer() {
		return new Server(55555, data -> {
			Platform.runLater(() -> {

				String string_data = data.toString();
				processData(string_data);
			});
		});
	}

	private Client createClient() {
		return new Client(this.serverIp, 55555, data -> {
			Platform.runLater(() -> {

				String string_data = data.toString();
				processData(string_data);
			});
		});
	}
	/////////////////////////////////////////////////////////////

	// getters
	Scene getScene() {
		return this.scene;
	}

	Mouse getPlayer() {
		return this.player;
	}

	Image getBG() {
		return this.bg;
	}

	int getGround() {
		return this.ground;
	}

}
