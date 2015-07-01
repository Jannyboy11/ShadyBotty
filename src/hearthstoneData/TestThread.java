package hearthstoneData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import main.Database;
import main.ShadyBotty;
import main.ShadyBottyMain;


public class TestThread extends Thread {
	//UNKNOWN HUMAN PLAYER
	public boolean win = false;
	FileReader log;
	private PrintWriter gamelog;
	private String lastname = null;
	private int spectated = -1;
	public String hero2 = "";
	private String p1 = null;
	private String p2 = null;
	public int wins = 0;
	public int losses = 0;
	//(?!GameEntity|UNKNOWN ENTITY)
	private static Pattern _entityNameRegex = Pattern.compile("\\[Power\\] GameState.DebugPrintPower().*TAG_CHANGE Entity=(?<name>(?!GameEntity).*) tag=(?<tag>\\w+) value=(?<value>\\w+)$");
	private static Pattern _entityNameIngameRegex = Pattern.compile("\\[Zone\\] ZoneChangeList.* change=powerTask=\\[power=\\[type=TAG_CHANGE entity=\\[id=(?<id>\\d) cardId= name=(?<name>(?!GameEntity).*?)\\] tag=.*");
	private static Pattern gameState = Pattern.compile(".*TAG_CHANGE Entity=(?<name>.*) tag=PLAYSTATE value=WON");

	public Pattern _entityHeroRegex = Pattern.compile(".*\\[name=(?<hero>.*) id=.* player=(?<id>\\d).* zone from  -> FRIENDLY PLAY .{3,6}");
	private ShadyBotty b;
	public TestThread(ShadyBotty b) {
		this.b = b;
		try {
			File f = new File("lastgame.log");
			f.createNewFile();
			gamelog  = new PrintWriter("lastgame.log", "UTF-8");
			log = new FileReader("C:\\Program Files (x86)\\Hearthstone\\Hearthstone_Data\\output_log.txt");
		} catch (IOException e) {
			try {
				log = new FileReader("C:\\Program Files\\Hearthstone\\Hearthstone_Data\\output_log.txt");
			} catch (FileNotFoundException e1) {
				System.err.println("Could not find Log file for shady BibleThump");
			}
		}
	}
	ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
	private boolean found = false;
	private boolean scheduled = false;

	@Override
	public void run() {
		BufferedReader lnr = null;
		String str;
		if (log != null) {
			try{

				lnr = new BufferedReader(log);
				while ((str=lnr.readLine())!=null) {

				}

				// read lines till the end of the stream
				loopwhile:
					while(true)
					{
						checkIsOver();
						if ((str=lnr.readLine())!=null) {
							

							writeLogs(str);//write the entry to the temporary log file

							Matcher hasName = _entityNameRegex.matcher(str);
							Matcher champ = _entityHeroRegex.matcher(str);
							Matcher hasGameName = _entityNameIngameRegex.matcher(str);
							//check for name matches
							if (hasName.matches()) {
								checkEntity(hasName);
							} else if (champ.matches()) {
								checkHero(champ);
							} else if (hasGameName.matches()) {
								checkName(hasGameName);
							}


						} else {
							Thread.sleep(500);
						}
					} 
			}catch(Exception e){

				// if any error occurs
				e.printStackTrace();
			}
		}
	}



	private void checkName(Matcher name) {
		if (spectated == -1)
			return;
		int id = Integer.parseInt(name.group("id"))-1;
		if ((id == 1 && p1 == null) || (id == 1 && p1.equalsIgnoreCase("UNKNOWN HUMAN PLAYER"))) {
			p1 = name.group("name");
			System.err.println(name.group());
			System.err.println("p1 corrected:" +p1);
			if (spectated == 1) {
				lastname = p1;
				System.err.println("lastname corrected: " +p1);
			}
		} else if (id == 2 && (p2 == null || p2.equalsIgnoreCase("UNKNOWN HUMAN PLAYER"))) {
			p2 = name.group("name");
			System.err.println(name.group());
			System.err.println("p2 corrected:" +p2);
			if (spectated == 2) {
				lastname = p2;
				System.err.println("lastname corrected: " +p2);
			}
		}
	}



	private void checkHero(Matcher champ) {
		b.sendMessage(ShadyBottyMain.ROOM, "is le playing " + champ.group("hero"));
		int player = Integer.parseInt(champ.group("id"));
		spectated = player;
		
		lastname = spectated == 1 ? p1 != null ? p1 : lastname : p2 != null ? p2 : lastname;
		System.err.println("last: " + lastname + " id:" + spectated);
		switch (champ.group("hero")) {

		case "Valeera Sanguinar":
			hero2 = "Rogue";
			break;
		case "Anduin Wrynn":
			hero2 = "Priest";
			break;
		case "Thrall":
			hero2 = "Shaman";
			break;
		case "Garrosh Hellscream":
			hero2 = "Warrior";
			break;
		case "Gul'dan":
			hero2 = "Warlock";
			break;
		case "Uther Lightbringer":
			hero2 = "Paladin";
			break;
		case "Jaina Proudmoore":
			hero2 = "Mage";
			break;
		case "Rexxar":
			hero2 = "Hunter";
			break;
		case "Malfurion Stormrage":
			hero2 = "Druid";
			break;
		}
		//		System.out.println("here" + hero2);
		b.setTitle(wins+"-"+losses+ " "+ hero2 +".");

	}

	private void writeLogs(String str) {
		try{
			gamelog.println(str);
			gamelog.flush();
		} catch (Exception e) {
			System.err.println("error writing gamelog " + (new Date(System.currentTimeMillis())).toString());
		}
	}

	private void checkIsOver() {
		if (wins == 12 || losses == 3 && !scheduled ) {
			scheduled = true;
			timer.schedule(
					() -> {
						scheduled = false;
						if (wins == 12 || losses == 3) {
							ScoreSaver.addScore(wins,losses,hero2);
							reset();								
						}
					},
					240L, TimeUnit.SECONDS);
		}
	}

	private void checkEntity(Matcher entity) {
		
		if (entity.group("tag").equalsIgnoreCase("PLAYER_ID")) {
			if (entity.group("value").equals("1")) {
				
				p1 = entity.group("name");
				System.err.println("1=" + p1);
			} else {
				p2 = entity.group("name");
				System.err.println("2=" + p2);
			}
			if (p1 != null && p2 != null && !p1.equals(lastname) && lastname != null && !p2.equals(lastname) && !"UNKNOWN HUMAN PLAYER".equalsIgnoreCase(p1) && !"UNKNOWN HUMAN PLAYER".equalsIgnoreCase(p2)) {
				b.setTitle("0-0 Draft");
				System.err.println("reset");
				ScoreSaver.createDeck("");
				lastname = null;
				spectated = -1;
				wins = 0;
				losses = 0;
			}
		} else if (entity.group("tag").equalsIgnoreCase("PLAYSTATE") && entity.group("value").equalsIgnoreCase("WON")) {
			win = false;
			if (entity.group("name").equals(p1)) {
				if (spectated == 1) {
					wins++;
					win = true;
					Database.subgame(p1,true,wins,losses);
				}else {
					losses++;
					Database.subgame(p1,false,wins,losses);
				}
			} else if (entity.group("name").equals(p2)) {
				if (spectated == 2) {
					wins++;
					win = true;
					Database.subgame(p2,true,wins,losses);
				}else  {
					losses++;
					Database.subgame(p2,false,wins,losses);
				}
			} else 
				losses++;
			b.setTitle(wins+"-"+losses+ " "+ hero2 +".");
			System.err.println(entity.group("name") + " has won. p1=" + p1 + "  p2="+p2);
			System.err.println("score is now " + wins + "W " + losses + "L");
			found = false;
			spectated = -1;
			p1 = null;
			p2 = null;


			timer.schedule(() ->
			b.sendMessage(ShadyBottyMain.ROOM, "dided the guy shady spectates wonnered: " + (win ? "wonnered" : "lossded")), 
			22L, TimeUnit.SECONDS);
			checkIsOver();
		}
	}

	public static void main(String[] args) {
		Matcher a = _entityNameIngameRegex.matcher("[Zone] ZoneChangeList.ProcessChanges() - processing index=0 change=powerTask=[power=[type=TAG_CHANGE entity=[id=16 cardId= name=[id=16 cardId= type=INVALID zone=DECK zonePos=0 player=1]] tag=ZONE value=HAND] complete=False] entity=[id=16 cardId= type=INVALID zone=DECK zonePos=0 player=1] srcZoneTag=INVALID srcPos= dstZoneTag=HAND dstPos=");
		System.out.println(a.matches());
		System.out.println(a.group("name"));
		System.out.println(a.group("id"));

	}

	public void reset() {
		b.setTitle("0-0 Draft");
		ScoreSaver.createDeck("");
		lastname = null;
		p1 = null;
		p2 = null;
		spectated = -1;
		wins = 0;
		losses = 0;
	}
	
	/*
	 * 		if (p1 == null) {
			p1 = entity.group("name");
		} else if (p2 == null && !p1.equals(entity.group("name"))) {
			p2 = entity.group("name");
		} else if (!p1.equals(entity.group("name")) && !p2.equals(entity.group("name"))) {
			if (spectated == 1)
				p2 = entity.group("name");
			else if (spectated == 2)
				p1 = entity.group("name");
			else {
				p1 = null;
				p2 = null;
			}
		} 
		if (!found && lastname != null) {
			if (lastname.equals(p1)) {
				found = true;
				spectated =1;
			} else if (lastname.equals(p2)) {
				found = true;
				spectated =2;
			} 
		}
		if (!found && lastname != null && p1 != null && p2 != null && !lastname.equals(p1) && !lastname.equals(p2)) {
			lastname=null;
			wins = 0;
			losses = 0;
		}
	 */
}


