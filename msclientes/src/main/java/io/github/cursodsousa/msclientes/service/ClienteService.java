package io.github.cursodsousa.msclientes.service;

import io.github.cursodsousa.msclientes.domain.Cliente;
import io.github.cursodsousa.msclientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteService {

    @Autowired private ClienteRepository clienteRepository;

    @Transactional
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Optional<Cliente> getByCPF(String cpf) {
        log.info("getByCPF :: processId = {} :: buscando CPF = {}", UUID.randomUUID(), cpf);
        return clienteRepository.findByCpf(cpf);
    }
}
