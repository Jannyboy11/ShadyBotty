package commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import main.Database;
import org.ini4j.Profile.Section;
import org.ini4j.Wini;



public class Util{


	public static Wini ini = Database.getAutoreplies();

	
	/**
	 *  Utility function created to make code look better. <br>
	 *  calls the <code> Thread.sleep(sleep) </code> function <br>
	 *  without the need of the try catch
	 * @param sleep amount of milliseconds to sleep 1000 ms = 1s
	 * @return returns true if sleep was successful, false if interrupted.
	 */
	public static boolean sleepMs(long sleep) {
		try {Thread.sleep(sleep);} catch (InterruptedException e) {return false;}
		return true;
	}
	/**
	 *  Utility function created to make code look better. <br>
	 *  calls the <code> Thread.sleep(sleep) </code> function <br>
	 *  without the need of the try catch. 
	 * @param sleep amount of  SECONDS to sleep
	 * @return returns true if sleep was successful, false if interrupted.
	 */
	public static boolean sleepS(long sleep) {
		try {Thread.sleep(sleep*1000);} catch (InterruptedException e) {return false;}
		return true;
	}

	public static void storeSettings() {
		try {
			ini.store();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *  Saves the given value mapped on key <br>
	 *  in the given section. The file saved to is settings.ini
	 * @param section the section where the key belongs
	 * @param key the name of the key
	 * @param value the value to map to the key
	 */
	public static void saveToSettings(String section,String key,Object value) {
		ini.put(section.toLowerCase(), key.toLowerCase(),value);
		storeSettings();
	}

	/**
	 *  removes the given key from the given section<br>
	 *The file saved to is settings.ini
	 * @param section the section where the key belongs
	 * @param key the name of the key
	 */
	public static void RemoveFromSettings(String section,String key) {
		ini.remove(section.toLowerCase(), key.toLowerCase());
		storeSettings();
	}


	/**
	 * Returns the value of the section/key combo <br>
	 * @param section the section in the settings.ini to read from
	 * @param key the key in the section to read from
	 * @return the String value mapped to the key or <br>
	 *  <code> null </code> if it doesn't exist
	 */
	public static String getSettings(String section,String key) {
		return ini.get(section.toLowerCase(), key.toLowerCase());


	}

	/**
	 * Returns the section with the name corresponding to <code> String section </code><br>
	 * @param section the section in the settings.ini to return
	 * @return the Section with all elements inside or <br>
	 *  <code> null </code> if it doesn't exist
	 */
	public static Section getSettingsSection(String section) {
		return ini.get(section);

	}

	/**
	 *  Saves the given points amount mapped on the name key <br>
	 *  to the points section. The file saved to is settings.ini
	 * @param name the name of the person
	 * @param amount the points of the person
	 */
	public static void saveSettingsPoints(String name, int amount) {
		saveToSettings("points",name,amount);
	}

	/**
	 *  Saves the given text mapped on the name settting<br>
	 *  to the displaymessages section. The file saved to is settings.ini
	 * @param setting the name of  the setting to change
	 * @param text the text of the setting to change
	 */
	public static void saveSettingsSetting(String setting, Object text) {
		saveToSettings("displaymessages",setting,text);
	}


	/**
	 *  gets the given setting<br>
	 *  from the settings.ini displaymessages section.
	 * @param setting the name of  the setting to get
	 * @return the String of the setting, if empty returns null.
	 */
	public static String getSettingsSetting(String setting) {
		return getSettings("displaymessages",setting);
	}

	/**
	 * Returns the points of a specific user from settings.ini points section
	 * @param key the name of the person
	 * @return The string points converted to an integer if it exists <br>
	 * or <code> 0 </code> if it doesn't exist
	 */
	public static int getSettingsPoints(String key) {
		int result;
		try {
			result =Integer.parseInt(ini.get("points", key.toLowerCase()));
		} catch (Exception e) {
			result  = 0;
		}

		return result;

	}


	/**
	 *  gets the section names "points" from settings.ini
	 * @return the section
	 */
	public static Section GetSettingsSectionPoints() {
		return getSettingsSection("points");

	}

	/**
	 *  Saves the given <code>command</code> as  and <code>text</code>-__-\\+_<code>creator</code> as value <br>
	 *  to the commands section. The file saved to is settings.ini
	 * @param command the chat command to save as key
	 * @param text the text that should be replied
	 * @param creator the creator of the text, appended after text with a -__-\\+_ in between
	 */
	public static void saveSettingsCommand(String command, String text,String creator) {


		saveToSettings("commands",command,text + "-__-+_" + creator + "-__-+_" + (System.currentTimeMillis() + 604800000));
	}

	/**
	 *  Saves the given <code>command</code> as  and <code>text</code>-__-\\+_<code>creator</code> as value <br>
	 *  to the commands section. The file saved to is settings.ini
	 * @param command the chat command to save as key
	 * @param text the text that should be replied
	 * @param creator the creator of the text, appended after text with a -__-\\+_ in between
	 * @param time the time to save
	 */
	public static void saveSettingsCommandWithTime(String command, String text,String creator,long time) {


		saveToSettings("commands",command,text + "-__-+_" + creator + "-__-+_" + time +"-__-+_0");
	}

	/**
	 * Removes the given command from the commands section in settings.ini
	 * @param command the command to remove
	 */
	public static void removeSettingsCommand(String command) {


		RemoveFromSettings("commands",command);
	}

	/**
	 * returns the value of the given command in the commands section of settings.ini
	 * @param command the command to be requested
	 * @return the command string including creator <br>
	 * or <code> null </code> if it doesn't exist
	 */
	public static String getSettingsCommand(String command) {
		return getSettings("commands",command);
	}
	/**
	 * returns the text response of a given command, without the creator
	 * @param command the command to get
	 * @return a text response of a command<br>
	 * or <code> null </code> if it doesn't exist
	 */
	public static String getCommandResponse(String command) {
		return getSettingsCommand(command).split("-__-\\+_")[0];
	}
	
	/**
	 * returns the last time a command was used
	 * @param command the command to get
	 * @return a Long<br>
	 * or <code> null </code> if it doesn't exist
	 */
	public static long getCommandLastUsed(String command) {
		long result =getSettingsCommand(command).split("-__-\\+_").length < 4 ? 0L : Long.parseLong(getSettingsCommand(command).split("-__-\\+_")[3]);
		return result;
	}
	
	/**
	 * sets the last time a command was used
	 * @param command the command to set
	 */
	public static void setCommandLastUsed(String command) {
		String[] temp = getSettingsCommand(command).split("-__-\\+_");
		String result = temp[0] + "-__-+_" + temp[1] + "-__-+_" + temp[2] + "-__-+_" + System.currentTimeMillis();
		ini.put("commands", command,result);
	}

	/**
	 * Returns the expiration time in millis of a specific command from settings.ini commands section
	 * @param command the name of the command
	 * @return The string points converted to an integer if it exists <br>
	 * or <code> 0 </code> if it doesn't exist
	 */
	public static long getCommandExpire(String command) {
		long result;
		try {
			result =Long.parseLong(getSettingsCommand(command).split("-__-\\+_")[2]);
		} catch (Exception e) {
			result  = 0;
		}

		return result;

	}

	/**
	 * returns the creator of a given command, without the text
	 * @param command the command of which to get the creator
	 * @return a username of the creator of the command <br>
	 * or <code> null </code> if it doesn't exist
	 */
	public static String getCommandCreator(String command) {
		return getSettingsCommand(command).split("-__-\\+_")[1];
	}

	/**
	 *  gets the section names "commands" from settings.ini
	 * @return the section
	 */
	public static Section GetSettingsSectionCommands() {
		return getSettingsSection("commands");

	}




	public static void toLowerCaseDisplayMessage() {
		if ( ini.get("displayMessages") != null) {
			ini.remove("displayMessages");
			storeSettings();
		}
	}
	
	/** transforms the given filename to another encoding.
	 * 
	 * @param source the file to change encoding
	 * @param srcEncoding encoding the file is currently in
	 * @param tgtEncoding new encoding
	 * @throws IOException dayumm file is busy.
	 */
	public static void transform(File source, String srcEncoding, String tgtEncoding) throws IOException {
		File target = source;
		File temp = new File("temp2.ini");
		boolean succes =source.renameTo(temp);
		System.out.println(succes);
		source = new File("temp2.ini");
		try (

				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source), srcEncoding));

				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding)); ) {
			char[] buffer = new char[16384];
			int read;
			while ((read = br.read(buffer)) != -1)
				bw.write(buffer, 0, read);
		} 
		source.delete();
	}
	
	/**
	 * adds a dummy entry to the commands section to make sure the file <br>
	 * is in the  correct format. if it is not, transform the file in the correct format <br>
	 * using {@link #transform(File, String, String)}
	 */
	public static void putSettingsCorrectFormat() {
		saveSettingsCommandWithTime("marco","polo","mod",0);
		System.out.println("before");
		String a;
		boolean contains = false;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("autoreply.ini"))));
			while ( (a = br.readLine()) != null && !contains) {
				if (a.contains("©"))
					contains = true;
			}	
			br.close();
			if (!contains) {
				Util.transform(new File("autoreply.ini"), "UTF-8", "Cp1252");
				System.out.println( "not contain!");
				File inifile = new File("autoreply.ini");
				try {Util.ini = new Wini(inifile);} catch (Exception e) {}
			}
			System.out.println("formatted");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] a) throws Exception{
		System.out.println(ini);
		}
}
