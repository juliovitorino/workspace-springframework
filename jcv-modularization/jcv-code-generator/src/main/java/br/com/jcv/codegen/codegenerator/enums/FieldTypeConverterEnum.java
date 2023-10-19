package br.com.jcv.codegen.codegenerator.enums;

import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum FieldTypeConverterEnum {
    Long("Long", "Long", "BIGINT"),
    String("String","String", "TEXT"),
    Double("Double","Double","FLOAT"),
    Integer("Integer", "Integer","BIGINT"),
    LocalDate("LocalDate","String", "DATE"),
    LocalDateTime("LocalDateTime","String","DATE"),
    Date("Date","String", "DATE"),
    UUID("UUID","UUID", "TEXT");

    private final String fieldType;
    private final String findByFilterType;
    private final String castType;

    private static FieldTypeConverterEnum[] VALUES = values();

    FieldTypeConverterEnum(String fieldType, String findByFilterType, String castType) {
        this.fieldType = fieldType;
        this.findByFilterType = findByFilterType;
        this.castType = castType;
    }

    public static FieldTypeConverterEnum fromFieldType(String fieldType) {
        return Arrays.stream(VALUES).filter(item -> item.toString().equals(fieldType)).findFirst().orElseThrow(
                () -> new CommoditieBaseException("Invalid Field Type " + fieldType,
                        HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }
}
