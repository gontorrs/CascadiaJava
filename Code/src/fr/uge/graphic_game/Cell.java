package fr.uge.graphic_game;
public class Cell {
	
	private final int secondAnimalId;
	private final int id;
	private boolean visible;
	public Cell(int i, int secondAnimalId) {
		id = i;
		this.secondAnimalId = secondAnimalId;
		visible = true;
	}
	public int id() {
		return id;
	}

	public void hide() {
		visible = false;
	}

	public void show() {
		visible = true;
	}

	public boolean visible() {
		return visible;
	}

	public int secondAnimalId() {
		return secondAnimalId;
	}
}
