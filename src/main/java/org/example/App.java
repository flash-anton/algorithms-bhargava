package org.example;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RestControllerAdvice
@Log4j2
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

    // e.g. http://localhost:8080/binarySearch?listSize=1_000_000
    @GetMapping(path = "/binarySearch")
    public String binarySearch(@RequestParam String listSize) {
        int i = Integer.parseInt(listSize.replaceAll("_", ""));
        if (i < 1 || i > 1_000_000_000) {
            throw new RuntimeException("listSize must be in range 1 ... 1_000_000_000");
        }
        Chapter1.BinarySearchResult bsr = Chapter1.binarySearch(new byte[i], (byte) 1);
        String res = String.format("list[%s] required %d steps %s", listSize, bsr.remains().size(), bsr.remains());
        log.info(res);
        return res;
    }
}
