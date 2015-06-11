package othelloGUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import othelloModel.Board;
import othelloModel.Game;
import othelloModel.SquareContents;
import Interfaces.IBoard;
import Interfaces.IGame;

public class OthelloPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	private final int SQUARE_SIZE;
	private Image white_piece=null, black_piece=null, cross=null, white_piece_transparent=null, black_piece_transparent=null;
	private IGame model;
	//state before the current state of the board
	private IBoard board;
	
	private int animation_speed;
	private int mouse_x_position;
	private int mouse_y_position;
	private boolean start = true;
	
	//used if animation is wanted or not. must be set before run. hard coded.
	private boolean animation_active = false;
	
	
	//current state of the board
	private IBoard newboard;
	
	public OthelloPanel(IGame model, int square_size) {
		this.model=model;
		this.SQUARE_SIZE=square_size;
		board = ((Board)((Game)model).getBoard()).clone();
		
		//sets the size of the panel
		this.setPreferredSize(new Dimension(square_size*model.getBoardWidth(), square_size*model.getBoardHeight()));
		this.setBackground(Color.black);
		
		//loads the applicable images for the GUI
		try {
			this.white_piece = ImageIO.read(new File("images/white_piece.png"));
			this.black_piece = ImageIO.read(new File("images/black_piece.png"));
			this.cross = ImageIO.read(new File("images/cross.png"));
			this.white_piece_transparent = ImageIO.read(new File("images/white_piece_t.png"));
			this.black_piece_transparent = ImageIO.read(new File("images/black_piece_t.png"));
		} catch (IOException e) {System.out.println("Error loading images: " + e.getMessage());
		e.printStackTrace();}
		
		
		//sets up listeners and gets the panel focus
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
	}
	
	
	public void animate(int x, int y, Graphics g, boolean white_play, boolean flip) {
		
		//speed variable increase in speed increases the time to flip
		int speed = 10;
		
		//set background to green rectangle
		g.setColor(Color.GREEN);
		g.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1);

		
		//if whites turn
		if(white_play) {

			//if the parameter to flip from black
			if(flip) {
				for(int i = 1; i <= black_piece.getHeight(null); i ++ ) {
					
					g.drawImage(black_piece, x*SQUARE_SIZE+(i/2), y*SQUARE_SIZE, SQUARE_SIZE-i, SQUARE_SIZE, null);
					
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					g.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1);

				}
				
			}
			
			//expand the white piece

			for(int i = white_piece.getHeight(null); i >= 20; i --) {
				g.drawImage(white_piece, x*SQUARE_SIZE+(i/2), y*SQUARE_SIZE, SQUARE_SIZE-i, SQUARE_SIZE, null);
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				g.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1);

			}

			g.drawImage(white_piece, x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1, null);

			
			//if not white e.g. black
		}else{
			//if the parameter to flip from qhite

			if(flip){
				
				for(int i = 1; i <= white_piece.getHeight(null); i ++ ) {
					
					g.drawImage(white_piece, x*SQUARE_SIZE+(i/2), y*SQUARE_SIZE, SQUARE_SIZE-i, SQUARE_SIZE, null);
					
					try {
						Thread.sleep(speed);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					g.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1);

				}
				
			}
			//expand out the black piece

			for(int i = black_piece.getHeight(null); i >= 20; i --) {
				g.drawImage(black_piece, x*SQUARE_SIZE+(i/2), y*SQUARE_SIZE, SQUARE_SIZE-i, SQUARE_SIZE, null);
				//g.drawI
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				g.fillRect(x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1);

			}
			//make sure fianl correctly size picture is draw by redrawing it.
			g.drawImage(black_piece, x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1, null);

		}

		
		
	}

	//called every time the game state changes
	public void update() {
		newboard = (Board) ((Game)model).getBoard();

		//creates a graphic the same size as the board
		Image background_buffer=createImage(this.getWidth(), this.getHeight());

		Graphics background_buffer_graphics = background_buffer.getGraphics();
		
		//sets the background green
		background_buffer_graphics.setColor(Color.green);
		background_buffer_graphics.fillRect(0,0,SQUARE_SIZE*model.getBoardWidth()+1, SQUARE_SIZE*model.getBoardHeight()+1);
		
		//sets the color to black (for grid)
		background_buffer_graphics.setColor(Color.black);

			
			//loops through all of the squares in the 2D array
			for (int i = 0; i < model.getBoardWidth(); i++){
				for (int j = 0; j < model.getBoardHeight(); j++){
					
					//draws a square for each square
					background_buffer_graphics.drawRect(i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1);
					
					//draws each relevant piece on each square 
					switch(((Board) ((Game)model).getBoard()).getSquareContents(i,j)){
					
					case BLACK_PIECE:
						//flip(i, j);
						background_buffer_graphics.drawImage(black_piece, i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
						break;
					case WHITE_PIECE:
						//flip(i, j);
						background_buffer_graphics.drawImage(white_piece, i*SQUARE_SIZE, j*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
						break;
					default:
						break;
					}
				}
			}		
		
		if(animation_active) {
			
			int row = mouse_x_position;
			int col = mouse_y_position;
			boolean onbaord = false;
			
			for (int i=1; i<8; i++){
				
				for (int j=0; j<=8; j++){
					
					//gets the co-ordinates of the 8 surrounding squares in turn
					int test_row_multiplier=((j/3)%3)-1;
					int test_col_multiplier=(j%3)-1;
					int test_row=test_row_multiplier*i+row;
					int test_col=test_col_multiplier*i+col;
					
					onbaord = (test_row >= 8 || test_row < 0 || test_col >= 8 || test_col < 0 );
					
					if(!(test_row == row && test_col == col) && !onbaord){
						//resets the background to a blank rectangle
						//background_buffer_graphics.drawRect(test_row * SQUARE_SIZE, test_col * SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1);
						
						//calls the method to flip the piece if there is a piece on the the board of either black or white
						switch(((Board) ((Game)model).getBoard()).getSquareContents(test_row, test_col)){
						case BLACK_PIECE:

							flip(test_row, test_col);
							break;
						case WHITE_PIECE:

							flip(test_row, test_col);
							
							break;
						default:
							break;
						}
					}
				}
			}
		}

		
		//draws the final outcome of graphics onto the panel
		this.getGraphics().drawImage(background_buffer, 0, 0, null);
		
		//updates the stored board to the last state of the board. Allowing it to allows be the previous status.
			board = ((Board)((Game)model).getBoard()).clone();
	}
	
	public void flip(int x, int y) {
		//IBoard newboard = (Board) ((Game)model).getBoard();
		//if piece has changed since last move animate its flip dependant on if it is white to black or black to white etc
		
		
		if(!board.getSquareContents(x,y).equals(newboard.getSquareContents(x,y))) {	
			System.out.println("match");
			switch(board.getSquareContents(x,y)){
				
			//if black to white, flip from a white piece to a black piece (with animate method)
			case BLACK_PIECE:
				if	(newboard.getSquareContents(x, y) == SquareContents.WHITE_PIECE){
					animate(x,y, this.getGraphics(), true, true);
				}
				break;
				
			//if white to black, flip from a black piece to a white piece (with animate method)
			case WHITE_PIECE:

					if	(newboard.getSquareContents(x, y) == SquareContents.BLACK_PIECE){
						animate(x,y, this.getGraphics(), false, true);
					}				
				break;
				
			
			case EMPTY:

				if	(newboard.getSquareContents(x, y) == SquareContents.BLACK_PIECE){
					animate(x,y, this.getGraphics(), false, false);
					break;
				}	
				
				if	(newboard.getSquareContents(x, y) == SquareContents.WHITE_PIECE){
					animate(x,y, this.getGraphics(), true, false);
				}	
				break;
			}
		}
	}

	//If a the Escape key it pressed, the game exits
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyChar() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
	}

	public void mouseMoved(MouseEvent me) {
		//clears off any previous move indicators
		
		update();
		
		//finds the x and y co-ordinate of the square they are hovering over in terms of array position
		int x = me.getX()/SQUARE_SIZE;
		int y = me.getY()/SQUARE_SIZE;
		
		mouse_x_position = me.getX()/SQUARE_SIZE;
		mouse_y_position = me.getY()/SQUARE_SIZE;
		
		//if whites turn, draws transparent white piece, if can't be played with add red cross to picture
		if (((Game)model).isWhiteToPlay()){
			
			if (((Game)model).isCan_play_white()){
				this.getGraphics().drawImage((white_piece_transparent), x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
				
				if (!((Board) ((Game)model).getBoard()).isValidMove(model.isWhiteToPlay(), x, y)){
					this.getGraphics().drawImage(cross, x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
		
					
					
				}
			}
	
		//if blacks turn, draws transparent white piece, if can't be played with add red cross to picture
		}else{
			
			if (((Game)model).isCan_play_black()){
				this.getGraphics().drawImage((black_piece_transparent), x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
				
				if (!((Board) ((Game)model).getBoard()).isValidMove(model.isWhiteToPlay(), x, y)){
					this.getGraphics().drawImage(cross, x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
				}
			}
			
		}

	}
	
	//indicates that the player wants to try move
	public void mouseClicked(MouseEvent me) {
		//finds the x and y co-ordinate of the square they are trying to move to
		int x = me.getX()/SQUARE_SIZE;
		int y = me.getY()/SQUARE_SIZE;
		
		//tries to play the move
		if (((Game)model).isWhiteToPlay()){
			
			if (((Game)model).isCan_play_white()){
				System.out.println(model.play(x,y));
				//animate(x,y,this.getGraphics(), true, false);
				update();

			}
			
		}else{
			
			if (((Game)model).isCan_play_black()){
				System.out.println(model.play(x,y));
				//animate(x,y,this.getGraphics(), false, false);
				update();

			}
			
		}
		
	}
	
	public void mouseDragged(MouseEvent me) {mouseMoved(me);}
	
	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent ke) {}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mousePressed(MouseEvent me) {}
	public void mouseReleased(MouseEvent me) {}

}
