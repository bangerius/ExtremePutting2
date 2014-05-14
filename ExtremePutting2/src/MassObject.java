import java.awt.Image;
import java.util.HashSet;
import java.util.Set;

public class MassObject extends Renderable implements SpringMate {
	double mass;
	MyVector speed;
	Set<ForceSource> affectingForces;
	Set<AccelerationSource> affectingAccs;
	
	public MassObject(Image image, double _mass, double x, double y) {
		super(image, x, y);
		mass= _mass;
		speed = new MyVector(0.0,0.0);
		affectingForces = new HashSet<ForceSource>();
		affectingAccs = new HashSet<AccelerationSource>();
	}
	
	public void addAffectingForce(ForceSource s){
		affectingForces.add(s);
	}
	
	public void addAffectingAcceleration(AccelerationSource a){
		affectingAccs.add(a);
	}
	
	public void update(long delta){
		MyVector netForce= new MyVector(0,0);
		double dx=0,dy=0;
		
		for (ForceSource s : affectingForces) {
			netForce.add(s.getForceVector(this));
		}
		MyVector netAcc = netForce.clone();
		netAcc.devide(mass);
		
		for (AccelerationSource a : affectingAccs) {
			netAcc.add(a.getAccVector());
		}
		
		
		dx = speed.x * (delta/1000.0) + netAcc.x * (delta/1000.0) * (delta/1000.0) /2;
		dy = speed.y * (delta/1000.0) + netAcc.y * (delta/1000.0) * (delta/1000.0) /2;
		speed.x += netAcc.x * (delta/1000.0);
		speed.y += netAcc.y * (delta/1000.0);
		xpos += dx;
		ypos += dy;
	}

	@Override
	public MyVector getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}
}
