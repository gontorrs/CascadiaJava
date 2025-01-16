package fr.uge.graphic_game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import fr.uge.DataGame.Board;
import fr.uge.DataGame.GamingBoard;
import fr.uge.DataGame.OptionBoard;
import fr.uge.DataGame.Position;
import fr.uge.DataGame.Tile;

public record SimpleGameView(int xOrigin, int yOrigin, int height, int width, int squareSize, ImageLoader loader) {

	public static SimpleGameView initGameGraphics(int xOrigin, int yOrigin, int length, GamingBoard data,
			ImageLoader loader) {
		Objects.requireNonNull(data);
		Objects.requireNonNull(loader);
		var squareSize = length / data.getWidth();
		return new SimpleGameView(xOrigin, yOrigin, length, data.getHeight() * squareSize, squareSize, loader);
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

	private void drawCell(Graphics2D graphics, Board data, int i, int j) {
	    var x = xFromI(i);
	    var y = yFromJ(j);
	    BufferedImage animalImage1, animalImage2, habitatImage, number;

	    // Obtener el Tile actual
	    Tile tile = data.getBoardMap().get(new Position(i, j));

	    // Verificar la visibilidad de la celda
	    if (!data.isVisible(i, j)) {
	        animalImage1 = loader.blankImg();
	        animalImage2 = loader.blankImg();
	        habitatImage = loader.blankImg();
	        number = loader.blankImg();
	    } else {
	        // Obtener las imágenes utilizando los métodos auxiliares
	        animalImage1 = getAnimalImage(tile.idAnimal(), tile.userTile(), loader);
	        animalImage2 = getAnimalImage(tile.secondAnimalId(), tile.userTile(), loader);
	        habitatImage = getHabitatImage(tile.idHabitat(), loader);
	    }
	    
	    int cellSize = squareSize;
	    int imageSize = cellSize / 3;

	    drawImage(graphics, habitatImage, x + cellSize / 2 + 2, y + 2, imageSize, imageSize);
	    drawImage(graphics, animalImage1, x + 2, y + 2, imageSize, imageSize);
	    drawImage(graphics, animalImage2, x + cellSize / 2 + 2, y + imageSize + 4, imageSize, imageSize);
	    if(data.getClass() == OptionBoard.class) {
	    	int xOpt = j;
	        int yOpt = i;
	        if(xOpt < 2 || yOpt < 2) {
	        	number = loader.numberImage(((yOpt + 2 * (xOpt % 2)) % 4));
	        	drawImage(graphics, number, x + 2, y + cellSize - imageSize - 2, imageSize, imageSize);
	        }
	        else{
	        	xOpt = i - 2;
	        	yOpt = j - 2;
	        	number = loader.numberImage(((yOpt + 2 * (xOpt % 2)) % 4));
	        	drawImage(graphics, number, x + 2, y + cellSize - imageSize - 2, imageSize, imageSize);
	        }
	    }

	    graphics.setColor(Color.BLACK);
	    graphics.drawRect((int) x, (int) y, cellSize, cellSize);
	}

	private void draw(Graphics2D graphics, GamingBoard data, OptionBoard opt, int turn) {
		graphics.setColor(Color.WHITE);
		graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, height, width));
		for (int i = 0; i < data.getWidth(); i++) {
			for (int j = 0; j < data.getHeight(); j++) {
				drawCell(graphics, data, i, j);
			}
		}

		float newXOrigin = xOrigin + width + 200;

		for (int i = 0; i < opt.getWidth(); i++) {
			for (int j = 0; j < opt.getHeight(); j++) {
				graphics.translate(newXOrigin - xOrigin, 0);
				drawCell(graphics, opt, i, j);
				graphics.translate(-(newXOrigin - xOrigin), 0);
			}
		}
		//Draw the player image
	    float margin = 50;
	    float imageWidth = 200;
	    float imageHeight = 200;
	    float x = newXOrigin + opt.getWidth() * squareSize + margin;
	    float y = margin;
	    if(turn % 2 == 0) {
	    	BufferedImage image = loader.playerImage(0);
	    	drawImage(graphics, image, x, y, imageWidth, imageHeight);
	    }
	    else {
	    	BufferedImage image = loader.playerImage(1);
	    	drawImage(graphics, image, x, y, imageWidth, imageHeight);
	    }
	}

	public static void draw(ApplicationContext context, GamingBoard data, SimpleGameView view, OptionBoard opt, int turn) {
		context.renderFrame(graphics -> view.draw(graphics, data, opt, turn));
	}
}