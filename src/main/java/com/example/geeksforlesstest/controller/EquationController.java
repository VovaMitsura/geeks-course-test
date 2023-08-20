package com.example.geeksforlesstest.controller;

import com.example.geeksforlesstest.dto.EquationDTO;
import com.example.geeksforlesstest.entity.Equation;
import com.example.geeksforlesstest.service.EquationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/equation")
public class EquationController {

    private final EquationService equationService;

    @PostMapping()
    public ResponseEntity<Equation> addEquation(@RequestBody EquationDTO request) {
        var response = equationService.save(request.getEquation());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("")
    public ResponseEntity<Equation> addRootToEquation(@RequestParam("equation") String equation, @RequestParam("root") String root) {
        var response = equationService.addRoot(equation, root);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}
