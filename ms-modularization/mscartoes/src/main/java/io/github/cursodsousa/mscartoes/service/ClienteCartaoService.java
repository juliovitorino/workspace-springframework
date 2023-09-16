package io.github.cursodsousa.mscartoes.service;

import io.github.cursodsousa.mscartoes.model.ClienteCartao;
import io.github.cursodsousa.mscartoes.repository.ClienteCartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteCartaoService {

    @Autowired private ClienteCartaoRepository clienteCartaoRepository;

    public List<ClienteCartao> listCartoesByCpf(String cpf) {
        return clienteCartaoRepository.findByCpf(cpf);
    }
}
