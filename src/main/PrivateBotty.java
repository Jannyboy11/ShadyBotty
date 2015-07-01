package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import points.Chips;
import points.Points;
import chat.Nicknames;

public class PrivateBotty extends PircBot {

	public PrivateBotty() {
		this.setName("ShadyBotty");
		
		sendPriv("jb940","KappaPride");
	}
	
	public void sendPriv(String user, String message) {
		sendMessage("#jtv",".w " +user + " " + message);
	}
	
	@Override
	protected void onUnknown(String line) {
		if (isWhisper(line))
			onWhisper(line);
	}
	
	private void onWhisper(String line) {
	System.out.println("WHISP" + line);
	}

	private boolean isWhisper(String line) {
		return line.split(" ")[1].equals("WHISPER");
	}

	public static void main(String[] args) {
		PrivateBotty a = new PrivateBotty();
		a.setVerbose(true);
		try {
			a.connect("199.9.253.119",6667, "oauth:n6yzqpe8uqqd7qh8fgqh0f1yyb6lq1");
		} catch (IOException | IrcException e) {
			// TODOHIGH Auto-generated catch block
			e.printStackTrace();
		}
		a.joinChannel("#jtv");
		a.sendRawLine("CAP REQ :twitch.tv/commands");
		
	}
}
