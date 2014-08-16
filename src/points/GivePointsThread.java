package points;

import main.*;

public class GivePointsThread extends Thread {
	ShadyBotty botty;
	public GivePointsThread(ShadyBotty bot) 
	{
	 botty = bot;
	}
	 
	public void run ()
	   {
		while (true) {
		botty.sendToBunny("a minute has passed");
		try {
			Thread.sleep(60000);
		} catch (Exception e) {}
		}
	   }
}
