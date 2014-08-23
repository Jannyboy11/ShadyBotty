package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class SubBotty extends PircBot {
	public static SubCheckThread thread;
	public static List<String> pendingUsers = Collections.synchronizedList(new ArrayList<String>());
	public static  List<String> subUsers = Collections.synchronizedList(new ArrayList<String>());
	
	public SubBotty() {
		
		this.setName("ShadyBotty");
		thread = new SubCheckThread(this);
		thread.start();
	}
	public String[] getPendingUsersArr() {
		String[] list = (String[])pendingUsers.toArray();
		System.out.println(list.length);
		return  list;
	}
	public String[] getSubUsersArr() {
		String[] list = (String[])subUsers.toArray();
		return  list;
	}
	public List<String> getPendingUsers() {
		List<String> list = Collections.synchronizedList(pendingUsers);
		return  list;
	}
	public List<String> getSubUsers() {
		List<String> list = Collections.synchronizedList(subUsers);
		return  list;
	}
	public void onConnect() {
		sendRawLine("TWITCHCLIENT 3");
		thread = new SubCheckThread(this);
		thread.start();
		return;
	}

	public void onDisconnect() {
		while (!isConnected()) {
			try {
				Thread.sleep(5000);
			} catch(Exception ex) {
			}
			try {
				// Connect to the IRC server.
				BufferedReader blub = new BufferedReader(new FileReader("C:/wachtwoord.txt"));
				String password = blub.readLine();
				blub.close();
				this.connect("irc.twitch.tv",6667,password);

				// Join the #pircbot channel.
				this.joinChannel("#shadybunny");
			} catch(Exception ex) {
			}
		}
	}
	
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		System.out.println(sender + !sender.equalsIgnoreCase("jtv") + !pendingUsers.contains(sender));
		if (!sender.equalsIgnoreCase("jtv") && !pendingUsers.contains(sender.toLowerCase())) {
			pendingUsers.add(sender);
			System.out.println(pendingUsers.size() +"added");
			System.out.println(getPendingUsers().get(0));
		}
		else if (sender.equalsIgnoreCase("jtv") && !subUsers.contains(message.split(" ")[1]) &&
				message.startsWith("SPECIALUSER") && message.endsWith("subscriber")) {
			subUsers.add(message.split(" ")[1]);	
			
			System.out.println(subUsers.size() +"jtv added" + message.split(" ")[1]);
		}
			
	}
}
