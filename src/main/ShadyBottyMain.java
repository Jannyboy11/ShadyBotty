package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import commands.StandardCmds;

public class ShadyBottyMain {
 public static ShadyBotty bot;
	public static void main(String[] args) throws Exception {
		System.out.println(new File(".").getCanonicalPath());
		// Now start our bot up.
		ShadyBotty bot = new ShadyBotty();
		SubBotty subbot = new SubBotty();
		PrivateBotty privbot = new PrivateBotty();
		// Enable debugging output.
		bot.setVerbose(false);
		subbot.setVerbose(false);
		privbot.setVerbose(true);
		// Connect to the IRC server.
		BufferedReader blub = new BufferedReader(new FileReader("C:/wachtwoord.txt"));
		String password = blub.readLine();
		blub.close();
		subbot.connect("irc.twitch.tv",6667,password);
		bot.connect("irc.twitch.tv",6667,password);
		privbot.connect("199.9.248.232",80,password);
		bot.joinChannel("#shadybunny");
		// Join the #pircbot channel.
		subbot.joinChannel("#shadybunny");
		privbot.joinChannel("#_shadybottyroom");
	}

}


