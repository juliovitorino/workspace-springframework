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
    CAMPO_DATE_FIX("\\$\\{campo-date-fix\\}"),
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
    MAGIC_CONTENT_FILTER("\\$\\{magic-content-filter\\}"),
    MAGIC_CONTENT_LONG("\\$\\{magic-content-long\\}"),
    MAGIC_CONTENT_SECONDARY("\\$\\{magic-content-secondary\\}"),
    CONVERT_FROM_FIELDTYPE_TO_FINDBYFILTERTYPE("\\$\\{convert-from-field-type-to-find-by-filter-type\\}"),
    CONVERT_FROM_FIELDTYPE_TO_VALUEOF("\\$\\{value-of\\}"),
    CONVERT_FROM_FIELDTYPE_TO_CASTVALUE("\\$\\{cast-value\\}"),
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
