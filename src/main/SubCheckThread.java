package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubCheckThread extends Thread {
	private SubBotty botty = new SubBotty();
	public SubCheckThread(SubBotty bot) {
		botty = bot;
	}

	public void run() {
		while (true) {
			while (botty.isConnected()) {
				System.out.println("checking subs" + botty.getSubUsersArr().length );
				try {Thread.sleep(15000);} catch (Exception e) {}
				List<String> temp = botty.getSubUsers();
				List<String> toTest = botty.getPendingUsers();
				botty.pendingUsers.clear();
				botty.subUsers.clear();
				for (int i = 0; i < toTest.size(); i++) {
					if (temp.contains(toTest.get(i))) {
						if (Database.usersIni.get(toTest.get(i),"Subscriber").equals("true"))
							//is a sub and in file.
							return;
						//is a sub, but needs to be added in file.
						Database.usersIni.add(toTest.get(i),"Subscriber","true");
						try {Database.usersIni.store();} catch (IOException e) { }			
						System.out.println(toTest.get(i) + " is a new sub");
					} else {
						if (!Database.usersIni.get(toTest.get(i),"Subscriber").equals("true"))
							//not a sub, not in file.
							return;
						//not a sub, but in file. -> delete
						Database.usersIni.add(toTest.get(i),"Subscriber","false");
						try {Database.usersIni.store();} catch (IOException e) { }			
						System.out.println(toTest.get(i) + " is no longer sub.");
					} 

				}
			this.stop();
			}
			try {Thread.sleep(30000);} catch (Exception e) {}
		}
	}
}
