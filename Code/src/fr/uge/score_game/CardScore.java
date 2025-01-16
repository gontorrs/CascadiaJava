package fr.uge.score_game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fr.uge.DataGame.Animal;
import fr.uge.DataGame.GameLogic;
import fr.uge.DataGame.GamingBoard;
import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;

public final class CardScore implements ScoreRule {

	private final ScoreCard scoreCard;
	private GameLogic gl;
	public CardScore(ScoreCard scoreCard) {
		this.scoreCard = Objects.requireNonNull(scoreCard);
		gl = new GameLogic();
	}

	@Override
	public int calculateScore(GamingBoard gb) {
		Objects.requireNonNull(gb);
		int score = 0;
		score += switch (scoreCard) {
		case A -> calculateBearScoreA(gb) + calculateSalmonScoreA(gb) + calculateElkScoreA(gb)
				+ calculateEagleScoreA(gb) + calculateFoxScoreA(gb);
		case B -> calculateBearScoreB(gb) + calculateSalmonScoreB(gb) + calculateElkScoreB(gb)
				+ calculateEagleScoreB(gb) + calculateFoxScoreB(gb);
		case C -> calculateBearScoreC(gb) + calculateSalmonScoreC(gb) + calculateElkScoreC(gb)
				+ calculateEagleScoreC(gb) + calculateFoxScoreC(gb);
		case D -> calculateBearScoreD(gb) + calculateSalmonScoreD(gb) + calculateElkScoreD(gb)
				+ calculateEagleScoreD(gb) + calculateFoxScoreD(gb);
		};
		return score;
	}

	private int calculateBearScoreA(GamingBoard gb) {
		Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
		List<List<Position>> bearGroups = animalGroups.getOrDefault(Animal.BEAR, List.of());
		return (int) bearGroups.stream().filter(group -> group.size() == 2) // Example: Scoring pairs of bears
				.count() * 5; // Example: 5 points per pair
	}

	private int calculateBearScoreB(GamingBoard gb) {
		Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
		List<List<Position>> bearGroups = animalGroups.getOrDefault(Animal.BEAR, List.of());
		return (int) bearGroups.stream().filter(group -> group.size() == 3) // Example: Scoring groups of exactly 3 //
																			// bears
				.count() * 8; // Example: 8 points per group of 3
	}

	private int calculateBearScoreC(GamingBoard gb) {
		Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
		List<List<Position>> bearGroups = animalGroups.getOrDefault(Animal.BEAR, List.of());
		int score = 0;
		boolean[] groupFlags = new boolean[3]; // Index 0 -> size 1, 1 -> size 2, 2 -> size 3
		for (List<Position> group : bearGroups) {
			score += calculateGroupPoints(group, groupFlags);
		}
		if (groupFlags[0] && groupFlags[1] && groupFlags[2]) {
			score += 3; // Bonus for having at least one group of each size
		}
		return score;
	}

	private int calculateGroupPoints(List<Position> group, boolean[] groupFlags) {
		int size = group.size();
		if (size == 1) {
			groupFlags[0] = true;
			return 3;
		} else if (size == 2) {
			groupFlags[1] = true;
			return 5;
		} else if (size == 3) {
			groupFlags[2] = true;
			return 8;
		}
		return 0;
	}

	private int calculateBearScoreD(GamingBoard gb) {
		Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
		List<List<Position>> bearGroups = animalGroups.getOrDefault(Animal.BEAR, List.of());
		return bearGroups.stream().filter(group -> group.size() >= 2 && group.size() <= 4) // Only groups of size 2, 3,
																							// or 4
				.mapToInt(group -> group.size() * 4) // Example: 4 points per bear in valid groups
				.sum();
	}

	private int calculateSalmonScoreA(GamingBoard gb) {
	    Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
	    List<List<Position>> salmonGroups = animalGroups.getOrDefault(Animal.SALMON, List.of());
	    return salmonGroups.stream()
	            .mapToInt(group -> Math.min(group.size(), 7)) // Up to 7 points.
	            .sum();
	}

	private int calculateSalmonScoreB(GamingBoard gb) {
	    Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
	    List<List<Position>> salmonGroups = animalGroups.getOrDefault(Animal.SALMON, List.of());
	    return salmonGroups.stream()
	            .mapToInt(group -> Math.min(group.size(), 5)) // Up to 5 points.
	            .sum();
	}

	private int calculateSalmonScoreC(GamingBoard gb) {
	    Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
	    List<List<Position>> salmonGroups = animalGroups.getOrDefault(Animal.SALMON, List.of());
	    return salmonGroups.stream()
	            .filter(group -> group.size() >= 3)
	            .mapToInt(group -> Math.min(group.size(), 5)) // Up to 7 points with a minimum of 3 in a group.
	            .sum();
	}

	private int calculateSalmonScoreD(GamingBoard gb) {
	    Map<Animal, List<List<Position>>> animalGroups = groupAnimals(gb);
	    List<List<Position>> salmonGroups = animalGroups.getOrDefault(Animal.SALMON, List.of());
	    return salmonGroups.stream()
	            .filter(group -> group.size() >= 3)
	            .mapToInt(group -> group.size() + countAdjacentFaunaTokens(gb, group)) // 1 point per salmon plus one per adjacent tile.
	            .sum();
	}
	
	private int calculateEagleScoreA(GamingBoard gb) {
		return 0;
	}

	private int calculateEagleScoreB(GamingBoard gb) {
		return 0;
	}

	private int calculateEagleScoreC(GamingBoard gb) {
		return 0;
	}

	private int calculateEagleScoreD(GamingBoard gb) {
		return 0;
	}

	private int calculateElkScoreA(GamingBoard gb) {
		return 0;
	}

	private int calculateElkScoreB(GamingBoard gb) {
		return 0;
	}

	private int calculateElkScoreC(GamingBoard gb) {
		return 0;
	}

	private int calculateElkScoreD(GamingBoard gb) {
		return 0;
	}

	private int calculateFoxScoreA(GamingBoard gb) {
		return 0;
	}

	private int calculateFoxScoreB(GamingBoard gb) {
		return 0;
	}

	private int calculateFoxScoreC(GamingBoard gb) {
		return 0;
	}

	private int calculateFoxScoreD(GamingBoard gb) {
		return 0;
	}

	private Map<Animal, List<List<Position>>> groupAnimals(GamingBoard gb) {
		Map<Animal, List<List<Position>>> groupedAnimals = new HashMap<>();
		gb.getBoardMap().forEach((position, tile) -> {
			for (Animal animal : tile.animalList()) {
				List<Position> group = findGroup(position, animal, gb);
				groupedAnimals.computeIfAbsent(animal, k -> new ArrayList<>()).add(group);
			}
		});
		return groupedAnimals;
	}

	private List<Position> findGroup(Position start, Animal animal, GamingBoard gb) {
		List<Position> visited = new ArrayList<>();
		List<Position> stack = new ArrayList<>();
		stack.add(start);
		while (!stack.isEmpty()) {
			Position current = stack.remove(stack.size() - 1);
			if (visited.contains(current)) {
				continue;
			}
			var tile = gb.getBoardMap().get(current);
			if (tile != null && tile.animalList().contains(animal)) {
				visited.add(current);
				stack.addAll(Arrays.asList(gl.adjacentPos(current)));
			}
		}
		return visited;
	}
	private int countAdjacentFaunaTokens(GamingBoard gb, List<Position> group) {
	    Objects.requireNonNull(gb);
	    Objects.requireNonNull(group);
	    Map<Position, Tile> boardMap = gb.getBoardMap();
	    Set<Position> countedPositions = new HashSet<>();
	    for (Position pos : group) {
	        Position[] adjacentPositions = gl.adjacentPos(pos);
	        for (Position adjPos : adjacentPositions) {
	            Tile adjTile = boardMap.get(adjPos);
	            if (adjTile != null && adjTile.userTile() != 3) {
	                countedPositions.add(adjPos);
	            }
	        }
	    }
	    return countedPositions.size();
	}


}
