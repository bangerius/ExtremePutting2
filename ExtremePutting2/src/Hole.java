import java.awt.Image;


public class Hole extends Renderable implements ColisionMate{
	Shape shape; //This is for handling colisions. I don't know if there will be support for other than round and square.
	public Hole(Image image, double x, double y) {
		super(image, x, y);
		shape = new Circle(image.getWidth(null)/5);
	}

	public MyVector getPosition() {
		return new MyVector(xpos, ypos);
	}

	public Shape getShape() {
		return shape;
	}

	public MyVector getSpeed() {
		return new MyVector(0,0);
	}

	public void setSpeed(MyVector v) {
		
	}

	public double getMass() {
		return 700000;
	}

}
