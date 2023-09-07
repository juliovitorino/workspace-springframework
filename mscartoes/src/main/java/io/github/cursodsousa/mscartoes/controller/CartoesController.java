package io.github.cursodsousa.mscartoes.controller;

import com.google.gson.Gson;
import io.github.cursodsousa.mscartoes.dto.CartaoRequest;
import io.github.cursodsousa.mscartoes.dto.CartoesPorClienteResponse;
import io.github.cursodsousa.mscartoes.model.Cartao;
import io.github.cursodsousa.mscartoes.model.ClienteCartao;
import io.github.cursodsousa.mscartoes.service.CartaoService;
import io.github.cursodsousa.mscartoes.service.ClienteCartaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@Slf4j
public class CartoesController {

    @Autowired private CartaoService cartaoService;
    @Autowired private ClienteCartaoService clienteCartaoService;
    @Autowired private Gson gson;

    @GetMapping("/status")
    public String getStatus() {
        return "ok";
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody CartaoRequest cartaoRequest) {
        log.info("salvar:: is starting with request -> {}", gson.toJson(cartaoRequest));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam Double renda) {
        return ResponseEntity.ok(cartaoService.getCartoesRendaMenorIgual(renda));
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam String cpf) {
        List<ClienteCartao> lista = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> resultList = lista.stream()
                .map(CartoesPorClienteResponse::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultList);

    }
}
