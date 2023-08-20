package com.example.geeksforlesstest.repository;

import com.example.geeksforlesstest.entity.Equation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquationRepository extends JpaRepository<Equation, Long> {

    Optional<Equation> findByEquation(String equation);

    List<Equation> findAllByRoot(Double root);
    List<Equation> findAllByRootBetween(Double root1, Double root2);
    @Query(value = "SELECT id, equation, root FROM Equation GROUP BY root HAVING COUNT(*) = 1;", nativeQuery = true)
    List<Equation> findAllUniqueRoot();
}
