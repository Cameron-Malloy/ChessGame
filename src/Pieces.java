import javax.swing.*;
import java.util.LinkedList;

/**
 * Represents a chess piece and all chess logic for movement, capturing, special moves, and scoring.
 */
public class Pieces extends ChessBoard {
    // Board position (square coordinates)
    int xBoardPosition;
    int yBoardPosition;
    // Pixel position (for drawing)
    int xPixelPosition;
    int yPixelPosition;

    // Static: true if it's white's turn, false if black's
    public static boolean isWhiteTurn;

    // Piece properties
    boolean isWhitePiece;
    LinkedList<Pieces> ps;
    String name;
    JFrame winScreen;
    JButton restart;
    JLabel winText;
    String winColor;
    int pointsWorth;
    public static int blackScore;
    public static int whiteScore;

    // For en passant
    public static int lastPawnDoubleMoveX = -1;
    public static int lastPawnDoubleMoveY = -1;
    public static boolean lastPawnDoubleMoveIsWhite = false;

    // For castling
    public boolean hasMoved = false;

    /**
     * Constructor for a chess piece.
     * @param xBoardPosition x position (0-7)
     * @param yBoardPosition y position (0-7)
     * @param isWhitePiece true if white, false if black
     * @param name piece name ("pawn", "rook", etc.)
     * @param points point value for scoring
     * @param piecesLinkedList list of all pieces
     */
    public Pieces(int xBoardPosition, int yBoardPosition, boolean isWhitePiece, String name, int points, LinkedList<Pieces> piecesLinkedList) {
        this.xBoardPosition = xBoardPosition;
        xPixelPosition = xBoardPosition * 64;

        this.yBoardPosition = yBoardPosition;
        yPixelPosition = yBoardPosition * 64;

        this.isWhitePiece = isWhitePiece;

        this.name = name;
        this.pointsWorth = points;

        this.ps = piecesLinkedList;
        piecesLinkedList.add(this);

        isWhiteTurn = true;

        winScreen = new JFrame();
        restart = new JButton();
        winText = new JLabel();
        winColor = "";

        blackScore = 0;
        whiteScore = 0;
    }

    /**
     * Move this piece to (xp, yp), handling all chess rules (promotion, en passant, castling, scoring).
     * @param xp destination x
     * @param yp destination y
     */
    public void move(int xp, int yp) {
        // Pawn promotion
        if (name.equalsIgnoreCase("pawn") && ((isWhitePiece && yp == 0) || (!isWhitePiece && yp == 7))) {
            // Show promotion dialog
            String[] options = {"Queen", "Rook", "Bishop", "Knight"};
            int choice = JOptionPane.showOptionDialog(null, "Choose promotion:", "Pawn Promotion",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == 1) this.name = "rook";
            else if (choice == 2) this.name = "bishop";
            else if (choice == 3) this.name = "knight";
            else this.name = "queen";
        }
        // Castling logic
        if (name.equalsIgnoreCase("king") && Math.abs(xp - this.xBoardPosition) == 2) {
            // Kingside
            if (xp > this.xBoardPosition) {
                for (Pieces p : ps) {
                    if (p.name.equalsIgnoreCase("rook") && p.isWhitePiece == isWhitePiece && p.xBoardPosition == 7 && !p.hasMoved) {
                        p.xBoardPosition = 5;
                        p.xPixelPosition = 5 * 64;
                        p.hasMoved = true;
                        break;
                    }
                }
            }
            // Queenside
            if (xp < this.xBoardPosition) {
                for (Pieces p : ps) {
                    if (p.name.equalsIgnoreCase("rook") && p.isWhitePiece == isWhitePiece && p.xBoardPosition == 0 && !p.hasMoved) {
                        p.xBoardPosition = 3;
                        p.xPixelPosition = 3 * 64;
                        p.hasMoved = true;
                        break;
                    }
                }
            }
        }
        hasMoved = true;

        // En passant logic
        if (name.equalsIgnoreCase("pawn")) {
            // Double move
            if (Math.abs(yp - this.yBoardPosition) == 2) {
                lastPawnDoubleMoveX = xp;
                lastPawnDoubleMoveY = yp;
                lastPawnDoubleMoveIsWhite = isWhitePiece;
            } else {
                lastPawnDoubleMoveX = -1;
                lastPawnDoubleMoveY = -1;
            }
            // En passant capture
            if (Math.abs(xp - this.xBoardPosition) == 1 && yp - this.yBoardPosition == (isWhitePiece ? -1 : 1) && ChessBoard.getPiece(xp * 64, yp * 64) == null) {
                // Remove the captured pawn
                Pieces captured = null;
                for (Pieces p : ps) {
                    if (p.name.equalsIgnoreCase("pawn") && p.xBoardPosition == xp && p.yBoardPosition == (isWhitePiece ? yp + 1 : yp - 1) && p.isWhitePiece != isWhitePiece) {
                        captured = p;
                        break;
                    }
                }
                if (captured != null) captured.kill();
            }
        } else {
            lastPawnDoubleMoveX = -1;
            lastPawnDoubleMoveY = -1;
        }

        // Remove captured piece (except king, which cannot be captured)
        Pieces captured = ChessBoard.getPiece(xp * 64, yp * 64);
        if (captured != null && !captured.name.equalsIgnoreCase("king")) {
            if (captured.isWhitePiece) {
                blackScore += captured.pointsWorth;
            } else {
                whiteScore += captured.pointsWorth;
            }
            captured.kill();
        } else if (captured != null && captured.name.equalsIgnoreCase("king")) {
            // Should never happen due to canMove check, but just in case
            return;
        }

        // Switch turn
        lastColor();

        // Update position
        this.xBoardPosition = xp;
        this.yBoardPosition = yp;
        xPixelPosition = xp * 64;
        yPixelPosition = yp * 64;
    }

    /**
     * Remove this piece from the board and update score.
     */
    public void kill() {
        if (ChessBoard.getPiece(xBoardPosition * 64, yBoardPosition * 64).isWhitePiece) {
            blackScore += Pieces.getPiece(xBoardPosition * 64, yBoardPosition * 64).pointsWorth;
        } else {
            whiteScore += Pieces.getPiece(xBoardPosition * 64, yBoardPosition * 64).pointsWorth;
        }
        trackerScreen();
        size -= 1;
        ps.remove(this);
    }

    /**
     * Switch turn after a move.
     */
    public void lastColor() {
        isWhiteTurn = !isWhiteTurn;
    }

    /**
     * Chess movement logic for all pieces, including all rules and king-capture prevention.
     * @param xDest destination x
     * @param yDest destination y
     * @param ignoreKingBlock if true, allow moves that would capture the king (for check detection)
     * @return true if the move is legal
     */
    public boolean canMove(int xDest, int yDest, boolean ignoreKingBlock) {
        // Move must be nonzero
        if (xDest == xBoardPosition && yDest == yBoardPosition) return false;
        // Move must be on board
        if (xDest < 0 || xDest > 7 || yDest < 0 || yDest > 7) return false;

        Pieces destPiece = ChessBoard.getPiece(xDest * 64, yDest * 64);
        // Prevent capturing the king (unless for check detection)
        if (!ignoreKingBlock && destPiece != null && destPiece.name.equalsIgnoreCase("king")) return false;
        if (destPiece != null && destPiece.isWhitePiece == this.isWhitePiece) return false;
        int xDiff = xDest - xBoardPosition;
        int yDiff = yDest - yBoardPosition;
        switch (name.toLowerCase()) {
            case "pawn":
                int dir = isWhitePiece ? -1 : 1;
                // Move forward
                if (xDiff == 0 && yDiff == dir && destPiece == null) return true;
                // Double move from start
                if (xDiff == 0 && yDiff == 2 * dir && destPiece == null && ChessBoard.getPiece(xBoardPosition * 64, (yBoardPosition + dir) * 64) == null && ((isWhitePiece && yBoardPosition == 6) || (!isWhitePiece && yBoardPosition == 1))) return true;
                // Capture
                if (Math.abs(xDiff) == 1 && yDiff == dir && destPiece != null && destPiece.isWhitePiece != isWhitePiece) return true;
                // En passant
                if (Math.abs(xDiff) == 1 && yDiff == dir && destPiece == null &&
                    lastPawnDoubleMoveX == xDest && lastPawnDoubleMoveY == (isWhitePiece ? yDest + 1 : yDest - 1) && lastPawnDoubleMoveIsWhite != isWhitePiece) return true;
                return false;
            case "rook":
                int xStepParallel = Integer.signum(xDiff);
                int yStepParallel = Integer.signum(yDiff);
                int xInc = xBoardPosition + xStepParallel, yInc = yBoardPosition + yStepParallel;
                while (xInc != xDest || yInc != yDest) {
                    if (ChessBoard.getPiece(xInc * 64, yInc * 64) != null) return false;
                    xInc += xStepParallel;
                    yInc += yStepParallel;
                }
                return true;
            case "knight":
                return (Math.abs(xDiff) == 2 && Math.abs(yDiff) == 1) || (Math.abs(xDiff) == 1 && Math.abs(yDiff) == 2);
            case "bishop":
                if (Math.abs(xDiff) != Math.abs(yDiff)) return false;
                int xStepDiagonal = Integer.signum(xDiff);
                int yStepDiagonal = Integer.signum(yDiff);
                xInc = xBoardPosition + xStepDiagonal; yInc = yBoardPosition + yStepDiagonal;
                while (xInc != xDest && yInc != yDest) {
                    if (ChessBoard.getPiece(xInc * 64, yInc * 64) != null) return false;
                    xInc += xStepDiagonal;
                    yInc += yStepDiagonal;
                }
                return true;
            case "queen":
                if (xDiff == 0 || yDiff == 0) {
                    // Rook-like move
                    xStepParallel = Integer.signum(xDiff);
                    yStepParallel = Integer.signum(yDiff);
                    xInc = xBoardPosition + xStepParallel; yInc = yBoardPosition + yStepParallel;
                    while (xInc != xDest || yInc != yDest) {
                        if (ChessBoard.getPiece(xInc * 64, yInc * 64) != null) return false;
                        xInc += xStepParallel;
                        yInc += yStepParallel;
                    }
                    return true;
                } else if (Math.abs(xDiff) == Math.abs(yDiff)) {
                    // Bishop-like move
                    xStepDiagonal = Integer.signum(xDiff);
                    yStepDiagonal = Integer.signum(yDiff);
                    xInc = xBoardPosition + xStepDiagonal; yInc = yBoardPosition + yStepDiagonal;
                    while (xInc != xDest && yInc != yDest) {
                        if (ChessBoard.getPiece(xInc * 64, yInc * 64) != null) return false;
                        xInc += xStepDiagonal;
                        yInc += yStepDiagonal;
                    }
                    return true;
                }
                return false;
            case "king":
                // Castling
                if (!hasMoved && yDiff == 0 && Math.abs(xDiff) == 2) {
                    // Kingside
                    if (xDiff == 2) {
                        // Find the rook, must not have moved
                        Pieces rook = null;
                        for (Pieces p : ps) {
                            if (p.name.equalsIgnoreCase("rook") && p.isWhitePiece == isWhitePiece && p.xBoardPosition == 7 && !p.hasMoved) rook = p;
                        }
                        if (rook == null) return false;

                        // Squares between king and rook must be empty
                        for (int xIncrement = xBoardPosition + 1; xIncrement < 7; xIncrement++) {
                            if (ChessBoard.getPiece(xIncrement * 64, yBoardPosition * 64) != null) return false;
                        }
                        // King must not be in check, pass through check, or end in check
                        for (int xIncrement = xBoardPosition; xIncrement <= xBoardPosition + 2; xIncrement++) {
                            int xBoardOriginal = this.xBoardPosition, xPixelOriginal = this.xPixelPosition;
                            this.xBoardPosition = xIncrement; this.xPixelPosition = xIncrement * 64;
                            boolean inCheck = ChessBoard.isKingInCheck(isWhitePiece);
                            this.xBoardPosition = xBoardOriginal; this.xPixelPosition = xPixelOriginal;
                            if (inCheck) return false;
                        }
                        return true;
                    }
                    // Queenside
                    if (xDiff == -2) {
                        Pieces rook = null;
                        for (Pieces p : ps) {
                            if (p.name.equalsIgnoreCase("rook") && p.isWhitePiece == isWhitePiece && p.xBoardPosition == 0 && !p.hasMoved) rook = p;
                        }
                        if (rook == null) return false;
                        // Squares between king and rook must be empty
                        for (int xx = xBoardPosition - 1; xx > 0; xx--) {
                            if (ChessBoard.getPiece(xx * 64, yBoardPosition * 64) != null) return false;
                        }
                        // King must not be in check, pass through check, or end in check
                        for (int xIncrement = xBoardPosition; xIncrement >= xBoardPosition - 2; xIncrement--) {
                            int xBoardOriginal = this.xBoardPosition, xPixelOriginal = this.xPixelPosition;
                            this.xBoardPosition = xIncrement; this.xPixelPosition = xIncrement * 64;
                            boolean inCheck = ChessBoard.isKingInCheck(isWhitePiece);
                            this.xBoardPosition = xBoardOriginal; this.xPixelPosition = xPixelOriginal;
                            if (inCheck) return false;
                        }
                        return true;
                    }
                }
                // Normal king move (one square in any direction)
                return Math.abs(xDiff) <= 1 && Math.abs(yDiff) <= 1;
        }
        return false;
    }

    // For backward compatibility, keep the old canMove signature
    public boolean canMove(int xDest, int yDest) {
        return canMove(xDest, yDest, false);
    }
}
