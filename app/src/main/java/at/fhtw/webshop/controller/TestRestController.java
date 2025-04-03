package at.fhtw.webshop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

    @GetMapping("/test-json")
    public String getTest() {
        // Return json
        return "{\"message\": \"GET works\"}";
    }

    @PostMapping("/test-json")
    public String postTest(@RequestBody String body) {
       // Return json
        return "{\"message\": \"POST works\"}";
    }
}