public class ColisionHandler {
	public static boolean doesColide(ColisionMate a, ColisionMate b) {
		if (a.getClass() == Circle.class && b.getClass() == Circle.class) {
			Circle aCircle = (Circle) a.getShape();
			Circle bCircle = (Circle) b.getShape();
			double dx = a.getPosition().x - b.getPosition().x;
			double dy = a.getPosition().y - b.getPosition().y;
			return (Math.sqrt(dx * dx + dy * dy) <= aCircle.radius
					+ bCircle.radius);
		} else {
			return false; // For now.
		}
	}

	public static void Colide(ColisionMate a, ColisionMate b) {
		// Vektorer som är vinkelräta mot stöten och således ej påverkar den
		MyVector aOtherComposantSpeed;
		MyVector bOtherComposantSpeed;

		// Vektorn mellan de olika objekten
		MyVector cmFromAToB = new MyVector(a.getPosition().x
				- b.getPosition().x, a.getPosition().y - b.getPosition().y);

		// a:s vektor i kolisionsriktningen
		MyVector aColisionComposandSpeed = cmFromAToB.clone();
		aColisionComposandSpeed.devide(cmFromAToB.magnitude());
		aColisionComposandSpeed.multiply(MyVector.scalarProduct(a.getSpeed(),
				cmFromAToB) * (-1));

		// b:s vektor i kolisionsriktningen
		MyVector bColisionComposandSpeed = cmFromAToB.clone();
		bColisionComposandSpeed.devide(cmFromAToB.magnitude());
		bColisionComposandSpeed.multiply(MyVector.scalarProduct(b.getSpeed(),
				cmFromAToB));

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
