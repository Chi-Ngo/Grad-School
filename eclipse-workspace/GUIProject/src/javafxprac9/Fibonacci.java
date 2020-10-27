package javafxprac9;

public class Fibonacci extends Series {

	@Override
	int getNthNumber(int n) {
		int result = 0;
		int prev1 = 1;
		int prev2 = 0;
		int count = 3;
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		else {
			while (count <= n) {
				result = prev1 + prev2;
				prev2 = prev1;
				prev1 = result;
				count++;
			}
			return result;
		}
	}

}
