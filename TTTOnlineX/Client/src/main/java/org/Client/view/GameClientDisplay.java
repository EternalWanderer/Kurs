package org.Client.view;

import java.awt.*;


import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import org.Client.model.*;

import org.Client.model.GameClient;
import org.Client.model.GameServer;
import org.Client.model.ITTTGameListener;


/**
 * The Graphics of the TicTacToe game
 */
public class GameClientDisplay extends JPanel implements ActionListener, ITTTGameListener
{
	
	private ArrayList<JButton> allSquares = new ArrayList<JButton>(); // The squares in Tic Tac Toe
	
	private JLabel gameStatus;
	
	private GameClient gClient;
	/**
	 * Creates a client that controls the Gui of the game
	 */
	public GameClientDisplay(GameClient aClient)
	{
		if (aClient == null)
			throw new NullPointerException();
		gClient = aClient;
		createGUI();
	}
	
	
	
	/**
	 * Creates GUI
	 */
	private void createGUI()
	{
		setLayout(new BorderLayout());
	
		
		// Top
		JPanel northPanel = new JPanel();
		gameStatus = (JLabel)northPanel.add(new JLabel("Waiting for players..."));
		northPanel.setBackground(Color.DARK_GRAY);
		gameStatus.setBackground(Color.DARK_GRAY);
		gameStatus.setForeground(Color.GREEN);
		
		add(northPanel, BorderLayout.NORTH);
		
		// Center
		JPanel gameBoard = new JPanel();
		gameBoard.setLayout(new GridLayout(10,10));
		for (int i = 0; i < 100; ++i)
		{
			JButton button = new JButton();
			button.setBackground(Color.BLACK);
			button.setForeground(Color.GREEN);
			allSquares.add(button);
			button.addActionListener(this);
			gameBoard.add(button);
		}
		add(gameBoard, BorderLayout.CENTER);
		setSquaresEnabled(false);
		
		//north - button quit
	}
	
	/**
	 * Sets all squares (JButtons) enabled or disabled
	 * @param aEnable True to enable, false to disable
	 */
	public void setSquaresEnabled(boolean aEnable)
	{
		for (JButton square : allSquares)
			square.setEnabled(aEnable);
	}
	
	/**
	 * Enable squares that aren't taken
	 */
	public void enableFreeSquares()
	{
		for (JButton square : allSquares)
		{
			if (square.getText().isEmpty())
			{
				square.setEnabled(true);
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent aev)
	{
		if (aev.getSource() instanceof JButton)
		{
			JButton pressedSquare = (JButton)aev.getSource();
			int pressedPosition = allSquares.indexOf(pressedSquare);
			gClient.sendMove(pressedPosition);
			setSquaresEnabled(false);
		}
	}

	@Override
	public void youLose()
	{
		setSquaresEnabled(false);
		JOptionPane.showMessageDialog(this, "You Lose... :с");
	
	}



	@Override
	public void youWin()
	{
		setSquaresEnabled(false);
		JOptionPane.showMessageDialog(this, "You Win! :з");
		
	}


	@Override
	public void youDraw()
	{
		setSquaresEnabled(false);
		JOptionPane.showMessageDialog(this, "Draw");
		
	}



	@Override
	public void playerMove(String aPlayer, int aPosition)
	{
		JButton square = allSquares.get(aPosition);
		square.setForeground(Color.GREEN);
		String currPlayer = null;
		square.setText(playerIDAsSymbol(aPlayer));
		if (gClient.isMyTurn())
		{
			gameStatus.setText("Your Turn!");
			enableFreeSquares();
		}
		else
		{
			if(aPlayer.equals(GameClient.host)) {
				currPlayer = GameServer.PLAYER_TWO;
			}else {										
				currPlayer = GameClient.host;
			}
			gameStatus.setText(currPlayer +" is making move...");
			setSquaresEnabled(false);
		}
	}

	@Override
	public void gameConnected()
	{
		gameStatus.setText("Connected with player. You are "+
				playerIDAsString(gClient.getNicknameID()) + ". Please wait...");	
	}



	@Override
	public void newGame()
	{
		if(gClient.isMyTurn())
			setSquaresEnabled(true);
		//TODO: Reset everything
		for (JButton square : allSquares)
			square.setText("");
		gameStatus.setText("Game Started. " + (gClient.isMyTurn()?"Make your move!":"Your Opponent is thinking..."));
	}

	/**
	 * Converts a playerID to an easier format
	 * @param aPlayerID the ID
	 * @return "Player One" or "Player Two"
	 */
	private String playerIDAsString(String aPlayerID)
	{
			return (aPlayerID.equals(GameClient.host)?GameServer.PLAYER_TWO:GameClient.host);
	}
	
	/**
	 * Converts a player id to symbol X or O
	 * @param aPlayerID the player id
	 * @return X if player one, O if player two
	 */
	private String playerIDAsSymbol(String aPlayerID)
	{
			return (aPlayerID.equals(GameClient.host)?"X":"O");
	}
}
