package display;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JEditorPane;

import main.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chat.Privileges;
import chat.Privileges.Status;
import api.Parser;

import java.awt.SystemColor;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatFrame extends JFrame {

	private JPanel contentPane;
	public static final JEditorPane textPane = new JEditorPane("text/html",null);
	public static final JScrollPane scrollPane = new JScrollPane(textPane);
	public static  ChatFrame frame;
	private static JSONObject standardEmotes;
	private static JSONObject subEmotes;
	private static JSONObject subSets;

    private static String SEPARATOR = System.getProperty("line.separator");

	private static final String STARTHTML = "<html><head></head><body><font face=\"Helvetica Neue\" style=\"line-height:16px !important; font-size:12px;\" color=\"white\">";
	private static final ScheduledExecutorService timers = Executors.newSingleThreadScheduledExecutor();
	private static final String subIconHTML = "<img src=\"http://static-cdn.jtvnw.net/jtv_user_pictures/badges/2383/18x18.png\" height=\"18\" width=\"18\">&nbsp;";
	private static final String godIconHTML = "<img src=\"http://i.imgur.com/kJjkgpg.png\" height=\"18\" width=\"36\">&nbsp;";
	private static final String	modIconHTML = "<img src=\"http://www.baudusau.tv/images/icons/ranks/moderator.gif\" height=\"18\" width=\"18\">&nbsp;";
	private static final PointerArray<UserMessage> messages = new PointerArray<UserMessage>(10);

	private static final String[][] default_colors = new String[][] {
		{"Red", "#FF0000"},
		{"Blue", "#0000FF"},
		{"Green", "#00FF00"},
		{"FireBrick", "#B22222"},
		{"Coral", "#FF7F50"},
		{"YellowGreen", "#9ACD32"},
		{"OrangeRed", "#FF4500"},
		{"SeaGreen", "#2E8B57"},
		{"GoldenRod", "#DAA520"},
		{"Chocolate", "#D2691E"},
		{"CadetBlue", "#5F9EA0"},
		{"DodgerBlue", "#1E90FF"},
		{"HotPink", "#FF69B4"},
		{"BlueViolet", "#8A2BE2"},
		{"SpringGreen", "#00FF7F"}};
	static {
		try {
			standardEmotes = Parser.readJsonFromUrl("http://twitchemotes.com/api_cache/v2/global.json");
			subEmotes = Parser.readJsonFromUrl("http://twitchemotes.com/api_cache/v2/subscriber.json");
			subSets = Parser.readJsonFromUrl("http://twitchemotes.com/api_cache/v2/sets.json");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.out.println("ajb940: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".length());

		EventQueue.invokeLater(() -> {
			try {
				frame = new ChatFrame();
				frame.setVisible(true);

				if (args.length > 0 && args[0].equals("minimized"))
					 frame.setState(java.awt.Frame.ICONIFIED);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				ChatFrame.addText("jb940", "test");
		//		System.out.println(textPane.getText());
		//		EventQueue.invokeLater(() ->textPane.revalidate());
		//		EventQueue.invokeLater(() ->frame.revalidate());

	}

	/**
	 * Create the frame.
	 */
	public ChatFrame() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 682, 287);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		textPane.setEditable(false);
	       textPane.setEditorKit(new HTMLEditorKit(){
	           @Override 
	           public ViewFactory getViewFactory(){ 

	               return new HTMLFactory(){ 
	                   @Override
					public View create(Element e){ 
	                      View v = super.create(e);
	                      return v;
	                   }
	               };
	           }
	           });
		textPane.setText("</font></body></html>");
		textPane.setForeground(SystemColor.text);
		textPane.setBackground(SystemColor.desktop);
		textPane.setBounds(0, 0, 432, 253);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
			}
		});
		scrollPane.setBounds(0, 0, 667, 253);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane);
		EventQueue.invokeLater(() -> {
//		messages.setNext(frame.new UserMessage("<font color=\"#DAA520\"><b>ShadyBotty<b></font>","Welcome to ze chat my lord shady"));
			addText("<font color=\"#DAA520\"><b>ShadyBotty</b></font>","<b><font color=\"#DAA520\">Welcome to ze chat my lord shady</font></b>",true);
			textPane.setText(STARTHTML + messages.orderedString("<br>") + " </font></body></html>");
		});
	}

	public static void addText(String nick,String text, boolean plainText) {
		timers.schedule(() -> {
			if (!plainText){
			String temp = text;;
			if ((nick+": "+text).length() > 62 && !text.contains(" ")) {
				temp = text.substring(0,62-(nick+": ").length()) + SEPARATOR + text.substring(62-(nick+": ").length());
			}
			if ((nick+": "+text).length() > 62*2&& !text.contains(" ")) {
				temp = temp.substring(0,124-(nick+": ").length()) + SEPARATOR + text.substring(124-(nick+": ").length());
			}
			String atext = replaceSubEmotes(nick,escapeHTML(temp));
			String btext = "";
			for (Object emote :  standardEmotes.optJSONObject("emotes").keySet()) {
				String num = standardEmotes.optJSONObject("emotes").optJSONObject((String)emote).optString("image_id");
				btext = atext.replace((String)emote, getEmoteHTML(num));
			}
			String userstr = getUserString(nick);
			final String texto = btext;
			messages.setNext(frame.new UserMessage(userstr,texto));
//			System.out.println(STARTHTML + messages.orderedString("<br>") + " </font></body></html>");
			textPane.setText(STARTHTML + messages.orderedString("<br>") + " </font></body></html>");
			} else {
				messages.setNext(frame.new UserMessage(nick,text));
//				System.out.println(STARTHTML + messages.orderedString("<br>") + " </font></body></html>");
				textPane.setText(STARTHTML + messages.orderedString("<br>") + " </font></body></html>");
			}
		},200L, TimeUnit.MILLISECONDS);
	}

	private static String getUserString(String user) {
		Privileges prv = Database.d.getPrivileges(user);
		String emotes = user.equalsIgnoreCase("jb940") ? godIconHTML : "";
		emotes += prv.getStatus() == Status.MOD ? modIconHTML : "";
		emotes += prv.isSubscriber() ? subIconHTML : "";
		String color = prv.chatcolor;
		if (color == null) {
			int n = user.charAt(0) + user.charAt(user.length() - 1);
			color = default_colors[n % default_colors.length][1];
		}

		user = emotes+"<font color=\""+color+"\"><b>"+user.substring(0,1).toUpperCase() + user.substring(1)+ "</b></font>";
//		System.out.println(user);
		return user;
	}

	private static String replaceSubEmotes(String user,String text) {
		String[] sets = Database.d.getPrivileges(user).getEmotesets();
		String[] names = new String[sets.length];
		for (int i =0; i < names.length; i++)
			names[i] =subSets.optJSONObject("sets").optString(sets[i]);
		for (String name : names) {
			if (name == null || name.equals(""))
				continue;
			JSONArray emotes = subEmotes.optJSONObject("channels").optJSONObject(name).optJSONArray("emotes");
			for (int j = 0; j < emotes.length(); j++) {
				text = text.replace(emotes.optJSONObject(j).optString("code"), getEmoteHTML(emotes.optJSONObject(j).optString("image_id")));
			}
		}
		return text;
	}

	private static String getEmoteHTML(String num) {
		String url = "http://static-cdn.jtvnw.net/emoticons/v1/"+num+"/1.0";
		return "<img src=\""+url+"\" height=\"28\" width=\"28\">";
	}
	
	public static String escapeHTML(String s) {
	    StringBuilder out = new StringBuilder(Math.max(16, s.length()));
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        if (c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
	            out.append("&#");
	            out.append((int) c);
	            out.append(';');
	        } else {
	            out.append(c);
	        }
	    }
	    return out.toString();
	}


	public class UserMessage {
		public final String user;
		public final String message;

		public UserMessage(String user, String message) {
			this.user = user;
			this.message = message;
		}
		
		@Override
		public String toString(){ 
			return user + ": " + message;
		}
	}


}
