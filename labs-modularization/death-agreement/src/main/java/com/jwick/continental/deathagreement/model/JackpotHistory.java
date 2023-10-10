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
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@CodeGeneratorDescriptor(outputDir = "/Users/juliovitorino/workspaces/workspace-springframework/labs-modularization/death-agreement/src/main/resources",
        project = "",
        fullDescription = "Control all Users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "jackpot_history")
public class JackpotHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jackpot")
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "id",fieldDescription = "User primary key")
    private Long id;

    @Column(nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Free description for bet")
    private String description;

    @Column(nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Type: B = Balance, C = Credit")
    private String type;

    @Column(name = "bet_value", precision=20, scale=8, nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bet value input for jackpot")
    private Double betValue;

    @Column(nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Ticket for punter")
    private UUID ticket;

    @Column(name = "id_punter", nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "ID punter")
    private Long idPunter;

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
