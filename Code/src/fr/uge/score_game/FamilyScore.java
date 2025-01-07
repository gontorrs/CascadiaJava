package fr.uge.score_game;
import fr.uge.DataGame.*;
import java.util.*;

public final class FamilyScore implements ScoreRule {
	
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
				stack.addAll(Arrays.asList(gb.adjacentPos(current)));  // convertir un tableau en liste avant de l'ajouter au stack 
			}
		}
		return visited;
	}

	private int calculateGroupScore(Set<Position> group) {
		int size = group.size();
		switch (size) {
		case 1:
			return 2;
		case 2:
			return 5;
		default:
			return 9;
		}
	}

}
