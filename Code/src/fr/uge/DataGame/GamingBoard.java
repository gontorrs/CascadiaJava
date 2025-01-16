package fr.uge.DataGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.forax.zen.Application;

import fr.uge.graphic_game.SimpleGameController;
import fr.uge.graphic_game.SimpleGameData;

public class GamingBoard {
	private Map<Position, Tile> board;
	private Player player;
	private int gridWidth = 5; // x
	private int gridHeight = 5; // y
	private final boolean command = true, graph = false;
	private SimpleGameData data;
	private SimpleGameData optionTiles;
	private GameLogic gl;

	public GamingBoard(Player player, boolean mode) { // Mode == true (command).
		this.player = player;
		this.board = new HashMap<>();
		gl = new GameLogic();
		if (mode) {
			emptyBoard(board);
			startingTiles(command);
			printBoard();
		} else {
			data = new SimpleGameData(gridWidth, gridHeight);
			optionTiles = new SimpleGameData(2, 4);
			emptyBoard(optionTiles.getMatrix());
			OptionTiles(graph);
			emptyBoard(data.getMatrix());
			startingTiles(graph);
		}
	}

	public void startingTiles(boolean mode) {
		Tile newTile;
		for (int row = 1; row < gridHeight; row++) {
			for (int col = 0; col < gridWidth; col++) {
				if (row == 2 && (col == 1 || col == 2 || col == 3)) {
					newTile = gl.randomTile();
					if (mode) {
						board.put(new Position(col, row), newTile);
					} else {
						data.getMatrix().put(new Position(col, row), newTile);
					}
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
				boardEmpty.put(new Position(col, row),
						gl.emptyTile());
			}
		}
	}

	public List<Tile> OptionTiles(boolean mode) {
		List<Tile> optionTilesList = new ArrayList<>();
		Tile[] tiles = generateTiles();
		if (mode) {
			for (int i = 0; i < 8; i++) {
				optionTilesList.add(tiles[i]);
			}
			displayTilesSummary(tiles);
			displayTileDetails(tiles);
		} else {
			int index = 0;
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 2; col++) {
					Position position = new Position(col, row);
					optionTiles.getMatrix().put(position, tiles[index]);
					index++;
				}
			}
		}
		return optionTilesList;
	}

	private Tile[] generateTiles() {
		return new Tile[] { gl.randomTile(), gl.randomTile(), gl.randomTile(), gl.randomTile(),
				gl.randomNoHabitatTile(), gl.randomNoHabitatTile(), gl.randomNoHabitatTile(),
				gl.randomNoHabitatTile() };

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
					boardFinal.put(extend(posExtend, row, col), gl.emptyTile());
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
	

	
	//--------------Getters and setter---------------------------------------------------------------------------------------------------------------

	public int getWidth() {
		return gridWidth;
	}

	public void setWidth(int newWidth) {
		this.gridWidth = newWidth;
	}

	public void setHeight(int newHeight) {
		this.gridHeight = newHeight;
	}

	public SimpleGameData getDataMatrix() {
		return data;
	}

	public SimpleGameData getOptionMatrix() {
		return optionTiles;
	}

	public int getHeight() {
		return gridHeight;
	}

	public Player getPlayer() {
		return player;
	}
}