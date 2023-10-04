package org.example;
import org.example.View.Interface;

public class App {
    public static void main(String[] args) {
        /*
        Polynomial p1 = Polynomial.parse("2x^2"); //2x^2 + 3x^1 + 4x^0
        Polynomial p2 = Polynomial.parse("1x^1"); //-x^3 + 2x^1 + 1x^0

        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);

        Polynomial sum = Operation.add(p1, p2);
        System.out.println("p1 + p2 = " + sum);

        Polynomial difference = Operation.subtract(p1, p2);
        System.out.println("p1 - p2 = " + difference);

        Polynomial product = Operation.multiply(p1, p2);
        System.out.println("p1 * p2 = " + product);

        Polynomial derivative = Operation.derivative(p1);
        System.out.println("p1' = " + derivative);

        Polynomial integral = Operation.integrate(p1);
        System.out.println("p1''(integrate) = " + integral);

        //Polynomial quotient = Operation.divide(p1, p2);
        //System.out.println("p1 / p2 = " + dividend + " + " + remainder

        */

        Interface view = new Interface();
        view.setVisible(true);
    }
}
