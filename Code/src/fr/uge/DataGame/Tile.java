package fr.uge.DataGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
//This tile record holds the content of each tile, animals, habitat, if the user puts the tile or not and visibility.
public record Tile(List<Animal> animalList, List<Habitat> habitatList, int userTile, boolean visibility) {
    public Tile {
        Objects.requireNonNull(animalList);
        Objects.requireNonNull(habitatList);
        if (userTile < 0) {
            throw new IllegalArgumentException("User Tile must be greater than 0.");
        }
    }
    public Tile(List<Animal> animalList, List<Habitat> habitatList, int userTile) {
        this(animalList, habitatList, userTile, false);
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
    public Tile hide() {
        return new Tile(this.animalList, this.habitatList, this.userTile, false);
    }

    public Tile show() {
        return new Tile(this.animalList, this.habitatList, this.userTile, true);
    }

    public Boolean visible() {
        return this.visibility;
    }
    public int animalToInt(String animal) {
        switch(animal.toUpperCase()) {
            case "BEAR":
                return 0;
            case "ELK":
                return 1;
            case "FOX":
                return 2;
            case "EAGLE":
                return 3;
            case "SALMON":
                return 4;
            default:
                return -1;
        }
    }

    public int habitatToInt(String habitat) {
        switch(habitat.toUpperCase()) {
            case "MOUNTAIN":
                return 0;
            case "FOREST":
                return 1;
            case "RIVER":
                return 2;
            case "GRASSLAND":
                return 3;
            case "WETLAND":
                return 4;
            default:
                return -1;
        }
    }


    public int idAnimal() {
        if (animalList.isEmpty()) {
            return -1;
        }
    	return animalToInt(animalList.get(0).toString());
    }
    public int idHabitat() {
        if (habitatList.isEmpty()) {
        	//System.out.println("Habitat empty.");
            return -1;
        }
        //System.out.println("Habitat ID: " + habitatToInt(habitatList.get(0).toString()));
        return habitatToInt(habitatList.get(0).toString());
    }
    
    public int secondAnimalId() {
        if (animalList.isEmpty()) {
            return -1;
        }
        else if (animalList.size() == 1) {
        	return -1;
        }
        else {
        	return animalToInt(animalList.get(1).toString());
        }
    }
}