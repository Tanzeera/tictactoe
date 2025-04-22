package com.example.tictactoe.controller;

import com.example.tictactoe.model.GameState;
import com.example.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://localhost:8080")  
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/start")
    public String startNewGame(@RequestParam String player1Name,
                             @RequestParam(required = false) String player2Name,
                             @RequestParam boolean vsComputer) {
        return gameService.createNewGame(player1Name, 
                                      vsComputer ? null : player2Name, 
                                      vsComputer);
    }

    @GetMapping("/state/{gameId}")
    public GameState getGameState(@PathVariable String gameId) {
        return gameService.getGameState(gameId);
    }

    @PostMapping("/move/{gameId}")
    public GameState makeMove(@PathVariable String gameId, 
                            @RequestParam int row, 
                            @RequestParam int col) {
        return gameService.makeMove(gameId, row, col);
    }
}