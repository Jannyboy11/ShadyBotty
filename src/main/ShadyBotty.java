package main;

import org.jibble.pircbot.PircBot;

public class ShadyBotty extends PircBot {

	public ShadyBotty(){
		this.setName("ShadyBotty");
	}

	public void onDisconnect() {
		while (!isConnected()) {
			try {
				Thread.sleep(5000);
				} catch(Exception ex) {
			}
			try {
   		// Connect to the IRC server.
				this.connect("irc.twitch.tv",6667,"oauth:h9c144makj10x84wdazgf0exgl13p9k");

	        // Join the #pircbot channel.
				this.joinChannel("#shadybunny");
				} catch(Exception ex) {
			}
		}
	}
	
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
	}
	

}
