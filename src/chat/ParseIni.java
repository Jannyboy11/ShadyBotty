package chat;

import java.io.File;
import java.io.IOException;

import org.ini4j.Wini;

public class ParseIni {

	public static void main(String[] args){
		
		Wini ini;
		
		try{
			ini = new Wini(new File("users.ini"));
			
			System.out.println(ini.size());
			
			
			
		} catch (IOException e){
			e.printStackTrace();
		}

	}

}
