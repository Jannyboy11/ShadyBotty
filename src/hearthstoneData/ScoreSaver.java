package hearthstoneData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.ini4j.Wini;

public class ScoreSaver {
	private static File saveFile = new File("stats.ini");
	private static final String DECKHTML ="<html>\n <script src=\"./jquery-1.11.2.min.js\"></script>\n <body>\n 	<script>\n 	function endsWith(str, suffix) {\n 		return str.indexOf(suffix, str.length - suffix.length) !== -1;\n 	}\n 	var reg = /.*<img id=\"image\" src=\"(\\S*)\"/g;\n 		 setInterval(function(){\n  	var last = document.getElementById(\"image\").src;\n 	   $.ajax({ \n    url : window.location.pathname,\n     dataType : \"text\",\n     ifModified : true,\n     success : function(data, textStatus) {\n 		reg.lastIndex = 0;\n 		var arr = reg.exec(data);\n 		arr =reg.exec(data);\n 		console.log(last);\n 		console.log(arr[1]);\n 		 if (last != arr[1] && !(endsWith(last,\"deck.html\") && arr[1] == \"\")) { \n		 location.reload();\n 		 } \n      }   }); }, 5000);\n 	</script>\n 	<div>\n 	<img id=\"image\" src=\"THEFRIGGENIMAGE\" width=\"220\" height=\"700\" >\n 	</div>\n 	</body>\n </html>";
	private static Wini saveIni;
	private static String hunter;
	private static int hunterCount;
	private static String warrior;
	private static int warriorCount;
	private static String druid;
	private static int druidCount;
	private static String rogue;
	private static int rogueCount;
	private static String priest;
	private static int priestCount;
	private static String warlock;
	private static int warlockCount;
	private static String shaman;
	private static int shamanCount;
	private static Class thisclass = ScoreSaver.class;
	private static NumberFormat form = new DecimalFormat("##.##");
	static{
		try {

			saveFile.createNewFile();
			saveIni = new Wini(saveFile);
			hunter = saveIni.get("score", "hunter") == null? "0-0" :  saveIni.get("score", "hunter");
			hunterCount = saveIni.get("count","hunter") == null? 0 : saveIni.get("count","hunter",int.class);
			warrior = saveIni.get("score", "warrior") == null? "0-0" :  saveIni.get("score", "warrior");
			warriorCount = saveIni.get("count","warrior") == null? 0 : saveIni.get("count","warrior",int.class);

			druid = saveIni.get("score", "druid") == null? "0-0" :  saveIni.get("score", "druid");
			druidCount = saveIni.get("count","druid") == null? 0 : saveIni.get("count","druid",int.class);
			rogue = saveIni.get("score", "rogue") == null? "0-0" :  saveIni.get("score", "rogue");
			rogueCount = saveIni.get("count","rogue") == null? 0 : saveIni.get("count","rogue",int.class);
			priest = saveIni.get("score", "priest") == null? "0-0" :  saveIni.get("score", "priest");
			priestCount = saveIni.get("count","priest") == null? 0 : saveIni.get("count","priest",int.class);
			warlock = saveIni.get("score", "warlock") == null? "0-0" :  saveIni.get("score", "warlock");
			warlockCount = saveIni.get("count","warlock") == null? 0 : saveIni.get("count","warlock",int.class);
			shaman = saveIni.get("score", "shaman") == null? "0-0" :  saveIni.get("score", "shaman");
			shamanCount = saveIni.get("count","shaman") == null? 0 : saveIni.get("count","shaman",int.class);
		} catch (Exception e) {

		}
	}





	public static void addScore(int wins, int losses, String hero2) {
		String h = hero2.toLowerCase();
		try {
			Field field = thisclass.getDeclaredField(h);
			Field field2 = thisclass.getDeclaredField(h+"Count");
			field.setAccessible(true);
			field2.setAccessible(true);
			String score = (String) field.get(null);
			score = score == null? "0-0" : score;
			int win = Integer.parseInt(score.split("-")[0])+wins;
			int lose =Integer.parseInt(score.split("-")[1])+losses;
			field.set(null, win + "-" + lose);
			field2.set(null, (int)field2.get(null)+1);
			saveIni.put("score",h,win + "-" + lose);
			saveIni.put("count",h,field2.get(null));
			saveIni.store();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean isScoreRequest(String input) {
		String[] msg = input.split(" ");
		return msg[0].equalsIgnoreCase("!hunter") ||msg[0].equalsIgnoreCase("!warlock") || 
				msg[0].equalsIgnoreCase("!druid") || msg[0].equalsIgnoreCase("!rogue") || 
				msg[0].equalsIgnoreCase("!priest") || msg[0].equalsIgnoreCase("!shaman") || 
				msg[0].equalsIgnoreCase("!warrior") || msg[0].equalsIgnoreCase("!stats");
	}

	public static String getStats(String input) {
		String requested = input.split(" ")[0].substring(1);
		if (requested.equalsIgnoreCase("stats")) {
			int win = Integer.parseInt(hunter.split("-")[0]) + Integer.parseInt(warrior.split("-")[0]) + 
					Integer.parseInt(druid.split("-")[0]) + Integer.parseInt(rogue.split("-")[0]) +
					Integer.parseInt(warlock.split("-")[0]) + Integer.parseInt(priest.split("-")[0]) +
					Integer.parseInt(shaman.split("-")[0]);
			int lose = Integer.parseInt(hunter.split("-")[1]) + Integer.parseInt(warrior.split("-")[1]) + 
					Integer.parseInt(druid.split("-")[1]) + Integer.parseInt(rogue.split("-")[1]) +
					Integer.parseInt(warlock.split("-")[1]) + Integer.parseInt(priest.split("-")[1]) +
					Integer.parseInt(shaman.split("-")[1]);
			String result = "Shadys stats in general are: " + win + "wins" +", " + lose+"losses. Win percentage: ";
			double winpercentage = win+lose != 0?((double)Math.round(((double)win)/((double)(win +lose))*10000))/100 : 0;
			int runs = (druidCount + hunterCount+warlockCount+shamanCount+priestCount+rogueCount+warriorCount);
			double winperrun = (double)win / (double)runs;
			result += form.format(winpercentage) + "%. on " + requested + " shady gets on average " + (winperrun == -1 ? "infinite " : form.format(winperrun)) + "wins. (" +runs +" runs)";
			return result;

		} else {
			Field field;
			try {
				field = thisclass.getDeclaredField(requested.toLowerCase());

				Field field2 = thisclass.getDeclaredField(requested.toLowerCase()+"Count");
				field.setAccessible(true);
				field2.setAccessible(true);
				String score = (String) field.get(null);
				score = score == null? "0-0" : score;
				int win = Integer.parseInt(score.split("-")[0]);
				int lose =Integer.parseInt(score.split("-")[1]);
				String result = "Shadys stats on " + requested + " are: " + win + "wins" +", " + lose+"losses. Win percentage: ";

				double winpercentage = win+lose != 0?((double)Math.round(((double)win)/((double)(win +lose))*10000))/100 : 0;
				double winperrun = field2.getInt(null) == 0? -1 : ((double)win)/((double)field2.getInt(null));
				result += form.format(winpercentage) + "%. on " + requested + " shady gets on average " + (winperrun == -1 ? "infinite " : form.format(winperrun)) + "wins. (" +field2.getInt(null) +" runs)";

				return result;
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		createDeck("http://www.picturesnew.com/media/images/picture-wallpaper.jpg");
	}
	
	public static void createDeck(String url) {
		String encUrl = url;
		String toWrite = DECKHTML.replace("THEFRIGGENIMAGE", url);
				try {
			if (!encUrl.equals("")) {
				URL b = new URL(url);
			}
			new File("deck.html").createNewFile();
			PrintWriter a = new PrintWriter("deck.html", "UTF-8");
			a.print(toWrite);
			a.flush();
			a.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
