package Display;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import com.intel.bluetooth.RemoteDeviceHelper;
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
	protected Dimension screenSize;
	protected AdvancedWindow pane;
	protected String os;
	protected int screenWidth, screenHeight;
	protected Timer swivelTimer;
	protected Timer clock;
	protected int duration;
	protected JButton hideButton;
	protected JPanel detailPanel;
	protected JButton expandButton;
	protected JPanel devicePanel;
	protected boolean offscreen;
	protected static Object lock;
	protected LocalDevice user;
	protected DiscoveryAgent agent;
	protected ArrayList<RemoteDevice> discoveredDevices;
	protected ArrayList<RemoteDevice> copydiscoveredDevices;
	protected ArrayList<Long> connectiontimes;
	protected RemoteDevice mainDevice;
	protected JLabel deviceLabel;
	protected JLabel AssignLabel;
	protected boolean operational = true;
	
	protected ArrayList<DataRelation> DataDevices;

	/*
	 * Main function, starts new BDTWindow.
	 */
	public static void main(String[] args) {
		new BDTWindow(true);
	}

	/*
	 * BDTWindow constructor. Follows 5 steps before continuous automation.
	 */
	public BDTWindow(boolean type) {
		SetSystemSettings();
		setup(type);
	}

	public void setup(boolean type) {
		discoveredDevices = new ArrayList<RemoteDevice>();
		connectiontimes= new ArrayList<Long>();
		DataDevices= new ArrayList<DataRelation>();
		copydiscoveredDevices= new ArrayList<RemoteDevice>();
		
		if(type) {
			draw();
			detect();
			clock();

		}
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
			devicePanel.setBackground(Color.ORANGE);
			devicePanel.repaint();
			lock = new Object();
			user = LocalDevice.getLocalDevice();
			agent = user.getDiscoveryAgent();
			copydiscoveredDevices.clear();
			agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());
			try {
				synchronized(lock) {
					lock.wait();
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			} System.out.println("Device Inquiry Completed.");
			deviceLabel.setText("");
			String label = "<html><body>" + deviceLabel.getText();
			int i=0;
			removeunfound();
			for(RemoteDevice a : discoveredDevices) {
				Date temp=new Date();
				System.out.println(a.getFriendlyName(false)+ " Display results time: " + temp);


				double len=((double)(temp.getTime())-(double)(DataDevices.get(i).startconnection))/1000;
				label += a.getFriendlyName(false) + " {" + a.getBluetoothAddress() + ") Connection Length: "+len+" seconds <br>";
				i++;
			}
			label+="</body></html>";
			deviceLabel.setText(label);
			devicePanel.setBackground(Color.GREEN);
			devicePanel.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Function draws all pertinent graphical components.
	 */
	public void draw() {
		hideButton = new JButton("Toggle\r\n");
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
		expandButton.addActionListener(this);
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
		this.setBounds(screenWidth-400, screenHeight-320, 400, 320);
		this.setAlwaysOnTop(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
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
		if(e.getSource() == expandButton) {
			writeDataDeviceLog();
			pane = new AdvancedWindow();
			pane.setVisible(true);
		}
		if(e.getSource() == hideButton) {
			new Thread(new Runnable() {
				public void run() {
					detect();
				}
			}).start();

		}
	}
	
	/*
	 * Writer
	 */
	
	public void writeDataDeviceLog() {
		ArrayList<String> str = new ArrayList<String>();
		for(int i = 0; i < DataDevices.size(); i++) {
			Date d = new Date();
			double temp =((double)(d.getTime())-(double)(DataDevices.get(i).startconnection))/1000;
			if(DataDevices.get(i).discoverable == true) {
				str.add(DataDevices.get(i).id + ", " + DataDevices.get(i).name + ", " + DataDevices.get(i).sigstr + ", " + DataDevices.get(i).discoverable + ", " + temp + ",");
			} else {
				str.add(DataDevices.get(i).id + ", " + DataDevices.get(i).name + ", " + DataDevices.get(i).sigstr + ", " + DataDevices.get(i).discoverable + ", " + DataDevices.get(i).startconnection + ",");
			}
		}
		String[] arr = new String[str.size()];
		for(int j = 0; j < str.size(); j++) {
			arr[j] = str.get(j);
		}
		try {
			Writer.write(arr, "history");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// This device stops all current timers in our Data Relation, because none of the devices are found anymore.
	public void stopallTimes()
	{
		for(int i=0; i<DataDevices.size();i++)
		{
			if(	DataDevices.get(i).discoverable)
			{
				Date temp= new Date();
				DataDevices.get(i).discoverable=false;
				// Stops the timer for the discovered devices bc they are no longer discoverable.
				DataDevices.get(i).startconnection=((double)(temp.getTime())-(double)(DataDevices.get(i).startconnection))/1000;
			}

		}
	}
	// Checks if devices in discovered devices are still found.
	public void removeunfound()
	{
		System.out.println("GOTHERE");
		System.out.println("The size of the copy is :"+ copydiscoveredDevices.size());
		if(copydiscoveredDevices.isEmpty())
		{
			discoveredDevices.clear();

			
			stopallTimes();
			//Clear connection times
			connectiontimes.clear();
		}
			
		else
		{
			for(int i =0; i<discoveredDevices.size();i++)
			{
				if(!copydiscoveredDevices.contains(discoveredDevices.get(i)))
				{
					discoveredDevices.remove(i);
					connectiontimes.remove(i);
					System.out.println("REMOVED");
					
				}
			}
		}
	}
	/*
	 * Listener class designed to meet specifications of DiscoveryListener
	 */
	public class MyDiscoveryListener implements DiscoveryListener {

		public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
			copydiscoveredDevices.add(btDevice);
			System.out.println(copydiscoveredDevices.size());
			String name; /*
			try {
				int x= RemoteDeviceHelper.readRSSI(btDevice);
				System.out.println("Connection Strength: "+x);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} */
			try
			{
				name = btDevice.getFriendlyName(false);
			} 
			catch (Exception e) 
			{
				name = btDevice.getBluetoothAddress();
			}
			// Adding all discovered devices to a copy.
		
			System.out.println("Device that was found: " +name);
			if(!discoveredDevices.contains(btDevice))
			{
				DataRelation Data = new DataRelation();
				Date d=new Date();
				
				Data.name= name;
				Data.id=btDevice.getBluetoothAddress();
				Data.discoverable=true;
				Data.startconnection=d.getTime();
							
				discoveredDevices.add(btDevice);
				
				// add data-relation object to ArrayList
				DataDevices.add(Data);						
				System.out.println("Device: "+name + " Time added: " + d);
				connectiontimes.add(d.getTime());

			}

		}

		public void inquiryCompleted(int arg0) {
			synchronized(lock) {
				lock.notify();
			}
		}
		public void serviceSearchCompleted(int arg0, int arg1) {}
		public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {}
	}

}
