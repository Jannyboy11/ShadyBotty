package main;

import hearthstoneData.ScoreSaver;
import hearthstoneData.TestThread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import commands.MadeCmds;
import commands.Shop;
import commands.StandardCmds;
import commands.autoreplies.Autoreplies;
import chat.ChatRules;
import chat.Nicknames;
import chat.Pair;
import chat.Privileges.Status;
import display.ChatFrame;
import api.CheckStreamThread;
import api.TwitchAPI;
import points.GivePointsThread;
import points.Points;
import trivia.TriviaThread;

public class ShadyBotty extends PircBot {
	public static Database database;
	public GivePointsThread pointsThread;
	public static CheckStreamThread streamThread;
	public static Points points;
	public static Nicknames nicks;
	public static Shop shop;
	public static MadeCmds madeCmds;
	public static StandardCmds standardCmds;
	private static Pattern title = Pattern.compile("^(?<score>((0|[2-9]|1[0-2]?)-[0-3])|In)(( )(?<hero>(?i:Warrior|Shaman|Rogue|Paladin|Hunter|Druid|Warlock|Mage|Priest|Draft)))?[^a-zA-Z]*(?<rest>.*)");
	private static Pattern score = Pattern.compile("^(?<score>(0|[2-9]|1[0-2]?)-[0-3])(( )(?<hero>(?i:Warrior|Shaman|Rogue|Paladin|Hunter|Druid|Warlock|Mage|Priest|Draft)))?[^a-zA-Z]*");


	@Deprecated //use sendToChannel instead.
	public void sendToBunny(String text) {
		sendMessage("#shadybunny",text);
	}

	@notNull
	public void sendNoNullMessage(String room, String message){
		if (message != null)
			sendMessage(room, message);
	}

	private TestThread a;

	public ShadyBotty(){
		this.setName("ShadyBotty");
		database = new Database();
		points = new Points();
		nicks = new Nicknames(this);
		pointsThread = new GivePointsThread(this);
		pointsThread.start();
		shop = new Shop(this);
		standardCmds = new StandardCmds(this);
		streamThread = new CheckStreamThread(this);
		streamThread.start();
		a = new TestThread(this);
		a.wins = 0;
		a.losses = 0;
		a.start();
	}
	public GivePointsThread getPointsThread() {
		return pointsThread;
	}
	public void newPointsThread(GivePointsThread thr) {
		this.pointsThread = thr;
		pointsThread.start();
	}

	@Override
	public void onConnect() {
		try {
			Thread.sleep(5000);
		} catch(Exception ex) {
		}
		joinChannel("#shadybunny");
		sendRawLine("CAP REQ :twitch.tv/membership, twitch.tv/commands");
		return;
	}

	@Override
	public void onDisconnect() {
		while (!isConnected()) {
			try {
				Thread.sleep(5000);
			} catch(Exception ex) {
			}
			try {
				database.clearCurrentUsers();
				// Connect to the IRC server.

				String password = "oauth:n6yzqpe8uqqd7qh8fgqh0f1yyb6lq1";
				this.connect("irc.twitch.tv",6667,password);

				// Join the #pircbot channel.
				this.joinChannel("#shadybunny");

			} catch(Exception ex) {
			}
		}
	}

	@Override
	@SuppressWarnings("unused")
	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		if (sender.equalsIgnoreCase("jb940") && message.startsWith("!restart")) {
			sendMessage(channel, "restarting bot...");
			try {
				Process proc = Runtime.getRuntime().exec("java -jar ./shadybotty.jar minimized");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.exit(0);
		}
		database.setLastMessage(sender, System.currentTimeMillis());
		database.addCurrentUsers(sender);
		database.addPrivileges(sender);
		Nicknames.addNick(sender);
		ChatFrame.addText(sender, message,false);

		Pair temp;
		temp = ChatRules.checkMessage(sender, message);
		if (temp.getTimeoutLength() > 0) {
			System.out.println(temp.getTimeoutLength() + " time for timeout for " + sender);
			sendMessage(channel,"/timeout "+ sender+ " " + temp.getTimeoutLength());
			sendMessage(channel,sender + " has been timed out for " 
					+ temp.getTimeoutLength() + " seconds. Reason: "
					+ temp.getReason());
			return;
		}



		String[] msg = message.split(" ");
		System.out.println("before chatTuAut");
		
		if (msg.length == 1 && ("!subscribe".equalsIgnoreCase(msg[0])))	{
			if (Database.addSub(sender))
				sendMessage(channel,sender + " has been successfully subscribed.");
				
		}
		
		if (msg.length == 1 && ("!unsubscribe".equalsIgnoreCase(msg[0])))	{
			if (Database.deleteSub(sender))
				sendMessage(channel,sender + " has been successfully unsubscribed.");
				
		}
		
		sendNoNullMessage(channel,Autoreplies.chatToAutoreply(sender,message,database.getPrivileges(sender).getStatus() == Status.MOD));


		if (ScoreSaver.isScoreRequest(message)) {
			String stats = ScoreSaver.getStats(message);
			if (stats != null)
				sendMessage(ShadyBottyMain.ROOM,stats);
		}
		if ((sender.equalsIgnoreCase("jb940") || database.getPrivileges(sender).getStatus() == Status.MOD) && msg[0].equalsIgnoreCase("!deck") && msg.length >= 1) {
			//		System.out.println("TEST 1!");
			ScoreSaver.createDeck(msg[1]);	
			sendMessage(ShadyBottyMain.ROOM, "TRIEDED IT!");
		}
		if (database.getPrivileges(sender).getStatus() == Status.MOD && msg[0].equalsIgnoreCase("!win") && msg.length >= 2) {
			if (msg[1].equalsIgnoreCase("add")) {
				a.wins +=1;
				sendMessage(ShadyBottyMain.ROOM, "added a win!");
			} else if (msg[1].equalsIgnoreCase("remove") || msg[1].equalsIgnoreCase("rem")) {
				a.wins -=1;
				sendMessage(ShadyBottyMain.ROOM, "removed a win!");
			}
			setTitle(a.wins + "-" + a.losses + " " + a.hero2);
		}else if (database.getPrivileges(sender).getStatus() == Status.MOD && msg[0].equalsIgnoreCase("!loss") && msg.length >= 2) {
			if (msg[1].equalsIgnoreCase("add")) {
				a.losses +=1;
				sendMessage(ShadyBottyMain.ROOM, "added a loss!");
			} else if (msg[1].equalsIgnoreCase("remove") || msg[1].equalsIgnoreCase("rem")) {
				a.losses -=1;
				sendMessage(ShadyBottyMain.ROOM, "removed a loss!");
			}
			setTitle(a.wins + "-" + a.losses + " " + a.hero2);
		} else if (database.getPrivileges(sender).getStatus() == Status.MOD && msg[0].equalsIgnoreCase("!score") && msg.length == 3) {
			System.out.println(msg[1] + " " +msg[2]);
			setTitle(msg[1] + " " +msg[2]);
			try {
				a.wins = Integer.parseInt(msg[1].split("-")[0]);
				a.losses = Integer.parseInt(msg[1].split("-")[1]);
			} catch (Exception e) {
			}
			sendMessage(ShadyBottyMain.ROOM,"Settted title for u!");
		}
		if (database.getPrivileges(sender).getStatus() == Status.MOD && msg[0].equalsIgnoreCase("!resetscore"))
			a.reset();
		//		System.out.println("here!!!!");
		if (StandardCmds.isValidStandardCmd(message,sender,channel))
			return;

		if (!TriviaThread.topic.equals("") && TriviaThread.answers.contains(message.replaceAll("[^a-zA-Z0-9]","").toLowerCase()))
			TriviaThread.addWinner(sender);
		if (database.getPrivileges(sender).getStatus() == Status.MOD && message.split(" ")[0].equals("!next") && (TriviaThread.tr != null)) {
			TriviaThread.tr.interrupt();
		}

		if ((database.getPrivileges(sender).getStatus() == Status.MOD || sender.equalsIgnoreCase("shadybunny"))&& msg[0].equals("!title")) {
			TwitchAPI.updateChannel(message.substring(msg[0].length() + 1).replace(" ", "+"));
			sendMessage(channel,"title test!");
		}

		if (database.getPrivileges(sender).getStatus() == Status.MOD && message.split(" ")[0].equals("!start")) {
			TriviaThread a = new TriviaThread(this);
			TriviaThread.off = false;
			a.start();
			//			System.out.println("in here");
		}
		if (database.getPrivileges(sender).getStatus() == Status.MOD && message.split("")[0].equals("!stop")) {
			TriviaThread.off = true;
		}



		//		System.out.println("before");

		if (Shop.isValidShopCommand(message,sender,channel))
			return;

		//		System.out.println(message);
		sendNoNullMessage(channel,MadeCmds.chatToCommand(sender,message,database.getPrivileges(sender).getStatus() == Status.MOD));
		System.out.println("before get resp");
		sendNoNullMessage(channel,Autoreplies.getResponse(message));
		System.out.println("after get resp");

		String b = MadeCmds.getCommand(msg[0]);
		if (b != null && !b.equals("")) {
			b = b.replace("(_NICK_)", sender);
			for (int i = 1; i < msg.length && i < 6; i ++) {
				b = b.replace("(_ARG"+i+"_)", msg[i]);
				if (b.contains("(_ARG"+i+"+_)")) {
					String rest = "";
					for (int y = i; y < msg.length; y++) 
						rest += " "+msg[y] ;
					b = b.replace("(_ARG"+i+"+_)", rest);
					break;
				}
			}

			if (b.matches(".*(_ARG\\d\\+?_).*"))
				return;
			else {
				sendMessage(ShadyBottyMain.ROOM,b);
			}
		}
		// CHECK IF HE TRIGGERED AN AUTOREPLY

		return;
	}
	public static void main(String[] args) {
		ShadyBotty bot = new ShadyBotty();
		bot.setTitle("12-3 Priest");
	}
	public void setTitle(String string) {
		Matcher match = score.matcher(string);
		//		System.out.println("before");
		if (!match.matches())
			return;
		//		System.out.println("string matches");
		String titlee = TwitchAPI.getTitle();
		Matcher current = title.matcher(titlee);
		//		System.out.println("title: " + titlee);

		if (current.matches()) {
			//			System.out.println("matches title " + titlee);
			String newTitle = match.group("hero") != null && 
					match.group("hero").equalsIgnoreCase("draft") ? "In ": match.group("score") + " " ;
			if (match.group("hero") == null || match.group("hero").equals("null")){
				newTitle += current.group("hero");
			} else {
				String hero = match.group("hero");
				hero = hero.substring(0,1).toUpperCase() + hero.substring(1).toLowerCase();
				newTitle += hero;
			}
			//					System.out.println(newTitle + "    " + current.group("rest"));
			TwitchAPI.updateChannel(newTitle + ". " + current.group("rest"));
		} else {
			String newTitle = match.group("hero") != null && 
					match.group("hero").equalsIgnoreCase("draft") ? "In ": match.group("score") + " " ;
			newTitle +=  match.group("hero") + ". " +titlee;
			TwitchAPI.updateChannel(newTitle);
		}

	}
	@Override
	public void onJoin(String channel, String sender, String login, String hostname) {
		database.addCurrentUsers(sender);
		database.addPrivileges(sender);
	}
	@Override
	public void onUserList(String channel, User[] users) {
		for (int i =0; i< users.length;i++){
			database.addCurrentUsers(users[i].getNick());
			database.addPrivileges(users[i].getNick());
			Nicknames.addNick(users[i].getNick());

		}

	}
	public void onLeave(String channel, String sender, String login, String hostname) {
		database.delCurrentUsers(sender);
	}

	public void timeout(String nick, int duration){
		sendRawLine(".timeout " + nick + duration);
	}

	@Override
	public void onUserMode(String targetNick, String sourceNick, String sourceLogin,String sourceHostname,String mode) {
		parseOp(mode);
	}

	private void parseOp(String mode) {
		database.addPrivileges(mode.split(" ")[2]);
		if (mode.split(" ")[1].equals("+o")) {
			database.getPrivileges(mode.split(" ")[2]).setStatus(Status.MOD);
		}
	}
	public static void scoreToFile(String theTitle) {
		System.out.println(theTitle);
		Matcher match = title.matcher(theTitle);
		System.out.println(match.matches());
		if (match.matches()) {
			File score = new File("score.txt");
			System.out.println(score.getAbsolutePath());
			if (!score.exists())
				try {
					score.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			try {
				BufferedWriter wr = new BufferedWriter(new FileWriter(score));

				wr.write(match.group("score") + " " + match.group("hero"));
				wr.flush();
				wr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onNotice(String sourceNick, String sourceLogin,
			String sourceHostname, String target, String notice) {
	
	System.out.println( notice);
	}
	
	
	


}
