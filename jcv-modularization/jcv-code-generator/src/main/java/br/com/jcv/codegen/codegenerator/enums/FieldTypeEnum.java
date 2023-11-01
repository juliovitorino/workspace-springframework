package br.com.jcv.codegen.codegenerator.enums;

import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum FieldTypeEnum {
    Long, String, Double, Integer, LocalDate, Date, UUID;

    private static FieldTypeEnum[] VALUES = values();
    public static FieldTypeEnum fromType(String fieldType) {
        return Arrays.stream(VALUES).filter(item -> item.toString().equals(fieldType)).findFirst().orElseThrow(
                () -> new CommoditieBaseException("Invalid Type " + fieldType,
                        HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }
}
