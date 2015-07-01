package chat;

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
		return nickList.get(user.toLowerCase()) == null ? user : nickList.get(user.toLowerCase());
	}

	public static void addNick(String nn){
		Wini ini;
			ini = Database.getUsers();
			nickList.put(nn.toLowerCase(),ini.get(nn,"nick") == null ? nn : ini.get(nn,"nick"));
	}	
	public static void ChangeNick(String nn, String nick){
		if (ShadyBotty.database.getPrivileges(nn).getStatus() == Status.VIEWER 
				|| ShadyBotty.database.getPrivileges(nn).getStatus() == Status.REGULAR) return;
	
		
			if (!nick.equalsIgnoreCase(nn) && (nickList.containsKey(nick.toLowerCase()) || nickList.containsValue(nick) || ShadyBotty.database.getPrivileges(nick.toLowerCase()) != null ||  Database.getUsers().get(nick.toLowerCase()) != null || Database.currentUsers.contains(nick.toLowerCase()))) {
				
				bot.sendToBunny("Dear " + getNick(nn) + ", this nick is already in use or a username.");
				return;
			}
			if (nick.length() > 16) {
				bot.sendToBunny("Dear " + getNick(nn) + ", this nick is too long.");
				return;
			}
			String goodnick = (double)ChatRules.countUppercase(nick)/(double)nick.length() > 0.4 ? 
					nick.substring(0,1).toUpperCase() + nick.substring(1).toLowerCase() : nick;

			Wini ini = Database.getUsers();
			ini.put(nn,"nick",goodnick);
			Database.storeUsers();
			bot.sendToBunny("Dear " + getNick(nn) + ", your nick has been changed for 50 points to: " + goodnick);
			nickList.put(nn,goodnick);
			Points.delPoints(nn, 50);
	}

}
