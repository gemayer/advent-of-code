package advent_of_code_2020;

import java.util.List;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/5
public class Day_5 {

	private static final char BACK = 'B';
	private static final char RIGHT = 'R';

	public static void main(final String[] args) throws Exception {
		final List<String> lines = new DefaultStringParser()
				.scanForLines(new DefaultFileReader().read("2020/day-5_input.txt"));

		int maxSeatId = 0;
		final boolean[] seatsOccupied = new boolean[1024];
		for (final String boardingPass : lines) {
			int seatId = 0;
			int row = 0;
			int seat = 0;
			for (int i = 0; i < boardingPass.length(); i++) {
				final int bitLocation = 0x01 << boardingPass.length() - 1 - i;
				if (boardingPass.charAt(i) == BACK) {
					seatId |= bitLocation;
					row |= bitLocation;
				} else if (boardingPass.charAt(i) == RIGHT) {
					seatId |= bitLocation;
					seat |= bitLocation;
				}
			}
			seatsOccupied[seatId] = true;

			if (maxSeatId < seatId) {
				maxSeatId = seatId;
			}

			System.out.printf("Boarding Pass: [%s], Row [%s], Seat [%s], Seat ID [%d], Seat ID Binary [%s].\n", boardingPass,
					row, seat, seatId, Integer.toBinaryString(seatId));
		}
		System.out.printf("Max Seat ID: %s\n", maxSeatId);

		for (int i = 1; i < seatsOccupied.length - 1; i++) {
			if (!seatsOccupied[i] && seatsOccupied[i - 1] && seatsOccupied[i + 1]) {
				System.out.printf("May Seat is: %s", i);
			}
		}

	}

}
