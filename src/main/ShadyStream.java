package main;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

public class ShadyStream extends PrintStream {

	PrintStream printTo;
	
	public ShadyStream(OutputStream old) throws FileNotFoundException, UnsupportedEncodingException {
		super(new BufferedOutputStream(new FileOutputStream("Logs.log",true)),true,"UTF-8");
		printTo = new PrintStream(old);
		Date date = new Date();
		super.println();
		super.println();
		super.println("-------------------------------------------------------------");
		super.println("RESTART ON " + date.toString());
		super.println("-------------------------------------------------------------");
		super.println();
		super.println();
	
	}

	@Override
	public void println(String x) {
		printTo.println(x);
		Calendar a = Calendar.getInstance();
		super.println(a.get(Calendar.HOUR_OF_DAY) + ":"+ a.get(Calendar.MINUTE) + ":"+ a.get(Calendar.SECOND) + "  "+ x);
		super.flush();
	}
	

}
