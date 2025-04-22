package com.example.tictactoe.model;

public abstract class Player {
    protected String name;
    protected char mark;

    public abstract Move makeMove();

    public boolean isValidMove(int row, int col, char[][] board) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            return board[row][col] == ' ';
        }
        return false;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public char getMark() {
        return mark;
    }
}