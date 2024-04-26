package game;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameTimer extends AnimationTimer{

	private GraphicsContext gc;
	private Scene theScene;
	private GameStage gs;
	private Player player;
	private Hole hole;
	private Cheese cheese;
	private MainMenu menu;
	
	private ArrayList<Mouse> mice;
	private ArrayList<Sprite> items;
	private ArrayList<Cheese> acquiredCheese;

	GameTimer(GraphicsContext gc, Scene scene, GameStage gamestage, MainMenu menu, Player player, Hole hole, Cheese cheese){
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
		mice.add(player);
		items.add(hole);
		items.add(cheese);

		//call method to handle mouse click event
		this.handleKeyPressEvent();
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(this.gs.getBG(), 0, 0);
		renderItems();
		renderMice();
		moveMice();
		checkCollisions();
	}
	
	private void checkCollisions() {
		if (!this.player.checkWithCheese() && this.player.collidesWith(cheese)) {
			this.player.setWithCheese();
			Cheese newCheese = new Cheese(this.player.x, this.player.y + Mouse.MOUSE_SIZE/2, Mouse.MOUSE_SIZE/2);
			newCheese.setPlayer(player);
			this.acquiredCheese.add(newCheese);
			System.out.println("Cheesed");
		}
		
		if (this.player.checkWithCheese() && this.player.collidesWith(hole)) {
			System.out.println("Win");
			this.stop();
			this.menu.setStage();
		}
	}

	// method that will render/draw the fishes to the canvas
	private void renderMice() {
		for (Mouse m : this.mice){ // loop through fishes, if dead, add to fishes to remove
			m.loadImage(m.getFullImgStr(), Mouse.MOUSE_SIZE);
			m.render(this.gc);
		}
	}
	
	// method that will render/draw the fishes to the canvas
	private void renderItems() {
		for (Sprite s : this.items){ // loop through fishes, if dead, add to fishes to remove
			s.loadImage(s.getImgStr(), Mouse.MOUSE_SIZE);
			s.render(this.gc);
		}
		
		for (Cheese c : this.acquiredCheese) {
			c.setX(c.getPlayer().getX() + 15);
			c.setY(c.getPlayer().getY() - Mouse.MOUSE_SIZE/3);
			c.render(this.gc);
		}
	}
	
	// method that will render/draw the fishes to the canvas
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
		if(key==KeyCode.W) this.player.setDY(1);

		if(key==KeyCode.A) this.player.setDX(-1);

		if(key==KeyCode.S) this.player.setDY(-1);

		if(key==KeyCode.D) this.player.setDX(1);
   	}
	
	// method that will stop the player's movement; set the player's DX and DY to 0
	private void stopPlayer(KeyCode key){
		if (key==KeyCode.W || key==KeyCode.S) {
			this.player.setDY(0);
		} else {
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