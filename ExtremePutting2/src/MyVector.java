
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
	public void subtract(MyVector v) {
		x-=v.x;
		y-=v.y;
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
	public static double angleBetweenVectors(MyVector v, MyVector u){
		return Math.acos(scalarProduct(v, u)/(v.magnitude()*u.magnitude()));
	}
	public double magnitude() {
		return Math.sqrt(x*x+y*y);
	}
}
