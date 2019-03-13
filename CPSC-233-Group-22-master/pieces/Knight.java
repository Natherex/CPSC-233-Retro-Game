package pieces;

import board.*;
import javafx.scene.image.Image;

public class Knight extends Piece {
    private Image icon;
    
    public Knight() {
        super("w", "Knight");
        
        //If the color is white, set the icon of the piece to the white knight, otherwise it'll be the black knight.
	    if (getColor().equals("w"))
	        setIcon(new Image("/assets/Chess_nlt60.png"));
	    else
	        setIcon(new Image("/assets/Chess_ndt60.png"));
    }

    public Knight(String color) {
        super(color, "Knight");
        
        //If the color is white, set the icon of the piece to the white knight, otherwise it'll be the black knight.
	    if (getColor().equals("w"))
	        setIcon(new Image("/assets/Chess_nlt60.png"));
	    else
	        setIcon(new Image("/assets/Chess_ndt60.png"));
    }

    public String toString() {
        return "Kn(" + getColor() + ")";
    }

    /**
     * Tests if move is a valid move on a given chess board.
     * @param c Needs a chess board that the pawn is on.
     * @param start Starting location of the piece on the chess board.
     * @param end Ending location of the piece on the chess board.
     * @return Returns true if the piece can make the move given,
     *         returns false otherwise.
     */
    public boolean isValidMove(ChessBoard c, String start, String end) {
    	int[] totalDistance = c.distance(start, end);
    	boolean valid = false;

        if (totalDistance == null)
            return false;

        int xDirection = totalDistance[1];
        int yDirection = totalDistance[0];

        // Can move two spaces forwards or backwards and one left or right
        if (Math.abs(xDirection) == 2 && Math.abs(yDirection) == 1) {
            c.removePiece(end);
            valid = true;
        }


        // Can move one space forward or backward and two left or right
        else if (Math.abs(xDirection) == 1 && Math.abs(yDirection) == 2) {
            c.removePiece(end);
            valid = true;
        }

        	
        if (valid) {
        	incrementTimesMoved();
        	return true;
        }

        return false;
    }
}