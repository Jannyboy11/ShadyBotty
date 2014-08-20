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
	private static long latestGambleRequest;
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
		boolean result = false;
		long call =  System.currentTimeMillis();
		if (latestPointsRequestByUser.containsKey(nick)){
			Long latestrequest = latestPointsRequestByUser.get(nick);
			if (call - latestrequest > 60000){				
				result = true;
			} else if (call - latestPointsRequest > 5000){
				result = true;			
			}
		}
		if (result){
			latestPointsRequestByUser.put(nick, call);
			latestPointsRequest = call;
		}
		return result;
	}

	public static boolean requestPoints(String nick){		
		if (canRequestPoints(nick)){
			String toSend = getNick(nick) + " has " + Points.getPoints(nick);
			if (Chips.getChips(nick) != 0)
				toSend += " and " + Chips.getChips(nick);
			toSend += ".";
			if (ShadyBotty.database.getPrivileges(nick).getFaction().equalsIgnoreCase("jb940")) {
				toSend += " " + getNick(nick) + " also has 1 GodPoint! Kappa/";	

				botty.sendToBunny(toSend);

			}
			return true;
		}
		return false;
	}

	public static boolean canGamble(String nick, int amount){
		boolean result = false;
		long call =  System.currentTimeMillis();
		if (latestGambleRequestByUser.containsKey(nick)){
			Long latestrequest = latestGambleRequestByUser.get(nick);
			if (call - latestrequest > 60000){				
				result = true;
			} else if (call - latestGambleRequest > 5000){
				result = true;			
			}
		}
		if (result){
			if (Chips.getChips(nick) > amount){ 
				latestGambleRequestByUser.put(nick, call);
				latestGambleRequest = call;
			} else {
				return false;
			}
		}
			return result;
	}

	public static boolean gamble(String nick, int amount){
		if (!canGamble(nick, amount)){
			return false;
		}
		if (Chips.getChips(nick) != 0){
			if (amount <= 100 && 0 < amount){
				performGamble(nick, Math.random(), amount);
				return true;
			} else {
				botty.sendToBunny("The stake must be in range of 1-100.");
			}
		}
		return false;
	}

	public static void performGamble(String nick, double random, int stake){
		Chips.subtractChips(nick, stake);
		if (random < 0.01){
			Chips.addChips(nick, stake * 10);
			botty.sendToBunny(nick + "Gamble rols... and " + getNick(nick) + " has won the JACKPOT!!! " + nick + " has gained " + stake * 10 + "chips!");
			return;
		}
		if (random < 0.05){
			Chips.addChips(nick, stake * 4);
			botty.sendToBunny(nick + " has QUADRUPLED his chips " + nick + " has gained " + stake * 10 + "chips!");
			return;
		}
		if (random < 0.15){
			Chips.addChips(nick, stake * 2);
			return;
		}
		if (random < 0.25){
			Chips.addChips(nick, stake);
			return;
		}

	}

	private static String getNick(String realname) {
		return ShadyBotty.database.getPrivileges(realname).getNick();
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

	public static boolean createLottery(String nick){
		//TODO
		return false;
	}

	public static boolean enterLottery(String nick){
		//TODO
		return false;
	}
	
	public static boolean stabRandom(String nick){
		//TODO
		return false;
	}
	
	public static boolean changeNick(String nick){
		//TODO
		return false;
	}

}
