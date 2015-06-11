package othelloModel;
import java.awt.Point;
import java.util.ArrayList;

import Interfaces.IBoard;

public class Board implements IBoard, Cloneable{
	private int black_score; //score of the black player
	private int white_score; //score of the white player
	private final int SIZE=8; //size of the board
	private SquareContents[][] squares; //2D array of all the pieces on the board
	private ArrayList<Point> black_moves= new ArrayList<Point>(); //all the available black moves
	private ArrayList<Point> white_moves= new ArrayList<Point>(); //all the available white moves
	
	public Board(SquareContents[][] squares){
		this.squares=squares;
	}
	
	public Board(){
		reset();
	}
	
	//getter for the height of the board
	public int getHeight() {
		return SIZE;
	}

	//getter for the width of the board
	public int getWidth() {
		return SIZE;
	}

	//sets up the board to way it should be at the beginning of the game
	public void reset() {
		//initializes the board
		squares=new SquareContents[SIZE][SIZE];
		
		//sets all of the squares on the board to empty
		for (int i = 0; i < SIZE*SIZE; i++)
			squares[i%SIZE][i/SIZE]=SquareContents.EMPTY;
		
		//places the first four initial pieces
		squares[3][4]=SquareContents.BLACK_PIECE;
		squares[4][3]=SquareContents.BLACK_PIECE;
		squares[4][4]=SquareContents.WHITE_PIECE;
		squares[3][3]=SquareContents.WHITE_PIECE;
		
		//sets the initial scores of the two players
		black_score=2;
		white_score=2;
		
		//updates the available moves
		updateAvailableMoves(SquareContents.WHITE_PIECE);
		updateAvailableMoves(SquareContents.BLACK_PIECE);
	}

	//getter for the white score
	public int getWhiteScore() {
		white_score = 0;
		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++){
				if(squares[i][j].isWhite()){
					white_score ++;
				}
			}
		}
		
		return white_score;
	}

	//getter for the black score
	public int getBlackScore() {
		black_score = 0;
		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++){
				if(squares[i][j].isBlack()){
					black_score ++;
				}
			}
		}
		return black_score;
	}

	
	//gets the contents of a specific square
	public SquareContents getSquareContents(int row, int col) {
		return squares[row][col];
	}

	//attempts to play a move if possible
	public boolean play(SquareContents piece, int row, int col) {
		//if the move is valid or not
		boolean is_valid = isValidMove(row, col, piece, true);
		if (is_valid){
			//places the piece down
			squares[row][col]=piece;

			//updates the available moves
			updateAvailableMoves(SquareContents.WHITE_PIECE);
			updateAvailableMoves(SquareContents.BLACK_PIECE);
		}
		return is_valid;
	}

	//returns if the particular color has a move
	public boolean hasMove(SquareContents piece) {
		if (piece.isWhite()){
			return (white_moves.size()>0);
		}else{
			return (black_moves.size()>0);
		}
	}
	
	//returns true if the current player can place a piece in the co-ordinate
	public boolean isValidMove(boolean is_white_to_play, int row, int col){
		//gets the correct list of available moves based on the current players turn
		ArrayList<Point> available_moves=((is_white_to_play)?white_moves:black_moves);
		
		//loops through all of the available moves 
		for(int i = 0; i<available_moves.size(); i++){
			
			// returns true if the attempting move is within the list
			if(available_moves.get(i).x==row && available_moves.get(i).y==col){
				return true;
			}
		}
		return false;
	}
	
	
	//finds all of the available moves
	private void updateAvailableMoves(SquareContents piece) {
		//clears the current list of available moves
		(piece.isWhite()?white_moves:black_moves).clear();
		
		//goes through all of the squares in the array
		for (int a=0; a<SIZE; a++){
			for (int b=0; b<SIZE;b++){
				if (isValidMove(a, b, piece, false)){
					(piece.isWhite()?white_moves:black_moves).add(new Point(a,b));
				}
			}
		}
	}
	
	public boolean isValidMove(int row, int col, SquareContents piece, boolean make_move_if_valid){
		boolean[] count_ended={false,false,false,false,true,false,false,false,false};
		boolean move_found=false;
		
		//if the square Isn't empty then it instantly Isn't a valid move
		if(squares[row][col]==SquareContents.EMPTY){
			
			//loops outwards of the current square
			for (int i=1; i<SIZE; i++){
				
				for (int j=0; j<=SIZE; j++){
					
					//gets the co-ordinates of the 8 surrounding squares in turn
					int test_row_multiplier=((j/3)%3)-1;int test_col_multiplier=(j%3)-1;
					int test_row=test_row_multiplier*i+row;int test_col=test_col_multiplier*i+col;
					
					//tests that each 'line' outwards from the current square hasn't reached the end
					if (test_row==SIZE || test_row<0 || test_col==SIZE || test_col<0 ) count_ended[j]=true;
					
					//if the line has reached a piece of same color as the currently square, then its a valid move
					if(count_ended[j]==false && squares[test_row][test_col]==piece && i>1){
						
						count_ended[j]=true;
						move_found=true;
						
						//moves the move if it's a valid move
						if (make_move_if_valid){
							
							squares[row][col]=piece;
							for (int k=i; k>0; k--){
								
								int move_row_multiplier=((j/3)%3)-1;int move_col_multiplier=(j%3)-1;
								int move_row=move_row_multiplier*k+row;int move_col=move_col_multiplier*k+col;
								squares[move_row][move_col]=piece;
							}
						}
						
						
					}else if(count_ended[j]==false && squares[test_row][test_col]!=piece.oppositeColour()) count_ended[j]=true;
				}
			}
		}
		return move_found;
	}
	
	
	public void setSquares(SquareContents[][] squares){
		this.squares=squares;
	}

	public Board clone(){
		SquareContents[][] squares_copy = new SquareContents[SIZE][SIZE];
		for (int i=0; i<SIZE; i++){
			for (int j=0; j<SIZE; j++){
				switch(squares[i][j]){
				case BLACK_PIECE:
					squares_copy[i][j]=SquareContents.BLACK_PIECE;
					break;
				case WHITE_PIECE:
					squares_copy[i][j]=SquareContents.WHITE_PIECE;
					break;
				case EMPTY:
					squares_copy[i][j]=SquareContents.EMPTY;
					break;
				}
			}
		}
		Board copy_board = new Board(squares_copy);
		return (copy_board);
	}

	public SquareContents[][] cloneSquares() {
		SquareContents[][] squares_copy = new SquareContents[SIZE][SIZE];
		  for (int i=0; i<SIZE; i++){
		   for (int j=0; j<SIZE; j++){
		    switch(squares[i][j]){
		    case BLACK_PIECE:
		     squares_copy[i][j]=SquareContents.BLACK_PIECE;
		     break;
		    case WHITE_PIECE:
		     squares_copy[i][j]=SquareContents.WHITE_PIECE;
		     break;
		    case EMPTY:
		     squares_copy[i][j]=SquareContents.EMPTY;
		     break;
		    }
		   }
		  }
		  return squares_copy;
	}
	
}
