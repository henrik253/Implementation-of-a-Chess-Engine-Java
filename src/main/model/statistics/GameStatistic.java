package main.model.statistics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.model.Move;
import main.model.chessPieces.ChessPieceColor;
import main.model.gameStates.State;

public class GameStatistic {

	private Map<String, Integer> userWon = new HashMap<>();
	private Map<String, Integer> userRemis = new HashMap<>();

	private Map<String, Integer> playedGames = new HashMap<>();
	private Map<String, List<Move>> moveHistory = new HashMap<>();

	public GameStatistic() {
	}

	public void enterMove(Move move, String bot, ChessPieceColor userColor) {
		List<Move> moves = moveHistory.getOrDefault(bot, new LinkedList<>());
		moves.add(move);
		moveHistory.put(bot, moves);
	}

	public List<Move> getMoves(String bot) {
		return moveHistory.getOrDefault(bot, new LinkedList<>());
	}

	public Integer getRoundCount(String bot) {
		return moveHistory.size();
	}

	public void enterGameWinner(String bot, ChessPieceColor userColor) {
		playedGames.put(bot, playedGames.getOrDefault(bot, 0) + 1);

		if (userWon(userColor) && State.gameState.isGameOver()) {
			userWon.put(bot, userWon.getOrDefault(bot, 0) + 1);
			System.out.println(userWon);
		}
	}

	public void enterDraw(String string) {
		userRemis.put(string, userRemis.getOrDefault(string, 0) + 1);
	}

	private boolean userWon(ChessPieceColor botColor) {
		return (State.gameOverReason.isBlackWon() && botColor.isWhite())
				|| (State.gameOverReason.isWhiteWon() && botColor.isBlack());
	}

	public int getUserWins(String bot) {
		return userWon.getOrDefault(bot, 0);
	}

	public int getPlayedGamesAgainstBot(String bot) {
		return playedGames.getOrDefault(bot, 0);
	}

	public int getBotWins(String bot) {
		return getPlayedGamesAgainstBot(bot) - getUserWins(bot);
	}

	public int getDraws(String bot) {
		return userRemis.get(bot);
	}

	public int getTotalUserWins() {
		int userWins = 0;
		for (Entry<String, Integer> entry : userWon.entrySet()) {
			userWins += entry.getValue();
		}
		return userWins;
	}

	public int getTotalBotWins() {
		int userWins = getTotalUserWins();
		int totalGames = 0;
		for (Entry<String, Integer> entry : playedGames.entrySet()) {
			totalGames += entry.getValue();
		}
		return totalGames - userWins;
	}

	@Override
	public String toString() {
		return userWon + " \n" + playedGames + " \n" + moveHistory;
	}

}
