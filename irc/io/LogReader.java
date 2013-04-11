package irc.io;

import irc.util.LogEntry;
import irc.util.LogHandler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class LogReader {
	
	private static final Logger logger 					= Logger.getLogger(LogReader.class.getName());
	
	private static final Pattern DATE_PATTERN 			= Pattern.compile("([\\w]+\\s\\d{2}\\s\\d{2}:\\d{2}:\\d{2})");
	private static final Pattern YEAR_PATTERN			= Pattern.compile("(\\d{4})");
	
	private LogReader() {
	}
	
	public synchronized static LogReader getInstance() {
		return new LogReader();
	}
	
	public void read(File file, LogHandler handler) throws IOException {
		RandomAccessFile ram 				= new RandomAccessFile(file, "r");
		final SimpleDateFormat DATE_FORMAT 	= new SimpleDateFormat("MMM dd kk:mm:ss");
		final Calendar calendar				= Calendar.getInstance();

		try {
			
		String line;
		long pos = ram.getFilePointer();
		int year = 2011;
		while ((line = ram.readLine()) != null) {
			int y = determineYear(line);
			if (y > 0) {
				year = y;
				pos = ram.getFilePointer();
				continue;
			}
			
			Matcher m = DATE_PATTERN.matcher(line);
			if (!m.find()) {
				pos = ram.getFilePointer();
				continue;
			}
			
			calendar.setTime(DATE_FORMAT.parse(m.group(1)));
			calendar.set(Calendar.YEAR, year);
			handler.store(calendar.getTimeInMillis(), new LogEntry(pos, file));
			
			pos = ram.getFilePointer();
		}
		
		} catch (ParseException e) {
			logger.severe(e.getMessage());
		} finally {
			ram.close();
		}
	}
	
	private int determineYear(String line) {
		if (line.startsWith("****")) {
			Matcher m = YEAR_PATTERN.matcher(line);
			m.find();
			return Integer.parseInt(m.group(1));
		}
		
		return -1;
	}

}
