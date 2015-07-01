package api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetShadyMoveThread extends Thread {
	//local=False (?<entity>\\[n.*)
	private Pattern _tagChangeRegex = Pattern.compile("TAG_CHANGE Entity=(?<entity>.+) tag=(?<tag>\\w+) value=(?<value>\\w+)");
	private Pattern _cardMovementRegex = Pattern.compile("\\w* local=(False|True) (?<entity>\\[n.+) zone from (?<from>.*) -> (?<to>.*)");
	private Pattern _cardAttackRegex = Pattern.compile(".*ACTION_START.*(cardId=(?<Id>\\w*)).*SubType=ATTACK.*");
	private Pattern _heroPowerRegex = Pattern.compile(".*ACTION_START.*(cardId=(?<Id>\\w*)).*SubType=POWER.*");
	private Pattern _entityNameRegex = Pattern.compile("TAG_CHANGE Entity=(?<name>\\w+) tag=PLAYER_ID value=(?<value>\\d)");
	private Pattern _entityRegex = Pattern.compile("(?=id=(?<id>(\\d+)))(?=name=(?<name>(\\w+)))?(?=zone=(?<zone>(\\w+)))?(?=zonePos=(?<zonePos>(\\d+)))?(?=cardId=(?<cardId>(\\w+)))?(?=player=(?<player>(\\d+)))?(?=type=(?<type>(\\w+)))?");

	FileReader log;
	public GetShadyMoveThread() {
		try {
			log = new FileReader("C:\\Program Files (x86)\\Hearthstone\\Hearthstone_Data\\output_log.txt");
		} catch (FileNotFoundException e) {
			try {
				log = new FileReader("C:\\Program Files\\Hearthstone\\Hearthstone_Data\\output_log.txt");
			} catch (FileNotFoundException e1) {
				System.err.println("Could not find Log file for shady BibleThump");
			}
		}
	}

	@Override
	public void run() {
		BufferedReader lnr = null;
		String str;
		if (log != null)
			try{

				lnr = new BufferedReader(log);
				while ((str=lnr.readLine())!=null) {
					
				}

				// read lines till the end of the stream
				while(true)
				{
					if ((str=lnr.readLine())!=null) {


						// prints string
						if (str.startsWith("[Power]")) {
							Matcher b = _tagChangeRegex.matcher(str);
							Matcher hero = _heroPowerRegex.matcher(str);
							Matcher attack = _cardAttackRegex.matcher(str);
							if (attack.find())
								System.err.println(true + " "  + str);
							if (hero.find())
								System.err.println(false + " "  + str);
						}
						else if(str.startsWith("[Zone]")){
							Matcher b = _cardMovementRegex.matcher(str);
							if (b.find()) {
								System.err.println(b.group("entity")+ "." + b.group("from") + " -> " + b.group("to"));
								Matcher c = _entityRegex.matcher(b.group("entity"));
							}
						}

					}
					else 
						Thread.sleep(500);
				}
			}catch(Exception e){

				// if any error occurs
				e.printStackTrace();
			}
	}



}
