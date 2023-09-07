package io.github.cursodsousa.msclientes.controller;

import io.github.cursodsousa.msclientes.model.Cliente;
import io.github.cursodsousa.msclientes.dto.ClienteDTO;
import io.github.cursodsousa.msclientes.service.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@Slf4j
public class ClienteController {

    @Autowired private ClienteService clienteService;

    @GetMapping("/status")
    private String status() {
        log.info("obtendo status do microservico cliente {}", UUID.randomUUID());
        return "ok";
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<Cliente> getDadosCliente(@RequestParam("cpf") String cpf) {
        Optional<Cliente> cliente = clienteService.getByCPF(cpf);
        if(cliente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente.get());
    }

    @PostMapping
    public ResponseEntity save(@RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteDTO.toModel();
        clienteService.save(cliente);
        URI headerLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf={cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerLocation).build();


    }
}
