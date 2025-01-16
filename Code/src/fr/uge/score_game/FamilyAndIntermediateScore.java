package fr.uge.score_game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.uge.DataGame.Animal;
import fr.uge.DataGame.Difficulty;
import fr.uge.DataGame.GameLogic;
import fr.uge.DataGame.GamingBoard;
import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;

public final class FamilyAndIntermediateScore implements ScoreRule {

	private final Difficulty difficulty;
	private GameLogic gl;
	public FamilyAndIntermediateScore(Difficulty difficulty) {
		this.difficulty = Objects.requireNonNull(difficulty);
		gl = new GameLogic();
	}

	@Override
	public int calculateScore(GamingBoard gb) {
		Objects.requireNonNull(gb, "Board must not be null");
		Map<Animal, List<Set<Position>>> animalGroups = groupAnimalsBySpecies(gb);
		return animalGroups.values().stream().flatMap(group -> group.stream())
				.mapToInt(group -> calculateGroupScore(group)).sum();
	}

	private Map<Animal, List<Set<Position>>> groupAnimalsBySpecies(GamingBoard gb) {
		Map<Animal, List<Set<Position>>> groupedAnimals = new HashMap<>();
		gb.getBoardMap().forEach((pos, tile) -> tile.animalList().forEach(animal -> groupedAnimals
				.computeIfAbsent(animal, k -> new ArrayList<>()).add(findGroup(pos, animal, gb))));
		return groupedAnimals;
	}

	private Set<Position> findGroup(Position start, Animal species, GamingBoard gb) {
		Set<Position> visited = new HashSet<>();
		Deque<Position> stack = new ArrayDeque<>();
		stack.push(start);
		while (!stack.isEmpty()) {
			Position current = stack.pop();
			if (visited.contains(current))
				continue;
			Tile tile = gb.getBoardMap().get(current);
			if (tile != null && tile.animalList().stream().anyMatch(a -> a.name().equals(species))) {
				visited.add(current);
				stack.addAll(Arrays.asList(gl.adjacentPos(current)));
			}
		}
		return visited;
	}

	private int calculateGroupScore(Set<Position> group) {
		int size = group.size();
		return switch (difficulty) {
		case FAMILY -> calculateFamilyScore(size);
		case INTERMEDIATE -> calculateIntermediateScore(size);
		};
	}

	private int calculateFamilyScore(int size) {
		return switch (size) {
		case 1 -> 2;
		case 2 -> 5;
		default -> 9;
		};
	}

	private int calculateIntermediateScore(int size) {
		return switch (size) {
		case 2 -> 5;
		case 3 -> 8;
		default -> 12;
		};
	}
}
