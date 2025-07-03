# Chess Game

A Java-based chess game with a graphical user interface.

## Setup Instructions

### Prerequisites
- Java JDK 8 or higher

### Getting Started

The chess piece images are already included in the repository! 
```

3. **Compile and run:**
   ```bash
   # Compile the Java files
   javac -d out src/*.java
   
   # Run the game
   java -cp out ChessBoard
   ```

## Game Controls

- **Start Game:** Click the "Start" button in the initial window
- **Move Pieces:** Click and drag pieces to move them
- **Quit Game:** Press Alt + F4

## Chess Piece Image Requirements

The chess piece image should be a sprite sheet with the following layout:
- 6 columns × 2 rows = 12 pieces total
- Each piece is 200×200 pixels in the source image
- The pieces should be arranged as follows:
  - Row 1: White pieces (King, Queen, Bishop, Knight, Rook, Pawn)
  - Row 2: Black pieces (King, Queen, Bishop, Knight, Rook, Pawn)

## Troubleshooting

If you get a "File not found" error, make sure:
1. You're running the game from the project root directory
2. The `resources/chess_pieces.png` file is present (it should be included in the repository)

## Project Structure

- `src/ChessBoard.java` - Main game logic and GUI
- `src/Pieces.java` - Chess piece class and movement rules
- `resources/chess_pieces.png` - Chess piece images (included)
- `out/` - Compiled Java classes (created when you compile) 