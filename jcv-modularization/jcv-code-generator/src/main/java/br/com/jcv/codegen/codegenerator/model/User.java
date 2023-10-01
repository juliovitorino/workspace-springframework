package br.com.jcv.codegen.codegenerator.model;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@CodeGeneratorDescriptor(project = "NovaTransporte",
        fullDescription = "Manipular todos as informações de users")
public class User{

    @Id
    @GeneratedValue
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "id",fieldDescription = "Chave primaria de Usuario")
    private Long id;

    @Column(name = "tx_first_name",nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Primeiro  nome")
    public String firstname;

    @Column(nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Ultimo nome")
    public String lastname;

    @Column(nullable = false, unique = true, length = 200)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Email do usuario")
    public String email;

    @JsonIgnore
    @Column(nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Senha criptografada do usuario em SHA-256")
    public String password;

    @JsonIgnore
    @Column
    @CodeGeneratorFieldDescriptor(fieldDescription = "Valor do salario")
    private String salt;

}