package com.example.geeksforlesstest.service;

import com.example.geeksforlesstest.entity.Equation;
import com.example.geeksforlesstest.exception.EquationException;
import com.example.geeksforlesstest.repository.EquationRepository;
import com.example.geeksforlesstest.utils.EquationValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
