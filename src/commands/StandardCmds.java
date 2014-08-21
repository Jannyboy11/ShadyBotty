package commands;

import java.util.HashMap;
import java.util.List;

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

	//used for suicide request
	private static long latestSuicideRequest;
	private static HashMap<String, Long> latestSuicideRequestByUser;

	//used for roulette request
	private static long latestRouletteRequest;
	private static HashMap<String, Long> latestRouletteRequestByUser;

	//used for challenge requset
	private static long latestChallengeRequest;
	private static HashMap<String, Long> latestChallengeRequestByUser;
	private static HashMap<String, Long> challengedNicks;



	public StandardCmds(ShadyBotty bot){
		botty = bot;
		setLatestPointsRequest(System.currentTimeMillis());
		setLatestGambleRequest(System.currentTimeMillis());
		setLatestSuicideRequest(System.currentTimeMillis());
		setLatestRouletteRequest(System.currentTimeMillis());
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

	private static void setLatestGambleRequest(long value){
		latestGambleRequest = value;
	}

	private static void setLatestSuicideRequest(long value){
		latestSuicideRequest = value;
	}

	private void setLatestRouletteRequest(long value) {
		latestRouletteRequest = value;
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
		Chips.delChips(nick, stake);
		if (random < 0.01){
			Chips.addChips(nick, stake * 10);
			botty.sendToBunny("Gamble rolls... and " + getNick(nick) + " has won the JACKPOT!!! " + getNick(nick) + " has won " + stake * 10 + "chips!");
			return;
		}
		if (random < 0.06){
			Chips.addChips(nick, stake * 4);
			botty.sendToBunny("Gamble rolls... and QUADRUPLES your chips! " + getNick(nick) + " has won " + stake * 4 + "chips!");
			return;
		}
		if (random < 0.21){
			Chips.addChips(nick, stake * 2);
			botty.sendToBunny("Gamble rolls... and doubles your chips! " + getNick(nick) + " has won" + stake * 2 + " chips!");
			return;
		}
		if (random < 0.46){
			Chips.addChips(nick, stake);
			botty.sendToBunny("Gamble rolls... and you get your chips back! " + getNick(nick) + " has retained his " + stake + " chips.");
			return;
		}
		botty.sendToBunny("Gamble rolls... and totally misses your bet! " + getNick(nick) + " has lost " + stake + " chips.");
	}

	public static boolean canSuicide(String nick){
		long call = System.currentTimeMillis();
		Long latestrequest = new Long(0);
		if (latestSuicideRequestByUser.containsKey(nick)) {
			latestrequest = latestSuicideRequestByUser.get(nick);
		}
		if (call - latestrequest > 90000 && call - latestSuicideRequest > 15000){				
			latestSuicideRequestByUser.put(nick, call);
			latestSuicideRequest = call;
			return true;
		}
		return false;
	}

	public static void suicide(String nick){
		if (!canSuicide(nick)) return;
		if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER){
			botty.sendToBunny(getNick(nick) + " is down! RIP in peace Kappa"); //no need to get the nick, caus only premiums can change their nicks? :P
			botty.sendToBunny(".timeout " + nick + "180");
		} else if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD){
			botty.sendToBunny("You're a mod. Genius Kappa");
		} else {
			botty.sendToBunny("You're a regular, pls we need you here! :(");
		}
		return;
	}

	public static boolean canRoulette(String nick){
		long call = System.currentTimeMillis();
		Long latestrequest = new Long(0);
		if (latestRouletteRequestByUser.containsKey(nick)) {
			latestrequest = latestRouletteRequestByUser.get(nick);
		}
		if (call - latestrequest > 240000 && call - latestRouletteRequest > 15000){				
			latestRouletteRequestByUser.put(nick, call);
			latestRouletteRequest = call;
			return true;
		}
		return false;
	}

	public static void roulette(String nick){
		if (!canRoulette(nick)) return;
		Status status = ShadyBotty.database.getPrivileges(nick).getStatus();
		if (Math.random() < 0.5){
			if (status == Status.VIEWER){
				botty.sendToBunny(getNick(nick) + " is down! RIP in peace Kappa");
				botty.sendToBunny(".timeout " + nick + "180");
			} else if (status == Status.DEMIMOD || status == Status.MOD){
				botty.sendToBunny(getNick(nick) + ", you're such a cheater with your mod armor!");
			} else {
				botty.sendToBunny("/me cathes bullet for " + getNick(nick) + "!");
			}
		} else {
			botty.sendToBunny(getNick(nick) + " survived roulette! Is that a God? Kappa");
		}
	}

	public static boolean canChallenge(String nick){
		if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER ||
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.REGULAR) return false;
		long call = System.currentTimeMillis();
		Long latestrequest = new Long(0);
		if (latestChallengeRequestByUser.containsKey(nick)) {
			latestrequest = latestChallengeRequestByUser.get(nick);
		}
		if (call - latestrequest > 180000 && call - latestChallengeRequest > 15000){				
			latestChallengeRequestByUser.put(nick, call);
			latestChallengeRequest = call;
			return true;
		}
		return false;
	}

	public static void challenge(String challengernick, String challengednick){

		if (!(canChallenge(challengernick))) return;

		botty.sendToBunny("Dear " + getNick(challengednick) + ", " + getNick(challengernick) + " has challenged you to a game of roulette! press !accept to play!");
		challengedNicks.add(challengednick);
		ChallengeThread hi = new ChallengeThread(challengednick);
		hi.start();
		return;
	}

	static class ChallengeThread extends Thread{

		public String challengednick;

		public ChallengeThread(String challenged){
			challengednick = challenged;
		}

		public void run(){
			challengedNicks.add(challengednick);
			try {
				sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			challengedNicks.remove(challengednick);
			return;
		}
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

	public static boolean dailyBonus(String nick){
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
