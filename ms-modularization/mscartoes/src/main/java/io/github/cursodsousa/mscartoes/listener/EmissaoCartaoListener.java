package io.github.cursodsousa.mscartoes.listener;

import com.google.gson.Gson;
import com.netflix.discovery.converters.Auto;
import io.github.cursodsousa.mscartoes.dto.DadosSolicitacaoEmissaoCartao;
import io.github.cursodsousa.mscartoes.model.Cartao;
import io.github.cursodsousa.mscartoes.model.ClienteCartao;
import io.github.cursodsousa.mscartoes.repository.CartaoRepository;
import io.github.cursodsousa.mscartoes.repository.ClienteCartaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmissaoCartaoListener {

    @Autowired private CartaoRepository cartaoRepository;
    @Autowired private ClienteCartaoRepository clienteCartaoRepository;

    @Autowired private Gson gson;

    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void onMessage(@Payload String payload) {
        log.info("onMessage :: message received from rabbit -> {}", payload);
        DadosSolicitacaoEmissaoCartao dados = gson.fromJson(payload, DadosSolicitacaoEmissaoCartao.class);
        Cartao cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow(() -> new RuntimeException("Erro ao criar cartao"));
        ClienteCartao clienteCartao = new ClienteCartao();
        clienteCartao.setCartao(cartao);
        clienteCartao.setCpf(dados.getCpf());
        clienteCartao.setLimite(dados.getLimiteLiberado());
        clienteCartaoRepository.save(clienteCartao);

        log.info("onMessage :: your request is being processed.");
    }
}
