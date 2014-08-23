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
		// Enable debugging output.
		bot.setVerbose(false);
		subbot.setVerbose(true);
		// Connect to the IRC server.
		BufferedReader blub = new BufferedReader(new FileReader("C:/wachtwoord.txt"));
		String password = blub.readLine();
		blub.close();
		subbot.connect("irc.twitch.tv",6667,password);
		bot.connect("irc.twitch.tv",6667,password);

		// Join the #pircbot channel.
		bot.joinChannel("#shadybunny");
		subbot.joinChannel("#shadybunny");
	}

}


