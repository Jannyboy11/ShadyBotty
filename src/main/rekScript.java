package main;

import org.jibble.pircbot.PircBot;

public class rekScript extends PircBot {
	
	public rekScript(){
		this.setName("shadybunny");
	}
	
	@Override
	public void onJoin(String channel, String sender, String login, String hostname) {
		System.out.println("test" );
		this.disconnect();
	}

	public static void main(String[] args) throws Exception{
		rekScript a = new rekScript();
		a.setVerbose(true);
		a.connect("irc.twitch.tv",6667,"oauth:kme7og92u2qluzu8f6ax2mo4fh4lja");
		a.joinChannel("#shadybunny");
	}

}
