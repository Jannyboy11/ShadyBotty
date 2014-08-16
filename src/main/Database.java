package main;

import java.util.HashMap;

public class Database {
 HashMap<String,Long> Users;
 
 public Database() {
	 Users = new HashMap<String,Long>();
 }
 
 public void setLastMessage(String user, Long time) { 
	 Users.put(user, time);
 }
 
}
