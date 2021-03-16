package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DefaultStringParser implements StringParser {

	@Override
	public List<Integer> scanForIntegers(final byte[] data) {
		final Scanner s = new Scanner(new String(data));
		final List<Integer> list = new ArrayList<>();
		while (s.hasNext()) {
			list.add(s.nextInt());
		}
		return list;
	}

	@Override
	public List<String> scanForLines(final byte[] data) {
		final Scanner s = new Scanner(new String(data));
		final List<String> list = new ArrayList<>();
		while (s.hasNextLine()) {
			list.add(s.nextLine());
		}
		return list;
	}

	@Override
	public char[][] scanForCharArrays(final byte[] read) {
		final List<String> list = scanForLines(read);
		final int rows = list.size();
		final int columns = list.get(0).length();
		final char[][] chars = new char[rows][columns];
		for (int iRow = 0; iRow < rows; iRow++) {
			final String row = list.get(iRow);
			for (int iCol = 0; iCol < row.getBytes().length; iCol++) {
				chars[iRow][iCol] = row.charAt(iCol);
			}
		}
		return chars;
	}

	@Override
	public byte[][] scanForByteArrays(final byte[] read) {
		final List<String> list = scanForLines(read);
		final int rows = list.size();
		final int columns = list.get(0).length();
		final byte[][] bytes = new byte[rows][columns];
		for (int iRow = 0; iRow < rows; iRow++) {
			final String row = list.get(iRow);
			for (int iCol = 0; iCol < row.getBytes().length; iCol++) {
				bytes[iRow][iCol] = row.getBytes()[iCol];
			}
		}
		return bytes;
	}

}
