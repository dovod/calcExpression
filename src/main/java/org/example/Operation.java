package org.example;

import java.util.Arrays;

public enum Operation {
    ADD("+"),
    SUB("-"),
    MOD("/"),
    MUL("*");

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public static Operation getBySymbol(String letter) {
        return Arrays.stream(Operation.values())
                .filter(o -> o.symbol.equals(letter))
                .findFirst()
                .orElse(null);
    }

    public static Operation getBySymbol(char symbol) {
        return getBySymbol(String.valueOf(symbol));
    }

    public String getSymbol() {
        return symbol;
    }
}
