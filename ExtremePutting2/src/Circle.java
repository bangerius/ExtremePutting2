import java.awt.Color;


public class Circle implements Shape{
	double radius;
	Color color;

	public Circle(double _radius, Color _color) {
		radius=_radius;
		color=_color;
	}
	public Circle(double _radius) {
		radius=_radius;
		color = new Color(0, 0, 0);
	}
	@Override
	public boolean isInside(MyVector pointToCheck, MyVector centerOfShape) {
		// TODO Auto-generated method stub
		return false;
	}
}
