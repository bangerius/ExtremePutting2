import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

public class MassObject extends Renderable implements SpringMate, ColisionMate{
	double mass;
	Shape shape; //This is for handling colisions. I don't know if there will be support for other than round and square.
	MyVector speed;
	Set<ForceSource> affectingForces;
	Set<AccelerationSource> affectingAccs;
	
	public MassObject(Image image, double _mass, double x, double y, Shape _shape) {
		
		super(image, x, y);
		mass= _mass;
		speed = new MyVector(0.0,0.0);
		affectingForces = new HashSet<ForceSource>();
		affectingAccs = new HashSet<AccelerationSource>();
		shape=_shape;
	}
	
	public void addAffectingForce(ForceSource s){
		affectingForces.add(s);
	}
	
	public void addAffectingAcceleration(AccelerationSource a){
		affectingAccs.add(a);
	}
	
	public void update(long delta){
		MyVector netForce= new MyVector(0,0);
		
		for (ForceSource s : affectingForces) {
			netForce.add(s.getForceVector(this));
		}
		MyVector netAcc = netForce.clone();
		netAcc.devide(mass);
		
		for (AccelerationSource a : affectingAccs) {
			netAcc.add(a.getAccVector());
		}
		
		speed.x += netAcc.x * (delta/1000.0);
		speed.y += netAcc.y * (delta/1000.0);
		xpos += speed.x * (delta/1000.0);
		ypos += speed.y * (delta/1000.0);
	}

	@Override
	public MyVector getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}
}
