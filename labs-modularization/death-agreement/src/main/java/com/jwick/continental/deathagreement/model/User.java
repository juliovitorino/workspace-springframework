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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @CodeGeneratorFieldDescriptor(fieldReferenceInDto = "id",fieldDescription = "User primary key")
    private Long id;

    @Column(nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "User nickname")
    private String nickname;

    @Column(name = "btc_address", nullable = false)
    @CodeGeneratorFieldDescriptor(fieldDescription = "Bitcoin wallet address")
    private String btcAddress;

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
