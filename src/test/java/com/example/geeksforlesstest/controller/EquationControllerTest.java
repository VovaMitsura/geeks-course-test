package com.example.geeksforlesstest.controller;

import com.example.geeksforlesstest.GeeksforlessTestApplication;
import com.example.geeksforlesstest.dto.EquationDTO;
import com.example.geeksforlesstest.dto.EquationRootDTO;
import com.example.geeksforlesstest.entity.Equation;
import com.example.geeksforlesstest.repository.EquationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GeeksforlessTestApplication.class})
class EquationControllerTest {

    private static final String URL = "/api/v1/equation";

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EquationRepository equationRepository;
    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach()
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2*x + (5 + 6) = 10",
            "2*x+5=17",
            "-1.3*5/x=1.2",
            "2*x=10",
            "2+x+-5=10",
            "2*x+5+x+5=10",
            "17=2*x+5"})
    void send_valid_equation_save_to_db(String equation) throws Exception {
        var request = new EquationDTO();
        request.setEquation(equation);

        var mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), Equation.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(request.getEquation(), response.getEquation());

        equationRepository.delete(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2 *) x = 10",
            "3+*4=x",
            "2 * ((x) = 10",
            "17 * x (= 2 + x",
            "(17 + 10 * x = 5)",
            "2*x=10=5*x",
            "-1.3*5/x",
            "=2*x+5",
            "2+2=4"})
    void invalid_equation_throw_exception(String equation) throws Exception {
        var request = new EquationDTO();
        request.setEquation(equation);

        var mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn();

        var exception = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
                ApplicationExceptionHandler.ErrorResponse.class);

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Invalid equation. Please rewrite in correct format", exception.getErrorMessage());
    }

    @ParameterizedTest
    @MethodSource("addValidRoots")
    void valid_root_for_equation_updates_entity(String equation, Double root) throws Exception {
        var request = new EquationRootDTO();
        request.setRoot(String.valueOf(root));
        request.setEquation(equation);

        var initialEntity = equationRepository.saveAndFlush(new Equation(equation));

        var mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andReturn();

        var response = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
                Equation.class);
        var entityInDb = equationRepository.findByEquation(equation).orElseThrow();

        Assertions.assertEquals(equation, response.getEquation());
        Assertions.assertEquals(initialEntity.getId(), entityInDb.getId());
        Assertions.assertEquals(Optional.of(root), response.getRoot());
        Assertions.assertEquals(entityInDb.getId(), response.getId());
        Assertions.assertEquals(entityInDb.getEquation(), response.getEquation());
        Assertions.assertEquals(entityInDb.getRoot(), response.getRoot());

        equationRepository.delete(response);
    }

    @ParameterizedTest
    @MethodSource("addValidRoots")
    void update_not_existing_equation_throw_exception(String equation, Double root) throws Exception {
        var request = new EquationRootDTO();
        request.setRoot(String.valueOf(root));
        request.setEquation(equation);

        var mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        var exception = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
                ApplicationExceptionHandler.ErrorResponse.class);

        Assertions.assertEquals(String.format("Equation: %s not found", equation), exception.getErrorMessage());
    }

    @Test
    void get_unique_equation_by_root_return_2_unique_values() throws Exception {
        equationRepository.saveAllAndFlush(equations());

        var mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/unique")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = List.of(objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
                Equation[].class));
        var uniqueValues = equationRepository.findAllUniqueRoot();

        Assertions.assertEquals(uniqueValues.size(), response.size());
        Assertions.assertNotEquals(equations(), response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(uniqueValues.get(0), response.get(0));
        Assertions.assertEquals(uniqueValues.get(uniqueValues.size() - 1), response.get(response.size() - 1));

        equationRepository.deleteAll();
    }

    @Test
    void get_all_equation_with_same_root_return_two_values() throws Exception {
        equationRepository.saveAllAndFlush(equations());

        var mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URL + "?root1=-0.5d")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = List.of(objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
                Equation[].class));
        var equationWithSameRoot = equationRepository.findAllByRoot(-0.5d);

        Assertions.assertEquals(equationWithSameRoot.size(), response.size());
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(Optional.of(-0.5d), response.get(0).getRoot());

        equationRepository.deleteAll();
    }

    @Test
    void get_all_equation_with_root_limited_return_3_values() throws Exception {
        equationRepository.saveAllAndFlush(equations());

        var mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(URL + "?root1=-6d&root2=-0.1d")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        var response = List.of(objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(),
                Equation[].class));
        var equationWithSameRoot = equationRepository.findAllByRootBetween(-6d, -0.1d);

        Assertions.assertEquals(equationWithSameRoot.size(), response.size());
        Assertions.assertEquals(3, response.size());

        equationRepository.deleteAll();
    }

    private static Stream<Arguments> addValidRoots() {
        return Stream.of(
                Arguments.of("2*x + (5 + 6) = 10", -0.5d),
                Arguments.of("-1.3*5/x=1.2", -65 / 12d),
                Arguments.of("2*x+5+x+5=10", 0d));
    }

    private static List<Equation> equations() {
        return List.of(
                new Equation("2*x + (5 + 6) = 10", -0.5d),
                new Equation("-1.3*5/x=1.2", -65 / 12d),
                new Equation("2*x+5+x+5=10", 0d),
                new Equation("2*x=-1", -0.5d)
        );
    }
}