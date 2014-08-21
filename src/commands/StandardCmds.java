package commands;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import chat.Nicknames;
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
	private static HashMap<String, Long> latestChallengeRequestByUser = new HashMap<String,Long>();
	private static HashMap<String, HashMap<String, Long>> challengedNicks = new HashMap<String, HashMap<String,Long>>();

	//used for dailybonus
	private static HashMap<String, Long> dailyBonus;

	//used for lottery
	private static long lastLottery;
	public static ArrayList<String> lotteryPlayers = new ArrayList<String>();
	private static int lotteryPot;
	private static boolean lottery = false;



	public StandardCmds(ShadyBotty bot){
		botty = bot;
		setLastLottery(0);
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
		} else if (words[0].equalsIgnoreCase("!challenge") && words.length == 2) {

			Status stat = ShadyBotty.database.getPrivileges(nick).getStatus();
			System.out.println("challenger is: " + nick + "and stat is:" +stat);
			if (!(stat == Status.VIEWER || stat == Status.REGULAR))
				System.out.println("challenger is premium+ : " + nick);
			challenge(nick, words[1]);
			return true;
		} else if (words[0].equalsIgnoreCase("!accept") && words.length == 1) {
			performChallenge(nick);
			return true;
		} else if (words[0].equalsIgnoreCase("!lottery")) {
			if ((ShadyBotty.database.getPrivileges(nick).getStatus() == Status.DEMIMOD || ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD)
					&& words.length == 2 && isDouble(words[1]))
				startLottery(nick, Double.parseDouble(words[1]));
			return true;
		} else if (words[0].equalsIgnoreCase("!enter")) {
				enterLottery(nick);
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

	public static boolean canChallenge(String nick, String challengednick){
		if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER ||
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.REGULAR) return false;
		long call = System.currentTimeMillis();
		Long latestrequest = new Long(0);
		Long latestrequest2 = new Long(0);
		if (latestChallengeRequestByUser.containsKey(nick)) {
			latestrequest = latestChallengeRequestByUser.get(nick);
		}
		if (latestChallengeRequestByUser.containsKey(challengednick)) {
			latestrequest2 = latestChallengeRequestByUser.get(challengednick);
		}
		if (call - latestrequest2 > 120000 &&call - latestrequest > 120000 && call - latestChallengeRequest > 15000){				
			latestChallengeRequestByUser.put(nick, call);
			latestChallengeRequestByUser.put(challengednick, call);
			latestChallengeRequest = call;
			return true;
		}
		return false;
	}

	public static void challenge(String challengernick, String challengednick){

		if (!(canChallenge(challengernick, challengednick))) return;
		HashMap<String,Long> temp = new HashMap<String, Long>();
		temp.put(challengernick, System.currentTimeMillis());
		botty.sendToBunny("Dear " + getNick(challengednick) + ", " + getNick(challengernick) + " has challenged you to a game of roulette! press !accept to play!");
		challengedNicks.put(challengednick,temp);
		temp = challengedNicks.get(challengednick);
		System.out.println(temp.keySet().toArray()[0] + " wooop  " +temp.get(temp.keySet().toArray()[0]));
		return;
	}

	public static void performChallenge(String challengednick){
		if (!challengedNicks.containsKey(challengednick)) return;
		HashMap<String,Long> temp = new HashMap<String, Long>();
		temp = challengedNicks.get(challengednick);
		String challengernick = (String) temp.keySet().toArray()[0];
		if (System.currentTimeMillis() - temp.get(challengernick) > 25000) return;
		String loser = Math.random() < 0.5 ? challengednick : challengernick;
		botty.sendToBunny( getNick(challengednick) + " and " + getNick(challengernick) + " play roulette. " + getNick(loser) + " got shot. REKT");
		botty.sendToBunny("/timeout " + loser + " 30");
		return;
	}



	public static boolean searchSlave(String nick){
		//TODO
		return false;
	}

	public static boolean slave(String nick){
		//TODO
		return false;
	}

	public static boolean stabRandom(String nick){
		//TODO
		return false;
	}

	public static boolean canDailyBonus(String nick){
		if (!ShadyBotty.database.getPrivileges(nick).isSubscriber()) return false;
		Long latest = new Long(0);
		if (dailyBonus.containsKey(nick)){
			latest = dailyBonus.get(nick);
		}
		long call = System.currentTimeMillis();
		if (call - latest > 24 * 60 * 60 * 1000){
			dailyBonus.put(nick, call);			
			return true;
		}
		return false;
	}

	public static void dailyBonus(String nick){
		if (!canDailyBonus(nick)) return;
		int amount = (int) Math.round(Math.random() * 300);
		Points.addPoints(nick, amount);
		botty.sendToBunny(getNick(nick) + " has got " + amount + " points today!");

	}

	public static String getNick(String realname) {
		return Nicknames.getNick(realname);
	}
	public static boolean canStartLottery(String nick, double amount) {
		if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER ||
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.REGULAR || 
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.PREMIUM) return false;
		long call = System.currentTimeMillis();
		if (call - getLastLottery() > 5 * 60 * 1000 && Points.getPoints(nick) >= amount) {
			lastLottery = call;
			return true;
		}
		return false;
	}



	public static void startLottery(String nick, double amount) {
		System.out.println("in lotterystart");
		if (lottery) return;
		System.out.println("lottery off");
		if (!canStartLottery(nick, amount)) return;
		lotteryPot = (int)Math.round(amount);
		botty.sendToBunny("the lottery has been started by " + getNick(nick) + ". The Lottery has started with " +lotteryPot + " points! type !enter to join(cost 25).");
		new LotteryThread(botty).start();
		lottery = true;
		lotteryPlayers.add(nick);
	}
	public static void endLottery() {
		String message;
		if (lotteryPlayers.size() > 7) {
			Random rand = new Random();
			message = "Lottery is over. The total pot was " + getPot() + ". The winners are: ";
			ArrayList<Integer> winnerNum = new ArrayList<Integer>();
			for (int i = 0; i < 3; i++) {
				
				int randomNum = rand.nextInt(lotteryPlayers.size());
				while (winnerNum.contains(randomNum)) //check if this player has already won.
					randomNum = rand.nextInt(lotteryPlayers.size());
				winnerNum.add(randomNum);
				
				double profit;
				 String winner = getNick(lotteryPlayers.get(randomNum));
				 if (i == 0) {
					 profit = (double) getPot()*0.5;
					 message += getNick(winner + " with " + profit + ", ");
					 Points.addPoints(winner,profit);
				 } else if (i == 1) {
					 profit = (double) getPot()*0.35;
					 message += getNick(winner + " with " + profit + ", ");
					 Points.addPoints(winner,profit);
				 } else {
					 profit = (double) getPot()*0.15;
					 message += getNick(winner + " with " + profit + ", ");
					 Points.addPoints(winner,profit);
				 }
			}
		} else {
			message = "Not enough people have participated. Everyone will receive their points back.";
			for (int i = 1; i < lotteryPlayers.size(); i++) {
				Points.addPoints(lotteryPlayers.get(i), (double) 25);
				lotteryPot -= 25;
			}
			Points.addPoints(lotteryPlayers.get(0),lotteryPot);
		}
		lotteryPot = 0;
		lottery = false;
		lotteryPlayers.clear();
		botty.sendToBunny(message);
	}

	public static void enterLottery(String nick) {
		if (Points.getPoints(nick) >=25 && lottery == true && !lotteryPlayers.contains(nick)) {
			lotteryPlayers.add(nick);
			addPot(25);
			Points.delPoints(nick, 25);
		}
	}
	public static long getLastLottery() {
		return lastLottery;
	}

	public static void setLastLottery(long lastLottery) {
		StandardCmds.lastLottery = lastLottery;
	}






	public static int getPot() {
		return lotteryPot;
	}
	public static void addPot(int amount) {
		lotteryPot = getPot() + amount;
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
