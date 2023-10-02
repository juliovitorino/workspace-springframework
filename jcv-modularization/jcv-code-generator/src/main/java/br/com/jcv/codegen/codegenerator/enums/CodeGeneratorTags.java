package br.com.jcv.codegen.codegenerator.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CodeGeneratorTags {

    BASE_PACKAGE("\\$\\{package\\}"),
    BASE_CLASS("\\$\\{classebase\\}"),
    BASE_CLASS_LOWER("\\$\\{lclassebase\\}")
    ;

    private String tag;
    public static final CodeGeneratorTags VALUES[] = values();

    CodeGeneratorTags(String tag) {
        this.tag = tag;
    }

    public static CodeGeneratorTags findFromTag(String tag) {
        return Arrays.stream(VALUES).filter(tagItem -> tagItem.getTag().toLowerCase().equals(tag)).findFirst().orElse(null);
    }
}
