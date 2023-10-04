package br.com.jcv.labs.microservicea.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mservicea")
@Slf4j
public class MServicoA {

    public static final String GET_STATUS_MICROSERVIÇO_A_UP = "Microserviço A - is Up";

    @GetMapping
    public ResponseEntity getStatus() {
        log.info("getStatus :: " + GET_STATUS_MICROSERVIÇO_A_UP);
        return ResponseEntity.ok(GET_STATUS_MICROSERVIÇO_A_UP);
    }
}
