
package advent_of_code_2020;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/8
public class Day_8 {

	private enum Operation {
		NOP, ACC, JMP
	}

	public static class Command {
		private final int lineNumber;
		private final Operation operation;
		private final int amount;

		public Command(final int lineNumber, final Operation operation, final int amount) {
			this.lineNumber = lineNumber;
			this.operation = operation;
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

		public Operation getOperation() {
			return operation;
		}

		public int getCommandLine() {
			return lineNumber;
		}

		public int accumulate(final int current) {
			if (Operation.ACC.equals(operation)) {
				return current + amount;
			}
			return current;
		}

		public int getNextCommandLine() {
			if (Operation.JMP.equals(operation)) {
				return lineNumber + amount;
			}
			return lineNumber + 1;
		}

		@Override
		public int hashCode() {
			return Objects.hash(lineNumber);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Command other = (Command) obj;
			if (lineNumber != other.lineNumber) {
				return false;
			}
			return true;
		}

	}

	public static void main(final String[] args) throws Exception {
		final List<String> lines = new DefaultStringParser()
				.scanForLines(new DefaultFileReader().read("2020/day-8_input.txt"));

		final List<Command> commands = parseCommands(lines);

		doPart1(commands);

		final List<Set<Command>> circuits = new ArrayList<>();
		final List<Integer> visited = new ArrayList<>();
		Set<Command> candidateCircuit = null;
		Set<Command> startingCircuit = null, endingCircuit = null;
		for (int currentLine = 0; currentLine < commands.size(); currentLine++) {
			if (!visited.contains(currentLine)) {
				visited.add(currentLine);

				final Command currentCommand = commands.get(currentLine);
				int nextLine = currentCommand.getNextCommandLine();
				Command nextCommand = nextLine < commands.size() ? commands.get(nextLine) : null;

				candidateCircuit = new HashSet<>();
				candidateCircuit.add(currentCommand);
				while (!visited.contains(nextLine) && nextLine < commands.size()) {
					visited.add(nextLine);
					nextCommand = commands.get(nextLine);
					candidateCircuit.add(nextCommand);
					nextLine = nextCommand.getNextCommandLine();
					nextCommand = nextLine < commands.size() ? commands.get(nextLine) : null;
				}

				if (nextCommand == null) {
					if (candidateCircuit.contains(commands.get(0))) {
						startingCircuit = candidateCircuit;
					}
					if (nextLine == commands.size()) {
						endingCircuit = candidateCircuit;
					}
					circuits.add(candidateCircuit);
				} else if (candidateCircuit.contains(nextCommand)) {
					if (candidateCircuit.contains(commands.get(0))) {
						startingCircuit = candidateCircuit;
					}
					circuits.add(candidateCircuit);
				} else {
					for (final Set<Command> circuit : circuits) {
						if (circuit.contains(nextCommand)) {
							circuit.addAll(candidateCircuit);
							break;
						}
					}
				}
			}
		}
		System.out.println(commands.size());
		System.out.println(startingCircuit.size());
		System.out.println(endingCircuit.size());
		System.out.println(circuits.size());

		int sum = 0;
		Command command = commands.get(0);
		while (command != null) {
			sum = command.accumulate(sum);
			int nextLine = command.getNextCommandLine();
			if (startingCircuit.contains(command) && Operation.NOP.equals(command.getOperation())
					&& endingCircuit.contains(commands.get(command.getCommandLine() + command.getAmount()))) {
				nextLine = command.getCommandLine() + command.getAmount();
			} else if (startingCircuit.contains(command) && Operation.JMP.equals(command.getOperation())
					&& endingCircuit.contains(commands.get(command.getCommandLine() + 1))) {
				nextLine = command.getCommandLine() + 1;
			}
			command = nextLine < commands.size() ? commands.get(nextLine) : null;
		}
		System.out.printf("Found program with sum [%d].\n", sum);
	}

	private static void doPart1(final List<Command> commands) {
		final Set<Integer> history = new HashSet<>();
		int sum = 0;
		int nextCommand = 0;
		while (nextCommand < commands.size()) {
			if (history.contains(nextCommand)) {
				System.out.printf("Found loop at index [%d] with sum [%d].\n", nextCommand, sum);
				return;
			}
			history.add(nextCommand);
			final Command command = commands.get(nextCommand);
			nextCommand = command.getNextCommandLine();
			sum = command.accumulate(sum);
		}
	}

	private static List<Command> parseCommands(final List<String> lines) {
		final Pattern pattern = Pattern.compile("^(.*) +([\\+|-]{1})([0-9]*)$");
		final List<Command> commands = new ArrayList<>();
		int lineNumber = 0;
		for (final String line : lines) {
			final Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				final Operation operation = Operation.valueOf(matcher.group(1).toUpperCase());
				final String sign = matcher.group(2);
				final int amount = Integer.parseInt(matcher.group(3));
				if (sign.equals("+")) {
					commands.add(new Command(lineNumber++, operation, amount));
				} else {
					commands.add(new Command(lineNumber++, operation, amount * -1));
				}
			}
		}
		return commands;
	}

}
