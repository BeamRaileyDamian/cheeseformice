package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class LargeBox extends Sprite {
	protected GraphicsContext gc;
	protected String imgStr;

    // Constructor
    public LargeBox(int xPos, int yPos, int size) {
    	super(xPos, yPos);
    	this.imgStr = "assets/large_box.png";

    	Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
    	this.gc = canvas.getGraphicsContext2D();
    	this.loadImage(this.imgStr, size);

    }
}
