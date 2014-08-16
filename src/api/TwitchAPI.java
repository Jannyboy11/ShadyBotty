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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	public static JSONObject getJSONStreamShady() {
		JSONObject json = getJSONShady();
		return json.getJSONObject("stream");
	}
	

}
