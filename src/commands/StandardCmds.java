package commands;

import java.util.HashMap;

import points.Chips;
import points.Points;
import main.ShadyBotty;

public class StandardCmds {

	private static ShadyBotty botty;
	private static long latestPointsRequest;
	private static HashMap<String, Long> latestRequestByUser;

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
		if (latestRequestByUser.containsKey(nick)){
			Long latestrequest = latestRequestByUser.get(nick);
			if (call - latestrequest > 60000){
				latestRequestByUser.put(nick, call);
				return true;
			}
		} else if (call - latestPointsRequest > 5000){
			latestRequestByUser.put(nick, call);
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
			return true;
		}
		return false;
	}




}
