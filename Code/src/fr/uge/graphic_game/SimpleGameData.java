package fr.uge.graphic_game;

import java.util.Random;
public class SimpleGameData {
	private final Cell[][] matrix;
	private int wins;
	public SimpleGameData(int nbLines, int nbColumns) {
		if (nbLines < 0 || nbColumns < 0) {
			throw new IllegalArgumentException();
		}
		matrix = new Cell[nbLines][nbColumns];
		wins = 0;
		randomise();
	}
	private void randomise() {
		var tab = new int[lines() * columns()];
		var random = new Random();
		for (var i = 0; i < tab.length; i++) {
		  var j = random.nextInt(i + 1);
		  tab[i] = tab[j];
		  tab[j] = i / 2;
		}
		for (var i = 0; i < tab.length; i++) {
		  int secondAnimalId = random.nextBoolean() ? (tab[i] + 1) % 5 : -1;
		  matrix[i % lines()][i / lines()] = new Cell(tab[i], secondAnimalId);
		}
	  }
	public int lines() {
		return matrix.length;
	}
	public int columns() {
		return matrix[0].length;
	}
	public int id(int i, int j) {
		return matrix[i][j].id();
	}
	public int secondAnimalId(int i, int j) {
		return matrix[i][j].secondAnimalId();
	}
	public void clickOnCell(int i, int j) {
		if (i < 0 || columns() <= i || j < 0 || lines() <= j || isVisible(i, j)) {
			return;
		}
		System.out.println("Clicked on cell: [" + i + "," +  j + "]");
	}
	public boolean win() {
		return 2 * wins == lines() * columns();
	}
	public boolean isVisible(int i, int j) {
		return matrix[i][j].visible();
	}
}