package org.Client.model;


import java.io.*;
import java.net.*;



public class GameClient
{
	private Socket connection; // connection to host;
	
	private BufferedReader 	 in;
	private PrintWriter 	 out;
	private ServerReader 	 serverReader;
	private ITTTGameListener listener = null; // Pumps events here
	public static 	String   nicknameID;
	private boolean 		 myTurn;
	
	public static String host;

	
	public GameClient(String nickname) {
		nicknameID = nickname;
	} 

	 public static String getNicknameID() {
		 return nicknameID;
	 }
	
	/**
	 * Connects to a TTTGameServer.
	 * @param aAddress Address to server
	 * @param aPort port to server
	 * @return true if connection successful
	 */
	public boolean connectToTTTGameServer(String aAddress, int aPort)
	{
		connection = null;
		try
		{
			connection = new Socket(aAddress, aPort);
			in = new BufferedReader(new InputStreamReader( connection.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter( connection.getOutputStream(), "ISO-8859-1"), true);
			serverReader = new ServerReader();
		}
		catch(UnknownHostException e)
		{
			System.err.println(e);
		}
		catch (IOException e)
		{
			System.err.println(e);
		}
		
		
		
		// TODO: Check if connection really is a TTT game server
		return connection != null;
	}
	
	
	/**
	 * Sends a move
	 * @param aPosition which square player clicked
	 */
	public void sendMove(int aPosition)
	{
		sendMessage(GameServer.PLAYER_MOVE, new String[]{nicknameID, aPosition+""});
	}
	
	public void sendID() {
		sendMessage(GameServer.PLAYER_GET_ID, new String[] {nicknameID});
	}
	
	public void getPL() {
		sendMessage(GameServer.GET_HOST, new String [] {});
	}
	
	
	/**
	 * Adds a listener. Can only be one registered listener
	 * @param aListener
	 */
	public void addTTTGameListener(ITTTGameListener aListener)
	{
		if (aListener == null)
			throw new NullPointerException();
		if (listener != null)
			throw new IllegalStateException("A listener have already been added");
		listener = aListener;
	}
	
	/**
	 * Gets the playerid of this client
	 * @return the playerid
	 */
	
	/**
	 * Checks whos turn it is
	 * @return true if this clients turn
	 */
	public boolean isMyTurn()
	{
		return myTurn;
	}
	
	/**
	 * Sends a message to the game server with array of data. Data can be null in 
	 * case you just want to invoke a command.
	 * Valid commands are those found in GameServer.
	 * '#' and ':' chars will be removed from the data
	 * @param aCommand The command
	 * @param aData The data to be sent. Can be null
	 */
	private void sendMessage(String aCommand, String[] aData)
	{
		//Validate arguments
		if (!GameServer.isValidCommand(aCommand))
			throw new IllegalArgumentException("aCommand is an invalid command: " + aCommand);
		
		if (aData != null)
		{
			
			CharSequence illegalChar0 = "#", illegalChar1 = ":";
			for (String dataPart : aData)
			{
				dataPart = dataPart.replace(":", "");
				dataPart = dataPart.replace("#", "");
				
				if (dataPart.contains(illegalChar0) || dataPart.contains(illegalChar1))
				{
					throw new IllegalArgumentException("aData cannot contain: '" + illegalChar0 + "' or '" + illegalChar1+ "': " + dataPart);
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
	private class ServerReader implements Runnable
	{
		boolean alive = true;
		public ServerReader()
		{
			Thread t = new Thread(this);
			t.start();
		}
		
		@Override
		public void run()
		{
			while (alive)
			{
				try
				{
					String readed = in.readLine();
					// Split message into command and a data array
					String[] commandAndDataSeparated = readed.split("#");
					String command = commandAndDataSeparated[0];
					String[] data = null;
					if (commandAndDataSeparated.length > 1)
						data = commandAndDataSeparated[1].split(":");
					if (GameServer.isValidCommand(command))
					{
						if (command.equals(GameServer.GAME_CONNECTED))
						{
							listener.gameConnected();
						}
						else if (command.equals(GameServer.NEW_GAME))
						{

							myTurn = (data[0].equals(nicknameID));
							listener.newGame();
						}
						else if (command.equals(GameServer.PLAYER_MOVE))
						{ 
							String mover = data[0];
							int position = Integer.parseInt(data[1]);
							myTurn = (!data[0].equals(nicknameID));
							listener.playerMove(mover, position);
							
						}
						else if (command.equals(GameServer.PLAYER_WINS))
						{
							listener.youWin();
						}
						else if (command.equals(GameServer.PLAYER_LOSES))
						{
							listener.youLose();
						}
						else if (command.equals(GameServer.DRAW))
						{
							listener.youDraw();
						}
						else if (command.equals(GameServer.PLAYER_GET_ID)) {
							sendID();
						}
						else if (command.equals(GameServer.GET_HOST)) {
							host = data[0];
						}
					}
					else
					{
						System.out.println("Invalid command recieved: " + command);
					}
				}
				catch (IOException e)
				{
					kill();
				}
			}
		}
		
		public void kill()
		{
			alive = false;
			try
			{
				if (out != null)
					out.close();
				if (in != null)
					in.close();
				if (connection != null)
					connection.close();
				
				out = null;
				in = null;
				connection = null;
			}
			catch (IOException e)
			{
				System.err.println(e);
			}
		}
	}
}
