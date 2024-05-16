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
	protected NetworkConnection connection;

	private ArrayList<Mouse> mice;
	private ArrayList<Sprite> items;
	private ArrayList<Cheese> acquiredCheese;

	GameTimer(GraphicsContext gc, Scene scene, GameStage gamestage, MainMenu menu, Player player, Hole hole, Cheese cheese,
	GamePlatform[] gameplatforms, Trampoline[] trampolines, LargeBox largeBox, Stage stage, int currentLevel, NetworkConnection connection){
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
			this.stop();
			// this.menu.setStage();
			this.player.setWithoutCheese();
			GameStage theGameStage = new GameStage(menu, this.currentLevel+1, this.stage);
			this.stage.setScene(theGameStage.getScene());
			try {
				this.connection.closeConnections();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// check if player collides with any GamePlatform from below
		for (Sprite s : this.items) {
			// check if sprite is a GamePlatform
			if (s instanceof GamePlatform) {
				if (this.player.collidesWith(s) && this.player.getY() < s.getY()) {
					this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
					this.player.setHasJumped();
					// this.player.setDY(0);
					// System.out.println("collided with GamePlatform from below");
				}
			}
		}

		// check if player collides with a trampoline
		for (Sprite s : this.items) {
			// check if sprite is a trampoline
			if (s instanceof Trampoline) {
				if (this.player.collidesWith(s) && this.player.getY() < s.getY()) {
					this.player.setY(s.getY() - Mouse.MOUSE_SIZE);
					this.player.setHasJumped();
					this.player.setDY(-1);
					this.player.setJumpVelocity(10);
					// System.out.println("collided with trampoline");
				}
			}
		}

		// check if player collides with a largeBox
		// limit player movement to the left and right of the largeBox
		for (Sprite s : this.items) {
			// check if sprite is a large box
			if (s instanceof LargeBox) {
				if (this.player.collidesWith(s)) {
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
					// TODO: edit this to stop sprite if it collides with any GamePlatform, not just the ground
					if (m.getY() >= Mouse.INITIAL_Y && m.getDy() == 1) {
						m.setHasJumped();
						m.setDY(0);
					} else {
						if (m.getY() <= m.getYBeforeJump()-m.getMaxJumpHeight()){
							m.setDY(1);
						}
						m.y += m.dy * m.getJumpVelocity();

						// increase jump velocity throughout jump
						m.setJumpVelocity(m.getJumpVelocity() + (float)0.05);

					}
				}

				else if (m.dy != 0 && !m.checkHasJumped()) {
					m.setHasJumped();
					m.setYBeforeJump();
					m.setMaxJumpHeight(Mouse.MOUSE_SIZE * 2);
					m.setJumpVelocity(Mouse.MOUSE_SPEED);

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

		//if(key==KeyCode.S) this.player.setDY(-1);

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