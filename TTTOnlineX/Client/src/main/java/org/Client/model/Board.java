package org.Client.model;

public class Board {
	private String[] board;
	
	public Board() {
		board = new String[100];
	}
	
	public void initBoard() {
		for(int i = 0; i < board.length; i++) {
			board[i] = "free";
		}
	}
	
	public String getElementBoard(int index) {
		return board[index];
	}
	
	public void setElementBoard(int index, String value) {
		board[index] = value;
	}
	
	public String[] allElements() {
		return board;
	}
	
	
	public boolean checkWinner(String name) {
		
		String id = name;
		
		// Check rows
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 100; j += 10)
					{
						if (board[j+i].equals(id)   && 
							board[j+i+1].equals(id) && 
							board[j+i+2].equals(id) && 
							board[j+i+3].equals(id) &&
							board[j+i+4].equals(id))
						{
							return true;
						}
					}
				}
				// Check columns
				for(int i = 0; i < 10; i++) {
					for (int j = 0; j < 60; j+= 10)
					{
						if (board[j+i].equals(id)    &&
							board[j+i+10].equals(id) &&
							board[j+i+20].equals(id) &&
							board[j+i+30].equals(id) &&
							board[j+i+40].equals(id))
						{
							return true;
						}
					}
				}
				
				//Check diagonal "\"
				for(int i = 0; i < 6; i++) {
					for(int j = 0; j < 60; j+=10)
					{	
						if (board[i+j].equals(id)    &&
							board[i+j+11].equals(id) &&
							board[i+j+22].equals(id) &&
							board[i+j+33].equals(id) &&
							board[i+j+44].equals(id))
						{
							return true;
						}
					}
				}
				
				 for(int i = 9; i > 3; i--) {
						for(int j = 0; j < 60; j+=10)
						{	
							if (board[i+j].equals(id)    &&
								board[i+j+9].equals(id)  &&
								board[i+j+18].equals(id) &&
								board[i+j+27].equals(id) &&
								board[i+j+36].equals(id))
							{
								return true;
							}
						}
					}
				// Player didn't win yet
				return false;
	}
}
