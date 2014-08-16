package chat;

import main.ShadyBotty;

public class ChatRules {
	
	public static String[] emoticons = {":)",":P",":D",":O","o_o",":(","4Head","ArsonNoSexy","AsianGlow","BCWarrior","BORT","BatChest","BibleThump","BigBrother","BionicBunion","BlargNaut","BloodTrail","BrainSlug","BrokeBack","CougarHunt","DAESuppy","DBstyle","DansGame","DatSheffy","DogFace","EagleEye","EleGiggle","EvilFetus","FPSMarksman","FUNgineer","FailFish","FrankerZ","FreakinStinkin","FuzzyOtterOO","GingerPower","GrammarKing","HassanChop","HotPokket","ItsBoshyTime","JKanStyle","Jebaited","JonCarnage","KZassault","KZcover","KZguerilla","KZhelghast","KZowl","KZskull","Kappa","Keepo","KevinTurtle","Kippa","Kreygasm","MVGame","MrDestructoid","NinjaTroll","NoNoSpot","OMGScoots","OneHand","OpieOP","OptimizePrime","PJSalt","PMSTwin","PanicVis","PazPazowitz","PeoplesChamp","PicoMause","PipeHype","PogChamp","Poooound","PunchTrees","RalpherZ","RedCoat","ResidentSleeper","RitzMitz","RuleFive","SMOrc","SMSkull","SSSsss","ShazBotstix","SoBayed","SoonerLater","StoneLightning","StrawBeary","SuperVinlin","SwiftRage","TF2John","TehFunrun","TheRinger","TheTarFu","TheThing","ThunBeast","TinyFace","TooSpicy","TriHard","UleetBackup","UnSane","UncleNox","Volcania","WTRuck","WholeWheat","WinWaker","YouWHY","aneleanele"};
		
	public ChatRules(){
				
	}
	
	//returs the amount of seconds the user should be timed out. If the result is -1, then no timeout should be performed.
	public int checkMessage(String nick, String message){
		int result = 0;
		if ((ShadyBotty.database.getPrivileges(nick).links() == -1 || checkLink(message) == -1)){
			return -1;
		} 
		
		if (checkCaps(message) != -1){
			return checkCaps(s);
		} else if (checkEmoticons(s) != -1) {
			return checkEmoticons(s);
		}
		
		
		
		
	}
	
	private int checkEmoticons(String s) {
		for (String sub : getWords(s)){
			for (){
				if(){
					
				}
			}
		}
		return 0;
	}

	private int checkLink(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int checkCaps(String s){
		return 0;
	}
	
	public String[] getWords(String s){
		return s.split(" ");
	}
	
	
}
