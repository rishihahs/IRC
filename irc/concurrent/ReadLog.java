package irc.concurrent;

import irc.io.LogReader;
import irc.util.LogHandler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;


public class ReadLog implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ReadLog.class.getName());
	
	private final File file;
	private final LogHandler handler;
	
	public ReadLog(File f, LogHandler handler) {
		file = f;
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			LogReader.getInstance().read(file, handler);
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}

}
