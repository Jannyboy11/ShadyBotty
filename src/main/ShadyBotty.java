package main;

import org.jibble.pircbot.PircBot;

public class ShadyBotty extends PircBot {
	public static Database database;
	public ShadyBotty(){
		this.setName("ShadyBotty");
		database = new Database();
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
				this.connect("irc.twitch.tv",6667,"oauth:h9c144makj10x84wdazgf0exgl13p9k");

	        // Join the #pircbot channel.
				this.joinChannel("#shadybunny");
				} catch(Exception ex) {
			}
		}
	}
	
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		sendMessage(channel,"your last message was: " + database.getDifferenceSeconds(sender) + "seconds ago.");
		database.setLastMessage(sender, System.currentTimeMillis());
		database.getTimeLastMessage(sender);
		//CHECK IF USER VIOLATED CHATRULES(CAPS/SWEAR/EMOTES ETC.)
		
		//CHECK IF USER USED A  COMMAND
		
		// CHECK IF HE TRIGGERED AN AUTOREPLY
		
		return;
	}
	

}
