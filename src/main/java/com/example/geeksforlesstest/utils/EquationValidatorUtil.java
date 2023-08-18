package com.example.geeksforlesstest.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

@Component
public class EquationValidatorUtil {
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
