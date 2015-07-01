package main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ini4j.Wini;

import chat.Privileges;

public class Database {
	private static HashMap<String,Long> lastChat;
	private HashMap<String,Privileges> privileges;
	public static ArrayList<String> currentUsers;
	private static Wini usersIni;
	private static Wini currenciesIni;
	private static Wini autoreplyIni;
	public static Database d;
	
	static {
		try {

			usersIni = new Wini(new File("users.ini"));
			if (usersIni.get("subscr") == null)
				usersIni.put("subscr", "jb940","1");
			currenciesIni = new Wini(new File("currencies.ini"));
			autoreplyIni = new Wini(new File("autoreply.ini"));
			//	autoreplyIni = new Wini(new File("autoreply.ini"));


		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		Pattern title = Pattern.compile("^(?<score>((0|[2-9]|1[0-2]?)-[0-3])|In)(( )(?<hero>(?i:Warrior|Shaman|Rogue|Paladin|Hunter|Druid|Warlock|Mage|Priest|Draft)))?[^a-zA-Z]*(?<rest>.*)");
		String hero = "In Draft";
		Matcher match = title.matcher(hero);

		System.out.println(match.matches());
		System.out.println(match.group("score"));
	}


	public Database() {
		d = this;
		lastChat = new HashMap<String,Long>();
		privileges = new HashMap<String,chat.Privileges>();
		currentUsers = (new ArrayList<String>());
	}




	public Privileges getPrivileges(String user) {
		return privileges.get(user.toLowerCase());
	}

	public void addPrivileges(String nick) {
		if (!privileges.containsKey(nick.toLowerCase()))
			privileges.put(nick.toLowerCase(), new Privileges(nick));
	}




	public void setLastMessage(String user, Long time) { 
		lastChat.put(user, time);
	}




	public Long getLongLastMessage(String user) { 
		Long last = lastChat.get(user);
		return last;
	}

	public String getTimeLastMessage(String user) { 
		Long last = getLongLastMessage(user);
		Date lastDate = new Date(last);
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
		return ft.format(lastDate);
	}




	public static Long getDifferenceInLong(String user) {
		Long last =  System.currentTimeMillis();;

		if (lastChat.containsKey(user)) 
			last = lastChat.get(user);
		else last = 0L;

		Long now = System.currentTimeMillis();
		Long difference = now-last;
		return difference;
	}

	public Long getDifferenceSeconds(String user) {
		Long difference = getDifferenceInLong(user);
		return difference/1000;
	}
	
	public static boolean lastMessageDelayBiggerThen(String user,Long milis) {
		return getDifferenceInLong(user) > milis;
	}



	public void clearCurrentUsers() {
		currentUsers.clear();
	}

	public ArrayList<String> getCurrentUsers() {
		return currentUsers;
	}





	public void addCurrentUsers(String nick) {
		if (!currentUsers.contains(nick))
			currentUsers.add(nick.toLowerCase());
	}

	public void delCurrentUsers(String nick) {
		if (currentUsers.contains(nick))
			currentUsers.remove(nick);
	}


	public static Wini getUsers() {
		synchronized(usersIni) {
			return usersIni;
		}
	}
	
	public static void  storeUsers() {
		synchronized(usersIni) {
			try {
				usersIni.store();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static Wini getCurrencies() {
		synchronized(currenciesIni) {
			return currenciesIni;
		}
	}

	public static synchronized void  storeCurrencies() {
		synchronized(currenciesIni) {
			try {
				currenciesIni.store();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * removes a person to the subscribed list<br>
	 * this nick no longer gets a message once bot gets started up and once a game is won/lost
	 * @param name the nick to add
	 * @return <code> true </code> if and only if, the user was subscribed and could be deleted
	 */
	public static boolean deleteSub(String name) {
		if (usersIni.get("subscr").containsKey(name)) {
			usersIni.remove("subscr", name);
			return true;
		}
		return false;
	}
	
	/**
	 * adds a person to the subscribed list<br>
	 * this nick gets a message once bot gets started up and once a game is won/lost
	 * @param name the nick to add
	 * @return <code> true </code> if and only if, the user was NOT subscribed and could be added
	 */
	public static boolean addSub(String name) {
		if (usersIni.get("subscr").containsKey(name)) {
			return false;
		}
		usersIni.put("subscr", name,"1");
		return true;
	}
	
	
	public static Wini getAutoreplies() {
		synchronized(autoreplyIni) {
			return autoreplyIni;
		}
	}
	
	public static synchronized void  storeAutoreplies() {
		synchronized(autoreplyIni) {
			try {
				autoreplyIni.store();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	public static void subgame(String player, boolean b, int wins, int losses) {
		String tosend = "Shady has just " + (b ? "won" : "lost") + " a game. current score is " + 
				wins + "-" + losses + " with " + player + ".";
		sendSubs(tosend);
	}


	public static void sendSubs(String tosend) {
		for (String a : usersIni.get("subscr").keySet()) {
			if (lastMessageDelayBiggerThen(a, 300000L))
				ShadyBottyMain.privbot.sendPriv(a, tosend);
		}
	}



}
