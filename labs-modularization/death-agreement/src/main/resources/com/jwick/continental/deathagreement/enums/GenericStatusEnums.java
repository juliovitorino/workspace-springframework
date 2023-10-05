/*
Copyright <YEAR> <COPYRIGHT HOLDER>

This software is Open Source and is under MIT license agreement

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
OR OTHER DEALINGS IN THE SOFTWARE.
*/


package com.jwick.continental.deathagreement.enums;

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
