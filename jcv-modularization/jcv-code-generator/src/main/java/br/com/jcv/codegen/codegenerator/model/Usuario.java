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
@CodeGeneratorDescriptor(outputDir = "/Users/juliovitorino/workspaces/workspace-springframework/jcv-modularization/jcv-code-generator/src/main/java/br/com/jcv/codegen/codegenerator",
        project = "aventura",
fullDescription = "Manipular todos as informações de usuários")
public class Usuario {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "id",fieldDescription = "Chave primaria de Usuario")
    private Long id;

    @Column()
    @CodeGeneratorFieldDescriptor(fieldDescription = "Nome do usuario")
    private String nome;

    @CodeGeneratorFieldDescriptor(fieldDescription = "Idade do usuário")
    private Long idade;

}
