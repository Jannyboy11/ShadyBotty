package points;

import java.util.ArrayList;

import org.ini4j.Wini;

import main.*;

public class GivePointsThread extends Thread {
	private static boolean  Online = false;
	ShadyBotty botty;
	public GivePointsThread(ShadyBotty bot) 
	{
		botty = bot;
	}

	@Override
	public void run ()
	{
		while (Online) {
			ArrayList<String> temp = ShadyBotty.database.getCurrentUsers();
			Wini ini =Database.getCurrencies();
//			System.out.println("poitns given");
			for (int i=0; i<temp.size(); i++) {
				String nick = temp.get(i);
				if (!ShadyBotty.database.getPrivileges(nick).isSubscriber()) { //nonsub 
					Points.addNoSavePoints(ini,nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2) * 0.25);
					if (ShadyBotty.database.getDifferenceSeconds(nick) < 600) {

						Points.addNoSavePoints(ini,nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2));
					}
				} else { // subs get x2
					Points.addNoSavePoints(ini,nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2) * 0.5);
					if (ShadyBotty.database.getDifferenceSeconds(nick) < 600) {

						Points.addNoSavePoints(ini,nick,(ShadyBotty.database.getPrivileges(nick).getGain() + 2) * 2);
					}
				}
			}
			Database.storeCurrencies();
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
