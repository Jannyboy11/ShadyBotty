package chat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import main.Database;
import main.ShadyBotty;

import org.ini4j.Wini;

import chat.Privileges.Status;
import points.Points;

public class Nicknames {
	public static HashMap<String,String> nickList;
	private static ShadyBotty bot;
	public Nicknames(ShadyBotty botty) {
		nickList = new HashMap<String,String>();
		bot = botty;
	}

	public static String getNick(String user) {
		return nickList.get(user) == null ? user : nickList.get(user);
	}

	public static void addNick(String nn){
		Wini ini;
			ini = Database.usersIni;
			System.out.println(nn + "  " + ini.get(nn,"nick"));
			nickList.put(nn.toLowerCase(),ini.get(nn,"nick") == null ? nn : ini.get(nn,"nick"));
	}	
	public static void ChangeNick(String nn, String nick){
		if (ShadyBotty.database.getPrivileges(nn).getStatus() == Status.VIEWER 
				|| ShadyBotty.database.getPrivileges(nn).getStatus() == Status.REGULAR) return;
		try {
		
			if (nickList.containsKey(nick.toLowerCase()) || nickList.containsValue(nick) || ShadyBotty.database.getPrivileges(nick.toLowerCase()) != null ||  Database.usersIni.get(nick) != null || Database.currentUsers.contains(nick.toLowerCase())) {
				bot.sendToBunny("Dear " + getNick(nn) + ", this nick is already in use or a username.");
				return;
			}
			if (nick.length() > 16) {
				bot.sendToBunny("Dear " + getNick(nn) + ", this nick is too long.");
				return;
			}
			String goodnick = (double)ChatRules.countUppercase(nick)/(double)nick.length() > (double) 0.4 ? 
					nick.substring(0,1).toUpperCase() + nick.substring(1).toLowerCase() : nick;
			nickList.put(nn,goodnick);
			Wini ini = Database.usersIni;
			ini.put(nn,"nick",goodnick);
			ini.store();
			bot.sendToBunny("Dear " + getNick(nn) + ", your nick has been changed for 50 points to: " + goodnick);
			Points.delPoints(nn, 50);
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
