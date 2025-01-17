package fr.uge.DataGame;
import java.util.Map;
//This class implements from board and it's the board that has all the option tiles (habitat + animal & user animal).
public record OptionBoard(Player player, boolean mode, Map<Position, Tile> board, int getWidth, int getHeight, GameLogic gl) implements Board {

	public OptionBoard(Player player, boolean mode, Map<Position, Tile> board, int getWidth, int getHeight, GameLogic gl) { // Mode == true (command).
	    this.mode = mode;
	    this.player = player;
	    this.gl = gl;
	    this.board = board;
	    this.getWidth = getWidth;
	    this.getHeight = getHeight;
	    emptyBoard(this.board);
	}
	
	
//Print Function.
	public void printBoard() {
	    System.out.println("Option Board: ");
	    int tileCount = 4;

	    for (int row = 0; row < getWidth; row++) {
	        for (int col = 0; col < getHeight; col++) {
	            int tileNumber = (row * getHeight + col) % tileCount + 1;
	            System.out.print("   Tile " + tileNumber + "   ");
	        }
	        System.out.println();

	        for (int line = 0; line < 4; line++) {
	            for (int col = 0; col < getHeight; col++) {
	            	Position pos = new Position(row, col);
	                Tile tile = board.get(pos);
	                String[] tileLines = getTileLines(tile, pos);
	                printTileLine(tileLines, line);

	                System.out.print("   ");
	            }
	            System.out.println();
	        }
	        System.out.println();
	    }
	}
	
	private String[] getTileLines(Tile tile, Position pos) {
	    if (tile.animalList().isEmpty()) {
	        return new String[]{"+--------+", "|        |", "| (Empty) |", "+--------+"};
	    }
	    return tile.toString().split("\n");
	}

	private void printTileLine(String[] tileLines, int line) {
	    if (line < tileLines.length) {
	        System.out.print(tileLines[line]);
	    } else {
	        System.out.print("          ");
	    }
	}

	public void emptyBoard(Map<Position, Tile> boardEmpty) {
		for (int row = 0; row < getWidth; row++) {
			for (int col = 0; col < getHeight; col++) {
				boardEmpty.put(new Position(row, col),
						gl.emptyTile());
			}
		}
	}

	public Map<Position, Tile> OptionTiles() {
		Tile[] tiles = generateTiles();
		int index = 0;
		for (int row = 0; row < getWidth; row++) {
			for (int col = 0; col < getHeight; col++) {
				Position position = new Position(row, col);
				board.put(position, tiles[index]);
				index++;
			}
		}
		return board;
	}

	private Tile[] generateTiles() {
		return new Tile[] { 
				gl.randomTile(), 
				gl.randomTile(), 
				gl.randomTile(), 
				gl.randomTile(),
				gl.randomNoHabitatTile(), 
				gl.randomNoHabitatTile(), 
				gl.randomNoHabitatTile(),
				gl.randomNoHabitatTile()
				};
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

	//-------------------------------------------------------------------------------------------------------------------------------------------

	public boolean isVisible(int i, int j) {
		Tile tile = board.get(new Position(i, j));
		if (tile == null) {
			return false;
		}
		return tile.visible();
	}
	
	public void hideAll() {
	    for (Position pos : board.keySet()) {
	        Tile t = board.get(pos);
	        board.put(pos, t.hide());
	    }
	}

	public void showAll() {
	    for (Position pos : board.keySet()) {
	        Tile t = board.get(pos);
	        board.put(pos, t.show());
	    }
	}

	public Map<Position, Tile> getBoardMap(){
		return board;
	}
}