package main;

import java.util.ArrayList;
import org.ini4j.Wini;

public class SubCheckThread extends Thread {
	private SubBotty botty;
	public SubCheckThread(SubBotty bot) {
		botty = bot;
	}

	@Override
	public void run() {
		while (true) {
			try {Thread.sleep(15000);} catch (Exception e) {}
			ArrayList<String> temp = SubBotty.subUsers;
			ArrayList<String> toTest = SubBotty.pendingUsers;
			Wini ini = Database.getUsers();
			for (int i = 0; i < toTest.size(); i++) {
				if (temp.contains(toTest.get(i))) {
					if (ini.get(toTest.get(i),"Subscriber") == null || 
							ini.get(toTest.get(i),"Subscriber").equals("false")) {
						ini.add(toTest.get(i),"Subscriber","true");
						System.out.println(toTest.get(i) + " is a new sub");
						ShadyBotty.database.getPrivileges(toTest.get(i)).setSubscriber(true);
						Database.storeUsers();	
					}
				} else {
					if (ini.get(toTest.get(i),"Subscriber") == null || 
							ini.get(toTest.get(i),"Subscriber").equals("true")) {
						ini.add(toTest.get(i),"Subscriber","false");
						ShadyBotty.database.getPrivileges(toTest.get(i)).setSubscriber(true);
						System.out.println(toTest.get(i) + " is no longer sub.");
						Database.storeUsers();
					}

				} 

			}
			SubBotty.pendingUsers.clear();
			SubBotty.subUsers.clear();
			//			}
			//			try {Thread.sleep(30000);} catch (Exception e) {}
		}
	}
}
