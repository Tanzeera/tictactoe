package com.example.tictactoe.model;

import java.util.Random;

public class AIPlayer extends Player {
    public AIPlayer(String name, char mark) {
        this.name = name;
        this.mark = mark;
    }

    public Move makeMove(char[][] board) {
        int row, col;
        Random r = new Random();
        do {
            row = r.nextInt(3);
            col = r.nextInt(3);
        } while (!isValidMove(row, col, board));

        return new Move(row, col, this.mark);
    }

	@Override
	public Move makeMove() {
		// TODO Auto-generated method stub
		return null;
	}

	
}