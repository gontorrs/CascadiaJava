package fr.uge.score_game;

import fr.uge.DataGame.GamingBoard;

public sealed interface ScoreRule permits FamilyAndIntermediateScore, CardScore, IntermediateScore, FamilyScore {
	int calculateScore(GamingBoard gb);
}
