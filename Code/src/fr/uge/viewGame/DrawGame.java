package fr.uge.viewGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import com.github.forax.zen.ApplicationContext;

import fr.uge.DataGame.Player;
import fr.uge.DataGame.Tile;

public class DrawGame {

	public static void render(ApplicationContext context, Tile tile, Player player, Map<String, Image> mapSkinToImage, float width, float height) { 
		
	}
	
	private static void drawPlayerScore(Graphics2D graphics, Player player, int posX, int posY) {
	    graphics.setColor(Color.BLACK);
	    graphics.fill(new Rectangle2D.Float(posX, posY, 150, 30));
	    graphics.setColor(Color.WHITE);
	    graphics.drawString("Score: " + player.getScore(), posX + 10, posY + 20);
	}

//	private static void drawOptionTiles (Graphics2D graphics, ) {
//		
//	}
	
	private static void drawOptionAnimals () {
		
	}
	
	private static void drawStartTiles() {
		
	}
	
	
}
