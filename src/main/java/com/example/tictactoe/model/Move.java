package com.example.tictactoe.model;

public class Move {
    private int row;
    private int col;
    private char mark;

    public Move(int row, int col, char mark) {
        this.row = row;
        this.col = col;
        this.mark = mark;
    }

    // Getters and setters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getMark() {
        return mark;
    }
}