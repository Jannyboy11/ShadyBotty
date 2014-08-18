package main;

import java.io.BufferedReader;
import java.io.FileReader;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import chat.ChatRules;
import chat.Pair;
import api.CheckStreamThread;
import points.GivePointsThread;
import points.Points;

public class ShadyBotty extends PircBot {
	public static Database database;
	public static GivePointsThread pointsThread;
	public static CheckStreamThread streamThread;
	public static Points points;
	public static ChatRules rules;
	public void sendToBunny(String text) {
		sendMessage("#shadybunny",text);
	}

	public ShadyBotty(){
		this.setName("ShadyBotty");
		database = new Database();
		points = new Points();
		rules = new ChatRules(this);
		pointsThread = new GivePointsThread(this);
		pointsThread.start();

		streamThread = new CheckStreamThread(this,pointsThread);
		streamThread.start();
	}
	public void newPointsThread(GivePointsThread thr) {
		this.pointsThread = thr;
		pointsThread.start();
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
		database.addCurrentUsers(sender);
		database.addPrivileges(sender);
		Pair temp;
		System.out.println("before check");
		temp = rules.checkMessage(sender, message);
		if (temp.getTimeoutLength() > 0) {
			System.out.println(temp.getTimeoutLength() + " time for timeout for " + sender);
			sendToBunny("/timeout "+ sender+ " " + temp.getTimeoutLength());
			sendToBunny(sender + " has been timed out for " 
			+ temp.getTimeoutLength() + " seconds. Reason: "
			+ temp.getReason());
			return;
		}
		
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
	
	public void timeout(String nick, int duration){
		sendRawLine(".timeout " + nick + duration);
	}



}
