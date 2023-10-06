package com.jwick.continental.deathagreement.model;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@CodeGeneratorDescriptor(outputDir = "/Users/juliovitorino/workspaces/workspace-springframework/labs-modularization/death-agreement/src/main/resources",
        project = "",
        fullDescription = "Control all Bet")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "id",fieldDescription = "Bet primary key")
    private Long id;

    @Column
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "idPunter",fieldDescription = "Punter's ID")
    private Long idPunter;

    @Column
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bet Obejct's ID")
    private Long idBetObject;

    @Column
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bet's value")
    private Double bet;

    @Column
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bitcoin address")
    private String bitcoinAddress;

    @CodeGeneratorFieldDescriptor(fieldDescription = "Status field")
    @Column(length = 1)
    private String status;

    @CodeGeneratorFieldDescriptor(fieldDescription = "record created at")
    @Column
    private Date dateCreated;

    @CodeGeneratorFieldDescriptor(fieldDescription = "record updated at")
    @Column
    private Date dateUpdated;


}
