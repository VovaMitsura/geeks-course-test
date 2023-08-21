package com.example.geeksforlesstest.utils;

import java.util.*;

public class EquationEvaluatorUtil {

    private EquationEvaluatorUtil() {
    }

    public static Double evaluate(String expression, Double root) {
        var operators = new LinkedList<String>();
        var values = new LinkedList<Double>();
        var sum = new ArrayDeque<Double>();

        var expInBrackets = processExpression(expression, root);

        for (int i = 0; i < expInBrackets.length(); i++) {
            String s = expInBrackets.substring(i, i + 1);

            switch (s) {
                case "(" -> i = processRoundBrackets(expInBrackets, values, i, sum);
                case "+", "*", "/" -> i = processMathSigns(expInBrackets, operators, values, i, s);
                case "-" -> {
                    if (i >= 1 && Character.isDigit(expInBrackets.charAt(i - 1)))
                        operators.add(s);
                    else
                        i = processNegativeSign(expInBrackets, operators, values, i, s);
                }
                case "|" -> {
                    int count = operators.size();
                    sum.push(values.poll());
                    while (count > 0) {
                        String op = operators.poll();
                        double val = sum.pop();
                        switch (op) {
                            case "+" -> val += values.poll();
                            case "-" -> val -= values.poll();
                            case "*" -> val *= values.poll();
                            case "/" -> val /= values.poll();
                        }
                        sum.push(val);
                        count--;
                    }
                }
                default -> {
                    var number = processNumber(expInBrackets, s, i);
                    i = number.length() > 1 ? i + number.length() - 1 : i;
                    values.add(Double.parseDouble(number));
                }
            }
        }

        return sum.pop();
    }

    private static String processExpression(String canonicalEquation, Double root) {
        var exp = canonicalEquation.replaceAll("x", String.valueOf(root));
        var compactExp = exp.replaceAll("\\s+", "");
        return compactExp + "|";
    }

    private static int processRoundBrackets(String expInBrackets,
                                            Deque<Double> values,
                                            int i,
                                            Deque<Double> sum) {

        Queue<String> operators = new LinkedList<>();
        Queue<Double> bracketsValues = new LinkedList<>();
        var substringIndex = countExpressionInBracketsLength(i, expInBrackets);
        var bracketsExpression = expInBrackets.substring(i, substringIndex + 1);

        for (i = 0; i < bracketsExpression.length(); i++) {
            String s = bracketsExpression.substring(i, i + 1);

            switch (s) {
                case "(" -> {
                    if (i == 0) break;
                    i = processRoundBrackets(bracketsExpression, values, i, sum);
                    var el = values.getLast();
                    bracketsValues.add(el);
                    values.remove(el);
                }
                case "+", "*", "/" -> i = processMathSigns(bracketsExpression, operators, bracketsValues, i, s);
                case "-" -> {
                    if (i > 1 && Character.isDigit(bracketsExpression.charAt(i - 1))) {
                        operators.add(s);
                    } else {
                        i = processNegativeSign(bracketsExpression, operators, values, i, s);
                        var el = values.getLast();
                        bracketsValues.add(el);
                        values.remove(el);
                    }
                }
                case ")" -> {
                    int count = operators.size();
                    sum.push(bracketsValues.poll());
                    while (count > 0) {
                        String op = operators.poll();
                        double v = sum.pop();
                        if (op.equals("+"))
                            v += bracketsValues.poll();
                        else if (op.equals("-"))
                            v -= bracketsValues.poll();
                        else if (op.equals("*"))
                            v *= bracketsValues.poll();
                        else if (op.equals("/"))
                            v /= bracketsValues.poll();
                        sum.push(v);
                        count--;
                    }
                }
                default -> {
                    var number = processNumber(bracketsExpression, s, i);
                    i = number.length() > 1 ? i + number.length() - 1 : i;
                    bracketsValues.add(Double.parseDouble(number));
                }
            }
        }

        values.add(sum.pop());
        return substringIndex;
    }

    private static int countExpressionInBracketsLength(int i, String expInBrackets) {

        int substringIndex = i;

        var stack = new ArrayDeque<Integer>();

        for (; i < expInBrackets.length(); i++) {
            if (expInBrackets.charAt(i) == '(') {
                stack.push((int) expInBrackets.charAt(i));
            } else if (expInBrackets.charAt(i) == ')') {
                stack.pop();
                if (stack.isEmpty()) {
                    return i;
                }
            }
        }

        return substringIndex;
    }

    private static int processMathSigns(String expInBrackets, Queue<String> operators, Queue<Double> values, int i, String s) {
        if (expInBrackets.charAt(i + 1) == '-') {
            var startNegative = expInBrackets.substring(i + 1, i + 2);
            var number = processNumber(expInBrackets, startNegative, i + 1);

            i = number.length() > 1 ? i + number.length() : i;

            values.add(Double.parseDouble(number));
        }
        operators.add(s);
        return i;
    }

    private static int processNegativeSign(String expInBrackets, Queue<String> operators, Queue<Double> values, int i, String s) {
        if (Character.isDigit(expInBrackets.charAt(i + 1))) {
            var startNegative = expInBrackets.substring(i, i + 1);
            var number = processNumber(expInBrackets, startNegative, i);

            i = number.length() > 1 ? i + number.length() : i;

            values.add(Double.parseDouble(number));
            return i - 1;
        } else operators.add(s);
        return i;
    }

    private static String processNumber(String expression, String currentLetter, int iterator) {
        var builder = new StringBuilder();

        while (Character.isDigit(currentLetter.charAt(0)) || currentLetter.charAt(0) == '-') {
            builder.append(currentLetter.charAt(0));
            if (Character.isDigit(expression.charAt(iterator + 1))) {
                iterator++;
                currentLetter = expression.substring(iterator, iterator + 1);
            } else if (expression.charAt(iterator + 1) == '.') {
                builder.append(".");
                iterator += 2;
                currentLetter = expression.substring(iterator, iterator + 1);
            } else {
                break;
            }
        }

        return builder.toString();
    }
}
