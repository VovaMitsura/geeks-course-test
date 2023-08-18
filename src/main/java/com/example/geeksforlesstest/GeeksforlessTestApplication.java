package com.example.geeksforlesstest;

import com.example.geeksforlesstest.service.EquationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication()
public class GeeksforlessTestApplication {


    public static void main(String[] args) {
        var appContext = SpringApplication.run(GeeksforlessTestApplication.class, args);
        var equationService = appContext.getBean(EquationService.class);

        equationService.save("2*x + 5 + 6) = 10");
    }

}
