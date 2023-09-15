package io.github.cursodsousa.msmensageiro.controller;

import com.google.gson.Gson;
import io.github.cursodsousa.msmensageiro.dto.GeneralRequest;
import io.github.cursodsousa.msmensageiro.producer.AdminExchangeDirectProducer;
import io.github.cursodsousa.msmensageiro.producer.IProducer;
import io.github.cursodsousa.msmensageiro.service.MensageiroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/mensageiro")
@Slf4j
public class MensageiroController {
    @Autowired private MensageiroService mensageiroService;
    @Autowired private Gson gson;

    @GetMapping("/status")
    public String status() {
        log.info("obtendo status do microservico mensageiro {}", UUID.randomUUID());
        return "ok";
    }

    @PostMapping("producer/{type}")
    public ResponseEntity sendMessageToAdmin(@RequestBody GeneralRequest request, @PathVariable(name = "type") String type) {
        log.info("sendMessageToAdmin :: is starting with request -> {}", gson.toJson(request));
        if(type.toLowerCase().equals("admin")) {
            mensageiroService.sendMessageToAdmin(request,true);
        } else if(type.toLowerCase().equals("finance")) {
            mensageiroService.sendMessageToFinance(request,true);
        } else {
            mensageiroService.sendMessageToMarketing(request,true);
        }
        return ResponseEntity.ok("Sua solicitação foi enviada para processamento.");
    }



}
