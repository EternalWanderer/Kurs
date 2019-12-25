package org.Server.model;

import org.junit.Assert;
import org.junit.Test;

public class MasterServerTest {

	@Test
	public void testAuthorizationTrue() {
		boolean expected = true;
		boolean actual = MasterServer.authorization("admin", "12345");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testAuthorizationFalse() {
		boolean expected = false;
		boolean actual = MasterServer.authorization("admin", "123");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testRegistrationUserFalse() {
		boolean expected = false;
		boolean actual = MasterServer.registrationUser("admin", "12345");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testIsValidCommandTrue() {
		boolean expected = true;
		boolean actual = MasterServer.isValidCommand("tttms4");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testIsValidCommandFalse() {
		boolean expected = false;
		boolean actual = MasterServer.isValidCommand("tttms10");;
		Assert.assertEquals(expected, actual);
	}

}

