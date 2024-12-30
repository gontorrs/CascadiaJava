package fr.uge.DataGame;

import java.util.ArrayList;
import java.util.List;

public class MapGame {
	private final Tile[][] matrix;
	private final ArrayList<Animal> list_animals;
	private final ArrayList<Habitat> list_habitats;
	private final ArrayList<Players> list_players;
	private final List<Tile> optionTiles;
	private Player player;
	
	
	// Constructeur 
	public MapGame () {
		this.matrix = new Tile[10][10];
		this.list_animals = new ArrayList<>();
		this.list_habitats = new ArrayList<>();
		this.list_players = new ArrayList<>();
		this.optionTiles = new ArrayList<>();
	}
	
}
