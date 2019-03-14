package pieces;

import board.ChessBoard;

public class Knight extends Piece {
    
    public Knight() {
        super("w", "Knight");
        setIconLocation();
    }

    public Knight(String color) {
        super(color, "Knight");
        setIconLocation();
    }

    /**
     * Sets the icon's picture location to the appropriate picture.
     */
    private void setIconLocation() {
        if (getColor().equals("w"))
            setIconLocation("/assets/Chess_nlt60.png");
        else
            setIconLocation("/assets/Chess_ndt60.png");
    }

    public String toString() {
        return "Kn(" + getColor() + ")";
    }

    /**
     * Tests if move is a valid move on a given chess board.
     * @param board Needs a chess board that the pawn is on.
     * @param start Starting location of the piece on the chess board.
     * @param end Ending location of the piece on the chess board.
     * @return Returns true if the piece can make the move given,
     *         returns false otherwise.
     */
    public boolean isValidMove(ChessBoard board, String start, String end) {
    	int[] totalDistance = board.distance(start, end);
    	boolean valid = false;

        if (totalDistance == null)
            return false;

        int xDirection = totalDistance[1];
        int yDirection = totalDistance[0];

        // Can move two spaces forwards or backwards and one left or right
        if (Math.abs(xDirection) == 2 && Math.abs(yDirection) == 1) {
<<<<<<< HEAD
            int[] startCoordinate = board.parseLocation(start);
=======

            int startY = startCoordinate[0];
            int startX = startCoordinate[1];

            int[] endCoordinate = board.parseLocation(end);
            int endY = endCoordinate[0];
            int endX = endCoordinate[1];
            if(!board.getGrid()[startY][startX].getColor().equals(board.getGrid()[endY][endX].getColor()))
            {
	            incrementTimesMoved();
	            board.removePiece(end);
	            return true;
	        }

        }

        // Can move one space forward or backward and two left or right
        else if (Math.abs(xDirection) == 1 && Math.abs(yDirection) == 2) {
            int[] startCoordinate = board.parseLocation(start);
            int startY = startCoordinate[0];
            int startX = startCoordinate[1];

            int[] endCoordinate = board.parseLocation(end);
            int endY = endCoordinate[0];
            int endX = endCoordinate[1];
            if(!board.getGrid()[startY][startX].getColor().equals(board.getGrid()[endY][endX].getColor()))
            {
	            incrementTimesMoved();
	            board.removePiece(end);
	            return true;
	        }
        }

        if (valid) {
        	incrementTimesMoved();
        	return true;
        }

        return false;
    }
}
