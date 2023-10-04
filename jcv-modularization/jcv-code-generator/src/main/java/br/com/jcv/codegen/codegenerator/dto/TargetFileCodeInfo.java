package br.com.jcv.codegen.codegenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TargetFileCodeInfo implements Serializable {
    private String targetPathFile;
    private String targetExtension;
}
