package irc.concurrent;

import irc.io.DataReader;
import irc.io.DataWriter;
import irc.util.LogEntry;

import java.io.IOException;
import java.util.logging.Logger;


public class WriteLog implements Runnable {
	
	private static final Logger logger = Logger.getLogger(WriteLog.class.getName());
	
	private final DataWriter writer;
	private final LogEntry entry;
	
	public WriteLog(DataWriter writer, LogEntry entry) {
		this.writer = writer;
		this.entry = entry;
	}

	@Override
	public void run() {
		try {
			DataReader.getInstance().read(writer, entry);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

}
