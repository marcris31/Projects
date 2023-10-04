package org.example.BusinessLogic;
import org.example.DataModels.Polynomial;
import java.util.ArrayList;
import java.util.Map;

public class Operation {
    public static Polynomial add(Polynomial a, Polynomial b) {
        Polynomial result = new Polynomial();
        for (Map.Entry<Integer, Double> entry : a.getMap().entrySet()) {
            result.addTerm(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Integer, Double> entry : b.getMap().entrySet()) {
            result.addTerm(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Polynomial subtract(Polynomial a, Polynomial b) {
        Polynomial result = new Polynomial();
        for (Map.Entry<Integer, Double> entry : a.getMap().entrySet()) {
            result.addTerm(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Integer, Double> entry : b.getMap().entrySet()) {
            result.addTerm(entry.getKey(), -entry.getValue());
        }
        return result;
    }

    public static Polynomial multiply(Polynomial a, Polynomial b) {
        Polynomial result = new Polynomial();
        for (Map.Entry<Integer, Double> aEntry : a.getMap().entrySet()) {
            for (Map.Entry<Integer, Double> bEntry : b.getMap().entrySet()) {
                int degree = aEntry.getKey() + bEntry.getKey();
                double coefficient = aEntry.getValue() * bEntry.getValue();
                result.addTerm(degree, coefficient);
            }
        }
        return result;
    }

    public static Polynomial derivative(Polynomial a) {
        Polynomial result = new Polynomial();
        for (Map.Entry<Integer, Double> entry : a.getMap().entrySet()) {
            int degree = entry.getKey();
            double coefficient = entry.getValue();
            if (degree > 0) {
                result.addTerm(degree - 1, degree * coefficient);
            }
        }
        return result;
    }

    public static Polynomial integrate(Polynomial a) {
        Polynomial result = new Polynomial();
        for (Map.Entry<Integer, Double> entry : a.getMap().entrySet()) {
            int degree = entry.getKey();
            double coefficient = entry.getValue();
            result.addTerm(degree + 1, coefficient / (degree + 1));
        }
        return result;
    }

    public static ArrayList<Polynomial> divide(Polynomial a, Polynomial b) {
        ArrayList<Polynomial> finalResult = new ArrayList<>();
        Polynomial result = new Polynomial();
        Polynomial remainder = new Polynomial();
        for (Map.Entry<Integer, Double> entry : a.getMap().entrySet()) {
            remainder.addTerm(entry.getKey(), entry.getValue());
        }
        while (remainder.getDegree() >= b.getDegree() && !remainder.isZero()) {
            if (remainder.getDegree() == 0 && b.getDegree() == 0) {
                double quotient = remainder.getCoefficient(0) / b.getCoefficient(0);
                result.addTerm(0, quotient);
                break;
            }
            Polynomial temp = new Polynomial();
            int degree = remainder.getDegree() - b.getDegree();
            double coefficient = remainder.getCoefficient(remainder.getDegree()) / b.getCoefficient(b.getDegree());
            temp.addTerm(degree, coefficient);
            result = add(result, temp);
            Polynomial temp2 = Operation.multiply(b, temp);
            remainder = subtract(remainder, temp2);
        }
        finalResult.add(result);
        finalResult.add(remainder);
        return finalResult;
    }
}

