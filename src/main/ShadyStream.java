package main;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class ShadyStream extends PrintStream {

	PrintStream printTo;
	
	public ShadyStream(OutputStream old) throws FileNotFoundException {
		super(new BufferedOutputStream(new FileOutputStream("Logs.log")),true);
		printTo = new PrintStream(old);
		
	}

	@Override
	public void println(String x) {
		printTo.println(x);
		
		super.println(x);
	}

}
