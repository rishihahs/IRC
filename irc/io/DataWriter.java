package irc.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataWriter {
	
	private static final Lock lock = new ReentrantLock();
	
	private final FileOutputStream stream;
	private final FileChannel channel;
	private final ByteBuffer buffer;
	
	public DataWriter(File file) throws IOException {
		stream = new FileOutputStream(file);
		channel = stream.getChannel();
		buffer = ByteBuffer.allocateDirect(1024);
	}
	
	public void write(final String string) throws IOException {
		lock.lock();
		
		try {
		
		byte[] data = string.getBytes(Charset.forName("UTF-8"));

		int read = 0;
		while (read < data.length) {
			while (buffer.position() < buffer.capacity() && read < data.length)
				buffer.put(data[read++]);
			
			buffer.flip();
			channel.write(buffer);
			buffer.clear();
		}
			
		} finally {
			lock.unlock();
		}
	}
	
	public void close() throws IOException {
		channel.close();
		stream.close();
	}

}
