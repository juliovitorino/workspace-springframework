package io.github.cursodsousa.mscartoes.service;

import com.netflix.discovery.converters.Auto;
import io.github.cursodsousa.mscartoes.model.Cartao;
import io.github.cursodsousa.mscartoes.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartaoService {

    @Autowired private CartaoRepository cartaoRepository;

    @Transactional
    public Cartao save(Cartao cartao) {
        return cartaoRepository.save(cartao);
    }

    public List<Cartao> getCartoesRendaMenorIgual(Double renda) {
        return cartaoRepository.findByRendaLessThan(renda);
    }
}
