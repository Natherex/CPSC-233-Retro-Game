package gamestate;

import board.ChessBoard;
import java.util.Arrays;

public class GameState {
	private boolean whiteCastleLeft;
	private boolean whiteCastleRight;
	private boolean blackCastleLeft;
	private boolean blackCastleRight;
	private int gameState;
	private boolean isWhiteTurn = true;
	private boolean isBlackTurn = false;
	private int[] fillersLocation;

	public GameState() {
		whiteCastleLeft = true;
		whiteCastleRight = true;
		blackCastleLeft = true;
		blackCastleRight = true;
		gameState = 0;
	}

	public GameState(GameState gs) {
		this.whiteCastleLeft = gs.whiteCastleLeft;
		this.whiteCastleRight = gs.whiteCastleRight;
		this.blackCastleLeft = gs.blackCastleLeft;
		this.blackCastleRight = gs.blackCastleRight;
		this.gameState = gs.gameState;
		this.isWhiteTurn = gs.isWhiteTurn;
		this.isBlackTurn = gs.isBlackTurn;
	}
	/**
	 *  Changes the current colors turn
	 */
	public void changeTurn() {
		isWhiteTurn = !isWhiteTurn;
		isBlackTurn = !isBlackTurn;
	}
	/**
	 * checks whos turn it is
	 * @return returns true if it is whites turn
	 */
	public boolean isWhiteTurn() {
		return isWhiteTurn;
	}

	/**
	 * checks whos turn it is
	 * @return returns true if it is blacks turn
	 */
	public boolean isBlackTurn() {
		return isBlackTurn;
	}

	/**
	 * checks what state the game is currently in
	 * @return returns an integer that corresponds with a gamestate
	 * 			- 1 is check , 2 is checkmate, 3 is stalemate, 0 is normal
	 */
	public int getGameState() {
		return gameState;
	}

	/**
	 * Updates the current game state
	 * Sets the gamestate to an integer that corresponds with a gamestate
	 * 			- 1 is check , 2 is checkmate, 3 is stalemate, 0 is normal
	 * @param c Chessboard that needs to be analyzed
	 * @param color color of the current player
	 */
	public void updateGameState(ChessBoard c, String color) {
		if (isCheck(c,color)) {
			if (isCheckmate(c, fillersLocation, color)) {
				gameState = 2;
			} else {
				gameState = 1;
			}
		} else if (isStaleMate(c)) {
			gameState = 3;
		} else {
			gameState = 0;
		}

		// Updates the castle left only if it's currently true since once it's false you don't need to update anymore.
		if (!whiteCastleLeft) {
			whiteCastleLeft = isLeftCastleLegal(c);
		}

		// Updates the castle right only if it's currently true since once it's false you don't need to update anymore.
		if (!whiteCastleRight) {
			whiteCastleRight = isRightCastleLegal(c);
		}

		if (!blackCastleLeft) {
			blackCastleLeft = isLeftCastleLegal(c);
		}

		if (!blackCastleRight) {
			blackCastleRight = isRightCastleLegal(c);
		}
	}

	/**
	 * Checks if the chessboard is currently in the state of checkmate for the current player
	 * Assumes king is in check and decides if it is actually a checkmate.
	 * @param c Chessboard to be analyzed
	 * @param checkersLocation location of the piece that has caused check
	 * @param color color of the current player
	 * @return true if chessboard is in checkmate
	 */
	public boolean isCheckmate(ChessBoard c, int[] checkersLocation, String color)
	{
		if(!isCheck(c,color) || canKingMove(c, color) || (!doubleCheck(c) && canKingBeBlocked(c, checkersLocation,color)) || (!doubleCheck(c) && canCheckerBeTaken(c, checkersLocation,color)))
			return false;
		return true;
	}
	//returns true if in check or if in checkmate
	/**
	 *	Checks if the chessboard is currently in the state of check for the current player
	 * @param c Chessboard to be analyzed
	 * @param color color of the current player
	 * @return if the player is currently in check
	 *
	 */
	public boolean isCheck(ChessBoard c, String color)
	{
		String oColor;
		if (color == "w")
			oColor ="b";
		else
			oColor = "w";

		if (canTileBeFilled(c,findKing(c,color),oColor))
			return true;

		return false;
	}
	public boolean kingIsSafe(ChessBoard c, String start, String end, String playersColor)
	{
		ChessBoard temp = new ChessBoard(c);
		int[] startCoordinate = c.parseLocation(start);
		int startY = startCoordinate[0];
		int startX = startCoordinate[1];

		int[] endCoordinate = c.parseLocation(end);
		int endY = endCoordinate[0];
		int endX = endCoordinate[1];


		if(temp.getGrid()[endY][endX] != null && !temp.getGrid()[endY][endX].getColor().equals(playersColor))
			temp.removePiece(end);
		if(temp.getGrid()[endY][endX] == null)
			temp.forcedMove(start,end);
		if(isCheck(temp,playersColor))
		{
			System.out.println("false");
			return false;
		}else
		{
			return true;
		}
	}

	/**
	 * Checks if the Chessboard is in stalemate for the current player
	 * @param c Chessboard to be analyzed
	 * @return if the player is currently in stalemate
	 *
	 */
	public boolean isStaleMate(ChessBoard c)
	{
		return false;
	}

	/**
	 * Checks if the king has any valid moves
	 * @param c Chessboard to be analyzed
	 * @return if the current players king can move
	 *
	 */
	public boolean canKingMove(ChessBoard c , String color)
	{
		int[] kingsLocation = findKing(c, color);
		int[] temp;

		for(int i = -1 ; i< 2;i++)
		{
			for(int j = -1 ; j < 2; j++)
			{
				if(kingsLocation[0] + j < 8 && kingsLocation[0] + j >= 0)
				{
					if(kingsLocation[1] + i < 8 && kingsLocation[1] + i >= 0)
					{
						temp = new int[] {kingsLocation[0]+j,kingsLocation[1]+i};
						//if((c.getGrid()[temp[0]][temp[1]] == null || c.getGrid()[temp[0]][temp[1]].getColor().equals(c.oppositePlayer()))
						//		&& !canTileBeFilled(c, temp,c.oppositePlayer()))
						if(kingIsSafe(c,c.unparseLocation(kingsLocation),c.unparseLocation(temp),color))
							return true;
					}
				}
			}
		}
		return false;

	}
	/**
	 *	Finds the king on the chessboard
	 * @param c Chessboard to be analyzed
	 * @param color color of the current player
	 * @return location of the king(y,x)
	 *
	 */
	public int[] findKing(ChessBoard c, String color)
	{
		int[] coordinates = new int[2];
		for (int row = 0; row < c.getHeight(); row++)
		{
			for (int column = 0; column < c.getLength(); column++)
			{
				if(c.getGrid()[row][column] != null && c.getGrid()[row][column].getName().equals("King") && c.getGrid()[row][column].getColor().equals(color))
				{
					coordinates[0] = row;
					coordinates[1] = column;
					return coordinates;
				}
			}
		}
		return null;

	}
	/**
	 *	Checks to see if another piece can save the king from check/checkmate
	 * @param c Chessboard to be analyzed
	 * @param color color of the current player
	 * @param checkersLocation location of the piece that has caused check
	 * @return if the king can be saved with another piece
	 *
	 */
	public boolean canKingBeBlocked(ChessBoard c,int[] checkersLocation, String color)
	{
		int[] kingsLocation = new int[2];
		kingsLocation = findKing(c, color);
		if(c.getGrid()[checkersLocation[0]][checkersLocation[1]].getName().equals("Knight"))
		{
			return false;
		}
		if(c.getGrid()[checkersLocation[0]][checkersLocation[1]].getName().equals("Pawn"))
		{
			return false;
		}
		while(!Arrays.equals(kingsLocation, checkersLocation))
		{
			if(canTileBeFilled(c,checkersLocation,color))
			{
				return true;
			}
			if(checkersLocation[0]<kingsLocation[0])
			{
				checkersLocation[0]++;
			}
			else if(checkersLocation[0]>kingsLocation[0])
			{
				checkersLocation[0]--;
			}
			if(checkersLocation[1]<kingsLocation[1])
			{
				checkersLocation[1]++;
			}else if(checkersLocation[1]>kingsLocation[1])
			{
				checkersLocation[1]--;
			}
		}

		return false;
	}
	/**
	 *	Checks if the specified tile can be filled by another piece
	 * @param c Chessboard to be analyzed
	 * @param color color of the current player
	 * @param coordinate location of the piece that wants to be checked
	 * @return if the king can be saved with another piece
	 *
	 */
	public boolean canTileBeFilled(ChessBoard c, int[] coordinate, String color)
	{
		//check if knight can fill tile
		if(coordinate[0]>=1 && coordinate[1]>=2 && c.getGrid()[coordinate[0]-1][coordinate[1]-2] != null)
		{
			if(c.getGrid()[coordinate[0]-1][coordinate[1]-2].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]-1][coordinate[1]-2].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] - 1, coordinate[1] - 2};
					return true;
				}
			}
		}
		if(coordinate[0]>=2 && coordinate[1]>=1 && c.getGrid()[coordinate[0]-2][coordinate[1]-1] != null)
		{
			if(c.getGrid()[coordinate[0]-2][coordinate[1]-1].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]-2][coordinate[1]-1].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] - 2, coordinate[1] - 1};
					return true;
				}
			}
		}
		if(coordinate[0]<7 && coordinate[1]>= 2 && c.getGrid()[coordinate[0]+1][coordinate[1]-2] != null)
		{
			if(c.getGrid()[coordinate[0]+1][coordinate[1]-2].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]+1][coordinate[1]-2].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] + 1, coordinate[1] - 2};
					return true;
				}
			}
		}
		if(coordinate[0]<6 && coordinate[1]>= 1 && c.getGrid()[coordinate[0]+2][coordinate[1]-1] != null)
		{
			if(c.getGrid()[coordinate[0]+2][coordinate[1]-1].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]+2][coordinate[1]-1].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] + 2, coordinate[1] - 1};
					return true;
				}
			}
		}
		if(coordinate[0]>=1 && coordinate[1] <6 && c.getGrid()[coordinate[0]-1][coordinate[1]+2] != null)
		{
			if(c.getGrid()[coordinate[0]-1][coordinate[1]+2].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]-1][coordinate[1]+2].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] - 1, coordinate[1] + 2};
					return true;
				}
			}
		}
		if(coordinate[0]>=2 && coordinate[1] <7 && c.getGrid()[coordinate[0]-2][coordinate[1]+1] != null)
		{
			if(c.getGrid()[coordinate[0]-2][coordinate[1]+1].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]-2][coordinate[1]+1].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] - 2, coordinate[1] + 1};
					return true;
				}
			}
		}
		if(coordinate[0]<7 && coordinate[1] <6 && c.getGrid()[coordinate[0]+1][coordinate[1]+2] != null)
		{
			if(c.getGrid()[coordinate[0]+1][coordinate[1]+2].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]+1][coordinate[1]+2].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] + 1, coordinate[1] + 2};
					return true;
				}
			}
		}
		if(coordinate[0]<6 && coordinate[1] <7 && c.getGrid()[coordinate[0]+2][coordinate[1]+1] != null)
		{
			if(c.getGrid()[coordinate[0]+2][coordinate[1]+1].getName().equals("Knight"))
			{
				if(c.getGrid()[coordinate[0]+2][coordinate[1]+1].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] + 2, coordinate[1] + 1};
					return true;
				}
			}
		}

		//check if pawn can fill tile
		if(coordinate[0]<0 && c.getGrid()[coordinate[0]-1][coordinate[1]] != null)
		{
			if(c.getGrid()[coordinate[0]-1][coordinate[1]].getName().equals("Pawn"))
			{
				if(c.getGrid()[coordinate[0]-1][coordinate[1]].getColor().equals(color)) {
					fillersLocation = new int[]{coordinate[0] - 1, coordinate[1]};
					return true;
				}
			}
		}
		//check right
		int i = 1;
		boolean open = true;
		while(coordinate[1]+i < 8 && open)
		{

			if(c.getGrid()[coordinate[0]][coordinate[1]+i] != null)
			{
				if(c.getGrid()[coordinate[0]][coordinate[1]+i].getName().equals("Rook")
						|| c.getGrid()[coordinate[0]][coordinate[1]+i].getName().equals("Queen") )
				{
					if(c.getGrid()[coordinate[0]][coordinate[1]+i].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0] , coordinate[1] + i};
						return true;
					}
					else
						break;
				}else
					break;

			}
			i++;
		}
		//check up
		i = 1;

		while(coordinate[0]+i<8 && open)
		{

			if(c.getGrid()[coordinate[0]+i][coordinate[1]] != null)
			{
				if(c.getGrid()[coordinate[0]+i][coordinate[1]].getName().equals("Rook")
						|| c.getGrid()[coordinate[0]+i][coordinate[1]].getName().equals("Queen") )
				{
					if(c.getGrid()[coordinate[0]+i][coordinate[1]].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0] + i, coordinate[1]};
						return true;
					}
					else
						break;
				}else
					break;

			}
			i++;
		}
		//check left
		i = 1;

		while(coordinate[1]-i>=0 && open)
		{

			if(c.getGrid()[coordinate[0]][coordinate[1]-i] != null) {
				if (c.getGrid()[coordinate[0]][coordinate[1] - i].getName().equals("Rook")
						|| c.getGrid()[coordinate[0]][coordinate[1] - i].getName().equals("Queen")) {
					if (c.getGrid()[coordinate[0]][coordinate[1] - i].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0], coordinate[1] -i};
						return true;
					}
					else
						break;
				}else
					break;

			}
			i++;
		}
		//check to down
		i = 1;

		while(coordinate[0]-i >= 0 && open)
		{

			if(c.getGrid()[coordinate[0]-i][coordinate[1]] != null) {
				if (c.getGrid()[coordinate[0] - i][coordinate[1]].getName().equals("Rook")
						|| c.getGrid()[coordinate[0] - i][coordinate[1]].getName().equals("Queen")) {
					if (c.getGrid()[coordinate[0] - i][coordinate[1]].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0] - i, coordinate[1]};
						return true;
					}
					else
						break;
				}else
					break;

			}
			i++;
		}
		//check top right
		i = 1;

		while(coordinate[0]+i < 8 && coordinate[1]+i < 8 &&  open)
		{

			if(c.getGrid()[coordinate[0]+i][coordinate[1]+i] != null) {
				if (c.getGrid()[coordinate[0] + i][coordinate[1] + i].getName().equals("Bishop")
						|| c.getGrid()[coordinate[0] + i][coordinate[1] + i].getName().equals("Queen")) {
					if (c.getGrid()[coordinate[0] + i][coordinate[1] + i].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0] + i, coordinate[1] + i};
						return true;
					}
					else
						break;
				}else
					break;

			}
			i++;
		}
		//check top left
		i = 1;

		while(coordinate[0]-i >= 0 && coordinate[1]+i < 8 &&  open)
		{

			if(c.getGrid()[coordinate[0]-i][coordinate[1]+i] != null) {
				if (c.getGrid()[coordinate[0] - i][coordinate[1] + i].getName().equals("Bishop")
						|| c.getGrid()[coordinate[0] - i][coordinate[1] + i].getName().equals("Queen")) {
					if (c.getGrid()[coordinate[0] - i][coordinate[1] + i].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0] - i, coordinate[1] + i};
						return true;
					}
					else
						break;
				}else
					break;

			}
			i++;
		}
		//check bottom left
		i = 1;

		while(coordinate[0]-i >= 0 && coordinate[1]-i >= 0 &&  open)
		{

			if(c.getGrid()[coordinate[0]-i][coordinate[1]-i] != null) {
				if (c.getGrid()[coordinate[0] - i][coordinate[1] - i].getName().equals("Bishop")
						|| c.getGrid()[coordinate[0] - i][coordinate[1] - i].getName().equals("Queen")) {
					if (c.getGrid()[coordinate[0] - i][coordinate[1] - i].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0] - i, coordinate[1] - i};
						return true;
					}
					else
						 break;
				}else
					break;

			}
			i++;
		}
		//check bottom right
		i = 1;

		while(coordinate[0]+i < 8 && coordinate[1]-i >= 0 &&  open)
		{

			if(c.getGrid()[coordinate[0]+i][coordinate[1]-i] != null) {
				if (c.getGrid()[coordinate[0] + i][coordinate[1] - i].getName().equals("Bishop") || c.getGrid()[coordinate[0] + i][coordinate[1] - i].getName().equals("Queen")) {
					if (c.getGrid()[coordinate[0] + i][coordinate[1] - i].getColor().equals(color)) {
						fillersLocation = new int[]{coordinate[0] + i, coordinate[1] - i};
						return true;
					}
					else
						break;
				}else
					break;



			}
			i++;
		}
		return false;

	}

	public boolean canCheckerBeTaken(ChessBoard c, int[] checkersLocation, String color)
	{
		return false;
	}
	public boolean doubleCheck(ChessBoard c)
	{
		return false;
	}

	/**
	 * 


	 */

	public boolean isLeftCastleLegal(ChessBoard c) {

		//Checks if white king is still in starting spot and hasn't made any moves yet
		if ( ((c.getGrid()[0][4]).getTimesMoved() == 0) && (((c.getGrid()[0][4]).getColor()).equals("w")) && (((c.getGrid()[0][4]).getName()).equals("King")) ) {

			//Checks if the white rook on the left is still in starting spot and hasn't made moves yet
			if ( ((c.getGrid()[0][0]).getTimesMoved() == 0) && (((c.getGrid()[0][0]).getColor()).equals("w")) && (((c.getGrid()[0][0]).getName()).equals("Rook")) ) {

				//Checks if the space between king and left rook is clear
				if (c.isWayClear("D1", "B1")) {
				
					return true;
				}

			} 
		}
	/*
		else if ( (c.getGrid()[7][4].getTimesMoved() == 0) && (((c.getGrid()[7][4]).getColor()).equals("b")) && (((c.getGrid()[7][4]).getName()).equals("King")) ) {

			if ( (c.getGrid()[7][7].getTimesMoved() == 0) && (((c.getGrid()[7][7]).getColor()).equals("b")) && (((c.getGrid()[7][7]).getName()).equals("Rook")) ) {

				if (c.isWayClear("F8", "G8")) {

					return true;
				}
			}
		}
	*/
		return false;
	}

	public boolean isRightCastleLegal(ChessBoard c) {

		if ( (c.getGrid()[0][4].getTimesMoved() == 0) && ((c.getGrid()[0][4].getColor()).equals("w")) && ((c.getGrid()[0][4].getName()).equals("King")) ) {

			if ( (c.getGrid()[0][7].getTimesMoved() == 0) && ((c.getGrid()[0][7].getColor()).equals("w")) && ((c.getGrid()[0][7].getName()).equals("Rook") )) {

				if (c.isWayClear("F1", "G1")) {

					return true;
				}
			}
		}

		else if ( (c.getGrid()[7][4].getTimesMoved() == 0) && ((c.getGrid()[7][4].getColor()).equals("b")) && ((c.getGrid()[7][4].getName()).equals("King")) ) {

			if ( (c.getGrid()[7][0].getTimesMoved() == 0) && ((c.getGrid()[7][0].getColor()).equals("b")) && ((c.getGrid()[7][0].getName()).equals("Rook")) ) {

				if (c.isWayClear("D8", "B8")) {

					return true;
				}
			}
		}
		return false;
	}

	public boolean getWhiteCastleLeft() {

		return this.whiteCastleLeft;

	}

	public boolean getWhiteCastleRight() {

		return this.whiteCastleRight;

	}

	public boolean getBlackCastleLeft() {

		return this.blackCastleLeft;

	}
	
	public boolean getBlackCastleRight() {

		return this.blackCastleRight;

	}
}

