package main;

import java.io.BufferedReader;
import java.io.FileReader;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import commands.Shop;
import commands.StandardCmds;
import chat.ChatRules;
import chat.Nicknames;
import chat.Pair;
import api.CheckStreamThread;
import points.GivePointsThread;
import points.Points;

public class ShadyBotty extends PircBot {
	public static Database database;
	public GivePointsThread pointsThread;
	public static CheckStreamThread streamThread;
	public static Points points;
	public static Nicknames nicks;
	public static Shop shop;
	public static StandardCmds standardCmds;
	
	@Deprecated //use sendToChannel instead.
	public void sendToBunny(String text) {
		sendMessage("#shadybunny",text);
	}
	public void sendToChannel(String room, String message){
		sendMessage(room, message);
	}

	public ShadyBotty(){
		this.setName("ShadyBotty");
		database = new Database();
		points = new Points();
		nicks = new Nicknames(this);
		pointsThread = new GivePointsThread(this);
		pointsThread.start();
		shop = new Shop(this);
		standardCmds = new StandardCmds(this);
		streamThread = new CheckStreamThread(this);
		streamThread.start();
	}
	public GivePointsThread getPointsThread() {
		return pointsThread;
	}
	public void newPointsThread(GivePointsThread thr) {
		this.pointsThread = thr;
		pointsThread.start();
	}
	
	public void onConnect() {
		try {
			Thread.sleep(5000);
			} catch(Exception ex) {
			}
		joinChannel("#shadybunny");
		joinChannel("#shadybunny");
		joinChannel("#shadybunny");
		return;
	}

	public void onDisconnect() {
		while (!isConnected()) {
			try {
				Thread.sleep(5000);
				} catch(Exception ex) {
			}
			try {
				database.clearCurrentUsers();
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
		if (sender.equals("failgaf") || sender.equals("honeybadgerino")) return;
		
		database.setLastMessage(sender, System.currentTimeMillis());
		database.addCurrentUsers(sender);
		database.addPrivileges(sender);
		Nicknames.addNick(sender);
		Pair temp;
		temp = ChatRules.checkMessage(sender, message);
		if (temp.getTimeoutLength() > 0) {
			System.out.println(temp.getTimeoutLength() + " time for timeout for " + sender);
			sendToBunny("/timeout "+ sender+ " " + temp.getTimeoutLength());
			sendToBunny(sender + " has been timed out for " 
			+ temp.getTimeoutLength() + " seconds. Reason: "
			+ temp.getReason());
			return;
		}
		if (Shop.isValidShopCommand(message,sender))
			return;
		if (StandardCmds.isValidStandardCmd(message,sender))
			return;
		
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
			Nicknames.addNick(users[i].getNick());
			
		}

	}
	public void onLeave(String channel, String sender, String login, String hostname) {
		database.delCurrentUsers(sender);
	}
	
	public void timeout(String nick, int duration){
		sendRawLine(".timeout " + nick + duration);
	}



}
