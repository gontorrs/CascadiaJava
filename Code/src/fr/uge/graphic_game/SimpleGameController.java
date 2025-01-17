package fr.uge.graphic_game;

//This class would be the main controller for the graphical interface option.
import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.DataGame.Animal;
import fr.uge.DataGame.GamingBoard;
import fr.uge.DataGame.Habitat;
import fr.uge.DataGame.OptionBoard;
import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;

public class SimpleGameController {
	private static boolean hasSelectedTile;
	private static Position fromPosition;
	private static OptionBoard fromMatrix;

	public SimpleGameController() {
		fromMatrix = null;
		fromPosition = null;
		hasSelectedTile = false;
	}

	private static boolean gameLoop(ApplicationContext context, GamingBoard data, SimpleGameView view, OptionBoard optionTiles, int turn) {
		boolean turnEnded = false;

		while (!turnEnded) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			switch (event) {
			case KeyboardEvent ke -> {
				if (ke.key() == KeyboardEvent.Key.Q) {
					System.out.println("Exiting the game...");
					return false;
				}
			}
			case PointerEvent pe -> {
				if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
					var location = pe.location();
					int column = view.columnFromX(location.x());
					int row = view.lineFromY(location.y());
					turnEnded = handleClick(row, column, data, optionTiles, context, view);
					SimpleGameView.draw(context, data, view, optionTiles, turn);
					try {
					    Thread.sleep(1000); // 1 second of pause to view the changes.
					} catch (InterruptedException e) {
					    Thread.currentThread().interrupt();
					}
					optionTiles.hideAll();
				}
			}
			}
		}
		return true;
	}

	private static boolean handleClick(int row, int col, GamingBoard data, OptionBoard optionTiles, ApplicationContext context, SimpleGameView view) {
		boolean isInData = (col < data.getWidth() && row < data.getHeight());
		boolean isInOptions = (col < optionTiles.getWidth() && row < optionTiles.getHeight());

		// Ajuste por offset (si lo usas)
		int offsetCol = col - data.getWidth() - 1;
		if (offsetCol >= 0 && offsetCol < optionTiles.getWidth() && row < optionTiles.getHeight()) {
			isInOptions = true;
		}

		// Primer clic: se selecciona una Tile
		if (!hasSelectedTile) {
			hasSelectedTile = true;
			if (isInOptions && optionTiles.isVisible(offsetCol, row)) {
				fromMatrix = optionTiles;
				fromPosition = new Position(offsetCol, row);
			} else {
				// No se seleccionó tile válida => anula la selección
				hasSelectedTile = false;
			}
			return false; // turno NO ha terminado
		}
		// Segundo clic: se coloca la Tile
		else {
			GamingBoard toMatrix;
			Position toPos;

			if (isInData) {
				toMatrix = data;
				toPos = new Position(col, row);
			} else {
				// Clic fuera del tablero => cancela
				hasSelectedTile = false;
				fromMatrix = null;
				fromPosition = null;
				return false; // turno NO termina, pero reinicia la selección
			}

			// Realizamos la transferencia
			doTransfer(fromMatrix, fromPosition, toMatrix, toPos);
			// Reseteamos selección
			hasSelectedTile = false;
			fromMatrix = null;
			fromPosition = null;

			return true;
		}
	}

	private static void doTransfer(OptionBoard originMatrix, Position originPos, GamingBoard targetMatrix, Position targetPos) {
		Tile fromTile = originMatrix.board().get(originPos);
		Tile toTile = targetMatrix.getBoardMap().get(targetPos);
		if (fromTile == null || toTile == null) {
			return;
		}
		
		List<Animal> newAnimalList = new ArrayList<>(fromTile.animalList());
		if(!toTile.animalList().isEmpty() && toTile.animalList().contains(fromTile.animalList().get(0))){
			
		}
		List<Habitat> newHabitatList = new ArrayList<>();
		
		if (fromTile.habitatList().isEmpty()) {
			newHabitatList = new ArrayList<>(toTile.habitatList());
			Tile newTile = new Tile(newAnimalList, newHabitatList, 1, true); //Meaning that is the user that chose that animal to distinguish it
			targetMatrix.getBoardMap().put(targetPos, newTile); // from the tile animal by default.
		} 
		else {
			newHabitatList = new ArrayList<>(fromTile.habitatList());
			Tile newTile = new Tile(newAnimalList, newHabitatList, 3, true);//Meaning that is it's the defualt animal in the tile.
			targetMatrix.getBoardMap().put(targetPos, newTile);
		}
		
		Tile clearedTile = new Tile(new ArrayList<>(), new ArrayList<>(), fromTile.userTile(), false);
		originMatrix.board().put(originPos, clearedTile);
	}
	
	private static ImageLoader LoadImages() {
		var animalImages = new String[] { "bear.png", "elk.png", "fox.png", "eagle.png", "salmon.png" };
		var habitatImages = new String[] { "mountain.png", "forest.png", "river.png", "grassland.png", "wetland.png" };
		var userAnimals = new String[] { "bearUser.png", "elkUser.png", "foxUser.png", "eagleUser.png", "salmonUser.png" };
		var numbers = new String[] { "one.png", "two.png", "three.png", "four.png" };
		var players = new String[] { "player1.png", "player2.png" };
		var blankImage = "blank.png";
		var images = new ImageLoader("data", animalImages, habitatImages, blankImage, userAnimals, numbers, players);
		return images;
	}

	public static void graphicBoard(ApplicationContext context, GamingBoard data1, OptionBoard opt1, GamingBoard data2, OptionBoard opt2) {
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();
		var margin = 50;
		int turn = 0;
		boolean cont = true;
		var images = LoadImages();
		var view1 = SimpleGameView.initGameGraphics(margin, margin, (int) Math.min(width, height) - 2 * margin, data1, images);
		var view2 = SimpleGameView.initGameGraphics(margin, margin, (int) Math.min(width, height) - 2 * margin, data2, images);
		while (true) {
			if (turn % 2 == 0) { // P1 Turn.
				data2.hideAll();
				opt2.hideAll();
				data1.showAll();
				opt1.showAll();
				SimpleGameView.draw(context, data1, view1, opt1, turn);
				cont = gameLoop(context, data1, view1, opt1, turn);
			} else { // P2 Turn.
				data1.hideAll();
				opt1.hideAll();
				data2.showAll();
				opt2.showAll();
				SimpleGameView.draw(context, data2, view2, opt2, turn);
				cont = gameLoop(context, data2, view2, opt2, turn);
			}
			if (!cont) {
				System.out.println("Thank you for quitting!");
				context.dispose();
				return;
			}
			turn++;
		}
	}
}