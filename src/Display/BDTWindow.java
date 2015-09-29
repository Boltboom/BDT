package Display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.JList;
import java.awt.Color;

public class BDTWindow extends JFrame implements ActionListener {

	private Dimension screenSize;
	private String os;
	private int screenWidth, screenHeight;
	private Timer swivelTimer;
	private int duration;
	private JButton hideButton;
	private JPanel detailPanel;
	private JButton expandButton;
	private JPanel devicePanel;
	
	public static void main(String[] args) {
		new BDTWindow();
	}
	
	public BDTWindow() {
		getContentPane().setLayout(null);
		
		hideButton = new JButton("Hide\r\n");
		hideButton.setBackground(Color.WHITE);
		hideButton.setBounds(278, 234, 96, 36);
		getContentPane().add(hideButton);
		
		detailPanel = new JPanel();
		detailPanel.setBackground(Color.WHITE);
		detailPanel.setBounds(10, 56, 364, 167);
		getContentPane().add(detailPanel);
		
		expandButton = new JButton("Expand");
		expandButton.setBackground(Color.WHITE);
		expandButton.setBounds(278, 11, 96, 36);
		getContentPane().add(expandButton);
		
		devicePanel = new JPanel();
		devicePanel.setBackground(Color.WHITE);
		devicePanel.setBounds(10, 234, 258, 36);
		getContentPane().add(devicePanel);
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
