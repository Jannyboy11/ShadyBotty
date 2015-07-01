package commands.autoreplies;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.ini4j.Wini;

public class WiniWithTime implements Iterator<Entry<String,Entry<String,Long>>>, Iterable<Entry<String,Entry<String,Long>>> {

	private Wini ini;
	Iterator<Entry<String, String>> theIt;
	private Entry<String,String> last;
	private String section;
	
	public void setUsed() {
		last.setValue(last.getValue().split("©©")[0]+"©©"+System.currentTimeMillis());
	}
	
	public void get(String sectionName, String optionName) {
		ini.get(sectionName, optionName);
		
	}
	
	public void put(String sectionName, String optionName,String value) {
		ini.put(sectionName, optionName,value);
	}
	
	public WiniWithTime(Wini ini,String section) {
		this.ini = ini;
		this.section = section;
		theIt = ini.get(section).entrySet().iterator();
	}
	
	public WiniWithTime(Wini ini) {
		this.ini = ini;
		
	}
	
	public void setSection(String section) {
		this.section = section;
		theIt = ini.get(section).entrySet().iterator();
	}

	@Override
	public boolean hasNext() {
		boolean Next = theIt.hasNext();
		if (!Next)
			theIt = ini.get(section).entrySet().iterator();
		return Next;
	}

	@Override
	public Map.Entry<String, 
		 Map.Entry<String,Long>> next() {
		last = theIt.next();
		
		Entry<String,Long> value = new AbstractMap.SimpleEntry<String,Long>(last.getValue().split("©©")[0],
				last.getValue().split("©©").length == 1 ? 1L : Long.parseLong(last.getValue().split("©©")[1]));
		System.out.println(value.getValue());
		Entry<String,Entry<String,Long>> result = new AbstractMap.SimpleEntry<String, 
				Map.Entry<String,Long>>(last.getKey(), value);
		return result; 
	}

	@Override
	public Iterator iterator(){
		return this ;
	}

}
