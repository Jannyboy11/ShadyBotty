package chat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import main.ShadyBotty;

import org.ini4j.Wini;

import points.Points;

public class Nicknames {
	public static HashMap<String,String> nickList;
	private static ShadyBotty bot;
	public Nicknames(ShadyBotty botty) {
		nickList = new HashMap<String,String>();
		bot = botty;
	}

	public String getNick(String user) {
		return nickList.get(user) == null ? user : nickList.get(user);
	}

	public void addNick(String nn){
		Wini ini;
		try {
			ini = new Wini(new File("users.ini"));
			nickList.put(nn,ini.get(nn,"nick") == null ? nn : ini.get(nn,"nick"));
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public void ChangeNick(String nn, String nick){

		try {
		
			if (nickList.containsKey(nick) || nickList.containsValue(nick)) {
				bot.sendToBunny("Dear " + getNick(nn) + ", this nick is already in use or a username.");
				return;
			}
			if (nick.length() > 16) {
				bot.sendToBunny("Dear " + getNick(nn) + ", this nick is too long.");
				return;
			}
			nickList.put(nn,nick);
			Wini ini = new Wini(new File("users.ini"));
			ini.put(nn,"nick",nick);
			ini.store();
			bot.sendToBunny("Dear " + getNick(nn) + ", your nick has been for 50 points changed to: " + nick);
			Points.delPoints(nn, 50);
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
