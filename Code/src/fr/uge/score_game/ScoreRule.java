package fr.uge.score_game;

import fr.uge.DataGame.GamingBoard;

public sealed interface ScoreRule permits FamilyAndIntermediateScore, CardScore {
	int calculateScore(GamingBoard gb);
}
