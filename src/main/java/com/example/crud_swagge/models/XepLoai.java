package com.example.crud_swagge.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum XepLoai {
    GIOI("Giỏi"),
    KHA("khá"),
    TRUNG_BINH("Trung bình"),
    YEU("Yếu");

    private final String xl;

    XepLoai(String xl) {
        this.xl = xl;
    }

    @JsonValue
    public String getXl() {
        return xl;
    }

    @JsonCreator
    public static XepLoai fromValue(String value) {
        for (XepLoai xepLoai : values()) {
            if (xepLoai.xl.equalsIgnoreCase(value)) {
                return xepLoai;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
