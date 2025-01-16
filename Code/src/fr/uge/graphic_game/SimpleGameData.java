package fr.uge.graphic_game;

import fr.uge.DataGame.Animal;
import fr.uge.DataGame.Habitat;
import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleGameData {
	private final Map<Position, Tile> matrix;
	private final int gridWidth;
	private final int gridHeight;

	public SimpleGameData(int gridWidth, int gridHeight) {
		if (gridWidth < 0 || gridHeight < 0) {
			throw new IllegalArgumentException();
		}
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.matrix = new HashMap<>();
	}

	public int width() {
		   return gridWidth;    // era lines()
		}

		public int height() {
		   return gridHeight;   // era columns()
		}

		//----Graphical Interface for the ids of the animals-----------------------------------------------------------------------------------------
		
		public int idAnimal(int i, int j) {
			return matrix.get(new Position(i, j)).idAnimal();
		}

		public int idHabitat(int i, int j) {
			return matrix.get(new Position(i, j)).idHabitat();
		}

		public int secondAnimalId(int i, int j) {
			return matrix.get(new Position(i, j)).secondAnimalId();
		}
		
		//----Graphical Interface for the ids of the animals-----------------------------------------------------------------------------------------

		public boolean isVisible(int i, int j) {
			Tile tile = matrix.get(new Position(i, j));
			if (tile == null) {
				return false; // O cualquier valor predeterminado que tenga sentido en tu contexto
			}
			return tile.visible();
		}
		public void hideAll() {
			for (int i = 0; i < gridWidth; i++) {
				for (int j = 0; j < gridHeight; j++) {
					Position pos = new Position(i,j);
					Tile t = matrix.get(pos);
					matrix.put(new Position(i, j), new Tile(t.animalList(), t.habitatList(), t.userTile(), false));
				}
			}
		}
		public void showAll() {
			for (int i = 0; i < gridWidth; i++) {
				for (int j = 0; j < gridHeight; j++) {
					Position pos = new Position(i,j);
					Tile t = matrix.get(pos);
					matrix.put(new Position(i, j), new Tile(t.animalList(), t.habitatList(), t.userTile(), true));
				}
			}
		}

	public Map<Position, Tile> getMatrix() {
		return this.matrix;
	}
}