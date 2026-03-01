package gritgear.example.GritGear.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "This is public. Everyone can see me!";
    }

    @GetMapping("/private/secret")
    public String secretMessage() {
        return "You found the secret! You must be logged in.";
    }
}