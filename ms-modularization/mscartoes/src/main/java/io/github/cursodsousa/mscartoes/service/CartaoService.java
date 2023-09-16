package io.github.cursodsousa.mscartoes.service;

import io.github.cursodsousa.mscartoes.model.Cartao;
import io.github.cursodsousa.mscartoes.repository.CartaoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CartaoService {

    @Autowired private CartaoRepository cartaoRepository;

    @Transactional
    public Cartao save(Cartao cartao) {
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> getCartoesRendaMenorIgual(Double renda) {
        log.info("Solicitando cartoes com renda abaixo de {}", renda);
        return cartaoRepository.findByRendaLessThanEqual(renda);
    }
}
