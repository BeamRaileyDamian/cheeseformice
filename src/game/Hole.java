package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Hole extends Sprite{
	protected GraphicsContext gc;
	protected String imgStr;
	
	public Hole() {
		super(5, GameStage.WINDOW_HEIGHT - 210);
		this.imgStr = "assets/hole.png";
		
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT); // open canvas with same size as stage
		this.gc = canvas.getGraphicsContext2D();
		this.loadImage(this.imgStr, Mouse.MOUSE_SIZE/4*3);
	}
}
