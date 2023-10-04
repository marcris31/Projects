package org.example.DataModels;
import java.util.HashMap;
import java.util.Map;


public class Polynomial {
    private final Map<Integer, Double> map;

    public Polynomial() {
        this.map = new HashMap<>();
    }

    public void addTerm(int degree, double coefficient) {
        if (coefficient != 0) {
            map.put(degree, map.getOrDefault(degree, 0.0) + coefficient);
        } else {
            map.remove(degree);
        }
    }

    public Map<Integer, Double> getMap() {
        return map;
    }

    public int getDegree() {
        int degree = -1;
        for (int key : map.keySet()) {
            if (key > degree) {
                degree = key;
            }
        }
        return degree;
    }

    public double getCoefficient(int degree) {
        return map.getOrDefault(degree, 0.0);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            int degree = entry.getKey();
            double coefficient = entry.getValue();
            if (coefficient != 0) {
                if (sb.length() > 0) {
                    sb.append(coefficient > 0 ? " + " : " - ");
                } else if (coefficient < 0) {
                    sb.append("-");
                }
                coefficient = Math.abs(coefficient);
                sb.append(coefficient);
                if (degree > 0) {
                    sb.append("x");
                    if (degree > 1) {
                        sb.append("^" + degree);
                    }
                }
            }
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        return sb.toString();
    }

    public boolean isZero() {
        for (double coefficient : map.values()) {
            if (coefficient != 0) {
                return false;
            }
        }
        return true;
    }

    public static Polynomial parse(String poli){
        Polynomial polinom = new Polynomial();
        String[] terms = poli.split("(?=[+-])");
        for (String term : terms) {
            String[] parts = term.split("\\*x\\^");
            double coefficient = Double.parseDouble(parts[0].trim());
            int degree = Integer.parseInt(parts[1].trim());
            polinom.addTerm(degree, coefficient);
        }
        return polinom;
    }
}
