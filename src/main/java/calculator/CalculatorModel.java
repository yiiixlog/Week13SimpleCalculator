package calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalculatorModel {
    private static final int MAX_INPUT_DIGITS = 12;
    private static final MathContext CALCULATION_CONTEXT = new MathContext(12, RoundingMode.HALF_UP);

    private String currentInput = "0.0";
    private BigDecimal leftOperand;
    private String operator;
    private boolean waitingForNumber;
    private boolean error;

    public String getDisplay() {
        return currentInput;
    }

    public void pressDigit(char digit) {
        if (!Character.isDigit(digit)) {
            return;
        }

        if (error || waitingForNumber || isZeroDisplay()) {
            currentInput = String.valueOf(digit);
            waitingForNumber = false;
            error = false;
            return;
        }

        if (currentInput.length() < MAX_INPUT_DIGITS) {
            currentInput += digit;
        }
    }

    public void chooseOperator(String nextOperator) {
        if (!isOperator(nextOperator)) {
            return;
        }

        if (error) {
            clear();
            return;
        }

        if (leftOperand != null && operator != null && !waitingForNumber) {
            calculate();
        }

        leftOperand = currentValue();
        operator = nextOperator;
        waitingForNumber = true;
    }

    public void calculate() {
        if (error || leftOperand == null || operator == null) {
            return;
        }

        BigDecimal rightOperand = waitingForNumber ? leftOperand : currentValue();

        try {
            BigDecimal result = switch (operator) {
                case "+" -> leftOperand.add(rightOperand, CALCULATION_CONTEXT);
                case "-" -> leftOperand.subtract(rightOperand, CALCULATION_CONTEXT);
                case "*" -> leftOperand.multiply(rightOperand, CALCULATION_CONTEXT);
                case "/" -> divide(leftOperand, rightOperand);
                default -> rightOperand;
            };

            currentInput = format(result);
            leftOperand = null;
            operator = null;
            waitingForNumber = true;
        } catch (ArithmeticException exception) {
            currentInput = "Error";
            leftOperand = null;
            operator = null;
            waitingForNumber = true;
            error = true;
        }
    }

    public void clear() {
        currentInput = "0.0";
        leftOperand = null;
        operator = null;
        waitingForNumber = false;
        error = false;
    }

    private BigDecimal currentValue() {
        return new BigDecimal(currentInput);
    }

    private BigDecimal divide(BigDecimal left, BigDecimal right) {
        if (BigDecimal.ZERO.compareTo(right) == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return left.divide(right, CALCULATION_CONTEXT);
    }

    private boolean isZeroDisplay() {
        return "0".equals(currentInput) || "0.0".equals(currentInput);
    }

    private static boolean isOperator(String value) {
        return "+".equals(value) || "-".equals(value) || "*".equals(value) || "/".equals(value);
    }

    private static String format(BigDecimal value) {
        BigDecimal rounded = value.round(CALCULATION_CONTEXT).stripTrailingZeros();

        if (BigDecimal.ZERO.compareTo(rounded) == 0) {
            return "0.0";
        }

        if (rounded.scale() <= 0) {
            return rounded.toPlainString() + ".0";
        }

        return rounded.toPlainString();
    }
}
