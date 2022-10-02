import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
public class Polynomial {
    double[] coefficients;
    int[] exponents;

    public Polynomial() {
        coefficients = new double[] {0};
	exponents = new int[] {0};
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients;
	this.exponents = exponents;
    }

    public Polynomial(File file) throws IOException, FileNotFoundException {
	BufferedReader br = new BufferedReader(new FileReader(file));
	String line = br.readLine();
	if (line.isBlank()) {
	    this.coefficients = null;
	    this.exponents = null;
	    br.close();
	    return;
	}

	String[] tokens = line.replace("-", "+-").split("\\+");
	int polySize = tokens.length;
	double[] coefficients = new double[polySize];
	int[] exponents = new int[polySize];

	int index = 0;
	for (String token : tokens) {
		if (token.isEmpty()) continue;
		double c;
		int e;
		if (token.contains("x")) {
		    String[] parts = token.split("x");
		    c = Double.parseDouble(parts[0]);
		    e = (parts.length > 1) ? Integer.parseInt(parts[1]) : 1;
		} else {
		    c = Double.parseDouble(token);
		    e = 0;
		}
		coefficients[index] = c;
		exponents[index] = e;
		index++;
	}
	br.close();
	this.exponents = exponents;
	this.coefficients = coefficients;
    }

    public Polynomial add(Polynomial toAdd) {
	if (this.coefficients == null || this.exponents == null || toAdd.coefficients == null || toAdd.exponents == null)
		return null;

	ArrayList<Double> coefficientsOne = new ArrayList<>();
	ArrayList<Double> coefficientsTwo = new ArrayList<>();
	ArrayList<Integer> exponentsOne = new ArrayList<>();
	ArrayList<Integer> exponentsTwo = new ArrayList<>();
        for (double d : coefficients) coefficientsOne.add(d);
        for (int d : exponents) exponentsOne.add(d);
        for (double d : toAdd.coefficients) coefficientsTwo.add(d);
        for (int d : toAdd.exponents) exponentsTwo.add(d);

	HashMap<Integer, Double> coefficients = new HashMap<>();
	for (int exponent = 0; exponent <= Math.max(Collections.max(exponentsOne), Collections.max(exponentsTwo)); exponent++) {
	    if (exponentsOne.contains(exponent) && exponentsTwo.contains(exponent)) {
		coefficients.put(exponent, coefficientsOne.get(exponentsOne.indexOf(exponent)) + coefficientsTwo.get(exponentsTwo.indexOf(exponent)));
	    } else if (exponentsOne.contains(exponent)) {
		coefficients.put(exponent, coefficientsOne.get(exponentsOne.indexOf(exponent)));
	    } else if (exponentsTwo.contains(exponent)) {
		coefficients.put(exponent, coefficientsTwo.get(exponentsTwo.indexOf(exponent)));
	    }
	}

	ArrayList<Double> nC = new ArrayList<>();
	ArrayList<Integer> nE = new ArrayList<>();

	for (int key : coefficients.keySet()) {
	    if (coefficients.get(key) != 0) {
		nC.add(coefficients.get(key));
		nE.add(key);
	    }
	}
	int newSize = nC.size();
	double[] newCoefficients = new double[newSize];
        int [] newExponents = new int [newSize];
	for (int i = 0; i < newSize; i++) {
	    newCoefficients[i] = nC.get(i);
	    newExponents[i] = nE.get(i);
	}
	return new Polynomial(newCoefficients, newExponents);
    }

    public Polynomial multiply(Polynomial toMultiply) {
	if (this.coefficients == null || this.exponents == null || toMultiply.coefficients == null || toMultiply.exponents == null)
		return null;

	ArrayList<Double> coefficientsOne = new ArrayList<>();
	ArrayList<Double> coefficientsTwo = new ArrayList<>();
	ArrayList<Integer> exponentsOne = new ArrayList<>();
	ArrayList<Integer> exponentsTwo = new ArrayList<>();
        for (double d : coefficients) coefficientsOne.add(d);
        for (int d : exponents) exponentsOne.add(d);
        for (double d : toMultiply.coefficients) coefficientsTwo.add(d);
        for (int d : toMultiply.exponents) exponentsTwo.add(d);

	HashMap<Integer, Double> polyOneCo = new HashMap<>();
	HashMap<Integer, Double> polyTwoCo = new HashMap<>();
	HashMap<Integer, Double> multipliedPoly = new HashMap<>();

	for (int i = 0; i < coefficientsOne.size(); i++) {
	    polyOneCo.put(exponentsOne.get(i), coefficientsOne.get(i));
	}
	for (int i = 0; i < coefficientsTwo.size(); i++) {
	    polyTwoCo.put(exponentsTwo.get(i), coefficientsTwo.get(i));
	}
	for (Integer exponentPoly1 : polyOneCo.keySet()) {
	    for (Integer exponentPoly2 : polyTwoCo.keySet()) {
		double coefficientPoly1 = polyOneCo.get(exponentPoly1);
	        double coefficientPoly2 = polyTwoCo.get(exponentPoly2);

		double multipliedCoefficient = coefficientPoly1 * coefficientPoly2;
		int multipliedExponent = exponentPoly1 + exponentPoly2;

		if (multipliedPoly.keySet().contains(multipliedExponent)) {
		    double currentCoefficientForExponent = multipliedPoly.get(multipliedExponent);
		    double newCoefficientForExponent = currentCoefficientForExponent + multipliedCoefficient;
		    multipliedPoly.put(multipliedExponent, newCoefficientForExponent);
		} else {
		    multipliedPoly.put(multipliedExponent, multipliedCoefficient);
		};
	    }
	}

	int termCount = 0;
	for (Integer i : multipliedPoly.keySet()) {
	    if (multipliedPoly.get(i) != 0) termCount++;
	}

	double[] newCoefficients = new double[termCount];
	int[] newExponents = new int[termCount];

	int index = 0;
	for (int exponent : multipliedPoly.keySet()) {
	    double coefficient = multipliedPoly.get(exponent);
	    if (coefficient != 0) {
	    newCoefficients[index] = multipliedPoly.get(exponent);
	    newExponents[index] = exponent;
	    index++;
	    }
	}
	return new Polynomial(newCoefficients, newExponents);
    }

    public void saveToFile(String file) throws IOException {
	File polynomialFile = new File(file);
	polynomialFile.createNewFile();
	FileWriter writer = new FileWriter(file);

	if (this.coefficients == null || this.exponents == null) {
		writer.write(""); writer.close(); return;
	}

	StringBuilder polynomial = new StringBuilder();
	for (int i = 0; i < this.coefficients.length; i++) {
	    double coefficient = this.coefficients[i];
	    int exponent = this.exponents[i];
	    if (coefficient == 0) continue;
	    if (exponent == 0) {
		polynomial.append(coefficient);
	    } else {
		if (coefficient > 0) {
		    polynomial.append("+" + coefficient + "x" + exponent);
		} else {
		    polynomial.append(coefficient + "x" + exponent);
		}
	    }
	}
	writer.write(polynomial.toString());
	writer.close();
    }

    public double evaluate(double x) {
	if (this.coefficients == null || this.exponents == null) return 0;
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * (Math.pow(x, i));
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}
