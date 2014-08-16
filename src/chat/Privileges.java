package chat;

public class Privileges {
	
	private String nick;
	
	private int links;
	private int filters; // used for caps & emoticons. 
	private boolean premium;
	private boolean subscriber;
	private int gain;
	
	
	public Privileges(String n){
		nick = n;
		//TODO get al the user privilges from the ini file and set the fields.
		//filters: read from ini file how many times the user spammed to much caps or too much emoticons. if the user has bought filters, set it to -1;
		//links: read from ini file how many times the users typed a link. if the user has bought links, set it to -1;
		//premium: read from inin file whether the user has premium.
		gain = 0;
		//gain: read from ini
		//sub: read from ini
		
		
	}
	
	public int links(){
		return links;
	}
	
	public int filters(){
		return filters;		
	}
	
	public boolean hasPremium(){
		return premium;
	}
	
	public boolean isSubscriber(){
		return subscriber;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(int gain) {
		this.gain = gain;
		//todo: WRITEINI
	}


}
