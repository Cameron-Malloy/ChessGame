import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.CardLayout;

public class ChessBoard extends JPanel {
    public static LinkedList<Pieces> ps = new LinkedList<>();
    public static Pieces selectedPiece=null;
    public static int size = 32;
    public static int screenWidth = 1920; // Default fallback
    public static int screenHeight = 1080; // Default fallback
    public static JFrame mainFrame = null;
    public static JPanel mainPanel = null;
    public static CardLayout cardLayout = null;
    public static JPanel startPanel = null;
    public static JPanel gamePanel = null;
    public static JPanel endPanel = null;
    public static JLabel blackScoreLabel = null;
    public static JLabel whiteScoreLabel = null;
    public static JLabel turnLabel = null;
    public static JLabel endText = null;
    public static JButton restartButton = null;
    public static java.util.List<Point> legalMoves = new java.util.ArrayList<>();
    public static boolean gameOver = false;
    public static boolean stalemate = false;
    public static String winnerColor = null;

    public static void main(String[] args) throws IOException, AWTException {
        run();
    }
    
    public static void run() throws AWTException, IOException {
        // Reset game state flags
        stalemate = false;
        gameOver = false;
        ps.clear();
        size = 32;
        // Get screen dimensions for dynamic positioning
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();

        // Main frame and card layout
        mainFrame = new JFrame();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setUndecorated(true);
        mainFrame.setTitle("The Best Chess Board");
        int chessWindowSize = 512;
        int chessX = (screenWidth - chessWindowSize) / 2;
        int chessY = (screenHeight - chessWindowSize) / 2;
        mainFrame.setBounds(chessX, chessY, chessWindowSize, chessWindowSize + 110);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // Initialize game pieces
        Robot bot = new Robot();
        int ind = 0;
        BufferedImage all = ImageIO.read(new File("resources/chess_pieces.png"));
        Image[] imgs = new Image[12];

        for (int y = 0; y < 400; y += 200) {
            for (int x = 0; x < 1200; x += 200) {
                imgs[ind] = all.getSubimage(x, y, 200, 200).getScaledInstance(64, 64, BufferedImage.SCALE_SMOOTH);
                ind++;
            }
        }
        
        // Create pieces
        Pieces bRook = new Pieces(0, 0, false, "rook",5, ps);
        Pieces bKnight = new Pieces(1, 0, false, "knight",3, ps);
        Pieces bBishop = new Pieces(2, 0, false, "bishop",3, ps);
        Pieces bQueen = new Pieces(3, 0, false, "queen",9, ps);
        Pieces bKing = new Pieces(4, 0, false, "king",0,  ps);
        Pieces bBishop2 = new Pieces(5, 0, false, "bishop", 3, ps);
        Pieces bKnight2 = new Pieces(6, 0, false, "knight", 3, ps);
        Pieces bRook2 = new Pieces(7, 0, false, "rook",5, ps);
        Pieces bPawn1 = new Pieces(1, 1, false, "pawn",1, ps);
        Pieces bPawn2 = new Pieces(2, 1, false, "pawn",1, ps);
        Pieces bPawn3 = new Pieces(3, 1, false, "pawn", 1, ps);
        Pieces bPawn4 = new Pieces(4, 1, false, "pawn",1, ps);
        Pieces bPawn5 = new Pieces(5, 1, false, "pawn",1, ps);
        Pieces bPawn6 = new Pieces(6, 1, false, "pawn",1, ps);
        Pieces bPawn7 = new Pieces(7, 1, false, "pawn",1, ps);
        Pieces bPawn8 = new Pieces(0, 1, false, "pawn",1, ps);

        Pieces wRook = new Pieces(0, 7, true, "rook",5, ps);
        Pieces wKnight = new Pieces(1, 7, true, "knight",3, ps);
        Pieces wBishop = new Pieces(2, 7, true, "bishop",3, ps);
        Pieces wQueen = new Pieces(3, 7, true, "queen",9, ps);
        Pieces wKing = new Pieces(4, 7, true, "king",0, ps);
        Pieces wBishop2 = new Pieces(5, 7, true, "bishop",3, ps);
        Pieces wKnight2 = new Pieces(6, 7, true, "knight",3, ps);
        Pieces wRook2 = new Pieces(7, 7, true, "rook",5, ps);
        Pieces wPawn1 = new Pieces(1, 6, true, "pawn",1, ps);
        Pieces wPawn2 = new Pieces(2, 6, true, "pawn",1, ps);
        Pieces wPawn3 = new Pieces(3, 6, true, "pawn",1, ps);
        Pieces wPawn4 = new Pieces(4, 6, true, "pawn",1, ps);
        Pieces wPawn5 = new Pieces(5, 6, true, "pawn",1, ps);
        Pieces wPawn6 = new Pieces(6, 6, true, "pawn",1, ps);
        Pieces wPawn7 = new Pieces(7, 6, true, "pawn",1, ps);
        Pieces wPawn8 = new Pieces(0, 6, true, "pawn",1, ps);

        // Start panel
        startPanel = new JPanel(null);
        startPanel.setBackground(new Color(30, 30, 30));
        JLabel label = new JLabel("ChessAI - Start Game", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.CYAN);
        label.setBounds(56, 60, 400, 60);
        startPanel.add(label);
        JLabel instructions = new JLabel("Press Start to play. Press Close to exit.", JLabel.CENTER);
        instructions.setFont(new Font("Arial", Font.PLAIN, 16));
        instructions.setForeground(Color.LIGHT_GRAY);
        instructions.setBounds(56, 130, 400, 30);
        startPanel.add(instructions);
        JButton button = new JButton("Start");
        button.setBounds(156, 200, 200, 60);
        button.setFont(new Font("Arial", Font.BOLD, 22));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 100, 150));
        startPanel.add(button);
        JButton startCloseButton = new JButton("Close");
        startCloseButton.setBounds(156, 280, 200, 40);
        startCloseButton.setFont(new Font("Arial", Font.BOLD, 18));
        startCloseButton.setFocusable(false);
        startCloseButton.setBorder(BorderFactory.createRaisedBevelBorder());
        startCloseButton.setForeground(Color.WHITE);
        startCloseButton.setBackground(new Color(150, 0, 0));
        startCloseButton.addActionListener(e -> System.exit(0));
        startPanel.add(startCloseButton);
        mainPanel.add(startPanel, "start");

        // Game panel
        gamePanel = new JPanel(null);
        gamePanel.setBackground(Color.GRAY);
        // Tracker labels
        blackScoreLabel = new JLabel("Black's Score: 0", JLabel.CENTER);
        blackScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        blackScoreLabel.setForeground(Color.WHITE);
        blackScoreLabel.setOpaque(true);
        blackScoreLabel.setBackground(Color.BLACK);
        blackScoreLabel.setBounds(0, 0, chessWindowSize, 30);
        gamePanel.add(blackScoreLabel);
        whiteScoreLabel = new JLabel("White's Score: 0", JLabel.CENTER);
        whiteScoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        whiteScoreLabel.setForeground(Color.WHITE);
        whiteScoreLabel.setOpaque(true);
        whiteScoreLabel.setBackground(Color.BLACK);
        whiteScoreLabel.setBounds(0, chessWindowSize + 30, chessWindowSize, 30);
        gamePanel.add(whiteScoreLabel);
        
        // Turn indicator
        turnLabel = new JLabel("White's Turn", JLabel.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setOpaque(true);
        turnLabel.setBackground(Color.BLUE);
        turnLabel.setBounds(0, chessWindowSize + 60, chessWindowSize, 30);
        gamePanel.add(turnLabel);
        
        // Chess board panel
        JPanel pn = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                boolean white = true;
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (white) {
                            g.setColor(new Color(111, 111, 111));
                        } else {
                            g.setColor(new Color(222, 222, 222));
                        }
                        g.fillRect(x * 64, y * 64, 64, 64);
                        white = !white;
                    }
                    white = !white;
                }
                // Draw legal move highlights
                for (Point p : ChessBoard.legalMoves) {
                    Pieces dest = getPiece(p.x * 64, p.y * 64);
                    if (dest != null && selectedPiece != null && dest.isWhitePiece != selectedPiece.isWhitePiece) {
                        // Draw red circle above the piece for capture
                        g.setColor(new Color(220, 30, 30, 200));
                        g.fillOval(p.x * 64 + 20, p.y * 64 + 4, 24, 24);
                        g.setColor(Color.BLACK);
                        g.drawOval(p.x * 64 + 20, p.y * 64 + 4, 24, 24);
                    } else {
                        // Draw green dot for normal move
                        g.setColor(new Color(0, 200, 0, 120));
                        g.fillOval(p.x * 64 + 20, p.y * 64 + 20, 24, 24);
                    }
                }
                // Highlight king in check
                for (boolean color : new boolean[]{true, false}) {
                    if (isKingInCheck(color)) {
                        Pieces king = getKing(color);
                        if (king != null) {
                            g.setColor(new Color(255, 0, 0, 120));
                            g.fillRect(king.xBoardPosition * 64, king.yBoardPosition * 64, 64, 64);
                        }
                    }
                }
                for (Pieces p : ps) {
                    int ind = 0;
                    if (p.name.equalsIgnoreCase("queen")) {
                        ind = 1;
                    }
                    if (p.name.equalsIgnoreCase("bishop")) {
                        ind = 2;
                    }
                    if (p.name.equalsIgnoreCase("knight")) {
                        ind = 3;
                    }
                    if (p.name.equalsIgnoreCase("rook")) {
                        ind = 4;
                    }
                    if (p.name.equalsIgnoreCase("pawn")) {
                        ind = 5;
                    }
                    if (!p.isWhitePiece) {
                        ind += 6;
                    }
                    g.drawImage(imgs[ind], p.xPixelPosition, p.yPixelPosition, this);
                }
            }
        };
        pn.setBounds(0, 30, chessWindowSize, chessWindowSize);
        pn.setFocusable(true);
        gamePanel.add(pn);

        // Close button for game panel (add after pn)
        JButton gameCloseButton = new JButton("Close");
        gameCloseButton.setBounds(chessWindowSize - 110, 0, 100, 30);
        gameCloseButton.setFont(new Font("Arial", Font.BOLD, 16));
        gameCloseButton.setFocusable(false);
        gameCloseButton.setBorder(BorderFactory.createRaisedBevelBorder());
        gameCloseButton.setForeground(Color.WHITE);
        gameCloseButton.setBackground(new Color(150, 0, 0));
        gameCloseButton.addActionListener(e -> System.exit(0));
        gamePanel.add(gameCloseButton);
        gamePanel.setComponentZOrder(gameCloseButton, 0); // Bring to front

        // Now add gamePanel to mainPanel
        mainPanel.add(gamePanel, "game");

        // End panel
        endPanel = new JPanel(null);
        endPanel.setBackground(new Color(30, 30, 30));
        JLabel endTitle = new JLabel("Game Over", JLabel.CENTER);
        endTitle.setFont(new Font("Arial", Font.BOLD, 36));
        endTitle.setForeground(Color.CYAN);
        endTitle.setBounds(56, 40, 400, 60);
        endPanel.add(endTitle);
        endText = new JLabel("", JLabel.CENTER);
        endText.setFont(new Font("Arial", Font.BOLD, 22));
        endText.setForeground(Color.LIGHT_GRAY);
        endText.setBounds(56, 120, 400, 60);
        endPanel.add(endText);
        restartButton = new JButton("Restart");
        restartButton.setBounds(156, 220, 200, 60);
        restartButton.setFont(new Font("Arial", Font.BOLD, 22));
        restartButton.setFocusable(false);
        restartButton.setBorder(BorderFactory.createRaisedBevelBorder());
        restartButton.setForeground(Color.WHITE);
        restartButton.setBackground(new Color(0, 100, 150));
        endPanel.add(restartButton);
        JButton endCloseButton = new JButton("Close");
        endCloseButton.setBounds(156, 300, 200, 40);
        endCloseButton.setFont(new Font("Arial", Font.BOLD, 18));
        endCloseButton.setFocusable(false);
        endCloseButton.setBorder(BorderFactory.createRaisedBevelBorder());
        endCloseButton.setForeground(Color.WHITE);
        endCloseButton.setBackground(new Color(150, 0, 0));
        endCloseButton.addActionListener(e -> System.exit(0));
        endPanel.add(endCloseButton);
        mainPanel.add(endPanel, "end");

        // Button actions
        button.addActionListener(e -> {
            cardLayout.show(mainPanel, "game");
            pn.requestFocusInWindow();
        });
        restartButton.addActionListener(e -> {
            // Reset game state
            mainFrame.dispose();
            try { run(); } catch (Exception ex) { throw new RuntimeException(ex); }
        });

        // Mouse listener for click-to-move (add to pn, not gamePanel)
        pn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // No logic here, handled in mousePressed for reliability
            }
            @Override
            public void mousePressed(MouseEvent e) {
                pn.requestFocusInWindow();
                if (gameOver) return;
                int clickedX = e.getX() / 64;
                int clickedY = e.getY() / 64;
                if (clickedY < 0 || clickedY > 7 || clickedX < 0 || clickedX > 7) {
                    // Clicked outside the board
                    selectedPiece = null;
                    ChessBoard.legalMoves.clear();
                    pn.repaint();
                    gamePanel.setComponentZOrder(gameCloseButton, 0);
                    gamePanel.repaint();
                    return;
                }
                if (selectedPiece == null) {
                    Pieces p = getPiece(clickedX * 64, clickedY * 64);
                    if (p != null && Pieces.isWhiteTurn == p.isWhitePiece) {
                        selectedPiece = p;
                        ChessBoard.legalMoves.clear();
                        // Show legal moves
                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                if (selectedPiece.canMove(x, y)) {
                                    // Simulate the move
                                    int oldX = selectedPiece.xBoardPosition, oldY = selectedPiece.yBoardPosition, oldx = selectedPiece.xPixelPosition, oldy = selectedPiece.yPixelPosition;
                                    Pieces captured = getPiece(x * 64, y * 64);
                                    int capturedIndex = -1;
                                    if (captured != null) capturedIndex = ps.indexOf(captured);
                                    selectedPiece.xBoardPosition = x; selectedPiece.yBoardPosition = y; selectedPiece.xPixelPosition = x * 64; selectedPiece.yPixelPosition = y * 64;
                                    if (captured != null) ps.remove(captured);
                                    boolean leavesKingInCheck = isKingInCheck(selectedPiece.isWhitePiece);
                                    // Undo
                                    selectedPiece.xBoardPosition = oldX; selectedPiece.yBoardPosition = oldY; selectedPiece.xPixelPosition = oldx; selectedPiece.yPixelPosition = oldy;
                                    if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                                    if (!leavesKingInCheck) {
                                        ChessBoard.legalMoves.add(new Point(x, y));
                                    }
                                }
                            }
                        }
                    } else {
                        selectedPiece = null;
                        ChessBoard.legalMoves.clear();
                    }
                    pn.repaint();
                    gamePanel.setComponentZOrder(gameCloseButton, 0);
                    gamePanel.repaint();
                } else {
                    // Try to move to clicked square
                    if (selectedPiece.canMove(clickedX, clickedY)) {
                        // Simulate the move
                        int oldX = selectedPiece.xBoardPosition, oldY = selectedPiece.yBoardPosition, oldx = selectedPiece.xPixelPosition, oldy = selectedPiece.yPixelPosition;
                        Pieces captured = getPiece(clickedX * 64, clickedY * 64);
                        int capturedIndex = -1;
                        if (captured != null) capturedIndex = ps.indexOf(captured);
                        selectedPiece.xBoardPosition = clickedX; selectedPiece.yBoardPosition = clickedY; selectedPiece.xPixelPosition = clickedX * 64; selectedPiece.yPixelPosition = clickedY * 64;
                        if (captured != null) ps.remove(captured);
                        boolean leavesKingInCheck = isKingInCheck(selectedPiece.isWhitePiece);
                        // Undo
                        selectedPiece.xBoardPosition = oldX; selectedPiece.yBoardPosition = oldY; selectedPiece.xPixelPosition = oldx; selectedPiece.yPixelPosition = oldy;
                        if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                        if (!leavesKingInCheck) {
                            selectedPiece.move(clickedX, clickedY);
                            trackerScreen();
                            // After move, check for checkmate or king capture
                            boolean opponentIsWhite = !selectedPiece.isWhitePiece;
                            System.out.println("Checkmate? " + isCheckmate(opponentIsWhite) + " for " + (opponentIsWhite ? "White" : "Black"));
                            if (isCheckmate(opponentIsWhite)) {
                                winnerColor = selectedPiece.isWhitePiece ? "White" : "Black";
                                gameOver = true;
                                showEndScreen();
                            } else if (isStalemate(opponentIsWhite)) {
                                stalemate = true;
                                gameOver = true;
                                showEndScreen();
                            }
                            // If either king is missing (captured), show end screen
                            if (getKing(true) == null || getKing(false) == null) {
                                gameOver = true;
                                showEndScreen();
                            }
                            selectedPiece = null;
                            ChessBoard.legalMoves.clear();
                        } else {
                            // Illegal move, keep selection
                        }
                    } else {
                        // If clicked another of your pieces, reselect
                        Pieces p = getPiece(clickedX * 64, clickedY * 64);
                        if (p != null && Pieces.isWhiteTurn == p.isWhitePiece) {
                            selectedPiece = p;
                            ChessBoard.legalMoves.clear();
                            // Show legal moves for new selection
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; y++) {
                                    if (selectedPiece.canMove(x, y)) {
                                        // Simulate the move
                                        int oldX = selectedPiece.xBoardPosition, oldY = selectedPiece.yBoardPosition, oldx = selectedPiece.xPixelPosition, oldy = selectedPiece.yPixelPosition;
                                        Pieces captured = getPiece(x * 64, y * 64);
                                        int capturedIndex = -1;
                                        if (captured != null) capturedIndex = ps.indexOf(captured);
                                        selectedPiece.xBoardPosition = x; selectedPiece.yBoardPosition = y; selectedPiece.xPixelPosition = x * 64; selectedPiece.yPixelPosition = y * 64;
                                        if (captured != null) ps.remove(captured);
                                        boolean leavesKingInCheck = isKingInCheck(selectedPiece.isWhitePiece);
                                        // Undo
                                        selectedPiece.xBoardPosition = oldX; selectedPiece.yBoardPosition = oldY; selectedPiece.xPixelPosition = oldx; selectedPiece.yPixelPosition = oldy;
                                        if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                                        if (!leavesKingInCheck) {
                                            ChessBoard.legalMoves.add(new Point(x, y));
                                        }
                                    }
                                }
                            }
                        } else {
                            // Deselect
                            selectedPiece = null;
                            ChessBoard.legalMoves.clear();
                        }
                    }
                    pn.repaint();
                    gamePanel.setComponentZOrder(gameCloseButton, 0);
                    gamePanel.repaint();
                }
            }
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

        mainFrame.setVisible(true);
        cardLayout.show(mainPanel, "start");
    }

    // Update trackerScreen to update labels instead of opening new windows
    public static void trackerScreen(){
        if (blackScoreLabel != null) blackScoreLabel.setText("Black's Score: " + Pieces.blackScore);
        if (whiteScoreLabel != null) whiteScoreLabel.setText("White's Score: " + Pieces.whiteScore);
        if (turnLabel != null) {
            if (Pieces.isWhiteTurn) {
                turnLabel.setText("White's Turn");
                turnLabel.setBackground(Color.BLUE);
            } else {
                turnLabel.setText("Black's Turn");
                turnLabel.setBackground(Color.RED);
            }
        }
    }
    
    // Show end screen
    public static void showEndScreen() {
        if (endText != null) {
            if (stalemate) {
                endText.setText("Stalemate! The game is a draw.");
            } else {
                // Use winnerColor if set, otherwise fallback to turn logic
                String winColor = winnerColor != null ? winnerColor : (Pieces.isWhiteTurn ? "Black" : "White");
                int winScore = winColor.equals("White") ? Pieces.whiteScore : Pieces.blackScore;
                endText.setText(winColor + " Wins With " + winScore + " Points!");
            }
        }
        if (cardLayout != null && mainPanel != null) {
            cardLayout.show(mainPanel, "end");
        }
    }
    
    public static Pieces getPiece(int x, int y){//this returns the piece at a given x,y
        int xp = x/64;
        int yp = y/64;
        for(Pieces p : ps){
            if(p.xBoardPosition == xp&&p.yBoardPosition ==yp){
                return p;
            }
        }
        return null;
    }
    public static String getName(int x, int y){//this returns the piece name
        int xp = x/64;
        int yp = y/64;
        for(Pieces p : ps){
            if(p.xBoardPosition == xp&&p.yBoardPosition ==yp){
                return p.name;
            }
        }
        return null;
    }
    public static void removeList(){//this returns the piece name
        for(int i = 0; i < size; i++){
            ps.remove();
            }
        }
    // Utility: Find the king for a color
    public static Pieces getKing(boolean isWhite) {
        for (Pieces p : ps) {
            if (p.name.equalsIgnoreCase("king") && p.isWhitePiece == isWhite) return p;
        }
        return null;
    }
    // Utility: Is the king in check?
    public static boolean isKingInCheck(boolean isWhite) {
        Pieces king = getKing(isWhite);
        if (king == null) {
            return false;
        }
        for (Pieces p : ps) {
            if (p.isWhitePiece != isWhite && p.canMove(king.xBoardPosition, king.yBoardPosition, true)) {
                return true;
            }
        }
        return false;
    }
    // Utility: Is it checkmate?
    public static boolean isCheckmate(boolean isWhite) {
        if (!isKingInCheck(isWhite)) {
            return false;
        }
        for (Pieces p : ps) {
            if (p.isWhitePiece == isWhite) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canMove(x, y)) {
                            // Try the move
                            int oldX = p.xBoardPosition, oldY = p.yBoardPosition, oldx = p.xPixelPosition, oldy = p.yPixelPosition;
                            Pieces captured = getPiece(x * 64, y * 64);
                            int capturedIndex = -1;
                            if (captured != null) capturedIndex = ps.indexOf(captured);
                            p.xBoardPosition = x; p.yBoardPosition = y; p.xPixelPosition = x * 64; p.yPixelPosition = y * 64;
                            if (captured != null) ps.remove(captured);
                            boolean stillInCheck = isKingInCheck(isWhite);
                            // Undo
                            p.xBoardPosition = oldX; p.yBoardPosition = oldY; p.xPixelPosition = oldx; p.yPixelPosition = oldy;
                            if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                            if (!stillInCheck) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    // Add stalemate detection
    public static boolean isStalemate(boolean isWhite) {
        if (isKingInCheck(isWhite)) return false;
        for (Pieces p : ps) {
            if (p.isWhitePiece == isWhite) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canMove(x, y)) {
                            // Simulate the move
                            int oldX = p.xBoardPosition, oldY = p.yBoardPosition, oldx = p.xPixelPosition, oldy = p.yPixelPosition;
                            Pieces captured = getPiece(x * 64, y * 64);
                            int capturedIndex = -1;
                            if (captured != null) capturedIndex = ps.indexOf(captured);
                            p.xBoardPosition = x; p.yBoardPosition = y; p.xPixelPosition = x * 64; p.yPixelPosition = y * 64;
                            if (captured != null) ps.remove(captured);
                            boolean leavesKingInCheck = isKingInCheck(isWhite);
                            // Undo
                            p.xBoardPosition = oldX; p.yBoardPosition = oldY; p.xPixelPosition = oldx; p.yPixelPosition = oldy;
                            if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                            if (!leavesKingInCheck) return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
