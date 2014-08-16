package points;

import java.util.ArrayList;
import main.*;

public class GivePointsThread extends Thread {
	private static boolean  Online = true;
	ShadyBotty botty;
	public GivePointsThread(ShadyBotty bot) 
	{
		botty = bot;
	}

	public void run ()
	{
		while (Online) {
			ArrayList<String> temp = ShadyBotty.database.getCurrentUsers();
			System.out.println(temp.size());
			for (int i=0; i<temp.size(); i++) {
				String nick = temp.get(i);
				ShadyBotty.points.addPoints(nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2) * 0.25);
				if (ShadyBotty.database.getDifferenceSeconds(nick) < 600)
					System.out.println(ShadyBotty.database.getDifferenceSeconds(nick) + "  " + nick);
				ShadyBotty.points.addPoints(nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2));
			}
			try {
				Thread.sleep(60000);
			} catch (Exception e) {}
		}
	}
	
	public static void setOnline() {
		Online = true;
	}
	
	public static void setOffline() {
		Online = false;
	}
}