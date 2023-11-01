package br.com.jcv.commons.library.commodities.enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public enum GenericStatusEnums {
    ATIVO("ATIVO", "A"),
    INATIVO("INATIVO", "I"),
    PENDENTE("PENDENTE", "P");

    private String value;
    private String shortValue;

    public static final GenericStatusEnums[] VALUES = values();

    GenericStatusEnums(String value, String shortValue) {
        this.value = value;
        this.shortValue = shortValue;
    }

    public static final GenericStatusEnums fromValue(String value) {
        return Arrays.stream(VALUES).filter(valueItem -> valueItem.getValue().equals(value)).findFirst().orElse(null);
    }
    public static final GenericStatusEnums fromShortValue(String value) {
        return Arrays.stream(VALUES).filter(valueItem -> valueItem.getShortValue().equals(value)).findFirst().orElse(null);
    }
    public static final GenericStatusEnums fromEnum(GenericStatusEnums value) {
        return Arrays.stream(VALUES).filter(valueItem -> valueItem.equals(value)).findFirst().orElse(null);
    }
}













































































































































