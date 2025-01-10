package fr.uge.graphic_game;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

public class SimpleGameController {
  public SimpleGameController() {

  }
  private static boolean gameLoop(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
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
      data.clickOnCell(view.columnFromX(location.x()), view.lineFromY(location.y()));
      SimpleGameView.draw(context, data, view);
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
  public static void graphicBoard(ApplicationContext context) {
    var screenInfo = context.getScreenInfo();
    var width = screenInfo.width();
    var height = screenInfo.height();
    var margin = 50;

    var animalImages = new String[]{"bear.png", "eagle.png", "elk.png", "fox.png", "salmon.png"};
    var habitatImages = new String[]{"forest.png", "grassland.png", "mountain.png", "river.png", "sea.png"};
    var images = new ImageLoader("data", animalImages, habitatImages);
    var data = new SimpleGameData(4, 4);
    var view = SimpleGameView.initGameGraphics(margin, margin, (int) Math.min(width, height) - 2 * margin, data,
        images);
    SimpleGameView.draw(context, data, view);

    while (true) {
      if (!gameLoop(context, data, view)) {
        System.out.println("Thank you for quitting!");
        context.dispose();
        return;
      }
    }
  }
}