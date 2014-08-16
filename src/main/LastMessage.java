package main;

import java.util.HashMap;

public class LastMessage {
 HashMap<String,Long> Users;
 
 public LastMessage() {
	 Users = new HashMap<String,Long>();
 }
 
 public void setLastMessage(String user, Long time) { 
	 Users.put(user, time);
 }
 
}
