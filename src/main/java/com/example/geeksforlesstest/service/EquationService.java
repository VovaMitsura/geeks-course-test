package com.example.geeksforlesstest.service;

import com.example.geeksforlesstest.entity.Equation;
import com.example.geeksforlesstest.exception.ENotFoundException;
import com.example.geeksforlesstest.exception.EquationException;
import com.example.geeksforlesstest.repository.EquationRepository;
import com.example.geeksforlesstest.utils.EquationEvaluatorUtil;
import com.example.geeksforlesstest.utils.EquationValidatorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EquationService {

    private final EquationRepository repository;

    public Equation save(String equation) {
        if (!EquationValidatorUtil.isEquationValid(equation)) {
            throw new EquationException("Invalid equation. Please rewrite in correct format");
        }

        return repository.save(new Equation(equation));
    }

    @Transactional
    public Equation addRoot(String equation, String root) {
        var eq = repository.findByEquation(equation).orElseThrow(() ->
                new ENotFoundException(String.format("Equation: %s not found", equation)));

        var equations = equation.split("=");
        var values = new ArrayList<Double>();
        var doubleRootValue = 0d;
        try {
            doubleRootValue = EquationValidatorUtil.fractionToDouble(root);
        } catch (ParseException ex) {
            throw new EquationException(String.format("invalid input for root: %s", root));
        }

        for (var e : equations) {
            var value = EquationEvaluatorUtil.evaluate(e, doubleRootValue);
            values.add(value);
        }

        if (!values.get(0).equals(values.get(1))) {
            throw new EquationException(String.format("Invalid root %s for equation %s", root, equation));
        }

        eq.setRoot(doubleRootValue);

        return eq;
    }

}
