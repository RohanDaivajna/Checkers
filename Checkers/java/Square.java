// Author: Rohan Daivajna 
// Roll number: 35
// Title: GUI Checkers Game
// Start Date: 08-09-2024
// Modified Date: 15-09-2024
// Description: The code implements a simple GUI-based Checkers game in Java using Swing, featuring a playable 8x8 board, piece movement, turn-based mechanics, and game state management.

import java.awt.*;

@SuppressWarnings("serial")
public class Square extends Canvas {

    // Enum to represent the square's background color, either light or dark
    public enum BackgroundColor { LIGHT, DARK }

    // Fields to store the background color, occupation status, piece on the square, row, and column
    private final BackgroundColor bgColor;
    private boolean occupied;
    private Piece occupant;
    private final int row;
    private final int col;

    // Constructor to initialize the square with a specific color, row, and column
    public Square(BackgroundColor color, int row, int col) {
        this.setSize(64, 64); // Set the size of each square
        
        // Set the background color based on the provided enum value
        if (color == BackgroundColor.DARK)
            this.setBackground(Color.DARK_GRAY);
        else
            this.setBackground(Color.LIGHT_GRAY);
        
        bgColor = color;
        occupied = false;
        occupant = null;
        this.row = row;
        this.col = col;
    }

    // Check if the square is currently occupied by a piece
    public boolean isOccupied() {
        return occupied;
    }

    // Get the row position of this square on the board
    public int getRow() {
        return row;
    }

    // Get the column position of this square on the board
    public int getCol() {
        return col;
    }

    // Retrieve the background color of the square
    public Square.BackgroundColor getBackgroundColor() {
        return bgColor;
    }

    // Get the piece currently occupying this square, if any
    public Piece getOccupant() {
        return occupied ? occupant : null;
    }

    // Highlight the square to indicate a valid move or selection
    public void setHighlight(boolean highlight) {
        Graphics g = this.getGraphics();
        
        if (highlight) {
            // If the square is empty, draw a circular highlight
            if (!occupied) {
                g.setColor(Color.BLACK);
                for (int i = 0; i < 360; i += 30)
                    g.drawArc(5, 5, 54, 54, i, 15);
            } else {
                // If occupied, draw a yellow border around the piece
                g.setColor(Color.YELLOW);
                g.draw3DRect(0, 0, 63, 63, false);
            }
        } else {
            // Remove the highlight by repainting the square
            super.update(this.getGraphics());
        }
    }

    // Place a piece on this square or remove it
    public void setOccupant(Piece newOccupant) {
        occupant = newOccupant;
        occupied = (newOccupant != null);
    }

    // Paint the square and its occupant, if any, onto the canvas
    @Override
    public void paint(Graphics g) {
        // Ensure the background color is correctly set during painting
        this.setBackground(bgColor == BackgroundColor.DARK ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        
        // Draw the piece if the square is occupied
        if (occupied) {
            g.setColor(occupant.getColor());
            g.fillOval(5, 5, 54, 54); // Draw the piece as a filled oval
        } else {
            // Clear the square if no piece is present
            g.clearRect(0, 0, 64, 64);
        }
    }
}
