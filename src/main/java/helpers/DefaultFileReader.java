package helpers;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class DefaultFileReader implements InputDataReader {

	@Override
	public byte[] read(final String filename) throws IOException {
		final URL url = getClass().getClassLoader().getResource(filename);
		final File file = new File(url.getPath());
		return FileUtils.readFileToByteArray(file);
	}

}
