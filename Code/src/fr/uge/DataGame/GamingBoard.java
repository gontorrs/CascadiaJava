package fr.uge.DataGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GamingBoard {
	private Map<Position, Tile> board;
	private Player player;
	private int gridWidth = 5; // x
	private int gridHeight = 5; // y
	private final int userAnimal1 = 0, /* userAnimal2 = 1 */ userNotAnimal = 3;

	public GamingBoard(Player player) {
		this.player = player;
		this.board = new HashMap<>();
		emptyBoard(board);
		startingTiles();
		printBoard();
	}

	private Tile randomTile() {
		Random r = new Random();
		int chance = 0;
		chance = r.nextInt(4);
		if (chance == 0 || chance == 1) {
			return new Tile(twoAnimal(), oneHabitat(), userNotAnimal);
		} else {
			return new Tile(oneAnimal(), oneHabitat(), userNotAnimal);
		}
	}

	private void startingTiles() {
		Tile newTile;
		for (int row = 1; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				if (row == 2 && (col == 1 || col == 2 || col == 3)) {
					newTile = randomTile();
					board.put(new Position(col, row), newTile);
				}
			}
		}
	}

	public void printBoard() {
		System.out.println("Board for " + player + "\n");
		String[] tileLines;
		for (int row = 0; row < gridHeight; row++) {
			for (int line = 0; line < 4; line++) {
				for (int col = 0; col < gridWidth; col++) {
					Position pos = new Position(col, row);
					Tile tile = board.get(pos);
					tileLines = tile.toString().split("\n");
					if (line < tileLines.length) {
						System.out.print(tileLines[line]);
					} else {
						System.out.print("          ");
					}
					System.out.print("   ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	public void emptyBoard(Map<Position, Tile> boardEmpty) {
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				boardEmpty.put(new Position(col, row), new Tile(emptyAnimal(), emptyHabitat(), userNotAnimal));
			}
		}
	}

	public List<Tile> OptionTiles() {
		Tile[] tiles = generateTiles();
		displayTilesSummary(tiles);
		displayTileDetails(tiles);
		List<Tile> optionTiles = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			optionTiles.add(tiles[i]);
		}
		return optionTiles;
	}

	private Tile[] generateTiles() {
		return new Tile[] { randomTile(), randomTile(), randomTile(), randomTile(),
				new Tile(oneAnimal(), emptyHabitat(), userAnimal1), new Tile(oneAnimal(), emptyHabitat(), userAnimal1),
				new Tile(oneAnimal(), emptyHabitat(), userAnimal1),
				new Tile(oneAnimal(), emptyHabitat(), userAnimal1) };
	}

	private void displayTilesSummary(Tile[] tiles) {
		String[][] tileLines = new String[4][];
		for (int i = 0; i < 4; i++) {
			tileLines[i] = tiles[i].toString().split("\n"); // Divides each tile in lines.
		}
		for (int i = 0; i < 4; i++) { // 4 lines: top border, |Animal|, |Habitat|, bottom border
			for (int j = 0; j < 4; j++) {
				if (i == 0) {
					System.out.print(" Tile " + (j + 1) + "     ");
				} else {
					System.out.print(tileLines[j][i]);
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private void displayTileDetails(Tile[] tiles) {
		for (int i = 4; i < 8; i++) {
			System.out.println("Tile " + (i - 3) + "\n" + tiles[i].toString());
		}
	}

	public Boolean validateAnimal(Tile tile, Position pos) {
		Tile boardTile = board.get(pos);
		if (!boardTile.animalList().isEmpty() && boardTile.animalList().contains(tile.animalList().get(0))) {
			return true;
		} else {
			System.out.println("Sorry, you cannot place your animal here.");
			return false;
		}
	}

	public Position[] adjacentPos(Position pos) {
		int x = pos.x();
		int y = pos.y();

		return new Position[] { new Position(x, y - 1), new Position(x, y + 1), new Position(x - 1, y),
				new Position(x + 1, y) };
	}

	public Boolean validateHabitat(Tile tile, Position pos) {
		Position[] adjacentPositions = adjacentPos(pos);
		Tile adjacentTile;
		Tile chosenTile = board.get(pos);
		if (!chosenTile.habitatList().isEmpty()) {
			return false;
		}
		for (Position adjacentPos : adjacentPositions) {
			adjacentTile = board.get(adjacentPos);
			if (adjacentTile != null) {
				if (!adjacentTile.habitatList().isEmpty()) {
					return true;
				}
			}
		}
		return false;
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

	public Map<Position, Tile> getBoardMap() {
		return board;
	}

	public void updateBoard(int posExtend) {
		Map<Position, Tile> boardFinal = new HashMap<>();
		emptyBoard(boardFinal);
		for (int row = 0; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				Position currentPos = new Position(col, row);
				Tile currentTile = board.get(currentPos);
				if (currentTile == null) {
					boardFinal.put(extend(posExtend, row, col), new Tile(emptyAnimal(), emptyHabitat(), userNotAnimal));
				} else if (!currentTile.habitatList().isEmpty()) {
					boardFinal.put(extend(posExtend, row, col), currentTile);
				}
			}
		}
		this.board = boardFinal;
	}

	public Position extend(int posExtend, int col, int row) {
		if (posExtend == 1) {
			return new Position(row, col + 1);
		} else if (posExtend == 4) {
			return new Position(row + 1, col);
		} else if (posExtend == 5) {
			return new Position(row + 1, col + 1);
		} else {
			return new Position(row, col);
		}
	}

	public int scorePlayer(Player p) {
		int x = 0;
		return x;
	}

	public int getWidth() {
		return gridWidth;
	}

	public void setWidth(int newWidth) {
		this.gridWidth = newWidth;
	}

	public void setHeight(int newHeight) {
		this.gridHeight = newHeight;
	}

	public int getHeight() {
		return gridHeight;
	}

	public Player getPlayer() {
		return player;
	}
}