package br.com.jcv.microservice.microserviceb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/microserviceb")
public class MicroserviceBController {

    @GetMapping("/welcome")
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok("Welcome to microservice B");
    }
}
