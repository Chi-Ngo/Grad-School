package hw2;

public class Test {
	static boolean StringContain(String longer, String shorter) {
		String test = longer;
	    for (char c : shorter.toCharArray()) {
	       if (!test.contains(Character.toString(c))) {
	    	   return false;
	       }
	       else {
	    	   test = test.replaceFirst(Character.toString(c), "-");
	       }
	    }
	    return true;
	}
	
	public static void main (String[] args) {
		System.out.println(StringContain("allege","allege"));
	}
}
