package irc.concurrent;

import irc.util.LogHandler;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class ReadThreadPool {
	
	private static final Logger logger = Logger.getLogger(ReadThreadPool.class.getName());

	private final ExecutorService executor;
	private final File[] folders;
	private final LogHandler handler;

	public ReadThreadPool(int maxThreads, File[] folders, LogHandler handler) {
		// Runtime.getRuntime().availableProcessors()
		executor = Executors.newFixedThreadPool(maxThreads);
		this.folders = folders;
		this.handler = handler;
	}

	public void run() {
		for (File folder : folders) {
			File[] dirs = folder.listFiles(new FileFilter() {

				public boolean accept(File pathname) {
					return pathname.isDirectory();
				}

			});
			
			for (File dir : dirs) {
				File[] files = dir.listFiles(new FileFilter() {

					public boolean accept(File pathname) {
						return pathname.toString().endsWith(".log");
					}

				});

				for (File file : files) {
					executor.execute(new ReadLog(file, handler));
				}
			}
		}
		
		executor.shutdown();
		
		try {
			executor.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.severe(e.getMessage());
		}
	}

}
