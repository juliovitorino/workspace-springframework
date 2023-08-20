package br.com.jcv.microservice.microservicea.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/microservice")
public class MicroserviceAController {
    @GetMapping("/welcome")
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok("Welcome to microservice A");
    }
}
