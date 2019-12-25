package org.Client.model;
import java.io.*;

import java.net.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Controls the logic of a TTTGame. 
 * Two clients must connect to the server.
 */
public class GameServer
{
	private ServerSocket 			 serverSocket;
	private ArrayList<ClientHandler> allPlayers;
	private MasterServerClient 		 msClient;
	private String 			   		 gameName;
	
	private Board gameBoard;// The Game Board
	private String 	 playerTurn; // Whos turn it is
	
	
	public static String 	PLAYER_ONE = null;
	public static String 	PLAYER_TWO = null;
	
	public static final String FREE_SQUARE = "free"; // Free squares on the board
	
	public static final String	GAME_CONNECTED		= "tttgs0", // When two players connect
								NEW_GAME			= "tttgs1", // Starts a new game. Send to both clients
								PLAYER_MOVE		 	= "tttgs2", // A players movement. Send to both clients
								PLAYER_WINS			= "tttgs3", // When sent to a player, he wins. Send to one client
								PLAYER_LOSES		= "tttgs4", // When sent to a player, he loses. Send to one client
								DRAW				= "tttgs5",
								PLAYER_GET_ID		= "tttgs6",
								GET_HOST			= "tttgs7";
			 
	/**
	* Compares the string to all commands and checks if the string is a command
	* @param aCommand The string to check
	* @return true if the string is a command
	*/
	public static boolean isValidCommand(String aCommand)
	{
		if (aCommand == null)
			throw new NullPointerException();
		
		return (aCommand.equals(GAME_CONNECTED) ||
				aCommand.equals(NEW_GAME) 		|| 
				aCommand.equals(PLAYER_MOVE) 	||
				aCommand.equals(PLAYER_WINS) 	||
				aCommand.equals(PLAYER_LOSES)	||
				aCommand.equals(PLAYER_GET_ID)  ||
				aCommand.equals(GET_HOST)         ||
				aCommand.equals(DRAW));
	}	
	
	/**
	 * Compares the string to all player IDs and check if the string is an ID
	 * @param aPlayerID the playerid
	 * @return true if its an ID, false if not
	 */
/*	public static boolean isValidPlayerID(String aPlayerID)
	{
		if (aPlayerID == null)
			throw new NullPointerException();
		
		return (aPlayerID.equals(PLAYER_ONE) 	||
				aPlayerID.equals(PLAYER_TWO));
	} */
	
	public void setHostName(String a) {
		PLAYER_ONE = a;
	}
	
	/**
	 * Creates a GameServer wrapped around a ServerSocket
	 * @param aSocket the server socket. 
	 * @param aMasterServerClient reference to the master server
	 * @param aGameName name of the game //TODO: Not used yet
	 */
	public GameServer(ServerSocket aSocket, MasterServerClient aMasterServerClient, String aGameName, String nickname)
	{
		if (aSocket == null || aMasterServerClient == null || aGameName == null)
			throw new NullPointerException();
		setHostName(nickname);
		serverSocket = aSocket;
		msClient = aMasterServerClient;
		allPlayers = new ArrayList<ClientHandler>(2);
		gameName = aGameName;
		
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// Wait for players
				while(allPlayers.size() < 2)
				{
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}; 
				
				// Tell players a connection have been made
				for (ClientHandler player : allPlayers)
					player.sendMessage(GAME_CONNECTED, new String[]{player.getPlayerID()});
				
				
				newGame();
				
			}	
		}).start();
	}
	

	/**
	 * Waits 2.5 seconds and inits a new game
	 */
	private void newGame()
	{
		gameBoard = new Board();
		gameBoard.initBoard();
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(2500);
					// Randomize who makes the first move
					Random generator = new Random();
					if (generator.nextInt(2) == 0)
						playerTurn = PLAYER_ONE;
					else
						playerTurn = PLAYER_TWO;
					
					sendMessageToAllClients(NEW_GAME, new String[]{playerTurn});
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Starts the server
	 */
	public void startServer()
	{
		
		if (!serverSocket.isBound())
			throw new IllegalStateException();
		
		// Accept connections until we reach maximum connections
		try
		{	
			int maximumPlayers = 2;
			do
			{
				Socket connection = serverSocket.accept(); // Waits  for connection
				ClientHandler ch = new ClientHandler(connection);
				allPlayers.add(ch);
			} while(allPlayers.size() < maximumPlayers);
			serverSocket.close();
			msClient.unregisterGameRoom(gameName, serverSocket.getLocalPort());
		}
		catch (IOException e)
		{
			System.err.println(e);
		}
	}
	
	/**
	 * Sends a message to all clients
	 * @param aCommand The GameServer command
	 * @param aData the data to send, can be null
	 */
	private void sendMessageToAllClients(String aCommand, String[] aData)
	{
		for (ClientHandler client : allPlayers)
		{
			client.sendMessage(aCommand, aData);
		}
	}
	
	/**
	 * Sends message to a specific client
	 * @param aTargetClientID the ID of the client
	 * @param aCommand the GameServer command
	 * @param aData the data to send
	 */
	private void sendMessageToClient(String aTargetClientID, String aCommand, String[] aData)
	{
		for (ClientHandler client : allPlayers)
		{
			if (client.getPlayerID().equals(aTargetClientID))
			{
				client.sendMessage(aCommand, aData);
			}
		}
	}
	
	/**
	 * Counts how many player moves been made
	 * @return 0 - 9
	 */
	private int countPlayerMoves()
	{
		int moves = 0;
		for (String square : gameBoard.allElements())
		{
			if (!square.equals(FREE_SQUARE))
				++moves;
		}
		return moves;
	}
	
	/**
	 * Checks if someone won yet. 
	 * @return Null if no one won yet. Else PlayerID of the winner.
	 */
	private String checkForWinner()
	{
		if (hasPlayerWon(PLAYER_ONE))
			return PLAYER_ONE;
		else if (hasPlayerWon(PLAYER_TWO))
			return PLAYER_TWO;
		else
			return null;
	}
	
	/**
	 * Checks if the given player wins
	 * @param aPlayerID id of the player to check
	 * @return true if player won, false if he havn't won yet
	 */
	private boolean hasPlayerWon(String aPlayerID)
	{	
		return gameBoard.checkWinner(aPlayerID);
		// Check rows	
	}
	
	
	public class ClientHandler implements Runnable
	{
		private Socket socket;
		private boolean alive;
		
		private BufferedReader in;
		private PrintWriter out;
		
		private String playerID; // Which player the client is. PLAYER_ONE or PLAYER_TWO
		/**
		 * Listens for network data from a client.
		 * 
		 * @param aSocket socket to the client/user
		 * @param Id of the player. PLAYER_ONE or PLAYER_TWO
		 */
		public ClientHandler(Socket aSocket) //$
		{
			socket = aSocket;
			
			alive = true;
			try
			{
				in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
				out = new PrintWriter(new OutputStreamWriter( socket.getOutputStream(), "ISO-8859-1"), true);
			} 
			catch (UnsupportedEncodingException e)
			{
				System.err.print(e);
			} 
			catch (IOException e)
			{
				System.err.print(e);
			}
			
			Thread t = new Thread(this);
			t.start();
			
		}
		
		/**
		 * Checks what to do with data received from the user
		 * @param aInData the data received
		 */
		private void readMessage(String aInData)
		{
			
			// Split message into command and a data array
			String[] commandAndDataSeparated = aInData.split("#");
			String command = commandAndDataSeparated[0];
			String[] data = null;
			if (commandAndDataSeparated.length > 1)
				data = commandAndDataSeparated[1].split(":");
			if (!isValidCommand(command))
			{
				System.out.println("Invalid Request:\t" + command);
			}
			
			else if(command.equals(PLAYER_GET_ID)) {
				System.out.println("LOG: GameSrever_Command_PLayer_GET_ID"+ data[0]);
				playerID = data[0];
				if(!playerID.equals(PLAYER_ONE)) {
					PLAYER_TWO = playerID;
					
				}
			}
			
			else if(command.equals(GET_HOST)) {
				sendMessageToAllClients(GET_HOST, new String[]{PLAYER_ONE});
			}
			
			else if (command.equals(PLAYER_MOVE))
			{
				String player = data[0];
				if (player.equals(playerTurn))
				{
					int playerMove = Integer.parseInt(data[1]);
					gameBoard.setElementBoard(playerMove, player);
					// Toggle turns
					playerTurn = (playerTurn.equals(PLAYER_ONE)?PLAYER_TWO:PLAYER_ONE);
					// Update all clients
					sendMessageToAllClients(PLAYER_MOVE, new String[]{player, playerMove+""});
					if (countPlayerMoves() >= 5 )
					{
						String winner = checkForWinner();
						if (winner != null)
						{
							sendMessageToClient(winner, GameServer.PLAYER_WINS, null);
							TableOfRanks.writeWinner(winner);
							String loser = (winner.equals(PLAYER_ONE)?PLAYER_TWO:PLAYER_ONE);
							sendMessageToClient(loser, GameServer.PLAYER_LOSES, null);
							TableOfRanks.writeLoser(loser);
							newGame();
						}
						else if (countPlayerMoves() >= 100)
						{
							sendMessageToAllClients(GameServer.DRAW, null);
							newGame();
						
						}
					}		
				}
				else
				{
					// Cheating client! :(
				}
			}
			
		}
		
		
		
		/**
		 * Sends a message to the client
		 * @param aCommand The GameServer Command
		 * @param aData data to send, can be null
		 */
		private void sendMessage(String aCommand, String[] aData)
		{
			//Validate arguments
			if (!GameServer.isValidCommand(aCommand))
				throw new IllegalArgumentException("aCommand is an invalid command: " + aCommand);
			
			if (aData != null)
			{
				
				CharSequence illegalChar1 = "#", illegalChar2 = ":";
				for (String dataPart : aData)
				{
					dataPart = dataPart.replace(":", "");
					dataPart = dataPart.replace("#", "");
					
					if (dataPart.contains(illegalChar1) || dataPart.contains(illegalChar2))
					{
						throw new IllegalArgumentException("aData cannot contain: '" + illegalChar1 + "' or '" + illegalChar2+ "': " + dataPart);
					}
				}
			}
			// Build string
			StringBuilder msg = new StringBuilder();
			msg.append(aCommand);
			msg.append("#"); // Separator between Command and the Data
			
			if (aData != null)
			{
				for (String dataPart : aData)
				{
					msg.append(dataPart);
					msg.append(':'); //Separator between data
				}
			}
			out.println(msg); // Send data
			out.flush();
		}
		
		/**
		 * Gets the playerID of this client
		 * @return the playerID
		 */
		public String getPlayerID()
		{
			return playerID;
		}
		
		@Override
		public void run()
		{
			
			try
			{
				String readed;
				while (alive && (readed = in.readLine()) != null)
				{
					readMessage(readed);
				}
				System.out.println("Server finished");
				// If we get here, the client disconnected in a nice way
				in.close();
				out.close();
				out.close();
				
			}
			catch (IOException e)
			{
				
			}
			kill();
		}
		
		
		/**
		 * Kills the client
		 */
		public void kill()
		{
			alive = false;
		}
	}
	
}
