public class ColisionHandler {
	public static void resolveColision(ColisionMate a, ColisionMate b) {
		if (a.getShape().getClass() == Circle.class
				&& b.getShape().getClass() == Circle.class) {
			if (checkIfCircleCollidesWithCircle(a, b)) {
				colideCircles(a, b);
				a.update(5);
				b.update(5);
			}
		} else if (a.getShape().getClass() == Circle.class
				&& b.getShape().getClass() == Rectangle.class) {
			resolveColisionBetweenCircleAndRectangle(a, b);
			a.update(3);
			b.update(3);
		} else if (b.getShape().getClass() == Circle.class
				&& a.getShape().getClass() == Rectangle.class) {
			resolveColisionBetweenCircleAndRectangle(b, a);
			a.update(3);
			b.update(3);
		} else {
			// Do nothing for now
		}
	}

	private static void resolveColisionBetweenCircleAndRectangle(
			ColisionMate a, ColisionMate b) {
		Circle circle = (Circle) a.getShape();
		Rectangle rect = (Rectangle) b.getShape();
		// Absolute values so that only one corner has to be evaluated.
		MyVector circleDistance = new MyVector(Math.abs(a.getPosition().x
				- b.getPosition().x), Math.abs(a.getPosition().y - b.getPosition().y));

		if (circleDistance.x <= (rect.width / 2+circle.radius)) {
			a.setSpeed(new MyVector((a.getMass() - b.getMass())
					/ (a.getMass() + b.getMass()) * a.getSpeed().x
					+ (2 * b.getMass()) / (a.getMass() + b.getMass())
					* b.getSpeed().x, a.getSpeed().y));
			b.setSpeed(new MyVector((b.getMass() - a.getMass())
					/ (b.getMass() + a.getMass()) * b.getSpeed().x
					+ (2 * a.getMass()) / (b.getMass() + a.getMass())
					* a.getSpeed().x, b.getSpeed().y));
		} if (circleDistance.y <= (rect.height / 2+circle.radius)) {
			a.setSpeed(new MyVector(a.getSpeed().x, (a.getMass() - b.getMass())
					/ (a.getMass() + b.getMass()) * a.getSpeed().y
					+ (2 * b.getMass()) / (a.getMass() + b.getMass())
					* b.getSpeed().y));
			b.setSpeed(new MyVector(b.getSpeed().x, (b.getMass() - a.getMass())
					/ (b.getMass() + a.getMass()) * b.getSpeed().y
					+ (2 * a.getMass()) / (b.getMass() + a.getMass())
					* a.getSpeed().y));
		} else {
			double cornerDistance_sq = Math.pow(
					(circleDistance.x - rect.width / 2), 2)
					+ Math.pow((circleDistance.y - rect.height / 2), 2);
			if (cornerDistance_sq <= Math.pow(circle.radius, 2)) {
				// Lös vad som händer när ett hörn träffas.
			}
		}
	}

	public static boolean checkIfCircleCollidesWithCircle(ColisionMate a,
			ColisionMate b) {
		Circle aCircle = (Circle) a.getShape();
		Circle bCircle = (Circle) b.getShape();
		double dx = a.getPosition().x - b.getPosition().x;
		double dy = a.getPosition().y - b.getPosition().y;
		return (Math.sqrt(dx * dx + dy * dy) <= (aCircle.radius + bCircle.radius));
	}
	
	public static boolean checkIfCircleCollidesWithHole(ColisionMate a,
			Hole b) {
		Circle aCircle = (Circle) a.getShape();
		Circle bCircle = (Circle) b.getShape();
		double dx = a.getPosition().x - b.getPosition().x;
		double dy = a.getPosition().y - b.getPosition().y;
		return (Math.sqrt(dx * dx + dy * dy) <= (bCircle.radius));
	}

	public static void colideCircles(ColisionMate a, ColisionMate b) {
		// Vektorer som är vinkelräta mot stöten och således ej påverkar den
		MyVector aOtherComposantSpeed;
		MyVector bOtherComposantSpeed;

		// Vektorn mellan de olika objekten
		MyVector cmFromAToB = new MyVector(a.getPosition().x
				- b.getPosition().x, a.getPosition().y - b.getPosition().y);

		// a:s vektor i kolisionsriktningen
		MyVector aColisionComposandSpeed = cmFromAToB.clone();
		aColisionComposandSpeed.devide(cmFromAToB.magnitude());
		if (Math.abs(a.getSpeed().x) < 0.0000001 && Math.abs(a.getSpeed().y) < 0.0000001) {
			aColisionComposandSpeed = new MyVector(0, 0);
		} else {
			aColisionComposandSpeed.multiply(a.getSpeed().magnitude()
					* Math.cos(MyVector.angleBetweenVectors(a.getSpeed(),
							cmFromAToB)));
		}
		// b:s vektor i kolisionsriktningen
		MyVector bColisionComposandSpeed = cmFromAToB.clone();
		bColisionComposandSpeed.devide(cmFromAToB.magnitude());
		if (Math.abs(b.getSpeed().x) < 0.0000001 && Math.abs(b.getSpeed().y) < 0.0000001) {
			bColisionComposandSpeed = new MyVector(0, 0);
		} else {
			bColisionComposandSpeed.multiply(b.getSpeed().magnitude()
					* Math.cos(MyVector.angleBetweenVectors(b.getSpeed(),
							cmFromAToB)));
		}
		// a:s vinkelräta vektor
		aOtherComposantSpeed = a.getSpeed().clone();
		aOtherComposantSpeed.subtract(aColisionComposandSpeed);

		// b:s vinkelräta vektor
		bOtherComposantSpeed = b.getSpeed().clone();
		bOtherComposantSpeed.subtract(bColisionComposandSpeed);

		// Sätt ny hastighet för a
		MyVector aNewSpeed = aColisionComposandSpeed.clone();
		MyVector btempSpeed = bColisionComposandSpeed.clone();
		aNewSpeed.multiply((a.getMass() - b.getMass())
				/ (a.getMass() + b.getMass()));
		btempSpeed.multiply(2 * b.getMass() / (a.getMass() + b.getMass()));
		aNewSpeed.add(btempSpeed);
		aNewSpeed.add(aOtherComposantSpeed);
		a.setSpeed(aNewSpeed);

		// Sätt ny hastighet för b
		MyVector bNewSpeed = bColisionComposandSpeed.clone();
		MyVector atempSpeed = aColisionComposandSpeed.clone();
		bNewSpeed.multiply((b.getMass() - a.getMass())
				/ (b.getMass() + a.getMass()));
		atempSpeed.multiply(2 * a.getMass() / (b.getMass() + a.getMass()));
		bNewSpeed.add(atempSpeed);
		bNewSpeed.add(bOtherComposantSpeed);
		b.setSpeed(bNewSpeed);
	}
}
