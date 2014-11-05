package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ini4j.Wini;

import chat.ChatRules;
import chat.Nicknames;
import main.Database;
import main.ShadyBotty;
import main.ShadyBottyMain;

public class MadeCmds {
	private static ShadyBotty botty;
	
	public MadeCmds(ShadyBotty bot) {
		botty = bot;
	}
	
	public static boolean sendCmd(String cmd2,String sender,String ch) {
		Wini cmds = Database.autoreplyIni;
		String cmd = ChatRules.getWords(cmd2)[0];
		if (cmds.get("command", cmd) != null) {
			botty.sendMessage(ch, cmds.get("command", cmd).replaceAll("\\*nick\\*", Nicknames.getNick(sender)));
			return true;
		}
		return false;
	}
}
