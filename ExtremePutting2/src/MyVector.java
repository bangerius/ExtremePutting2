
public class MyVector {
	double x, y;
	public MyVector(double _x, double _y){
		x=_x;
		y=_y;
	}
	public void add(MyVector v) {
		x+=v.x;
		y+=v.y;
	}
	public void devide(double d) {
		x/=d;
		y/=d;
	}
	public void multiply(double d) {
		x*=d;
		y*=d;
	}
	@Override
	public MyVector clone(){
		return (new MyVector(x, y));
	}
	public static double scalarProduct(MyVector v, MyVector u) {
		return v.x*u.x+ v.y*u.y;
	}
}
