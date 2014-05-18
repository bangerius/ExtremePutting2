import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;


public class Spring extends Renderable implements ForceSource{
	double springConstant;
	double desiredLength;
	double currentLength;
	double frictionConstant;
	SpringMate mate1;
	SpringMate mate2;
	
	public Spring(double _springConstant, double _desiredLength, SpringMate _mate1, SpringMate _mate2, Image image){
		super(image, 0, 0);
		frictionConstant=-0.1;
		springConstant = _springConstant;
		desiredLength = _desiredLength;
		mate1 = _mate1;
		mate2 = _mate2;
		currentLength= Math.sqrt((mate1.getX()-mate2.getX())*(mate1.getX()-mate2.getX())+(mate1.getY()-mate2.getY())*(mate1.getY()-mate2.getY()));
		mate1.addAffectingForce(this);
		mate2.addAffectingForce(this);
	}

	@Override
	public MyVector getForceVector(Object o) {
		try {
			SpringMate m = (SpringMate)o;
			double mag = springConstant*(currentLength-desiredLength);
			double direction = Math.atan2(m.getX()-centerX(), m.getY()-centerY())-Math.PI/2;
			
			MyVector inflictingForce=new MyVector(-mag*Math.cos(direction), mag*Math.sin(direction));
			
			MyVector FrictionForce = inflictingForce.clone();
			FrictionForce.multiply((MyVector.scalarProduct(m.getSpeed(), inflictingForce))/(MyVector.scalarProduct(inflictingForce, inflictingForce)));
			FrictionForce.multiply(frictionConstant);
			
			inflictingForce.add(FrictionForce);
			
			return inflictingForce;
			
		} catch (Exception e) {
			System.out.println("Object is not a springmate");
			return null;
		}
	}
	@Override
	public void render(Graphics2D g) {
		g.translate(xpos, ypos);
		g.rotate(angle);
		g.drawImage(image, (int)-currentLength/2, -image.getHeight(null)/2, (int)currentLength, image.getHeight(null), null);
		g.rotate(-angle);
		g.translate(-xpos, -ypos);
	}
	private double centerX(){
		return (mate1.getX() + mate2.getX())/2;
	}
	private double centerY(){
		return (mate1.getY() + mate2.getY())/2;
	}
	
	public void update(){
		currentLength= Math.sqrt((mate1.getX()-mate2.getX())*(mate1.getX()-mate2.getX())+(mate1.getY()-mate2.getY())*(mate1.getY()-mate2.getY()));
		xpos = centerX();
		ypos = centerY();
		angle = Math.atan2(mate1.getY()-mate2.getY(),mate1.getX()-mate2.getX());
	}
}
