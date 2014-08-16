package main;

import org.jibble.pircbot.PircBot;

import points.GivePointsThread;

public class ShadyBotty extends PircBot {
	public static Database database;
	public static GivePointsThread pointsThread;
	
	public void sendToBunny(String text) {
		sendMessage("#shadybunny",text);
	}

	public ShadyBotty(){
		this.setName("ShadyBotty");
		database = new Database();
		pointsThread = new GivePointsThread(this);
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
				this.connect("irc.twitch.tv",6667,"oauth:h9c144makj10x84wdazgf0exgl13p9k");

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
	}



}
