package fr.uge.graphic_game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;

public record SimpleGameView(int xOrigin, int yOrigin, int height, int width, int squareSize, ImageLoader loader) {

	public static SimpleGameView initGameGraphics(int xOrigin, int yOrigin, int length, SimpleGameData data,
			ImageLoader loader) {
		Objects.requireNonNull(data);
		Objects.requireNonNull(loader);
		var squareSize = length / data.lines();
		return new SimpleGameView(xOrigin, yOrigin, length, data.columns() * squareSize, squareSize, loader);
	}

	private static void checkRange(double min, double value, double max) {
		if (value < min || value > max) {
			throw new IllegalArgumentException("Invalid coordinate: " + value);
		}
	}

	private int indexFromRealCoord(float coord, int origin) {
		return (int) ((coord - origin) / squareSize);
	}

	public int lineFromY(float y) {
		checkRange(yOrigin, y, y + width);
		return indexFromRealCoord(y, yOrigin);
	}

	public int columnFromX(float x) {
		checkRange(xOrigin, x, x + height);
		return indexFromRealCoord(x, xOrigin);
	}

	private float realCoordFromIndex(int index, int origin) {
		return origin + index * squareSize;
	}

	private float xFromI(int i) {
		return realCoordFromIndex(i, xOrigin);
	}

	private float yFromJ(int j) {
		return realCoordFromIndex(j, yOrigin);
	}
	

	private void drawImage(Graphics2D graphics, BufferedImage image, float x, float y, float dimX, float dimY) {
		var width = image.getWidth();
		var height = image.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
		var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
				y + (dimY - scale * height) / 2);
		graphics.drawImage(image, transform, null);
	}

	private void drawCell(Graphics2D graphics, SimpleGameData data, int i, int j) {
	    var x = xFromI(i);
	    var y = yFromJ(j);
	    BufferedImage animalImage1;
	    BufferedImage animalImage2;
	    BufferedImage habitatImage;

	    if (!data.isVisible(i, j)) {
	        animalImage1 = loader.blankImg();
	        animalImage2 = loader.blankImg();
	        habitatImage = loader.blankImg();
	    } 
	    else if (!data.isVisible(i, j)){
	    	animalImage1 = loader.animalImage(data.idAnimal(i, j) % 5);
	        animalImage2 = loader.animalImage(data.secondAnimalId(i, j) % 5);
	        habitatImage = loader.blankImg();
	    }
	    else {
	        animalImage1 = loader.animalImage(data.idAnimal(i, j) % 5);
	        animalImage2 = loader.animalImage(data.secondAnimalId(i, j) % 5);
	        habitatImage = loader.habitatImage(data.idHabitat(i, j) % 5);
	    }

	    int cellSize = squareSize;
	    int imageSize = cellSize / 3;

	    drawImage(graphics, habitatImage, x + cellSize / 2 + 2, y + 2, imageSize, imageSize);
	    drawImage(graphics, animalImage1, x + 2, y + 2, imageSize, imageSize);
	    drawImage(graphics, animalImage2, x + cellSize / 2 + 2, y + imageSize + 4, imageSize, imageSize);

	    graphics.setColor(Color.BLACK);
	    graphics.drawRect((int) x, (int) y, cellSize, cellSize);
	}

	private void draw(Graphics2D graphics, SimpleGameData data1, SimpleGameData data2) {
	    graphics.setColor(Color.WHITE);
	    graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, height, width));
	    for (int i = 0; i < data1.lines(); i++) {
	        for (int j = 0; j < data1.columns(); j++) {
	            drawCell(graphics, data1, i, j);
	        }
	    }

	    float newXOrigin = xOrigin + width + 200;

	    for (int i = 0; i < data2.lines(); i++) {
	        for (int j = 0; j < data2.columns(); j++) {
	            graphics.translate(newXOrigin - xOrigin, 0);
	            drawCell(graphics, data2, i, j);
	            graphics.translate(-(newXOrigin - xOrigin), 0);
	        }
	    }
	}

	public static void draw(ApplicationContext context, SimpleGameData data, SimpleGameView view, SimpleGameData data2) {
		context.renderFrame(graphics -> view.draw(graphics, data, data2));
	}
}