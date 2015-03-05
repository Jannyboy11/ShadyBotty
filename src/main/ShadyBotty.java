package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

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
		System.out.println((database.getPrivileges(sender).getStatus() == Status.MOD && message.split(" ")[0].equals("!score") && msg.length == 3));
		if (database.getPrivileges(sender).getStatus() == Status.MOD && message.split(" ")[0].equals("!score") && msg.length == 3) {
			setTitle(msg[1] + " " + msg[2]);
			sendMessage(ShadyBottyMain.ROOM,"score set!");
			return;
		}
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
		System.out.println(msg[0] + "   " + b);
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
	public void setTitle(String string) {
	if (!string.matches("^(([2-9])|(1[0-2]?))-[0-3] (Warrior|Shaman|Rogue|Paladin|Hunter|Druid|Warlock|Mage|Priest).*"))
		return;
		String title = TwitchAPI.getTitle();
		System.out.println("set title " + title);
		if (title.matches("^(([2-9])|(1[0-2]?))-[0-3] (Warrior|Shaman|Rogue|Paladin|Hunter|Druid|Warlock|Mage|Priest)\\..*")) {
			System.out.println("matches title " + title);
			
			String newtitle = title.substring(title.indexOf(".")+2);
			if (string.trim().endsWith("."))
				TwitchAPI.updateChannel((string + " " + newtitle).replace(" ", "+"));
			else
				TwitchAPI.updateChannel((string + ". " + newtitle).replace(" ", "+"));
		} else {
			TwitchAPI.updateChannel((string + " " + title).replace(" ", "+"));
		}
			
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
	
	@Override
	public void onUserMode(String targetNick, String sourceNick, String sourceLogin,String sourceHostname,String mode) {
		parseOp(mode);
	}

	private void parseOp(String mode) {
		database.addPrivileges(mode.split(" ")[2]);
		if (mode.split(" ")[1].equals("+o")) {
			database.getPrivileges(mode.split(" ")[2]).setStatus(Status.MOD);
		}
	}
	public static void scoreToFile(String theTitle) {
		System.out.println(theTitle);
		if (theTitle.matches("^(([2-9])|(1[0-2]?))-[0-3] (Warrior|Shaman|Rogue|Paladin|Hunter|Druid|Warlock|Mage|Priest)\\..*")) {
			File score = new File("score.txt");
			if (!score.exists())
				try {
					score.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			try {
				BufferedWriter wr = new BufferedWriter(new FileWriter(score));
				wr.write(theTitle.split("\\.")[0]);
				wr.flush();
				wr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



}
