package api;

import org.json.JSONObject;

import points.GivePointsThread;
import main.ShadyBotty;

public class CheckStreamThread extends Thread {
	private int printtimer = 0;

	ShadyBotty botty;
	public CheckStreamThread(ShadyBotty bot) 
	{
		botty = bot;
	}
	@Override
	public void run ()
	{
		while (true) {
			while (botty.isConnected()) {

				try {Thread.sleep(5000);} catch (Exception e) {}
				
				SongAPI.checkMusic();
				JSONObject a = TwitchAPI.getJSONStreamShady();
				if (printtimer == 2) {
//				System.out.println("shady is: " + (a != null));
				printtimer = 0;
				}
				
				printtimer++;
				if (a != null) {
					if(!botty.getPointsThread().getStatus()) {
						botty.getPointsThread().setOnline();
						GivePointsThread thr = new GivePointsThread(botty);
						botty.newPointsThread(thr);
					}
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
			try {Thread.sleep(10000);} catch (Exception e) {}
		}
	}
}
