package game;

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
	private Player player;
	private Hole hole;
	private Cheese cheese;
	private MainMenu menu;
	private Stage stage;
	private int currentLevel;
	private boolean isServer;
	private String serverIp;
	protected NetworkConnection connection;

	private ArrayList<Mouse> mice;
	private ArrayList<Sprite> items;
	private ArrayList<Cheese> acquiredCheese;

	GameTimer(GraphicsContext gc, Scene scene, GameStage gamestage, MainMenu menu, Player player, Hole hole, Cheese cheese,
	GamePlatform[] gameplatforms, Trampoline[] trampolines, LargeBox largeBox, Stage stage, int currentLevel, NetworkConnection connection, boolean isServer, String serverIp){
		this.gs = gamestage; // set values
		this.gc = gc;
		this.theScene = scene;
		this.mice = new ArrayList<Mouse>();
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
		mice.add(player);
		items.add(hole);
		items.add(cheese);

		for (int i = 0; i < gameplatforms.length; i++) {
			items.add(gameplatforms[i]);
		}

		for (int i = 0; i < trampolines.length; i++) {
			items.add(trampolines[i]);
		}

		items.add(largeBox);

		//call method to handle mouse click event
		this.handleKeyPressEvent();
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(this.gs.getBG(), 0, 0);
		moveMice();
		checkCollisions();
		renderItems();
		renderMice();
	}

	private void checkCollisions() {
		if (!this.player.checkWithCheese() && this.player.collidesWith(cheese)) {
			this.player.setWithCheese();
			Cheese newCheese = new Cheese(this.player.x, this.player.y + Mouse.MOUSE_SIZE/2, Mouse.MOUSE_SIZE/2);
			newCheese.setPlayer(player);
			this.acquiredCheese.add(newCheese);
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
		
		if (this.player.checkHasJumped() && this.player.getY() >= GameStage.GROUND) {
			this.player.setHasJumped();
			this.player.setY(GameStage.GROUND);
			this.player.setDY(0);
		}

		for (Sprite s : this.items) {
			if (this.player.collidesWith(s)) {				
				if (s instanceof GamePlatform) {
					if (this.player.getDy() == -1 || this.player.getY() > s.getY()) {
						this.player.setDY(1);
					} else {	
						this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
						this.player.setHasJumped();
						
					}
				}
				
				else if (s instanceof Trampoline) {
					if (this.player.getDy() == -1 || this.player.getY() > s.getY()) {
						this.player.setDY(1);
					} else if (this.player.getY() < s.getY()) {
						this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
						this.player.setHasJumped();
						this.player.setDY(-1);
						this.player.setJumpVelocity(Mouse.MOUSE_SPEED * (float)2);
					} 
				}
				
				else if (s instanceof LargeBox) {
					if (this.player.getY() < s.getY()) {
						this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
						this.player.setHasJumped();
					} else {
						if (this.player.getX() < s.getX()) {
							this.player.setX(s.getX() - Mouse.MOUSE_SIZE);
						} else {
							this.player.setX((s.getX() + 340) + Mouse.MOUSE_SIZE);
						}
					}
				}
			}
		}
	}

	// method that will render/draw the mice to the canvas
	private void renderMice() {
		for (Mouse m : this.mice){
			m.loadImage(m.getFullImgStr(), Mouse.MOUSE_SIZE);
			m.render(this.gc);
		}
	}

	// method that will render/draw the mice to the canvas
	private void renderItems() {
		for (Sprite s : this.items){
			s.loadImage(s.getImgStr(), Mouse.MOUSE_SIZE);
			s.render(this.gc);
		}

		for (Cheese c : this.acquiredCheese) {
			c.setX(c.getPlayer().getX() + 15);
			c.setY(c.getPlayer().getY() - Mouse.MOUSE_SIZE/3);
			c.render(this.gc);
		}

		// Render the names for each mice above their heads
		for (Mouse m : this.mice) {
			this.gc.fillText(m.getName(), m.getX() + 8, m.getY() - 10);
		}
	}

	// method that will render/draw the mice to the canvas
	private void moveMice() {
		for (Mouse m : this.mice){
			if (this.checkBounds()) {
				if (m.dx != 0) {
					if (m.dx > 0) {
						m.setImgDirection(Mouse.RIGHT);
					} else {
						m.setImgDirection(Mouse.LEFT);
					}

					m.x += m.dx * Mouse.MOUSE_SPEED;
				}

				if(m.checkHasJumped()) {			
					if (m.getY() >= GameStage.GROUND && m.getDy() == 1) {
						m.setHasJumped();
						m.setDY(0);
					} else {
						if (m.getY() <= m.getYBeforeJump()-m.getMaxJumpHeight()){
							m.setDY(1);
						}
						m.y += m.dy * m.getJumpVelocity();

						if (m.getDy() == 1) m.setJumpVelocity(m.getJumpVelocity() + (float)0.2);
						else m.setJumpVelocity(m.getJumpVelocity() - (float)0.2);
					}
				}

				else if (m.dy != 0 && !m.checkHasJumped()) {
					m.setHasJumped();
					m.setYBeforeJump();
					m.setMaxJumpHeight(Mouse.MOUSE_SIZE * 3);
					m.setJumpVelocity(Mouse.MOUSE_SPEED * (float)2);

					m.y += m.dy * m.getJumpVelocity();
				}
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
		if(key==KeyCode.W && !this.player.checkHasJumped()) this.player.setDY(-1);

		if(key==KeyCode.A) this.player.setDX(-1);

		if(key==KeyCode.D) this.player.setDX(1);
   	}

	// method that will stop the player's movement; set the player's DX and DY to 0
	private void stopPlayer(KeyCode key){
		if (key==KeyCode.A || key==KeyCode.D) {
			this.player.setDX(0);
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
	Player getPlayer() {
		return this.player;
	}
}