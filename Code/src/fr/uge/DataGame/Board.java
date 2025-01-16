package fr.uge.DataGame;

import java.util.List;
import java.util.Map;

public sealed interface Board permits GamingBoard, OptionBoard{
	void printBoard();
	void emptyBoard(Map<Position, Tile> boardEmpty);
	int idAnimal(int i, int j);
	int idHabitat(int i, int j);
	int secondAnimalId(int i, int j);
	boolean isVisible(int i, int j);
	void hideAll();
	void showAll();
	Map<Position, Tile> getBoardMap();
}
