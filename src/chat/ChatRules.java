package chat;

import main.ShadyBotty;

public class ChatRules {

	public static String[] emoticons = {":)",":P",":D",":O","o_o",":(","4Head","ArsonNoSexy","AsianGlow","BCWarrior","BORT","BatChest","BibleThump","BigBrother","BionicBunion","BlargNaut","BloodTrail","BrainSlug","BrokeBack","CougarHunt","DAESuppy","DBstyle","DansGame","DatSheffy","DogFace","EagleEye","EleGiggle","EvilFetus","FPSMarksman","FUNgineer","FailFish","FrankerZ","FreakinStinkin","FuzzyOtterOO","GingerPower","GrammarKing","HassanChop","HotPokket","ItsBoshyTime","JKanStyle","Jebaited","JonCarnage","KZassault","KZcover","KZguerilla","KZhelghast","KZowl","KZskull","Kappa","Keepo","KevinTurtle","Kippa","Kreygasm","MVGame","MrDestructoid","NinjaTroll","NoNoSpot","OMGScoots","OneHand","OpieOP","OptimizePrime","PJSalt","PMSTwin","PanicVis","PazPazowitz","PeoplesChamp","PicoMause","PipeHype","PogChamp","Poooound","PunchTrees","RalpherZ","RedCoat","ResidentSleeper","RitzMitz","RuleFive","SMOrc","SMSkull","SSSsss","ShazBotstix","SoBayed","SoonerLater","StoneLightning","StrawBeary","SuperVinlin","SwiftRage","TF2John","TehFunrun","TheRinger","TheTarFu","TheThing","ThunBeast","TinyFace","TooSpicy","TriHard","UleetBackup","UnSane","UncleNox","Volcania","WTRuck","WholeWheat","WinWaker","YouWHY","aneleanele"};
	ShadyBotty botty;
	public ChatRules(ShadyBotty bot){
		botty = bot;
	}

	//returns the amount of seconds the user should be timed out. If the result is -1, then no timeout should be performed.
	public Pair checkMessage(String nick, String message){
		int a;
		//TODO make botty send warning message when a user violates the chat rules.		
		int result = 0;
		String reason = "";
		System.out.println("checking");
		if ((a = checkLink(nick, message)) != 0){
			result += a;

			reason += "Posted Link";
		}

		System.out.println("checked links " + reason);

		if ((a = checkEmoticons(nick, message)) != 0) {
			result += a;
			reason += (reason.equals("")) ? "Too many Emoticons" : " , too many Emoticons";
		}

		System.out.println("checked emotes " + reason);

		if ((a = checkCaps(nick, message)) != 0){
			result += a;
			reason += (reason.equals("")) ? "Too much Caps" : " , too much Caps";
		}

		System.out.println("checked caps " + reason);

		return new Pair(result, reason);
	}

	private int checkEmoticons(String nick, String s) {
		int filters = ShadyBotty.database.getPrivileges(nick).getFilters();	
		if (filters == -1) return 0;
		int emocounter = 0;
		int total = 0;
		for (String sub : getWords(s)){
			total++;
			for(String emo : emoticons){
				if (sub.contains(emo)){
					System.out.println(emo + " " + sub);
					emocounter++;
				}
			}
		}
		System.out.println(emocounter +"<emo total> " + total);
		if ((emocounter > 4 && total < 15) || emocounter > 6){ 
			//emo's just too annoying. pls keep low. no annoying methz

			ShadyBotty.database.getPrivileges(nick).setFilters(filters + 1); //adds one point to the warningcounter
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
		if (links == -1) return 0;

		for (String word : getWords(message)){
			int length = word.length();
			boolean isLink = false;
			if (word.startsWith("www.") || word.startsWith("http://") || word.startsWith("https://")){
				//the word is obviously a url
				ShadyBotty.database.getPrivileges(nick).setLinks(links + 1);
				isLink = true;
			} else if ((length > 5 && (word.charAt(length - 4) == '.' || word.charAt(length - 3 ) == '.' || word.charAt(length - 5) == '.'))){
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
		if (filters == -1) return 0;
		
		String messageWithoutEmoticons = removeEmoticons(message);
		
		if (messageWithoutEmoticons.length() > 9 && (double)countUppercase(messageWithoutEmoticons)/(double)messageWithoutEmoticons.length() > 0.6){
			//user spammed to many caps
			ShadyBotty.database.getPrivileges(nick).setFilters(filters + 1);
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
	
	public String removeEmoticons(String s){
		String result = "";
		for (String word : getWords(s)){
			for (String emo : emoticons){
				if (!(word.contains(emo))){
					result += word = " ";
				}
			}
		}
		System.out.println(result);
		return result;
	}
}
