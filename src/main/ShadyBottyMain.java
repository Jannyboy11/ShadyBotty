package main;

public class ShadyBottyMain {
    
    public static void main(String[] args) throws Exception {
        
        // Now start our bot up.
        ShadyBotty bot = new ShadyBotty();
        
        // Enable debugging output.
        bot.setVerbose(true);
        
        // Connect to the IRC server.
        bot.connect("irc.freenode.net");

        // Join the #pircbot channel.
        bot.joinChannel("#pircbot");
        
    }
    
}
