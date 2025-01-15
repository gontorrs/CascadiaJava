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
	private int wins;
	private Position firstClickPosition = null;

	public SimpleGameData(int gridWidth, int gridHeight) {
		if (gridWidth < 0 || gridHeight < 0) {
			throw new IllegalArgumentException();
		}
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.matrix = new HashMap<>();
		this.wins = 0;

		// Inicializar todas las celdas con instancias válidas de Tile
		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				List<Animal> animalList = new ArrayList<>(); // Inicializa con una lista vacía o con datos
																// predeterminados
				List<Habitat> habitatList = new ArrayList<>(); // Inicializa con una lista vacía o con datos
																// predeterminados
				Tile tile = new Tile(animalList, habitatList, 0, false); // Ajusta según sea necesario
				matrix.put(new Position(i, j), tile);
			}
		}
	}

	public int width() {
		   return gridWidth;    // era lines()
		}

		public int height() {
		   return gridHeight;   // era columns()
		}


	public int idAnimal(int i, int j) {
		return matrix.get(new Position(i, j)).idAnimal();
	}

	public int idHabitat(int i, int j) {
		return matrix.get(new Position(i, j)).idHabitat();
	}

	public int secondAnimalId(int i, int j) {
		return matrix.get(new Position(i, j)).secondAnimalId();
	}

	public boolean win() {
		return 2 * wins == gridWidth * gridHeight;
	}

	public Map<Position, Tile> getMatrix() {
		return this.matrix;
	}

	public boolean isVisible(int i, int j) {
		Tile tile = matrix.get(new Position(i, j));
		if (tile == null) {
			return false; // O cualquier valor predeterminado que tenga sentido en tu contexto
		}
		return tile.visible();
	}
}