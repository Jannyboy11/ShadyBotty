package api;

import points.GivePointsThread;
import main.ShadyBotty;

public class CheckStreamThread extends Thread {

	ShadyBotty botty;
	public CheckStreamThread(ShadyBotty bot) 
	{
		botty = bot;
	}
	public void run ()
	{
		while (true) {
			try {Thread.sleep(5000);} catch (Exception e) {}
			System.out.println("shady is: " + (TwitchAPI.getJSONStreamShady() != null));
			if (TwitchAPI.getJSONStreamShady() != null) {
				if(!botty.getPointsThread().getStatus())
					botty.getPointsThread().setOnline();
				GivePointsThread thr = new GivePointsThread(botty);
				botty.newPointsThread(thr);
			} else {
				if(!botty.getPointsThread().getStatus()) continue;
				try {Thread.sleep(2000);} catch (Exception e) {}
				if (TwitchAPI.getJSONStreamShady() != null) continue;
				try {Thread.sleep(2000);} catch (Exception e) {}
				if (TwitchAPI.getJSONStreamShady() != null) continue;
				try {Thread.sleep(2000);} catch (Exception e) {}
				if (TwitchAPI.getJSONStreamShady() != null) continue;
				botty.getPointsThread().setOffline();
				botty.sendToBunny("Shadyb00ny is now offline!");
			}

		}
	}
}
