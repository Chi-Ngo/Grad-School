package javafxprac8;

public class Bird extends Pet {
	static int birdCount;
	
	@Override
	String talk() {
		birdCount += 1;
		petCount += 1;
		return ("Tweet...");
	}
}
