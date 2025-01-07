package fr.uge.controllerGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import fr.uge.DataGame.Animal;
import fr.uge.DataGame.Difficulty;
import fr.uge.DataGame.ExecutionMode;
import fr.uge.DataGame.GamingBoard;
import fr.uge.DataGame.Player;
import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;
import fr.uge.score_game.FamilyAndIntermediateScore;
import fr.uge.score_game.ScoreRule;

public class GamePlay {

	private final BufferedReader reader;
	private Difficulty difficulty;
	private GamingBoard gb1;
	private GamingBoard gb2;
	private ExecutionMode executionMode;

	public GamePlay(Difficulty difficulty) {
		reader = new BufferedReader(new InputStreamReader(System.in));
		Objects.requireNonNull(difficulty);
		this.difficulty = difficulty;
	}

	private void modechoices() {
		System.out.println("Choose game mode:");
		System.out.println("1 - Command Line Mode");
		System.out.println("2 - Graphical interface.");
		System.out.print("Enter your choice (1 or 2): ");
	}

	public ExecutionMode chooseExecutionMode() throws IOException {
		ExecutionMode mode = null;
		while (mode == null) {
			modechoices();
			mode = parseExecutionMode(reader.readLine());
		}
		return mode;
	}

	private ExecutionMode parseExecutionMode(String input) {
		switch (input) {
		case "1":
			return ExecutionMode.COMMAND_LINE;
		case "2":
			return ExecutionMode.SQUARE_TILE;
		default:
			System.out.println("Invalid choice!! Please enter 1 or 2.");
			return null;
		}
	}

	public void startGame() throws IOException {
		System.out.println("Starting the game in " + difficulty + " mode.");
		Player p1 = playerCreate(1);
		GamingBoard gb1 = new GamingBoard(p1);
		Player p2 = playerCreate(2);
		GamingBoard gb2 = new GamingBoard(p2);
		gameTurns(p1, p2, gb1, gb2);
	}

	public GamingBoard getGamingBoard1() {
		return gb1;
	}

	public GamingBoard getGamingBoard2() {
		return gb2;
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

	private Tile chooseHabitatOption(List<Tile> options, int opt) {
		switch (opt) {
		case 1:
			return options.get(0);
		case 2:
			return options.get(1);
		case 3:
			return options.get(2);
		case 4:
			return options.get(3);
		default:
			throw new IllegalArgumentException("Unexpected value: " + opt);
		}
	}

	public Tile chooseAnimalOption(List<Tile> options, int opt) {
		switch (opt) {
		case 1:
			return options.get(4);
		case 2:
			return options.get(5);
		case 3:
			return options.get(6);
		case 4:
			return options.get(7);
		default:
			throw new IllegalArgumentException("Unexpected value: " + opt);
		}
	}

	public void turn(Player p, GamingBoard gb) throws IOException {
		boolean check = false;
		int opt = 0;
		System.out.println("Player's " + p.getName() + " turn:");
		List<Tile> options = gb.OptionTiles();
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
		} while (posHabitat.x() < 0 || posHabitat.y() < 0 || posHabitat.x() > gb.getWidth()
				|| posHabitat.y() > gb.getHeight());
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
		int posExtend = (x == 0 ? 4 : x == gb.getWidth() - 1 ? 2 : 0) + (y == 0 ? 1 : y == gb.getHeight() - 1 ? 3 : 0);

		if (x == 0 || x == gb.getWidth() - 1)
			gb.setWidth(gb.getWidth() + 1);
		if (y == 0 || y == gb.getHeight() - 1)
			gb.setHeight(gb.getHeight() + 1);

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
		this.executionMode = chooseExecutionMode();
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
		while (difficulty == null) {
			Game_Modes_Choices();
			difficulty = parseDifficulty(reader.readLine());
		}
		System.out.println("You have chosen " + difficulty + " mode.");
		return difficulty;
	}

	private static Difficulty parseDifficulty(String input) {
		switch (input) {
		case "1":
			return Difficulty.FAMILY;
		case "2":
			return Difficulty.INTERMEDIATE;
		default:
			System.out.println("Invalid choice!! Please enter 1 or 2.");
			return null;
		}
	}

	public void endGame(GamingBoard gb1, GamingBoard gb2) throws IOException {
		Objects.requireNonNull(gb1);
		Objects.requireNonNull(gb2);
		int scoreP1 = calculateScore(gb1);
		int scoreP2 = calculateScore(gb2);
		displayScores(gb1, gb2, scoreP1, scoreP2);
		displayWinner(scoreP1, scoreP2);
		reader.close();
		System.out.println("Game Over !");
	}

	private int calculateScore(GamingBoard gb) {
		ScoreRule scoreRule = new FamilyAndIntermediateScore(difficulty);
		return scoreRule.calculateScore(gb);
	}

	private void displayScores(GamingBoard gb1, GamingBoard gb2, int scoreP1, int scoreP2) {
		System.out.println("Game Over!");
		System.out.println("Player 1 (" + gb1.getPlayer().getName() + ") Score: " + scoreP1);
		System.out.println("Player 2 (" + gb2.getPlayer().getName() + ") Score: " + scoreP2);
	}

	private void displayWinner(int scoreP1, int scoreP2) {
		if (scoreP1 > scoreP2) {
			System.out.println("Player 1 wins!");
		} else if (scoreP2 > scoreP1) {
			System.out.println("Player 2 wins!");
		} else {
			System.out.println("It's a tie!");
		}
	}
}
