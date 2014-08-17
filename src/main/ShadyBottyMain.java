package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ShadyBottyMain {

	public static void main(String[] args) throws Exception {
		System.out.println(new File(".").getCanonicalPath());
		// Now start our bot up.
		ShadyBotty bot = new ShadyBotty();

		// Enable debugging output.
		bot.setVerbose(true);

		// Connect to the IRC server.
		BufferedReader blub = new BufferedReader(new FileReader("C:/wachtwoord.txt"));
		String password = blub.readLine();
		blub.close();
		bot.connect("irc.twitch.tv",6667,password);

		// Join the #pircbot channel.
		bot.joinChannel("#shadybunny");

	}

}


