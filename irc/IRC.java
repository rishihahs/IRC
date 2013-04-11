package irc;

import irc.concurrent.ReadThreadPool;
import irc.concurrent.WriteThreadPool;
import irc.io.DataWriter;
import irc.util.LogHandler;

import java.io.File;
import java.io.IOException;


public class IRC {

	private static final File OUTPUT_LOG = new File(
			System.getProperty("user.dir"), "logdump.log");
	
	private static final File[] INPUT_DIRECTORIES = {
		new File(System.getProperty("user.home"), "irc")
	};

	public static void main(String... args) throws IOException {
		LogHandler handler = new LogHandler();

		ReadThreadPool readPool = new ReadThreadPool(Runtime.getRuntime()
				.availableProcessors(), INPUT_DIRECTORIES, handler);
		readPool.run();

		DataWriter writer = new DataWriter(OUTPUT_LOG);
		WriteThreadPool writePool = new WriteThreadPool(Runtime.getRuntime()
				.availableProcessors(), writer, handler);
		writePool.run();
		writer.close();
	}

}
