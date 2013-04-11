package irc.util;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class LogHandler {
	
	private final ConcurrentNavigableMap<Long, LogEntry> storage;
	
	public LogHandler() {
		storage = new ConcurrentSkipListMap<Long, LogEntry>();
	}
	
	public void store(long time, LogEntry log) {
		if (storage.containsKey(time)) {
			storage.get(time).duplicates.add(log);
			return;
		}
		
		storage.putIfAbsent(time, log);
	}
	
	public NavigableSet<Long> keys() {
		return storage.keySet();
	}
	
	public LogEntry get(Long key) {
		return storage.get(key);
	}
	
	public int size() {return storage.size();}
	
}
