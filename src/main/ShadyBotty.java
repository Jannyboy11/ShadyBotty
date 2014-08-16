package main;

import java.io.BufferedReader;
import java.io.FileReader;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import api.CheckStreamThread;
import points.GivePointsThread;
import points.Points;

public class ShadyBotty extends PircBot {
	public static Database database;
	public static GivePointsThread pointsThread;
	public static CheckStreamThread streamThread;
	public static Points points;
	public void sendToBunny(String text) {
		sendMessage("#shadybunny",text);
	}

	public ShadyBotty(){
		this.setName("ShadyBotty");
		database = new Database();
		points = new Points();
		pointsThread = new GivePointsThread(this);
		pointsThread.start();
		streamThread = new CheckStreamThread(this);
		streamThread.start();
	}
	
	public void onConnect() {
//something
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
				database.clearCurrentUsers();
				} catch(Exception ex) {
			}
		}
	}
	
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {

		database.setLastMessage(sender, System.currentTimeMillis());
		
		//CHECK IF USER VIOLATED CHATRULES(CAPS/SWEAR/EMOTES ETC.)
		
		//CHECK IF USER USED A  COMMAND
		
		// CHECK IF HE TRIGGERED AN AUTOREPLY
		
		return;
	}
	public void onJoin(String channel, String sender, String login, String hostname) {
		database.addCurrentUsers(sender);
		database.addPrivileges(sender);
	}
	public void onUserList(String channel, User[] users) {
		for (int i =0; i< users.length;i++){
			database.addCurrentUsers(users[i].getNick());
			database.addPrivileges(users[i].getNick());
		}

	}
	public void onLeave(String channel, String sender, String login, String hostname) {
		database.delCurrentUsers(sender);
	}



}
