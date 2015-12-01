package Display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.Font;
import javax.swing.BoxLayout;

public class AdvancedWindow extends BDTWindow implements ActionListener {
	private JTabbedPane tabs;
	private JButton contractButton;
	private JPanel devicesTab;
	private JPanel graphTab;
	private JPanel errorTab;
	private GraphPane graph;
	public ArrayList<DataRelation> pertInfo;
	private JTable table;
	
	public AdvancedWindow() {
		super(false);
		this.setTitle("Advanced Utility Pane");
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setBounds(10, 55, 924, 615);
		getContentPane().add(tabs);
		
		System.out.println("Starting Read Procedure: ");
		readInfo();
		
		System.out.println("Ending the Read Procedure. " + pertInfo.size());
		
		Object[][] art = new Object[pertInfo.size()][4];
		for(int i = 0; i < pertInfo.size(); i++) {
			art[i][1] = pertInfo.get(i).id;
			art[i][0] = pertInfo.get(i).name;
			art[i][2] = pertInfo.get(i).discoverable;
			art[i][3] = pertInfo.get(i).startconnection;
			System.out.println("Table Setting...");
		}
		
		devicesTab = new JPanel();
		devicesTab.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		devicesTab.setBackground(new Color(245, 255, 250));
		tabs.addTab("Devices", null, devicesTab, null);
		
		table = new JTable();
		table.setFont(new Font("Courier New", Font.PLAIN, 18));
		table.setRowSelectionAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(art,
			new String[] {
				"Device Name", "Bluetooth Address", "Discoverable", "Interval"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(247);
		table.getColumnModel().getColumn(1).setPreferredWidth(153);
		table.getColumnModel().getColumn(3).setPreferredWidth(126);
		devicesTab.setLayout(new BoxLayout(devicesTab, BoxLayout.X_AXIS));
		table.setBackground(new Color(255, 255, 255));
		devicesTab.add(table);
		
		graphTab = new JPanel();
		graphTab.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		graphTab.setBackground(new Color(240, 248, 255));
		tabs.addTab("Graphical Data", null, graphTab, null);
		graphTab.setLayout(null);
		
		graph = new GraphPane();
		readData();
		graph.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		graph.setBounds(2, 79, 915, 506);
		graph.init();
		graphTab.add(graph);
		
		
		errorTab = new JPanel();
		errorTab.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		errorTab.setBackground(new Color(255, 240, 245));
		tabs.addTab("Error History", null, errorTab, null);
		
		contractButton = new JButton("Contract");
		contractButton.setBackground(Color.WHITE);
		contractButton.setBounds(792, 11, 142, 42);
		contractButton.addActionListener(this);
		getContentPane().add(contractButton);
		this.draw();
	}
	
	public void readInfo() {
		System.out.println("Reading...");
		String[] raw = Reader.readAllLines("bin/history.txt");
		ArrayList<ArrayList<String>> str = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < raw.length; i++) {
			str.add(i, new ArrayList<String>());
			int last = 0;
			for(int j = 0; j < raw[i].length(); j++) {
				if(raw[i].charAt(j) == ',')
				{
					str.get(i).add(raw[i].substring(last, j));
					last = j + 1;
				}
			}
		}
		/*
		 * Parse to data relations
		 */
		System.out.println("Parsing...");
		this.pertInfo = new ArrayList<DataRelation>();
		for(int x = 0; x < str.size(); x++) {
			System.out.println("Cycle " + x);
			ArrayList<String> current = str.get(x);
			String id = current.get(0);
			String name = current.get(1);
			double sigstr = Double.parseDouble(current.get(2));
			boolean discoverable = current.get(3).equals(" true");
			double startconnection = Double.parseDouble(current.get(4));
			
			DataRelation temp = new DataRelation();
			temp.id = id;
			temp.name = name;
			temp.sigstr = sigstr;
			temp.discoverable = discoverable;
			temp.startconnection = startconnection;
			pertInfo.add(temp);
		}
	}
	
	public void readData() {
		String[] raw = Reader.readAllLines("bin/device_0.txt");
		int last = 0;
		/*
		 * Reading
		 */
		ArrayList<String> str = new ArrayList<String>();
		for(int j = 0; j < raw[1].length(); j++) {
			if(raw[1].charAt(j) == ',')
			{
				str.add(raw[1].substring(last, j));
				last = j + 1;
			}
		}
		/*
		 * Parsing
		 */
		ArrayList<Point> temp_data = new ArrayList<Point>();
		for(int i = 0; i < str.size(); i++) {
			double y = Double.parseDouble(str.get(i));
			double x = i;
			temp_data.add(new Point(x,y));
		}
		graph.setData(temp_data);
	}
	public void draw() {
		this.setBounds((screenSize.width / 2) - (480), (screenSize.height/2) - 360, 960, 720);
	}
	
	public void visible(boolean b) {
		this.setVisible(b);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == contractButton) {
			super.setVisible(true);
			this.setVisible(false);
		}
	}
}
