package chat;

import main.ShadyBotty;

public class ChatRules {

	public static String[] emoticons = {":)",":P",":D",":O","o_o",":(","4Head","ArsonNoSexy","AsianGlow","BCWarrior","BORT","BatChest","BibleThump","BigBrother","BionicBunion","BlargNaut","BloodTrail","BrainSlug","BrokeBack","CougarHunt","DAESuppy","DBstyle","DansGame","DatSheffy","DogFace","EagleEye","EleGiggle","EvilFetus","FPSMarksman","FUNgineer","FailFish","FrankerZ","FreakinStinkin","FuzzyOtterOO","GingerPower","GrammarKing","HassanChop","HotPokket","ItsBoshyTime","JKanStyle","Jebaited","JonCarnage","KZassault","KZcover","KZguerilla","KZhelghast","KZowl","KZskull","Kappa","Keepo","KevinTurtle","Kippa","Kreygasm","MVGame","MrDestructoid","NinjaTroll","NoNoSpot","OMGScoots","OneHand","OpieOP","OptimizePrime","PJSalt","PMSTwin","PanicVis","PazPazowitz","PeoplesChamp","PicoMause","PipeHype","PogChamp","Poooound","PunchTrees","RalpherZ","RedCoat","ResidentSleeper","RitzMitz","RuleFive","SMOrc","SMSkull","SSSsss","ShazBotstix","SoBayed","SoonerLater","StoneLightning","StrawBeary","SuperVinlin","SwiftRage","TF2John","TehFunrun","TheRinger","TheTarFu","TheThing","ThunBeast","TinyFace","TooSpicy","TriHard","UleetBackup","UnSane","UncleNox","Volcania","WTRuck","WholeWheat","WinWaker","YouWHY","aneleanele"};

	public ChatRules(){

	}

	//returns the amount of seconds the user should be timed out. If the result is -1, then no timeout should be performed.
	public Pair checkMessage(String nick, String message){
		
		//TODO make botty send warning message when a user violates the chat rules.		
		int result = 0;
		String reason = "";
		if (checkLink(nick, message) != -1){
			result += checkLink(nick, message);
			
			reason += "Posted Link";
		}
		
		if (checkEmoticons(nick, message) != -1) {
			result += checkEmoticons(nick, message);
			reason += (reason == "") ? "Too many Emoticons" : " , too many Emoticons";
		}
		
		if (checkCaps(nick, message) != -1){
			result += checkCaps(nick, message);
			reason += (reason == "") ? "Too much Caps" : " , too much Caps";
		}
		
		return new Pair(result, reason);
	}

	private int checkEmoticons(String nick, String s) {
		int emocounter = 0;
		int total = 0;
		for (String sub : getWords(s)){
			total++;
			for(String emo : emoticons){
				if (emo.contains(sub)){
					emocounter++;
				}
			}
		}
		if ((emocounter > 4 && total < 10) || emocounter > 7){ 
			//emo's just too annoying. pls keep low. no annoying methz
			int filters = ShadyBotty.database.getPrivileges(nick).getFilters();	
			if (filters == -1) return -1;
			ShadyBotty.database.getPrivileges(nick).setFilters(++filters); //adds one point to the warningcounter
			if (filters == 0) return 2;
			if (filters == 1) return 30;
			if (filters == 2) return 60;
			if (filters == 3) return 240;
			if (filters == 4) return 400;			
			return 600;
			//TODO create a thread in the ShadyBotty.java that decreases the value of filters above 0 for all users every 10 minutes. also for links Kappa.
		} else {
			// not too many emoticons;
			return 0;

		}
	}

	private int checkLink(String nick, String message) {
		int links = ShadyBotty.database.getPrivileges(nick).getLinks();
		if (links == -1) return -1;
		
		for (String word : getWords(message)){
			int length = word.length();
			boolean isLink = false;
			if (word.startsWith("www.") || word.startsWith("http://") || word.startsWith("https://")){
				//the word is obviously a url
				ShadyBotty.database.getPrivileges(nick).setLinks(++links);
				isLink = true;
			} else if (word.charAt(length - 4) == '.' || word.charAt(length - 3 ) == '.' || word.charAt(length - 5) == '.'){
				//the word is probably a link or emailaddress
				ShadyBotty.database.getPrivileges(nick).setLinks(++links);
				isLink = true;
			}
			if (isLink){
				if (links == 0) return 120;
				if (links == 1) return 240;
				if (links == 2) return 400;
				if (links == 3) return 600;
				return 1200;
			}
		}
		// all words weren't links
		return 0;
	}

	public int checkCaps(String nick, String message){
		int filters = ShadyBotty.database.getPrivileges(nick).getFilters();
		if (filters == -1) return -1;
		
		if (message.length() > 9 && countUppercase(message)/message.length() > 0.6){
			//user spammed to many caps
			ShadyBotty.database.getPrivileges(nick).setFilters(++filters);
			if (filters == 0) return 2;
			if (filters == 1) return 30;
			if (filters == 2) return 60;
			if (filters == 3) return 240;
			if (filters == 4) return 400;
			return 600;			
		}
		//not too much caps Kappa
		return 0;		
	}
	
	
	//some useful utility methods, maybe create some utility class for these?
	
	public String[] getWords(String s){
		return s.split(" ");
	}
	
	public int countUppercase(String s){
		int result = 0;
		for (int i = 0; i < s.length(); i++){
			if (Character.isUpperCase(s.charAt(i))) result++;
		}
		return result;
	}
}
