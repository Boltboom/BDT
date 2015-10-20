package Display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Icon;
import javax.swing.JButton;

public class AdvancedWindow extends BDTWindow implements ActionListener {
	private JTabbedPane tabs;
	private JTabbedPane deviceTab;
	private JButton contractButton;
	public AdvancedWindow() {
		super(false);
		this.setTitle("Advanced Utility Pane");
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setBounds(10, 55, 924, 615);
		getContentPane().add(tabs);
		
		deviceTab = new JTabbedPane(JTabbedPane.TOP);
		tabs.addTab("Devices", (Icon) null, deviceTab, null);
		
		JTabbedPane graphTab = new JTabbedPane(JTabbedPane.TOP);
		tabs.addTab("Graph Data", (Icon) null, graphTab, null);
		
		JTabbedPane errorTab = new JTabbedPane(JTabbedPane.TOP);
		tabs.addTab("Error History", (Icon) null, errorTab, null);
		
		contractButton = new JButton("Contract");
		contractButton.setBounds(792, 11, 142, 42);
		contractButton.addActionListener(this);
		getContentPane().add(contractButton);
		this.draw();
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
