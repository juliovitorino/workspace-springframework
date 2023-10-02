package br.com.jcv.codegen.codegenerator.model;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "tb_user")
@CodeGeneratorDescriptor(outputDir = "/Users/juliovitorino/workspaces/workspace-springframework/jcv-modularization/jcv-code-generator/src/main/java/br/com/jcv/codegen/codegenerator",
        project = "aventura",
fullDescription = "Manipular todos as informações de usuários")
public class Usuario {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "id",fieldDescription = "Chave primaria de Usuario")
    private Long id;

    @Column()
    @CodeGeneratorFieldDescriptor(fieldDescription = "Nome do usuario")
    private String nome;

    @CodeGeneratorFieldDescriptor(fieldDescription = "Idade do usuário")
    @Column
    private Long idade;


    @CodeGeneratorFieldDescriptor(fieldDescription = "Status da entidade: (P)endende; (A)tivo; (B)loqueado; (D)eletado")
    @Column(length = 1)
    private String status;


    @CodeGeneratorFieldDescriptor(fieldDescription = "Idade do usuário")
    @Column
    private Date dateCreated;

    @CodeGeneratorFieldDescriptor(fieldDescription = "Idade do usuário")
    @Column
    private Date dateUpdated;



}
