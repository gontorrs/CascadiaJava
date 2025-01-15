package fr.uge.graphic_game;

import java.awt.Color;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import fr.uge.DataGame.Animal;
import fr.uge.DataGame.Habitat;
import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;

public class SimpleGameController {
	private static boolean hasSelectedTile;
	private static Position fromPosition;
	private static SimpleGameData fromMatrix;

	public SimpleGameController() {
		fromMatrix = null;
		fromPosition = null;
		hasSelectedTile = false;
	}

	private static boolean gameLoop(ApplicationContext context, SimpleGameData data, SimpleGameView view, SimpleGameData optionTiles, int turn) {
		boolean turnEnded = false; // Flag para saber si la jugada (transferencia) ha acabado

		while (!turnEnded) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue; // nada que procesar, seguir esperando
			}
			switch (event) {
			case KeyboardEvent ke -> {
				if (ke.key() == KeyboardEvent.Key.Q) {
					System.out.println("Exiting the game...");
					return false; // señal de que se quiere salir por completo
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
					    Thread.sleep(1000); // 1 segundo de pausa
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

	private static boolean handleClick(int row, int col, SimpleGameData data, SimpleGameData optionTiles, ApplicationContext context, SimpleGameView view) {
		boolean isInData = (col < data.width() && row < data.height());
		boolean isInOptions = (col < optionTiles.width() && row < optionTiles.height());

		// Ajuste por offset (si lo usas)
		int offsetCol = col - data.height() - 1;
		if (offsetCol >= 0 && offsetCol < optionTiles.width() && row < optionTiles.height()) {
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
			SimpleGameData toMatrix;
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

	private static void doTransfer(SimpleGameData originMatrix, Position originPos, SimpleGameData targetMatrix, Position targetPos) {
		Tile fromTile = originMatrix.getMatrix().get(originPos);
		Tile toTile = targetMatrix.getMatrix().get(targetPos);
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
			targetMatrix.getMatrix().put(targetPos, newTile); // from the tile animal by default.
		} 
		else {
			newHabitatList = new ArrayList<>(fromTile.habitatList());
			Tile newTile = new Tile(newAnimalList, newHabitatList, 3, true);//Meaning that is it's the defualt animal in the tile.
			targetMatrix.getMatrix().put(targetPos, newTile);
		}
		
		Tile clearedTile = new Tile(new ArrayList<>(), new ArrayList<>(), fromTile.userTile(), false);
		originMatrix.getMatrix().put(originPos, clearedTile);
	}

	public static void graphicBoard(ApplicationContext context, SimpleGameData data1, SimpleGameData opt1,
			SimpleGameData data2, SimpleGameData opt2) {
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();
		var margin = 50;
		int turn = 0;
		boolean cont = true;

		var animalImages = new String[] { "bear.png", "elk.png", "fox.png", "eagle.png", "salmon.png" };
		var habitatImages = new String[] { "mountain.png", "forest.png", "river.png", "grassland.png", "wetland.png" };
		var userAnimals = new String[] { "bearUser.png", "elkUser.png", "foxUser.png", "eagleUser.png", "salmonUser.png" };
		var numbers = new String[] { "one.png", "two.png", "three.png", "four.png" };
		var players = new String[] { "player1.png", "player2.png" };
		var blankImage = "blank.png";
		var images = new ImageLoader("data", animalImages, habitatImages, blankImage, userAnimals, numbers, players);
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