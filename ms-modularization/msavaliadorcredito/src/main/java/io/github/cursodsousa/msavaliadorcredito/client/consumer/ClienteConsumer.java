package io.github.cursodsousa.msavaliadorcredito.client.consumer;

import io.github.cursodsousa.msavaliadorcredito.dto.DadosCliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * value = propriedade spring.application.name do arquivo yml do projeto msclientes
 * path = RequestMapping do controller do projeto msclientes
 */
@FeignClient(value = "msclientes", path = "/clientes")
public interface ClienteConsumer {

    @GetMapping(params = "cpf")
    public ResponseEntity<DadosCliente> getDadosCliente(@RequestParam("cpf") String cpf);

}
