import org.example.DataModels.Polynomial;
import org.example.BusinessLogic.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CalculatorTest {

    @Test
    public void testAdd() {
        Polynomial a = new Polynomial();
        a.addTerm(2, 2);
        a.addTerm(1, 4);
        a.addTerm(0, 6);
        Polynomial b = new Polynomial();
        b.addTerm(2, 3);
        b.addTerm(1, 1);
        b.addTerm(0, 2);
        Polynomial expected = new Polynomial();
        expected.addTerm(2, 5);
        expected.addTerm(1, 5);
        expected.addTerm(0, 8);
        Polynomial result = Operation.add(a, b);
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testSubtract() {
        Polynomial a = new Polynomial();
        a.addTerm(2, 2);
        a.addTerm(1, 4);
        a.addTerm(0, 6);
        Polynomial b = new Polynomial();
        b.addTerm(2, 3);
        b.addTerm(1, 1);
        b.addTerm(0, 2);
        Polynomial expected = new Polynomial();
        expected.addTerm(2, -1);
        expected.addTerm(1, 3);
        expected.addTerm(0, 4);
        Polynomial result = Operation.subtract(a, b);
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testMultiply() {
        Polynomial a = new Polynomial();
        a.addTerm(2, 2);
        a.addTerm(1, 4);
        a.addTerm(0, 6);
        Polynomial b = new Polynomial();
        b.addTerm(2, 3);
        b.addTerm(1, 1);
        b.addTerm(0, 2);
        Polynomial expected = new Polynomial();
        expected.addTerm(4, 6);
        expected.addTerm(3, 14);
        expected.addTerm(2, 26);
        expected.addTerm(1, 14);
        expected.addTerm(0, 12);
        Polynomial result = Operation.multiply(a, b);
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDerivative() {
        Polynomial a = new Polynomial();
        a.addTerm(2, 2);
        a.addTerm(1, 4);
        a.addTerm(0, 6);
        Polynomial expected = new Polynomial();
        expected.addTerm(1, 4);
        expected.addTerm(0, 4);
        Polynomial result = Operation.derivative(a);
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testIntegrate() {
        Polynomial a = new Polynomial();
        a.addTerm(2, 2);
        a.addTerm(1, 4);
        a.addTerm(0, 6);
        Polynomial expected = new Polynomial();
        expected.addTerm(3,0.6666666666666666);
        expected.addTerm(2,2);
        expected.addTerm(1,6);

        Polynomial result = Operation.integrate(a);
        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testDivide() {
        Polynomial a = new Polynomial();
        a.addTerm(3, 3);
        a.addTerm(2, -5);
        a.addTerm(1, 2);
        a.addTerm(0, 4);
        Polynomial b = new Polynomial();
        b.addTerm(1,1);
        b.addTerm(0,-3);
        Polynomial expectedQuotient = new Polynomial();
        expectedQuotient.addTerm(2,3);
        expectedQuotient.addTerm(1,4);
        expectedQuotient.addTerm(0,14);
        Polynomial expectedRemainder = new Polynomial();
        expectedRemainder.addTerm(0,46);
        ArrayList<Polynomial> result = Operation.divide(a, b);
        Assertions.assertEquals(expectedQuotient.toString(), result.get(0).toString());
        Assertions.assertEquals(expectedRemainder.toString(), result.get(1).toString());
    }
}
