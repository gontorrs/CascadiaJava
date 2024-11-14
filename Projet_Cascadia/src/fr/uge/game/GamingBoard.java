package fr.uge.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamingBoard {
	private final List<Animal> animals;
	private final List<Habitat> habitats;

	public GamingBoard() {
		animals = new ArrayList<>();
		habitats = new ArrayList<>();
	}
	
	// For play 
	private String randomV(int opt) {
		Random r = new Random();
		int randomValue = 0;
		switch (opt) {
		case 1: {
			Animal[] animals = Animal.values();
			randomValue = r.nextInt(animals.length);
			Animal randomAnimal = animals[randomValue];
			return randomAnimal.toString();
		}
		case 2: {
			Habitat[] habitats = Habitat.values();
			randomValue = r.nextInt(habitats.length);
			Habitat randomHabitat = habitats[randomValue];
			return randomHabitat.toString();
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + opt);
		}
	}
	
	private List<Animal> initialAnimals() {
		String animal1 = randomV(1);
		String animal2 = randomV(1);
		String animal3 = randomV(1);
		List<Animal> listeA = new ArrayList<>();
		Random r = new Random();
		int randomNb = r.nextInt(4);
		if (randomNb == 0) {
			String animal4 = randomV(1);
		}
		return listeA;
	}
	
	public void initialTile() {
		String[] animals = {randomV(1), randomV(1), randomV(1)};
		String[] habitats = {randomV(2), randomV(2), randomV(2)};
		for(int i = 0;i < 3;i++) {
			drawTile(animals[i], habitats[i]);
		}
	}

	public void drawTile(String word1, String word2) {
	    // Store the words in an array for easy looping
	    String[] words = { word1, word2 };

	    // Determine the maximum word length and set the width with padding
	    int maxWordLength = Math.max(word1.length(), word2.length());
	    int width = maxWordLength + 4; // +4 for padding and borders

	    // Print the top border
	    System.out.print("+");
	    for (int i = 0; i < width - 2; i++) {
	        System.out.print("-");
	    }
	    System.out.println("+");

	    // Print each word with padding using a loop
	    for (String word : words) {
	        System.out.print("| ");
	        System.out.print(String.format("%-" + maxWordLength + "s", word));
	        System.out.println(" |");
	    }

	    // Print the bottom border
	    System.out.print("+");
	    for (int i = 0; i < width - 2; i++) {
	        System.out.print("-");
	    }
	    System.out.println("+");
	}
	public void drawGrid(String word1, String word2) {
		int rows = 8;
		int cols = 8;
	    // Loop through each row of the grid
	    for (int row = 0; row < rows; row++) {
	        // Loop through each column of the grid
	        for (int col = 0; col < cols; col++) {
	            // Call the drawTile method to draw each tile
	            drawTile(word1, word2);

	            // Print a space between tiles in the same row (except after the last tile)
	            if (col < cols - 1) {
	                System.out.print(" "); // Add a space to separate tiles horizontally
	            }
	        }
	        
	        // Print a newline after each row to move to the next row in the grid
	        System.out.println();
	    }
	}
}
