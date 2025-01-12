package fr.uge.graphic_game;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

public class SimpleGameController {
	public SimpleGameController() {

	}

	private static boolean gameLoop(ApplicationContext context, SimpleGameData data, SimpleGameView view, SimpleGameData data2) {
		var event = context.pollOrWaitEvent(10);
		switch (event) {
		case null:
			return true;
		case KeyboardEvent ke:
			return ke.key() != KeyboardEvent.Key.Q;
		case PointerEvent pe:
			if (pe.action() != PointerEvent.Action.POINTER_DOWN) {
				return true;
			}
			var location = pe.location();
			data.clickOnCell(view.columnFromX(location.x()), view.lineFromY(location.y()), data2);
			SimpleGameView.draw(context, data, view, data2);
			if (data.win()) {
				System.out.println("You have won!");
				sleep(1000);
				return false;
			}
			return true;
		}
	}

	private static boolean sleep(int duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException ex) {
			return false;
		}
		return true;
	}

	public static void graphicBoard(ApplicationContext context, SimpleGameData data, SimpleGameData data2) {
		var screenInfo = context.getScreenInfo();
		var width = screenInfo.width();
		var height = screenInfo.height();
		var margin = 50;

		var animalImages = new String[] { "bear.png", "elk.png", "fox.png", "eagle.png", "salmon.png"};
		var habitatImages = new String[] { "mountain.png", "forest.png", "river.png", "grassland.png", "wetland.png"};
		var blankImage = "blank.png";
		var images = new ImageLoader("data", animalImages, habitatImages, blankImage);
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