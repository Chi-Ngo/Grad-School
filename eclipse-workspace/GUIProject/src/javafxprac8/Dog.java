package javafxprac8;

public class Dog extends Pet {
	static int dogCount;
	
	@Override
	String talk() {
		dogCount += 1;
		petCount += 1;
		return ("Bark...");
	}
}
