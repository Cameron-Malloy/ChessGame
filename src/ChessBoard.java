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
    public static String gameMode = "Classic";


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
        BufferedImage all = ImageIO.read(new File("ChessGame/resources/chess_pieces.png"));
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
        JButton button = new JButton("Classic 1v1 Chess");
        button.setBounds(35, 200, 200, 60);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 100, 150));
        startPanel.add(button);
        JButton AIButton = new JButton("Play Versus AI");
        AIButton.setBounds(280, 200, 200, 60);
        AIButton.setFont(new Font("Arial", Font.BOLD, 18));
        AIButton.setFocusable(false);
        AIButton.setBorder(BorderFactory.createRaisedBevelBorder());
        AIButton.setForeground(Color.WHITE);
        AIButton.setBackground(new Color(0, 100, 150));

        startPanel.add(AIButton);
        JButton startCloseButton = new JButton("Close");
        startCloseButton.setBounds(156, 300, 200, 40);
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
                    if (dest != null && selectedPiece != null && dest.isWhite != selectedPiece.isWhite) {
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
                            g.fillRect(king.xp * 64, king.yp * 64, 64, 64);
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
                    if (!p.isWhite) {
                        ind += 6;
                    }
                    g.drawImage(imgs[ind], p.x, p.y, this);
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
            gameMode = "Classic";
            try { run(); } catch (Exception ex) { throw new RuntimeException(ex); }
        });
        AIButton.addActionListener(e -> {
            gameMode = "AI";
            // AI mode selected
            cardLayout.show(mainPanel, "game");
            pn.requestFocusInWindow();
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
                // Only make AI move if it's black's turn in AI mode
                if (gameMode.equals("AI") && !Pieces.color) {
                    // AI makes its move
                    makeAIMove();
                    return;
                }

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
                if (selectedPiece == null && (gameMode.equals("Classic")||(gameMode.equals("AI")&&Pieces.color))) {
                    Pieces p = getPiece(clickedX * 64, clickedY * 64);
                    if (p != null && Pieces.color == p.isWhite) {
                        selectedPiece = p;
                        ChessBoard.legalMoves.clear();
                        // Show legal moves
                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                if (selectedPiece.canMove(x, y)) {
                                    // Simulate the move
                                    int oldX = selectedPiece.xp, oldY = selectedPiece.yp, oldx = selectedPiece.x, oldy = selectedPiece.y;
                                    Pieces captured = getPiece(x * 64, y * 64);
                                    int capturedIndex = -1;
                                    if (captured != null) capturedIndex = ps.indexOf(captured);
                                    selectedPiece.xp = x; selectedPiece.yp = y; selectedPiece.x = x * 64; selectedPiece.y = y * 64;
                                    if (captured != null) ps.remove(captured);
                                    boolean leavesKingInCheck = isKingInCheck(selectedPiece.isWhite);
                                    // Undo
                                    selectedPiece.xp = oldX; selectedPiece.yp = oldY; selectedPiece.x = oldx; selectedPiece.y = oldy;
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
                        int oldX = selectedPiece.xp, oldY = selectedPiece.yp, oldx = selectedPiece.x, oldy = selectedPiece.y;
                        Pieces captured = getPiece(clickedX * 64, clickedY * 64);
                        int capturedIndex = -1;
                        if (captured != null) capturedIndex = ps.indexOf(captured);
                        selectedPiece.xp = clickedX; selectedPiece.yp = clickedY; selectedPiece.x = clickedX * 64; selectedPiece.y = clickedY * 64;
                        if (captured != null) ps.remove(captured);
                        boolean leavesKingInCheck = isKingInCheck(selectedPiece.isWhite);
                        // Undo
                        selectedPiece.xp = oldX; selectedPiece.yp = oldY; selectedPiece.x = oldx; selectedPiece.y = oldy;
                        if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                        if (!leavesKingInCheck) {
                            selectedPiece.move(clickedX, clickedY);
                            trackerScreen();
                            // After move, check for checkmate or king capture
                            boolean opponentIsWhite = !selectedPiece.isWhite;
                            if (isCheckmate(opponentIsWhite)) {
                                winnerColor = selectedPiece.isWhite ? "White" : "Black";
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
                        if (p != null && Pieces.color == p.isWhite) {
                            selectedPiece = p;
                            ChessBoard.legalMoves.clear();
                            // Show legal moves for new selection
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; y++) {
                                    if (selectedPiece.canMove(x, y)) {
                                        // Simulate the move
                                        int oldX = selectedPiece.xp, oldY = selectedPiece.yp, oldx = selectedPiece.x, oldy = selectedPiece.y;
                                        Pieces captured = getPiece(x * 64, y * 64);
                                        int capturedIndex = -1;
                                        if (captured != null) capturedIndex = ps.indexOf(captured);
                                        selectedPiece.xp = x; selectedPiece.yp = y; selectedPiece.x = x * 64; selectedPiece.y = y * 64;
                                        if (captured != null) ps.remove(captured);
                                        boolean leavesKingInCheck = isKingInCheck(selectedPiece.isWhite);
                                        // Undo
                                        selectedPiece.xp = oldX; selectedPiece.yp = oldY; selectedPiece.x = oldx; selectedPiece.y = oldy;
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
            if (Pieces.color) {
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
                String winColor = winnerColor != null ? winnerColor : (Pieces.color ? "Black" : "White");
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
            if(p.xp == xp&&p.yp==yp){
                return p;
            }
        }
        return null;
    }
    public static String getName(int x, int y){//this returns the piece name
        int xp = x/64;
        int yp = y/64;
        for(Pieces p : ps){
            if(p.xp == xp&&p.yp==yp){
                return p.name;
            }
        }
        return null;
    }
    public static void removeList(){//this removes all pieces from the list
        ps.clear();
    }
    // Finds the king for a color 
    public static Pieces getKing(boolean isWhite) {
        for (Pieces p : ps) {
            if (p.name.equalsIgnoreCase("king") && p.isWhite == isWhite) return p;
        }
        return null;
    }
    // Checks if the king is in check 
    public static boolean isKingInCheck(boolean isWhite) {
        Pieces king = getKing(isWhite);
        if (king == null) {
            return false;
        }
        
        // Create a copy of the pieces list to avoid concurrent modification
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            if (p.isWhite != isWhite && p.canMove(king.xp, king.yp, true)) {
                return true;
            }
        }
        return false;
    }
    // Checks if the king is in checkmate 
    public static boolean isCheckmate(boolean isWhite) {
        if (!isKingInCheck(isWhite)) {
            return false;
        }
        
        // Create a copy of the pieces list to avoid concurrent modification
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            if (p.isWhite == isWhite) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canMove(x, y)) {
                            // Try the move
                            int oldX = p.xp, oldY = p.yp, oldx = p.x, oldy = p.y;
                            Pieces captured = getPiece(x * 64, y * 64);
                            int capturedIndex = -1;
                            if (captured != null) capturedIndex = ps.indexOf(captured);
                            p.xp = x; p.yp = y; p.x = x * 64; p.y = y * 64;
                            if (captured != null) ps.remove(captured);
                            boolean stillInCheck = isKingInCheck(isWhite);
                            // Undo
                            p.xp = oldX; p.yp = oldY; p.x = oldx; p.y = oldy;
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
        
        // Create a copy of the pieces list to avoid concurrent modification
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            if (p.isWhite == isWhite) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canMove(x, y)) {
                            // Simulate the move
                            int oldX = p.xp, oldY = p.yp, oldx = p.x, oldy = p.y;
                            Pieces captured = getPiece(x * 64, y * 64);
                            int capturedIndex = -1;
                            if (captured != null) capturedIndex = ps.indexOf(captured);
                            p.xp = x; p.yp = y; p.x = x * 64; p.y = y * 64;
                            if (captured != null) ps.remove(captured);
                            boolean leavesKingInCheck = isKingInCheck(isWhite);
                            // Undo
                            p.xp = oldX; p.yp = oldY; p.x = oldx; p.y = oldy;
                            if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                            if (!leavesKingInCheck) return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public static void makeAIMove(){
        // Show thinking button with very prominent styling
        JButton thinkingButton = new JButton("🤖 AI IS THINKING 🤖") {
            @Override
            protected void paintComponent(Graphics g) {
                // Force the background color
                g.setColor(new Color(255, 0, 0)); // Bright red
                g.fillRect(0, 0, getWidth(), getHeight());
                
                
                g.setColor(new Color(255, 255, 0)); 
                g.setFont(new Font("Arial", Font.BOLD,24));
                FontMetrics fm = g.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(getText(), textX, textY);
            }
        };
        
        thinkingButton.setFont(new Font("Arial", Font.BOLD, 24));
        thinkingButton.setEnabled(false); 
        thinkingButton.setFocusable(false);
        thinkingButton.setOpaque(true); 
        thinkingButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3)); 
        
        // Position the button in the center of the game panel with larger size
        thinkingButton.setBounds((gamePanel.getWidth() - 300) / 2, 
                               (gamePanel.getHeight() - 80) / 2, 
                               300, 80);
        
        // Add the button to the game panel
        gamePanel.add(thinkingButton);
        gamePanel.setComponentZOrder(thinkingButton, 0); 
        gamePanel.repaint();
        
        // Use SwingUtilities.invokeLater to ensure the button shows
        SwingUtilities.invokeLater(() -> {
            try {
                // Check if this is the AI's first move
                if (aiMoveCount == 0) {
                    makeFirstMove();
                    aiMoveCount++;
                } else {
                    // Make the AI move normally - deeper search for endgames
                    int depth = getOptimalSearchDepth();
                    makeMinimaxMove(depth); 
                }
                trackerScreen();
                
                // Check for game over conditions after AI move
                if (isCheckmate(true)) {
                    winnerColor = "Black";
                    gameOver = true;
                    showEndScreen();
                } else if (isStalemate(true)) {
                    stalemate = true;
                    gameOver = true;
                    showEndScreen();
                }
            } finally {
                // Always remove the thinking button
                gamePanel.remove(thinkingButton);
                gamePanel.repaint();
            }
        });
    }
    
    // Minimax AI with alpha-beta pruning
    public static void makeMinimaxMove(int depth) {

        int bestScore = Integer.MAX_VALUE;  // Black is minimizing player
        Pieces bestPiece = null;
        int bestX = -1, bestY = -1;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Create a copy to avoid concurrent modification
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);

        // Loop through all black pieces (AI is black)
        for (Pieces p : piecesCopy) {
            if (!p.isWhite) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canMove(x, y)) {
                            // Simulate move
                            int oldX = p.xp, oldY = p.yp, oldx = p.x, oldy = p.y;
                            Pieces captured = getPiece(x * 64, y * 64);
                            int capturedIndex = -1;
                            if (captured != null) capturedIndex = ps.indexOf(captured);
                            p.xp = x; p.yp = y; p.x = x * 64; p.y = y * 64;
                            if (captured != null) ps.remove(captured);

                            // Only consider move if it doesn't leave black king in check
                            boolean leavesKingInCheck = isKingInCheck(false);

                            int score = Integer.MIN_VALUE;
                            if (!leavesKingInCheck) {
                                score = minimax(depth - 1, true, alpha, beta); // true = white's turn
                            }

                            // Undo move
                            p.xp = oldX; p.yp = oldY; p.x = oldx; p.y = oldy;
                            if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);

                            if (!leavesKingInCheck && score < bestScore) {  // Black is minimizing
                                bestScore = score;
                                bestPiece = p;
                                bestX = x;
                                bestY = y;
                            } else if (!leavesKingInCheck && score == bestScore) {
                                // Randomly choose between equal moves to add variety
                                if (Math.random() < 0.3) {
                                    bestPiece = p;
                                    bestX = x;
                                    bestY = y;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Make the best move found
        if (bestPiece != null && bestX != -1 && bestY != -1) {
            bestPiece.move(bestX, bestY);
            trackerScreen();
            
            // Check for game over conditions after AI move
            if (isCheckmate(true)) {
                winnerColor = "Black";
                gameOver = true;
                showEndScreen();
            } else if (isStalemate(true)) {
                stalemate = true;
                gameOver = true;
                showEndScreen();
            }
        } else {
            // If no move was found, make a random legal move as fallback
            makeRandomMove();
        }
    }

    // Optimized minimax with alpha-beta pruning for 800-1000 ELO
    public static int minimax(int depth, boolean isWhiteTurn, int alpha, int beta) {
        if (depth == 0) {
            return quiescenceSearch(isWhiteTurn, alpha, beta);
        }
        


        int bestScore = isWhiteTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        boolean foundLegalMove = false;

        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        // Move ordering: prioritize captures and center moves for better pruning
        LinkedList<MoveInfo> moves = new LinkedList<>();
        for (Pieces p : piecesCopy) {
            if (p.isWhite == isWhiteTurn) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canMove(x, y)) {
                            Pieces captured = getPiece(x * 64, y * 64);
                            int movePriority = 0;
                            // Prioritize captures
                            if (captured != null) {
                                movePriority = getPieceValue(captured.name) * 10;
                            }
                            // Prioritize center moves
                            if (x >= 2 && x <= 5 && y >= 2 && y <= 5) {
                                movePriority += 50;
                            }
                            // Prioritize queen moves
                            if (p.name.equalsIgnoreCase("queen")) {
                                movePriority += 30;
                            }
                            moves.add(new MoveInfo(p, x, y, movePriority));
                        }
                    }
                }
            }
        }
        // Sort moves by priority (highest first)
        moves.sort((a, b) -> Integer.compare(b.priority, a.priority));
        for (MoveInfo moveInfo : moves) {
            Pieces p = moveInfo.piece;
            int x = moveInfo.x;
            int y = moveInfo.y;
            // Simulate move
            int oldX = p.xp, oldY = p.yp, oldx = p.x, oldy = p.y;
            Pieces captured = getPiece(x * 64, y * 64);
            int capturedIndex = -1;
            if (captured != null) capturedIndex = ps.indexOf(captured);
            p.xp = x; p.yp = y; p.x = x * 64; p.y = y * 64;
            if (captured != null) ps.remove(captured);
            // Check if move leaves king in check
            boolean leavesKingInCheck = isKingInCheck(isWhiteTurn);
            int score = Integer.MIN_VALUE;
            if (!leavesKingInCheck) {
                score = minimax(depth - 1, !isWhiteTurn, alpha, beta);
                foundLegalMove = true;
            }
            // Undo move
            p.xp = oldX; p.yp = oldY; p.x = oldx; p.y = oldy;
            if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
            if (!leavesKingInCheck) {
                if (isWhiteTurn) {
                    bestScore = Math.max(bestScore, score);  // White maximizes
                    alpha = Math.max(alpha, bestScore);
                } else {
                    bestScore = Math.min(bestScore, score);  // Black minimizes
                    beta = Math.min(beta, bestScore);
                }
                // Alpha-beta pruning
                if (beta <= alpha) {
                    break;
                }
            }
        }
        // If no legal moves found, this is checkmate or stalemate
        if (!foundLegalMove) {
            if (isKingInCheck(isWhiteTurn)) {
                // Checkmate - opponent wins
                return isWhiteTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            } else {
                // Stalemate - draw
                return 0;
            }
        }
        return bestScore;
    }

    // Quiescence search to prevent horizon effect
    public static int quiescenceSearch(boolean isWhiteTurn, int alpha, int beta) {
        int standPat = evaluateBoard();
        
        if (isWhiteTurn) {
            if (standPat >= beta) return beta;
            alpha = Math.max(alpha, standPat);
        } else {
            if (standPat <= alpha) return alpha;
            beta = Math.min(beta, standPat);
        }
        
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        // Only look at captures
        for (Pieces p : piecesCopy) {
            if (p.isWhite == isWhiteTurn) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Pieces target = getPiece(x * 64, y * 64);
                        if (target != null && target.isWhite != p.isWhite && p.canMove(x, y)) {
                            // Simulate capture
                            int oldX = p.xp, oldY = p.yp, oldx = p.x, oldy = p.y;
                            int capturedIndex = ps.indexOf(target);
                            p.xp = x; p.yp = y; p.x = x * 64; p.y = y * 64;
                            ps.remove(target);
                            
                            // Check if capture leaves king in check
                            boolean leavesKingInCheck = isKingInCheck(isWhiteTurn);
                            int score = Integer.MIN_VALUE;
                            if (!leavesKingInCheck) {
                                score = quiescenceSearch(!isWhiteTurn, alpha, beta);
                            }
                            
                            // Undo capture
                            p.xp = oldX; p.yp = oldY; p.x = oldx; p.y = oldy;
                            ps.add(capturedIndex, target);
                            
                            if (!leavesKingInCheck) {
                                if (isWhiteTurn) {
                                    alpha = Math.max(alpha, score);
                                } else {
                                    beta = Math.min(beta, score);
                                }
                                if (beta <= alpha) break;
                            }
                        }
                    }
                }
            }
        }
        
        return isWhiteTurn ? alpha : beta;
    }
    
    // Helper class for move ordering
    private static class MoveInfo {
        Pieces piece;
        int x, y;
        int priority;
        MoveInfo(Pieces piece, int x, int y, int priority) {
            this.piece = piece;
            this.x = x;
            this.y = y;
            this.priority = priority;
        }
    }

    // Opening development evaluation
    public static int getOpeningDevelopmentBonus() {
        int bonus = 0;
        
        // Create a copy to avoid concurrent modification
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        // Count total pieces to estimate game phase
        int totalPieces = piecesCopy.size();
        
        // If we're in the opening (many pieces still on board)
        if (totalPieces >= 24) {
            for (Pieces p : piecesCopy) {
                if (!p.isWhite) { // Black pieces (AI)
                    int developmentBonus = 0;
                    
                    switch (p.name.toLowerCase()) {
                        case "pawn":
                            // Pawns should advance in opening
                            if (p.yp < 4) developmentBonus += 50;
                            break;
                        case "knight":
                        case "bishop":
                            // Minor pieces should develop to center
                            if (p.xp >= 2 && p.xp <= 5 && p.yp >= 2 && p.yp <= 5) {
                                developmentBonus += 50;
                            }
                            break;
                        case "rook":
                            // Rooks should connect or control open files
                            if (p.yp >= 3) developmentBonus += 30;
                            break;
                        case "queen":
                            // Queen should not come out too early
                            if (p.yp >= 4) developmentBonus += 100;
                            break;
                        case "king":
                            // King should stay safe in opening
                            if (p.xp >= 5 || p.xp <= 2) developmentBonus += 100;
                            break;
                    }
                    
                    bonus += developmentBonus;
                }
            }
        }
        
        return bonus;
    }

    // Optimized evaluateBoard method - faster and smarter
    public static int evaluateBoard() {
        int score = 0;
        
        // Check for immediate checkmate opportunities
        if (isCheckmate(true)) {
            return Integer.MIN_VALUE; // White is checkmated, Black wins
        }
        if (isCheckmate(false)) {
            return Integer.MAX_VALUE; // Black is checkmated, White wins
        }
        
        // Basic piece values (use consistent values)
        int material = 0;
        for (Pieces p : ps) {
            int value = 0;
            switch (p.name.toLowerCase()) {
                case "pawn": value = 100; break;
                case "knight": value = 320; break;
                case "bishop": value = 330; break;
                case "rook": value = 500; break;
                case "queen": value = 900; break;
                case "king": value = 2000; break;
            }
            material += p.isWhite ? value : -value;
        }
        score += material * 10;  // Make material much more important
        
        int kingSafety = getKingSafetyBonus() * 1;
        int hangingPenalty = getHangingPiecePenalty() * 1;
        int tactical = getTacticalEvaluation() * 2;
        int center = getCenterControlBonus() * 1;
        int positional = getPositionalBonus() * 1;
        int mobility = getMobilityBonus() * 1;
        int opening = getOpeningDevelopmentBonus() * 1;
        int queenSafety = getQueenSafetyBonus() * 1;
        int endgame = getEndgameEvaluation();
        int checkmate = getCheckmateDetectionBonus();
        
        score += kingSafety;
        score += hangingPenalty;
        score += tactical;
        score += center;
        score += positional;
        score += mobility;
        score += opening;
        score += queenSafety;
        score += endgame;
        score += checkmate;
        

        
        return score;
    }
    // Helper to determine whose turn it is
    private static boolean isWhiteTurnToMove() {
        int whiteCount = 0, blackCount = 0;
        for (Pieces p : ps) {
            if (p.isWhite) whiteCount++;
            else blackCount++;
        }
        // If even, it's white's turn (assuming white starts)
        return (whiteCount + blackCount) % 2 == 0;
    }
    
    // Checkmate detection bonus - encourages moves that lead to checkmate
    public static int getCheckmateDetectionBonus() {
        int bonus = 0;
        
        // Check if current position is checkmate
        if (isCheckmate(true)) {
            return Integer.MIN_VALUE; // White is checkmated
        }
        if (isCheckmate(false)) {
            return Integer.MAX_VALUE; // Black is checkmated
        }
        
        // Check for immediate checkmate opportunities
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        // Check if any move leads to checkmate
        for (Pieces p : piecesCopy) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (p.canMove(x, y)) {
                        // Simulate move
                        int oldX = p.xp, oldY = p.yp, oldx = p.x, oldy = p.y;
                        Pieces captured = getPiece(x * 64, y * 64);
                        int capturedIndex = -1;
                        if (captured != null) capturedIndex = ps.indexOf(captured);
                        p.xp = x; p.yp = y; p.x = x * 64; p.y = y * 64;
                        if (captured != null) ps.remove(captured);
                        
                        // Check if this move leads to checkmate
                        boolean leavesKingInCheck = isKingInCheck(p.isWhite);
                        if (!leavesKingInCheck) {
                            if (isCheckmate(!p.isWhite)) {
                                // This move leads to checkmate!
                                if (p.isWhite) {
                                    bonus += 10000; // White can checkmate
                                } else {
                                    bonus -= 10000; // Black can checkmate
                                }
                            }
                        }
                        
                        // Undo move
                        p.xp = oldX; p.yp = oldY; p.x = oldx; p.y = oldy;
                        if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                    }
                }
            }
        }
        
        return bonus;
    }

    // Improved queen safety bonus logic - less extreme but still protective
    public static int getQueenSafetyBonus() {
        int bonus = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            if (p.name.equalsIgnoreCase("queen")) {
                int queenSafety = 0;
                
                // Check if queen is currently under attack
                boolean isUnderAttack = false;
                boolean isDefended = false;
                Pieces attacker = null;
                
                for (Pieces potentialAttacker : piecesCopy) {
                    if (potentialAttacker.isWhite != p.isWhite && potentialAttacker.canMove(p.xp, p.yp, true)) {
                        isUnderAttack = true;
                        attacker = potentialAttacker;
                        
                        // Check if queen is defended by a friendly piece
                        for (Pieces defender : piecesCopy) {
                            if (defender.isWhite == p.isWhite && defender != p && defender.canMove(p.xp, p.yp, true)) {
                                isDefended = true;
                                break;
                            }
                        }
                        break; // Found first attacker
                    }
                }
                
                if (isUnderAttack) {
                    // If queen is under attack
                    if (isDefended) {
                        // Queen is defended - very small penalty
                        queenSafety -= 50;
                    } else {
                        // Queen is undefended - moderate penalty
                        queenSafety -= 500;
                        
                        // Extra penalty if attacker is less valuable than queen
                        if (attacker != null) {
                            int attackerValue = getPieceValue(attacker.name);
                            int queenValue = 900;
                            if (attackerValue < queenValue) {
                                queenSafety -= 300; // Extra penalty for hanging queen
                            }
                        }
                    }
                } else {
                    // Queen is safe - small bonus
                    queenSafety += 50;
                }
                
                // Add to bonus (positive for white, negative for black)
                bonus += p.isWhite ? queenSafety : -queenSafety;
            }
        }
        
        return bonus;
    }
    
    // Advanced endgame evaluation for checkmate detection
    public static int getEndgameEvaluation() {
        int endgameScore = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        // Count pieces to determine game phase
        int whitePieces = 0, blackPieces = 0;
        int whitePawns = 0, blackPawns = 0;
        boolean whiteHasQueen = false, blackHasQueen = false;
        boolean whiteHasRook = false, blackHasRook = false;
        
        for (Pieces p : piecesCopy) {
            if (p.isWhite) {
                whitePieces++;
                if (p.name.equalsIgnoreCase("pawn")) whitePawns++;
                if (p.name.equalsIgnoreCase("queen")) whiteHasQueen = true;
                if (p.name.equalsIgnoreCase("rook")) whiteHasRook = true;
            } else {
                blackPieces++;
                if (p.name.equalsIgnoreCase("pawn")) blackPawns++;
                if (p.name.equalsIgnoreCase("queen")) blackHasQueen = true;
                if (p.name.equalsIgnoreCase("rook")) blackHasRook = true;
            }
        }
        
        // Endgame scenarios
        if (whitePieces <= 4 && blackPieces <= 4) {
            // Very simplified endgame - focus on king positioning
            endgameScore += getSimplifiedEndgameEvaluation();
        } else if (whitePieces <= 8 && blackPieces <= 8) {
            // Simplified endgame - focus on material and king safety
            endgameScore += getSimplifiedEndgameEvaluation();
        }
        
        // King and pawn endgame
        if (whitePawns > 0 && blackPawns > 0 && !whiteHasQueen && !blackHasQueen && !whiteHasRook && !blackHasRook) {
            endgameScore += getKingPawnEndgameEvaluation();
        }
        
        // Queen endgame
        if (whiteHasQueen && blackHasQueen && whitePieces <= 6 && blackPieces <= 6) {
            endgameScore += getQueenEndgameEvaluation();
        }
        
        return endgameScore;
    }
    
    // Simplified endgame evaluation for very few pieces
    private static int getSimplifiedEndgameEvaluation() {
        int score = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        Pieces whiteKing = getKing(true);
        Pieces blackKing = getKing(false);
        
        if (whiteKing != null && blackKing != null) {
            // King distance evaluation
            int kingDistance = Math.abs(whiteKing.xp - blackKing.xp) + Math.abs(whiteKing.yp - blackKing.yp);
            
            // Kings should be close in simplified endgames
            if (kingDistance <= 2) {
                score += 200; // Bonus for close kings
            }
            
            // King centralization bonus
            double whiteKingCenterDistance = Math.abs(whiteKing.xp - 3.5) + Math.abs(whiteKing.yp - 3.5);
            double blackKingCenterDistance = Math.abs(blackKing.xp - 3.5) + Math.abs(blackKing.yp - 3.5);
            
            score += (int)((4 - whiteKingCenterDistance) * 50); // White king centralization
            score -= (int)((4 - blackKingCenterDistance) * 50); // Black king centralization
        }
        
        return score;
    }
    
    // King and pawn endgame evaluation
    private static int getKingPawnEndgameEvaluation() {
        int score = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        Pieces whiteKing = getKing(true);
        Pieces blackKing = getKing(false);
        
        if (whiteKing != null && blackKing != null) {
            // Pawn advancement bonus
            for (Pieces p : piecesCopy) {
                if (p.name.equalsIgnoreCase("pawn")) {
                    if (p.isWhite) {
                        score += (7 - p.yp) * 100; // White pawn advancement
                    } else {
                        score -= p.yp * 100; // Black pawn advancement
                    }
                }
            }
            
            // King opposition (kings facing each other)
            if (Math.abs(whiteKing.xp - blackKing.xp) <= 1 && Math.abs(whiteKing.yp - blackKing.yp) <= 1) {
                score += 300; // Strong bonus for king opposition
            }
        }
        
        return score;
    }
    
    // Queen endgame evaluation
    private static int getQueenEndgameEvaluation() {
        int score = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        Pieces whiteKing = getKing(true);
        Pieces blackKing = getKing(false);
        Pieces whiteQueen = null, blackQueen = null;
        
        // Find queens
        for (Pieces p : piecesCopy) {
            if (p.name.equalsIgnoreCase("queen")) {
                if (p.isWhite) whiteQueen = p;
                else blackQueen = p;
            }
        }
        
        if (whiteKing != null && blackKing != null && whiteQueen != null && blackQueen != null) {
            // Queen should be close to enemy king
            int whiteQueenToBlackKing = Math.abs(whiteQueen.xp - blackKing.xp) + Math.abs(whiteQueen.yp - blackKing.yp);
            int blackQueenToWhiteKing = Math.abs(blackQueen.xp - whiteKing.xp) + Math.abs(blackQueen.yp - whiteKing.yp);
            
            score += (8 - whiteQueenToBlackKing) * 50; // White queen close to black king
            score -= (8 - blackQueenToWhiteKing) * 50; // Black queen close to white king
        }
        
        return score;
    }
    
    // Helper method to get piece value - consistent across all functions
    private static int getPieceValue(String pieceName) {
        switch (pieceName.toLowerCase()) {
            case "pawn": return 100;
            case "knight": return 320;
            case "bishop": return 330;
            case "rook": return 500;
            case "queen": return 900;
            case "king": return 2000;
            default: return 0;
        }
    }
    
    // Aggressive hanging piece evaluation to prevent blunders
    public static int getHangingPiecePenalty() {
        int penalty = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            boolean isAttacked = false;
            boolean isDefended = false;
            int attackerValue = 0;
            int defenderValue = 0;
            int attackCount = 0;
            int defendCount = 0;
            int bestAttackerValue = 0;
            
            // Check if piece is attacked and by what
            for (Pieces attacker : piecesCopy) {
                if (attacker.isWhite != p.isWhite && attacker.canMove(p.xp, p.yp, true)) {
                    isAttacked = true;
                    attackCount++;
                    int attackerPieceValue = getPieceValue(attacker.name);
                    if (attackerPieceValue > attackerValue) {
                        attackerValue = attackerPieceValue;
                    }
                    if (attackerPieceValue > bestAttackerValue) {
                        bestAttackerValue = attackerPieceValue;
                    }
                }
            }
            
            // Check if piece is defended and by what
            if (isAttacked) {
                for (Pieces defender : piecesCopy) {
                    if (defender.isWhite == p.isWhite && defender != p && defender.canMove(p.xp, p.yp, true)) {
                        isDefended = true;
                        defendCount++;
                        int defenderPieceValue = getPieceValue(defender.name);
                        if (defenderPieceValue > defenderValue) {
                            defenderValue = defenderPieceValue;
                        }
                    }
                }
                
                int pieceValue = getPieceValue(p.name);
                
                // If piece is attacked but not defended, apply massive penalty
                if (!isDefended) {
                    penalty += p.isWhite ? -pieceValue * 5 : pieceValue * 5; // Massive penalty for hanging pieces
                    
                    // Extra penalty for hanging high-value pieces
                    if (pieceValue >= 300) {
                        penalty += p.isWhite ? -pieceValue * 2 : pieceValue * 2; // Additional penalty for hanging knights/bishops/rooks
                    }
                    
                    // Massive penalty for hanging the queen
                    if (p.name.equalsIgnoreCase("queen")) {
                        penalty += p.isWhite ? -pieceValue * 3 : pieceValue * 3; // Extra massive penalty for hanging queen
                    }
                }
                // If piece is attacked by more valuable piece and defended by less valuable piece
                else if (attackerValue > defenderValue) {
                    penalty += p.isWhite ? -pieceValue * 3 : pieceValue * 3; // Strong penalty for weak defense
                }
                // If piece is attacked multiple times but defended fewer times
                else if (attackCount > defendCount) {
                    penalty += p.isWhite ? -pieceValue * 2 : pieceValue * 2; // Strong penalty for being outnumbered
                }
                // If piece is attacked by queen, extra penalty
                else if (bestAttackerValue >= 900) { // Queen attack
                    penalty += p.isWhite ? -pieceValue : pieceValue; // Extra penalty for queen attacks
                }
                
                // Special penalty for hanging pawns
                if (p.name.equalsIgnoreCase("pawn") && !isDefended) {
                    penalty += p.isWhite ? -50 : 50; // Extra penalty for hanging pawns
                }
            }
        }
        
        return penalty;
    }
    
    // Advanced tactical evaluation to prevent blunders and capture easy pieces
    public static int getTacticalEvaluation() {
        int tacticalScore = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        // Only evaluate pieces that can actually move (not just any piece on the board)
        for (Pieces p : piecesCopy) {
            // Check if this piece can actually move anywhere
            boolean canMoveAnywhere = false;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (p.canMove(x, y)) {
                        canMoveAnywhere = true;
                        break;
                    }
                }
                if (canMoveAnywhere) break;
            }
            
            // Skip pieces that can't move
            if (!canMoveAnywhere) continue;
            
            // Find the best tactical move for this specific piece
            int bestPieceTacticalScore = 0;
            
            // Evaluate each possible move for this piece
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (p.canMove(x, y)) {
                        int moveTacticalScore = 0;
                        
                        // Check if this move is a capture
                        Pieces target = getPiece(x * 64, y * 64);
                        if (target != null && target.isWhite != p.isWhite) {
                            int attackerValue = getPieceValue(p.name);
                            int targetValue = getPieceValue(target.name);
                            
                            // Evaluate capture safety
                            boolean isSafeCapture = true;
                            boolean isDefended = false;
                            
                            // Check if target is defended
                            for (Pieces defender : piecesCopy) {
                                if (defender != p && defender.isWhite == target.isWhite && defender.canMove(x, y, true)) {
                                    isDefended = true;
                                    break;
                                }
                            }
                            
                            // If defended, check if recapture is profitable
                            if (isDefended) {
                                int bestRecaptureValue = 0;
                                for (Pieces recapturer : piecesCopy) {
                                    if (recapturer.isWhite == target.isWhite && recapturer.canMove(x, y)) {
                                        int recaptureValue = getPieceValue(recapturer.name);
                                        if (recaptureValue > bestRecaptureValue) {
                                            bestRecaptureValue = recaptureValue;
                                        }
                                    }
                                }
                                // Only safe if we can recapture with equal or better piece
                                if (bestRecaptureValue >= attackerValue) {
                                    isSafeCapture = false;
                                }
                            }
                            
                            if (isSafeCapture) {
                                moveTacticalScore = targetValue / 2; // Base capture bonus
                                

                                
                                // Extra bonus for capturing with much less valuable piece
                                if (targetValue > attackerValue * 2) {
                                    moveTacticalScore += targetValue / 4;
                                }
                                
                                // Bonus for capturing undefended pieces
                                if (!isDefended) {
                                    moveTacticalScore += targetValue / 4;
                                }
                            }
                        }
                        
                        // Check for fork opportunities (attacking multiple pieces)
                        int attackCount = 0;
                        int totalAttackValue = 0;
                        
                        for (Pieces forkTarget : piecesCopy) {
                            if (forkTarget != p && forkTarget.isWhite != p.isWhite && p.canMove(forkTarget.xp/64, forkTarget.yp/64)) {
                                int targetValue = getPieceValue(forkTarget.name);
                                if (targetValue >= 200) {
                                    attackCount++;
                                    totalAttackValue += targetValue;
                                }
                            }
                        }
                        
                        if (attackCount >= 2) {
                            moveTacticalScore += 50; // Fork bonus
                        }
                        
                        // Check for queen attacks
                        for (Pieces queenTarget : piecesCopy) {
                            if (queenTarget != p && queenTarget.isWhite != p.isWhite && queenTarget.name.equalsIgnoreCase("queen") && p.canMove(queenTarget.xp/64, queenTarget.yp/64)) {
                                moveTacticalScore += 100; // Queen attack bonus
                            }
                        }
                        
                        // Keep track of the best tactical score for this piece
                        if (moveTacticalScore > bestPieceTacticalScore) {
                            bestPieceTacticalScore = moveTacticalScore;
                        }
                    }
                }
            }
            
            // Add the best tactical score for this piece to the total
            tacticalScore += p.isWhite ? bestPieceTacticalScore : -bestPieceTacticalScore;
            

        }
        
        return tacticalScore;
    }

    // Fix king safety bonus logic
    public static int getKingSafetyBonus() {
        int bonus = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (boolean isWhite : new boolean[]{true, false}) {
            Pieces king = getKing(isWhite);
            if (king == null) continue;
            
            int safetyScore = 0;
            
            // Check if king is in check
            if (isKingInCheck(isWhite)) {
                safetyScore -= 200; // Strong penalty for being in check
            }
            
            // Bonus for pawns protecting king
            int pawnProtection = 0;
            for (Pieces p : piecesCopy) {
                if (p.isWhite == isWhite && p.name.equalsIgnoreCase("pawn")) {
                    int dx = Math.abs(p.xp - king.xp);
                    int dy = Math.abs(p.yp - king.yp);
                    if (dx <= 1 && dy <= 1) {
                        pawnProtection += 30; // Bonus for pawn protection
                    }
                }
            }
            safetyScore += pawnProtection;
            
            // Heavy penalty for king in center during middle game
            if (king.xp >= 3 && king.xp <= 4 && king.yp >= 3 && king.yp <= 4) {
                safetyScore -= 100; // Penalty for king in center
            }
            
            // Bonus for castled king
            if ((isWhite && king.xp >= 6) || (!isWhite && king.xp <= 1)) {
                safetyScore += 50; // Bonus for castled king
            }
            
            // Add to bonus (positive for white, negative for black)
            bonus += isWhite ? safetyScore : -safetyScore;
        }
        return bonus;
    }

    // Improved center control evaluation
    public static int getCenterControlBonus() {
        int bonus = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            int centerBonus = 0;
            
            // Center squares (d4, d5, e4, e5) - highest value
            if ((p.xp == 3 || p.xp == 4) && (p.yp == 3 || p.yp == 4)) {
                centerBonus += 100; // Strong center bonus
            }
            // Extended center (c3-c6, d3-d6, e3-e6, f3-f6)
            else if (p.xp >= 2 && p.xp <= 5 && p.yp >= 2 && p.yp <= 5) {
                centerBonus += 50; // Extended center bonus
            }
            
            // Extra bonus for pawns in center
            if (p.name.equalsIgnoreCase("pawn")) {
                centerBonus *= 2; // Pawns in center are especially valuable
            }
            
            bonus += p.isWhite ? centerBonus : -centerBonus;
        }
        return bonus;
    }

    // Quick mobility evaluation - simplified for speed
    public static int getMobilityBonus() {
        int whiteMobility = 0;
        int blackMobility = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            int moves = 0;
            // Quick count of legal moves without deep validation
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (p.canMove(x, y)) {
                        // Only validate captures and center moves
                        Pieces target = getPiece(x * 64, y * 64);
                        if (target != null || (x >= 2 && x <= 5 && y >= 2 && y <= 5)) {
                            // Quick validation for important moves
                            int oldX = p.xp, oldY = p.yp;
                            p.xp = x; p.yp = y;
                            boolean leavesKingInCheck = isKingInCheck(p.isWhite);
                            p.xp = oldX; p.yp = oldY;
                            
                            if (!leavesKingInCheck) {
                                moves++;
                            }
                        } else {
                            // Assume other moves are legal for speed
                            moves++;
                        }
                    }
                }
            }
            
            if (p.isWhite) {
                whiteMobility += moves;
            } else {
                blackMobility += moves;
            }
        }
        
        return (whiteMobility - blackMobility);
    }

    // Improved positional bonus evaluation - more aggressive
    public static int getPositionalBonus() {
        int bonus = 0;
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        
        for (Pieces p : piecesCopy) {
            int posBonus = 0;
            switch (p.name.toLowerCase()) {
                case "pawn":
                    // Pawns in center are better
                    if (p.xp >= 3 && p.xp <= 4) posBonus += 50; // Strong center pawn bonus
                    // Pawns on 6th/3rd rank are good
                    if ((p.isWhite && p.yp <= 3) || (!p.isWhite && p.yp >= 4)) posBonus += 50; // Advanced pawn bonus
                    // Bonus for connected pawns
                    for (Pieces other : piecesCopy) {
                        if (other != p && other.isWhite == p.isWhite && other.name.equalsIgnoreCase("pawn")) {
                            if (Math.abs(other.xp - p.xp) == 1 && other.yp == p.yp) {
                                posBonus += 30; // Connected pawns bonus
                            }
                        }
                    }
                    break;
                case "knight":
                case "bishop":
                    // Knights and bishops in center are better
                    if (p.xp >= 2 && p.xp <= 5 && p.yp >= 2 && p.yp <= 5) {
                        posBonus += 50; // Strong center bonus
                    }
                    // Bonus for developed pieces (out of starting position)
                    if ((p.isWhite && p.yp < 7) || (!p.isWhite && p.yp > 0)) {
                        posBonus += 50; // Development bonus
                    }
                    break;
                case "rook":
                    // Rooks on open files or 7th rank are good
                    if (p.yp == (p.isWhite ? 1 : 6)) posBonus += 50; // Strong 7th rank bonus
                    // Bonus for rooks on open files
                    boolean openFile = true;
                    for (Pieces other : piecesCopy) {
                        if (other.name.equalsIgnoreCase("pawn") && other.xp == p.xp) {
                            openFile = false;
                            break;
                        }
                    }
                    if (openFile) posBonus += 50; // Open file bonus
                    break;
                case "queen":
                    // Queen in center for attack, but not too early
                    if (p.xp >= 2 && p.xp <= 5 && p.yp >= 2 && p.yp <= 5) posBonus += 50; // Center queen bonus
                    break;
                case "king":
                    // King safety - prefer kingside in middle game
                    if (p.xp >= 5 || p.xp <= 2) posBonus += 100; // Kingside safety bonus
                    break;
            }
            bonus += p.isWhite ? posBonus : -posBonus;
        }
        return bonus;
    }
    // Determine optimal search depth based on game phase
    public static int getOptimalSearchDepth() {
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        int totalPieces = piecesCopy.size();
        
        // Deeper search for endgames
        if (totalPieces <= 8) {
            return 6; // Very deep search for endgames
        } else if (totalPieces <= 12) {
            return 5; // Deep search for simplified positions
        } else if (totalPieces <= 16) {
            return 4; // Medium-deep search for middle game
        } else {
            return 3; // Standard depth for opening/middle game
        }
    }
    
    // Add this method to force the AI's first move
    public static void makeFirstMove() {
        // Find the king's pawn (e7) and queen's pawn (d7)
        Pieces kingPawn = null;
        Pieces queenPawn = null;
        
        for (Pieces p : ps) {
            if (!p.isWhite && p.name.equalsIgnoreCase("pawn")) {
                if (p.xp == 4 && p.yp == 1) { // e7 (king's pawn)
                    kingPawn = p;
                } else if (p.xp == 3 && p.yp == 1) { // d7 (queen's pawn)
                    queenPawn = p;
                }
            }
        }
        
        // Randomly choose between king's pawn and queen's pawn
        Pieces pawnToMove = null;
        if (kingPawn != null && queenPawn != null) {
            // Both pawns available, randomly choose
            if (Math.random() < 0.5) {
                pawnToMove = kingPawn;
            } else {
                pawnToMove = queenPawn;
            }
        }
        
        if (pawnToMove != null && pawnToMove.canMove(pawnToMove.xp, pawnToMove.yp + 2)) {
            // Move the pawn up 2 squares
            pawnToMove.move(pawnToMove.xp, pawnToMove.yp + 2);
            trackerScreen();
            
            // Check for game over conditions after AI move
            if (isCheckmate(true)) {
                winnerColor = "Black";
                gameOver = true;
                showEndScreen();
            } else if (isStalemate(true)) {
                stalemate = true;
                gameOver = true;
                showEndScreen();
            }
        } else {
            // Fallback to normal minimax if pawn move is not possible
            makeMinimaxMove(3);
        }
    }

    // Add a move counter to track if this is the first move
    public static int aiMoveCount = 0;
    
    // Fallback method to make a random legal move
    public static void makeRandomMove() {
        LinkedList<Pieces> piecesCopy = new LinkedList<>(ps);
        java.util.List<Object[]> legalMoves = new java.util.ArrayList<>();
        
        // Find all legal moves for black pieces
        for (Pieces p : piecesCopy) {
            if (!p.isWhite) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (p.canMove(x, y)) {
                            // Simulate move to check if it's legal
                            int oldX = p.xp, oldY = p.yp, oldx = p.x, oldy = p.y;
                            Pieces captured = getPiece(x * 64, y * 64);
                            int capturedIndex = -1;
                            if (captured != null) capturedIndex = ps.indexOf(captured);
                            p.xp = x; p.yp = y; p.x = x * 64; p.y = y * 64;
                            if (captured != null) ps.remove(captured);
                            
                            boolean leavesKingInCheck = isKingInCheck(false);
                            
                            // Undo move
                            p.xp = oldX; p.yp = oldY; p.x = oldx; p.y = oldy;
                            if (captured != null && capturedIndex != -1) ps.add(capturedIndex, captured);
                            
                            if (!leavesKingInCheck) {
                                legalMoves.add(new Object[]{p, x, y});
                            }
                        }
                    }
                }
            }
        }
        
        // Make a random legal move
        if (!legalMoves.isEmpty()) {
            int randomIndex = (int)(Math.random() * legalMoves.size());
            Object[] move = legalMoves.get(randomIndex);
            Pieces piece = (Pieces)move[0];
            int x = (Integer)move[1];
            int y = (Integer)move[2];
            
            piece.move(x, y);
            trackerScreen();
            
            // Check for game over conditions
            if (isCheckmate(true)) {
                winnerColor = "Black";
                gameOver = true;
                showEndScreen();
            } else if (isStalemate(true)) {
                stalemate = true;
                gameOver = true;
                showEndScreen();
            }
        }
    }
}