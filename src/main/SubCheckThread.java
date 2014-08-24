package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubCheckThread extends Thread {
	private SubBotty botty;
	public SubCheckThread(SubBotty bot) {
		botty = bot;
	}

	public void run() {
		while (true) {
			System.out.println("w true");
				try {Thread.sleep(15000);} catch (Exception e) {}
				System.out.println("15 sec passed");
				System.out.println(SubBotty.pendingUsers.size());
				ArrayList<String> temp = SubBotty.subUsers;
				ArrayList<String> toTest = SubBotty.pendingUsers;

				for (int i = 0; i < toTest.size(); i++) {
					System.out.println("checking nick: " +toTest.get(i));
					if (temp.contains(toTest.get(i))) {

						if (Database.usersIni.get(toTest.get(i),"Subscriber").equals("true"))
							//is a sub and in file.
							continue;
						//is a sub, but needs to be added in file.
						Database.usersIni.add(toTest.get(i),"Subscriber","true");
						try {Database.usersIni.store();} catch (IOException e) { }			
						System.out.println(toTest.get(i) + " is a new sub");
					} else {
						if (Database.usersIni.get(toTest.get(i),"Subscriber") == null) {
							Database.usersIni.add(toTest.get(i),"Subscriber","false");
							try {Database.usersIni.store();} catch (IOException e) { }	
						}
						System.out.println("not contains: " +toTest.get(i));
						if (!Database.usersIni.get(toTest.get(i),"Subscriber").equals("true"))
							//not a sub, not in file.
							continue;
						//not a sub, but in file. -> delete
						Database.usersIni.add(toTest.get(i),"Subscriber","false");
						try {Database.usersIni.store();} catch (IOException e) { }			
						System.out.println(toTest.get(i) + " is no longer sub.");
					} 

				}
				SubBotty.pendingUsers.clear();
				SubBotty.subUsers.clear();
//			}
//			try {Thread.sleep(30000);} catch (Exception e) {}
		}
	}
}
