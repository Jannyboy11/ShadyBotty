package api;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class RiotAPI {
	private static final String API = "https://__.api.pvp.net/api/lol/__";
	private static final String sumVersion = "/v1.4";
	private static final String sumProfile = "/summoner/by-name/";
	private static final String rankVersion = "/v2.5";
	private static final String rankProfile = "/league/by-summoner/__/entry";
	private static final String key  = "?api_key=fdc129d4-aaea-47f4-924c-5768b8d205ac";

	public static String getSummoner(String name,String server) {

		try {
			JSONObject json = Parser.readJsonFromUrl(API.replaceAll("__",server) + sumVersion + sumProfile + name.toLowerCase() + key);
			System.out.println(json.optJSONObject(name.toLowerCase()).optString("id"));
			return json.optJSONObject(name.toLowerCase()).optString("id");
		} catch (JSONException | IOException  | NullPointerException e) {
		}
		return "user not found";
	}

	public static String getRank(String name,String server) {
		try {
			String id = getSummoner(name,server);
			System.out.println(API.replaceAll("__",server) + rankVersion + rankProfile.replace("__", id)+ key);
			if (id.startsWith("user"))
				return id;
			System.out.println(API.replaceAll("__",server) + rankVersion + rankProfile.replace("__", id)+ key);
			JSONObject json = Parser.readJsonFromUrl(API.replace("__",server) + rankVersion + rankProfile.replace("__", id)+ key);
			
			if (json.optJSONArray(id).optJSONObject(0).optString("tier").length() < 3)
				return "user not level 30 or no rank..";
			JSONObject rank = json.optJSONArray(id).optJSONObject(0).optJSONArray("entries").optJSONObject(0);
			return name + "\'s current rank is: " + json.optJSONArray(id).optJSONObject(0).optString("tier") 
					+ " " + rank.optString("division") + " " + rank.optString("leaguePoints") + " LP! (name: "
					+ json.optJSONArray(id).optJSONObject(0).optString("name") + ")";
		} catch (JSONException | IOException  | NullPointerException e) {
		}
		return "user not level 30 or no rank.";
	}

}
