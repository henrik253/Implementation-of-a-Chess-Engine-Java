package main.gui.game.gameOver;

import java.util.HashMap;
import java.util.Map;
import main.gui.game.settings.settingsViewComponents.BotRepresentation;
import main.model.chessPieces.ChessPieceColor;
import main.model.gameStates.State;

public class GameStatistic {

	private Map<BotRepresentation, Integer> userWon = new HashMap<>();
	private Map<BotRepresentation, Integer> playedGames = new HashMap<>();

	public GameStatistic() {
	}

	public void gamePlayed(BotRepresentation bot) {
		playedGames.put(bot, playedGames.getOrDefault(bot, 0) + 1);

		if (userWon(bot)) {
			userWon.put(bot, userWon.getOrDefault(bot, 0) + 1);
		}
	}

	private boolean userWon(BotRepresentation bot) {
		ChessPieceColor userColor = bot.getUserColor();
		return (State.gameOverReason.isBlackWon() && userColor.isBlack())
				|| (State.gameOverReason.isWhiteWon() && userColor.isWhite());
	}

	public int getUserWins(BotRepresentation bot) {
		return userWon.get(bot);
	}

	public int getPlayedGamesAgainstBot(BotRepresentation bot) {
		return playedGames.get(bot);
	}

	public int getBotWins(BotRepresentation bot) {
		return playedGames.get(bot) - userWon.get(bot);
	}

}
