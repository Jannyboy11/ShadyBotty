package main;

import hearthstoneData.ScoreSaver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import display.ChatFrame;

public class ShadyBottyMain {
	public static ShadyBotty bot;
	public static final String ROOM = "#shadybunny";
	public static PrivateBotty privbot = new PrivateBotty();;
 
	private static String config = "[Ben]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Bob]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Cameron]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Derek]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[FaceDownCard]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Kyle]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Mike]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Net]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Power]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Rachelle]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false\n[Zone]\nLogLevel=1\nFilePrinting=false\nConsolePrinting=true\nScreenPrinting=false";

	public static void main(String[] args)  {
		setOut();
		try {
		File a = new File(System.getenv("AppData"));
		File b = new File(a.getParent() +"\\Local\\Blizzard\\Hearthstone\\log.config");
		b.createNewFile();
			BufferedWriter wr = new BufferedWriter(new FileWriter(b));
		wr.write(config);
		wr.flush();
		wr.close();
		System.out.println(new File(".").getCanonicalPath());
		ScoreSaver.createDeck("");
		ChatFrame.main(args);
		// Now start our bot up.
		ShadyBotty bot = new ShadyBotty();
//		SubBotty subbot = new SubBotty();

//		new GetShadyMoveThread().start();
		// Enable debugging output.
		bot.setVerbose(true);
//		subbot.setVerbose(false);
//		privbot.setVerbose(true);



//		 Connect to the IRC server.
//		BufferedReader blub = new BufferedReader(new FileReader("C:/wachtwoord.txt"));
//		String password = blub.readLine();
//		blub.close();

		String password = "oauth:n6yzqpe8uqqd7qh8fgqh0f1yyb6lq1";
		privbot.setMessageDelay(1500L);
		privbot.connect("199.9.253.119",6667,password);
		
//		subbot.connect("irc.twitch.tv",6667,password);
		bot.connect("irc.twitch.tv",6667,password);
		RekScript q = new RekScript();
		q.setVerbose(false);
		q.connect("irc.twitch.tv",6667,"oauth:6s3zahxold5ibp84c7r60gqr5jswzo");
		q.joinChannel("#shadybunny");
		bot.joinChannel(ROOM);
//		subbot.joinChannel(ROOM);
		System.err.println("################ STARTED UP CORRECTLY #############");
		
		// Join the #pircbot channel.
		//.joinChannel(ROOM);

//		privbot.joinChannel("#_shadybottyroom");
	

		//Util.putSettingsCorrectFormat();
		Thread.sleep(10000L);
		Long last = Database.getCurrencies().get("lastOn", "lastOn",Long.class);
		if (last == null || System.currentTimeMillis() - last  > 75000L)
			Database.sendSubs("ShadyBunny is online! ");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	
	
}


