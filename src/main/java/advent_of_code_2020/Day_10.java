
package advent_of_code_2020;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/10
public class Day_10 {

	public static void main(final String[] args) throws Exception {
		final List<Integer> numbers = new DefaultStringParser()
				.scanForIntegers(new DefaultFileReader().read("2020/day-10_input.txt"));

		numbers.add(0, 0);
		numbers.sort((i1, i2) -> i1 - i2);
		numbers.add(numbers.size(), numbers.get(numbers.size() - 1) + 3);

		int ones = 0, twos = 0, threes = 0;
		for (int i = 1; i < numbers.size(); i++) {
			final int difference = numbers.get(i) - numbers.get(i - 1);
			if (difference == 1) {
				ones++;
			} else if (difference == 2) {
				twos++;
			} else if (difference == 3) {
				threes++;
			}
		}
		System.out.printf("Differences: 1s [%d], 2s [%d], 3s [%d].\n", ones, twos, threes);
		System.out.printf("Product of 1s [%d] 3s [%d] is [%d].\n", ones, threes, ones * threes);

		System.out.printf("Combinations is [%d].\n", getCombinations(0, numbers, new HashMap<>()));
	}

	private static long getCombinations(final int i, final List<Integer> numbers,
			final Map<Integer, Long> combinationMap) {
		if (combinationMap.containsKey(i)) {
			return combinationMap.get(i);
		}
		if (i == numbers.size() - 1) {
			return 1;
		}
		long combinations = 0;
		for (int j = i + 1; j < Math.min(numbers.size(), i + 4); j++) {
			final long difference = numbers.get(j) - numbers.get(i);
			if (difference <= 3) {
				combinations += getCombinations(j, numbers, combinationMap);
			}
		}
		combinationMap.put(i, combinations);
		return combinations;
	}

}
