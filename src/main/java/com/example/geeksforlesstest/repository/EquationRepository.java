package com.example.geeksforlesstest.repository;

import com.example.geeksforlesstest.entity.Equation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquationRepository extends JpaRepository<Equation, Long> {

    Optional<Equation> findByEquation(String equation);
}
