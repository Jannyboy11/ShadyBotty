package commands;

import main.ShadyBotty;


public class LotteryThread extends Thread {
	ShadyBotty botty;
	public LotteryThread(ShadyBotty bot) {
	botty = bot;
	}

	public void run() {
		try {Thread.sleep(60000);} catch (Exception e) {}
		String msg = "one minute has passed. type !enter (25points) to join lottery. current pot: " + StandardCmds.getPot() + ". people in: " + getInLottery();
		botty.sendToBunny(msg);
		try {Thread.sleep(60000);} catch (Exception e) {}
		 msg = "two minutes have passed. type !enter (25points) to join lottery. current pot: " + StandardCmds.getPot() + ". people in: " + getInLottery();
		botty.sendToBunny(msg);
		try {Thread.sleep(60000);} catch (Exception e) {}
		StandardCmds.endLottery();
	}
	
	public String getInLottery() {
		String msg = "";
		for (int i =0; i < StandardCmds.lotteryPlayers.size();i++) {
			msg += StandardCmds.lotteryPlayers.get(i);
			if (i != StandardCmds.lotteryPlayers.size()-1)
				msg +=  ", ";
			else 
				msg += ".";
		}
		return msg;
	}
}
