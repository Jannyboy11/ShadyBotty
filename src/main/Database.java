package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import chat.Privileges;

public class Database {
 private HashMap<String, Long> lastChat;
 private HashMap<String, Privileges> privileges;
 
 public Database() {
	 lastChat = new HashMap<String,Long>();
	 privileges = new HashMap<String,Privileges>();
 }
 public Privileges getPrivileges(String user) {
	 return privileges.get(user);
 }
 public void setLastMessage(String user, Long time) { 
	 lastChat.put(user, time);
 }
 
 public Long getLongLastMessage(String user) { 
	 Long last = lastChat.get(user);
     return last;
 }
 
 public String getTimeLastMessage(String user) { 
	 Long last = getLongLastMessage(user);
	 Date lastDate = new Date(last);
     SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
     System.out.println(ft.format(lastDate));
     return ft.format(lastDate);
 }
 
 public Long getDifferenceInLong(String user) {
	 Long last =  System.currentTimeMillis();;
	 
	 if (lastChat.containsKey(user)) 
		 	last = lastChat.get(user);
	 
	 Long now = System.currentTimeMillis();
	 Long difference = now-last;
	 return difference;
 }
 
 public Long getDifferenceSeconds(String user) {
	Long difference = getDifferenceInLong(user);
    System.out.println(difference/1000);
    return difference/1000;
 }
 
}
