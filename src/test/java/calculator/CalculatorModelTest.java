package calculator;

public class CalculatorModelTest {
    public static void main(String[] args) {
        startsAtZeroPointZero();
        addsTwoNumbers();
        subtractsTwoNumbers();
        multipliesTwoNumbers();
        dividesTwoNumbers();
        evaluatesChainedOperations();
        clearsCalculator();
        reportsDivisionByZero();

        System.out.println("All calculator model tests passed.");
    }

    private static void startsAtZeroPointZero() {
        CalculatorModel calculator = new CalculatorModel();

        assertEquals("0.0", calculator.getDisplay(), "initial display");
    }

    private static void addsTwoNumbers() {
        CalculatorModel calculator = new CalculatorModel();

        press(calculator, "12");
        calculator.chooseOperator("+");
        press(calculator, "7");
        calculator.calculate();

        assertEquals("19.0", calculator.getDisplay(), "12 + 7");
    }

    private static void subtractsTwoNumbers() {
        CalculatorModel calculator = new CalculatorModel();

        press(calculator, "20");
        calculator.chooseOperator("-");
        press(calculator, "8");
        calculator.calculate();

        assertEquals("12.0", calculator.getDisplay(), "20 - 8");
    }

    private static void multipliesTwoNumbers() {
        CalculatorModel calculator = new CalculatorModel();

        press(calculator, "6");
        calculator.chooseOperator("*");
        press(calculator, "7");
        calculator.calculate();

        assertEquals("42.0", calculator.getDisplay(), "6 * 7");
    }

    private static void dividesTwoNumbers() {
        CalculatorModel calculator = new CalculatorModel();

        press(calculator, "9");
        calculator.chooseOperator("/");
        press(calculator, "2");
        calculator.calculate();

        assertEquals("4.5", calculator.getDisplay(), "9 / 2");
    }

    private static void evaluatesChainedOperations() {
        CalculatorModel calculator = new CalculatorModel();

        press(calculator, "5");
        calculator.chooseOperator("+");
        press(calculator, "3");
        calculator.chooseOperator("*");
        press(calculator, "2");
        calculator.calculate();

        assertEquals("16.0", calculator.getDisplay(), "(5 + 3) * 2");
    }

    private static void clearsCalculator() {
        CalculatorModel calculator = new CalculatorModel();

        press(calculator, "99");
        calculator.clear();

        assertEquals("0.0", calculator.getDisplay(), "clear");
    }

    private static void reportsDivisionByZero() {
        CalculatorModel calculator = new CalculatorModel();

        press(calculator, "8");
        calculator.chooseOperator("/");
        press(calculator, "0");
        calculator.calculate();

        assertEquals("Error", calculator.getDisplay(), "division by zero");
    }

    private static void press(CalculatorModel calculator, String input) {
        for (int index = 0; index < input.length(); index++) {
            calculator.pressDigit(input.charAt(index));
        }
    }

    private static void assertEquals(String expected, String actual, String label) {
        if (!expected.equals(actual)) {
            throw new AssertionError(label + " expected <" + expected + "> but was <" + actual + ">");
        }
    }
}
