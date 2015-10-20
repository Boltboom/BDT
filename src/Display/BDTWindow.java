package Display;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JButton;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.JList;

import java.awt.Color;

import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.swing.JTextPane;
import javax.swing.AbstractListModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.SwingConstants;
import java.awt.Font;

/**
 * This file contains the early work of the Bluetooth Diagnostic utility.
 * Currently, the only function available is finding available bluetooth devices.
 * Classes: BDTWindow - Main program and GUI, MyDiscoveryListener - Listener class to check for valid devices.
 * @author Robert
 *
 */
public class BDTWindow extends JFrame implements ActionListener {

	/*
	 * Fields and Variables
	 */
	private Dimension screenSize;
	private AdvancedWindow pane;
	private String os;
	private int screenWidth, screenHeight;
	private Timer swivelTimer;
	private Timer clock;
	private int duration;
	private JButton hideButton;
	private JPanel detailPanel;
	private JButton expandButton;
	private JPanel devicePanel;
	private boolean offscreen;
	private static Object lock;
	protected LocalDevice user;
	protected DiscoveryAgent agent;
	protected ArrayList<RemoteDevice> discoveredDevices;
	protected RemoteDevice mainDevice;
	private JLabel deviceLabel;
	private JLabel AssignLabel;
	
	/*
	 * Main function, starts new BDTWindow.
	 */
	public static void main(String[] args) {
		new BDTWindow();
	}
	
	/*
	 * BDTWindow constructor. Follows 5 steps before continuous automation.
	 */
	public BDTWindow() {
		SetSystemSettings();
		draw();
		detect();
		move();
		clock();
		pane = new AdvancedWindow();
	}
	
	/*
	 * Function finds all relevant system information for the utility. More information will be pinged later.
	 */
	public void SetSystemSettings() {
		getContentPane().setLayout(null);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.width;
		screenHeight = screenSize.height;
	}
	
	/*
	 * Function detects all available bluetooth devices and adds them to the GUI.
	 */
	public void detect() {
		try {
			System.out.println("Starting Device Discovery...");
			lock = new Object();
			user = LocalDevice.getLocalDevice();
			agent = user.getDiscoveryAgent();
			agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());
			discoveredDevices = new ArrayList<RemoteDevice>();
			try {
				synchronized(lock) {
					lock.wait();
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			} System.out.println("Device Inquiry Completed.");
			String label = "<html><body>" + deviceLabel.getText();
			for(RemoteDevice a : discoveredDevices) {
				label += a.getFriendlyName(false) + " {" + a.getBluetoothAddress() + ") <br>";
			}
			label+="</body></html>";
			deviceLabel.setText(label);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Function draws all pertinent graphical components.
	 */
	public void draw() {
		hideButton = new JButton("Hide\r\n");
		hideButton.setBackground(Color.WHITE);
		hideButton.setBounds(278, 234, 96, 36);
		hideButton.addActionListener(this);
		getContentPane().add(hideButton);
		
		detailPanel = new JPanel();
		detailPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		detailPanel.setBackground(Color.WHITE);
		detailPanel.setBounds(10, 56, 364, 167);
		getContentPane().add(detailPanel);
		detailPanel.setLayout(new BorderLayout(0, 0));
		
		deviceLabel = new JLabel("");
		detailPanel.add(deviceLabel, BorderLayout.CENTER);
		deviceLabel.setVerticalAlignment(SwingConstants.TOP);
		deviceLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		expandButton = new JButton("Expand");
		expandButton.setBackground(Color.WHITE);
		expandButton.setBounds(278, 11, 96, 36);
		getContentPane().add(expandButton);
		
		devicePanel = new JPanel();
		devicePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		devicePanel.setBackground(Color.WHITE);
		devicePanel.setBounds(10, 234, 258, 36);
		getContentPane().add(devicePanel);
		
		AssignLabel = new JLabel("Available Devices:");
		AssignLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
		AssignLabel.setBounds(10, 34, 258, 20);
		getContentPane().add(AssignLabel);
		
		this.setTitle("Bluetooth Diagnostic Utility");
		this.setBounds(screenWidth, screenHeight-320, 400, 320);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/*
	 * Function responsible for "swivel" animation in and out of desktop area.
	 */
	public void move() {
		offscreen = this.getLocation().x >= screenWidth;
		swivelTimer = new Timer(5, this);
		swivelTimer.start();
	}
	
	/*
	 * Function responsible for 100ms clock. This will be used later for graphing and ping analysis.
	 * Currently used to bring GUI back into desktop after being hidden for debugging purposes.
	 */
	public void clock() {
		clock = new Timer(100, this);
		clock.start();
	}
	
	/*
	 * EVENT HANDLER
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		//If the GUI is offscreen, move it onto screen.
		if(e.getSource() == swivelTimer && offscreen) {
			if(this.getLocation().x > screenWidth - 400) {
				this.setLocation(this.getLocation().x - 20, this.getLocation().y);
				this.setAlwaysOnTop(true);
				this.toFront();
				this.repaint();
			} else {
				offscreen = !offscreen;
				swivelTimer.stop();
			}
		//If the GUI is onscreen, move it off screen
		} else if (e.getSource() == swivelTimer && !offscreen) {
			if(this.getLocation().x < screenWidth) {
				this.setLocation(this.getLocation().x + 20, this.getLocation().y);
				this.setAlwaysOnTop(true);
				this.toFront();
				this.repaint();
			}
			else {
				offscreen = !offscreen;
				swivelTimer.stop();
			}
		}
		if(e.getSource() == hideButton) {
			move();
		}		
		if(e.getSource() == expandButton) {
			pane.visible(true);
		}
		if(e.getSource() == clock) {
			duration = duration + 1;
			if(duration % 100 == 0) {
				if(offscreen) move();
			}
		}
	}
	
	/*
	 * Listener class designed to meet specifications of DiscoveryListener
	 */
	public class MyDiscoveryListener implements DiscoveryListener {

		public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
			String name;
			try {
				name = btDevice.getFriendlyName(false);
			} catch (Exception e) {
				name = btDevice.getBluetoothAddress();
			}
			discoveredDevices.add(btDevice);
			System.out.println("Device found: " + name);
		}

		public void inquiryCompleted(int arg0) {
			synchronized(lock) {
				lock.notify();
			}
		}
		public void serviceSearchCompleted(int arg0, int arg1) {}
		public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {}
	}
	
	public class AdvancedWindow extends JFrame implements ActionListener {
		public AdvancedWindow() {
			this.draw();
		}
		
		public void draw() {
			this.setBounds((screenSize.width / 2) - (480), (screenSize.height) - (360), 960, 720);
		}
		
		public void visible(boolean b) {
			this.setVisible(b);
		}
		public void actionPerformed(ActionEvent arg0) {

			
		}
	}
}

