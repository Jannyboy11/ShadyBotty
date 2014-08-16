package points;
import java.io.File;
import java.io.IOException;

import org.ini4j.*;
public class Points {
	public Points() {
	
	}
	
	public void setPoints(String nick, double amount) {
		Wini ini;
		try {
			ini = new Wini(new File("C:/Users/Gebruiker/Documents/GitHub/users.ini"));
			ini.put(nick, "points", amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addPoints(String nick, double amount) {
		Wini ini;
		try {
			ini = new Wini(new File("C:/Users/Gebruiker/Documents/GitHub/users.ini"));
			if (ini.get(nick,"points") == null) {
					setPoints(nick, amount);
					return;
			}
			ini.put(nick, "points", Double.parseDouble(ini.get(nick,"points"))+amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void delPoints(String nick, double amount) {
		Wini ini;
		try {
			ini = new Wini(new File("C:/Users/Gebruiker/Documents/GitHub/users.ini"));
			ini.put(nick, "points", Double.parseDouble(ini.get(nick,"points")) - amount);
			ini.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
