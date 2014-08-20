package commands;

import java.util.HashMap;

import points.Chips;
import points.Points;
import main.ShadyBotty;

public class StandardCmds {

	private static ShadyBotty botty;
	
	//used for points request
	private static long latestPointsRequest;
	private static HashMap<String, Long> latestPointsRequestByUser;
	
	//used for gamble request
	private static long lastGambleRequest;
	private static HashMap<String, Long> latestGambleRequestByUser;
	
	

	public StandardCmds(ShadyBotty bot){
		botty = bot;
		setLatestPointsRequest(System.currentTimeMillis());
	}

	public void setLatestPointsRequest(long value){
		latestPointsRequest = value;
	}

	public static boolean canRequestPoints(String nick){
		if (ShadyBotty.database.getPrivileges(nick).getCooldown() == -1){
			return true;
		}
		long call =  System.currentTimeMillis();
		if (latestPointsRequestByUser.containsKey(nick)){
			Long latestrequest = latestPointsRequestByUser.get(nick);
			if (call - latestrequest > 60000){
				latestPointsRequestByUser.put(nick, call);
				return true;
			}
		} else if (call - latestPointsRequest > 5000){
			latestPointsRequestByUser.put(nick, call);
			return true;			
		}
		return false;
	}

	public static boolean requestPoints(String nick){		
		if (canRequestPoints(nick)){
			String toSend = nick + " has " + Points.getPoints(nick);
			if (Chips.getChips(nick) != 0)
				toSend += " and " + Chips.getChips(nick);
			toSend += ".";
			if (ShadyBotty.database.getPrivileges(nick).getFaction().equalsIgnoreCase("jb940")) {
				toSend += " " + nick + " also has 1 GodPoint! Kappa/";	

				botty.sendToBunny(toSend);

			}
			return true;
		}
		return false;
	}
	
	public static boolean gamble(String nick){
		//TODO
		return false;
	}
	
	public static boolean suicide(String nick){
		//TODO
		return false;
	}
	
	public static boolean roulette(String nick){
		//TODO
		return false;
	}
	
	public static boolean challenge(String nick){
		//TODO
		return false;
	}
	
	public static boolean searchSlave(String nick){
		//TODO
		return false;
	}
	
	public static boolean slave(String nick){
		//TODO
		return false;
	}

}
