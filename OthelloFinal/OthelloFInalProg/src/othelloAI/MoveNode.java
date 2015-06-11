package othelloAI;
import java.util.ArrayList;

import othelloModel.Board;
import othelloModel.SquareContents;

public class MoveNode implements Runnable{
	
	private int x;
	private int y;
	private ArrayList<MoveNode> children = new ArrayList<MoveNode>();
	private MoveNode parent_node;
	private Board board;
	private SquareContents current_turn;
	private SquareContents who_is_max;
	
	public MoveNode(Board board, int x, int y, SquareContents current_turn, SquareContents who_is_max, MoveNode parent_node){
		this.parent_node=parent_node;
		this.x=x;
		this.y=y;
		this.board=board;
		this.current_turn=current_turn;
		this.who_is_max=who_is_max;
		while(ThreadCounter.count>50){}
		(new Thread(this)).start();
	}
	
	public MoveNode getChildNode(int row, int col) {
		for (int i =0; i<children.size(); i++){
			MoveNode child=children.get(i);
			if (child.getX()==row && child.getY()==col){
				return child;
			}
		}
		return null;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	private void updateAvailableMoves(SquareContents piece) {
		//goes through all of the squares in the array
		for (int a=0; a<8; a++){
			for (int b=0; b<8;b++){
				Board copy_board=board.clone();
				if (copy_board.isValidMove(a, b, piece, true)){
					System.out.println("Move found");
					while(ThreadCounter.getCount() > 2000){}
					children.add(new MoveNode(copy_board, a, b, (current_turn.isWhite()?SquareContents.BLACK_PIECE:SquareContents.WHITE_PIECE), who_is_max, this));
				}
			}
		}
	}

	public void run() {
		ThreadCounter.plus();
		updateAvailableMoves(current_turn);
		ThreadCounter.minus();
	}
}
