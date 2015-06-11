package othelloAI;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import othelloModel.Game;
import othelloModel.Board;

public class WebBroker{
	private Game model=null;
	private Robot robot = null;
	private Point top_left=new Point(1055,355);
	private Point bottom_right=new Point(1435,735);
	private int spacing = (bottom_right.x-top_left.x)/8;
	
	public WebBroker(Game model){
		this.model=model;
		try {
			robot = new Robot();
		} catch (AWTException e) {e.printStackTrace();}

		robot.mouseMove(top_left.x, top_left.y);
		//robot.mouseMove(bottom_right.x, bottom_right.y);
	}
	
	
	public Point playMove(int x, int y){
		robot.mouseMove(top_left.x + x*spacing + spacing/2, top_left.y + spacing*y + spacing/2);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		try {
			Thread.sleep(900);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i=0; i<8; i++){
			for (int j=0; j<8; j++){
				//robot.mouseMove(top_left.x + i*spacing + spacing/2, top_left.y + spacing*j + spacing/2);
				if ((robot.getPixelColor(top_left.x + i*spacing + spacing/2, top_left.y + spacing*j + spacing/2).equals(Color.black) || (robot.getPixelColor(top_left.x + i*spacing + spacing/2, top_left.y + spacing*j + spacing/2).equals(Color.white)))){
					if(((Board)((Game)model).getBoard()).getSquareContents(i, j).isEmpty()){
						System.out.println("Find at " + i + " " + j);
						model.play(j,i);
						//return(new Point(j,i));
					} 
				}
			}
		}
		
		return null;
	}
}
