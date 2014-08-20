package commands;

import java.util.HashMap;

import chat.Privileges.Status;
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


	public static boolean isValidStandardCmd(String msg, String nick) {
		String[] words = chat.ChatRules.getWords(msg);
		if (words[0].equalsIgnoreCase("!points")) {
			if ((ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD 
					|| ShadyBotty.database.getPrivileges(nick).getStatus() == Status.DEMIMOD) && words.length == 2) 
				requestPoints(words[1]);
			else
				requestPoints(nick);
			return true;

		} else if (words[0].equalsIgnoreCase("!gamble")) {
			if (ShadyBotty.database.getPrivileges(nick).getStatus() != Status.VIEWER && words.length == 2 && isDouble(words[1]))
				gamble(nick, Double.parseDouble(words[1]));
			return true;
		}



		return false;
	}












	public void setLatestPointsRequest(long value){
		latestPointsRequest = value;
	}

	public static boolean canRequestPoints(String nick){
		long call =  System.currentTimeMillis();
		if (ShadyBotty.database.getPrivileges(nick).getCooldown() == -1){
			latestPointsRequest = call;
			return true;
		};

		Long latestrequest = new Long(0);
		if (latestPointsRequestByUser.containsKey(nick))
			latestrequest = latestPointsRequestByUser.get(nick);
		if (call - latestrequest > 60000 && call - latestPointsRequest > 5000){				
			latestPointsRequestByUser.put(nick, call);
			latestPointsRequest = call;
			return true;
		} else return false;
	}

	public static void requestPoints(String nick){		
		if (canRequestPoints(nick)){
			String toSend = getNick(nick) + " has " + Math.round(Points.getPoints(nick)*10)/10 + " points ";
			if (Chips.getChips(nick) != 0)
				toSend += " and " + Chips.getChips(nick);
			toSend+= ". ";
			if (ShadyBotty.database.getPrivileges(nick).getFaction().equalsIgnoreCase("jb940")) 
				toSend += getNick(nick) + " also has 1 GodPoint! Kappa/";	
			System.out.println(toSend);
			botty.sendToBunny(toSend);

		}
	}





	public static boolean canGamble(String nick, int amount){
		boolean result = false;
		Long latestrequest = new Long(0);
		long call =  System.currentTimeMillis();
		Long wait = ShadyBotty.database.getPrivileges(nick).getCooldown() == -1 ? new Long(45000) : new Long(60000);
		if (latestGambleRequestByUser.containsKey(nick))
			latestrequest = latestGambleRequestByUser.get(nick);
		if (call - latestrequest > wait && call - latestGambleRequest > 5000)			
			result = true;


		if (result){
			if (Chips.getChips(nick) >= amount){ 
				latestGambleRequestByUser.put(nick, call);
				latestGambleRequest = call;
			} else {
				return false;
			}
		}
		return result;
	}

	public static void gamble(String nick, double amount){
		if (canGamble(nick, (int) Math.round(amount))){

			if (amount <= 100 && 0 < amount)
				performGamble(nick, Math.random(), (int) Math.round(amount));
			else 
				botty.sendToBunny("The stake must be in range of 1-100.");
		}
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





	public static String getNick(String realname) {
		return ShadyBotty.database.getPrivileges(realname).getNick();
	}

	private static void changeNick(String realname, String nick){
		ShadyBotty.database.getPrivileges(realname).setNick(nick);
	}



	public static boolean isDouble(String s) {
		try { 
			Double.parseDouble(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		// only got here if we didn't return false
		return true;
	}

}
