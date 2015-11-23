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
	public double[] limits;
	
	public GraphPane() {
		super();
		bounds = new double[2];
		limits = new double[2];
		bounds[0] = this.getWidth();
		bounds[1] = this.getHeight();
	}
	
	public void setData(ArrayList<Point> data) {
		this.data = data;
		double max_y = 0.0;
		double max_x = 0.0;
		for(int i = 0; i < data.size(); i++) {
			if(data.get(i).x > max_x) {
				max_x = data.get(i).x;
			}
			if(data.get(i).y > max_y) {
				max_y = data.get(i).y;
			}
		}
		limits[0] = max_x;
		limits[1] = max_y;
	}
	public void init() {
		bounds = new double[2];
		bounds[0] = this.getWidth();
		bounds[1] = this.getHeight();
	}
	protected void paintComponent(Graphics g) {
		if(data != null) {
		//Draw Base
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, (int) bounds[0], (int) bounds[1]);
		g.setColor(Color.BLACK);
		g.drawLine(2, 0, 2, (int)bounds[1]);
		g.drawLine(0, (int)(bounds[1]), (int)(bounds[0]), (int)(bounds[1]));
		//Draw Axis
		for(int i = 0; i < 10; i++) {
			int x_tick = (int)(i * bounds[0] / 10);
			int y_tick = (int)(i * bounds[1] / 10);
			int x_mark = (int)(i * limits[0] / 10);
			int y_mark = (int)(limits[1] - (i * limits[1] / 10));
			g.drawLine(x_tick, (int) bounds[1] - 5, x_tick, (int) bounds[1] + 5);
			g.drawLine(-10, y_tick, 10, y_tick);
			g.drawString("" + x_mark, x_tick + 3, (int)(bounds[1]) - 7);
			g.drawString("" + y_mark, 12, y_tick);
		}
		for(int j = 0; j < data.size(); j++) {
			g.fillRect((int)(bounds[0] / limits[0] * data.get(j).x), (int)(bounds[1] - (bounds[1] / limits[1] * data.get(j).y)), 3, 3);
		}
		}
	}
}
