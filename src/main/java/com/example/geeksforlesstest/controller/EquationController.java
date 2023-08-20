package com.example.geeksforlesstest.controller;

import com.example.geeksforlesstest.dto.EquationDTO;
import com.example.geeksforlesstest.dto.EquationRootDTO;
import com.example.geeksforlesstest.entity.Equation;
import com.example.geeksforlesstest.service.EquationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping()
    public ResponseEntity<Equation> addRootToEquation(@RequestBody EquationRootDTO equationRootDTO) {
        var response = equationService.addRoot(equationRootDTO.getEquation(), equationRootDTO.getRoot());
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/unique")
    public ResponseEntity<List<Equation>> getAllEquationWithUniqueRoot() {
        var response = equationService.getAllEquationWithUniqueRoot();
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<Equation>> getEquations(@RequestParam(name = "root1") String root1,
                                                       @RequestParam(required = false, name = "root2") String root2) {

        List<Equation> response;
        if (root2 == null)
            response = equationService.getAllEquationByRoot(root1);
        else
            response = equationService.getAllEquationByRootBetween(root1, root2);

        return ResponseEntity.ok(response);
    }

}
