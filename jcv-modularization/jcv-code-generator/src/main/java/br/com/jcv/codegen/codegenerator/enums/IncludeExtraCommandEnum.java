package br.com.jcv.codegen.codegenerator.enums;

import lombok.Getter;
import org.h2.command.Command;

import java.util.Arrays;

@Getter
public enum IncludeExtraCommandEnum {
    excludeCharAtBeginFromFirstField("&& excludeCharAtBeginFromFirstField["),
    excludeCharAtEndOfLineFromLastField("&& excludeCharAtEndOfLineFromLastField["),
    excludeFields("&& excludeFields[");

    private String command;

    public static final IncludeExtraCommandEnum[] VALUES = values();

    IncludeExtraCommandEnum(String command) {
        this.command = command;
    }

    public static final IncludeExtraCommandEnum fromCommand(String command) {
        return Arrays.stream(VALUES)
                .filter(commandItem -> commandItem.getCommand().equals(command))
                .findFirst()
                .orElse(null);
    }
}
