import java.awt.Image;


public class MasslessObject extends Renderable implements SpringMate{
	Shape shape;
	public MasslessObject(Image image, double x, double y){
		super(image, x, y);
	}

	@Override
	public void addAffectingForce(ForceSource s) {
		
	}

	@Override
	public MyVector getSpeed() {
		// TODO Auto-generated method stub
		return (new MyVector(0,0));
	}
}
