package commands.autoreplies;

import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Map.Entry;
import main.Database;

import org.ini4j.Wini;

public class Autoreplies  {
	private static Wini ini = Database.getAutoreplies();
	public static String putReply(String regex, String response) {
		ini.put("autoreply", regex,response);
		Database.storeAutoreplies();
//		System.out.println(regex +" the rsponse: " + response);
//		System.out.println("response: \""+getResponse("hello !")+"\"");
		return "SUCCES!";
	}
	
	public static void main(String[] args) {
		System.out.println("yay!");
		try {
			Process proc = Runtime.getRuntime().exec("java -jar ./test.jar");
			while (true)
				convertStreamToString(proc.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
//		System.out.println("hello shadybunny_".matches(".*(?i)(hello|hey|whats up|yow|elleu|(good )?(day|evening|afternoon)) ?((shady(bunny)?)|chat|( [^\\w+])|[!\\.,]|$).*"));
	}
	private static void convertStreamToString(InputStream inputStream) {
		while (true)
			try {
				int num = inputStream.read();
				if (num != -1)
				System.out.print((char) inputStream.read());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public static String getResponse(String message) {
//		System.out.println("message = \""+message+"\"");
//		System.out.println(replies);

		 WiniWithTime timed = new WiniWithTime(ini,"autoreply");
		for (Entry<String,Entry<String,Long>> a : timed) {
//				System.out.println(a.getKey() + "  " + a.getValue().getKey() + "   " + a.getValue());
			if (message.matches(a.getKey()) && System.currentTimeMillis() - a.getValue().getValue() > 12000L) {
				timed.setUsed();
				
				return a.getValue().getKey();
			}
		}
		return null;
	}
	
	public static String chatToAutoreply(String user,String message, boolean mod) {
		String[] msg = message.split(" ");
		String[] reply = msg.length < 2 || message.length() < msg[0].length()+msg[1].length() +2 ?
				new String[] { } : message.substring(msg[0].length()+msg[1].length() +2).split("\\|\\|");
		if (msg.length < 2 || !msg[0].equalsIgnoreCase("!autoreply") || reply.length < 2)
			return null;
		if (msg[0].startsWith("!")) {
			if (mod) {
				if (msg[1].equalsIgnoreCase("add") || msg[1].equalsIgnoreCase("edit")) {
//					System.out.println("HERE");
					return putReply(reply[0],reply[1]);
					
				}


			}
		}
		return null;
	}
	
	public static String forRegex(String aRegexFragment){
	    final StringBuilder result = new StringBuilder();

	    final StringCharacterIterator iterator = new StringCharacterIterator(aRegexFragment);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	      /*
	       All literals need to have backslashes doubled.
	      */
	      if (character == '.') {
	        result.append("\\.");
	      }
	      else if (character == '\\') {
	        result.append("\\\\");
	      }
	      else if (character == '?') {
	        result.append("\\?");
	      }
	      else if (character == '+') {
	        result.append("\\+");
	      }
	      else if (character == '&') {
	        result.append("\\&");
	      }
	      else if (character == ':') {
	        result.append("\\:");
	      }
	      else if (character == '{') {
	        result.append("\\{");
	      }
	      else if (character == '}') {
	        result.append("\\}");
	      }
	      else if (character == '[') {
	        result.append("\\[");
	      }
	      else if (character == ']') {
	        result.append("\\]");
	      }
	      else if (character == '(') {
	        result.append("\\(");
	      }
	      else if (character == ')') {
	        result.append("\\)");
	      }
	      else if (character == '^') {
	        result.append("\\^");
	      }
	      else if (character == '$') {
	        result.append("\\$");
	      }
	      else {
	        //the char is not a special one
	        //add it to the result as is
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString().replaceAll("®\\\\", "");
	  }
}
