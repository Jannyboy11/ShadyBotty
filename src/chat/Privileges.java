package chat;
import java.io.IOException;
import main.Database;
import org.ini4j.*;

public class Privileges {
	
	private String nick;
	
	private int cooldown;
	private int links;
	private int capsFilter; // used for caps & emoticons. 
	private int emoFilter;
	private boolean subscriber;
	private int gain;
	private boolean templink;
	private String faction;
	private String[] emotesets = new String[] { };
	public String chatcolor;
	
	public enum Status{VIEWER, REGULAR, PREMIUM, DEMIMOD, MOD};
	private Status status;
	public Privileges(String n){
		nick = n;
		Wini ini = Database.getUsers();
			templink = false;

			links = ini.get(nick,"Links") == null ? 0 : -1;
			cooldown = ini.get(nick,"Cooldown") == null ? 0 : -1;
			capsFilter = ini.get(nick,"Filter") == null ? 0 : -1;
			emoFilter = ini.get(nick,"Filter") == null ? 0 : -1;
			faction = ini.get(nick,"Faction") == null ? "null" : ini.get(nick,"Faction");
			subscriber = "true".equalsIgnoreCase(ini.get(nick,"Subscriber")) ? true : false;
			if (ini.get(nick,"Status") == null) {
			status = Status.VIEWER;
			ini.add(nick,"Status","viewer");
			try {
				ini.store();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} else {
			status = Status.valueOf(ini.get(nick,"Status").toUpperCase());
			}
			ini = Database.getCurrencies();
			gain = ini.get(nick,"Gain") == null ? 0 : new Integer(ini.get(nick,"Gain"));


		
		
	}

	
	public int getLinks(){
		return links;
	}
	
	public void setLinks(String i){
		
		links = Integer.parseInt(i);
	}
	
	
	
	public boolean isSubscriber(){
		return subscriber;
	}

	public int getGain() {
		return gain;
	}

	public void setGain(String gain) {
		this.gain = Integer.parseInt(gain);
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
		this.capsFilter = Integer.parseInt(capsFilter);
	}

	public int getEmoFilter() {
		return emoFilter;
	}

	public void setEmoFilter(String emoFilter) {
		this.emoFilter = Integer.parseInt(emoFilter);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status =  Status.valueOf(status.toUpperCase());
	}
	
	public void setStatus(Status status) {
		this.status =  status;
	}
	
	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(String cooldown) {
//		System.out.println(cooldown + "<string .. Int in priv>" + this.cooldown);
		this.cooldown = Integer.parseInt(cooldown);
	}




	public boolean isTemplink() {
		return templink;
	}

	public boolean equalEmoteSetSize(int length){
		return emotesets.length ==length;
	}
	
	public String[] getEmotesets() {
		return emotesets;
	}
	
	public void setEmotesets(String[] emotesets) {
		this.emotesets = emotesets;
	}
	
	public void setSubscriber(boolean subscriber) {
		this.subscriber = subscriber;
	}

	
	public void setTemplink(boolean remplink) {
		this.templink = remplink;
	}
	

	public static void main(String[] args) {
		Privileges a = new Privileges("jb940");
		System.out.println(a.getStatus());
		a.setStatus("regular");
		System.out.println(a.getStatus());
	}

}
