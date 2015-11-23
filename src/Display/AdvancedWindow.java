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

public class AdvancedWindow extends BDTWindow implements ActionListener {
	private JTabbedPane tabs;
	private JButton contractButton;
	private JPanel devicesTab;
	private JPanel graphTab;
	private JPanel errorTab;
	private GraphPane graph;
	private ArrayList<DataRelation> pertInfo;
	private JTable table;
	
	public AdvancedWindow() {
		super(false);
		this.setTitle("Advanced Utility Pane");
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setBounds(10, 55, 924, 615);
		getContentPane().add(tabs);
		
		devicesTab = new JPanel();
		devicesTab.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		devicesTab.setBackground(new Color(245, 255, 250));
		tabs.addTab("Devices", null, devicesTab, null);
		devicesTab.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"Device Name", "Bluetooth Address", "Discoverable", "Interval"
			}
		));
		table.setBackground(new Color(255, 255, 255));
		devicesTab.add(table);
		
		graphTab = new JPanel();
		graphTab.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		graphTab.setBackground(new Color(240, 248, 255));
		tabs.addTab("Graphical Data", null, graphTab, null);
		graphTab.setLayout(null);
		
		graph = new GraphPane();
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
		this.readInfo();
	}
	
	public void readInfo() {
		
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
