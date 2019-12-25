package org.Client.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import org.Client.model.*;


public class LoginDisplay extends JFrame{
	
		private final JLabel jlblUsername = new JLabel("Username");
	    private final JLabel jlblPassword = new JLabel("Password");

	    private final JTextField jtfUsername = new JTextField(15);
	    private final JPasswordField jpfPassword = new JPasswordField();

	    private final JButton btnAuthorization = new JButton("Login");
	    private final JButton btnCancel = new JButton("Cancel");
	    private final JButton btnRegistration = new JButton("Registration");

	    private final JLabel jlblStatus = new JLabel(" ");
	
	    
	    private MasterServerClient msClient = new MasterServerClient();;
	    
	    
	    public static void main(String[] args) {
	    	new LoginDisplay();
	    }
		
	public LoginDisplay() {
		super("Tic Tac Toe Online");
		
		setVisible(true);
		setSize(500,500);	
		
		btnAuthorization.setBackground(Color.DARK_GRAY);
		btnAuthorization.setForeground(Color.GREEN);
		
		
		btnCancel.setBackground(Color.DARK_GRAY);
		btnCancel.setForeground(Color.GREEN);
		
		btnRegistration.setBackground(Color.DARK_GRAY);
		btnRegistration.setForeground(Color.GREEN);
		
		jtfUsername.setBackground(Color.DARK_GRAY);
		jtfUsername.setForeground(Color.GREEN);
		
		jpfPassword.setBackground(Color.DARK_GRAY);
		jpfPassword.setForeground(Color.GREEN);

		JPanel p3 = new JPanel(new GridLayout(2, 1));
		jlblUsername.setForeground(Color.GREEN);
		jlblPassword.setForeground(Color.GREEN);
        p3.add(jlblUsername);
        p3.add(jlblPassword);
        p3.setBackground(Color.BLACK);
        
        JPanel p4 = new JPanel(new GridLayout(2, 1));
        p4.add(jtfUsername);
        p4.add(jpfPassword);
        p3.setBackground(Color.BLACK);
        JPanel p1 = new JPanel();
        p1.add(p3);
        p1.add(p4);
        p1.setBackground(Color.BLACK);
        JPanel p2 = new JPanel();
        p2.add(btnAuthorization);
        p2.add(btnRegistration);
        p2.add(btnCancel);
        p2.setBackground(Color.BLACK);
        
		
        JPanel p5 = new JPanel(new BorderLayout());
        p5.add(p2, BorderLayout.CENTER);
        p5.add(jlblStatus, BorderLayout.NORTH);
        p5.setBackground(Color.BLACK);
        jlblStatus.setForeground(Color.RED);
        jlblStatus.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(p1, BorderLayout.CENTER);
        add(p5, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
        
        //
        
        initConnectionToMasterServer(MasterServerClient.DEFAULT_HOST, MasterServerClient.DEFAULT_PORT, "default Master Server");
        //
        addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });
       
        
		btnAuthorization.addActionListener(new ActionListener() { //Авторизация
			@Override
			public void actionPerformed(ActionEvent e) {	
				
			if(jtfUsername.getText().equals(null) || String.copyValueOf(jpfPassword.getPassword()).equals(null))
			{
				jlblStatus.setText("Input your login or password! Please :з");
			}
			
			
			if (msClient.isConnected()) 
			{
			msClient.loginUser(jtfUsername.getText(), String.copyValueOf(jpfPassword.getPassword()));
			
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				if(msClient.getTrueFalseLogin()) 
				{
					new MainFrame(jtfUsername.getText(), msClient);
					setVisible(false);
            		dispose();
				}else {
					jlblStatus.setText("Invalid username or password!");
				}
			
			}
			
		
			}
		});
		
		btnRegistration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(jtfUsername.getText().equals("") || String.copyValueOf(jpfPassword.getPassword()).equals(""))
				{
					jlblStatus.setText("Input your login or password! Please :з");
				}
			
				
				
				if (msClient.isConnected()) 
				{
				msClient.registrationUser(jtfUsername.getText(), String.copyValueOf(jpfPassword.getPassword()));
				
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					if(msClient.getTrueFalseRegistration()) 
					{
						jlblStatus.setForeground(Color.green);
						jlblStatus.setText("You are registered! :з");
					
						TableOfRanks.setPlayerAndInitialValue(jtfUsername.getText());
					}else {
						jlblStatus.setText("This login is already registered!");
					}
				
				}
				
			}
		});
		
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
                dispose();
                System.exit(0);
			}
		});
		
		
	}
	
	
	private void initConnectionToMasterServer(final String aHost, final int aPort, final String aDisplayDestination)
	{
		
		
		jlblStatus.setText("Connecting to Server...");
		if (msClient.connectToMasterServer(aHost, aPort))
		{
			jlblStatus.setForeground(Color.green);
			jlblStatus.setText("Connection is established! :з");
		}
		else
		{
			jlblStatus.setText("No connection! :c " + msClient.getConnectionStatus());
		}
	
	}
	
	
	
}
