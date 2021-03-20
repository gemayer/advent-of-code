
package advent_of_code_2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/9
public class Day_9 {

	public static void main(final String[] args) throws Exception {
		final List<Long> numbers = new DefaultStringParser()
				.scanForLongs(new DefaultFileReader().read("2020/day-9_input.txt"));

		final int preambleSize = 25;

		long invalidNumber = 0;
		final List<Long> preambleIntegers = new ArrayList<>();
		final Map<Long, Long> sumCountMap = new HashMap<>();
		final List<Long> continuousSums = new ArrayList<>();
		for (int i = 0; i < numbers.size(); i++) {
			final long candidate = numbers.get(i);
			if (i > preambleSize && !sumCountMap.containsKey(candidate)) {
				System.out.printf("Found invalid number: [%d].\n", candidate);
				invalidNumber = candidate;
			}

			if (i > preambleSize) {
				final long toBeRemoved = preambleIntegers.remove(0);
				for (final long preambleInteger : preambleIntegers) {
					final long sum = toBeRemoved + preambleInteger;
					final long count = sumCountMap.get(sum);
					if (count <= 1) {
						sumCountMap.remove(sum);
					} else {
						sumCountMap.put(sum, count - 1);
					}
				}
			}

			for (final long preambleInteger : preambleIntegers) {
				final long sum = candidate + preambleInteger;
				sumCountMap.putIfAbsent(sum, 0L);
				sumCountMap.put(sum, sumCountMap.get(sum) + 1);
			}
			preambleIntegers.add(candidate);

			if (i > 0) {
				continuousSums.add(continuousSums.get(i - 1) + candidate);
			} else {
				continuousSums.add(candidate);
			}
		}

		for (int i = 0; i < continuousSums.size(); i++) {
			final long continuousSum = continuousSums.get(i);
			for (int j = 0; j < i; j++) {
				final long difference = continuousSum - continuousSums.get(j);
				if (difference == invalidNumber) {
					long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
					for (int k = j + 1; k < i; k++) {
						max = Math.max(max, numbers.get(k));
						min = Math.min(min, numbers.get(k));
					}
					System.out.printf(
							"Found invalid number [%d] as continuous sum from index, number [%d, %d] to [%d, %d] with min [%d], max [%d], sum [%d].\n",
							invalidNumber, j + 1, numbers.get(j + 1), i, numbers.get(i), min, max, min + max);
				}
			}

		}
	}

}
