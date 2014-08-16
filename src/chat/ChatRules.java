package chat;

import main.ShadyBotty;

public class ChatRules {
	
	
	
	
	public ChatRules(){
		
	}
	
	//returs the amount of seconds the user should be timed out. If the result is -1, then no timeout should be performed.
	public int checkMessage(String nick, String message){
		int result = 0;
		if ((ShadyBotty.database.getPrivileges(nick).links() == -1 || checkLink(message) == -1){
			return -1;
		} 
		
		if (checkCaps(message) != -1){
			return checkCaps(s);
		} else if (checkEmoticons(s) != -1) {
			return checkEmoticons(s);
		}
		
		
		
		
	}
	
	private int checkEmoticons(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int checkLink(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int checkCaps(String s){
		return 0;
	}
	
	
}
