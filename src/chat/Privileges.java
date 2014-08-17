package chat;

public class Privileges {
	
	private String nick;
	
	private int links;
	private int capsFilter; // used for caps & emoticons. 
	private int emoFilter;
	private boolean premium;
	private boolean subscriber;
	private int gain;
	private String faction;
	
	private enum Status{VIEWER, REGULAR, PREMIUM, DEMIMOD, MOD};
	
	public Privileges(String n){
		nick = n;
		setCapsFilter(0);
		setLinks(0);
		setEmoFilter(0);
		//TODO get al the user privilges from the ini file and set the fields.
		//filters: read from ini file how many times the user spammed to much caps or too much emoticons. if the user has bought filters, set it to -1;
		//links: read from ini file how many times the users typed a link. if the user has bought links, set it to -1;
		//premium: read from inin file whether the user has premium.
		gain = 0;
		//gain: read from ini
		//sub: read from ini
		
		
	}
	
	public int getLinks(){
		return links;
	}
	
	public void setLinks(int i){
		links = i;
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
		//TODO: WRITEINI
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String f) {
		faction = f;
	}

	public int getCapsFilter() {
		return capsFilter;
	}

	public void setCapsFilter(int capsFilter) {
		this.capsFilter = capsFilter;
	}

	public int getEmoFilter() {
		return emoFilter;
	}

	public void setEmoFilter(int emoFilter) {
		this.emoFilter = emoFilter;
	}


}
