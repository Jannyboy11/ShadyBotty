package main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import api.GetShadyMoveThread;
import commands.StandardCmds;

public class ShadyBottyMain {
	public static ShadyBotty bot;
	public static final String ROOM = "#shadybunny";
 
	public static void main(String[] args) throws Exception {
		setOut();
		System.out.println(new File(".").getCanonicalPath());
		// Now start our bot up.
		ShadyBotty bot = new ShadyBotty();
		SubBotty subbot = new SubBotty();
		PrivateBotty privbot = new PrivateBotty();
//		new GetShadyMoveThread().start();
		// Enable debugging output.
		bot.setVerbose(false);
		subbot.setVerbose(false);
		privbot.setVerbose(true);
		// Connect to the IRC server.
//		BufferedReader blub = new BufferedReader(new FileReader("C:/wachtwoord.txt"));
//		String password = blub.readLine();
//		blub.close();

		String password = "oauth:tls40isx4l73vq8prflalgq1t9sitn";
		subbot.connect("irc.twitch.tv",6667,password);
		bot.connect("irc.twitch.tv",6667,password);
		privbot.connect("199.9.248.232",80,password);
		bot.joinChannel(ROOM);
		// Join the #pircbot channel.
		subbot.joinChannel(ROOM);
		privbot.joinChannel("#_shadybottyroom");
	}

	private static void setOut() {
		File file = new File("Logs.log");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			System.setErr(new ShadyStream(System.err));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}


