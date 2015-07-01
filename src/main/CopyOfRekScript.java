package main;


import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class CopyOfRekScript extends PircBot {
	
	String toUnban = "theclonedone rubbinns kinitis1 nikisan4 haron6 k1nder_surprise caosguard volodarsbd ivankatok ramsay_bolton stady23 bageira binocunha eldenno7 charlieflipsh bavzen martinzrn masterpiecee kenkengor sjnez gor49 koska21 asithestrong swuggmaisterflex lineage199tlt defenderrip grufalo2 ekriversbeer welds2562 umeaver1980 gund1993 anding799 yforre7798 thelegitguy23 pkjq xxxtuck_frumpxxx tootiredforsoccertommrow letmewatchadsfrommyphone seyter20 winstrolltv calculatedd tobbsken jeandujardin777 exreval mordbrandinyll jonathan23ludwig20 leaguee_of_legends thishasswag tegglaj lelm8zz calpacks";
	
	public CopyOfRekScript(){
		this.setName("shadybunny");
	}
	
	public static void main(String[] args) {
		CopyOfRekScript a = new CopyOfRekScript();
		a.setVerbose(true);
		try {
			a.connect("199.9.253.119",6667,"oauth:6s3zahxold5ibp84c7r60gqr5jswzo");
		} catch (IOException | IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a.joinChannel("#shadybunny");
	}
	
	@Override
	public void onJoin(String channel, String sender, String login, String hostname) {
	}
	
	@Override
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
	
	}


}

