package com.example.geeksforlesstest.repository;

import com.example.geeksforlesstest.entity.Equation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquationRepository extends JpaRepository<Equation, Long> {
}
