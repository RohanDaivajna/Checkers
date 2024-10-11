// Author: Rohan Daivajna 
// Roll number: 35
// Title: GUI Checkers Game
// Start Date: 08-09-2024
// Modified Date: 15-09-2024
// Description: The code implements a simple GUI-based Checkers game in Java using Swing, featuring a playable 8x8 board, piece movement, turn-based mechanics, and game state management.

import java.awt.*;

public class Piece {

    // Fields to store the row, column, and color of the piece
    private int row;
    private int col;
    public Color color;

    // Constructor to initialize the piece with a specific color, row, and column
    public Piece(Color c, int row, int col) {
        color = c; // Set the color of the piece
        this.row = row; // Set the row position of the piece
        this.col = col; // Set the column position of the piece
    }

    // Method to get the row position of the piece
    public int getRow() {
        return row;
    }

    // Method to get the color of the piece
    public Color getColor() {
        return color;
    }

    // Method to get the column position of the piece
    public int getCol() {
        return col;
    }

    // Method to set the location of the piece (update row and column)
    public void setLoc(int row, int col) {
        this.row = row; // Update the row position
        this.col = col; // Update the column position
    }

    // Method to return a string representation of the piece
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        // Append the color of the piece as a string
        if (this.color == Color.BLACK)
            s.append("Black ");
        else
            s.append("Red ");
        
        // Append the position of the piece (row and column)
        s.append("piece at row " + this.getRow() + 
                 ", col " + this.getCol());
        return s.toString(); // Return the full description of the piece
    }
}
