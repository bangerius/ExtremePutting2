import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * PhysicsEmulator class encapsulates an Emulator
 * 
 * @author Sebastian B�ngerius
 */
public class PhysicsEmulator extends Canvas implements Runnable {

	/*
	 * Konstanter som säger hur stort fönstret är, hur många zombie som
	 * börjar på fältet, hur långt en kula max får färdas, och hur snabbt
	 * zombie spawnar samt hur snabbt man får skjuta
	 */

	private static final int WINDOW_WIDTH = 1200;
	private static final int WINDOW_HEIGHT = 800;

	private boolean running;
	
	//Ljud


	// Våra bilder
	private BufferedImage MassObjectImage;
	private BufferedImage SpringImage;
	private BufferedImage FixedPointImage;
	private BufferedImage HoleImage;
	private BufferedImage BallBlueImage;
	private BufferedImage BallGreenImage;
	private BufferedImage BallYellowImage;
	private BufferedImage BGGreenImage;
	private BufferedImage HWall;
	private BufferedImage VWall;

	// allting på skärmen
	MasslessObject bg;
	Hole hole;
	ArrayList<MassObject> masses;
	ArrayList<Spring> springs;
	AccelerationSource gravity;
	AccelerationSource antigravity;
	AccelerationSource testhit;

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
			HoleImage = ImageIO.read(getClass().getResource(
					"/assets/holeupd.png"));
			MassObjectImage = ImageIO.read(getClass().getResource(
					"/assets/bullet.png"));
			SpringImage = ImageIO.read(getClass().getResource(
					"/assets/spring.png"));
			FixedPointImage = ImageIO.read(getClass().getResource(
					"/assets/masslesspoint.png"));
			BallBlueImage = ImageIO.read(getClass().getResource(
					"/assets/ballb.png"));
			BallGreenImage = ImageIO.read(getClass().getResource(
					"/assets/ballg.png"));
			BallYellowImage = ImageIO.read(getClass().getResource(
					"/assets/bally.png"));
			BGGreenImage = ImageIO.read(getClass().getResource(
					"/assets/bggreen.png"));
			HWall = ImageIO.read(getClass().getResource(
					"/assets/HorizontalWall.png"));
			VWall = ImageIO.read(getClass().getResource(
					"/assets/VerticalWall.png"));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Skapa allt på skärmen
		masses = new ArrayList<MassObject>();
		springs = new ArrayList<Spring>();
		
		bg = new MasslessObject(BGGreenImage, 750, 500, 
				new Circle(0));
		hole = new Hole(HoleImage, 1100, 100);

		masses.add(new MassObject(BallYellowImage, 25, 100, 700, new Circle(
				BallYellowImage.getHeight() / 2)));
		masses.add(new MassObject(BallYellowImage, 25, 1100, 350, new Circle(
				BallYellowImage.getHeight() / 2)));
		
		masses.add(new MassObject(HWall, 10000000, 600, 7, new Rectangle(
				HWall.getWidth(), HWall.getHeight())));
		masses.add(new MassObject(HWall, 10000000, 600, 792, new Rectangle(
				HWall.getWidth(), HWall.getHeight())));
		
		masses.add(new MassObject(VWall, 10000000, 7, 400, new Rectangle(
				VWall.getWidth(), VWall.getHeight())));
		masses.add(new MassObject(VWall, 10000000, 1192, 400, new Rectangle(
				VWall.getWidth(), VWall.getHeight())));

		testhit = new AccelerationSource() {
			public MyVector getAccVector() {
				return (new MyVector(60.15, -20));
			}

		};
		masses.get(0).addAffectingAcceleration(testhit);
		Sound.playSound("GolfSwing.wav");
		
		gravity = new AccelerationSource() {
			public MyVector getAccVector() {
				return (new MyVector(0, 98.2));
			}
		};
		antigravity = new AccelerationSource() {
			public MyVector getAccVector() {
				return (new MyVector(0, -98.2));
			}
		};
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
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame window = new JFrame("-=ExtremePutting2=-");
		PhysicsEmulator game = new PhysicsEmulator();
		window.add(game);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		game.start();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Method for @see {@link Runnable} implementation Main Game loop. Runs more
	 * or less forever
	 */
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
				Thread.sleep(5);
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
		// g.drawImage(backgroundImage, 0, 0, null);

		g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		bg.render(g);
		hole.render(g);
		for (int i = 0; i < masses.size(); i++) {
			masses.get(i).render(g);
		}
		for (int i = 0; i < springs.size(); i++) {
			springs.get(i).render(g);
		}
		// Gör så att allt vi målat ut synns
		strategy.show();
	}

	/**
	 * Update method updates the game logic
	 * 
	 * @param delta
	 *            the delta in milliseconds since last iteration
	 */
	private void update(long delta) {
		for (int i = 0; i < masses.size(); i++) {
			masses.get(i).update(delta);
		}
//		if (ColisionHandler.checkIfCircleCollidesWithCircle(hole, masses.get(1))){
//			for (int i = 0; i < masses.size(); i++) {
//				masses.get(i).update(delta); 	skall vara remove velocity
//			}
//		}
		for (int i = 0; i < masses.size(); i++) {
			for (int j = i + 1; j < masses.size(); j++) {
				ColisionHandler.resolveColision(masses.get(i), masses.get(j));
			}
		}
		for (Spring spring : springs) {
			spring.update();
		}
	}
}
