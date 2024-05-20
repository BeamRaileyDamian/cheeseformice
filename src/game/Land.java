package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Land extends Sprite {
	protected GraphicsContext gc;
	protected String imgStr;

    // Constructor
    public Land(int xPos, int yPos, int width, int height) {
    	super(xPos, yPos);
    	this.imgStr = "assets/land.png";

    	Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
    	this.gc = canvas.getGraphicsContext2D();
    	this.loadImage(this.imgStr, width, height);

    }
}
