import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * PhysicsEmulator class encapsulates an Emulator
 * @author Sebastian B�ngerius
 */
public class PhysicsEmulator extends Canvas implements Runnable {

	/* Konstanter som säger hur stort fönstret är, 
	 * hur många zombie som börjar på fältet,
	 * hur långt en kula max får färdas,
	 * och hur snabbt zombie spawnar samt
	 * hur snabbt man får skjuta */
	
	private static final int WINDOW_WIDTH = 1100;
	private static final int WINDOW_HEIGHT = 600;
	
	private boolean running;
	
	// Våra bilder
	private BufferedImage MassObjectImage;
	private BufferedImage SpringImage;
	private BufferedImage FixedPointImage;

	//allting på skärmen
	MasslessObject fp;
	MassObject mass1;
	MassObject mass2;
	Spring s1;
	Spring s2;
	AccelerationSource gravity;

	/**
	 * Create a GameCanvas
	 */
	public PhysicsEmulator() {
		// Sätter storleken på målarduken
		setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		setPreferredSize(getMinimumSize());
		setMaximumSize(getMinimumSize());
		
		// Försök ladda in filer, krasha om något går snett
		try {			
			MassObjectImage = ImageIO.read(getClass().getResource(
					"/assets/bullet.png"));
			SpringImage = ImageIO.read(getClass().getResource(
					"/assets/spring.png"));
			FixedPointImage = ImageIO.read(getClass().getResource(
					"/assets/masslesspoint.png"));

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Skapa allt på skärmen
		fp = new MasslessObject(FixedPointImage, WINDOW_WIDTH/2, 50);
		mass1 = new MassObject(MassObjectImage, 35, 450, 50);
		mass2 = new MassObject(MassObjectImage, 20, 250, 150);

		s1 = new Spring(60, 200, mass1, mass2, SpringImage);
		s2 = new Spring(60, 200, fp, mass1, SpringImage);
		gravity = new AccelerationSource(){
			@Override
			public MyVector getAccVector() {
				return (new MyVector(0, 98.2));
			}
			
		};
		mass1.addAffectingAcceleration(gravity);
		mass2.addAffectingAcceleration(gravity);
	}

	/**
	 * Start the game in the GameCanvas
	 */
	public void start() {
		if (!running) {
			Thread t = new Thread(this);
			// Trippelbuffring, prova att ändra denna siffran.
			createBufferStrategy(3);
			running = true;
			t.start();
		}
	}

	/**
	 * Main function, sets up a JFrame and starts a game
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("Mitt coola demo");
		PhysicsEmulator game = new PhysicsEmulator();
		window.add(game);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		game.start();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Method for @see {@link Runnable} implementation
	 * Main Game loop. Runs more or less forever
	 */
	@Override
	public void run() {
		long last = System.currentTimeMillis();
		while (running) {
			long now = System.currentTimeMillis();
			long delta = now - last;
			update(delta);
			render();

			// Låt tråden sova i 10 millisekunder, 
			// prova att ändra denna siffran.
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			last = now;
		}
	}

	/**
	 * Render method draws everything on the screen
	 */
	private void render() {
		// Hämta grafikobjektet
		BufferStrategy strategy = getBufferStrategy();
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

		// Måla bakgrund, zombies och kulor 
		//g.drawImage(backgroundImage, 0, 0, null);
		g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		mass1.render(g);
		mass2.render(g);
		s1.render(g);
		s2.render(g);
		fp.render(g);
		
		// Gör så att allt vi målat ut synns
		strategy.show();
	}

	/**
	 * Update method updates the game logic
	 * @param delta the delta in milliseconds since last iteration
	 */
	private void update(long delta) {
		mass1.update(delta);
		mass2.update(delta);
		s1.update();
		s2.update();
	}
}
