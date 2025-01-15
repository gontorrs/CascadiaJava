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
		var squareSize = length / data.width();
		return new SimpleGameView(xOrigin, yOrigin, length, data.height() * squareSize, squareSize, loader);
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

	private BufferedImage getAnimalImage(int animalId, int userTile, ImageLoader loader) {
	    if (animalId == -1) {
	        return loader.blankImg(); // Sin animal
	    } else {
	        if (userTile == 3) {
	            return loader.animalImage(animalId);
	        } else {
	            return loader.userImage(animalId);
	        }
	    }
	}

	// Método auxiliar para obtener la imagen del hábitat
	private BufferedImage getHabitatImage(int habitatId, ImageLoader loader) {
	    if (habitatId == -1) {
	        return loader.blankImg();
	    } else {
	        return loader.habitatImage(habitatId);
	    }
	}

	private void drawCell(Graphics2D graphics, SimpleGameData data, int i, int j) {
		var x = xFromI(i);
		var y = yFromJ(j);
		BufferedImage animalImage1;
		BufferedImage animalImage2;
		BufferedImage habitatImage;

		// Obtener el Tile actual
		Tile tile = data.getMatrix().get(new Position(i, j));

		// Verificar la visibilidad de la celda
		if (!data.isVisible(i, j)) {
			// Si no es visible, usar imágenes en blanco
			animalImage1 = loader.blankImg();
			animalImage2 = loader.blankImg();
			habitatImage = loader.blankImg();
		} else {
			// Obtener las imágenes utilizando los métodos auxiliares
			animalImage1 = getAnimalImage(tile.idAnimal(), tile.userTile(), loader);
			animalImage2 = getAnimalImage(tile.secondAnimalId(), tile.userTile(), loader);
			habitatImage = getHabitatImage(tile.idHabitat(), loader);
		}

		// Dibujar las imágenes en la celda
		int cellSize = squareSize;
		int imageSize = cellSize / 3;

		drawImage(graphics, habitatImage, x + cellSize / 2 + 2, y + 2, imageSize, imageSize);
		drawImage(graphics, animalImage1, x + 2, y + 2, imageSize, imageSize);
		drawImage(graphics, animalImage2, x + cellSize / 2 + 2, y + imageSize + 4, imageSize, imageSize);

		// Dibujar el borde de la celda
		graphics.setColor(Color.BLACK);
		graphics.drawRect((int) x, (int) y, cellSize, cellSize);
	}

	private void draw(Graphics2D graphics, SimpleGameData data1, SimpleGameData data2) {
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, height, width));
		for (int i = 0; i < data1.width(); i++) {
			for (int j = 0; j < data1.height(); j++) {
				drawCell(graphics, data1, i, j);
			}
		}

		float newXOrigin = xOrigin + width + 200;

		for (int i = 0; i < data2.width(); i++) {
			for (int j = 0; j < data2.height(); j++) {
				graphics.translate(newXOrigin - xOrigin, 0);
				drawCell(graphics, data2, i, j);
				graphics.translate(-(newXOrigin - xOrigin), 0);
			}
		}
	}

	public static void draw(ApplicationContext context, SimpleGameData data, SimpleGameView view,
			SimpleGameData data2) {
		context.renderFrame(graphics -> view.draw(graphics, data, data2));
	}
}