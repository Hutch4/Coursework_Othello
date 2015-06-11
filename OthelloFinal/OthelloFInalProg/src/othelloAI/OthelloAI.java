package othelloAI;

import java.awt.Point;

import othelloModel.Board;
import othelloModel.SquareContents;

public class OthelloAI extends MoveNode{
	private MoveNode best_move;
	private int best_score;
	private MoveNode last_move;
	
	public OthelloAI(Board board, int x, int y, SquareContents current_turn, SquareContents who_is_max, MoveNode parent_node) {
		super(board, x, y, current_turn, who_is_max, parent_node);
	}
	
	public void updateBestMove(int test_move_score, MoveNode test_move_node){
		if (test_move_score>best_score){
			best_score=test_move_score;
			best_move=test_move_node;
		}
	}
	
	public void updateLastMove(int row, int col){
		last_move=last_move.getChildNode(row,col);
	}
	
	public Point getBestMove(){
		return (new Point(best_move.getX(), best_move.getY()));
	}
}
