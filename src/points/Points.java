package points;
import java.io.File;
import java.util.*;
import java.io.IOException;

import main.Database;
import main.ShadyBotty;

import org.ini4j.*;
public class Points {
	public Points() {
	
	}
	
	public static double getPoints(String nick) {
		double points;
		Wini ini;
			ini = Database.currenciesIni;
//			Set<String> a = ini.get(nick) == null ?  null : ini.get(nick).keySet();
//			if (a != null) {
//				Iterator<String> q = a.iterator();
//				while (q.hasNext()) {
//					System.out.println(q.next());
//				}
//			}
		//	System.out.println(ini.get(nick.toLowerCase(),"points") + " points." + ini.get(nick,"points") + " not lowercase " + nick);
			points = ini.get(nick.toLowerCase(),"points") == null ? new Double(0) : Double.parseDouble(ini.get(nick.toLowerCase(),"points"));
			return points;	
	}
	
	public void setPoints(String nick, double amount) {
		Wini ini;
		try {
			ini = Database.currenciesIni;
			ini.put(nick.toLowerCase(), "points", amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addPoints(String nick, double amount) {
		Wini ini;
		try {
			ini = Database.currenciesIni;
			ini.put(nick.toLowerCase(), "points", getPoints(nick)+amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static void delPoints(String nick, double amount) {
		Wini ini;
		try {
			ini = Database.currenciesIni;
			ini.put(nick, "points", getPoints(nick) - amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void buyItemWithPoints(String nick, double amount) {
		double amount2 = amount;
		if (ShadyBotty.database.getPrivileges(nick).getFaction().equals("JB940"))
			amount2 = amount*0.9;
		delPoints(nick, amount2);
	}
	
	public static double getCostItem(String nick, double amount) {
		System.out.println(ShadyBotty.database.getPrivileges(nick).getFaction());
		double amount2 = amount;
		if (ShadyBotty.database.getPrivileges(nick).getFaction().equals("JB940"))
		 amount2 = amount*0.9;
		return amount2;
	}

}
