package com.jwick.continental.deathagreement.model;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @CodeGeneratorFieldDescriptor(fieldDescription = "Status field")
    @Column(length = 1, name = "in_status")
    private String status;

    @CodeGeneratorFieldDescriptor(fieldDescription = "record created at")
    @Column
    private Date dateCreated;

    @CodeGeneratorFieldDescriptor(fieldDescription = "record updated at")
    @Column
    private Date dateUpdated;
}
