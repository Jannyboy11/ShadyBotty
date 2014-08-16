package main;

public class ShadyBottyMain {
    
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        ShadyBotty bot = new ShadyBotty();
            
            // Enable debugging output.
            bot.setVerbose(true);
            
            // Connect to the IRC server.
            bot.connect("irc.twitch.tv",6667,"oauth:h9c144makj10x84wdazgf0exgl13p9k");

            // Join the #pircbot channel.
            bot.joinChannel("#shadybunny");
            
        }
        
    }
    

