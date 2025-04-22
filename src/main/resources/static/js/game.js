document.addEventListener('DOMContentLoaded', function() {
	const startScreen = document.getElementById('start-screen');
	    const gameScreen = document.getElementById('game-screen');
	    const vsComputerBtn = document.getElementById('vs-computer-btn');
	    const vsPlayerBtn = document.getElementById('vs-player-btn');
	    const playerForm = document.getElementById('player-form');
	    const player1NameInput = document.getElementById('player1-name');
	    const player2NameInput = document.getElementById('player2-name');
	    const startBtn = document.getElementById('start-btn');
	    const newGameBtn = document.getElementById('new-game-btn');
	    const playerTurnDisplay = document.getElementById('player-turn');
	    const gameStatusDisplay = document.getElementById('game-status');
	    const cells = document.querySelectorAll('.cell');
	    
	    let gameId = null;
	    let vsComputer = true;
    
  // Game mode selection
    vsComputerBtn.addEventListener('click', () => {
        vsComputer = true;
        player2NameInput.classList.add('hidden');
        playerForm.classList.remove('hidden');
    });
    
    vsPlayerBtn.addEventListener('click', () => {
        vsComputer = false;
        player2NameInput.classList.remove('hidden');
        playerForm.classList.remove('hidden');
    });
    
    // Start new game
    startBtn.addEventListener('click', startNewGame);
    newGameBtn.addEventListener('click', resetGame);
    
    // Cell click handler
    cells.forEach(cell => {
        cell.addEventListener('click', handleCellClick);
    });
    
    function startNewGame() {
        const player1Name = player1NameInput.value.trim();
        const player2Name = vsComputer ? null : player2NameInput.value.trim();
        
        if (!player1Name || (!vsComputer && !player2Name)) {
            alert('Please enter player names');
            return;
        }
        
        fetch('/api/game/start', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `player1Name=${encodeURIComponent(player1Name)}` +
                  `&player2Name=${vsComputer ? '' : encodeURIComponent(player2Name)}` +
                  `&vsComputer=${vsComputer}`
        })
        .then(response => response.text())
        .then(id => {
            gameId = id;
            startScreen.classList.add('hidden');
            gameScreen.classList.remove('hidden');
            updateGameState();
        })
        .catch(error => {
            console.error('Error starting game:', error);
            alert('Failed to start game');
        });
    }
    
    function handleCellClick(event) {
        if (!gameId) return;
        
        const row = parseInt(event.target.dataset.row);
        const col = parseInt(event.target.dataset.col);
        
        fetch(`/api/game/move/${gameId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `row=${row}&col=${col}`
        })
        .then(response => response.json())
        .then(updateBoard)
        .catch(error => {
            console.error('Error making move:', error);
        });
    }
    
    function updateGameState() {
        if (!gameId) return;
        
        fetch(`/api/game/state/${gameId}`)
            .then(response => response.json())
            .then(updateBoard)
            .catch(error => {
                console.error('Error fetching game state:', error);
            });
    }
    
    function updateBoard(gameState) {
        // Update board
        const board = gameState.board;
        for (let i = 0; i < 3; i++) {
            for (let j = 0; j < 3; j++) {
                const cell = document.querySelector(`.cell[data-row="${i}"][data-col="${j}"]`);
                cell.textContent = board[i][j] === ' ' ? '' : board[i][j];
            }
        }
        
        // Update game info
        const currentPlayer = gameState.currentPlayer;
        playerTurnDisplay.textContent = `${currentPlayer.name}'s turn (${currentPlayer.mark})`;
        
        // Update game status
        switch (gameState.gameStatus) {
            case 'PLAYER1_WIN':
                gameStatusDisplay.textContent = `${gameState.player1.name} wins!`;
                break;
            case 'PLAYER2_WIN':
                gameStatusDisplay.textContent = `${gameState.player2.name} wins!`;
                break;
            case 'DRAW':
                gameStatusDisplay.textContent = "It's a draw!";
                break;
            default:
                gameStatusDisplay.textContent = '';
        }
    }
    
    function resetGame() {
        gameId = null;
        gameScreen.classList.add('hidden');
        startScreen.classList.remove('hidden');
        playerNameInput.value = '';
        
        // Clear board
        cells.forEach(cell => {
            cell.textContent = '';
        });
    }
});