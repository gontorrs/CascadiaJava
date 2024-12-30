package fr.uge.Cascadia;

import java.util.Objects;

public class Player {
	private final String name;
	private int score = 0; // to verify if we have if

	public Player(String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Player: " + name;
	}
}
