package game;

import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class GameTimer extends AnimationTimer{

	private GraphicsContext gc;
	private Scene theScene;
	private GameStage gs;
	private Mouse player;
	private Hole hole;
	private Cheese cheese;
	private MainMenu menu;
	private Stage stage;
	private int currentLevel;
	private boolean isServer;
	private String serverIp;
	protected NetworkConnection connection;

	// private ArrayList<Mouse> mice;

	private ArrayList<Sprite> items;
	public static ArrayList<Cheese> acquiredCheese;

	private static final int FRAME_UPDATE_INTERVAL = 7;
	private int frameCounter = 0;

	GameTimer(GraphicsContext gc, Scene scene, GameStage gamestage, MainMenu menu, Mouse player, Hole hole, Cheese cheese,
	GamePlatform[] gameplatforms, Trampoline[] trampolines, LargeBox largeBox, Land[] lands, Stage stage, int currentLevel, NetworkConnection connection, boolean isServer, String serverIp){
		this.gs = gamestage; // set values
		this.gc = gc;
		this.theScene = scene;
		this.items = new ArrayList<Sprite>();
		this.acquiredCheese = new ArrayList<Cheese>();
		this.player = player;
		this.hole = hole;
		this.cheese = cheese;
		this.menu = menu;
		this.connection = connection;
		this.stage = stage;
		this.isServer = isServer;
		this.serverIp = serverIp;
		this.currentLevel = currentLevel;
		items.add(hole);
		items.add(cheese);

		for (int i = 0; i < gameplatforms.length; i++) {
			items.add(gameplatforms[i]);
		}

		for (int i = 0; i < trampolines.length; i++) {
			items.add(trampolines[i]);
		}

		for (int i = 0; i < lands.length; i++) {
			items.add(lands[i]);
		}

		items.add(largeBox);

		//call method to handle mouse click event
		this.handleKeyPressEvent();
	}

	public void sendCoordinates() {
		//if (this.frameCounter % 1 == 0) {
			String message = "COORD " +  this.player.getName() + " " + this.player.getX() + " " + this.player.getY() + " " + this.player.getImgNum() + " " + this.player.imgDirection;
			try {
				this.connection.send(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(this.gs.getBG(), 0, 0);
		moveMice();
		checkCollisions();
		renderItems();
		renderMice();
		frameCounter++;
	}

	private void checkCollisions() {
		if (!this.player.checkWithCheese() && this.player.collidesWith(cheese)) {
			this.player.setWithCheese();
			Cheese newCheese = new Cheese(this.player.x, this.player.y + Mouse.MOUSE_SIZE/2, Cheese.CHEESE_SIZE/2);
			newCheese.setPlayer(player);
			this.acquiredCheese.add(newCheese);
			try {
				connection.send("CHEESE " + this.player.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (this.player.checkWithCheese() && this.player.collidesWith(hole)) {
			this.player.setWithoutCheese();
			this.acquiredCheese.clear();
			this.gs.setLevel(this.gs.getLevel()+1);
			//this.stop();
			// this.menu.setStage();
			//GameStage theGameStage = new GameStage(this.menu, this.currentLevel+1, this.stage, this.player.getName(), this.isServer, this.serverIp);
			//this.stage.setScene(theGameStage.getScene());
//			try {
//				this.connection.closeConnections();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}

		if (this.player.checkHasJumped() && this.player.getY() >= this.gs.getGround()) {
			this.player.setHasJumped();
			this.player.setY(this.gs.getGround());
			this.player.setDY(0);
			this.player.setJumpVelocity(Mouse.MOUSE_SPEED * (float)2);
			this.player.setImgNum(3);
		}

		for (Sprite s : this.items) {
			if (this.player.collidesWith(s)) {
				if (s instanceof GamePlatform) {
					this.player.setDY(1);
					if (s.getY() >= this.player.getY()) {
						this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
						this.player.setHasJumped(false);
						this.player.setJumpVelocity(Mouse.MOUSE_SPEED * (float)2);
					}
				}

				else if (s instanceof Trampoline) {
					if (s.getY() < this.player.getY()) {
						this.player.setDY(1);
					} else if (this.player.getY() < s.getY()) {
						this.player.setImgNum(4);
						this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
						this.player.setHasJumped(true);
						this.player.setDY(-1);
						// CHANGE THE NUMBER TO CHANGE INCREMENTS ON VELOCITY THROUGH JUMPS
						this.player.setJumpVelocity(this.player.getJumpVelocity()*((float)-1.01));
					}
				}

				else if (s instanceof LargeBox) {
					if (this.player.getY() < s.getY()) {
						this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
						this.player.setDY(1);
						this.player.setHasJumped(false);
					} else {
						if (this.player.getX() < s.getX()) {
							this.player.setX(s.getX() - Mouse.MOUSE_SIZE);
						} else {
							this.player.setX((s.getX() + 340) + Mouse.MOUSE_SIZE);
						}
					}
				}

				else if (s instanceof Land) {
					if (this.player.getY() < s.getY()) {
						this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
						this.player.setDY(1);
						this.player.setHasJumped(false);
					} else {
						this.player.setDX(0);
					}
				}
			}
		}
	}

	// method that will render/draw the mice to the canvas
	private void renderMice() {

		this.player.loadImage(this.player.getFullImgStr(), Mouse.MOUSE_SIZE);
		this.player.render(this.gc);

		for (Mouse m : GameStage.mice.values()){
			m.loadImage(m.getFullImgStr(), Mouse.MOUSE_SIZE);
			m.render(this.gc);
		}
	}

	// method that will render/draw the mice to the canvas
	private void renderItems() {
		for (Sprite s : this.items){
			s.render(this.gc);
		}

		for (Cheese c : this.acquiredCheese) {
			c.setX(c.getPlayer().getX() + 15);
			c.setY(c.getPlayer().getY() - Mouse.MOUSE_SIZE/3);
			c.render(this.gc);
		}

		// Render the names for each mice above their heads
		for (Mouse m : GameStage.mice.values()) {
			// System.out.println(m.getName());
			this.gc.fillText(m.getName(), m.getX() + 8, m.getY() - 10);
		}

		this.gc.fillText(this.player.getName(), this.player.getX() + 8, this.player.getY() - 10);
	}

	// method that will render/draw the mice to the canvas
	private void moveMice() {
		if (this.checkBounds()) {
			if (this.player.dx != 0) {
				if (frameCounter % FRAME_UPDATE_INTERVAL == 0 && !this.player.checkHasJumped()) {
					this.player.setImgNum(((this.player.getImgNum() + 1) % 6) + 1);
				}

				if (this.player.dx > 0) {
					this.player.setImgDirection(Mouse.RIGHT);
				} else {
					this.player.setImgDirection(Mouse.LEFT);
				}

				this.player.x += this.player.dx * Mouse.MOUSE_SPEED;
				this.sendCoordinates();
			}

			if(this.player.checkHasJumped()) {
				this.player.y += this.player.dy * this.player.getJumpVelocity();
				if (this.player.getDy() == 1) this.player.setJumpVelocity(this.player.getJumpVelocity() + (float)0.2);
				else this.player.setJumpVelocity(this.player.getJumpVelocity() - (float)0.2);
				this.sendCoordinates();
			}

			else if (this.player.dy != 0 && !this.player.checkHasJumped()) {
				this.player.setHasJumped();
				this.player.setJumpVelocity(Mouse.MOUSE_SPEED * (float)2);

				this.player.y += this.player.dy * this.player.getJumpVelocity();
				this.sendCoordinates();
			}
		}

	}

	// method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
                movePlayer(e.getCode());
			}
		});

		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		    public void handle(KeyEvent e){
		        stopPlayer(e.getCode());
		    }
		});
    }

	// method that will change dx and dy depending on the key pressed
	private void movePlayer(KeyCode key) {
		if(key==KeyCode.W && !this.player.checkHasJumped()) {
			this.player.setDY(-1);
			this.player.setImgNum(4);
		}

		if(key==KeyCode.A) this.player.setDX(-1);

		if(key==KeyCode.D) this.player.setDX(1);
   	}

	// method that will stop the player's movement; set the player's DX and DY to 0
	private void stopPlayer(KeyCode key){
		if (key==KeyCode.A || key==KeyCode.D) {
			this.player.setDX(0);
			this.player.setImgNum(3);

			sendCoordinates();
		}

	}

	private boolean checkBounds() {
		// check if the player has reached the bounds
		if ((this.player.getX() <= 0 && this.player.getDx() == -1) // check if the player has reached the left border and is pressing A
		|| (this.player.getX() >= GameStage.WINDOW_WIDTH - 60 && this.player.getDx() == 1)) // check if the player has reached the right border and is pressing D
		{
			return false;
		}
		return true;
	}

	// getter
	Mouse getPlayer() {
		return this.player;
	}

	int getNumOfMice() {
		return GameStage.mice.size();
	}
}