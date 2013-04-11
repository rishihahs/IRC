package irc.concurrent;

import irc.io.DataWriter;
import irc.util.LogHandler;

import java.util.NavigableSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class WriteThreadPool {
	
	private static final Logger logger = Logger.getLogger(WriteThreadPool.class.getName());

	private final ExecutorService executor;
	private final DataWriter writer;
	private final LogHandler handler;

	public WriteThreadPool(int maxThreads, DataWriter writer, LogHandler handler) {
		// Runtime.getRuntime().availableProcessors()
		executor = Executors.newFixedThreadPool(maxThreads);
		this.writer = writer;
		this.handler = handler;
	}

	public void run() {
		NavigableSet<Long> keys = handler.keys();
		for (Long key : keys) {
			executor.execute(new WriteLog(writer, handler.get(key)));
		}
		
		executor.shutdown();
		
		try {
			executor.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
		}
	}

}
