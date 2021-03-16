package helpers;

import java.util.List;

public interface StringParser {

	List<Integer> scanForIntegers(byte[] data);

	List<String> scanForLines(byte[] data);

	char[][] scanForCharArrays(byte[] data);

	byte[][] scanForByteArrays(byte[] data);

}
