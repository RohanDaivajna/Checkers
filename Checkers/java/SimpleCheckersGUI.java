// Author: Rohan Daivajna 
// Roll number: 35
// Title: GUI Checkers Game
// Start Date: 08-09-2024
// Modified Date: 15-09-2024
// Description: The code implements a simple GUI-based Checkers game in Java using Swing, featuring a playable 8x8 board, piece movement, turn-based mechanics, and game state management.

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

public class SimpleCheckersGUI implements MouseListener, ActionListener {
    
    private JFrame frame;
    private JPanel boardpanel;
    private JLabel piecesLabel;
    private JMenuBar menubar;
    private JMenu fileMenu;
    private JMenuItem newGame;
    private JMenuItem exit;
    private Color currentTurn;
    private final int borderWidth = 1;
    private Board board;
    private int blackCheckersLeft;
    private int redCheckersLeft;
    private Square selectedSquare;

    // Constructor initializes the GUI and game state
    public SimpleCheckersGUI() {
        CreateAndShowGUI();
        currentTurn = Color.GREEN; // Set initial turn
        redCheckersLeft = 12;
        blackCheckersLeft = 12;
        updateStatus();
    }

    // Create and display the GUI components
    public void CreateAndShowGUI() {
        frame = new JFrame("SimpleCheckersGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Initialize board panel with a grid layout
        boardpanel = new JPanel(new GridLayout(8, 8));
        boardpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Create and initialize the board
        board = new Board();
        board.placeStartingPieces();

        // Initialize the status label
        piecesLabel = new JLabel(" ");
        piecesLabel.setHorizontalTextPosition(JLabel.LEFT);
        piecesLabel.setVerticalTextPosition(JLabel.BOTTOM);

        // Create menu bar and items
        menubar = new JMenuBar();
        fileMenu = new JMenu("File");
        newGame = new JMenuItem("New Game");
        newGame.addActionListener(this); // Add action listener for new game
        exit = new JMenuItem("Exit");
        exit.addActionListener(this); // Add action listener for exit
        fileMenu.add(newGame);
        fileMenu.add(exit);
        menubar.add(fileMenu);

        // Add board and status label to the frame
        addBoardToPanel(board, boardpanel);
        frame.add(boardpanel);
        frame.add(piecesLabel);
        frame.setJMenuBar(menubar);
        frame.pack();

        // Adjust frame size and visibility
        Rectangle boundingRect = frame.getBounds();
        frame.setBounds(boundingRect.x, boundingRect.y, boundingRect.width + 5, boundingRect.height);
        frame.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Square sel = (Square) e.getComponent();

        // Handle selection and movement of pieces
        if (sel.isOccupied() && sel.getOccupant().getColor() != currentTurn && currentTurn != Color.GREEN) {
            piecesLabel.setText("This isn't the time to use that!");
            return;
        }
        if (sel.isOccupied() && selectedSquare == null) {
            selectedSquare = sel;
            selectedSquare.setHighlight(true);
            board.setMovesHighlighted(selectedSquare.getOccupant(), true);
            return;
        } else if (sel.isOccupied() && !sel.equals(selectedSquare)) {
            selectedSquare.setHighlight(false);
            board.setMovesHighlighted(selectedSquare.getOccupant(), false);
            selectedSquare = sel;
            selectedSquare.setHighlight(true);
            board.setMovesHighlighted(selectedSquare.getOccupant(), true);
            return;
        } else if (sel.equals(selectedSquare)) {
            selectedSquare.setHighlight(false);
            board.setMovesHighlighted(selectedSquare.getOccupant(), false);
            selectedSquare = null;
        } else if (!sel.isOccupied() && selectedSquare != null) {
            boolean found = false;
            boolean jumped = false;
            Vector<Square> oldPossibleMoves = board.getPossibleMoves(selectedSquare.getOccupant());
            for (Square choice : oldPossibleMoves) {
                if (choice.equals(sel)) {
                    if (currentTurn == Color.GREEN)
                        currentTurn = selectedSquare.getOccupant().getColor();
                    jumped = board.move(selectedSquare, sel);
                    found = true;
                }
            }
            if (found) {
                if (jumped) {
                    if (currentTurn == Color.BLACK) {
                        redCheckersLeft--;
                    } else {
                        blackCheckersLeft--;
                    }
                }
                selectedSquare.setHighlight(false);
                for (Square unhighlight : oldPossibleMoves)
                    unhighlight.setHighlight(false);
                selectedSquare = null;
                endTurn();
                updateStatus();
                String winningStr = winner();
                if (winningStr != null) {
                    int restart = JOptionPane.showConfirmDialog(null, winningStr + " Do you want to start a new game?", "New Game?", JOptionPane.YES_NO_OPTION);
                    if (restart == JOptionPane.YES_OPTION)
                        restartGame();
                    else {
                        frame.setVisible(false);
                        frame.dispose();
                    }
                }
            } else {
                piecesLabel.setText("Can't let you do that");
            }
        }
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGame) {
            restartGame();
        } else if (e.getSource() == exit) {
            frame.setVisible(false);
            frame.dispose();
        }
    }

    // Add board squares to the panel
    public void addBoardToPanel(Board b, JPanel p) {
        for (int i=0;i<8;i++) {
            for (int j=0; j<8;j++) {
                Square sq = b.getSquare(i, j);
                sq.addMouseListener(this);
                JPanel ContainerPanel = new JPanel(new FlowLayout());
                ContainerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderWidth));
                ContainerPanel.add(sq);
                if (sq.getBackgroundColor() == Square.BackgroundColor.DARK)
                    ContainerPanel.setBackground(Color.DARK_GRAY);
                else
                    ContainerPanel.setBackground(Color.LIGHT_GRAY);
                p.add(ContainerPanel);
            }
        }
    }

    // Update the status label with the number of remaining checkers
    public void updateStatus() {
        piecesLabel.setText("Red pieces left: " + redCheckersLeft + "             Black pieces left: " + blackCheckersLeft);
    }

    // Determine if there is a winner
    public String winner() {
        if (blackCheckersLeft == 0)
            return "Red has won by taking Black's pieces!";
        if (redCheckersLeft == 0)
            return "Black has won by taking Red's pieces!";
        boolean redCanMove = false;
        boolean blackCanMove = false;
        for (int i=0; i<8;i++) {
            for (int j=0; j<8;j++) {
                if (board.getSquare(i, j).isOccupied()) {
                    Vector<Square> potentialMoves = board.getPossibleMoves(board.getSquare(i, j).getOccupant());
                    if (!potentialMoves.isEmpty()) {
                        if (board.getSquare(i, j).getOccupant().getColor() == Color.black)
                            blackCanMove = true;
                        else
                            redCanMove = true;
                    }
                }
            }
        }
        if (redCanMove && !blackCanMove) {
            return "Red wins since Black cannot move!";
        } else if (blackCanMove && !redCanMove) {
            return "Black wins since Red cannot move!";
        } else if (!redCanMove && !blackCanMove) {
            return "Neither side can make a move!";
        }
        return null;
    }

    // Switch turns between Black and Red
    public void endTurn() {
        if (currentTurn == Color.BLACK) {
            currentTurn = Color.RED;
        } else {
            currentTurn = Color.BLACK;
        }
    }

    // Restart the game
    public void restartGame() {
        frame.setVisible(false);
        selectedSquare = null;
        frame.remove(boardpanel);
        boardpanel = new JPanel(new GridLayout(8, 8));
        boardpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        board = new Board();
        board.placeStartingPieces();
        addBoardToPanel(board, boardpanel);
        frame.add(boardpanel, 0);
        redCheckersLeft = 12;
        blackCheckersLeft = 12;
        currentTurn = Color.BLACK;
        updateStatus();
        frame.pack();
        frame.setVisible(true);
    }

    // Main method to run the application
    public static void main(String[] args) {
        new SimpleCheckersGUI();
    }
}
