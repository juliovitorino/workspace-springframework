package br.com.jcv.codegen.codegenerator.model;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user", schema = "seglog")
@CodeGeneratorDescriptor(basePackage = "br.com.jcv.projeto",project = "aventura",
fullDescription = "Manipular todos as informações de usuários")
public class Usuario {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CodeGeneratorFieldDescriptor(dtoFieldReference = "id",fieldDescription = "Chave primaria de Usuario")
    private Long id;

    @Column()
    @CodeGeneratorFieldDescriptor(fieldDescription = "Nome do usuario")
    private String nome;

    @CodeGeneratorFieldDescriptor(fieldDescription = "Idade do usuário")
    private Long idade;

}
