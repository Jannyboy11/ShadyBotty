package points;
import java.io.File;
import java.io.IOException;

import org.ini4j.*;
public class Points {
	public Points() {
	
	}
	
	public double getPoints(String nick) {
		double points;
		Wini ini;
		try {
			ini = new Wini(new File("C:/Users/Gebruiker/Documents/GitHub/users.ini"));
			points = ini.get(nick,"points") == null ? new Double(0) : Double.parseDouble(ini.get(nick,"points"));
			return points;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Double(0);
		
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

			ini.put(nick, "points", getPoints(nick)+amount);
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

}
