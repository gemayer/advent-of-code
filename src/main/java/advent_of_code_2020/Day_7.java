
package advent_of_code_2020;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import helpers.DefaultFileReader;
import helpers.DefaultStringParser;

// https://adventofcode.com/2020/day/7
public class Day_7 {

	public static class Edges {
		private final Set<Edge> containedBy = new HashSet<>(), contains = new HashSet<>();

		public void addContainedBy(final String bagName) {
			containedBy.add(new Edge(bagName, 0));
		}

		public void addContains(final String bagName, final int count) {
			contains.add(new Edge(bagName, count));
		}

		public Set<Edge> getContainedBy() {
			return containedBy;
		}

		public Set<Edge> getContains() {
			return contains;
		}

	}

	public static class Edge {
		private final String bagName;
		private final int count;

		public Edge(final String bagName, final int count) {
			this.bagName = bagName;
			this.count = count;
		}

		public String getBagName() {
			return bagName;
		}

		public int getCount() {
			return count;
		}

		@Override
		public int hashCode() {
			return Objects.hash(bagName);
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
			final Edge other = (Edge) obj;
			if (!Objects.equals(bagName, other.bagName)) {
				return false;
			}
			return true;
		}

	}

	public static void main(final String[] args) throws Exception {
		final List<String> lines = new DefaultStringParser()
				.scanForLines(new DefaultFileReader().read("2020/day-7_input.txt"));

		final Map<String, Edges> graph = new HashMap<>();
		for (final String line : lines) {
			final String[] tokens = line.split("\\s");

			final String bagName = String.join(" ", tokens[0], tokens[1]);
			graph.putIfAbsent(bagName, new Edges());
			for (int iToken = 3; iToken < tokens.length; iToken++) {
				if (tokens[iToken].startsWith("bag")) {
					final String containsBagName = String.join(" ", tokens[iToken - 2], tokens[iToken - 1]);
					if (!containsBagName.equals("no other")) {
						final int count = Integer.parseInt(tokens[iToken - 3]);
						graph.get(bagName).addContains(containsBagName, count);
						graph.putIfAbsent(containsBagName, new Edges());
						graph.get(containsBagName).addContainedBy(bagName);
					}
				}
			}
		}

		final String targetBag = "shiny gold";
		final Set<String> bagsThatContainTargetBag = new HashSet<>();
		markBagsThatContainTargetBags(graph, targetBag, bagsThatContainTargetBag);
		System.out.printf("Number of bags: [%s].\n", bagsThatContainTargetBag.size() - 1);

		final int bagsInside = getNumberOfBagsContainedInsideTarget(graph, targetBag);
		System.out.printf("Number of bags inside: [%s].\n", bagsInside - 1);
	}

	private static void markBagsThatContainTargetBags(final Map<String, Edges> graph, final String target,
			final Set<String> markedBags) {
		markedBags.add(target);
		for (final Edge containBy : graph.get(target).getContainedBy()) {
			if (!markedBags.contains(containBy.getBagName())) {
				markBagsThatContainTargetBags(graph, containBy.getBagName(), markedBags);
			}
		}
	}

	private static int getNumberOfBagsContainedInsideTarget(final Map<String, Edges> graph, final String target) {
		int size = 1;
		for (final Edge contain : graph.get(target).getContains()) {
			size += contain.getCount() * getNumberOfBagsContainedInsideTarget(graph, contain.getBagName());
		}
		return size;
	}
}
