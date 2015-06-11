package othelloModel ;

import java.io.File;
import java.io.IOException;
import java.util.Observable;

import util.PropertiesWrapper;

import Interfaces.IBoard;
import Interfaces.IGame;
import Interfaces.IOthelloFrame;
import factory.BoardFactory;
import factory.GUIFactory;

/**
 * Keeps track of the board state, whose turn it is and
 * whether it is game over
 */
public final class Game extends Observable implements IGame  {
	private IBoard board;
	public static IOthelloFrame gui;
	private boolean can_play_black = true;
	private boolean can_play_white = true;
	/**
	 * Black plays first
	 */
	private boolean whiteToPlay = false ;
	
	/**
	 * Don't allow further moves after game over
	 */
	private boolean gameOver = false ;
	
	/**
	 * Initialise board. Black plays first
	 */
	public Game( PropertiesWrapper pw) {
		board = BoardFactory.makeBoard( pw );
	}
	
	/**
	 * Get the board
	 */
	public IBoard getBoard() {	
		return board;
	}
	
	/**
	 * Report whose turn it is, display board
	 */
	public String toString() {
		StringBuffer result = new StringBuffer(board.toString()) ;
		if (whiteToPlay) result.append("White to play\n") ;
		else result.append("Black to play\n") ;
		return result.toString() ;
	}
	
	/**
	 * Accessor for board height
	 */
	public int getBoardHeight() { return board.getHeight() ; }
	
	/**
	 * Accessor for board width
	 */
	public int getBoardWidth() { return board.getWidth() ; }
	
	/* (non-Javadoc)
	 * @see othelloModel.iGame#isGameOver()
	 */
	@Override
	public boolean isGameOver() { return gameOver ; }
	
	/* (non-Javadoc)
	 * @see othelloModel.iGame#isWhiteToPlay()
	 */
	@Override
	public boolean isWhiteToPlay() { return whiteToPlay ; }
	
	/**
	 * Accessor for contents of a particular square
	 */
	public SquareContents getSquareContents(int row, int col) {
		return board.getSquareContents(row, col)  ;
	}
	
	/**
	 * Resets the board and game state
	 */
	public void reset() {
		board.reset() ;
		whiteToPlay = false ;
		gameOver = false ;
	}
	
	private boolean make_move(int row, int col) {
		//System.out.println("attempting move");
		if (gameOver) {
			System.out.println("Attempt to play move when game over") ;
			return false ;
		}
		SquareContents piece = (whiteToPlay) ?  
				SquareContents.WHITE_PIECE : SquareContents.BLACK_PIECE ;
		if (board.play(piece, row, col)) {
			if (board.hasMove(piece.oppositeColour())) {
				whiteToPlay = !whiteToPlay ;
			}
			else if (!board.hasMove(piece)) {
				gameOver = true ;
			}
			return true ;
		}
		return false ;
	}
	
	public boolean play(int row, int col) {
		boolean result = make_move(row,col);
		if( result ) {
			setChanged();
			notifyObservers( new Move( row, col ) );
			gui.update();
		}
		return result;
	}
	
	
	// used remotely to avoid propogation of notifications
	public boolean remote_play(int row, int col) {
		gui.update();
		return make_move(row,col);
	}
	
	/**
	 * Intended for testing
	 */
	public static void main(String[] args) {
		PropertiesWrapper pw;
		
		try {
			
			pw = new PropertiesWrapper( new File( "othello.properties" ) );
			IGame game = new Game(pw) ;
			Game.gui = GUIFactory.makeGUI(pw, game);
			
		} catch ( IOException e ) {
			System.out.println( "Cannot open properties file: othello.properties" );
		}

	}

	public void setMode(String string) {
		
		if(string.equals("server")){
			can_play_white = false;
		}else if(string.equals("client")){
			can_play_black = false;			
		}
		
	}

	public boolean isCan_play_black() {
		return can_play_black;
	}

	public boolean isCan_play_white() {
		return can_play_white;
	}
	
	
	
	
	
}