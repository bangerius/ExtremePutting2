import java.awt.Image;


public class Booster extends Renderable implements AccelerationSource{
	MyVector acc;
	double halfHeight;
	double halfWidth;
	public Booster(Image image, Direction d, double magnitude, double x, double y){
		super(image, x, y);
		
		halfHeight=image.getHeight(null)/2;
		halfWidth=image.getWidth(null)/2;
		
		switch (d) {
		case UP:
			acc = new MyVector(0, (-1)*magnitude);
			break;
		case DOWN:
			acc = new MyVector(0, magnitude);
			break;
		case LEFT:
			acc = new MyVector((-1)*magnitude,0);
			break;
		case RIGHT:
			acc = new MyVector(magnitude,0);
			break;
		}
	}

	@Override
	public MyVector getAccVector(Object o) {
		MassObject ball = (MassObject) o;
		MyVector circleDistance = new MyVector(Math.abs(ball.getPosition().x
				- xpos), Math.abs(ball.getPosition().y - ypos));
		if (circleDistance.x<halfWidth&&circleDistance.y<halfHeight){
			return acc;
		}
		return new MyVector(0,0);
	}
}
