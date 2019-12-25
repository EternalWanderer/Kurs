package org.Client.model;

import org.junit.Assert;
import org.junit.Test;



public class GameServerTest{

	
	@Test
	public void isValidateCommandTrue() {
		boolean expected = true;
		boolean actual = GameServer.isValidCommand("tttgs0");
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void isValidateCommandFalse() {
		boolean expected = false;
		boolean actual = GameServer.isValidCommand("tttgs10");
		
		Assert.assertEquals(expected, actual);
	}
	
}
