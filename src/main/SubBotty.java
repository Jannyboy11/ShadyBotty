package main;

import java.util.ArrayList;
import org.jibble.pircbot.PircBot;
import chat.Privileges;

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
	@Override
	public void onConnect() {
		sendRawLine("TWITCHCLIENT 3");
		return;
	}

	@Override
	public void onDisconnect() {
		while (!isConnected()) {
			try {
				Thread.sleep(5000);
			} catch(Exception ex) {
			}
			try {
				// Connect to the IRC server.
				String password ="oauth:n6yzqpe8uqqd7qh8fgqh0f1yyb6lq1";
				this.connect("irc.twitch.tv",6667,password);

				// Join the #pircbot channel.
				this.joinChannel(ShadyBottyMain.ROOM);
			} catch(Exception ex) {
			}
		}
	}
	
	@Override
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		String[] msg = message.split(" ");
		if (!sender.equalsIgnoreCase("jtv") && !pendingUsers.contains(sender.toLowerCase())) {
			pendingUsers.add(sender);
		}
		else if (sender.equalsIgnoreCase("jtv")) {
			Database.d.addPrivileges(msg[1]);
//			System.out.println("jtv: " + message);
			if (message.startsWith("SPECIALUSER") && !subUsers.contains(msg[1]) &&
				 message.endsWith("subscriber")) {
			subUsers.add(msg[1]);
			} else if (message.startsWith("EMOTESET")) {
				Privileges user = null;
				try {
					
					user = ShadyBotty.database.getPrivileges(msg[1]);
					String emotesets = msg[2].substring(1,msg[2].length()-1);
					if (user == null || !user.equalEmoteSetSize(emotesets.length()))
					user.setEmotesets(emotesets.split(","));
//					System.out.println(emotesets);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

				
			} else if (message.startsWith("USERCOLOR")) {
				Privileges user = null;
				try {
					
					user = ShadyBotty.database.getPrivileges(msg[1]);
					user.chatcolor = msg[2];
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}
			
	}
}
