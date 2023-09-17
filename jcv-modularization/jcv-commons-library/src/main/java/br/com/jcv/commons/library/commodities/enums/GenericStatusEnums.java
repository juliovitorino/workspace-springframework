package br.com.jcv.commons.library.commodities.enums;

public enum GenericStatusEnums {
    ATIVO("ATIVO", "A"),
    INATIVO("INATIVO", "I"),
    PENDENTE("PENDENTE", "P");

    private String value;
    private String shortValue;

    GenericStatusEnums(String value, String shortValue) {
        this.value = value;
        this.shortValue = shortValue;
    }

    public String getShortValue() {
        return shortValue;
    }

    public void setShortValue(String shortValue) {
        this.shortValue = shortValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return this.getValue();
    }
}













































































































































