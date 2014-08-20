package chat;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.ini4j.Wini;

public class ParseIni {
	
	File theIniFile;
	
	public ParseIni(File file){
		
	}

	public static void main(String[] args){
		
		Wini ini;
		
		try{
			ini = new Wini(new File("users.ini"));
			
			System.out.println(ini.size());
			
			
			
		} catch (IOException e){
			e.printStackTrace();
		}

	}
	
	public List<String> getSettings(String nick){
		
		Wini ini;
		List<String> result = new LinkedList<String>();
		try {
			ini = new Wini(theIniFile);
			Scanner scanner = new Scanner(theIniFile);
			
			String foundprofile = scanner.findWithinHorizon("[" + nick + "]", ini.size());
			while (scanner.hasNextLine()){
				String next = scanner.next();
				if (!(next.startsWith("["))){
					result.add(next);
				} else {
					return result;
				}
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//there was no profile for this user
		return null;
	}

}
