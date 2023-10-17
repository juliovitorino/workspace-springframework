package br.com.jcv.codegen.codegenerator.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CodeGeneratorTags {
    REGEX_VALIDATION("\\$\\{regexValidation\\}"),
    STATUS_CAMPO("\\$\\{campostatus\\}"),
    PROJECT("\\$\\{project\\}"),
    PROJETO("\\$\\{projeto\\}"),
    FIELD("\\$\\{field\\}"),
    CAMPO("\\$\\{campo\\}"),
    TABLE("\\$\\{table\\}"),
    TABELA("\\$\\{tabela\\}"),
    PKDTO("\\$\\{pkdto\\}"),
    PK("\\$\\{pk\\}"),
    SCHEMAP("\\$\\{schemap\\}"),
    SCHEMA("\\$\\{schema\\}"),
    TYPE("\\$\\{tipodart\\}"),
    ADTO("\\$\\{Adto\\}"),
    UDTO("\\$\\{Udto\\}"),
    CCDTO("\\$\\{CCdto\\}"),
    DTO("\\$\\{dto\\}"),
    AUTHOR("\\$\\{author\\}"),
    AUTOR("\\$\\{autor\\}"),
    NOW("\\$\\{now\\}"),
    AGORA("\\$\\{agora\\}"),
    MAGIC_CONTENT("\\$\\{magic-content\\}"),
    MAGIC_CONTENT_SECONDARY("\\$\\{magic-content-secondary\\}"),
    BASE_PACKAGE("\\$\\{package\\}"),
    BASE_CLASS("\\$\\{classebase\\}"),
    BASE_CLASS_LOWER("\\$\\{lclassebase\\}"),
    BASE_CLASS_UPPER("\\$\\{Uclassebase\\}")
    ;

    private String tag;
    public static final CodeGeneratorTags[] VALUES = values();

    CodeGeneratorTags(String tag) {
        this.tag = tag;
    }

    public static CodeGeneratorTags findFromTag(String tag) {
        return Arrays.stream(VALUES).filter(tagItem -> tagItem.getTag().toLowerCase().equals(tag)).findFirst().orElse(null);
    }
}
