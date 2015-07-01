package points;

import main.Database;

import org.ini4j.Wini;

public class Chips {

	public static double getChips(String nick) {
		double chips;
		Wini ini;
		ini = Database.getCurrencies();
		chips = ini.get(nick,"Chips") == null ? new Double(0) : Double.parseDouble(ini.get(nick,"Chips"));
		return chips;
	}

	public static void delChips(String nick, int amount) {
		Wini ini;
			ini = Database.getCurrencies();
			ini.put(nick, "Chips", getChips(nick)-amount);
			Database.storeCurrencies();

		
	}
	
	public static void addChips(String nick, int amount){
		Wini ini;
			ini = Database.getCurrencies();
			ini.put(nick, "Chips", getChips(nick)+amount);
			Database.storeCurrencies();
		
	}
	
	public static void setChips(String nick){
		// TODO
		
	}

}
