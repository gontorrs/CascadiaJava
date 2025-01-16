// New GameLogic class
package fr.uge.DataGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameLogic {
	private final int userAnimal1 = 0, /* userAnimal2 = 1 */ userNotAnimal = 3;
    public boolean validateAnimal(Tile tile, Position pos, GamingBoard board) {
        Tile boardTile = board.getBoardMap().get(pos);
        if (!boardTile.animalList().isEmpty() && boardTile.animalList().contains(tile.animalList().get(0))) {
            return true;
        } else {
            System.out.println("Sorry, you cannot place your animal here.");
            return false;
        }
    }

    public boolean validateHabitat(Tile tile, Position pos, GamingBoard board) {
        Position[] adjacentPositions = adjacentPos(pos);
        Tile adjacentTile;
        Tile chosenTile = board.getBoardMap().get(pos);
        if (!chosenTile.habitatList().isEmpty()) {
            return false;
        }
        for (Position adjacentPos : adjacentPositions) {
            adjacentTile = board.getBoardMap().get(adjacentPos);
            if (adjacentTile != null && !adjacentTile.habitatList().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
	public Tile randomTile() {
		Random r = new Random();
		int chance = 0;
		chance = r.nextInt(4);
		if (chance == 0 || chance == 1) {
			return new Tile(twoAnimal(), oneHabitat(), userNotAnimal, true);
		} else {
			return new Tile(twoAnimal(), oneHabitat(), userNotAnimal, true);
		}
	}
	public Tile randomNoHabitatTile() {
		Random r = new Random();
		int chance = 0;
		chance = r.nextInt(4);
		if (chance == 0 || chance == 1) {
			return new Tile(twoAnimal(), emptyHabitat(), userAnimal1, true);
		} else {
			return new Tile(twoAnimal(), emptyHabitat(), userAnimal1, true);
		}
	}
	public Tile emptyTile() {
		return new Tile(emptyAnimal(), emptyHabitat(), userAnimal1, false);
	}
	
	public List<Animal> oneAnimal() {
		List<Animal> animalList = new ArrayList<>();
		Random r = new Random();
		Animal[] animals = Animal.values();
		Animal randomAnimal = animals[r.nextInt(animals.length)];
		animalList.add(randomAnimal);
		return animalList;
	}

	public List<Animal> twoAnimal() {
		List<Animal> animalList = new ArrayList<>();
		Random r = new Random();
		Animal[] animals = Animal.values();
		Animal randomAnimal1 = animals[r.nextInt(animals.length)];
		Animal randomAnimal2 = animals[r.nextInt(animals.length)];
		animalList.add(randomAnimal1);
		animalList.add(randomAnimal2);
		return animalList;
	}

	public List<Habitat> oneHabitat() {
		List<Habitat> habitatList = new ArrayList<>();
		Random r = new Random();
		Habitat[] habitat = Habitat.values();
		Habitat randomHabitat = habitat[r.nextInt(habitat.length)];
		habitatList.add(randomHabitat);
		return habitatList;
	}

	public List<Habitat> emptyHabitat() {
		List<Habitat> habitatList = new ArrayList<>();
		return habitatList;
	}

	public List<Animal> emptyAnimal() {
		List<Animal> animalList = new ArrayList<>();
		return animalList;
	}

    public Position[] adjacentPos(Position pos) {
        int x = pos.x();
        int y = pos.y();
        return new Position[] {
            new Position(x, y - 1),
            new Position(x, y + 1),
            new Position(x - 1, y),
            new Position(x + 1, y)
        };
    }

    public Position updatePos(int x, int y, GamingBoard board) {
        Objects.requireNonNull(board);
        int posExtend = (x == 0 ? 4 : x == board.getWidth() - 1 ? 2 : 0) + (y == 0 ? 1 : y == board.getHeight() - 1 ? 3 : 0);

        if (x == 0 || x == board.getWidth() - 1) {
            board.setWidth(board.getWidth() + 1);
        }
        if (y == 0 || y == board.getHeight() - 1) {
            board.setHeight(board.getHeight() + 1);
        }

        if (posExtend != 0) {
            board.updateBoard(posExtend);
            return board.extend(posExtend, y, x);
        }
        return new Position(x, y);
    }

    public int equalAnimal(List<Animal> animalLong, List<Animal> animalShort) {
        Objects.requireNonNull(animalLong);
        Objects.requireNonNull(animalShort);
        for (int i = 0; i < animalLong.size(); i++) {
            if (animalLong.get(i).equals(animalShort.get(0))) {
                return i;
            }
        }
        return 3;
    }
}
