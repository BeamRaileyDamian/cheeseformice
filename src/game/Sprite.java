package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected Image img;
	protected int x, y, dx, dy;
	protected double width;
	protected double height;

	public Sprite(int xPos, int yPos){
		this.x = xPos;
		this.y = yPos;
	}

	// method to set the object's image
	protected void loadImage(String str, int size){
		try{
			Image img = new Image(str, size, size, false, false);
			this.img = img;
	        this.setSize();
		} catch(Exception e){}
	}

	// method to set the image to the image view node
	void render(GraphicsContext gc){
		gc.drawImage(this.img, this.x, this.y);
    }

	// method to set the object's width and height properties
	private void setSize(){
		this.width = this.img.getWidth();
	    this.height = this.img.getHeight();
	}

	// method that will check for collision of two sprites
	boolean collidesWith(Sprite rect2)	{
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();

		return rectangle1.intersects(rectangle2);
	}

//	// method that will check for collision of two items
//	boolean collidesWith(Item rect2)	{
//		Rectangle2D rectangle1 = this.getBounds();
//		Rectangle2D rectangle2 = rect2.getBounds();
//
//		return rectangle1.intersects(rectangle2);
//	}

	// method that will return the bounds of an image
	private Rectangle2D getBounds(){
		return new Rectangle2D(this.x, this.y, this.width, this.height);
	}

	// method to return the image
	Image getImage(){
		return this.img;
	}
	// getter
	int getX() {
    	return this.x;
	}

	// getter
	int getY() {
    	return this.y;
	}

	// getter
	int getDx() {
    	return this.dx;
	}

	// getter
	int getDy() {
    	return this.dy;
	}

	// setters
	void setDX(int dx){
		this.dx = dx;
	}

	// setter
	void setDY(int dy){
		this.dy = dy;
	}

	// setter
	void setWidth(double val){
		this.width = val;
	}

	// setter
	void setHeight(double val){
		this.height = val;
	}
}
