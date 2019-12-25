package org.Client.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.Client.model.MasterServerClient;

public class MainFrame extends JFrame{

	private static MainFrame instance;
	
	public MainFrame(String nickname, MasterServerClient a) {
		super("Tic Tac Toe Online");
		setSize(640, 480);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		loadMainMenu("org.Client.view.MainMenuDisplay", nickname, a);
		
		instance = this;
		validate();
		
	}
	
	
	public static MainFrame getInstance()
	{
		return instance;
	}
	
	private void loadMainMenu(final String aJPanelName, String nickname, MasterServerClient msClient) {
		try
		{
			Class cls = Class.forName(aJPanelName);
			Object o = cls.newInstance();
			if (!(o instanceof JPanel))
				throw new IllegalArgumentException("The Class must be a JPanel! Class is: " + o.getClass());
			setTitle("Tic Tac Toe Online");
			MainMenuDisplay.setNickname(nickname);
			MasterServerClientDisplay.setNickname(nickname);
			MasterServerClientDisplay.setMSClient(msClient);
			TableOfRanksDisplay.setNickname(nickname);
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