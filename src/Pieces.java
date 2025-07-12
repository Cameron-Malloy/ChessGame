import javax.swing.*;
import java.util.LinkedList;

/**
 * Represents a chess piece and all chess logic for movement, capturing, special moves, and scoring.
 */
public class Pieces extends ChessBoard {
    // Board position (square coordinates)
    int xp;
    int yp;
    // Pixel position (for drawing)
    int x;
    int y;

    // Static: true if it's white's turn, false if black's
    public static boolean color;

    // Piece properties
    boolean isWhite;
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
     * @param xp x position (0-7)
     * @param yp y position (0-7)
     * @param isWhite true if white, false if black
     * @param n piece name ("pawn", "rook", etc.)
     * @param points point value for scoring
     * @param ps list of all pieces
     */
    public Pieces(int xp, int yp, boolean isWhite, String n, int points, LinkedList<Pieces> ps) {
        this.xp = xp;
        this.yp = yp;
        x = xp * 64;
        y = yp * 64;
        this.isWhite = isWhite;
        this.ps = ps;
        name = n;
        ps.add(this);
        color = true;
        winScreen = new JFrame();
        restart = new JButton();
        winText = new JLabel();
        winColor = new String();
        pointsWorth = points;
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
        if (name.equalsIgnoreCase("pawn") && ((isWhite && yp == 0) || (!isWhite && yp == 7))) {
            // Auto-promote to queen for AI (black pieces), show dialog for human (white pieces)
                if (!isWhite && ChessBoard.gameMode.equals("AI")) {
                // AI automatically promotes to queen
                this.name = "queen";
            } else {
                // Human player gets to choose
                String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                int choice = JOptionPane.showOptionDialog(null, "Choose promotion:", "Pawn Promotion",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (choice == 1) this.name = "rook";
                else if (choice == 2) this.name = "bishop";
                else if (choice == 3) this.name = "knight";
                else this.name = "queen";
            }
        }
        // Castling logic
        if (name.equalsIgnoreCase("king") && Math.abs(xp - this.xp) == 2) {
            // Kingside
            if (xp > this.xp) {
                for (Pieces p : ps) {
                    if (p.name.equalsIgnoreCase("rook") && p.isWhite == isWhite && p.xp == 7 && !p.hasMoved) {
                        p.xp = 5;
                        p.x = 5 * 64;
                        p.hasMoved = true;
                        break;
                    }
                }
            }
            // Queenside
            if (xp < this.xp) {
                for (Pieces p : ps) {
                    if (p.name.equalsIgnoreCase("rook") && p.isWhite == isWhite && p.xp == 0 && !p.hasMoved) {
                        p.xp = 3;
                        p.x = 3 * 64;
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
            if (Math.abs(yp - this.yp) == 2) {
                lastPawnDoubleMoveX = xp;
                lastPawnDoubleMoveY = yp;
                lastPawnDoubleMoveIsWhite = isWhite;
            } else {
                lastPawnDoubleMoveX = -1;
                lastPawnDoubleMoveY = -1;
            }
            // En passant capture
            if (Math.abs(xp - this.xp) == 1 && yp - this.yp == (isWhite ? -1 : 1) && ChessBoard.getPiece(xp * 64, yp * 64) == null) {
                // Remove the captured pawn
                Pieces captured = null;
                for (Pieces p : ps) {
                    if (p.name.equalsIgnoreCase("pawn") && p.xp == xp && p.yp == (isWhite ? yp + 1 : yp - 1) && p.isWhite != isWhite) {
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
            if (captured.isWhite) {
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
        this.xp = xp;
        this.yp = yp;
        x = xp * 64;
        y = yp * 64;
    }

    /**
     * Remove this piece from the board and update score.
     */
    public void kill() {
        // Store the score before removing the piece
        int scoreToAdd = this.pointsWorth;
        boolean isWhitePiece = this.isWhite;
        
        // Remove the piece from the list
        ps.remove(this);
        
        // Update the score
        if (isWhitePiece) {
            blackScore += scoreToAdd;
        } else {
            whiteScore += scoreToAdd;
        }
        
        // Update the display
        trackerScreen();
    }

    /**
     * Switch turn after a move.
     */
    public void lastColor() {
        color = !color;
    }

    /**
     * Chess movement logic for all pieces, including all rules and king-capture prevention.
     * @param toX destination x
     * @param toY destination y
     * @param ignoreKingBlock if true, allow moves that would capture the king (for check detection)
     * @return true if the move is legal
     */
    public boolean canMove(int toX, int toY, boolean ignoreKingBlock) {
        if (toX == xp && toY == yp) return false;
        if (toX < 0 || toX > 7 || toY < 0 || toY > 7) return false;
        Pieces dest = ChessBoard.getPiece(toX * 64, toY * 64);
        // Prevent capturing the king (unless for check detection)
        if (!ignoreKingBlock && dest != null && dest.name.equalsIgnoreCase("king")) return false;
        if (dest != null && dest.isWhite == this.isWhite) return false;
        int dx = toX - xp;
        int dy = toY - yp;
        switch (name.toLowerCase()) {
            case "pawn":
                int dir = isWhite ? -1 : 1;
                // Move forward
                if (dx == 0 && dy == dir && dest == null) return true;
                // Double move from start
                if (dx == 0 && dy == 2 * dir && dest == null && ChessBoard.getPiece(xp * 64, (yp + dir) * 64) == null && ((isWhite && yp == 6) || (!isWhite && yp == 1))) return true;
                // Capture
                if (Math.abs(dx) == 1 && dy == dir && dest != null && dest.isWhite != isWhite) return true;
                // En passant
                if (Math.abs(dx) == 1 && dy == dir && dest == null &&
                    lastPawnDoubleMoveX == toX && lastPawnDoubleMoveY == (isWhite ? toY + 1 : toY - 1) && lastPawnDoubleMoveIsWhite != isWhite) return true;
                return false;
            case "rook":
                if (dx != 0 && dy != 0) return false;
                int stepX = Integer.signum(dx);
                int stepY = Integer.signum(dy);
                int x = xp + stepX, y = yp + stepY;
                while (x != toX || y != toY) {
                    if (ChessBoard.getPiece(x * 64, y * 64) != null) return false;
                    x += stepX;
                    y += stepY;
                }
                return true;
            case "knight":
                return (Math.abs(dx) == 2 && Math.abs(dy) == 1) || (Math.abs(dx) == 1 && Math.abs(dy) == 2);
            case "bishop":
                if (Math.abs(dx) != Math.abs(dy)) return false;
                int stepx = Integer.signum(dx);
                int stepy = Integer.signum(dy);
                x = xp + stepx; y = yp + stepy;
                while (x != toX && y != toY) {
                    if (ChessBoard.getPiece(x * 64, y * 64) != null) return false;
                    x += stepx;
                    y += stepy;
                }
                return true;
            case "queen":
                if (dx == 0 || dy == 0) {
                    // Rook-like move
                    stepX = Integer.signum(dx);
                    stepY = Integer.signum(dy);
                    x = xp + stepX; y = yp + stepY;
                    while (x != toX || y != toY) {
                        if (ChessBoard.getPiece(x * 64, y * 64) != null) return false;
                        x += stepX;
                        y += stepY;
                    }
                    return true;
                } else if (Math.abs(dx) == Math.abs(dy)) {
                    // Bishop-like move
                    stepx = Integer.signum(dx);
                    stepy = Integer.signum(dy);
                    x = xp + stepx; y = yp + stepy;
                    while (x != toX && y != toY) {
                        if (ChessBoard.getPiece(x * 64, y * 64) != null) return false;
                        x += stepx;
                        y += stepy;
                    }
                    return true;
                }
                return false;
            case "king":
                // Castling
                if (!hasMoved && dy == 0 && Math.abs(dx) == 2) {
                    // Kingside
                    if (dx == 2) {
                        Pieces rook = null;
                        for (Pieces p : ps) {
                            if (p.name.equalsIgnoreCase("rook") && p.isWhite == isWhite && p.xp == 7 && !p.hasMoved) rook = p;
                        }
                        if (rook == null) return false;
                        // Squares between king and rook must be empty
                        for (int xx = xp + 1; xx < 7; xx++) {
                            if (ChessBoard.getPiece(xx * 64, yp * 64) != null) return false;
                        }
                        // King must not be in check, pass through check, or end in check
                        for (int xx = xp; xx <= xp + 2; xx++) {
                            int oldX = this.xp, oldx = this.x;
                            this.xp = xx; this.x = xx * 64;
                            boolean inCheck = ChessBoard.isKingInCheck(isWhite);
                            this.xp = oldX; this.x = oldx;
                            if (inCheck) return false;
                        }
                        return true;
                    }
                    // Queenside
                    if (dx == -2) {
                        Pieces rook = null;
                        for (Pieces p : ps) {
                            if (p.name.equalsIgnoreCase("rook") && p.isWhite == isWhite && p.xp == 0 && !p.hasMoved) rook = p;
                        }
                        if (rook == null) return false;
                        // Squares between king and rook must be empty
                        for (int xx = xp - 1; xx > 0; xx--) {
                            if (ChessBoard.getPiece(xx * 64, yp * 64) != null) return false;
                        }
                        // King must not be in check, pass through check, or end in check
                        for (int xx = xp; xx >= xp - 2; xx--) {
                            int oldX = this.xp, oldx = this.x;
                            this.xp = xx; this.x = xx * 64;
                            boolean inCheck = ChessBoard.isKingInCheck(isWhite);
                            this.xp = oldX; this.x = oldx;
                            if (inCheck) return false;
                        }
                        return true;
                    }
                }
                // Normal king move (one square in any direction)
                return Math.abs(dx) <= 1 && Math.abs(dy) <= 1;
        }
        return false;
    }

    // For backward compatibility, keep the old canMove signature
    public boolean canMove(int toX, int toY) {
        return canMove(toX, toY, false);
    }
}
