package api;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class SongAPI {
	
	private static final String API = "http://ws.audioscrobbler.com/2.0/?method=user.getrecenttracks&user=shadybunnylive&api_key=6bb626000cf39dabadb9e904e613d024&format=json&limit=1";
///,\"recenttracks\",\"track\",\"name\")
	private static String latestMusic = "none";
	public SongAPI() {
	}
	
	public static JSONObject getJSONSong() {
		try {
			return Parser.readJsonFromUrl(API);
		} catch (JSONException | IOException e) {
		}
		return null;
	}
	
	public static String getSongNameShady() {
		JSONObject json = getJSONSong();

		if (json != null) {
			try {
			String song = json.optJSONObject("recenttracks")
					.optJSONArray("track").optJSONObject(0)
					.optString("name");
			return song;
			} catch (NullPointerException e) {
				return "none";
			}
		}
		return "none";
	}
	
	public static String getArtistNameShady() {
		JSONObject json = getJSONSong();

		if (json != null) {
			try {
			String song = 	json.optJSONObject("recenttracks")
					.optJSONArray("track").optJSONObject(0)
					.optJSONObject("artist")
					.optString("#text");
			return song;
			} catch (NullPointerException e) {
				return "none";
			}
			

		}
		return "none";
	}
	public static void checkMusic() {
		String musicAPI = getArtistNameShady() + " - " + getSongNameShady();
		if (musicAPI == "none" || musicAPI == null || musicAPI == latestMusic)
			return;
		latestMusic = musicAPI;
	}
	
	public static String getLatestmusic() {
		return latestMusic;
	}
	
	public static void setLatestmusic(String music) {
		return;
	}
	
}
