package othelloGUI;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import othelloModel.Game;
import Interfaces.IGame;
import Interfaces.IOthelloFrame;

public class OthelloFrame extends JFrame implements IOthelloFrame{
	private final int STATS_PANEL_WIDTH=150; //the width of the stats panel
	private final int SQUARE_SIZE=80; //the width (and height) of each square
	private OthelloPanel board_panel; //the panel which displays the state of the game
	private StatsPanel stats_panel; //the panel which displays any messages to the user (scores and whos turn etc)
	private Game model; //the model of the game
	
	public OthelloFrame(IGame model){
		//sets up the frame
		super("Othello");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		
		//creates the components to put in the frame
		this.board_panel=new OthelloPanel(model, SQUARE_SIZE);
		this.stats_panel=new StatsPanel(model, STATS_PANEL_WIDTH, SQUARE_SIZE*model.getBoardWidth(), this);
		
		//adds the components to the frame
		Container container = this.getContentPane();
		container.add(board_panel, "Center");
		container.add(stats_panel, "East");
		
		//finally displays the frame
		this.setVisible(true);
		this.setSize(new Dimension(model.getBoardWidth()*SQUARE_SIZE+STATS_PANEL_WIDTH + this.getInsets().left*2, model.getBoardHeight()*SQUARE_SIZE+this.getInsets().top+this.getInsets().bottom));
		this.setVisible(true);
		update();
	}
	
	//updates the board panel to display the current game state
	public void update() {
		board_panel.update();
		stats_panel.update();
	}

	//Deprecated method. Message box replaced with stats panel.
	public void updateMsg() {}

	//window listener methods
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {System.exit(0);}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
}
