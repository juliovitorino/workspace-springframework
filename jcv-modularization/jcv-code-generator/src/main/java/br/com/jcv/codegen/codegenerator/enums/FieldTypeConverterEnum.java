package br.com.jcv.codegen.codegenerator.enums;

import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum FieldTypeConverterEnum {
    Long("Long", "Long", "BIGINT","Long.valueOf(entry.getValue().toString())"),
    String("String","String", "TEXT","entry.getValue().toString()"),
    Double("Double","Double","FLOAT","Double.valueOf(entry.getValue().toString())"),
    Integer("Integer", "Integer","BIGINT","Integer.valueOf(entry.getValue().toString())"),
    LocalDate("LocalDate","String", "DATE","entry.getValue().toString()"),
    LocalDateTime("LocalDateTime","String","DATE","entry.getValue().toString()"),
    Date("Date","String", "DATE","entry.getValue().toString()"),
    UUID("UUID","UUID", "TEXT","UUID.fromString(entry.getValue().toString())");

    private final String fieldType;
    private final String findByFilterType;
    private final String castType;
    private final String valueOf;

    private static FieldTypeConverterEnum[] VALUES = values();

    FieldTypeConverterEnum(String fieldType, String findByFilterType, String castType, String valueOf) {
        this.fieldType = fieldType;
        this.findByFilterType = findByFilterType;
        this.castType = castType;
        this.valueOf = valueOf;
    }

    public static FieldTypeConverterEnum fromFieldType(String fieldType) {
        return Arrays.stream(VALUES).filter(item -> item.toString().equals(fieldType)).findFirst().orElseThrow(
                () -> new CommoditieBaseException("Invalid Field Type " + fieldType,
                        HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }
}
