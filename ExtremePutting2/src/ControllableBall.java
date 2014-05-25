import java.awt.Image;

/**
 * Our hero!
 * 
 * @author Mikael Silv√©n
 */
public class ControllableBall extends MassObject {
	private boolean disabled;
	Controller controller;

	public ControllableBall(Image image, int xpos, int ypos, double _mass,
			Shape _shape, Controller _controller) {
		super(image, xpos, ypos, _mass, _shape);
		disabled = true;
		controller = _controller;
	}

	@Override
	public void update(long delta) {

		if (disabled) {
			MyVector netForce = new MyVector(0, 0);

			for (ForceSource s : affectingForces) {
				netForce.add(s.getForceVector(this));
			}
			MyVector netAcc = netForce.clone();
			netAcc.devide(mass);

			for (AccelerationSource a : affectingAccs) {
				netAcc.add(a.getAccVector(this));
			}
			speed.x += netAcc.x * (delta / 1000.0);
			speed.y += netAcc.y * (delta / 1000.0);
			xpos += speed.x * (delta / 1000.0);
			ypos += speed.y * (delta / 1000.0);

		} else {
			if (controller.mouseWasDown()) {
				Sound.playSound("ForeGolf.wav");
				setSpeed(new MyVector((controller.mouseX - xpos)/2,
						(controller.mouseY - ypos)/2));
				Sound.playSound("GolfSwing.wav");
				disable();
				System.out.println("Du har skjutit, hastighet i x-led ‰r nu: "
						+ speed.x + " och i y-led: " + speed.y);
			}
		}

	}

	public void push(double dx, double dy) {
		xpos -= dx;
		ypos -= dy;
	}

	public void disable() {
		disabled = true;
	}

	public void enable() {
		disabled = false;
	}
	public boolean controllable(){
		return !disabled;
	}
}
