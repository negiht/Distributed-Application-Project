package com.alexronnegi.serialization;

import java.io.Serializable;

public class MagicToken implements Serializable {
    private int value;

    public MagicToken() {
    }

    public MagicToken(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
