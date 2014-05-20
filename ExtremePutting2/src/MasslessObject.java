import java.awt.Image;


public class MasslessObject extends Renderable implements SpringMate, ColisionMate{
	Shape shape; //This is for handling colisions. I don't know if there will be support for other than round and square.
	public MasslessObject(Image image, double x, double y, Shape _shape){
		super(image, x, y);
	}

	public void addAffectingForce(ForceSource s) {
		
	}

	public MyVector getSpeed() {
		// TODO Auto-generated method stub
		return (new MyVector(0,0));
	}

	public MyVector getPosition() {
		// TODO Auto-generated method stub
		return (new MyVector(xpos,ypos));
	}

	public Shape getShape() {
		return shape;
	}

	public void setSpeed(MyVector v) {
		//Can't move massless objects.
	}

	public double getMass() {
		return 6000000;
	}
}
