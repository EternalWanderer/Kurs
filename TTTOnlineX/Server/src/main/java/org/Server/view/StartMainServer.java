package org.Server.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartMainServer extends JFrame{
	
	
	public static void main(String[] args) {
		new StartMainServer();
	}
	
	
	private static StartMainServer instance;
	
	
	public StartMainServer()
	{
		super("Tic Tac Toe Online");
		setSize(640, 480);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		loadServerDisplay("org.Server.view.MasterServerDisplay");
		
		instance = this;
		validate();
	}
	

private void loadServerDisplay(final String aJPanelName) {
		try
		{
			Class cls = Class.forName(aJPanelName);
			Object o = cls.newInstance(); // o = new
			if (!(o instanceof JPanel))
				throw new IllegalArgumentException("The Class must be a JPanel! Class is: " + o.getClass());
			setTitle("Tic Tac Toe Online");
			setContentPane((JPanel)o);
			((JPanel)o).validate();
			validate();
		}
		catch (Exception e)
		{
			System.err.println(e);
			System.exit(1);
		}
	}
}