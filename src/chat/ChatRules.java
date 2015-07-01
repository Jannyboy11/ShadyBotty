package chat;

import java.util.regex.Pattern;

import chat.Privileges.Status;
import main.ShadyBotty;

public class ChatRules {
	private static Pattern url = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
	public static String[] emoticons = {":)",":P",":D",":O","o_o",":(","4Head","ArsonNoSexy","AsianGlow","BCWarrior","BORT","BatChest","BibleThump","BigBrother","BionicBunion","BlargNaut","BloodTrail","BrainSlug","BrokeBack","CougarHunt","DAESuppy","DBstyle","DansGame","DatSheffy","DogFace","EagleEye","EleGiggle","EvilFetus","FPSMarksman","FUNgineer","FailFish","FrankerZ","FreakinStinkin","FuzzyOtterOO","GingerPower","GrammarKing","HassanChop","HotPokket","ItsBoshyTime","JKanStyle","Jebaited","JonCarnage","KZassault","KZcover","KZguerilla","KZhelghast","KZowl","KZskull","Kappa","Keepo","KevinTurtle","Kippa","Kreygasm","MVGame","MrDestructoid","NinjaTroll","NoNoSpot","OMGScoots","OneHand","OpieOP","OptimizePrime","PJSalt","PMSTwin","PanicVis","PazPazowitz","PeoplesChamp","PicoMause","PipeHype","PogChamp","Poooound","PunchTrees","RalpherZ","RedCoat","ResidentSleeper","RitzMitz","RuleFive","SMOrc","SMSkull","SSSsss","ShazBotstix","SoBayed","SoonerLater","StoneLightning","StrawBeary","SuperVinlin","SwiftRage","TF2John","TehFunrun","TheRinger","TheTarFu","TheThing","ThunBeast","TinyFace","TooSpicy","TriHard","UleetBackup","UnSane","UncleNox","Volcania","WTRuck","WholeWheat","WinWaker","YouWHY","aneleanele"};
	ShadyBotty botty;
	public ChatRules(ShadyBotty bot){
		botty = bot;
	}

	//returns the amount of seconds the user should be timed out. If the result is -1, then no timeout should be performed.
	public static Pair checkMessage(String nick, String message){
		int a;
		//TODO make botty send warning message when a user violates the chat rules.		
		int result = 0;
		String reason = "";
		if(ShadyBotty.database.getPrivileges(nick).getStatus() == Status.VIEWER 
				|| ShadyBotty.database.getPrivileges(nick).getStatus() == Status.REGULAR) {
			if ((a = checkLink(nick, message)) != 0){
				result += a;

				reason += "Posted Link";
			}


			if ((a = checkEmoticons(nick, message)) != 0) {
				result += a;
				reason += (reason.equals("")) ? "Too many Emoticons" : " , too many Emoticons";
			}


			if ((a = checkCaps(nick, message)) != 0){
				result += a;
				reason += (reason.equals("")) ? "Too much Caps" : " , too much Caps";
			}
		}

		return new Pair(result, reason);
	}

	private static int checkEmoticons(String nick, String s) {
		int filters = (ShadyBotty.database.getPrivileges(nick) != null) ? ShadyBotty.database.getPrivileges(nick).getEmoFilter() : 0;
		if (filters == -1) return 0;
		int emocounter = 0;
		int total = 0;
		for (String sub : getWords(s)){
			total++;
			for(String emo : emoticons){
				if (sub.contains(emo)){
					emocounter++;
				}
			}
		}
		if ((emocounter > 6 && total < 10) || emocounter > 9){ 
			//emo's just too annoying. pls keep low. no annoying methz

			ShadyBotty.database.getPrivileges(nick).setEmoFilter(""+ (filters + 1)); //adds one point to the warningcounter
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

	private static int checkLink(String nick, String message) {
		int links = (ShadyBotty.database.getPrivileges(nick).getLinks());
		if (links == -1 || ShadyBotty.database.getPrivileges(nick).isTemplink()) {
			ShadyBotty.database.getPrivileges(nick).setTemplink(false);
			return 0;
		}

		for (String word : getWords(message)){
//			if (url.matcher(word).matches())
//				System.out.println(word);
			int length = word.length();
			boolean isLink = false;
			if (word.startsWith("www.") || word.startsWith("http://") || word.startsWith("https://")){
				//the word is obviously a url
				ShadyBotty.database.getPrivileges(nick).setLinks("" +links + 1);
				isLink = true;
			} else if (length > 5) {					
				for (int i = 0; i < length-4; i++) {

					if ((word.charAt(i) == '.' && Character.isLetter(word.charAt(i +1)) && Character.isLetter(word.charAt(i +2)) && Character.isLetter(word.charAt(i+3))) || (word.charAt(i+1) == '.' && Character.isLetter(word.charAt(i+2)) && Character.isLetter(word.charAt(i+3)))) {
						//the word is probably a link or emailaddress
						if (word.contains("..")) return 0;
						ShadyBotty.database.getPrivileges(nick).setLinks("" +links + 1);
						isLink = true;
					}
				}
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

	public static int checkCaps(String nick, String message){
		int filters = (ShadyBotty.database.getPrivileges(nick) != null) ? ShadyBotty.database.getPrivileges(nick).getCapsFilter() : 0;
		if (filters == -1) return 0;

		String messageWithoutEmoticons = removeEmoticons(message);

		if (messageWithoutEmoticons.length() > 25 && (double)countUppercase(messageWithoutEmoticons)/(double)messageWithoutEmoticons.length() > 0.7){
			//user spammed to many caps
			ShadyBotty.database.getPrivileges(nick).setCapsFilter("" +filters + 1);
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

	public static String[] getWords(String s){
		return s.split(" ");
	}

	public static int countUppercase(String s){
		int result = 0;
		for (int i = 0; i < s.length(); i++){
			if (Character.isUpperCase(s.charAt(i))) result++;
		}
		return result;
	}

	public static String removeEmoticons(String s){
		String result = "";
		for (String word : getWords(s)){
			boolean in = false;
			for (String emo : emoticons){
				if ((word.contains(emo))){
					in = true;
				}
			}
			if (!in)
				result += word + " ";
		}
		return result;
	}
}
