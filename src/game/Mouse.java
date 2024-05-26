package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Mouse extends Sprite{
	protected GraphicsContext gc;
	protected String imgDirection, name;
	protected int imgNum, port;
	protected float jumpVelocity;

	public final static int MOUSE_SIZE = 80;
	public final static int MOUSE_SPEED = 4;

	public final static int INITIAL_X = 50;
	public final static int INITIAL_Y = GameStage.WINDOW_HEIGHT - 400;

	public static final String RIGHT = "r";
	public static final String LEFT = "l";

	private boolean withCheese;
	private boolean hasJumped;

	protected int points = 0;
	protected boolean isVisible = true;

	public Mouse(int x, int y, String name) {
		super(x, y);
		this.imgStr = "assets/mouse";
		this.imgDirection = Mouse.RIGHT;
		this.imgNum = 3;
		this.withCheese = false;
		this.jumpVelocity = MOUSE_SPEED;
		this.port = 2000;
		this.hasJumped = false;
		this.name = name;

		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT); // open canvas with same size as stage
		this.gc = canvas.getGraphicsContext2D();
		this.loadImage(this.getFullImgStr(), Mouse.MOUSE_SIZE);
	}

	// getters
	String getFullImgStr() {
		return this.imgStr + "_" + this.imgNum + "_" + this.imgDirection + ".png";
	}

	int getImgNum() {
		return this.imgNum;
	}

	boolean checkHasJumped() {
		return this.hasJumped;
	}

	boolean checkWithCheese() {
		return this.withCheese;
	}

	float getJumpVelocity() {
		return this.jumpVelocity;
	}

	String getName() {
		return this.name;
	}

	int getPort() {
		return this.port;
	}

	// setters
	void setImgDirection(String direction) {
		this.imgDirection = direction;
	}

	void setImgNum(int num) {
		this.imgNum = num;
	}


	void setWithCheese() {
		this.withCheese = true;
	}

	void setWithCheese(boolean val) {
		this.withCheese = val;
	}

	void setWithoutCheese() {
		this.withCheese = false;
	}

	void setHasJumped() {
		this.hasJumped = !this.hasJumped;
	}

	void setHasJumped(boolean val) {
		this.hasJumped = val;
	}

	void setJumpVelocity(float jumpVelocity) {
		this.jumpVelocity = jumpVelocity;
	}

	void setName(String name) {
		this.name = name;
	}

	void setPort(int port) {
		this.port = port;
	}

	void setIsVisible(boolean val) {
		this.isVisible = val;
	}

	boolean getIsVisible(){
		return this.isVisible;
	}

	void addPoints(int points) {
		this.points += points;
	}
}
