package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Cheese extends Sprite{
	protected GraphicsContext gc;
	protected String imgStr;
	protected Player player;
	
	public final static int CHEESE_SIZE = 60;
	
	public Cheese(int xPos, int yPos, int size) {
		super(xPos, yPos);
		this.imgStr = "assets/cheese.png";
		this.player = null;
		
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT); // open canvas with same size as stage
		this.gc = canvas.getGraphicsContext2D();
		this.loadImage(this.imgStr, size);
	}
	
	Player getPlayer() {
		return this.player;
	}
	
	void setPlayer(Player player) {
		this.player = player;
	}
}
