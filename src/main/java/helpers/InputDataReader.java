package helpers;

import java.io.IOException;

public interface InputDataReader {

	byte[] read(String uri) throws IOException;
}
