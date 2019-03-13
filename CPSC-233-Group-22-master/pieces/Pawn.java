package pieces;

import board.ChessBoard;
import javafx.scene.image.Image;

public class Pawn extends Piece {
    private Image icon;

    public Pawn() {
        super("w", "Pawn");

        // If the color is white, set the icon of the piece to the white pawn, otherwise it'll be the black pawn.
        if (getColor().equals("w"))
            setIcon(new Image("/assets/Chess_plt60.png"));
        else
            setIcon(new Image("/assets/Chess_pdt60.png"));

    }

    public Pawn(String color) {
        super(color, "Pawn");

        // If the color is white, set the icon of the piece to the white pawn, otherwise it'll be the black pawn.
        if (getColor().equals("w"))
            setIcon(new Image("/assets/Chess_plt60.png"));
        else
            setIcon(new Image("/assets/Chess_pdt60.png"));
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
        if (totalDistance == null)
            return false;

        int xDirection = totalDistance[1];
        int yDirection = totalDistance[0];

        // Can move two spaces forwards if it's the first move of the pawn and it's not blocked.
        if (getTimesMoved() == 0 && yDirection == 2 && board.isNotBlocked(start, end)) {
            incrementTimesMoved();
            return true;
        }

        // Can move one space forwards
        else if (yDirection == 1 && board.isNotBlocked(start, end)) {
            incrementTimesMoved();
            return true;
        }

        // Can kill piece one up and one right
        else if (yDirection == 1 && xDirection == 1 && board.isBlocked(start, end)) {
            incrementTimesMoved();
            board.removePiece(end);
            return true;
        }

        // Can kill piece one up and one left
        else if (yDirection == 1 && xDirection == -1 && board.isBlocked(start, end)) {
            incrementTimesMoved();
            board.removePiece(end);
            return true;
        }

        return false;
    }

    public String toString() {
        return "Pa(" + getColor() + ")";
    }

}