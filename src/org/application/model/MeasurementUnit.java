package org.application.model;

public enum MeasurementUnit {
    METERS("meters"),
    SQ_METERS("sq meters"),
    PIECES("pieces"),
    LITERS("liters"),
    KILOGRAMS("kilograms");

    private final String value;

    MeasurementUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
