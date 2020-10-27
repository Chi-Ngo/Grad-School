package javafxprac9;

public class Factorial extends Series {

	@Override
	int getNthNumber(int n) {
		int result = 1;
		if (n == 0) {
			return result;
		}
		else {
			while (n > 1) {
				result *= n;
				n--;
			}
			return result;
		}
	}
}
