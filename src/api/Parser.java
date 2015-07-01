package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class Parser {

	public Parser() {

	}
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket(); 
		System.out.println("before con!");
		socket.connect(new InetSocketAddress("www.google.com", 80), 30000);
		System.out.println("after con");
		SocketAddress javalobby = socket.getRemoteSocketAddress( ); 
		socket.close( );
		System.out.println("done!");
	}
}
