package fr.uge.score_game;

import java.util.Objects;

import fr.uge.Cascadia.GamingBoard;

public final class IntermediateScore implements ScoreRule {
	@Override
	public int calculateScore(GamingBoard gb) {
		Objects.requireNonNull(gb);
	}
}
