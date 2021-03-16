package advent_of_code_2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/1
public class Day_1 {
	public static void main(final String[] args) throws Exception {
		final List<Integer> integers = new DefaultStringParser()
				.scanForIntegers(new DefaultFileReader().read("2020/day-1_input.txt"));

		final int TARGET_SUM = 2020;
		final Set<Integer> toLookForNumbers = new HashSet<>();
		for (final Integer candidate : integers) {
			if (toLookForNumbers.contains(candidate)) {
				final int otherCandidate = TARGET_SUM - candidate;
				final int sum = otherCandidate + candidate;
				final int product = otherCandidate * candidate;
				System.out.printf("1a: Answer is %d + %d = %d && %d * %d = %d\n", otherCandidate, candidate, sum,
						otherCandidate, candidate, product);
			} else {
				toLookForNumbers.add(TARGET_SUM - candidate);
			}
		}

		final Map<Integer, List<Set<Integer>>> map = new HashMap<>();
		for (final Integer candidate : integers) {

			final Map<Integer, List<Set<Integer>>> mapUpdates = new HashMap<>();
			for (final Map.Entry<Integer, List<Set<Integer>>> entry : map.entrySet()) {
				if (entry.getKey() >= candidate) {
					for (final Set<Integer> candidateSolution : entry.getValue()) {
						final Set<Integer> newPotentialSolution = new HashSet<>(candidateSolution);
						newPotentialSolution.add(candidate);
						final int differnce = TARGET_SUM - newPotentialSolution.stream().mapToInt(Integer::intValue).sum();
						if (differnce > 0 && candidateSolution.size() <= 2) {
							mapUpdates.putIfAbsent(differnce, new ArrayList<>());
							mapUpdates.get(differnce).add(newPotentialSolution);
						} else if (differnce == 0 && newPotentialSolution.size() == 3) {
							final int[] arr = newPotentialSolution.stream().filter(Objects::nonNull).mapToInt(Integer::intValue)
									.toArray();
							final int sum = arr[0] + arr[1] + arr[2];
							final int product = arr[0] * arr[1] * arr[2];
							System.out.printf("1b: Answer is %d + %d + %d = %d && %d * %d * %d = %d\n", arr[0], arr[1], arr[2], sum,
									arr[0], arr[1], arr[2], product);
						}
					}
				}
			}

			for (final Map.Entry<Integer, List<Set<Integer>>> mapUpdate : mapUpdates.entrySet()) {
				map.putIfAbsent(mapUpdate.getKey(), new ArrayList<Set<Integer>>());
				map.get(mapUpdate.getKey()).addAll(mapUpdate.getValue());
			}

			final int difference = TARGET_SUM - candidate;
			if (!map.containsKey(difference)) {
				final List<Set<Integer>> candidateSolution = new ArrayList<>();
				candidateSolution.add(new HashSet<>(Arrays.asList(candidate)));
				map.put(difference, candidateSolution);
			}
		}
	}
}
