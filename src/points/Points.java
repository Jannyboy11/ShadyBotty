package points;
import main.Database;
import main.ShadyBotty;

import org.ini4j.*;
public class Points {

	public Points() {

	}

	public static double getPoints(String nick) {
		double points;
		Wini ini;
		ini = Database.getCurrencies();
		points = ini.get(nick.toLowerCase(),"points") == null ? new Double(0) : Double.parseDouble(ini.get(nick.toLowerCase(),"points"));
		return points;	
	}

	public void setPoints(String nick, double amount) {
		Wini ini = Database.getCurrencies();
		ini.put(nick, "points", amount);
		Database.storeCurrencies();
	}

	public static void addPoints(String nick, double amount) {
		Wini ini = Database.getCurrencies();
		ini.put(nick, "points", getPoints(nick)+amount);
		Database.storeCurrencies();
	}
	
	public static void addNoSavePoints(Wini ini, String nick, double amount) {
		ini.put(nick, "points", getPoints(nick)+amount);
	}


	public static void delPoints(String nick, double amount) {
		Wini ini = Database.getCurrencies();
		ini.put(nick, "points", getPoints(nick) - amount);
		Database.storeCurrencies();
	}

	public static void buyItemWithPoints(String nick, double amount) {
		double amount2 = amount;
		if (ShadyBotty.database.getPrivileges(nick).getFaction().equals("JB940"))
			amount2 = amount*0.9;
		delPoints(nick, amount2);
	}

	public static double getCostItem(String nick, double amount) {
//		System.out.println(ShadyBotty.database.getPrivileges(nick).getFaction());
		double amount2 = amount;
		if (ShadyBotty.database.getPrivileges(nick).getFaction().equals("JB940"))
			amount2 = amount*0.9;
		return amount2;
	}

}
