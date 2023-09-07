package io.github.cursodsousa.msavaliadorcredito.controller;

import io.github.cursodsousa.msavaliadorcredito.exception.DadosClienteNotFoundException;
import io.github.cursodsousa.msavaliadorcredito.exception.IntegrationErrorClientConsumerException;
import io.github.cursodsousa.msavaliadorcredito.model.SituacaoCliente;
import io.github.cursodsousa.msavaliadorcredito.service.AvaliadorCreditoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avaliacoes-credito")
@Slf4j
public class AvaliadorCreditoController {

    @Autowired private AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping("/status")
    public String getStatus() {
        log.info("Serviço avaliacoes-credito is up");
        return "ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {

        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch(IntegrationErrorClientConsumerException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }
}
