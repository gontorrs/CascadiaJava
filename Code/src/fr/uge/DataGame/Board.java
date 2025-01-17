package fr.uge.DataGame;
import java.util.Map;
//This is an interface that has two boards that implement them, 
//the main board, where each player plays and also the option board, where the option tiles will be.
public sealed interface Board permits GamingBoard, OptionBoard{
	void printBoard(); //Prints the board. 
	void emptyBoard(Map<Position, Tile> boardEmpty); //Initialises the board to all empty. 
	int idAnimal(int i, int j); //Takes the id of the animal for the graphical interface. 
	int idHabitat(int i, int j); //Takes the id of the habitat for the graphical interface. 
	int secondAnimalId(int i, int j); //Takes the id of the second animal for the graphical interface. 
	boolean isVisible(int i, int j); //If false draws an empty tile whereas if not it draws the tile. 
	void hideAll(); //Hides all the tiles, sets its visibility to false. 
	void showAll(); //Shows all the tiles, sets its visibility to true. 
	Map<Position, Tile> getBoardMap(); //Needs to have the same function to draw it afterwaards, it returns the map, the GamingBoard or OptionBoard.
}
