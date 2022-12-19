package org.example;

public enum Mode {


    ENCRYPT("encrypting"),
    DECRYPT("decrypting");

    private final String name;

    Mode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
