package io.github.cursodsousa.msavaliadorcredito.client.microservices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * value = propriedade spring.application.name do arquivo yml do projeto msclientes
 * path = RequestMapping do controller do projeto msclientes
 */
@FeignClient(value = "msclientes", path = "/clientes")
public interface Cliente {

    @GetMapping(params = "cpf")
    public ResponseEntity<Cliente> getDadosCliente(@RequestParam("cpf") String cpf)

}
