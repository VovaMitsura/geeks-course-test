package com.example.geeksforlesstest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "equation")
public class Equation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String equation;
    private Double root;

    public Equation(String equation) {
        this.equation = equation;
    }

    public Equation(String equation, Double root) {
        this.equation = equation;
        this.root = root;
    }

    public Optional<Double> getRoot(){
        return Optional.ofNullable(root);
    }
}
