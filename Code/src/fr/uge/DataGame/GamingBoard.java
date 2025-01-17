package fr.uge.DataGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.forax.zen.Application;

import fr.uge.graphic_game.SimpleGameController;
//This is the class for the board, there is one GamingBoard for each player.
public final class GamingBoard implements Board {
	private final Map<Position, Tile> board;
	private final Map<Position, Tile> optBoard;
	private final OptionBoard opt;
	private final Player player;
	private final int gridWidth;
	private final int gridHeight;
	private final GameLogic gl;

	public GamingBoard(Player player, boolean mode, int gridWidth, int gridHeight) { // Mode == true (command).
		this.player = player;
		this.board = new HashMap<>();
		this.optBoard = new HashMap<>();
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		gl = new GameLogic();
		emptyBoard(board);
		if (mode) {
			startingTiles();
			opt = new OptionBoard(player, mode, optBoard, 2, 4, gl);
			opt.OptionTiles();
			printBoard();
		} else {
			startingTiles();
			opt = new OptionBoard(player, mode, optBoard, 2, 4, gl);
			opt.OptionTiles();
		}
	}

	public void startingTiles() {
		Tile newTile;
		for (int row = 1; row < gridWidth; row++) {
			for (int col = 0; col < gridHeight; col++) {
				if (row == 2 && (col == 1 || col == 2 || col == 3)) {
					newTile = gl.randomTile();
					board.put(new Position(row, col), newTile);
				}
			}
		}
	}

	public void printBoard() {
		System.out.println("Board for " + player + "\n");
		String[] tileLines;
		for (int row = 0; row < gridWidth; row++) {
			for (int line = 0; line < 4; line++) {
				for (int col = 0; col < gridHeight; col++) {
					Position pos = new Position(row, col);
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
		for (int row = 0; row < gridWidth; row++) {
			for (int col = 0; col < gridHeight; col++) {
				boardEmpty.put(new Position(row, col),
						gl.emptyTile());
			}
		}
	}

	public void updateBoard(int posExtend) {
	    Map<Position, Tile> boardFinal = new HashMap<>();
	    emptyBoard(boardFinal);
	    
	    for (int row = 0; row < gridWidth; row++) {
	        for (int col = 0; col < gridHeight; col++) {
	            Position currentPos = new Position(col, row);
	            Tile currentTile = board.get(currentPos);
	            if (currentTile == null) {
	                boardFinal.put(extend(posExtend, col, row), gl.emptyTile());
	            } else if (!currentTile.habitatList().isEmpty()) {
	                boardFinal.put(extend(posExtend, col, row), currentTile);
	            }
	        }
	    }
	    board.clear();
	    board.putAll(boardFinal);
	}


	public Position extend(int posExtend, int col, int row) {
		if (posExtend == 1) {
			return new Position(col, row + 1);
		} else if (posExtend == 4) {
			return new Position(col + 1, row);
		} else if (posExtend == 5) {
			return new Position(col + 1, row + 1);
		} else {
			return new Position(col, row);
		}
	}

	public int scorePlayer(Player p) {
		int x = 0;
		return x;
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
			return false;
		}
		return tile.visible();
	}
	public void hideAll() {
		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				Position pos = new Position(i,j);
				Tile t = board.get(pos);
				board.put(new Position(i, j), new Tile(t.animalList(), t.habitatList(), t.userTile(), false));
			}
		}
	}
	public void showAll() {
		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				Position pos = new Position(i,j);
				Tile t = board.get(pos);
				board.put(new Position(i, j), new Tile(t.animalList(), t.habitatList(), t.userTile(), true));
			}
		}
	}
	
	//--------------Getters---------------------------------------------------------------------------------------------------------------

	public Map<Position, Tile> getBoardMap() {
		return board;
	}
	
	public OptionBoard getOpt() {
		return opt;
	}
	
	public GameLogic getLogic() {
		return gl;
	}
	
	public int getWidth() {
		return gridWidth;
	}

	public int getHeight() {
		return gridHeight;
	}

	public Player getPlayer() {
		return player;
	}
}