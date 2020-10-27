package javafxprac9;

public class test {
	public static void main (String[] args) {
		int n = 12;
		System.out.println(getNthNumber(n));
	}
	static int getNthNumber(int n) {
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
