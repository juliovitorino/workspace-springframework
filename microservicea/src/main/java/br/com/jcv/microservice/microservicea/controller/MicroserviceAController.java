package br.com.jcv.microservice.microservicea.controller;

import br.com.jcv.exchange.dto.book.BookDTO;
import br.com.jcv.microservice.microservicea.service.MicroserviceAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/")
    public ResponseEntity<BookDTO> book(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(microserviceAService.callBookOnB(bookDTO));
    }
}
