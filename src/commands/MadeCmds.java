package commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ini4j.Wini;

import chat.ChatRules;
import chat.Nicknames;
import main.Database;
import main.ShadyBotty;
import main.ShadyBottyMain;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import org.ini4j.Wini;
import org.ini4j.Profile.Section;

public class MadeCmds {

	/**
	 * Checks if a command already exists as a key in settings.ini commands section 
	 * @param command the Command to check for.
	 * @return  ({@link Util#getSettingsCommand(String) } != null)
	 * 
	 */
	public static boolean isCommand(String command) {
		return Util.getSettingsCommand(command.toLowerCase()) != null;
	}

	/**
	 * checks if a given user has an active command.
	 * <br> this means, check if his name is at the end of the value <br>
	 * of any command in the commands section in settings.ini <br>
	 * uses {@link #getCommandByUser(String)}
	 * @param nick the nick so search for
	 * @return ({@link #getCommandByUser(String)} != null)
	 */
	public static boolean hasCommand(String nick) {
		return getCommandByUser(nick) != null;
	}

	private static String getCommandList() {
		Section s = Util.GetSettingsSectionCommands();
		if (s == null || s.size() == 0)
			return "no commands!";
		String modCmds = "mod commands: ";
		String userCmds = "user made: ";
		System.out.println("CO MEON");
		for (Entry<String,String> cmd : s.entrySet()) {
			System.out.println(cmd.getKey());
			String name =Util.getCommandCreator(cmd.getKey());
			System.out.println(name);
			if (name.equals("mod"))
				modCmds += cmd.getKey() + ", ";
			else
				userCmds+= cmd.getKey() + " (" + name+ "), ";
		}
		System.out.println("WOOP"+ modCmds + userCmds);
		return modCmds.substring(0, modCmds.length()-2) + ". " + userCmds;
	}

	/**
	 * checks if a user has a command. if the command exists, <br>
	 * return the name (key) of that command.
	 * @param nick the user to check for
	 * @return the name of the command or <code> null </code> if none found
	 */
	public static String getCommandByUser(String nick) {
		Section s = Util.GetSettingsSectionCommands();
		if (s == null || s.size() == 0)
			return null;
		for (String key : s.keySet())
			if (nick.toLowerCase().equals(Util.getCommandCreator(key)) || (nick + "®").toLowerCase().equals(Util.getCommandCreator(key)))
				return key;
		return null;
	}


	/**
	 * a function that adds a command if it doesn't already exist. <br>
	 * this function is specifically created for mods. <br>
	 * it does not take points or check if the user already has a command.
	 * <br> uses {@link Util#saveSettingsCommandWithTime(String, String, String, long)}
	 * @param command the command to add(key)
	 * @param text the text for the command(part of value)
	 * @return a string directly usable in chat, containing info whether it was successful <br>
	 * or not and for what reason it was not if it failed.
	 */
	public static String addCommandMod(String command, String text) {
		if (isCommand(command))
			return "this command already exists";
		Util.saveSettingsCommandWithTime(command, text, "mod", 0L);
		return "you have succesfully created command: " + command;
	}

	/**
	 * gets the response of a given command, and only the response. <br>
	 * also checks if the command still exists and removes it if it has expired.
	 * @param command the command to return the text for
	 * @return the text (value) of the command(key), or "" if it doesn't exist anymore.
	 */
	public static String getCommand(String command) {
		String cmd = command.toLowerCase();
		System.out.println(cmd);
		checkCommand(cmd);
		System.out.println(isCommand(cmd));
		if (!isCommand(cmd))
			return "";

		return Util.getCommandResponse(cmd);
	}

	/**
	 * helper function. Used in {@link #getCommand(String)} <br>
	 * if a command exists, checks if it has expired. If it has, remove the command. <br>
	 * uses {@link Util#removeSettingsCommand(String)}
	 * @param command command to check expiration date for
	 */
	public static void checkCommand(String command) {
		String cmd = command.toLowerCase();
		if (!isCommand(cmd))
			return;
		if (System.currentTimeMillis() > Util.getCommandExpire(cmd) && !Util.getCommandCreator(cmd).equals("mod"))
			Util.removeSettingsCommand(cmd);
	}

	/**
	 * Changes a cmd with {@link Util#saveSettingsCommandWithTime(String, String, String,long)} <br>
	 * if, and only if the command  exists &amp;&amp; the user created the given command <br>
	 * &amp;&amp; the user hasn't changed it before. 
	 * @param command the command to change(key)
	 * @param text the text for the command(part of value)
	 * @param user the user who wants to change a command (part of value) <br>
	 * also used to check if user created command with {@link Util#getCommandCreator(String)}
	 * @return a string directly usable in chat, containing info whether it was successful <br>
	 * or not and for what reason it was not if it failed.
	 */
	public static String changeCommandNormal(String command, String text, String user) {
		String userCommand = getCommandByUser(user);
		if (userCommand == null)
			return user + " ,you don't have a command.";
		if (!userCommand.equals(command))
			return user + " ,this isn't your command";
		if (getCommand(command).equals(""))
			return user + " ,this command has expired";
		if (!Util.getCommandCreator(command).equals(user))
			return user + " ,you have already edited this command";
		Util.saveSettingsCommandWithTime(command, text, user + "®",Util.getCommandExpire(command));
		return user + " , command " + command + " successfully changed.";
	}

	/**
	 * a function that changes a command if it does already exist. <br>
	 * this function is specifically created for mods. <br>
	 * it does not check who owns the command.
	 * <br> uses {@link Util#saveSettingsCommandWithTime(String, String, String, long)} <br>
	 * preserves the original creator and expiration date if there was any
	 * @param command the command to change(key)
	 * @param text the text for the command(part of value)
	 * @return a string directly usable in chat, containing info whether it was successful <br>
	 * or not and for what reason it was not if it failed.
	 */
	public static String changeCommandMod(String command, String text) {
		if (getCommand(command).equals(""))
			return "this command has expired";
		Util.saveSettingsCommandWithTime(command, text, Util.getCommandCreator(command),Util.getCommandExpire(command));
		return "command " + command + " successfully changed";
	}

	/**
	 * a function specifically designed for mods. <br>
	 * checks if a command still exists and if it still exists <br>
	 * remove the command using {@link Util#removeSettingsCommand(String)}
	 *
	 * @param command the command to be deleted
	 * @return a string directly usable in chat, containing info whether it was successful <br>
	 * or not and for what reason it was not if it failed.
	 */
	public static String deleteCommandMod(String command) {
		System.out.println("in del");
		if (getCommand(command).equals(""))
			return "command doesn't exist";
		Util.removeSettingsCommand(command);
		return command + " has been deleted";
	}
	/**
	 * Gives a <code>String</code> as response to be shown as chat, if the given <code>message</code> from the user <br>
	 * is a valid command. <code>mod</code> is Used to check whether the user is mod or not.
	 * @param user The user who sent the message
	 * @param message the "chat message" that needs to be checked.
	 * @param mod <code>true</code> if the user is a mod. otherwise <code>false</code> 
	 * @return a response <code>String</code> if the <code>message</code> was a command. <br>
	 * otherwise <code>null</code>
	 */
	public static String chatToCommand(String user,String message, boolean mod) {
		String messageLower =message.toLowerCase();
		String[] msg = message.split(" ");

		if ((msg.length < 3 || !msg[0].equalsIgnoreCase("!cmd")))
			return null;
		String text = "";
		for (int i = 3; i < msg.length; i++)
			text += msg[i] + " ";
		System.out.println(mod);
		if (msg[2].startsWith("!")) {
			if (mod) {
				if (msg[1].equalsIgnoreCase("list"))
					return getCommandList();
				if (msg[1].equalsIgnoreCase("add"))
					return addCommandMod(msg[2],text);
				if (msg[1].equalsIgnoreCase("edit"))
					return changeCommandMod(msg[2],text);
				if (msg[1].equalsIgnoreCase("remove")) {
					return deleteCommandMod(msg[2]);
				}

			}
		}
		return null;
	}



	public static void main(String[] args) {



		System.out.println(System.currentTimeMillis());
		System.out.println(hasCommand("jb940"));
		System.out.println(isCommand("marco"));
		System.out.println(getCommand("!testcommand2"));
		System.out.println(Util.getSettingsCommand("!testcommand2"));
		System.out.println(changeCommandNormal("!testcommand2","response2","jb9401"));
	}
}
