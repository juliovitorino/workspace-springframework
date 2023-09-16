package io.github.cursodsousa.msavaliadorcredito.exception;

public class DadosClienteNotFoundException extends RuntimeException {
    public DadosClienteNotFoundException() {
        super("Dados do cliente não encontrado");
    }
}
