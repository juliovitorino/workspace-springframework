package br.com.jcv.codegen.codegenerator.enums;

import br.com.jcv.codegen.codegenerator.dto.TargetFileCodeInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum TargetFileEnum {
    CodeGeneratorAbstractAnalyser("CodeGeneratorAbstractAnalyser", "/analyser/AbstractAnalyser"),
    CodeGeneratorAnalyserCPF("CodeGeneratorAnalyserCPF","/analyser/AnalyserCPF"),
    CodeGeneratorInvalidFormatException("CodeGeneratorInvalidFormatException", "/exception/InvalidFormatException"),
    CodeGeneratorAnalyserException("CodeGeneratorAnalyserException", "/exception/AnalyserException"),
    CodeGeneratorBusinessService("CodeGeneratorBusinessService" ,"/interfaces/BusinessService"),
    CodeGeneratorCommoditiesBaseException("CodeGeneratorCommoditiesBaseException", "/exception/CommoditieBaseException"),
    CodeGeneratorGenericConstantes("CodeGeneratorGenericConstantes", "/constantes/GenericConstantes"),
    CodeGeneratorGenericResponse("CodeGeneratorGenericResponse","/dto/GenericErrorResponse"),
    CodeGeneratorGenericStatusEnum("CodeGeneratorGenericStatusEnum" ,"/enums/GenericStatusEnums"),
    CodeGeneratorIAnalyser("CodeGeneratorIAnalyser" ,"/analyser/IAnalyser"),
    CodeGeneratorLogback("CodeGeneratorLogback","/dto/logback"),
    CodeGeneratorMensagemConstantes("CodeGeneratorMensagemConstantes","/dto/MensagemConstantes"),
    CodeGeneratorMensagemResponse("CodeGeneratorMensagemResponse","/dto/MensagemResponse"),
    CodeGeneratorRegexConstantes("CodeGeneratorRegexConstantes","/constantes/RegexConstantes"),
    CodeGeneratorRequestFilter("CodeGeneratorRequestFilter","/dto/RequestFilter"),
    CodeGeneratorSwaggerConfig("CodeGeneratorSwaggerConfig","/config/SwaggerConfig")
    ;

    private final String codeGeneratorClass;
    private final String targetFilePath;

    public static TargetFileEnum[] VALUES = values();
    TargetFileEnum(String codeGeneratorClass, String targetFilePath) {
        this.codeGeneratorClass = codeGeneratorClass;
        this.targetFilePath = targetFilePath;
    }

    public static TargetFileEnum fromCodeGeneratorClass(String codeGeneratorClass) {
        return Arrays.stream(VALUES).filter(enumItem -> enumItem.codeGeneratorClass.trim().equals(codeGeneratorClass)).findFirst().orElse(null);
    }

}
