package commands;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.IOException;

import org.ini4j.Wini;

import points.Points;
import main.ShadyBotty;
import main.ShadyBottyMain;
import chat.ChatRules;
import chat.Privileges;
import chat.Privileges.Status;
public class Shop {
	private static long latestShop;
	private static ShadyBotty bot;
	private static Privileges obj;
	private static Class argType;


	public Shop(ShadyBotty bot) {
		setLatestShop(0);
		Shop.bot = bot;
	}

	public static long getLatestShop() {
		return latestShop;
	}



	public static void setLatestShop(long latestShop) {
		Shop.latestShop = latestShop;
	}

	public static boolean shopAvailable() {
		Long call =  System.currentTimeMillis();
		if (call - getLatestShop() > 5000)
			return true;
		return false;
	}

	public static boolean isValidShopCommand(String message, String nick) {
		String[] msg = ChatRules.getWords(message.toLowerCase());
		if (msg.length == 1 && msg[0].equals("!shop") && shopAvailable())  {
			bot.sendToBunny("Dear " +nick+", The current items in the shop are: cooldown, link, regular and gain.");
			setLatestShop(System.currentTimeMillis());
			return true;
		}
		if (msg.length != 3)
			return false;
System.out.println("correct length shop");
		if (msg[0].equals("!shop") && msg[1].equals("info"))
			return getInfoCommand(msg[2],nick);

		else if (msg[0].equals("!shop") && msg[1].equals("buy"))
			return getBuyCommand(msg[2],nick);

		else if (msg[0].equals("!regshop") && msg[1].equals("info"))
			return getInfoRegCommand(msg[2],nick);

		else if (msg[0].equals("!regshop") && msg[1].equals("buy"))
			return getBuyRegCommand(msg[2],nick);

		return false;
	}

	private static boolean getBuyRegCommand(String string, String nick) {

		return false;
	}

	private static boolean getInfoRegCommand(String string, String nick) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean getBuyCommand(String msg, String nick) {
		System.out.println("in buy cmd");
		if (msg.equals("cooldown") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(1800))) {
			if (ShadyBotty.database.getPrivileges(nick).getCooldown() == -1){
				bot.sendToBunny("you already have this item.");
				setLatestShop(System.currentTimeMillis());
 				return true;
			}
			bot.sendToBunny("Pls No abuserino spammerino! " + nick + " has bought cooldown for " + Math.round(Points.getCostItem(nick,new Double(1800))));
			Points.buyItemWithPoints(nick, new Double(1800));
			setLatestShop(System.currentTimeMillis());	
			writePrivileges(nick,msg,"-1");
			return true;
		} else if (msg.equals("link") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(500))) {
			bot.sendToBunny(nick +" can quickly post a link! paid " + Math.round(Points.getCostItem(nick,new Double(100))) + " points.");
			Points.buyItemWithPoints(nick, new Double(100));
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("regular") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(1800))) {
			if (ShadyBotty.database.getPrivileges(nick).getStatus() != Status.VIEWER){
				bot.sendToBunny("you already have this item.");
				setLatestShop(System.currentTimeMillis());
 				return true;
			}
			bot.sendToBunny(nick + " is now a regular WOOOOH! Kappa " + Math.round(Points.getCostItem(nick,new Double(1600))) + " points have been removed");
			Points.buyItemWithPoints(nick, new Double(1600));
			setLatestShop(System.currentTimeMillis());
			writePrivileges(nick,"status",msg);
			return true;
		} else if (msg.equals("gain") && shopAvailable()) {
			bot.sendToBunny(nick + "has bought level "
		+ ShadyBotty.database.getPrivileges(nick).getGain()+1
		+ " gain for " + 
		Math.round(Points.getCostItem(nick,new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500))));
			Points.buyItemWithPoints(nick, new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500));
			setLatestShop(System.currentTimeMillis());
			writePrivileges(nick,msg,"" +ShadyBotty.database.getPrivileges(nick).getGain()+1);
			return true;
		}
		return false;
	}

	private static boolean getInfoCommand(String msg, String nick) {
		if (msg.equals("cooldown") && shopAvailable()) {
			bot.sendToBunny("Cooldown let's you ignore botdelays, so he answers immediately for most commands! reduces duration of long delays. cost: " + Points.getCostItem(nick,new Double(1800)) + " for you.");
			setLatestShop(System.currentTimeMillis());	
			return true;
		} else if (msg.equals("link") && shopAvailable()) {
			bot.sendToBunny("Link allows you to post a single link within 60 seconds! Costs " + Math.round(Points.getCostItem(nick,new Double(100))) + " for you  and requires 450 points minimum.");
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("regular") && shopAvailable()) {
			bot.sendToBunny("Regular let's you lose less points when swearing and gives you access to the cool shop and factions! JUST THE TIP Kappa Cost: " + Math.round(Points.getCostItem(nick,new Double(1600))) + " for you.");
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("gain") && shopAvailable()) {
			bot.sendToBunny("PRICES VARY ON CURRENT GAIN. get more points/min! current price for " + nick + " is: "
					+ Math.round(Points.getCostItem(nick,new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500)))
					+ " for level " + (ShadyBotty.database.getPrivileges(nick).getGain()+1));
			setLatestShop(System.currentTimeMillis());			
			return true;
		}
		return false;
	}



	public static void writePrivileges(String nick, String item, String value) {
		Method method = null;
		Method filter = null;
		 argType = value.getClass();
		chat.Privileges a = ShadyBotty.database.getPrivileges(nick);
		 item = item.substring(0, 1).toUpperCase() + item.substring(1);
		 
		try {
			if(item.equals("Filter")) {
		  method = a.getClass().getDeclaredMethod("setEmoFilter", argType);
		  filter = a.getClass().getDeclaredMethod("setCapsFilter", argType);
			} else {
			method = a.getClass().getDeclaredMethod("set" + item, argType);
			}
			method.invoke(ShadyBotty.database.getPrivileges(nick), value);
			if (filter != null)
				filter.invoke(ShadyBotty.database.getPrivileges(nick), value);
		} catch (SecurityException e) {
		 System.out.println("security");
		} catch (NoSuchMethodException e) {
		 System.out.println("nosuchmethod");
		}catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Wini ini;
		try {
			
			ini = new Wini(new File("users.ini"));
			ini.put(nick,item,value);
			ini.store();
		
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
