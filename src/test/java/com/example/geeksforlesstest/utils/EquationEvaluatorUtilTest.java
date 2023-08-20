package com.example.geeksforlesstest.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class EquationEvaluatorUtilTest {

    @ParameterizedTest
    @MethodSource("validEquationAndRoots")
    void valid_equation_and_roots_return_equals_results(String equation, Double root){
        var equations = equation.split("=");

        var leftPart = EquationEvaluatorUtil.evaluate(equations[0], root);
        var rightPart = EquationEvaluatorUtil.evaluate(equations[1], root);

        Assertions.assertEquals(leftPart, rightPart);
    }

    @ParameterizedTest
    @MethodSource("invalidEquationAndRoots")
    void invalid_equation_and_roots_return_not_equals_results(String equation, Double root){
        var equations = equation.split("=");

        var leftPart = EquationEvaluatorUtil.evaluate(equations[0], root);
        var rightPart = EquationEvaluatorUtil.evaluate(equations[1], root);

        Assertions.assertNotEquals(leftPart, rightPart);
    }


    private static Stream<Arguments> validEquationAndRoots() {
        return Stream.of(
                Arguments.of("2*x + (5 + 6) = 10", -0.5d),
                Arguments.of("2*x +( 7 * (5 + 5)) = 10", -30d),
                Arguments.of("2*x * (5 + ( 7 * (5 + 5))) = 10", 1 / 15d),
                Arguments.of("2*x+5=17", 6d),
                Arguments.of("2 * (x + 3) = 10", 2d),
                Arguments.of("-1.3*5/x=1.2", -65 / 12d),
                Arguments.of("2*x+5+x+5=10", 0d)
        );
    }

    private static Stream<Arguments> invalidEquationAndRoots() {
        return Stream.of(
                Arguments.of("2*x + (5 + 6) = 10", -1d),
                Arguments.of("2*x+5=113", 6d),
                Arguments.of("2 * (x + 3) = 7", 2d)
        );
    }

}