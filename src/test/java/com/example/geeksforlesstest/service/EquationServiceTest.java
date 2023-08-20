package com.example.geeksforlesstest.service;

import com.example.geeksforlesstest.entity.Equation;
import com.example.geeksforlesstest.exception.EquationException;
import com.example.geeksforlesstest.repository.EquationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EquationService.class)
class EquationServiceTest {

    @Autowired
    EquationService equationService;

    @MockBean
    EquationRepository equationRepository;

    @ParameterizedTest
    @ValueSource(strings = {"2*x + (5 + 6) = 10",
            "2*x+5=17",
            "-1.3*5/x=1.2",
            "2*x=10",
            "2+x+-5=10",
            "2*x+5+х+5=10",
            "17=2*x+5"})
    void valid_equations_save_to_database(String equation) {

        var eq = new Equation(equation);
        eq.setId(new Random(25).nextLong());

        Mockito.when(equationRepository.save(Mockito.any(Equation.class)))
                .thenReturn(eq);

        var response = equationService.save(equation);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(eq.getEquation(), response.getEquation());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2 *) x = 10",
            "2 * ((x) = 10",
            "17 * x (= 2 + x",
            "(17 + 10 * x = 5)",
            "2*x=10=5*x",
            "-1.3*5/x",
            "=2*x+5",
            "2+2=4"})
    void invalid_equations_throw_exception(String equation) {
        var exception = Assertions.assertThrows(EquationException.class, () -> {
            equationService.save(equation);
        });

        Assertions.assertEquals("Invalid equation. Please rewrite in correct format", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("addValidRoots")
    void valid_root_for_equation_return_true(String equation, Double root) {
        var eq = new Equation(equation);
        eq.setId(new Random(25L).nextLong());

        Mockito.when(equationRepository.findByEquation(equation))
                .thenReturn(Optional.of(eq));

        Assertions.assertNotNull(equationService.addRoot(equation, String.valueOf(root)));
    }

    @ParameterizedTest
    @MethodSource("addInvalidRoots")
    void invalid_root_for_equation_return_false(String equation, Double root) {
        var eq = new Equation(equation);
        eq.setId(new Random(25L).nextLong());

        Mockito.when(equationRepository.findByEquation(equation))
                .thenReturn(Optional.of(eq));

        var exception = Assertions.assertThrows(EquationException.class, () -> {
            equationService.addRoot(equation, String.valueOf(root));
        });

        Assertions.assertEquals(String.format("Invalid root %s for equation %s", root, equation),
                exception.getMessage());
    }

    @Test
    void add_invalid_root_format_throw_expedition() {
        String equation = "2*x + (5 + 6) = 10";
        var root = "+32//3";

        var eq = new Equation(equation);
        eq.setId(new Random(25L).nextLong());

        Mockito.when(equationRepository.findByEquation(equation))
                .thenReturn(Optional.of(eq));

        var exception = Assertions.assertThrows(EquationException.class, () -> {
            equationService.addRoot(equation, root);
        });

        Assertions.assertEquals((String.format("invalid input for root: %s", root)), exception.getMessage());
    }


    private static Stream<Arguments> addValidRoots() {
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

    private static Stream<Arguments> addInvalidRoots() {
        return Stream.of(
                Arguments.of("2*x + (5 + 6) = 10", -1d),
                Arguments.of("2*x+5=113", 6d),
                Arguments.of("2 * (x + 3) = 7", 2d)
        );
    }
}