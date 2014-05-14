import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Base class for everything that can be rendered
 * @author Mikael Silvén
 */
public class Renderable {

	Image image;
	protected double ypos;
	protected double xpos;
	protected double angle;
	
	public Renderable(Image image, double x, double y) {
		this.image = image;
		this.xpos = x;
		this.ypos = y;
		this.angle = 0;
	}
	
	public void render(Graphics2D g) {
		g.translate(xpos, ypos);
		g.rotate(angle);
		g.drawImage(image, -image.getWidth(null) / 2,
				           -image.getHeight(null) / 2, null);
		g.rotate(-angle);
		g.translate(-xpos, -ypos);
	}
	
	public double getY() {
		return ypos;
	}
	
	public double getX() {
		return xpos;
	}
}
