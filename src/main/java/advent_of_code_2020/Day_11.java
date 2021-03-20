
package advent_of_code_2020;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day1
public class Day_11 {

	private static char EMPTY = 'L';
	private static char OCCUPIED = '#';

	private enum Direction {
		UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), LEFT_UP(-1, -1), RIGHT_UP(1, -1), LEFT_DOWN(-1, 1),
		RIGHT_DOWN(1, 1);

		private final int row, col;

		Direction(final int row, final int col) {
			this.row = row;
			this.col = col;
		}

		public int getRelativeRow() {
			return row;
		}

		public int getRelativeCol() {
			return col;
		}
	}

	public static void main(final String[] args) throws Exception {
		final char[][] map = new DefaultStringParser()
				.scanForCharArrays(new DefaultFileReader().read("2020/day-11_input.txt"));

		doPart1(map);
		doPart2(map);
	}

	private static void doPart1(final char[][] map) {
		char[][] currentMap = clone2DArray(map);
		char[][] nextMap = new char[currentMap.length][currentMap[0].length];
		boolean hasChanges;
		int numberOfOccupiedSeats = 0;
		do {
			hasChanges = false;
			numberOfOccupiedSeats = 0;
			for (int iRow = 0; iRow < currentMap.length; iRow++) {
				for (int iCol = 0; iCol < currentMap[0].length; iCol++) {
					final int numberOfAdjacentOccupiedSeats = getNumberOfAdjacentOccupiedSeats(currentMap, iRow, iCol);
					if (currentMap[iRow][iCol] == OCCUPIED && numberOfAdjacentOccupiedSeats >= 4) {
						nextMap[iRow][iCol] = EMPTY;
						hasChanges = true;
					} else if (currentMap[iRow][iCol] == EMPTY && numberOfAdjacentOccupiedSeats == 0) {
						nextMap[iRow][iCol] = OCCUPIED;
						hasChanges = true;
					} else {
						nextMap[iRow][iCol] = currentMap[iRow][iCol];
					}

					if (nextMap[iRow][iCol] == OCCUPIED) {
						numberOfOccupiedSeats++;
					}
				}
				System.out.println(new String(currentMap[iRow]));
			}

			final char[][] temp = currentMap;
			currentMap = nextMap;
			nextMap = temp;
			System.out.println();
		} while (hasChanges);
		System.out.printf("Part 1 number of occupied seats [%d].\n", numberOfOccupiedSeats);
	}

	private static void doPart2(final char[][] map) {
		char[][] currentMap = clone2DArray(map);
		char[][] nextMap = new char[currentMap.length][currentMap[0].length];
		boolean hasChanges;
		int numberOfOccupiedSeats = 0;
		do {
			hasChanges = false;
			numberOfOccupiedSeats = 0;
			for (int iRow = 0; iRow < currentMap.length; iRow++) {
				for (int iCol = 0; iCol < currentMap[0].length; iCol++) {
					final int numberOfAdjacentOccupiedSeats = getNumberOfAdjacentOccupiedSeatsWithIncreasedLineOfSight(currentMap,
							iRow, iCol);
					if (currentMap[iRow][iCol] == OCCUPIED && numberOfAdjacentOccupiedSeats >= 5) {
						nextMap[iRow][iCol] = EMPTY;
						hasChanges = true;
					} else if (currentMap[iRow][iCol] == EMPTY && numberOfAdjacentOccupiedSeats == 0) {
						nextMap[iRow][iCol] = OCCUPIED;
						hasChanges = true;
					} else {
						nextMap[iRow][iCol] = currentMap[iRow][iCol];
					}

					if (nextMap[iRow][iCol] == OCCUPIED) {
						numberOfOccupiedSeats++;
					}
				}
				System.out.println(new String(currentMap[iRow]));
			}

			final char[][] temp = currentMap;
			currentMap = nextMap;
			nextMap = temp;
			System.out.println();
		} while (hasChanges);
		System.out.printf("Part 2 number of occupied seats [%d].\n", numberOfOccupiedSeats);
	}

	private static int getNumberOfAdjacentOccupiedSeats(final char[][] currentMap, final int row, final int col) {
		int occupied = 0;
		for (final Direction direction : Direction.values()) {
			final int targetRow = row + direction.getRelativeRow();
			final int targetCol = col + direction.getRelativeCol();
			if (targetRow >= 0 && targetRow < currentMap.length && targetCol >= 0 && targetCol < currentMap[0].length
					&& currentMap[targetRow][targetCol] == OCCUPIED) {
				occupied++;
			}
		}
		return occupied;
	}

	private static int getNumberOfAdjacentOccupiedSeatsWithIncreasedLineOfSight(final char[][] currentMap, final int row,
			final int col) {
		int occupied = 0;
		for (final Direction direction : Direction.values()) {
			boolean seatFound = false;
			boolean boundaryFound = false;
			int scale = 1;
			while (!seatFound && !boundaryFound) {
				final int targetRow = row + scale * direction.getRelativeRow();
				final int targetCol = col + scale * direction.getRelativeCol();
				if (targetRow < 0 || targetRow >= currentMap.length || targetCol < 0 || targetCol >= currentMap[0].length) {
					boundaryFound = true;
				} else if (currentMap[targetRow][targetCol] == OCCUPIED) {
					occupied++;
					seatFound = true;
				} else if (currentMap[targetRow][targetCol] == EMPTY) {
					seatFound = true;
				}
				scale++;
			}
		}
		return occupied;
	}

	private static char[][] clone2DArray(final char[][] map) {
		final char[][] clone = new char[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			clone[i] = map[i].clone();
		}
		return clone;
	}
}
