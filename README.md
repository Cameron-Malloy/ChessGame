# Chess Game with AI

A complete GUI chess game implementation in Java with my ehhhhhhh attempt at an AI opponent. 

## Game Controls

### Mouse Controls
- **Click to Select**: Click on a piece to select it
- **Click to Move**: Click on a highlighted square to move the selected piece
- **Deselect**: Click on an empty square or another piece to deselect

### Game Flow
1. Choose game mode from the start screen
2. White moves first (human player)
3. In AI mode, Black is controlled by the computer #After you move, click the screen for the AI to start thinking!
4. The game continues until checkmate, stalemate, or resignation
5. Winner is announced on the end screen

## How to Run

### Prerequisites
- Java 8 or higher
- Image file: `resources/chess_pieces.png` (included)

### Running the Game

#### On Windows:
```bash
run.bat
```

#### On macOS/Linux:
```bash
chmod +x run.sh
./run.sh
```

#### Manual Compilation:
```bash
javac -cp . src/*.java
java -cp . src.ChessBoard
```

## Features

### Game Modes
- **Classic 1v1 Chess**: Play against another human player
- **Play Versus AI**: Challenge the computer AI

### Chess Rules Implementation
- Complete chess piece movement rules
- Special moves: castling, en passant, pawn promotion
- Check and checkmate detection
- Stalemate detection
- Legal move highlighting
- Turn-based gameplay

### AI Features
- **Minimax Algorithm**: Core decision-making algorithm
- **Alpha-Beta Pruning**: Optimizes search efficiency
- **Quiescence Search**: Prevents horizon effect in tactical positions
- **Dynamic Search Depth**: Adapts search depth based on game phase
- **Comprehensive Evaluation**: Considers material, position, tactics, and safety

### AI Evaluation Components
- **Material Evaluation**: Piece values and material balance
- **Positional Evaluation**: Piece positioning and development
- **Tactical Evaluation**: Capture opportunities and tactical threats
- **King Safety**: King protection and safety assessment
- **Center Control**: Control of central squares
- **Mobility**: Number of legal moves available
- **Opening Development**: Proper piece development in opening
- **Queen Safety**: Queen protection and positioning
- **Endgame Evaluation**: Specialized endgame assessment
- **Hanging Piece Detection**: Identifies undefended pieces

### User Interface
- Legal move highlighting (green dots for moves, red circles for captures)
- King in check highlighting
- Score tracking for both players
- Turn indicator
- Game over screen with winner announcement
- Restart functionality


## Technical Details

### Architecture
- **ChessBoard.java**: Main game logic, UI, and AI implementation
- **Pieces.java**: Individual piece behavior and movement rules
- **CardLayout**: Manages different game screens (start, game, end)

### AI Algorithm
- **Search Depth**: 3-6 plies depending on game phase
- **Move Ordering**: Prioritizes captures and center moves for better pruning
- **Evaluation Function**: Multi-component scoring system
- **Fallback**: Random move generation if no minimax move is found

### Performance Optimizations
- Alpha-beta pruning for efficient search
- Move ordering to improve pruning effectiveness
- Quiescence search to handle tactical positions
- Dynamic search depth based on game phase

## File Structure
```
ChessGame/
├── src/
│   ├── ChessBoard.java    # Main game class
│   └── Pieces.java        # Chess piece implementation
├── resources/
│   └── chess_pieces.png   # Piece sprites
├── run.bat               # Windows run script
├── run.sh                # Unix run script
└── README.md             # This file
```

## Future Enhancements

Potential improvements for future versions that I may do (anyone can do these if they want though lol):
- Opening book integration
- Time controls
- Save/load game functionality
- Multiple AI difficulty levels
- Move history and analysis
- Network multiplayer support


## Contributing

Feel free to contribute improvements, bug fixes, or new features. Please ensure all chess rules are correctly implemented and the AI maintains reasonable playing strength. 