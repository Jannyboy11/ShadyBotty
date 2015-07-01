package api;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import main.ShadyBotty;

import org.json.JSONException;
import org.json.JSONObject;


public class TwitchAPI {
	private static final String API = "https://api.twitch.tv/kraken/";
	private static final String SHADY = "streams/shadybunny";
	private static final String SHADYCHANNEL = "channels/shadybunny";
	private static final String SECRETTITLE = "6s3zahxold5ibp84c7r60gqr5jswzo";

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

	public static void PUT(String url, String[] names,String[] values) {
		try {
		String toWrite = "?"+names[0] + "=" + values[0];
		for (int i = 1; i < names.length; i++) {
			toWrite += "&"+names[i]+"="+values[i];
		}
		URL site = new URL(url + toWrite);
		HttpURLConnection httpCon = (HttpURLConnection) site.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
 	httpCon.addRequestProperty("Authorization", "OAuth 6s3zahxold5ibp84c7r60gqr5jswzo");
		OutputStreamWriter out = new OutputStreamWriter(
		    httpCon.getOutputStream());


		out.write(toWrite.substring(1));
//	System.out.println(toWrite);
		out.close();
		httpCon.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateChannel(String theTitle) {
		try {
			PUT(API + SHADYCHANNEL,new String[] {"oauth_token","channel[status]"}, new String[] {SECRETTITLE,URLEncoder.encode(theTitle)});
			ShadyBotty.scoreToFile(theTitle.replaceAll("\\+", " "));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		public static void main(String[] args) {
			ShadyBotty a = new ShadyBotty();
			a.setTitle("5-2 Priest");
		}
	
	public static String getTitle() {
		try {
			return Parser.readJsonFromUrl(API + SHADYCHANNEL + "?oauth=" + SECRETTITLE).optString("status");
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
