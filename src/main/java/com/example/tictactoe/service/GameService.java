package com.example.tictactoe.service;

import com.example.tictactoe.model.*;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {
    private Map<String, GameState> games = new HashMap<>();

    @PostConstruct
    public void init() {
        
    }

    public String createNewGame(String player1Name, String player2Name, boolean vsComputer) {
        String gameId = UUID.randomUUID().toString();
        GameState gameState = new GameState();
        
        gameState.setPlayer1(new HumanPlayer(player1Name, 'X'));
        
        if (vsComputer) {
            gameState.setPlayer2(new AIPlayer("Computer", 'O'));
        } else {
            gameState.setPlayer2(new HumanPlayer(player2Name, 'O'));
        }
        
        gameState.setCurrentPlayer(gameState.getPlayer1());
        gameState.setGameStatus("IN_PROGRESS");
        
        games.put(gameId, gameState);
        return gameId;
    }

    public GameState getGameState(String gameId) {
        return games.get(gameId);
    }

    public GameState makeMove(String gameId, int row, int col) {
        GameState gameState = games.get(gameId);
        if (gameState == null || !"IN_PROGRESS".equals(gameState.getGameStatus())) {
            return gameState;
        }

        Player currentPlayer = gameState.getCurrentPlayer();
        char[][] board = gameState.getBoard();

        if (currentPlayer.isValidMove(row, col, board)) {
            board[row][col] = currentPlayer.getMark();
            
            if (checkWin(board, currentPlayer.getMark())) {
                gameState.setGameStatus(currentPlayer == gameState.getPlayer1() ? "PLAYER1_WIN" : "PLAYER2_WIN");
            } else if (isBoardFull(board)) {
                gameState.setGameStatus("DRAW");
            } else {
                // Switch player
                gameState.setCurrentPlayer(
                    currentPlayer == gameState.getPlayer1() ? gameState.getPlayer2() : gameState.getPlayer1());
                
                // If next player is AI, make its move
                if (gameState.getCurrentPlayer() instanceof AIPlayer) {
                    Move aiMove = ((AIPlayer) gameState.getCurrentPlayer()).makeMove(board);
                    board[aiMove.getRow()][aiMove.getCol()] = aiMove.getMark();
                    
                    if (checkWin(board, aiMove.getMark())) {
                        gameState.setGameStatus("PLAYER2_WIN");
                    } else if (isBoardFull(board)) {
                        gameState.setGameStatus("DRAW");
                    } else {
                        gameState.setCurrentPlayer(gameState.getPlayer1());
                    }
                }
            }
        }

        return gameState;
    }

    private boolean checkWin(char[][] board, char mark) {
        
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) {
                return true;
            }
        }
        
      
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == mark && board[1][j] == mark && board[2][j] == mark) {
                return true;
            }
        }
        
       
        if (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) {
            return true;
        }
        if (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark) {
            return true;
        }
        
        return false;
    }

    private boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}