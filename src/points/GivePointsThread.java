package points;

import java.util.ArrayList;
import main.*;

public class GivePointsThread extends Thread {
	private static boolean  Online = false;
	ShadyBotty botty;
	public GivePointsThread(ShadyBotty bot) 
	{
		botty = bot;
	}

	public void run ()
	{
		while (Online) {
			ArrayList<String> temp = ShadyBotty.database.getCurrentUsers();
			System.out.println("poitns given");
			for (int i=0; i<temp.size(); i++) {
				String nick = temp.get(i);
				ShadyBotty.points.addPoints(nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2) * 0.25);
				if (ShadyBotty.database.getDifferenceSeconds(nick) < 600) {

					ShadyBotty.points.addPoints(nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2));
				}
			}
			try {
				Thread.sleep(60000);
			} catch (Exception e) {}
		}
		try {
			Thread.sleep(5000);
		} catch (Exception e) {}
	}

	public void setOnline() {
		Online = true;
	}

	public void setOffline() {
		Online = false;
	}
	public boolean getStatus() {
		return Online;
	}
}
