package main;

import java.io.BufferedReader;
import java.io.FileReader;

import org.jibble.pircbot.PircBot;

import points.Chips;
import points.Points;
import chat.Nicknames;

public class PrivateBotty extends PircBot {

	public PrivateBotty() {
		this.setName("ShadyBotty");
	}

	public void onDisconnect() {
		while (!isConnected()) {
			try {
				Thread.sleep(5000);
			} catch(Exception ex) {
			}
			try {
				// Connect to the IRC server.
				BufferedReader blub = new BufferedReader(new FileReader("C:/wachtwoord.txt"));
				String password = blub.readLine();
				blub.close();
				this.connect("199.9.248.232",80,password);

				// Join the #pircbot channel.
				this.joinChannel("#_shadybottyroom");

			} catch(Exception ex) {
			}
		}
	}

	public void onMessage(String channel, String sender,
			String login, String hostname, String message) {
		Nicknames.addNick(sender);
		ShadyBotty.database.addPrivileges(sender);
		if (message.toLowerCase().startsWith("!points")) {
			String toSend = Nicknames.getNick(sender) + " has " + Math.round(Points.getPoints(sender.toLowerCase())) + " points";
			if (Chips.getChips(sender) != 0)
				toSend += " and " + Math.round(Chips.getChips(sender.toLowerCase())) + " chips";
			toSend+= ". ";
			if (ShadyBotty.database.getPrivileges(sender.toLowerCase()).getFaction().equalsIgnoreCase("jb940")) 
				toSend += Nicknames.getNick(sender) + " also has 1 GodPoint! Kappa/";	
			System.out.println(toSend);
			this.sendMessage("#_shadybottyroom",toSend);
		}
				
	}
}
