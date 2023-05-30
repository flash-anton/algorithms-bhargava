package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@SpringBootApplication
@RestController
@RestControllerAdvice
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String exceptionHandler(Exception ex) {
        return ex.toString();
    }

    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping(path = "/binarySearch")
    public ArrayList<Integer> binarySearch(@RequestParam String listSize) {
        int i = Integer.parseInt(listSize.replaceAll("_",""));
        if (i < 1 || i > 1_000_000_000) {
            throw new RuntimeException("listSize must be in range 1 ... 1_000_000_000");
        }
        return Chapter1.binarySearch(new byte[i], (byte) 1).remains();
    }
}
