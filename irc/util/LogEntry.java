package irc.util;

import java.io.File;
import java.util.Vector;

public class LogEntry {
	
	public final long pos;
	public final File file;
	public final Vector<LogEntry> duplicates;
	
	public LogEntry(long pos, File file) {
		this.pos = pos;
		this.file = file;
		duplicates = new Vector<LogEntry>();
	}
	
	@Override
	public String toString() {
		return "[ pos: " + pos + " file: " + file + " ]";
	}

}
