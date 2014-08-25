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
	public static ArrayList<String> pendingUsers;
	public static  ArrayList<String> subUsers;
	
	public SubBotty() {
		subUsers =  new ArrayList<String>();
		pendingUsers  = new ArrayList<String>();
		this.setName("ShadyBotty");
		thread = new SubCheckThread(this);
		thread.start();
	}

	public ArrayList<String> getPendingUsers() {
		ArrayList<String> list = pendingUsers;
		return  list;
	}
	public ArrayList<String> getSubUsers() {
		ArrayList<String> list = subUsers;
		return  list;
	}
	public void onConnect() {
		sendRawLine("TWITCHCLIENT 3");
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
		if (!sender.equalsIgnoreCase("jtv") && !pendingUsers.contains(sender.toLowerCase())) {
			pendingUsers.add(sender);
		}
		else if (sender.equalsIgnoreCase("jtv") && !subUsers.contains(message.split(" ")[1]) &&
				message.startsWith("SPECIALUSER") && message.endsWith("subscriber")) {
			subUsers.add(message.split(" ")[1]);	
		}
			
	}
}
