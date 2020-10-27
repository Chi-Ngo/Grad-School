package javafxprac8;

public class Cat extends Pet {
	static int catCount;
	
	@Override
	String talk() {
		catCount += 1;
		petCount += 1;
		return ("Meow...");
	}
}
