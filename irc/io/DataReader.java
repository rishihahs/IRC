package irc.io;

import irc.util.LogEntry;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;


public class DataReader {
	
	private static final Pattern DATE_PATTERN 			= Pattern.compile("([\\w]+\\s\\d{2}\\s\\d{2}:\\d{2}:\\d{2})");
	private static final String LINE_SEPARATOR			= System.getProperty("line.separator");
	
	private DataReader() {
	}
	
	public synchronized static DataReader getInstance() {
		return new DataReader();
	}
	
	public void read(DataWriter writer, LogEntry entry) throws IOException {
		StringBuilder contents = readFile(entry);
				
		writer.write(contents.toString());
		handleDuplicates(writer, entry, contents.toString());
	}
	
	private void handleDuplicates(DataWriter writer, LogEntry entry, String original) throws IOException {
		if (entry.duplicates.isEmpty())
			return;
		
		for (LogEntry duplicate : entry.duplicates) {
			StringBuilder line = readFile(duplicate);
			if (!line.toString().equals(original))
				writer.write(line.toString());
		}
	}
	
	private StringBuilder readFile(LogEntry entry) throws IOException {
		RandomAccessFile ram = new RandomAccessFile(entry.file, "r");
		
		try {
			
		ram.seek(entry.pos);
		
		StringBuilder sb = new StringBuilder(ram.readLine());
		String line;
		while ((line = ram.readLine()) != null) {
			if (line.replaceAll(DATE_PATTERN.pattern(), "").length() < line.length())
				break;
			sb.append(LINE_SEPARATOR);
			sb.append(line);
		}
		
		sb.append(LINE_SEPARATOR);
		return sb;
		
		} finally {
			ram.close();
		}
	}

}
