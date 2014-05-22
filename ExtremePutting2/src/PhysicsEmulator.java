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

	// Ljud

	// Våra bilder
	private BufferedImage MassObjectImage;
	private BufferedImage SpringImage;
	private BufferedImage FixedPointImage;
	private BufferedImage HoleImage;
	private BufferedImage ballToHit;
	private BufferedImage queBallImage;
	private BufferedImage BallYellowImage;
	private BufferedImage BGGreenImage;
	private BufferedImage HWall;
	private BufferedImage VWall;
	private BufferedImage TJones;
	private BufferedImage BallBigImage;

	private BufferedImage BoosterUpImage;
	private BufferedImage BoosterRightImage;
	private BufferedImage BoosterDownImage;
	private BufferedImage BoosterLeftImage;

	// allting på skärmen
	Renderable bg;
	Renderable tj;
	Hole hole;
	ArrayList<MassObject> masses;
	ArrayList<MasslessObject> fixedShapes;
	ArrayList<ColisionMate> ColidingShapes;
	ArrayList<Spring> springs;
	ArrayList<Booster> boosters;
	AccelerationSource gravity;
	AccelerationSource antigravity;
	ForceSource grassFriction;

	ControllableBall queBall;
	MassObject targetBall;
	MassObject movingHinder;

	private Controller controller;

	/**
	 * Create a GameCanvas
	 */
	public PhysicsEmulator() {
		// Sätter storleken på målarduken
		setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		setPreferredSize(getMinimumSize());
		setMaximumSize(getMinimumSize());

		controller = new Controller();
		addKeyListener(controller);
		addMouseListener(controller);
		addMouseMotionListener(controller);

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
			ballToHit = ImageIO.read(getClass().getResource(
					"/assets/baltoHit.png"));
			queBallImage = ImageIO.read(getClass().getResource(
					"/assets/cueBall.png"));
			BallYellowImage = ImageIO.read(getClass().getResource(
					"/assets/bally.png"));
			BallBigImage = ImageIO.read(getClass().getResource(
					"/assets/bigball.png"));
			BGGreenImage = ImageIO.read(getClass().getResource(
					"/assets/bggreen.png"));
			HWall = ImageIO.read(getClass().getResource(
					"/assets/HorizontalWall.png"));
			VWall = ImageIO.read(getClass().getResource(
					"/assets/VerticalWall.png"));
			TJones = ImageIO.read(getClass()
					.getResource("/assets/tomjones.png"));
			BoosterDownImage = ImageIO.read(getClass().getResource(
					"/assets/boost_down_big.png"));
			BoosterRightImage = ImageIO.read(getClass().getResource(
					"/assets/boost_right_big.png"));
			BoosterUpImage = ImageIO.read(getClass().getResource(
					"/assets/boost_up_big.png"));
			BoosterLeftImage = ImageIO.read(getClass().getResource(
					"/assets/boost_left_big.png"));

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Skapa allt på skärmen
		masses = new ArrayList<MassObject>();
		fixedShapes = new ArrayList<MasslessObject>();
		springs = new ArrayList<Spring>();
		ColidingShapes = new ArrayList<ColisionMate>();
		boosters = new ArrayList<Booster>();

		bg = new Renderable(BGGreenImage, 600, 400);
		tj = new Renderable(TJones, 600, 400);

		Sound.playSound("SeventiesPornMusic.wav");
		hole = new Hole(HoleImage, 1100, 100);

		queBall = new ControllableBall(queBallImage, 25, 100, 700, new Circle(
				queBallImage.getHeight() / 2), controller);
		targetBall = new MassObject(ballToHit, 25, 400, 700, new Circle(
				ballToHit.getHeight() / 2));
		movingHinder = new MassObject(BallBigImage, 400, 1000, 550, new Circle(
				BallBigImage.getHeight() / 2));

		masses.add(queBall);
		masses.add(targetBall);
		masses.add(movingHinder);

		boosters.add(new Booster(BoosterUpImage, Direction.UP, 100, 80, 300));
		boosters.add(new Booster(BoosterDownImage, Direction.DOWN, 100, 400, 80));
		boosters.add(new Booster(BoosterRightImage, Direction.RIGHT, 100, 180,
				300));
		boosters.add(new Booster(BoosterLeftImage, Direction.LEFT, 100, 600,
				500));
		boosters.add(new Booster(BoosterLeftImage, Direction.LEFT, 100, 700,
				200));
		boosters.add(new Booster(BoosterUpImage, Direction.UP, 100, 1000,
				700));

		fixedShapes.add(new MasslessObject(HWall, 600, 7, new Rectangle(HWall
				.getWidth(), HWall.getHeight())));
		fixedShapes.add(new MasslessObject(HWall, 600, 792, new Rectangle(HWall
				.getWidth(), HWall.getHeight())));

		fixedShapes.add(new MasslessObject(VWall, 7, 400, new Rectangle(VWall
				.getWidth(), VWall.getHeight())));
		fixedShapes.add(new MasslessObject(VWall, 1192, 400, new Rectangle(
				VWall.getWidth(), VWall.getHeight())));
		fixedShapes.add(new MasslessObject(BallBigImage, 450, 300, new Circle(
				BallBigImage.getHeight() / 2)));
		fixedShapes.add(new MasslessObject(BallBigImage, 1000, 200, new Circle(
				BallBigImage.getHeight() / 2)));

		springs.add(new Spring(60, 300, 0, movingHinder, fixedShapes.get(1),
				SpringImage));
		springs.add(new Spring(60, 300, 0, movingHinder, fixedShapes.get(3),
				SpringImage));
		springs.add(new Spring(60, 300, 0, movingHinder, fixedShapes.get(0),
				SpringImage));
		springs.add(new Spring(60, 300, 0, movingHinder, fixedShapes.get(2),
				SpringImage));

		ColidingShapes.addAll(fixedShapes);
		ColidingShapes.addAll(masses);

		grassFriction = new ForceSource() {
			public MyVector getForceVector(Object o) {
				ColisionMate cm = (ColisionMate) o;
				MyVector frictionForce = cm.getSpeed().clone();
				if (cm.getSpeed().magnitude() > 0.00001) {
					frictionForce.devide(cm.getSpeed().magnitude());
				} else {
					frictionForce = new MyVector(0, 0);
				}
				frictionForce.multiply(-6 * cm.getMass());
				return frictionForce;
			}

		};

		gravity = new AccelerationSource() {
			public MyVector getAccVector(Object o) {
				return (new MyVector(0, 98.2));
			}
		};
		for (MassObject m : masses) {
			if (m != movingHinder)
				m.addAffectingForce(grassFriction);
		}
		for (MassObject m : masses) {
			for (Booster b : boosters) {
				m.addAffectingAcceleration(b);
			}
		}
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

		if (running == false) {
			tj.render(g);
			strategy.show();
		} else {
			// Måla bakgrund, zombies och kulor
			// g.drawImage(backgroundImage, 0, 0, null);

			g.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			bg.render(g);
			hole.render(g);

			for (int i = 0; i < fixedShapes.size(); i++) {
				fixedShapes.get(i).render(g);
			}
			for (Booster b : boosters) {
				b.render(g);
			}
			for (int i = 0; i < masses.size(); i++) {
				masses.get(i).render(g);
			}
			for (int i = 0; i < springs.size(); i++) {
				springs.get(i).render(g);
			}
			// Gör så att allt vi målat ut synns
			strategy.show();
		}
	}

	/**
	 * Update method updates the game logic
	 * 
	 * @param delta
	 *            the delta in milliseconds since last iteration
	 */
	private void update(long delta) {
		double totalVelocity = 0;
		for (MassObject mass : masses) {
			mass.update(delta);
			if(mass!=movingHinder){
			totalVelocity += mass.speed.magnitude();
			}
		}
		if (totalVelocity <= 1&&!queBall.controllable()) {
			queBall.enable();
			System.out.println("Du kan skjuta nu");
		}else if(totalVelocity >= 1&&queBall.controllable()){queBall.disable(); System.out.println("V�nta p� att bollarna ska bli stilla igen");}
		for (Spring spring : springs) {
			spring.update();
		}
		if (ColisionHandler.checkIfCircleCollidesWithHole(targetBall, hole)) {
			running = false;
			Sound.playSound("Applause2.wav");
			render();
		}
		for (int i = 0; i < ColidingShapes.size(); i++) {
			for (int j = i + 1; j < ColidingShapes.size(); j++) {
				ColisionHandler.resolveColision(ColidingShapes.get(i),
						ColidingShapes.get(j));
			}
		}
	}
}
