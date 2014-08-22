package points;

import java.io.File;
import java.io.IOException;

import main.Database;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class Chips {

	public static double getChips(String nick) {
		double chips;
		Wini ini;
		ini = Database.currenciesIni;
		chips = ini.get(nick,"Chips") == null ? new Double(0) : Double.parseDouble(ini.get(nick,"Chips"));
		return chips;
	}

	public static void delChips(String nick, int amount) {
		Wini ini;
		try {
			ini = Database.currenciesIni;
			ini.put(nick, "Chips", getChips(nick)-amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void addChips(String nick, int amount){
		Wini ini;
		try {
			ini = Database.currenciesIni;
			ini.put(nick, "Chips", getChips(nick)+amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void setChips(String nick){
		// TODO
		
	}

}
