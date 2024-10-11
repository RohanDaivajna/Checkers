// Author: Rohan Daivajna 
// Roll number: 35
// Title:  GUI Checkers Game
// Start Date: 08-09-2024
// Modified Date: 15-09-2024
// Description: The code implements a simple GUI-based Checkers game in Java using Swing, featuring a playable 8x8 board, piece movement, turn-based mechanics, and game state management.

import java.util.Vector;
import java.awt.*;

public class Board {

    // Constants representing the number of rows and columns on the board
    public static final int rows = 8;
    public static final int cols = 8;

    // 2D array to hold the squares of the board
    private Square[][] gameBoard;

    // Constructor to initialize the board and set up the squares
    public Board() {
        gameBoard = new Square[rows][cols]; // Create an 8x8 board
        boolean lastColor = false; // Tracks the color for alternating pattern
        
        // Loop through each row and column to create and color squares
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Alternate between dark and light squares
                if (lastColor)
                    gameBoard[i][j] = new Square(Square.BackgroundColor.DARK, i, j);
                else
                    gameBoard[i][j] = new Square(Square.BackgroundColor.LIGHT, i, j);
                lastColor = !lastColor; // Flip the color for the next square
            }
            lastColor = !lastColor; // Flip the starting color for the next row
        }
    }

    // Check if a given row and column are within the board's bounds
    public static boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Get the square at a specific row and column
    public Square getSquare(int row, int col) {
        if (inBounds(row, col))
            return gameBoard[row][col];
        return null; // Return null if out of bounds
    }

    // Place the starting pieces on the board for both players
    public void placeStartingPieces() {
        // Place RED pieces on the top three rows on dark squares
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < cols; col++)
                if (getSquare(row, col).getBackgroundColor() == Square.BackgroundColor.DARK)
                    getSquare(row, col).setOccupant(new Piece(Color.RED, row, col));
        
        // Place BLACK pieces on the bottom three rows on dark squares
        for (int row = 5; row < rows; row++)
            for (int col = 0; col < cols; col++)
                if (getSquare(row, col).getBackgroundColor() == Square.BackgroundColor.DARK)
                    getSquare(row, col).setOccupant(new Piece(Color.BLACK, row, col));
    }

    // Get all possible moves for a given piece
    public Vector<Square> getPossibleMoves(Piece p) {
        Vector<Square> possibleMoves = new Vector<Square>(); // Store possible moves
        Color pColor = p.getColor(); // Get the color of the piece
        int row = p.getRow(); // Current row of the piece
        int col = p.getCol(); // Current column of the piece

        // Calculate possible moves for BLACK pieces
        if (Board.inBounds(row - 1, col - 1) && pColor == Color.BLACK) {
            if (!this.getSquare(row - 1, col - 1).isOccupied())
                possibleMoves.add(this.getSquare(row - 1, col - 1)); // Regular move
            else if (Board.inBounds(row - 2, col - 2))
                if (!this.getSquare(row - 2, col - 2).isOccupied() &&
                    (this.getSquare(row - 1, col - 1).getOccupant().getColor() != pColor))
                    possibleMoves.add(this.getSquare(row - 2, col - 2)); // Capture move
        }

        if (Board.inBounds(row - 1, col + 1) && pColor == Color.BLACK) {
            if (!this.getSquare(row - 1, col + 1).isOccupied())
                possibleMoves.add(this.getSquare(row - 1, col + 1)); // Regular move
            else if (Board.inBounds(row - 2, col + 2))
                if (!this.getSquare(row - 2, col + 2).isOccupied() && 
                    (this.getSquare(row - 1, col + 1).getOccupant().getColor() != pColor))
                    possibleMoves.add(this.getSquare(row - 2, col + 2)); // Capture move
        }

        // Calculate possible moves for RED pieces
        if (Board.inBounds(row + 1, col - 1) && pColor == Color.RED) {
            if (!this.getSquare(row + 1, col - 1).isOccupied())
                possibleMoves.add(this.getSquare(row + 1, col - 1)); // Regular move
            else if (Board.inBounds(row + 2, col - 2))
                if (!this.getSquare(row + 2, col - 2).isOccupied() && 
                    (this.getSquare(row + 1, col - 1).getOccupant().getColor() != pColor))
                    possibleMoves.add(this.getSquare(row + 2, col - 2)); // Capture move
        }

        if (Board.inBounds(row + 1, col + 1) && pColor == Color.RED) {
            if (!this.getSquare(row + 1, col + 1).isOccupied())
                possibleMoves.add(this.getSquare(row + 1, col + 1)); // Regular move
            else if (Board.inBounds(row + 2, col + 2))
                if (!this.getSquare(row + 2, col + 2).isOccupied() && 
                    (this.getSquare(row + 1, col + 1).getOccupant().getColor() != pColor))
                    possibleMoves.add(this.getSquare(row + 2, col + 2)); // Capture move
        }

        return possibleMoves; // Return the vector of possible moves
    }

    // Highlight or remove highlighting from possible moves for a given piece
    public void setMovesHighlighted(Piece p, boolean doHighlight) {
        Vector<Square> possibleMoves = getPossibleMoves(p); // Get possible moves
        for (Square highlight : possibleMoves)
            highlight.setHighlight(doHighlight); // Highlight each possible move
    }

    // Move a piece from one square to another
    public boolean move(Square from, Square to) {
        boolean jumpPerformed = false; // Track if a jump (capture) was made
        Piece beingMoved = from.getOccupant(); // Get the piece to move
        int oldRow = from.getRow(), newRow = to.getRow();
        int oldCol = from.getCol(), newCol = to.getCol();
        
        from.setOccupant(null); // Remove the piece from the original square
        beingMoved.setLoc(to.getRow(), to.getCol()); // Update the piece's location
        to.setOccupant(beingMoved); // Place the piece on the new square

        // Check if a jump (capture) was performed
        if (Math.abs(oldRow - newRow) > 1 || Math.abs(oldCol - newCol) > 1) {
            int takeRow = (oldRow + newRow) / 2; // Row of the captured piece
            int takeCol = (oldCol + newCol) / 2; // Column of the captured piece
            Square takeSquare = getSquare(takeRow, takeCol);
            takeSquare.setOccupant(null); // Remove the captured piece
            takeSquare.update(takeSquare.getGraphics()); // Update the square
            jumpPerformed = true; // A jump was made
        }

        from.update(from.getGraphics()); // Update the original square
        to.update(to.getGraphics()); // Update the new square
        return jumpPerformed; // Return if a jump was performed
    }
}
