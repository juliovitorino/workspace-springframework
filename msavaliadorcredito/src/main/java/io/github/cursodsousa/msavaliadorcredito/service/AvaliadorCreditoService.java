package io.github.cursodsousa.msavaliadorcredito.service;

import feign.FeignException;
import io.github.cursodsousa.msavaliadorcredito.client.consumer.CartaoConsumer;
import io.github.cursodsousa.msavaliadorcredito.client.consumer.ClienteConsumer;
import io.github.cursodsousa.msavaliadorcredito.dto.CartaoCliente;
import io.github.cursodsousa.msavaliadorcredito.dto.DadosCliente;
import io.github.cursodsousa.msavaliadorcredito.exception.DadosClienteNotFoundException;
import io.github.cursodsousa.msavaliadorcredito.exception.IntegrationErrorClientConsumerException;
import io.github.cursodsousa.msavaliadorcredito.model.SituacaoCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliadorCreditoService {

    @Autowired private ClienteConsumer clienteConsumer;
    @Autowired private CartaoConsumer cartaoConsumer;

    public SituacaoCliente obterSituacaoCliente(String cpf) {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteConsumer.getDadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartaoClienteResponseEntity = cartaoConsumer.getCartoesByCliente(cpf);

            return SituacaoCliente.builder()
                    .dadosCliente(dadosClienteResponseEntity.getBody())
                    .cartaoClienteList(cartaoClienteResponseEntity.getBody())
                    .build();
        } catch (FeignException.FeignClientException e) {
            int status = e.status();

            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }

            throw new IntegrationErrorClientConsumerException(e.getMessage(), status);
        } catch (FeignException e) {
            throw new IntegrationErrorClientConsumerException(e.getMessage(), e.status());
        }

    }

}
