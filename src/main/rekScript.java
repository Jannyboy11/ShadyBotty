package main;


import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class RekScript extends PircBot {
	
	String toUnban = "theclonedone rubbinns kinitis1 nikisan4 haron6 k1nder_surprise caosguard volodarsbd ivankatok ramsay_bolton stady23 bageira binocunha eldenno7 charlieflipsh bavzen martinzrn masterpiecee kenkengor sjnez gor49 koska21 asithestrong swuggmaisterflex lineage199tlt defenderrip grufalo2 ekriversbeer welds2562 umeaver1980 gund1993 anding799 yforre7798 thelegitguy23 pkjq xxxtuck_frumpxxx tootiredforsoccertommrow letmewatchadsfrommyphone seyter20 winstrolltv calculatedd tobbsken jeandujardin777 exreval mordbrandinyll jonathan23ludwig20 leaguee_of_legends thishasswag tegglaj lelm8zz calpacks";
	
	public RekScript(){
		this.setName("shadybunny");
	}
	
	public static void main(String[] args) {
		RekScript a = new RekScript();
		a.setVerbose(false);
		a.setVerbose(true);
		try {
			a.connect("irc.twitch.tv",6667,"oauth:6s3zahxold5ibp84c7r60gqr5jswzo");
		} catch (IOException | IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a.joinChannel("#shadybunny");
		a.sendMessage("#shadybunny","/mod shadybotty");
	}
	
	@Override
	public void onJoin(String channel, String sender, String login, String hostname) {
		sendMessage("#shadybunny","/mod jb940");
		System.out.println("connected");
	}
	
	@Override
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		if (sender.equalsIgnoreCase("jb940") && message.split(" ")[0].equalsIgnoreCase("!bug")) {
			sendMessage("#shadybunny","/unmod " + message.split(" ")[1] );
			sendMessage("#shadybunny","/timeout "+message.split(" ")[1]+" " + message.split(" ")[2]);
			try {
				Thread.sleep(Integer.parseInt(message.split(" ")[2]) * 1000 );
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			sendMessage("#shadybunny","/mod " + message.split(" ")[1]);
		}
	}


}

