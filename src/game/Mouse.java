package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Mouse extends Sprite{
	protected GraphicsContext gc;
	protected String imgDirection, name;
	protected int imgNum, port;
	protected float jumpVelocity;
	protected int maxJumpHeight;

	public final static int MOUSE_SIZE = 60;
	public final static int MOUSE_SPEED = 4;

	public final static int INITIAL_X = 50;
	public final static int INITIAL_Y = GameStage.WINDOW_HEIGHT - 400;

	public static final String RIGHT = "r";
	public static final String LEFT = "l";

	private boolean withCheese;

	private boolean hasJumped;
	private int yBeforeJump;

	public Mouse(int x, int y) {
		super(x, y);
		this.imgStr = "assets/mouse";
		this.imgDirection = Mouse.RIGHT;
		this.imgNum = 1;
		this.withCheese = false;
		this.maxJumpHeight = MOUSE_SIZE * 3;
		this.jumpVelocity = MOUSE_SPEED;
		this.name = "Beam";
		this.port = 2000;

		this.hasJumped = false;
		this.yBeforeJump = INITIAL_Y;

		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT); // open canvas with same size as stage
		this.gc = canvas.getGraphicsContext2D();
		this.loadImage(this.getFullImgStr(), Mouse.MOUSE_SIZE);
	}

	// getters
	String getFullImgStr() {
		return this.imgStr + "_" + this.imgNum + "_" + this.imgDirection + ".png";
	}

	boolean checkHasJumped() {
		return this.hasJumped;
	}

	boolean checkWithCheese() {
		return this.withCheese;
	}

	int getYBeforeJump() {
		return this.yBeforeJump;
	}

	int getMaxJumpHeight() {
		return this.maxJumpHeight;
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

	void setWithCheese() {
		this.withCheese = true;
	}

	void setWithoutCheese() {
		this.withCheese = false;
	}

	void setHasJumped() {
		this.hasJumped = !this.hasJumped;
	}

	void setYBeforeJump() {
		this.yBeforeJump = this.y;
	}

	void setMaxJumpHeight(int val) {
		this.maxJumpHeight = val;
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
}
