package io.github.cursodsousa.msavaliadorcredito.service;

import feign.FeignException;
import io.github.cursodsousa.msavaliadorcredito.client.consumer.CartaoConsumer;
import io.github.cursodsousa.msavaliadorcredito.client.consumer.ClienteConsumer;
import io.github.cursodsousa.msavaliadorcredito.dto.Cartao;
import io.github.cursodsousa.msavaliadorcredito.dto.CartaoAprovado;
import io.github.cursodsousa.msavaliadorcredito.dto.CartaoCliente;
import io.github.cursodsousa.msavaliadorcredito.dto.DadosCliente;
import io.github.cursodsousa.msavaliadorcredito.dto.DadosSolicitacaoEmissaoCartao;
import io.github.cursodsousa.msavaliadorcredito.dto.ProtocoloSolicitacaoCartao;
import io.github.cursodsousa.msavaliadorcredito.exception.DadosClienteNotFoundException;
import io.github.cursodsousa.msavaliadorcredito.exception.IntegrationErrorClientConsumerException;
import io.github.cursodsousa.msavaliadorcredito.dto.RetornoAvaliacaoCliente;
import io.github.cursodsousa.msavaliadorcredito.dto.SituacaoCliente;
import io.github.cursodsousa.msavaliadorcredito.producer.EmissaoCartaoProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvaliadorCreditoService {

    @Autowired private ClienteConsumer clienteConsumer;
    @Autowired private CartaoConsumer cartaoConsumer;
    @Autowired private EmissaoCartaoProducer emissaoCartaoProducer;

    public ProtocoloSolicitacaoCartao solictarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
        for(int i=0; i<50000; i++) {
            emissaoCartaoProducer.execute(dados);
        }
        return new ProtocoloSolicitacaoCartao(UUID.randomUUID().toString());
    }
    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Double renda) {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponseEntity = clienteConsumer.getDadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartaoListResponseEntity = cartaoConsumer.getCartoesRendaAteh(renda);

            DadosCliente dadosCliente = dadosClienteResponseEntity.getBody();
            List<Cartao> cartaoList = cartaoListResponseEntity.getBody();

            List<CartaoAprovado> cartoesAprovados = cartaoList
                    .stream()
                    .map(cartao -> {
                        Double fator = dadosCliente.getIdade().doubleValue() / 10;
                        Double limiteAprovado = fator * cartao.getLimiteCartao();

                        CartaoAprovado cartaoAprovado = new CartaoAprovado();
                        cartaoAprovado.setCartao(cartao.getNome());
                        cartaoAprovado.setBandeira(cartao.getBandeiraCartao());
                        cartaoAprovado.setLimiteAprovado(limiteAprovado);
                        return cartaoAprovado;
                    })
                    .collect(Collectors.toList());
            return new RetornoAvaliacaoCliente(cartoesAprovados);

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
