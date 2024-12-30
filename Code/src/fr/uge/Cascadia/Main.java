package fr.uge.Cascadia;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		GamePlay gp = new GamePlay(Difficulty.FAMILY);
		gp.summaryGame();
		gp.startGame();
		gp.endGame();
	}
}
