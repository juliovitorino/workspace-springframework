package io.github.cursodsousa.msclientes.dto;

import io.github.cursodsousa.msclientes.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private String cpf;
    private String nome;
    private Integer idade;

    public Cliente toModel() {
        return new Cliente(cpf,nome,idade);
    }
}
