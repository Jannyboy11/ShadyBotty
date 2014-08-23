package main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.ini4j.Wini;

import chat.Privileges;

public class Database {
	private HashMap<String,Long> lastChat;
	private HashMap<String,Privileges> privileges;
	public static ArrayList<String> currentUsers;
	public static Wini usersIni;
	public static Wini currenciesIni;


	public Database() {
		try {
			usersIni = new Wini(new File("users.ini"));
			currenciesIni = new Wini(new File("currencies.ini"));
		lastChat = new HashMap<String,Long>();
		privileges = new HashMap<String,chat.Privileges>();
		currentUsers = (new ArrayList<String>());

		} catch (IOException e) { 
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}




	public Privileges getPrivileges(String user) {
		return privileges.get(user);
	}

	public void addPrivileges(String nick) {
		if (!privileges.containsKey(nick))
		privileges.put(nick, new Privileges(nick));
	}


	
	
	public void setLastMessage(String user, Long time) { 
		lastChat.put(user, time);
	}




	public Long getLongLastMessage(String user) { 
		Long last = lastChat.get(user);
		return last;
	}

	public String getTimeLastMessage(String user) { 
		Long last = getLongLastMessage(user);
		Date lastDate = new Date(last);
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
		return ft.format(lastDate);
	}




	public Long getDifferenceInLong(String user) {
		Long last =  System.currentTimeMillis();;

		if (lastChat.containsKey(user)) 
			last = lastChat.get(user);
		else return new Long(6000000);
		
		Long now = System.currentTimeMillis();
		Long difference = now-last;
		return difference;
	}

	public Long getDifferenceSeconds(String user) {
		Long difference = getDifferenceInLong(user);
		return difference/1000;
	}



	public void clearCurrentUsers() {
		currentUsers.clear();
	}

	public ArrayList<String> getCurrentUsers() {
		return currentUsers;
	}
	
	
	
	
	
	public void addCurrentUsers(String nick) {
		if (!currentUsers.contains(nick))
		currentUsers.add(nick.toLowerCase());
	}
	
	public void delCurrentUsers(String nick) {
		if (currentUsers.contains(nick))
		currentUsers.remove(nick);
	}
	


}
