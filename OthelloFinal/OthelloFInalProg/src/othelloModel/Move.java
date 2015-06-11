package othelloModel;

/**
 * Value class used to encode updates to board
 * @author Alan Dearle
 */

public class Move {
	
	public int row;
	public int col;

	public Move(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
}
