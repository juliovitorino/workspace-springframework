package br.com.jcv.codegen.codegenerator.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GenericStatusEnums {
    MANUTENCAO("MANUTENCAO", "W"),
    BLOQUEADO("BLOQUEADO", "B"),
    PENDENTE("PENDENTE", "P"),
    ATIVO("ATIVO", "A"),
    INATIVO("INATIVO", "I");

    private String value;
    private String shortValue;

    GenericStatusEnums(String value, String shortValue) {
        this.value = value;
        this.shortValue = shortValue;
    }

    public static final GenericStatusEnums[] VALUES = values();

    public static GenericStatusEnums fromValue(String value) {
        return Arrays.stream(VALUES).filter(valueItem -> valueItem.getValue().toUpperCase().equals(value)).findFirst().orElse(null);
    }
    public static GenericStatusEnums fromShortValue(String shortValue) {
        return Arrays.stream(VALUES).filter(valueItem -> valueItem.getShortValue().toUpperCase().equals(shortValue)).findFirst().orElse(null);
    }
}
