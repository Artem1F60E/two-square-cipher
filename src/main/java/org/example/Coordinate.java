package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
class Coordinate {
    private int row;
    private int column;

    public Coordinate() {
        this.row = -1;
        this.column = -1;
    }

    public boolean isValid() {
        return row != -1 && column != -1;
    }
}
