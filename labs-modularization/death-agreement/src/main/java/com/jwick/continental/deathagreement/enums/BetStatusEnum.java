package com.jwick.continental.deathagreement.enums;

import com.jwick.continental.deathagreement.exception.InvalidBetStatusException;
import com.jwick.continental.deathagreement.model.Bet;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum BetStatusEnum {
    PENDING("Pending", "P"),
    WORKING("Working", "A"),
    FINISHED("Finished", "F");

    private String value;
    private String status;

    public static final BetStatusEnum[] VALUES = values();
    BetStatusEnum(String status, String value) {
        this.value = value;
        this.status = status;
    }

    public static BetStatusEnum fromValue(String value) {
        return Arrays.stream(VALUES).filter(valueItem -> valueItem.getValue().equals(value)).findFirst().orElseThrow(
                ()-> new InvalidBetStatusException("Invalid status " + value, HttpStatus.BAD_REQUEST)
        );
    }

    public static BetStatusEnum fromStatus(String status) {
        return Arrays.stream(VALUES).filter(valueItem -> valueItem.getStatus().equals(status)).findFirst().orElseThrow(
                ()-> new InvalidBetStatusException("Invalid status " + status, HttpStatus.BAD_REQUEST)
        );
    }


}
