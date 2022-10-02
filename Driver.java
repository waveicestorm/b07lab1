import java.io.File;
import java.io.IOException;
public class Driver {
    public static void main(String[] args) throws IOException {
	double[] c1 = new double[] {6,-2,5};
	int[] e1 = new int[] {0,1,3};
	double[] c2 = new double[] {-2,3,1,-7};
	int[] e2 = new int[] {0,1,2,3};

	Polynomial p1 = new Polynomial(c1, e1);
	Polynomial p2 = new Polynomial(c2, e2);

	Polynomial a12 = p1.add(p2);
	Polynomial m12 = p1.multiply(p2);

	for (int i = 0; i < a12.coefficients.length; i++) {
	    System.out.println("Term: " + a12.coefficients[i] + "x" + a12.exponents[i]);
	}
	System.out.println();
	for (int i = 0; i < m12.coefficients.length; i++) {
	    System.out.println("Term: " + m12.coefficients[i] + "x" + m12.exponents[i]);
	}

	Polynomial f1 = new Polynomial(new File("./input.txt"));
	f1.saveToFile("./output.txt");
    }
}
