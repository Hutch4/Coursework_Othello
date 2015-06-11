package othelloAI;

import othelloModel.Board;
import othelloModel.Move;
import othelloModel.SquareContents;

public class ArisiaNode {
	private SquareContents[][] current_state;
	private SquareContents who_max;
	private SquareContents current_turn;
	private int alpha;
	private int beta;
	private int count;
	private double score = 0;
	private Move best_move = null;
	private Move move;

	public ArisiaNode(SquareContents[][] current_state, SquareContents who_max,
			SquareContents current_turn, int count, int alpha, int beta, int x,
			int y) {
		this.current_state = current_state;
		this.who_max = who_max;
		this.current_turn = current_turn;
		this.count = count - 1;
		this.alpha = alpha;
		this.beta = beta;
		move = new Move(x, y);
		best_move = move;
		if (count == 0) {
			calculateScore();
		} else {
			createChildren();
			if (best_move == null) {
				calculateScore();
			}
		}
		// frees up memory
		current_state = null;

		if (x < 8 && y < 8) {
			if (current_turn.equals(who_max)) {
				score = (int) (score / ThreadCounter.getLogic(x, y));
			} else {
				score = (int) (score * ThreadCounter.getLogic(x, y));
			}
		}
	}

	private void createChildren() {
		//initialises all of the variables
		double min_score = 0;
		double max_score = 0;
		Move max_move = null;
		Move min_move = null;
		boolean first = true;
		SquareContents[][] squares_copy;
		ArisiaNode child;
		
		//goes through all of the squares and creates a new node for each of the possible moves
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				//creates a copy of the game state before playing the move
				squares_copy = getCopySquares();
				//if the move is valid
				if (isValidMove(squares_copy, i, j)) {
					//create a new node for the new move
					child = new ArisiaNode(squares_copy, who_max,
							current_turn.oppositeColour(), count, alpha, beta,
							i, j);
					//if its the first node, set the alpha and betas
					if (first) {
						max_score = child.getScore();
						min_score = child.getScore();
						min_move = child.getMove();
						max_move = child.getMove();
						first = false;
					}
					
					//monitor alpha betas and curent best move
					if (child.getScore() > max_score) {
						max_score = child.getScore();
						max_move = child.getMove();
					}
					if (child.getScore() < min_score) {
						min_score = child.getScore();
						min_move = child.getMove();
					}
				}
			}
		}
		//sets the score dependant on if its min or if its maxs turn
		if (current_turn.equals(who_max)) {
			score = max_score;
			best_move = max_move;
		} else {
			score = min_score;
			best_move = min_move;
		}
	}

	private boolean isValidMove(SquareContents[][] squares_copy, int a, int b) {
		boolean[] count_ended = { false, false, false, false, true, false,
				false, false, false };
		boolean move_found = false;
		// if the square Isn't empty then it instantly Isn't a valid move
		if (squares_copy[a][b] == SquareContents.EMPTY) {
			// loops outwards of the current square
			for (int i = 1; i < squares_copy.length; i++) {
				for (int j = 0; j <= squares_copy.length; j++) {
					// gets the co-ordinates of the 8 surrounding squares in
					// turn
					int test_row_multiplier = ((j / 3) % 3) - 1;
					int test_col_multiplier = (j % 3) - 1;
					int test_row = test_row_multiplier * i + a;
					int test_col = test_col_multiplier * i + b;

					// tests that each 'line' outwards from the current square
					// hasn't reached the end
					if (test_row == squares_copy.length || test_row < 0
							|| test_col == squares_copy.length || test_col < 0)
						count_ended[j] = true;

					// if the line has reached a piece of same color as the
					// currently square, then its a valid move
					if (count_ended[j] == false
							&& squares_copy[test_row][test_col] == current_turn
							&& i > 1) {
						count_ended[j] = true;
						move_found = true;

						// moves the move if it's a valid move
						squares_copy[a][b] = current_turn;
						for (int k = i; k > 0; k--) {
							int move_row_multiplier = ((j / 3) % 3) - 1;
							int move_col_multiplier = (j % 3) - 1;
							int move_row = move_row_multiplier * k + a;
							int move_col = move_col_multiplier * k + b;
							squares_copy[move_row][move_col] = current_turn;
						}
					} else if (count_ended[j] == false
							&& squares_copy[test_row][test_col] != current_turn
									.oppositeColour())
						count_ended[j] = true;
				}
			}
		}
		return move_found;
	}

	private SquareContents[][] getCopySquares() {
		SquareContents[][] squares_copy = new SquareContents[current_state.length][current_state.length];
		for (int i = 0; i < current_state.length; i++) {
			for (int j = 0; j < current_state.length; j++) {
				switch (current_state[i][j]) {
				case BLACK_PIECE:
					squares_copy[i][j] = SquareContents.BLACK_PIECE;
					break;
				case WHITE_PIECE:
					squares_copy[i][j] = SquareContents.WHITE_PIECE;
					break;
				case EMPTY:
					squares_copy[i][j] = SquareContents.EMPTY;
					break;
				}
			}
		}
		return squares_copy;
	}

	private void calculateScore() {
		for (int i = 0; i < current_state.length; i++)
			for (int j = 0; j < current_state.length; j++)
				if (current_state[i][j].equals(who_max))
					score++;
	}

	public double getScore() {
		return score;
	}

	public Move getMove() {
		return move;
	}

	public Move getBestMove() {
		return best_move;
	}
}
