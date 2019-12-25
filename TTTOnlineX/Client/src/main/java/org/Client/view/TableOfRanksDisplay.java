package org.Client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;


import org.Client.model.TableOfRanks;


public class TableOfRanksDisplay extends JPanel implements ActionListener {
	
	private JButton 	backMenu;
	
public static String nickname;

private JTable table;
private String[] columnNames = { 
		"RANK",
		"NICKNAME",
		"POINTS",
};
	
	public TableOfRanksDisplay(){
		createGUI();
	}
	
	public static void setNickname(String a) {
		nickname = a;
	}
	
	private void createGUI()
	{
		setVisible(true);
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		
 		String[][] playerList = TableOfRanks.getPlayerList(); 

 
 		table = new JTable(playerList,columnNames) {
 			@Override
 			public boolean isCellEditable(int arg0, int arg1) {
 				return false;
 			}
 		};
 		
 		table.setGridColor(Color.BLACK);
 		
 		
 		table.setForeground(Color.GREEN);
 		table.setBackground(Color.DARK_GRAY);
		JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,	ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBackground(Color.BLACK);
		add(scroll, BorderLayout.CENTER);
		
		
		// South @
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1, 1));
		backMenu = new JButton("BACK TO THE MAIN MENU");
		backMenu.setBackground(Color.DARK_GRAY);
		backMenu.setForeground(Color.GREEN);
		backMenu.addActionListener(this);
		southPanel.add(backMenu);
		add(southPanel, BorderLayout.SOUTH);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent aev)
	{
		if (aev.getSource() instanceof JButton)
		{
			JButton button = ((JButton)aev.getSource());
			if(button == this.backMenu)
			{
				MainMenuDisplay mmd = new MainMenuDisplay();
				MainFrame.getInstance().setContentPane(mmd);
				MainFrame.getInstance().validate();
				MainFrame.getInstance().repaint();
			}
		}
	}
}
