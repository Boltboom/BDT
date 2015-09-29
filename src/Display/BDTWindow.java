package Display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

public class BDTWindow extends JFrame implements ActionListener {

	private Dimension screenSize;
	private String os;
	private int screenWidth, screenHeight;
	private Timer swivelTimer;
	private int duration;
	
	public static void main(String[] args) {
		new BDTWindow();
	}
	
	public BDTWindow() {
		SetSystemSettings();
		draw();
		move();
	}
	
	public void SetSystemSettings() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
	}
	
	public void draw() {
		this.setBounds(screenWidth, screenHeight-320, 400, 320);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void move() {
		duration = 400;
		swivelTimer = new Timer(20, this);
		swivelTimer.start();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == swivelTimer) {
			if(this.getLocation().x > screenWidth - 400) {
				this.setLocation(this.getLocation().x - 16, this.getLocation().y);
				this.setAlwaysOnTop(true);
				this.toFront();
				this.repaint();
			} else {
				swivelTimer.stop();
			}
		}
	}
	
	
}
