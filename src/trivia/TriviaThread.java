package trivia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import chat.Nicknames;
import points.Points;
import main.ShadyBotty;
import main.ShadyBottyMain;

public class TriviaThread extends Thread{
	public static final int QUESTIONAMOUNT = 173;
	public static ArrayList<String> questions = new ArrayList<String>(QUESTIONAMOUNT);
	public static String topic = "";
	public static String question ="";
	public static String hint ="";
	public static ArrayList<String> answers = new ArrayList<String>();
	public static String displayAnswer = "";
	public static boolean off;
	public static LinkedHashMap<String,Integer> winners =new  LinkedHashMap<String,Integer>();
	public static long firstAnswer = 0;
	public static int lastHalfMinChats = 0;
	public ShadyBotty b;
	public static TriviaThread tr;




	public TriviaThread(ShadyBotty b) {
		off = true;
		this.b=b;
		if (questions.size() == 0)
			reload();
		tr = this;
	}

	public static void reload() {
		try {
			InputStream is = TriviaThread.class.getResourceAsStream("HearthstoneQuestions.txt");
			InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
			BufferedReader in = new BufferedReader(isr);
			System.out.println("! no excep");
			for (int i = 0; i < QUESTIONAMOUNT; i++) {
				questions.add(in.readLine());
			}

			in.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		Random rand = new Random();
		while (!off) {
			
			if (questions.size() == 0)
				reload();
			int randomNum = rand.nextInt(questions.size());			
			String nextQuestion = questions.get(randomNum);
			questions.remove(randomNum);
			System.out.println("!" + nextQuestion);
			separateQuestion(nextQuestion); //separates the questions based on the ©
			System.out.println(topic + "  " + question);
			b.sendMessage(ShadyBottyMain.ROOM, "[" + randomNum + "] topic: " + topic + ". Question: " + question);
			questionOn();// wait till the question is done, whether answered or ran out of time.
			topic = "";
			
			if (hasWinner())  {
				String toChatMsg = getWinnerStringAddPoints();
				System.out.println(toChatMsg);
				b.sendMessage(ShadyBottyMain.ROOM, toChatMsg);
				winners.clear();
				answers.clear();
				firstAnswer = 0;
			} else {
				b.sendMessage(ShadyBottyMain.ROOM, "the answer was: " + displayAnswer + ". Nobody has guessed correctly! motionRekt");
					
				System.out.println("the answer was: " + displayAnswer + ". Nobody has guessed correctly! motionRekt");
				answers.clear();
			}
			try {
				Thread.sleep(180000);
			} catch (InterruptedException e) {

			}
			
		}
	}

	
	public void questionOn() {
		Long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start <= 30000 && firstAnswer == 0)
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (hasWinner())
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}


	public void separateQuestion(String qu) {
		String[] sepQu = qu.split("©");
		topic = sepQu[0];
		question = sepQu[1];
		hint = sepQu[2];
		displayAnswer = sepQu[3];
		System.out.println(displayAnswer);
		for (int i = 3; i < sepQu.length; i++) {
			answers.add(sepQu[i].replaceAll("[^a-zA-Z0-9]","").toLowerCase());
			System.out.println(sepQu[i].replaceAll("[^a-zA-Z0-9]","").toLowerCase() );
		}
	}

	public boolean hasWinner() {
		if (winners.size() == 0)
			return false;
		return true;
	}

	public static void addWinner(String winner) {
		if (firstAnswer == 0) {
			winners.put(winner, 6);
			firstAnswer = System.currentTimeMillis();
			return;
		}//check if question has already been answered, if not, input time of first answer and give guy points

		Long now = System.currentTimeMillis();
		if (!winners.containsKey(winner)) {
			if (now - firstAnswer < 500)
				winners.put(winner, 5);
			else 	if (now - firstAnswer < 1100)
				winners.put(winner, 4);
			else 	if (now - firstAnswer < 1800)
				winners.put(winner, 3);
			else 	if (now - firstAnswer < 2700)
				winners.put(winner, 2);
			else if (now - firstAnswer < 3800)
				winners.put(winner, 1);		
		}
	}




	
	public String getWinnerStringAddPoints() {
		
		String winner2 = winners.keySet().iterator().next();
		 String toChat = "the answer was: " + displayAnswer + ". motionTens Winner(30p): " + Nicknames.getNick(winner2)+ ".";
		 Points.addPoints(winner2, 30);
		 winners.remove(winner2);
		int last = 30;
		if (winners.size() == 0)
			return toChat;
		for (String a : winners.keySet()) {
			Points.addPoints(a, winners.get(a));
			if (winners.get(a) != last) {
				last = winners.get(a);
				toChat = toChat.substring(0, toChat.length()-1) + ". " + last + "p:";
			}
			toChat += " " + Nicknames.getNick(a) + ",";
		}
		toChat = toChat.substring(0, toChat.length()-1) + ".";
		return toChat;
	}
	
	public static void main(String[] args) {
		reload();
		System.out.println(questions.get(QUESTIONAMOUNT-1));
	}

}
