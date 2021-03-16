package advent_of_code_2020;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/2
public class Day_2 {
	public static void main(final String[] args) throws Exception {
		final List<String> lines = new DefaultStringParser()
				.scanForLines(new DefaultFileReader().read("2020/day-2_input.txt"));

		final Pattern pattern = Pattern.compile("(.*)-(.*) (.{1}): (.*)");
		int part1_isValidCount = 0, part2_isValidCount = 0;
		for (final String line : lines) {
			final Matcher matcher = pattern.matcher(line);
			matcher.matches();

			final int minimum = Integer.parseInt(matcher.group(1));
			final int maximum = Integer.parseInt(matcher.group(2));
			final byte character = matcher.group(3).getBytes()[0];
			final String password = matcher.group(4);
			final int count = findByteCount(character, password);

			final boolean part1_IsValid = count >= minimum && count <= maximum;
			if (part1_IsValid) {
				part1_isValidCount++;
			}

			final int firstIndex = Integer.parseInt(matcher.group(1)) - 1;
			final int secondIndex = Integer.parseInt(matcher.group(2)) - 1;
			final boolean isFirstIndexEquals = isByteEquals(character, firstIndex, password);
			final boolean isSecondIndexEquals = isByteEquals(character, secondIndex, password);
			final boolean part2_IsValid = isFirstIndexEquals ^ isSecondIndexEquals;
			if (part2_IsValid) {
				part2_isValidCount++;
			}

			System.out.printf(
					"Password [%s] is part 1 [%s], part 2 [%s] with character [%c], min/first [%d], max/second [%d].\n", password,
					part1_IsValid, part2_IsValid, character, minimum, maximum);
		}
		System.out.printf("Part #1: Valid count: %d\n", part1_isValidCount);
		System.out.printf("Part #2: Valid count: %d\n", part2_isValidCount);
	}

	private static int findByteCount(final byte target, final String password) {
		int count = 0;
		for (final byte candidate : password.getBytes()) {
			if (candidate == target) {
				count++;
			}
		}
		return count;
	}

	private static boolean isByteEquals(final byte target, final int index, final String password) {
		return password.getBytes()[index] == target;
	}
}
