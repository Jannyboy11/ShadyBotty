package commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import api.RiotAPI;
import api.SongAPI;
import chat.Nicknames;
import chat.Privileges.Status;
import points.Chips;
import points.Points;
import main.ShadyBotty;
import main.ShadyBottyMain;

public class StandardCmds {

	private static ShadyBotty botty;

	//used for rank request
	private static long latestRankRequest = 0;

	//used for points request
	private static long latestPointsRequest;
	private static HashMap<String, Long> latestPointsRequestByUser = new HashMap<String, Long>();

	//used for gamble request
	private static long latestGambleRequest;
	private static HashMap<String, Long> latestGambleRequestByUser = new HashMap<String, Long>();

	//used for suicide request
	private static long latestSuicideRequest;
	private static HashMap<String, Long> latestSuicideRequestByUser = new HashMap<String, Long>();

	//used for roulette request
	private static long latestRouletteRequest;
	private static HashMap<String, Long> latestRouletteRequestByUser = new HashMap<String, Long>();

	//used for challenge requset
	private static long latestChallengeRequest;
	private static HashMap<String, Long> latestChallengeRequestByUser = new HashMap<String,Long>();
	private static HashMap<String, HashMap<String, Long>> challengedNicks = new HashMap<String, HashMap<String,Long>>();

	//used for dailybonus
	private static HashMap<String, Long> dailyBonus  = new HashMap<String,Long>();

	//used for lottery
	private static long lastLottery;
	public static ArrayList<String> lotteryPlayers = new ArrayList<String>();
	private static int lotteryPot;
	private static boolean lottery = false;


	//for stabrandom
	private static long latestStabRequest;
	private static HashMap<String, Long> latestStabRequestByUser  = new HashMap<String, Long>();




	public StandardCmds(ShadyBotty bot){
		botty = bot;
		setLatestStabRequest(0);
		setLastLottery(0);
		setLatestPointsRequest(0);
		setLatestGambleRequest(0);
		setLatestSuicideRequest(0);
		setLatestRouletteRequest(0);
		latestChallengeRequest = 0;
	}

	public void setLatestStabRequest(int i) {
		latestStabRequest = i;	
	}

	public static boolean isValidStandardCmd(String msg, String nick, String ch) {

		String[] words = chat.ChatRules.getWords(msg);

		Status stat = ShadyBotty.database.getPrivileges(nick).getStatus();
//		System.out.println("inhere2");
		
		if (words[0].equalsIgnoreCase("!points")) {
//			System.out.println("in points");
			if ((ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD 
					|| ShadyBotty.database.getPrivileges(nick).getStatus() == Status.DEMIMOD) && words.length == 2)  {
				if (ShadyBotty.database.getPrivileges(words[1]) == null) {
					ShadyBotty.database.addPrivileges(words[1]);
					Nicknames.addNick(words[1]);	
				}
				System.out.println("punbts");
				requestPoints(nick,words[1],ch);
			}
			else {
				System.out.println("pubts");
				requestPoints(nick, nick,ch);
				
			}
			return true;

		} else if (words[0].equalsIgnoreCase("!accept")) {
			ShadyBotty.database.addPrivileges(nick);
			stat = ShadyBotty.database.getPrivileges(nick).getStatus();
			performChallenge(nick,ch);
			return true;
		}else if (words[0].equalsIgnoreCase("!gamble")) {
			if (ShadyBotty.database.getPrivileges(nick).getStatus() != Status.VIEWER && words.length == 2 && isDouble(words[1]))
				gamble(nick, Double.parseDouble(words[1]),ch);
			return true;
		} else if (words[0].equalsIgnoreCase("!challenge") && words.length == 2) {

			ShadyBotty.database.addPrivileges(nick);
			ShadyBotty.database.addPrivileges(words[1]);
			stat = ShadyBotty.database.getPrivileges(nick).getStatus();

			if (!(stat == Status.VIEWER || stat == Status.REGULAR))
				challenge(nick, words[1],ch);
			return true;
		}  else if (words[0].equalsIgnoreCase("!lottery")) {
			if ((ShadyBotty.database.getPrivileges(nick).getStatus() == Status.DEMIMOD || ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD)
					&& words.length == 2 && isDouble(words[1]))
				startLottery(nick, Double.parseDouble(words[1]),ch);
			if ((ShadyBotty.database.getPrivileges(nick).getStatus() == Status.DEMIMOD || ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD)
					&& words.length == 1)
				startLottery(nick, 0,ch);
			return true;
		} else if (words[0].equalsIgnoreCase("!enter")) {
			enterLottery(nick);
			return true;
		} else if (words[0].equalsIgnoreCase("!dailybonus")) {
			dailyBonus(nick,ch);
			return true;
		} else if (words[0].equalsIgnoreCase("!nick") && words.length >= 2) {
			Nicknames.ChangeNick(nick, msg.substring(6));
			return true;
		} else if (words[0].equalsIgnoreCase("!stabrandom")) {
			if (!(stat == Status.VIEWER || stat == Status.REGULAR)) 
				stabRandom(nick,ch);
			return true;
		}else if (words[0].equalsIgnoreCase("!song")) {
			SongAPI.checkMusic();
			botty.sendMessage(ch,SongAPI.getLatestmusic());
			return true;
		}
		else if (words[0].equalsIgnoreCase("!rank")) {
			return getRank(words,ch);
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

	public static boolean getRank(String[] words,String ch) {
		long call =  System.currentTimeMillis();
		if (call - latestRankRequest < 8000)
			return false;
		latestRankRequest = call;
		if (words.length == 1)
			botty.sendMessage(ch,RiotAPI.getRank("ACE Shady", "euw"));
		if (words.length == 2)
			botty.sendMessage(ch,RiotAPI.getRank(words[1], "euw"));
		if (words.length == 3)
			botty.sendMessage(ch,RiotAPI.getRank(words[2], words[1]));
		return true;
	}
	public static boolean canRequestPoints(String nick){
		long call =  System.currentTimeMillis();
		if (ShadyBotty.database.getPrivileges(nick).getCooldown() == -1 ||
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD ||  
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.DEMIMOD){
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

	public static void requestPoints(String nick,String target, String ch){		
		if (canRequestPoints(nick)){
			String toSend = getNick(target) + " has " + Math.round(Points.getPoints(target.toLowerCase())) + " points";
			if (Chips.getChips(target) != 0)
				toSend += " and " + Math.round(Chips.getChips(target.toLowerCase())) + " chips";
			toSend+= ". ";
			if (ShadyBotty.database.getPrivileges(target.toLowerCase()).getFaction().equalsIgnoreCase("jb940")) 
				toSend += getNick(target) + " also has 1 GodPoint! Kappa/";	
//			System.out.println(toSend);
			ShadyBottyMain.privbot.sendPriv(nick,toSend);

		}
	}





	public static boolean canGamble(String nick, int amount){
		boolean result = false;
		Long latestrequest = new Long(0);
		long call =  System.currentTimeMillis();
		Long wait = ShadyBotty.database.getPrivileges(nick.toLowerCase()).getCooldown() == -1 ? new Long(45000) : new Long(60000);
		if (latestGambleRequestByUser.containsKey(nick))
			latestrequest = latestGambleRequestByUser.get(nick);
		if (call - latestrequest > wait && call - latestGambleRequest > 5000)			
			result = true;


		if (result){
//			System.out.println("result true");
			if (Chips.getChips(nick) >= amount){ 
				latestGambleRequestByUser.put(nick, call);
				latestGambleRequest = call;
			} else {
				return false;
			}
		}
		return result;
	}

	public static void gamble(String nick, double amount, String ch){
//		System.out.println("test");
		if (canGamble(nick, (int) Math.round(amount))){
//			System.out.println("passed cangamble");
			if (amount <= 100 && 0 < amount)
				performGamble(nick, Math.random(), (int) Math.round(amount),ch);
			else 
				botty.sendMessage(ch,"The stake must be in range of 1-100.");
		}
	}

	public static void performGamble(String nick, double random, int stake, String ch){
		Chips.delChips(nick, stake);
		if (random < 0.01){
			Chips.addChips(nick, stake * 10);
			botty.sendMessage(ch,"Gamble rolls... and " + getNick(nick) + " has won the JACKPOT!!! " + getNick(nick) + " has won " + stake * 10 + "chips!");
			return;
		}
		if (random < 0.06){
			Chips.addChips(nick, stake * 4);
			botty.sendMessage(ch,"Gamble rolls... and QUADRUPLES your chips! " + getNick(nick) + " has won " + stake * 4 + "chips!");
			return;
		}
		if (random < 0.21){
			Chips.addChips(nick, stake * 2);
			botty.sendMessage(ch,"Gamble rolls... and doubles your chips! " + getNick(nick) + " has won" + stake * 2 + " chips!");
			return;
		}
		if (random < 0.46){
			Chips.addChips(nick, stake);
			botty.sendMessage(ch,"Gamble rolls... and you get your chips back! " + getNick(nick) + " has retained his " + stake + " chips.");
			return;
		}
		botty.sendMessage(ch,"Gamble rolls... and totally misses your bet! " + getNick(nick) + " has lost " + stake + " chips.");
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

	public static void suicide(String nick, String ch){
		if (!canSuicide(nick)) return;
		if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER){
			botty.sendMessage(ch,getNick(nick) + " is down! RIP in peace Kappa"); //no need to get the nick, caus only premiums can change their nicks? :P
			botty.sendMessage(ch,".timeout " + nick + "180");
		} else if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.MOD){
			botty.sendMessage(ch,"You're a mod. Genius Kappa");
		} else {
			botty.sendMessage(ch,"You're a regular, pls we need you here! :(");
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

	public static void roulette(String nick, String ch){
		if (!canRoulette(nick)) return;
		Status status = ShadyBotty.database.getPrivileges(nick).getStatus();
		if (Math.random() < 0.5){
			if (status == Status.VIEWER){
				botty.sendMessage(ch,getNick(nick) + " is down! RIP in peace Kappa");
				botty.sendMessage(ch,".timeout " + nick + "180");
			} else if (status == Status.DEMIMOD || status == Status.MOD){
				botty.sendMessage(ch,getNick(nick) + ", you're such a cheater with your mod armor!");
			} else {
				botty.sendMessage(ch,"/me cathes bullet for " + getNick(nick) + "!");
			}
		} else {
			botty.sendMessage(ch,getNick(nick) + " survived roulette! Is that a God? Kappa");
		}
	}

	public static boolean canChallenge(String nick, String challengednick){
		if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER ||
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.REGULAR) return false;
		long call = System.currentTimeMillis();
		Long latestrequest = 0L;
		Long latestrequest2 =  0L;
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

	public static void challenge(String challengernick, String challengednick, String ch){

		if (!(canChallenge(challengernick, challengednick))) return;
		HashMap<String,Long> temp = new HashMap<String, Long>();
		temp.put(challengernick, System.currentTimeMillis());
		botty.sendMessage(ch,"Dear " + getNick(challengednick) + ", " + getNick(challengernick) + " has challenged you to a game of roulette! press !accept to play!");
		challengedNicks.put(challengednick,temp);
		temp = challengedNicks.get(challengednick);
//		System.out.println(temp.keySet().toArray()[0] + " wooop  " +temp.get(temp.keySet().toArray()[0]));
		return;
	}

	public static void performChallenge(String challengednick,String ch){
//		System.out.println(challengednick);
		if (!challengedNicks.containsKey(challengednick)) return;
//		System.out.println(challengednick);
		HashMap<String,Long> temp = new HashMap<String, Long>();
		temp = challengedNicks.get(challengednick);
		String challengernick = (String) temp.keySet().toArray()[0];
//		System.out.println(challengernick + " ");
		if ((System.currentTimeMillis() - temp.get(challengernick)) > 39000) return;
//		System.out.println("in time");
		String loser = Math.random() < 0.5 ? challengednick : challengernick;
		botty.sendMessage(ch, getNick(challengednick) + " and " + getNick(challengernick) + " play roulette. " + getNick(loser) + " got shot. REKT");
		if (ShadyBotty.database.getPrivileges(loser).getStatus() != Status.DEMIMOD)
			botty.sendMessage(ch,"/timeout " + loser + " 30");
		challengedNicks.remove(challengednick);
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


	public static boolean canStabRandom(String nick){
		Long latestrequest = new Long(0);
		if (ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER ||
				ShadyBotty.database.getPrivileges(nick).getStatus() == Status.REGULAR) return false;
		long call =  System.currentTimeMillis();
		Long wait = ShadyBotty.database.getPrivileges(nick.toLowerCase()).getCooldown() == -1 ? new Long(60000) : new Long(90000);
		if (latestStabRequestByUser.containsKey(nick))
			latestrequest = latestStabRequestByUser.get(nick);
		if (call - latestrequest > wait && call - latestStabRequest > 15000) {
			latestStabRequestByUser.put(nick, call);
			latestStabRequest = call;
			return true;
		} else return false;
	}

	public static void stabRandom(String nick, String ch) {
		if (!canStabRandom(nick)) return;
		ArrayList<String> temp = main.Database.currentUsers;
		String stabNick = temp.get((int) Math.round(Math.random() *(temp.size() - 1)));
		while (Points.getPoints(stabNick) < 200 || (ShadyBotty.database.getPrivileges(stabNick).getStatus() == Status.MOD || ShadyBotty.database.getPrivileges(stabNick).getStatus() == Status.DEMIMOD)) {
			stabNick = temp.get((int) Math.round(Math.random() *(temp.size() - 1)));

		}
		botty.sendMessage(ch,"/me has stabbed " + getNick(stabNick) + " randomly! Ouch! 5 seconds timeout :(");
		botty.sendMessage(ch,".timeout " + stabNick + " 5");

	}

	public static boolean canDailyBonus(String nick){
//		System.out.println(nick +" used daily bonus");
		System.out.println(ShadyBotty.database.getPrivileges(nick).isSubscriber());
		if (!ShadyBotty.database.getPrivileges(nick).isSubscriber()) return false;
//		System.out.println("sub");
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

	public static void dailyBonus(String nick, String ch){
		if (!canDailyBonus(nick)) return;
		int amount = (int) Math.round(Math.random() * 300);
		Points.addPoints(nick, amount);
		botty.sendMessage(ch,getNick(nick) + " has got " + amount + " points today!");

	}

	public static String getNick(String realname) {
		return Nicknames.getNick(realname);
	}
	public static boolean canStartLottery(String nick, double amount) {
		if (0 >= amount && amount <= 500) return false;
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



	public static void startLottery(String nick, double amount, String ch) {
		if (lottery) return;
		if (!canStartLottery(nick, amount)) return;
		lotteryPot = (int)Math.round(amount);
		botty.sendMessage(ch,"the lottery has been started by " + getNick(nick) + ". The Lottery has started with " +lotteryPot + " points! type !enter to join(cost 25).");
		new LotteryThread(botty,ch).start();
		Points.delPoints(nick, amount);
		lottery = true;
		lotteryPlayers.add(nick);
	}
	public static void endLottery(String ch) {
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
					profit = getPot()*0.5;
					message += getNick(winner + " with " + profit + ", ");
					Points.addPoints(winner,profit);
				} else if (i == 1) {
					profit = getPot()*0.35;
					message += getNick(winner + " with " + profit + ", ");
					Points.addPoints(winner,profit);
				} else {
					profit = getPot()*0.15;
					message += getNick(winner + " with " + profit + ", ");
					Points.addPoints(winner,profit);
				}
			}
		} else {
			message = "Not enough people have participated. Everyone will receive their points back.";
			for (int i = 1; i < lotteryPlayers.size(); i++) {
				Points.addPoints(lotteryPlayers.get(i), 25);
				lotteryPot -= 25;
			}
			Points.addPoints(lotteryPlayers.get(0),lotteryPot);
		}
		lotteryPot = 0;
		lottery = false;
		lotteryPlayers.clear();
		botty.sendMessage(ch,message);
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
