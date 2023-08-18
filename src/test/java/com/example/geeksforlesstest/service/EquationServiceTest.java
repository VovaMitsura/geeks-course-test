package com.example.geeksforlesstest.service;

import com.example.geeksforlesstest.entity.Equation;
import com.example.geeksforlesstest.exception.EquationException;
import com.example.geeksforlesstest.repository.EquationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Random;

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
}