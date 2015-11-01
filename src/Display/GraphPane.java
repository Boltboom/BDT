package Display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GraphPane extends JPanel{
	public ArrayList<Point> data;
	public double[] bounds;
	
	public GraphPane() {
		super();
		bounds = new double[2];
		bounds[0] = this.getWidth();
		bounds[1] = this.getHeight();
		System.out.println(bounds[0] + " " + bounds[1]);
	}
	
	public void init() {
		bounds = new double[2];
		bounds[0] = this.getWidth();
		bounds[1] = this.getHeight();
		System.out.println(bounds[0] + " " + bounds[1]);
	}
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int) bounds[0], (int) bounds[1]);
		g.setColor(Color.BLACK);
		g.drawLine((int)(bounds[0]/2), 0, (int)(bounds[0]/2), (int)bounds[1]);
		g.drawLine(0, (int)(bounds[1]/2), (int)(bounds[0]), (int)(bounds[1]/2));
	}
}
