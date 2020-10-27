package javafxprac9;

public class Prime extends Series {

	@Override
	int getNthNumber(int n) {
		int count = 1;
		int number = 2;
		if (n == 0) {
			return 1;
		}
		else {
			int result = 2;
			while (count <= n) {
				if (ifPrime(number) == true) {
					result = number;
					count++;
				}
				number++;
			}
			return result;
		}
	}
	public boolean ifPrime (int input) {
		int count = 0;
		for (int i = 1; i <= input; i++) {
			if (input%i == 0) {
				count++;
			}
			if (count > 2) {
				break;
			}
		}
		if (count == 2) {
			return true;
		}
		return false;
	}
}
