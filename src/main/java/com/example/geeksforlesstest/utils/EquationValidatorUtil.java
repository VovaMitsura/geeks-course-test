package com.example.geeksforlesstest.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

@Component
public class EquationValidatorUtil {

    private EquationValidatorUtil() {
    }

    public static boolean isEquationValid(String expression) {

        if (!containsOneEqualsSign(expression)) {
            return false;
        }

        var compactExpression = expression.replaceAll("\\s+", "");
        var expressions = compactExpression.split("=");
        var roots = 0;

        for (var exp : expressions) {
            if (!validateRoundBrackets(exp) || !isValidSigns(exp)) {
                return false;
            }
             roots += countRoots(exp);
        }

        return roots > 0;
    }

    public static Double fractionToDouble(String fraction)
            throws ParseException {
        Double d = null;
        if (fraction != null) {
            if (fraction.contains("/")) {
                String[] numbers = fraction.split("/");
                if (numbers.length == 2) {
                    BigDecimal d1 = BigDecimal.valueOf(Double.valueOf(numbers[0]));
                    BigDecimal d2 = BigDecimal.valueOf(Double.valueOf(numbers[1]));
                    BigDecimal response = d1.divide(d2, MathContext.DECIMAL128);
                    d = response.doubleValue();
                }
            } else {
                d = Double.valueOf(fraction);
            }
        }
        if (d == null) {
            throw new ParseException(fraction, 0);
        }
        return d;
    }

    private static boolean validateRoundBrackets(String expression) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : expression.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }

        return stack.isEmpty();
    }

    private static boolean containsOneEqualsSign(String expression) {
        var equalsCount = 0;

        for (char c : expression.toCharArray()) {
            if (c == '=') {
                equalsCount++;
            }
        }

        return equalsCount == 1;
    }

    private static int countRoots(String expression) {

        var countRoots = 0;

        for (var c : expression.toCharArray()) {
            if (c == 'x') {
                countRoots++;
            }
        }

        return countRoots;
    }

    private static boolean isValidSigns(String expression) {
        var signs = new HashSet<Character>();
        signs.add('*');
        signs.add('+');
        signs.add('-');
        signs.add('/');

        var expressionCharacters = expression.toCharArray();
        for (int i = 0; i < expressionCharacters.length - 1; i++) {
            if (signs.contains(expressionCharacters[i])) {
                var nextExpressionChar = expressionCharacters[i + 1];
                if (nextExpressionChar == '*' || nextExpressionChar == '+' || nextExpressionChar == '/')
                    return false;
            }
        }

        return !expression.startsWith("=") && !expression.endsWith("=") && !expression.isEmpty();
    }
}
