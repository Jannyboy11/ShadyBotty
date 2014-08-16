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
		if (TwitchAPI.getJSONStreamShady() != null) {
			GivePointsThread.setOnline();
		} else {
			try {Thread.sleep(2000);} catch (Exception e) {}
			if (TwitchAPI.getJSONStreamShady() != null) return;
			try {Thread.sleep(2000);} catch (Exception e) {}
			if (TwitchAPI.getJSONStreamShady() != null) return;
			try {Thread.sleep(2000);} catch (Exception e) {}
			if (TwitchAPI.getJSONStreamShady() != null) return;
			GivePointsThread.setOffline();
			botty.sendToBunny("Shadyb00ny is now offline!");
		}
	}
	
}
