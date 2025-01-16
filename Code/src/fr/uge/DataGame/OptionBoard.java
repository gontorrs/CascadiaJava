package fr.uge.DataGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.forax.zen.Application;

import fr.uge.graphic_game.SimpleGameController;

public record OptionBoard(Player player, boolean mode, Map<Position, Tile> board, int getWidth, int getHeight, GameLogic gl) implements Board {

	public OptionBoard(Player player, boolean mode, Map<Position, Tile> board, int getWidth, int getHeight, GameLogic gl) { // Mode == true (command).
	    this.mode = mode;
	    this.player = player;
	    this.gl = gl;
	    this.board = board;
	    this.getWidth = getWidth;
	    this.getHeight = getHeight;
	    emptyBoard(this.board);
		//printBoard();
	}
	
	
//Commented to implement this function in the terminal version if there is time.
	public void printBoard() {  
		System.out.println("Board for " + player + "\n");
		String[] tileLines;
		for (int row = 0; row < getHeight; row++) {
			for (int line = 0; line < 4; line++) {
				for (int col = 0; col < getWidth; col++) {
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
	
	public List<Tile> MaptoList() {
		List<Tile> list = new ArrayList<>();
		for(int row = 0; row < getWidth;row++) {
			for(int col = 0; col < getHeight;col++) {
				Tile t = board.get(new Position(row, col));
				list.add(t);
			}
		}
		return list;
	}

	public void emptyBoard(Map<Position, Tile> boardEmpty) {
		for (int row = 0; row < getWidth; row++) {
			for (int col = 0; col < getHeight; col++) {
				boardEmpty.put(new Position(row, col),
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
					board.put(position, tiles[index]);
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

//This will be here for future implementation.
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

	public Map<Position, Tile> getOptionMap() {
		return board;
	}
	
	//----Graphical Interface for the ids of the animals-----------------------------------------------------------------------------------------
	
	public int idAnimal(int i, int j) {
		return board.get(new Position(i, j)).idAnimal();
	}

	public int idHabitat(int i, int j) {
		return board.get(new Position(i, j)).idHabitat();
	}

	public int secondAnimalId(int i, int j) {
		return board.get(new Position(i, j)).secondAnimalId();
	}
	
	//----Graphical Interface for the ids of the animals-----------------------------------------------------------------------------------------

	public boolean isVisible(int i, int j) {
		Tile tile = board.get(new Position(i, j));
		if (tile == null) {
			return false; // O cualquier valor predeterminado que tenga sentido en tu contexto
		}
		return tile.visible();
	}
	public void hideAll() {
		for (int i = 0; i < getWidth; i++) {
			for (int j = 0; j < getHeight; j++) {
				Position pos = new Position(i,j);
				Tile t = board.get(pos);
				board.put(new Position(i, j), new Tile(t.animalList(), t.habitatList(), t.userTile(), false));
			}
		}
	}
	public void showAll() {
		for (int i = 0; i < getWidth; i++) {
			for (int j = 0; j < getHeight; j++) {
				Position pos = new Position(i,j);
				Tile t = board.get(pos);
				board.put(new Position(i, j), new Tile(t.animalList(), t.habitatList(), t.userTile(), true));
			}
		}
	}
	public Map<Position, Tile> getBoardMap(){
		return board;
	}
}