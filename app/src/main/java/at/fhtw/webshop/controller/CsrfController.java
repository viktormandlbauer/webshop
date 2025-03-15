package at.fhtw.webshop.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(CsrfToken token) {
        return token;
    }

    @GetMapping("/test")
    public String getTest() {
        return "GET works!";
    }

    @PostMapping("/test")
    public String postTest(@RequestBody String body) {
        return "POST works: " + body;
    }
}