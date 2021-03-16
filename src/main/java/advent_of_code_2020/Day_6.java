package advent_of_code_2020;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/5
public class Day_6 {

	public static void main(final String[] args) throws Exception {
		final List<String> lines = new DefaultStringParser()
				.scanForLines(new DefaultFileReader().read("2020/day-6_input.txt"));

		final List<List<String>> groups = splitByEmptyLine(lines);

		int anyoneSum = 0;
		int everyoneSum = 0;
		for (final List<String> group : groups) {
			final Set<Character> anyoneCharacters = new HashSet<>();
			Set<Character> everyoneCharacters = null;
			for (final String person : group) {
				final Set<Character> characters = person.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
				anyoneCharacters.addAll(characters);

				if (everyoneCharacters == null) {
					everyoneCharacters = new HashSet<>(characters);
				} else {
					final Set<Character> toRemoveCharacters = new HashSet<>();
					for (final Character character : everyoneCharacters) {
						if (!characters.contains(character)) {
							toRemoveCharacters.add(character);
						}
					}
					everyoneCharacters.removeAll(toRemoveCharacters);
				}
			}

			anyoneSum += anyoneCharacters.size();
			everyoneSum += everyoneCharacters.size();
		}
		System.out.printf("Anyone Yes Sum: [%d]\n", anyoneSum);
		System.out.printf("Everyone Yes Sum: [%d]\n", everyoneSum);
	}

	private static List<List<String>> splitByEmptyLine(final List<String> lines) {
		final List<List<String>> lists = new ArrayList<>();
		List<String> list = new ArrayList<>();
		for (final String line : lines) {
			if (StringUtils.isBlank(line) && !list.isEmpty()) {
				lists.add(list);
				list = new ArrayList<>();
			} else {
				list.add(line);
			}
		}
		if (!list.isEmpty()) {
			lists.add(list);
		}
		return lists;
	}
}
