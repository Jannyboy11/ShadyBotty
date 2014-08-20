package chat;
import java.io.File;
import java.io.IOException;

import org.ini4j.*;

public class Privileges {
	
	private String nick;
	
	private int cooldown;
	private int links;
	private int capsFilter; // used for caps & emoticons. 
	private int emoFilter;
	private boolean subscriber;
	private int gain;
	
	private String faction;
	
	public enum Status{VIEWER, REGULAR, PREMIUM, DEMIMOD, MOD};
	private Status status;
	public Privileges(String n){
		nick = n;
		Wini ini;
		try {
			ini = new Wini(new File("users.ini"));
			links = ini.get(nick,"Link") == null ? 0 : -1;
			cooldown = ini.get(nick,"cooldown") == null ? 0 : -1;
			capsFilter = ini.get(nick,"Filter") == null ? 0 : -1;
			emoFilter = ini.get(nick,"Filter") == null ? 0 : -1;
			faction = ini.get(nick,"faction") == null ? "null" : ini.get(nick,"faction");
			if (ini.get(nick,"status") == null) {
			status = Status.VIEWER;
			ini.add(nick,"status","viewer");
			} else {
			status = Status.valueOf(ini.get(nick,"status").toUpperCase());
			}
			ini = new Wini(new File("currencies.ini"));
			gain = ini.get(nick,"gain") == null ? 0 : new Integer(ini.get(nick,"gain"));
				} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO get al the user privilges from the ini file and set the fields.
		//filters: read from ini file how many times the user spammed to much caps or too much emoticons. if the user has bought filters, set it to -1;
		//links: read from ini file how many times the users typed a link. if the user has bought links, set it to -1;
		//premium: read from inin file whether the user has premium.
		//gain: read from ini
		//sub: read from ini
		
		
	}

	
	public int getLinks(){
		return links;
	}
	
	public void setLinks(String i){
		
		links = Integer.getInteger(i);
	}
	
	
	
	public boolean isSubscriber(){
		return subscriber;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(String gain) {
		this.gain = Integer.getInteger(gain);
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

	public void setCapsFilter(String capsFilter) {
		this.capsFilter = Integer.getInteger(capsFilter);
	}

	public int getEmoFilter() {
		return emoFilter;
	}

	public void setEmoFilter(String emoFilter) {
		this.emoFilter = Integer.getInteger(emoFilter);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(String cooldown) {
		this.cooldown = Integer.getInteger(cooldown);
	}
	



}
