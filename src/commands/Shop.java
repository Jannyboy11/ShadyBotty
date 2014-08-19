package commands;
import java.io.File;
import java.io.IOException;

import org.ini4j.Wini;

import points.Points;
import main.ShadyBotty;
import main.ShadyBottyMain;
import chat.ChatRules;
import chat.Privileges.Status;
public class Shop {
	private static long latestShop;



	public Shop() {
		setLatestShop(0);
	}

	public static long getLatestShop() {
		return latestShop;
	}



	public static void setLatestShop(long latestShop) {
		Shop.latestShop = latestShop;
	}

	public static boolean shopAvailable() {
		Long call =  System.currentTimeMillis();
		if (call - getLatestShop() < 5000)
			return true;
		return false;
	}

	public static boolean isValidShopCommand(String message, String nick) {
		String[] msg = ChatRules.getWords(message.toLowerCase());
		if (msg.length == 1 && msg[0].equals("!shop") && shopAvailable())  {
			ShadyBottyMain.bot.sendToBunny("Dear " +nick+", The current items in the shop are: cooldown, link, regular and gain.");
			setLatestShop(System.currentTimeMillis());
			return true;
		}
		if (msg.length != 3)
			return false;

		if (msg[0].equals("!shop") && msg[1].equals("info"))
			return getInfoCommand(msg[3],nick);

		else if (msg[0].equals("!shop") && msg[1].equals("buy"))
			return getBuyCommand(msg[3],nick);

		else if (msg[0].equals("!regshop") && msg[1].equals("info"))
			return getInfoRegCommand(msg[3],nick);

		else if (msg[0].equals("!regshop") && msg[1].equals("buy"))
			return getBuyRegCommand(msg[3],nick);

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
		if (msg.equals("cooldown") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(1800))) {
			if (ShadyBotty.database.getPrivileges(nick).getCooldown() == -1){
				ShadyBottyMain.bot.sendToBunny("you already have this item.");
				setLatestShop(System.currentTimeMillis());
 				return true;
			}
			ShadyBottyMain.bot.sendToBunny("Pls No abuserino spammerino! " + nick + " has bought cooldown for " + Points.getCostItem(nick,new Double(1800)));
			Points.buyItemWithPoints(nick, new Double(1800));
			setLatestShop(System.currentTimeMillis());	
			writePrivileges(nick,msg);
			return true;
		} else if (msg.equals("link") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(500))) {
			ShadyBottyMain.bot.sendToBunny(nick +" can quickly post a link! paid " + Points.getCostItem(nick,new Double(100)) + " points.");
			Points.buyItemWithPoints(nick, new Double(100));
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("regular") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(1800))) {
			if (ShadyBotty.database.getPrivileges(nick).getStatus() != Status.VIEWER){
				ShadyBottyMain.bot.sendToBunny("you already have this item.");
				setLatestShop(System.currentTimeMillis());
 				return true;
			}
			ShadyBottyMain.bot.sendToBunny(nick + " is now a regular WOOOOH! Kappa " + Points.getCostItem(nick,new Double(1600)) + " points have been removed");
			Points.buyItemWithPoints(nick, new Double(1600));
			setLatestShop(System.currentTimeMillis());
			writePrivileges(nick,msg);
			return true;
		} else if (msg.equals("gain") && shopAvailable()) {
			ShadyBottyMain.bot.sendToBunny(nick + "has bought level "
		+ ShadyBotty.database.getPrivileges(nick).getGain()+1
		+ " gain for " + 
		Points.getCostItem(nick,new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500)));
			Points.buyItemWithPoints(nick, new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500));
			setLatestShop(System.currentTimeMillis());
			writePrivileges(nick,msg);
			return true;
		}
		return false;
	}

	private static boolean getInfoCommand(String msg, String nick) {
		if (msg.equals("cooldown") && shopAvailable()) {
			ShadyBottyMain.bot.sendToBunny("Cooldown let's you ignore botdelays, so he answers immediately for most commands! reduces duration of long delays. cost: " + Points.getCostItem(nick,new Double(1800)) + " for you.");
			setLatestShop(System.currentTimeMillis());	
			return true;
		} else if (msg.equals("link") && shopAvailable()) {
			ShadyBottyMain.bot.sendToBunny("Link allows you to post a single link within 60 seconds! Costs " + Points.getCostItem(nick,new Double(100)) + " for you  and requires 450 points minimum.");
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("regular") && shopAvailable()) {
			ShadyBottyMain.bot.sendToBunny("Regular let's you lose less points when swearing and gives you access to the cool shop and factions! JUST THE TIP Kappa Cost: " + Points.getCostItem(nick,new Double(1600)) + " for you.");
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("gain") && shopAvailable()) {
			ShadyBottyMain.bot.sendToBunny("PRICES VARY ON LEVEL. current price for " + nick + " is: "
					+ Points.getCostItem(nick,new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500))
					+ " for level " + (ShadyBotty.database.getPrivileges(nick).getGain()+1));
			setLatestShop(System.currentTimeMillis());			
			return true;
		}
		return false;
	}



	public static void writePrivileges(String nick, String item) {
		Wini ini;
		try {
			
			ini = new Wini(new File("users.ini"));
			
			
			/*
			 * FLIPPING UGLY, I KNOW, GEEN IDEE HOE DIT SNELLER KAN, IK WOU DAT JE 
			 * iets kon doen zoals getPrivileges(nick).set + item + (-1);
			 * ofzo zodat je niet de string hoefde te parsen xD
			 * maar liever al het lelijke in 1 functie :P
			 */
			if(item.equals("Filter")) {
				ShadyBotty.database.getPrivileges(nick).setEmoFilter(-1);
				ShadyBotty.database.getPrivileges(nick).setCapsFilter(-1);
				ini.put(nick,item,-1);
			} else if(item.equals("cooldown")) {
				ShadyBotty.database.getPrivileges(nick).setCooldown(-1);
				ini.put(nick,item,-1);
			} else if(item.equals("regular")) {
				ShadyBotty.database.getPrivileges(nick).setStatus(Status.REGULAR);
				ini.put(nick,"status",item);
			} else if(item.equals("gain")) {
				ShadyBotty.database.getPrivileges(nick).setGain(ShadyBotty.database.getPrivileges(nick).getGain() + 1);
				ini.put(nick,item,ini.get(nick,item) +1);
			} else if(item.equals("jb940")) {
				ShadyBotty.database.getPrivileges(nick).setFaction("jb940");
				ini.put(nick,"faction",item);
			} else if(item.equals("phantom")) {
				ShadyBotty.database.getPrivileges(nick).setFaction("phantom");
				ini.put(nick,"faction",item);
			} else if(item.equals("kevin")) {
				ShadyBotty.database.getPrivileges(nick).setFaction("kevin");
				ini.put(nick,"faction",item);
			} else if(item.equals("links")) {
				ShadyBotty.database.getPrivileges(nick).setLinks(-1);
				ini.put(nick,item,-1);
			}
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
