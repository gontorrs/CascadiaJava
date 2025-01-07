package fr.uge.score_game;
import fr.uge.DataGame.GamingBoard;
import java.util.Objects;

public final class IntermediateScore implements ScoreRule {
	@Override
	public int calculateScore(GamingBoard gb) {
		Objects.requireNonNull(gb);
		return 0;
	}

}
