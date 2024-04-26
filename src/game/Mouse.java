package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Mouse extends Sprite{
	protected GraphicsContext gc;
	protected String imgDirection;
	protected int imgNum;
	
	public final static int MOUSE_SIZE = 60;
	public final static int MOUSE_SPEED = 3;

	public static final String RIGHT = "r";
	public static final String LEFT = "l";
	
	private boolean withCheese;

	public Mouse() {
		super(50, GameStage.WINDOW_HEIGHT - 225);
		this.imgStr = "assets/mouse";
		this.imgDirection = Mouse.RIGHT;
		this.imgNum = 1;
		this.withCheese = false;
		
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT); // open canvas with same size as stage
		this.gc = canvas.getGraphicsContext2D();
		this.loadImage(this.getFullImgStr(), Mouse.MOUSE_SIZE);
	}

	// getters
	String getFullImgStr() {
		return this.imgStr + "_" + this.imgNum + "_" + this.imgDirection + ".png";
	}
	
	boolean checkWithCheese() {
		return this.withCheese;
	}
 	
	// setters
	void setImgDirection(String direction) {
		this.imgDirection = direction;
	}
	
	void setWithCheese() {
		this.withCheese = true;
	}
}
