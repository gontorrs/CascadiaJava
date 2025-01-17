package fr.uge.controllerGame;

import java.io.IOException;

import fr.uge.DataGame.Difficulty;
//Main function that creates the match between player one and two.
public class Main {
	public static void main(String[] args) throws IOException {
		GamePlay gp = new GamePlay(Difficulty.FAMILY);
		gp.summaryGame();
		gp.startGame();
		gp.endGame(gp.getGamingBoard1(), gp.getGamingBoard2());
	}
}
