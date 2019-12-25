package org.Client.model;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class BoardTest {

	@Test
	public void testInitBoard() {
		String[] boardTest = new String[100];
		for (int i = 0; i < boardTest.length; ++i) {
			boardTest[i] = "free";
		}
		Board a = new Board();
		a.initBoard();
		Assert.assertArrayEquals(a.allElements(), boardTest);
	}

	@Test
	public void testGetElementBoard() {
		Board a = new Board();
		a.initBoard();
		String expected = "free";
		String actual = a.getElementBoard(0);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSetElementBoard() {
		String[] boardTest = new String[100];
		for (int i = 0; i < boardTest.length; ++i) {
			boardTest[i] = "free";
		}
		
		Board a = new Board();
		a.initBoard();
		a.setElementBoard(0, "player");
		
		boardTest[0] = "player";
		
		Assert.assertArrayEquals(a.allElements(), boardTest);
	}

	@Test
	public void testCheckWinner() {
		
		
		Board a = new Board();
		a.initBoard();
		a.setElementBoard(0, "player");
		a.setElementBoard(11, "player");
		a.setElementBoard(22, "player");
		a.setElementBoard(33, "player");
		a.setElementBoard(44, "player");
		
		boolean expected = true;
		boolean actual = a.checkWinner("player");
		
		Assert.assertEquals(expected, actual);
		
	}
	
}
