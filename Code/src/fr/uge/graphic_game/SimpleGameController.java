package fr.uge.graphic_game;

import java.awt.Color;
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

	private static boolean gameLoop(ApplicationContext context, SimpleGameData data, SimpleGameView view,
			SimpleGameData optionTiles) {
		var event = context.pollOrWaitEvent(10);

		// Si no hay eventos, seguir ejecutando
		if (event == null) {
			return true;
		}

		switch (event) {
		case KeyboardEvent ke -> {
			// Salir del juego si se presiona 'Q'
			if (ke.key() == KeyboardEvent.Key.Q) {
				System.out.println("Exiting the game...");
				return false;
			}
		}
		case PointerEvent pe -> {
			if (pe.action() == PointerEvent.Action.POINTER_DOWN) {
				var location = pe.location();
				int column = view.columnFromX(location.x()); // x
				int row = view.lineFromY(location.y()); // y

				// En tu código original llamas a data.clickOnCell(...) u
				// optionTiles.clickOnCell(...)
				// en lugar de eso, delegaremos a un método "handleClick" único:
				handleClick(row, column, data, optionTiles, context, view);
			}
		}
		}

		// Retornar true por defecto para continuar el bucle
		return true;
	}

	private static void handleClick(int row, int col, SimpleGameData data, SimpleGameData optionTiles,
			ApplicationContext context, SimpleGameView view) {
		// 1) Averiguamos si el clic fue dentro de la matriz data o de optionTiles.
		// (Adaptando tu lógica de offsets para la segunda matriz).
		boolean isInData = (col < data.width() && row < data.height());
		boolean isInOptions = (col < optionTiles.width() && row < optionTiles.height());
		int offsetCol = col - data.height() - 1;
		if (offsetCol >= 0 && offsetCol < optionTiles.width() && row < optionTiles.height()) {
			isInOptions = true;
		}
		// 2) Si aún NO hemos seleccionado ningún Tile:
		if (!hasSelectedTile) {
			hasSelectedTile = true;
			if (isInOptions) {
				System.out.println("DEntro de optiontiles");
				fromMatrix = optionTiles;
				fromPosition = new Position(offsetCol, row);
			} else {
				// Click fuera de ambas matrices
				hasSelectedTile = false;
				return;
			}
		} else {
			// 3) YA tenemos un Tile seleccionado => realizamos la “transferencia” al
			// segundo clic.
			// Determinamos dónde se hizo el segundo clic:
			SimpleGameData toMatrix;
			Position toPos;

			if (isInData) {
				toMatrix = data;
				toPos = new Position(col, row);
			}else {
				// Clic fuera de ambas matrices, no hacemos nada y reseteamos
				hasSelectedTile = false;
				fromMatrix = null;
				fromPosition = null;
				return;
			}

			// 4) Realizamos la transferencia
			doTransfer(fromMatrix, fromPosition, toMatrix, toPos);

			// 5) Reseteamos
			hasSelectedTile = false;
			fromMatrix = null;
			fromPosition = null;
		}

		// Después de procesar el clic, redibujamos
		SimpleGameView.draw(context, data, view, optionTiles);
	}

	private static void doTransfer(SimpleGameData originMatrix, Position originPos, SimpleGameData targetMatrix,
			Position targetPos) {
		Tile fromTile = originMatrix.getMatrix().get(originPos);
		Tile toTile = targetMatrix.getMatrix().get(targetPos);
		if (fromTile == null || toTile == null) {
			return;
		}

		// Copiamos los animales del Tile “origen”
		List<Animal> newAnimalList = new ArrayList<>(fromTile.animalList());

		// Si el Tile origen NO tiene hábitat, conservamos el hábitat del Tile destino
		// Caso contrario, sobreescribimos con el hábitat del origen.
		List<Habitat> newHabitatList;
		if (fromTile.habitatList().isEmpty()) {
			newHabitatList = new ArrayList<>(toTile.habitatList());
		} else {
			newHabitatList = new ArrayList<>(fromTile.habitatList());
		}

		// Creamos un Tile “nuevo” para colocar en el destino
		// Aquí puedes decidir si usas toTile.userTile() o fromTile.userTile()...
		Tile newTile = new Tile(newAnimalList, newHabitatList, toTile.userTile(), true);
		targetMatrix.getMatrix().put(targetPos, newTile);

		// Empty the origin tile selection.
		Tile clearedTile = new Tile(new ArrayList<>(), new ArrayList<>(), fromTile.userTile(), false);
		originMatrix.getMatrix().put(originPos, clearedTile);
	}

	public static void graphicBoard(ApplicationContext context, SimpleGameData data, SimpleGameData data2) {
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();
		var margin = 50;

		var animalImages = new String[] { "bear.png", "elk.png", "fox.png", "eagle.png", "salmon.png" };
		var habitatImages = new String[] { "mountain.png", "forest.png", "river.png", "grassland.png", "wetland.png" };
		var userAnimals = new String[] { "bearUser.png", "elkUser.png", "foxUser.png", "eagleUser.png", "salmonUser.png" };
		var blankImage = "blank.png";
		var images = new ImageLoader("data", animalImages, habitatImages, blankImage, userAnimals);
		var view = SimpleGameView.initGameGraphics(margin, margin, (int) Math.min(width, height) - 2 * margin, data,
				images);
		SimpleGameView.draw(context, data, view, data2);
		while (true) {
			if (!gameLoop(context, data, view, data2)) {
				System.out.println("Thank you for quitting!");
				context.dispose();
				return;
			}
		}
	}
}