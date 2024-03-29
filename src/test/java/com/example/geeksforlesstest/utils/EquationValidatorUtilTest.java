package com.example.geeksforlesstest.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;

class EquationValidatorUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {"2*x + (5 + 6) = 10",
            "2*x+5=17",
            "-1.3*5/x=1.2",
            "2*x=10",
            "2+x+-5=10",
            "2*x+5+х+5=10",
            "17=2*x+5",})
    void valid_equation_return_true(String equations) {
        Assertions.assertTrue(EquationValidatorUtil.isEquationValid(equations));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2 *) x = 10",
            "2 * ((x) = 10",
            "17 * x (= 2 + x",
            "(17 + 10 * x = 5)"})
    void invalid_brackets_return_false(String equations) {
        Assertions.assertFalse(EquationValidatorUtil.isEquationValid(equations));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2*x=10=5*x",
            "-1.3*5/x",
            "=2*x+5"})
    void not_one_equals_sign_return_false(String equations) {
        Assertions.assertFalse(EquationValidatorUtil.isEquationValid(equations));
    }

    @ParameterizedTest
    @ValueSource(strings = {"3*+4+x=12",
            "5/*7=x",
            "12-+x=10"})
    void invalid_order_of_sign_return_false(String equations) {
        Assertions.assertFalse(EquationValidatorUtil.isEquationValid(equations));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2+2=4"})
    void equations_without_root_return_false(String equations) {
        Assertions.assertFalse(EquationValidatorUtil.isEquationValid(equations));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.5", "-65/12", "0", "1.4"})
    void valid_fraction_converts_to_double_value(String fract) throws ParseException {
        var doubleValue = EquationValidatorUtil.fractionToDouble(fract);
        Assertions.assertEquals(Double.class, doubleValue.getClass());
    }

    @ParameterizedTest
        @ValueSource(strings = {"+32//3"})
    void invalid_fraction_throws_error(String frac) {
        Assertions.assertThrows(ParseException.class, () -> EquationValidatorUtil.fractionToDouble(frac));
    }
}