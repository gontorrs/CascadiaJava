package fr.uge.DataGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Tile(List<Animal> animalList, List<Habitat> habitatList, int userTile) { // *****
	public Tile {
		Objects.requireNonNull(animalList);
		Objects.requireNonNull(habitatList);
		if (userTile < 0) {
			throw new IllegalArgumentException("");
		}
	}

	private int maxLength(List<String> items) {
		return items.stream().mapToInt(String::length).max().orElse(0);
	}

	private String generateLine(char character, int count) {
		return new String(new char[count]).replace('\0', character);
	}

	private String print(List<String> items) {
		StringBuilder sb = new StringBuilder();
		int maxLength = maxLength(items);
		int width = maxLength + 4;
		sb.append("+").append(generateLine('-', width - 2)).append("+\n");
		items.forEach(item -> {
			int padding = (width - 4) - item.length();
			sb.append("| ").append(item).append(generateLine(' ', padding)).append(" |\n");
		});
		sb.append("+").append(generateLine('-', width - 2)).append("+");
		return sb.toString();
	}

	@Override
	public String toString() {
		if (animalList.isEmpty() && habitatList.isEmpty()) {
			return "+--------+\n|        |\n+--------+";
		}
		List<String> animalItems = animalList.stream().map(Object::toString).collect(Collectors.toList());
		List<String> habitatItems = habitatList.stream().map(Object::toString).collect(Collectors.toList());
		List<String> items = new ArrayList<>();
		items.addAll(animalItems);
		items.addAll(habitatItems);
		List<String> processedItems = IntStream.range(0, items.size())
				.mapToObj(index -> userTile == index && userTile != 3 ? "U:" + items.get(index) : items.get(index))
				.collect(Collectors.toList());
		return print(processedItems);
	}

}