package api;

import points.GivePointsThread;
import main.ShadyBotty;

public class CheckStreamThread extends Thread {

	ShadyBotty botty;
	GivePointsThread pointsth;
	public CheckStreamThread(ShadyBotty bot,GivePointsThread points) 
	{
		botty = bot;
		pointsth = points;
	}
	public void run ()
	{
		while (true) {
			try {Thread.sleep(5000);} catch (Exception e) {}
			System.out.println("shady is: " + (TwitchAPI.getJSONStreamShady() != null));
			if (TwitchAPI.getJSONStreamShady() != null) {
				if(!pointsth.getStatus())
					pointsth.setOnline();
					
			} else {
				if(!pointsth.getStatus()) continue;
				try {Thread.sleep(2000);} catch (Exception e) {}
				if (TwitchAPI.getJSONStreamShady() != null) continue;
				try {Thread.sleep(2000);} catch (Exception e) {}
				if (TwitchAPI.getJSONStreamShady() != null) continue;
				try {Thread.sleep(2000);} catch (Exception e) {}
				if (TwitchAPI.getJSONStreamShady() != null) continue;
				pointsth.setOffline();
				botty.sendToBunny("Shadyb00ny is now offline!");
			}

		}
	}
}
