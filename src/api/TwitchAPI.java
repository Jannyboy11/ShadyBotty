package api;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


public class TwitchAPI {
	private static final String API = "https://api.twitch.tv/kraken/";
	private static final String SHADY = "streams/shadybunny";
	
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
			return json.optInt("viewer");
		return 0;
	}
	

}
