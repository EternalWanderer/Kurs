package org.Client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainMenuDisplay extends JPanel implements ActionListener
{
	private JButton  playTTT,
			 ToR,
			 quit;
	
	private static String nickname;
	
	public MainMenuDisplay()
	{
		setVisible(true);
		setLayout(new GridLayout(3,1));
		playTTT = new JButton("Play");
		ToR = new JButton("TABLE Of RANKS");
		quit = new JButton("QUIT");
		
		playTTT.setForeground(Color.GREEN);
		ToR.setForeground(Color.GREEN);
		quit.setForeground(Color.GREEN);
		
		playTTT.setBackground(Color.DARK_GRAY);
		ToR.setBackground(Color.DARK_GRAY);
		quit.setBackground(Color.DARK_GRAY);
		
		
		playTTT.addActionListener(this);
		ToR.addActionListener(this);
		quit.addActionListener(this);
		
		add(playTTT);
		add(ToR);
		add(quit);
		
		validate();
		
	}
	
	public static void setNickname(String nick) {
		nickname = nick;
	}

	@Override
	public void actionPerformed(ActionEvent aev)
	{
		if (aev.getSource() instanceof JButton)
		{
			JButton button = ((JButton)aev.getSource());
			if(button == this.playTTT)
			{
				MasterServerClientDisplay mscd = new MasterServerClientDisplay();
				MasterServerClientDisplay.setNickname(nickname);
				MainFrame.getInstance().setContentPane(mscd);
				MainFrame.getInstance().validate();
				MainFrame.getInstance().repaint();
			}
			else if (button == this.ToR)
			{
				TableOfRanksDisplay ToR = new TableOfRanksDisplay();
				MainFrame.getInstance().setContentPane(ToR);
				MainFrame.getInstance().validate();
				MainFrame.getInstance().repaint();
			}
			else if (button == this.quit)
			{
				setVisible(false);
                System.exit(0);
			}
		}
	}
	
	
}

