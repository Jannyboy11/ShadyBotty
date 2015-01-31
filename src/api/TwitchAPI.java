package api;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;


public class TwitchAPI {
	private static final String API = "https://api.twitch.tv/kraken/";
	private static final String SHADY = "streams/shadybunny";
	private static final String SHADYCHANNEL = "channels/shadybunny";
	private static final String SECRETTITLE = "kme7og92u2qluzu8f6ax2mo4fh4lja";

	public TwitchAPI() {

	}

	public static JSONObject getJSONShady() {
		try {
			return Parser.readJsonFromUrl(API + SHADY);
		} catch (JSONException | IOException e) {
		}
		return null;
	}

	public static JSONObject getJSONStreamShady() {
		JSONObject json = getJSONShady();
		if (json != null)
			return json.optJSONObject("stream");
		return null;
	}

	public static int getViewersShady() {
		JSONObject json = getJSONStreamShady();
		if (json != null)
			return json.optInt("viewers");
		return 0;
	}

	public static void PUT(String url, String[] names,String[] values) throws Exception {
		String toWrite = "?"+names[0] + "=" + values[0];
		for (int i = 1; i < names.length; i++) {
			toWrite += "&"+names[i]+"="+values[i];
		}
		URL site = new URL(url + toWrite);
		HttpURLConnection httpCon = (HttpURLConnection) site.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
 	httpCon.addRequestProperty("Authorization", "OAuth 493i5sfeqdpe89v6y6xwa0ltdtlyp4");
		OutputStreamWriter out = new OutputStreamWriter(
		    httpCon.getOutputStream());


		out.write(toWrite);
		System.out.println(toWrite);
		out.close();
		httpCon.getInputStream();
	}
	
	public static void updateChannel(String theTitle) {
		try {
			PUT(API + SHADYCHANNEL,new String[] {"oauth_token","channel[status]"}, new String[] {SECRETTITLE,theTitle});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		updateChannel("Gods of the arena Kappa happy CodeDay JB!".replace(" ", "+"));
	}


}
