package commands;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.IOException;

import org.ini4j.Wini;

import points.Chips;
import points.Points;
import main.ShadyBotty;
import chat.ChatRules;
import chat.Privileges;
import chat.Privileges.Status;
public class Shop {
	private static long latestShop;
	private static ShadyBotty bot;
	private static Privileges obj;
	private static Class<? extends String> argType;


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
			bot.sendToBunny("Dear " +StandardCmds.getNick(nick)+", The current items in the shop are: cooldown, link, regular and gain.");
			setLatestShop(System.currentTimeMillis());
			return true;
		} else if (msg.length == 1 && msg[0].equals("!regshop") && shopAvailable() && ShadyBotty.database.getPrivileges(nick).getStatus() != Status.VIEWER)  {
			bot.sendToBunny("Dear " +StandardCmds.getNick(nick)+", The items in the regularshop are: Chips, Points, Links, Filter, Premium");
			setLatestShop(System.currentTimeMillis());
			return true;
		}
		if (msg.length != 3)
			return false;
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

	private static boolean getBuyRegCommand(String msg, String nick) {
		String nickn = StandardCmds.getNick(nick);
		if (msg.equals("chips") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(500))) {
			bot.sendToBunny("You gamble addict Kappa. "+ nickn + " has bought 250 chips for: " + Points.getCostItem(nick,new Double(500)));
			Points.buyItemWithPoints(nick, new Double(500));
			Chips.addChips(nick, 250);
			setLatestShop(System.currentTimeMillis());	
			return true;
		}
		else if (msg.equals("points") && shopAvailable() && Chips.getChips(nick) >= 420) {
			bot.sendToBunny(nickn + "has just bought 800 points with 420 chips. such points Kappa");
			Chips.delChips(nick, 420);
			Points.addPoints(nick,new Double(800));
			setLatestShop(System.currentTimeMillis());	
			return true;
		} else if (msg.equals("links") && shopAvailable()  && Points.getPoints(nick) > Points.getCostItem(nick,new Double(650))) {
			if (ShadyBotty.database.getPrivileges(nick).getLinks() == -1){
				bot.sendToBunny("you already have this item " + nickn + ".");
				setLatestShop(System.currentTimeMillis());
				return true;
			}
			bot.sendToBunny(nickn + " can now permanently post links for just " + Math.round(Points.getCostItem(nick,new Double(650))) + " points.  No ab00se pls");
			Points.buyItemWithPoints(nick, new Double(650));
			setLatestShop(System.currentTimeMillis());		
			writePrivileges(nick,msg,"-1");
			return true;
		} else if (msg.equals("filter") && shopAvailable()   && Points.getPoints(nick) > Points.getCostItem(nick,new Double(600))) {
			if (ShadyBotty.database.getPrivileges(nick).getEmoFilter() == -1){
				bot.sendToBunny("you already have this item " + nickn + ".");
				setLatestShop(System.currentTimeMillis());
				return true;
			}
			bot.sendToBunny("Pls not too much Capserino " +nickn+ " Kappa You have bought filter for " + Math.round(Points.getCostItem(nick,new Double(600))) + " points " );
			Points.buyItemWithPoints(nick, new Double(600));
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("premium") && shopAvailable()   && Points.getPoints(nick) > Points.getCostItem(nick,new Double(5000))) {
			if (ShadyBotty.database.getPrivileges(nick).getStatus() != Status.REGULAR){
				bot.sendToBunny("you already have this item or are a moderino :3 " + nickn + ".");
				setLatestShop(System.currentTimeMillis());
				return true;
			}
			bot.sendToBunny("WOOOH. "+ nickn + " IS NOW A PREMIUM. SUCCES! commands: !nick NAME !challenge NICK !stabrandom. " + Math.round(Points.getCostItem(nick,new Double(5000))) + " points substracted.");
			Points.buyItemWithPoints(nick, new Double(5000));
			setLatestShop(System.currentTimeMillis());			
			return true;
		}
		return false;
	}

	private static boolean getInfoRegCommand(String msg, String nick) {
		String nickn = StandardCmds.getNick(nick);
		if (msg.equals("chips") && shopAvailable()) {
			bot.sendToBunny("Chips are used to gamble and make PROFITZ :3 costs" + Points.getCostItem(nick,new Double(500)) + " for 250 chips, for " + nickn);
			setLatestShop(System.currentTimeMillis());	
			return true;
		}
		else if (msg.equals("points") && shopAvailable()) {
			bot.sendToBunny("Buy Points to buy other stuff Kappa Costs 420 (BLAZE IT) chips for 800 points.");
			setLatestShop(System.currentTimeMillis());	
			return true;
		} else if (msg.equals("links") && shopAvailable()) {
			bot.sendToBunny("Links permanently allows you to post links. Costs " + Math.round(Points.getCostItem(nick,new Double(650))) + " points for " + nickn);
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("filter") && shopAvailable()) {
			bot.sendToBunny("Filter will make caps/emote filters ignore you and have a higher limit for characters. Costs:  " + Math.round(Points.getCostItem(nick,new Double(600))) + " for  " + nickn);
			setLatestShop(System.currentTimeMillis());	
			writePrivileges(nick,msg,"-1");
			return true;
		} else if (msg.equals("premium") && shopAvailable()) {
			bot.sendToBunny("The Highest of Highest. Premium: access to a custom nick (!nick NAME), !challenge someone to a duel and !stabrandom people! cost for " + nickn + " is: "
					+ Math.round(Points.getCostItem(nick,new Double(5000))));
			setLatestShop(System.currentTimeMillis());	
			writePrivileges(nick,"Status",msg);
			return true;
		}
		return false;
	}

	private static boolean getBuyCommand(String msg, String nick) {
		String nickn = StandardCmds.getNick(nick);
		System.out.println("in buy cmd");
		if (msg.equals("cooldown") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(1800))) {
			if (ShadyBotty.database.getPrivileges(nick).getCooldown() == -1){
				bot.sendToBunny("you already have this item " + nickn + ".");
				setLatestShop(System.currentTimeMillis());
				return true;
			}
			bot.sendToBunny("Pls No abuserino spammerino! " + nickn + " has bought cooldown for " + Math.round(Points.getCostItem(nick,new Double(1800))));
			Points.buyItemWithPoints(nick, new Double(1800));
			setLatestShop(System.currentTimeMillis());	
			writePrivileges(nick,msg,"-1");
			return true;
		} else if (msg.equals("link") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(500))) {
			bot.sendToBunny(nickn +" can quickly post a link! paid " + Math.round(Points.getCostItem(nick,new Double(100))) + " points.");
			Points.buyItemWithPoints(nick, new Double(100));
			ShadyBotty.database.getPrivileges(nick).setTemplink(true);
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("regular") && shopAvailable() && Points.getPoints(nick) > Points.getCostItem(nick,new Double(1800))) {
			if (ShadyBotty.database.getPrivileges(nick).getStatus() != Status.VIEWER){
				bot.sendToBunny("you already have this item " + nickn + ".");
				setLatestShop(System.currentTimeMillis());
				return true;
			}
			bot.sendToBunny(nickn + " is now a regular WOOOOH! Kappa " + Math.round(Points.getCostItem(nick,new Double(1600))) + " points have been removed");
			Points.buyItemWithPoints(nick, new Double(1600));
			setLatestShop(System.currentTimeMillis());
			writePrivileges(nick,"Status",msg);
			return true;
		} else if (msg.equals("gain") && shopAvailable() && Points.getPoints(nick) >= (Points.getCostItem(nick,new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500)))) {
			bot.sendToBunny(nickn + " has bought level "
					+ (ShadyBotty.database.getPrivileges(nick).getGain()+1)
					+ " gain for " + 
					Math.round(Points.getCostItem(nick,new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500))));
			Points.buyItemWithPoints(nick, new Double((ShadyBotty.database.getPrivileges(nick).getGain()+1)*1500));
			setLatestShop(System.currentTimeMillis());
			System.out.println(nick + "  " + msg +"  " +(ShadyBotty.database.getPrivileges(nick).getGain()+1));
			writePrivileges(nick,msg,"" +(ShadyBotty.database.getPrivileges(nick).getGain()+1));
			return true;
		}
		return false;
	}

	private static boolean getInfoCommand(String msg, String nick) {
		String nickn = StandardCmds.getNick(nick);
		if (msg.equals("cooldown") && shopAvailable()) {
			bot.sendToBunny("Cooldown let's you ignore botdelays, so he answers immediately for most commands! reduces duration of long delays. cost: " + Points.getCostItem(nick,new Double(1800)) + " for " + nickn);
			setLatestShop(System.currentTimeMillis());	
			return true;
		} else if (msg.equals("link") && shopAvailable()) {
			bot.sendToBunny("Link allows you to post a single link within 60 seconds! Costs " + Math.round(Points.getCostItem(nick,new Double(100))) + " and requires 450 points minimum. for " + nickn);
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("regular") && shopAvailable()) {
			bot.sendToBunny("Regular let's you lose less points when swearing and gives you access to the cool shop and factions! JUST THE TIP Kappa Cost: " + Math.round(Points.getCostItem(nick,new Double(1600))) + " for  " + nickn);
			setLatestShop(System.currentTimeMillis());			
			return true;
		} else if (msg.equals("gain") && shopAvailable()) {
			bot.sendToBunny("PRICES VARY ON CURRENT GAIN. get more points/min! current price for " + nickn + " is: "
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
		String item2 = item.substring(0, 1).toUpperCase() + item.substring(1);

		try {
			if(item.equals("Filter")) {
				method = a.getClass().getDeclaredMethod("setEmoFilter", argType);
				filter = a.getClass().getDeclaredMethod("setCapsFilter", argType);
			} else {
				method = a.getClass().getDeclaredMethod("set" + item2, argType);
			}
			method.invoke(ShadyBotty.database.getPrivileges(nick.toLowerCase()), value);
			if (filter != null)
				filter.invoke(ShadyBotty.database.getPrivileges(nick.toLowerCase()), value);
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
		System.out.println(item2 + " bought for " + nick + "fr value " + value);
		
		Wini ini;
		try {
			if (item2.equals("Gain")) {
				ini = new Wini(new File("currencies.ini"));
				ini.put(nick.toLowerCase(),item2,value);
				ini.store();
			} else {
			ini = new Wini(new File("users.ini"));
			ini.put(nick.toLowerCase(),item2,value);
			ini.store();
			}
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
