package org.example.View;
import org.example.BusinessLogic.Operation;
import org.example.DataModels.Polynomial;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Interface extends JFrame {

    private JLabel label1, label2, label3;
    private JTextField textField1, textField2;
    private JTextArea textArea;
    private JButton buttonSum, buttonSubstract, buttonMultiplication, buttonDivision, buttonDerivative, buttonIntegrate;

    public Interface() {
        super("Polynomial Calculator");

        // Initialize components
        label1 = new JLabel("Polynomial 1:");
        label2 = new JLabel("Polynomial 2:");
        label3 = new JLabel("Result:");
        textField1 = new JTextField(40);
        textField2 = new JTextField(40);
        textArea = new JTextArea(1, 20);
        textArea.setSize(100,1);
        buttonSum = new JButton("Sum");
        buttonSubstract = new JButton("Subtraction");
        buttonMultiplication = new JButton("Multiplication");
        buttonDivision = new JButton("Division");
        buttonDerivative = new JButton("Derivative");
        buttonIntegrate = new JButton("Integration");


        Font font = new Font("Verdana", Font.PLAIN, 14);
        label1.setFont(font);
        label2.setFont(font);
        label3.setFont(font);
        textField1.setFont(font);
        textField2.setFont(font);
        textArea.setFont(font);
        buttonSum.setFont(font);
        buttonSubstract.setFont(font);
        buttonMultiplication.setFont(font);
        buttonDivision.setFont(font);
        buttonDerivative.setFont(font);
        buttonIntegrate.setFont(font);
        Color backgroundColor = new Color(248, 183, 136);
        getContentPane().setBackground(backgroundColor);
        Color foregroundColor = new Color(50, 50, 50);
        label1.setForeground(foregroundColor);
        label2.setForeground(foregroundColor);
        label3.setForeground(foregroundColor);
        textField1.setForeground(foregroundColor);
        textField2.setForeground(foregroundColor);
        textArea.setForeground(foregroundColor);
        buttonSum.setForeground(foregroundColor);
        buttonSubstract.setForeground(foregroundColor);
        buttonMultiplication.setForeground(foregroundColor);
        buttonDivision.setForeground(foregroundColor);
        buttonDerivative.setForeground(foregroundColor);
        buttonIntegrate.setForeground(foregroundColor);
        Color buttonColor = new Color(162, 132, 132);
        buttonSum.setBackground(buttonColor);
        buttonSubstract.setBackground(buttonColor);
        buttonMultiplication.setBackground(buttonColor);
        buttonDivision.setBackground(buttonColor);
        buttonDerivative.setBackground(buttonColor);
        buttonIntegrate.setBackground(buttonColor);


        JPanel panel1 = new JPanel(new GridLayout(2, 2, 10, 10));
        panel1.setBackground(backgroundColor);
        panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel1.add(label1);
        panel1.add(textField1);
        panel1.add(label2);
        panel1.add(textField2);


        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(backgroundColor);
        panel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel2.add(label3, BorderLayout.NORTH);
        panel2.add(textArea, BorderLayout.CENTER);


        JPanel panel3 = new JPanel(new GridLayout(2, 3, 10, 10));
        panel3.setBackground(backgroundColor);
        panel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel3.add(buttonSum);
        panel3.add(buttonSubstract);
        panel3.add(buttonMultiplication);
        panel3.add(buttonDivision);
        panel3.add(buttonDerivative);
        panel3.add(buttonIntegrate);

        setLayout(new BorderLayout());
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(panel3, BorderLayout.SOUTH);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setVisible(true);


        buttonSum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = textField1.getText();
                Polynomial p1 = Polynomial.parse(s1);
                String s2 = textField2.getText();
                Polynomial p2 = Polynomial.parse(s2);

                Polynomial d = Operation.add(p1, p2);
                textArea.setText(d.toString());
            }
        });

        buttonSubstract.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = textField1.getText();
                Polynomial p1 = Polynomial.parse(s1);
                String s2 = textField2.getText();
                Polynomial p2 =Polynomial.parse(s2);

                Polynomial d = Operation.subtract(p1, p2);
                textArea.setText(d.toString());
            }
        });

        buttonMultiplication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = textField1.getText();
                Polynomial p1 = Polynomial.parse(s1);
                String s2 = textField2.getText();
                Polynomial p2 = Polynomial.parse(s2);

                Polynomial d = Operation.multiply(p1, p2);
                textArea.setText(d.toString());
            }
        });

        buttonDivision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = textField1.getText();
                Polynomial p1 = Polynomial.parse(s1);
                String s2 = textField2.getText();
                Polynomial p2 = Polynomial.parse(s2);

                ArrayList<Polynomial> d = Operation.divide(p1, p2);
                Polynomial m = d.get(0);
                Polynomial n = d.get(1);
                textArea.setText("Quotient: " + m.toString() + "\nReminder: " + n.toString());
            }
        });

        buttonDerivative.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = textField1.getText();
                Polynomial p1 = Polynomial.parse(s1);

                Polynomial d = Operation.derivative(p1);
                textArea.setText(d.toString());
            }
        });

        buttonIntegrate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = textField1.getText();
                Polynomial p1 = Polynomial.parse(s1);

                Polynomial d = Operation.integrate(p1);
                textArea.setText(d.toString() + " + C");
            }
        });

    }


}