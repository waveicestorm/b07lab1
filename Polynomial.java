import java.lang.Math;
public class Polynomial {
    double[] coefficients;

    public Polynomial() {
        coefficients = new double[] {0};
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public Polynomial add(Polynomial toAdd) {
        int len = Math.max(coefficients.length, toAdd.coefficients.length);
        double[] smaller;
        double[] bigger;
        if (coefficients.length > toAdd.coefficients.length) {
            bigger = coefficients.clone();
            smaller = toAdd.coefficients;
        } else {
            bigger = toAdd.coefficients.clone();
            smaller = coefficients;
        }
        for (int i = 0; i < len; i++) {
            if (i < smaller.length)
            bigger[i] += smaller[i];
        }
        return new Polynomial(bigger);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * (Math.pow(x, i));
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0 ? true : false;
    }
}
