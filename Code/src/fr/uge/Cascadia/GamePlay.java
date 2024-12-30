package fr.uge.Cascadia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GamePlay {

	private BufferedReader reader;
	private Difficulty difficulty;

	public GamePlay(Difficulty difficulty) {
		reader = new BufferedReader(new InputStreamReader(System.in));
		Objects.requireNonNull(difficulty);
		this.difficulty = difficulty;
	}

	public void startGame() throws IOException {
		System.out.println("Starting the game in " + difficulty + " mode.");
		Player p1 = playerCreate(1);
		GamingBoard gb1 = new GamingBoard(p1);
		Player p2 = playerCreate(2);
		GamingBoard gb2 = new GamingBoard(p2);
		gameTurns(p1, p2, gb1, gb2);
	}

	private Player playerCreate(int numberPL) throws IOException {
		System.out.print("Player " + numberPL + " Name: ");
		String name = reader.readLine();
		return new Player(name);
	}

	private void gameTurns(Player p1, Player p2, GamingBoard gb1, GamingBoard gb2) throws IOException {
		int turn = 1;
		do {
			if (turn % 2 != 0) {
				turn(p1, gb1);
				turn++;
			} else {
				turn(p2, gb2);
				turn++;
			}
		} while (turn <= 20);
	}

	private Position chooseCoord(Tile t, Player p, GamingBoard gb) throws IOException {
		System.out.print("Enter the x-coordinate: ");
		int x = Integer.parseInt(reader.readLine());
		System.out.print("Enter the y-coordinate: ");
		int y = Integer.parseInt(reader.readLine());
		return new Position(x, y);
	}

	private int ChooseOpt(Player p) throws IOException {
		System.out.print("Player " + p.getName() + " choose one tile (Animal + Habitat): ");
		int opt = Integer.parseInt(reader.readLine());
		if (opt > 4 || opt < 1) {
			System.out.println("There are 4 option tiles !! \n Choose a Tile between 1 and 4 please \n");
		}
		return opt;
	}

	private Tile chooseHabitatOption(Tile[] options, int opt) {
		switch (opt) {
		case 1:
			return options[0];
		case 2:
			return options[1];
		case 3:
			return options[2];
		case 4:
			return options[3];
		default:
			throw new IllegalArgumentException("Unexpected value: " + opt);
		}
	}

	public Tile chooseAnimalOption(Tile[] options, int opt) {
		switch (opt) {
		case 1:
			return options[4];
		case 2:
			return options[5];
		case 3:
			return options[6];
		case 4:
			return options[7];
		default:
			throw new IllegalArgumentException("Unexpected value: " + opt);
		}
	}

	public void turn(Player p, GamingBoard gb) throws IOException {
		boolean check = false;
		int opt = 0;
		System.out.println("Player's " + p.getName() + " turn:");
		Tile[] options = gb.OptionTiles();
		do {
			opt = ChooseOpt(p);
		} while (opt < 0 || opt > 4);
		Tile habitatTile = chooseHabitatOption(options, opt);
		do {
			check = placeHabitat(habitatTile, gb, p);
		} while (!check);
		gb.printBoard();
		Tile animalTile = chooseAnimalOption(options, opt);
		do {
			check = placeAnimal(animalTile, gb, p);
		} while (!check);
		gb.printBoard();
	}

	public boolean placeHabitat(Tile habitatTile, GamingBoard gb, Player p) throws IOException {
		boolean check = false;
		Tile userHabitat;
		Position posHabitat, posHabitatNew;
		System.out.println("Choose the coordinates for you habitat: ");
		do {
			posHabitat = chooseCoord(habitatTile, p, gb);
		} while (posHabitat.x() < 0 || posHabitat.y() < 0 || posHabitat.x() > gb.getWidth() || posHabitat.y() > gb.getHeight());
		check = gb.validateHabitat(habitatTile, posHabitat);
		if (check) {
			posHabitatNew = updatePos(posHabitat.x(), posHabitat.y(), gb);
			userHabitat = new Tile(habitatTile.animalList(), habitatTile.habitatList(), 3);
			gb.getBoardMap().put(posHabitatNew, userHabitat); // puts the habitat in the board.
			return check;
		} else {
			System.out.println("Please choose an adjacent tile.");
			return check;
		}
	}

	public boolean placeAnimal(Tile animalTile, GamingBoard gb, Player p) throws IOException {
		Tile userAnimal;
		boolean check = false;
		System.out.println("Choose the coordinates for you animal: ");
		Position posAnimal = chooseCoord(animalTile, p, gb);
		Tile currentTile = gb.getBoardMap().get(posAnimal);
		check = gb.validateAnimal(animalTile, posAnimal);
		int userAnimaloption = equalAnimal(currentTile.animalList(), animalTile.animalList());
		if (check) {
			userAnimal = new Tile(currentTile.animalList(), currentTile.habitatList(), userAnimaloption);
			gb.getBoardMap().put(posAnimal, userAnimal);
			return check;
		} else {
			System.out.println("Please choose a tile that contains the animal you chose.");
			return check;
		}
	}

	private int equalAnimal(List<Animal> animalLong, List<Animal> animalShort) {
		Objects.requireNonNull(animalLong);
		Objects.requireNonNull(animalShort);
		for (int i = 0; i < animalLong.size(); i++) {
			if (animalLong.get(i).equals(animalShort.get(0))) {
				return i;
			}
		}
		return 3;
	}

	public Position updatePos(int x, int y, GamingBoard gb) {
	    Objects.requireNonNull(gb);
	    int posExtend = (x == 0 ? 4 : x == gb.getWidth() - 1 ? 2 : 0) 
	                  + (y == 0 ? 1 : y == gb.getHeight() - 1 ? 3 : 0);
	    
	    if (x == 0 || x == gb.getWidth() - 1) gb.setWidth(gb.getWidth() + 1);
	    if (y == 0 || y == gb.getHeight() - 1) gb.setHeight(gb.getHeight() + 1);
	    
	    if (posExtend != 0) {
	        gb.updateBoard(posExtend);
	        return gb.extend(posExtend, y, x);
	    }
	    return new Position(x, y);
	}


	public void summaryGame() throws IOException {
		System.out.println("********************************\n");
		System.out.println("*    WELCOME TO CASCADIA GAME  *\n");
		System.out.println("*          CONSOLE MODE        *\n");
		System.out.println("********************************\n\n");
		System.out.println("The Summary : ");
		System.out.println("Choosing game difficulty...\n");
		this.difficulty = chooseDifficulty(reader);
		System.out.println("The Game has started ! Good luck :)\n");
	}

	private static void Game_Modes_Choices() {
		System.out.println("Choose game mode:");
		System.out.println("1 - Family Mode");
		System.out.println("2 - Intermediate Mode");
		System.out.print("Enter your choice (1 or 2): ");
	}

	public static Difficulty chooseDifficulty(BufferedReader reader) throws IOException {
		Difficulty difficulty = null;
		boolean validChoice = false;
		while (!validChoice) {
			Game_Modes_Choices();
			String inputUser = reader.readLine();
			if (inputUser.equals("1") || inputUser.equals("2")) {
				int choice = Integer.parseInt(inputUser);
				switch (choice) {
				case 1:
					difficulty = Difficulty.FAMILY;
					validChoice = true;
					break;
				case 2:
					difficulty = Difficulty.INTERMEDIATE;
					validChoice = true;
					break;
				}
			} else {
				System.out.println("Invalid choice!! Please enter 1 or 2.");
			}
		}
		System.out.println("You have chosen " + difficulty + " mode.");
		return difficulty;
	}

	private int bearA(GamingBoard gb, Player p) {
		List<List<Position>> bearGroups = new ArrayList<>();
		return calculateScore(bearGroups);
	}

	private List<List<Position>> findBearGroups(GamingBoard gb) {
		List<List<Position>> bearGroups = new ArrayList<>();
		for (int row = 0; row < gb.getHeight(); row++) {
			for (int col = 0; col < gb.getWidth(); col++) {
				Position pos = new Position(col, row);
				if (hasBear(gb, pos)) {
					findBearGroup(gb, pos, bearGroups);
				}
			}
		}
		return bearGroups;
	}

	private boolean hasBear(GamingBoard gb, Position pos) {
		Tile tile = gb.getBoardMap().get(pos);
		return tile != null && tile.animalList().contains(Animal.BEAR);
	}

	private int calculateScore(List<List<Position>> bearGroups) {
		int score = 0;
		boolean hasGroupOf1 = false;
		boolean hasGroupOf2 = false;
		boolean hasGroupOf3 = false;

		for (List<Position> group : bearGroups) {
			score += calculateGroupScore(group);
			updateGroupFlags(group, hasGroupOf1, hasGroupOf2, hasGroupOf3);
		}

		if (hasGroupOf1 && hasGroupOf2 && hasGroupOf3) {
			score += 3;
		}

		return score;
	}

	private int calculateGroupScore(List<Position> group) {
		int groupSize = group.size();
		switch (groupSize) {
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		default:
			return (groupSize >= 2 && groupSize <= 4) ? groupSize : 0; // Règle D
		}
	}

	private void updateGroupFlags(List<Position> group, boolean hasGroupOf1, boolean hasGroupOf2, boolean hasGroupOf3) {
		int groupSize = group.size();
		if (groupSize == 1)
			hasGroupOf1 = true;
		else if (groupSize == 2)
			hasGroupOf2 = true;
		else if (groupSize == 3)
			hasGroupOf3 = true;
	}

	private void findBearGroup(GamingBoard gb, Position startPos, List<List<Position>> bearGroups) {
		Set<Position> visited = new HashSet<>();
		List<Position> group = new ArrayList<>();
		exploreGroup(gb, startPos, visited, group);

		if (!group.isEmpty()) {
			bearGroups.add(group);
		}
	}

	private void exploreGroup(GamingBoard gb, Position pos, Set<Position> visited, List<Position> group) {
		if (visited.contains(pos))
			return;

		Tile tile = gb.getBoardMap().get(pos);
		if (tile != null && tile.animalList().contains(Animal.BEAR)) {
			group.add(pos);
			visited.add(pos);
			exploreAdjacentPositions(gb, pos, visited, group);
		}
	}

	private void exploreAdjacentPositions(GamingBoard gb, Position pos, Set<Position> visited, List<Position> group) {
		Position[] adjacentPositions = gb.adjacentPos(pos);
		for (Position adjacentPos : adjacentPositions) {
			exploreGroup(gb, adjacentPos, visited, group);
		}
	}

	public void endGame() throws IOException {
		reader.close();
		System.out.println("Game Over !");
	}
}