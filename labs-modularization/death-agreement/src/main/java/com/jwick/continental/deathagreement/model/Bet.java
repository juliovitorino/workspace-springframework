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
import java.util.UUID;

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
    @Column(name = "id_bet")
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "id",fieldDescription = "Bet primary key")
    private Long id;

    @Column(name = "id_punter")
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "idPunter",fieldDescription = "Punter's ID")
    private Long idPunter;

    @Column(name = "id_bet_object")
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bet Obejct's ID")
    private Long idBetObject;

    @Column(precision=20, scale=8)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bet's value")
    private Double bet;

    @Column(name = "btc_address")
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bitcoin address")
    private String bitcoinAddress;

    @Column
    @CodeGeneratorFieldDescriptor(fieldDescription = "Ticket for bet")
    private UUID ticket;

    @Column(name = "death_date")
    @CodeGeneratorFieldDescriptor(fieldDescription = "Suggest death date for Object")
    private Date deathDate;

    @CodeGeneratorFieldDescriptor(fieldDescription = "Status field")
    @Column(length = 1)
    private String status;

    @CodeGeneratorFieldDescriptor(fieldDescription = "record created at")
    @Column(name = "date_created")
    private Date dateCreated;

    @CodeGeneratorFieldDescriptor(fieldDescription = "record updated at")
    @Column(name = "date_updated")
    private Date dateUpdated;


}
