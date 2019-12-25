package org.Server.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;

public class UserIO {

	private String path;

	public UserIO(String path) {
		this.path = path;
	}

	public synchronized ConcurrentHashMap<String, String> read() {

		ConcurrentHashMap<String, String> users = new ConcurrentHashMap<>();

		String line = null;

		try(BufferedReader reader = new BufferedReader(new FileReader(this.path));) {

			while((line = reader.readLine()) != null) {

				if(validate(line)) {

					String[] components = line.split("name=|&password=");

					users.put(components[1], components[2]);

				}

			}

		}

		catch(IOException exception) {}
		return users;
	}

	private boolean validate(String line) {
		if(line.matches("^name=\\w+&password=[0-9a-zA-z]+$")) {
			return true;
		}
		return false;
	}

	public synchronized void write(ConcurrentHashMap<String, String> users) {

		Iterator<Map.Entry<String, String>> iterator = users.entrySet().iterator();

		Map.Entry<String, String> entry = null;

	
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.path));) {
			while(iterator.hasNext()) {

				entry = iterator.next();

				writer.write("name=");

				writer.write(entry.getKey());

				writer.write("&password=");

				writer.write(entry.getValue());

				writer.newLine();

			}

			writer.flush();
		}

		catch(IOException exception) {}
	}

	
}