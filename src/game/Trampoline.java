package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Trampoline extends Sprite {
  protected GraphicsContext gc;
  protected String imgStr;

    // Constructor
    public Trampoline(int xPos, int yPos, int size) {
    	super(xPos, yPos);
    	this.imgStr = "assets/trampoline.png";

    	Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
    	this.gc = canvas.getGraphicsContext2D();
    	this.loadImage(this.imgStr, size);

    }
}