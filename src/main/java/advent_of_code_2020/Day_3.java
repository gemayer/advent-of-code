package advent_of_code_2020;

import java.util.Arrays;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/3
public class Day_3 {
	public static void main(final String[] args) throws Exception {
		final char[][] map = new DefaultStringParser()
				.scanForCharArrays(new DefaultFileReader().read("2020/day-3_input.txt"));

		final int rows = map.length;
		final int columns = map[0].length;
		System.out.printf("Rows: %d, Columns: %d\n", rows, columns);

		final int[][] slopes = { { 1, 1 }, { 1, 3 }, { 1, 5 }, { 1, 7 }, { 2, 1 } };
		long solution = 1;
		for (final int[] slope : slopes) {
			int iRow = 0, iCol = 0;
			int treeCount = 0;
			final int riseRate = slope[0];
			final int runRate = slope[1];

			System.out.printf("Rise: %d, Run %d\n", riseRate, runRate);
			while (iRow + riseRate < rows) {
				iRow += riseRate;
				iCol = (iCol + runRate) % columns;

				final char[] row = map[iRow];
				final char[] rowSolution = Arrays.copyOf(row, row.length);

				final boolean isTree = row[iCol] == '#';

				if (isTree) {
					rowSolution[iCol] = 'X';
					treeCount++;
				} else {
					rowSolution[iCol] = 'O';
				}

				System.out.printf("%s ||| %s\n", new String(row), new String(rowSolution));
			}
			System.out.printf("Trees: %d\n", treeCount);
			solution *= treeCount;
		}
		System.out.printf("Solution: %d\n", solution);
	}

}
