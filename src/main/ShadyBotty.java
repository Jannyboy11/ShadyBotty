package main;

import java.io.BufferedReader;
import java.io.FileReader;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import commands.MadeCmds;
import commands.Shop;
import commands.StandardCmds;
import commands.Util;
import chat.ChatRules;
import chat.Nicknames;
import chat.Pair;
import chat.Privileges.Status;
import api.CheckStreamThread;
import api.RiotAPI;
import api.TwitchAPI;
import points.GivePointsThread;
import points.Points;
import trivia.TriviaThread;

public class ShadyBotty extends PircBot {
	public static Database database;
	public GivePointsThread pointsThread;
	public static CheckStreamThread streamThread;
	public static Points points;
	public static Nicknames nicks;
	public static Shop shop;
	public static MadeCmds madeCmds;
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
		
		database.setLastMessage(sender, System.currentTimeMillis());
		database.addCurrentUsers(sender);
		database.addPrivileges(sender);
		Nicknames.addNick(sender);
		String[] msg = message.split(" ");
		if (StandardCmds.isValidStandardCmd(message,sender,channel))
			return;
		if (!TriviaThread.topic.equals("") && TriviaThread.answers.contains(message.replaceAll("[^a-zA-Z0-9]","").toLowerCase()))
			TriviaThread.addWinner(sender);
		if (database.getPrivileges(sender).getStatus() == Status.MOD && message.split(" ")[0].equals("!next") && (TriviaThread.tr != null)) {
		TriviaThread.tr.interrupt();
		}
		
		if ((database.getPrivileges(sender).getStatus() == Status.MOD || sender.equalsIgnoreCase("shadybunny"))&& msg[0].equals("!title")) {
			TwitchAPI.updateChannel(message.substring(msg[0].length() + 1).replace(" ", "+"));
			sendMessage(channel,"title test!");
		}
		
		if (database.getPrivileges(sender).getStatus() == Status.MOD && message.split(" ")[0].equals("!start")) {
			TriviaThread a = new TriviaThread(this);
			a.off = false;
			a.start();
			System.out.println("in here");
		}
		if (database.getPrivileges(sender).getStatus() == Status.MOD && message.split("")[0].equals("!stop")) {
			TriviaThread.off = true;
		}
		System.out.println("before");
		Pair temp;
		temp = ChatRules.checkMessage(sender, message);
		if (temp.getTimeoutLength() > 0) {
			System.out.println(temp.getTimeoutLength() + " time for timeout for " + sender);
			sendMessage(channel,"/timeout "+ sender+ " " + temp.getTimeoutLength());
			sendMessage(channel,sender + " has been timed out for " 
			+ temp.getTimeoutLength() + " seconds. Reason: "
			+ temp.getReason());
			return;
		}
		if (Shop.isValidShopCommand(message,sender,channel))
			return;

		System.out.println(message);
		String cmd = MadeCmds.chatToCommand(sender,message,database.getPrivileges(sender).getStatus() == Status.MOD);
		System.out.println(cmd);
		if (cmd != null) {
			sendMessage(channel,cmd);
		}
		String b = MadeCmds.getCommand(msg[0]);
		
		if (b != null && !b.equals("")) {
			b = b.replace("(_NICK_)", sender);
			for (int i = 1; i < msg.length && i < 6; i ++) {
				b = b.replace("(_ARG"+i+"_)", msg[i]);
				if (b.contains("(_ARG"+i+"+_)")) {
					String rest = "";
					for (int y = i; y < msg.length; y++) 
						rest += " "+msg[y] ;
					b = b.replace("(_ARG"+i+"+_)", rest);
					break;
				}
			}

			if (b.matches(".*(_ARG\\d\\+?_).*"))
				return;
			else
				sendMessage(channel,b);
		}
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
