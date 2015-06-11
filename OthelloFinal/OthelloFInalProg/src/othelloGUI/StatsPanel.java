package othelloGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import othelloAI.ArisiaNode;
import othelloAI.OthelloAI;
import othelloAI.ThreadCounter;
import othelloAI.WebBroker;
import othelloModel.Board;
import othelloModel.Game;
import othelloModel.SquareContents;
import Interfaces.IGame;

public class StatsPanel extends JPanel implements ActionListener {
	private IGame model; // the game which the stats panel is displaying the
							// information on
	private JButton ai_button = new JButton("Arisia Move");

	private JPanel stats = new JPanel();
	private JPanel buttons = new JPanel();
	private WebBroker web_broker;
	private OthelloFrame parent_frame;

	public StatsPanel(IGame model, int panel_width, int panel_height, OthelloFrame parent_frame) {
		this.model = model;
		this.parent_frame = parent_frame;
		// sets up the sizes of the panels
		this.setPreferredSize(new Dimension(panel_width, panel_height));
		stats.setPreferredSize(new Dimension(panel_width, panel_height - 150));
		buttons.setPreferredSize(new Dimension(panel_width, 150));

		// adds the components to the various panels
		buttons.add(ai_button, "Center");
		this.add(stats, "Center");
		this.add(buttons, "South");

		// sets up the action listener on the AI button
		ai_button.addActionListener(this);

		loadLogic();

		web_broker = new WebBroker((Game) model);
	}

	// called every time an update of the game is made
	public void update() {
		Image background_buffer = createImage(stats.getWidth(),
				stats.getHeight());
		Graphics background_buffer_graphics = background_buffer.getGraphics();
		// updates the stats on screen
		background_buffer_graphics.drawString(
				"Current Move: " + (model.isWhiteToPlay() ? "White" : "Black"),
				5, 20);
		background_buffer_graphics.drawString("White Points: "
				+ (((Game) model).getBoard().getWhiteScore()), 20, 125);
		background_buffer_graphics.drawString("Black Points: "
				+ (((Game) model).getBoard().getBlackScore()), 20, 145);

		Image wc = null;
		Image bc = null;
		//
		try {
			wc = ImageIO.read(new File(("images/white_piece.png")));
			bc = ImageIO.read(new File(("images/black_piece.png")));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (model.isWhiteToPlay()) {
			background_buffer_graphics.drawImage(wc, 25, 30, null, null);

		} else {
			background_buffer_graphics.drawImage(bc, 25, 30, null, null);

		}

		stats.getGraphics().drawImage(background_buffer, 0, 0, null);

	}

	public void actionPerformed(ActionEvent ae) {

		if (!model.isGameOver()) {

			SquareContents piece;

			if (model.isWhiteToPlay()) {
				piece = SquareContents.WHITE_PIECE;
			} else {
				piece = SquareContents.BLACK_PIECE;
			}

			long timer = System.nanoTime();

			ArisiaNode arisia = new ArisiaNode(
					((Board) ((Game) model).getBoard()).cloneSquares(), piece,
					piece, 5, 0, 0, 9, 9);

			System.out.println(arisia.getBestMove().col + " "
					+ arisia.getBestMove().row + " " + arisia.getScore());

			System.out.println(System.nanoTime() - timer);
			model.play(arisia.getBestMove().row, arisia.getBestMove().col);
			parent_frame.update();
			// Point point =
			// web_broker.playMove(arisia.getBestMove().col,arisia.getBestMove().row);
			// if (point!=null){
			// model.play(point.x, point.y);
			// }
			System.gc();			
		}
	}

	private void loadLogic() {
		double temp[][] = new double[8][8];
		try {
			File file = new File("logic");
			FileReader file_reader = new FileReader(file);
			BufferedReader reader = new BufferedReader(file_reader);
			String line = null;
			int count = 0;

			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				for (int i = 0; i < values.length; i++) {
					temp[count][i] = Double.parseDouble(values[i]);
					//System.out.print(temp[count][i] + " ");
				}
				//System.out.println("");
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ThreadCounter.setLogic(temp);
	}

}
