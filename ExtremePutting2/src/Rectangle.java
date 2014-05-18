import java.awt.Color;

public class Rectangle implements Shape{
	double width;
	double height;
	Color color;

	public Rectangle(double _width, double _height, Color _color) {
		height=_height;
		width=_width;
		color=_color;
	}
	public Rectangle(double _width, double _height) {
		height=_height;
		width=_width;
		color = new Color(0, 0, 0);
	}
	@Override
	public boolean isInside(MyVector pointToCheck, MyVector centerOfShape) {
		return false;
	}
}
