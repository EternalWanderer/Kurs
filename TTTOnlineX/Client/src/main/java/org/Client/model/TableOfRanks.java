package org.Client.model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TableOfRanks {
	
	private static String path = "C:/Users/Akarumey/Desktop/University/5 Semester/TP/dataRanks.txt";
	
	private static HashMap<String, Integer> dataBase = new LinkedHashMap<String,Integer>();

	public static synchronized void writeWinner(String name) {
		readFile();
		for(HashMap.Entry<String, Integer> pair : dataBase.entrySet()) {
			if(name.equals(pair.getKey())) {
				int temp = pair.getValue();
				pair.setValue(temp+5);
			}	
		}
		writeDataBase();	
	}
	
	public static synchronized void writeLoser(String name) {
		readFile();
		for(HashMap.Entry<String, Integer> pair : dataBase.entrySet()) {
			if(name.equals(pair.getKey())) {
				int temp = pair.getValue();
				pair.setValue(temp-5);
			}	
		}
		writeDataBase();	
	}
	
	public synchronized static void setPlayerAndInitialValue(String name) {
		readFile();
		dataBase.put(name, 100);
		writeDataBase();
		
	}
		
	public synchronized static void readFile() {
          File file = new File(path);
          String words[]; 
          String line;
          try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file))))
          {
              while((line = br.readLine()) != null){
                  words = line.split(" ");
                  dataBase.put(words[0],Integer.parseInt(words[1]));
              }
              br.close();
          } catch (FileNotFoundException e) {
                  e.printStackTrace();
          } catch (IOException e) {
                  e.printStackTrace();
          }    
	}
	
	public synchronized static void writeDataBase() {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(path));) {
			for(HashMap.Entry<String, Integer> pair : dataBase.entrySet()) {
				writer.write(pair.getKey() + " " + pair.getValue() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized static String[][] getPlayerList() {
		readFile();
		Vector<String> pl = new Vector<String>();
		
		String[][] plT = new String[dataBase.size()][3];
		String[][] tempSortPL = new String[dataBase.size()][3];
		
		HashMap<String, Integer> sortMap = sortByValue(dataBase);
		
		
		for(HashMap.Entry<String, Integer> pair : sortMap.entrySet()) {
				pl.add(pair.getKey() + " [" + pair.getValue() + "]\n");		
		}
		int j = 0;
		for (Iterator<Map.Entry<String, Integer>> i = sortMap.entrySet().iterator(); i.hasNext(); ) {
			  final Map.Entry<String, Integer> entry = i.next();
			  Integer rank = dataBase.size()-j;
			  plT[j][0] = rank.toString();
			  plT[j][1] = entry.getKey();
			  plT[j][2] = entry.getValue().toString();
			  	j +=1;
			}
		
		
		for(int i = pl.size() - 1, z = 0; i >= 0; i--, z++) {
			tempSortPL[z][0] = plT[i][0];
			tempSortPL[z][1] = plT[i][1];
			tempSortPL[z][2] = plT[i][2];
		} 
		
		return tempSortPL;
	}
	
	 public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
	    { 
	        List<Map.Entry<String, Integer> > list = 
	               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
	  
	        
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
	            public int compare(Map.Entry<String, Integer> o1,  
	                               Map.Entry<String, Integer> o2) 
	            { 
	                return (o1.getValue()).compareTo(o2.getValue()); 
	            } 
	        }); 

	        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
	        for (Map.Entry<String, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
	    } 
	
	
}


