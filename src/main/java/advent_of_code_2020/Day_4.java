package advent_of_code_2020;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/3
public class Day_4 {

	@FunctionalInterface
	interface Constraint {
		boolean isValid(String value);
	}

	static class PatternConstraint implements Constraint {

		private final Pattern pattern;

		public PatternConstraint(final String pattern) {
			this.pattern = Pattern.compile(pattern);
		}

		@Override
		public boolean isValid(final String value) {
			return pattern.matcher(value).matches();
		}

	}

	static class EnumerationConstraint implements Constraint {

		private final List<String> validStrings;

		public EnumerationConstraint(final List<String> validStrings) {
			this.validStrings = Collections.unmodifiableList(validStrings);
		}

		@Override
		public boolean isValid(final String value) {
			return validStrings.contains(value);
		}

	}

	static class NumberConstraint implements Constraint {

		private final int length, minimum, maximum;

		public NumberConstraint(final int length, final int minimum, final int maximum) {
			this.length = length;
			this.minimum = minimum;
			this.maximum = maximum;
		}

		@Override
		public boolean isValid(final String value) {
			if (value.length() != length) {
				return false;
			}
			try {
				final int number = Integer.parseInt(value);
				return number >= minimum && number <= maximum;
			} catch (final Exception e) {
				return false;
			}
		}

	}

	static class CompositeOrConstraint implements Constraint {

		private final List<Constraint> constraints;

		public CompositeOrConstraint(final Constraint... constraints) {
			this.constraints = Collections.unmodifiableList(Arrays.asList(constraints));
		}

		@Override
		public boolean isValid(final String value) {
			boolean isValid = false;
			for (final Constraint constraint : constraints) {
				isValid |= constraint.isValid(value);
			}
			return isValid;
		}
	}

	static class HeightConstraint implements Constraint {

		private final int minimum, maximum;
		private final String label;

		public HeightConstraint(final String label, final int minimum, final int maximum) {
			this.label = label;
			this.minimum = minimum;
			this.maximum = maximum;
		}

		@Override
		public boolean isValid(final String value) {
			if (!value.endsWith(label)) {
				return false;
			}
			try {
				final int number = Integer.parseInt(value.split(label)[0]);
				return number >= minimum && number <= maximum;
			} catch (final Exception e) {
				return false;
			}
		}

	}

	public static void main(final String[] args) throws Exception {
		final List<String> lines = new DefaultStringParser()
				.scanForLines(new DefaultFileReader().read("2020/day-4_input.txt"));

		final List<String> entries = combineLinesToEntries(lines);

		final Map<String, Constraint> requiredFieldConstraintMap = new HashMap<>();
		requiredFieldConstraintMap.put("byr", new NumberConstraint(4, 1920, 2002));
		requiredFieldConstraintMap.put("iyr", new NumberConstraint(4, 2010, 2020));
		requiredFieldConstraintMap.put("eyr", new NumberConstraint(4, 2020, 2030));
		requiredFieldConstraintMap.put("hgt",
				new CompositeOrConstraint(new HeightConstraint("cm", 150, 193), new HeightConstraint("in", 59, 76)));
		requiredFieldConstraintMap.put("hcl", new PatternConstraint("^#[0-9a-f]{6}$"));
		requiredFieldConstraintMap.put("ecl",
				new EnumerationConstraint(Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth")));
		requiredFieldConstraintMap.put("pid", new PatternConstraint("^[0-9]{9}$"));

		// List<String> optionalFields = Arrays.asList("cid");
		int part1_isValidCount = 0;
		int part2_isValidCount = 0;
		for (final String entry : entries) {

			final Map<String, String> pairs = new HashMap<>();
			for (final String pair : entry.split("\\s")) {
				final String key = pair.split(":")[0];
				final String value = pair.split(":")[1];
				pairs.put(key, value);
			}

			boolean part1_isValid = true;
			boolean part2_isValid = true;
			for (final Map.Entry<String, Constraint> requiredFieldConstraint : requiredFieldConstraintMap.entrySet()) {
				final String field = requiredFieldConstraint.getKey();
				if (!pairs.containsKey(field)) {
					part1_isValid = false;
					part2_isValid = false;
				} else if (!requiredFieldConstraint.getValue().isValid(pairs.get(field))) {
					part2_isValid = false;
				}
				if (!part1_isValid && !part2_isValid) {
					break;
				}
			}

			if (part1_isValid && part2_isValid) {
				part1_isValidCount++;
				part2_isValidCount++;
			} else if (part1_isValid) {
				part1_isValidCount++;
			}
		}
		System.out.printf("Part 1 Valid Count: %d\n", part1_isValidCount);
		System.out.printf("Part 2 Valid Count: %d\n", part2_isValidCount);
	}

	private static List<String> combineLinesToEntries(final List<String> lines) {
		final List<String> entries = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (final String line : lines) {
			if (isBlank(line) && !sb.isEmpty()) {
				entries.add(sb.toString().trim());
				sb = new StringBuilder();
			} else {
				sb.append(" " + line);
			}
		}
		if (!sb.isEmpty()) {
			entries.add(sb.toString().trim());
		}
		return entries;
	}

}
