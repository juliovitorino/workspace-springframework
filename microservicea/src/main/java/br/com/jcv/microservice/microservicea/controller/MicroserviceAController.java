package br.com.jcv.microservice.microservicea.controller;

import br.com.jcv.microservice.microservicea.service.MicroserviceAService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/microservicea")
public class MicroserviceAController {
    @Autowired private MicroserviceAService microserviceAService;
    @GetMapping("/welcome")
    public ResponseEntity<String> getMessage() {
        return ResponseEntity.ok("Welcome to microservice A");
    }

    @GetMapping("/callb")
    public ResponseEntity<String> callb() {
        return ResponseEntity.ok(microserviceAService.getWelcomeFromB());
    }
}
