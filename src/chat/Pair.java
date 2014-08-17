package chat;

public class Pair{
	
	private int timeoutLength;
	private String reason;
	
	protected Pair(int tl, String r){
		timeoutLength = tl;
		reason = r;
	}

	public int getTimeoutLength() {
		return timeoutLength;
	}

	public void setTimeoutLength(int tl) {
		timeoutLength = tl;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String r) {
		reason = r;
	}		
	
}
