package main.gui.game.board.presenter;

import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.Settings;
import main.gui.MainPresenter;
import main.gui.game.board.view.GameView;
import utils.ChessPieceColor;
import utils.Move;
import utils.SimplePiece;
import utils.Vector2D;

public class GamePresenter {
	private GameView gameView;
	private MainPresenter mainPresenter;
	private Settings settings;

	private Vector2D markedSquareOld;
	private Vector2D markedSquareNew;

	private Vector2D lastMarkedKingPos;

	private List<Vector2D> moveablePositions;
	private Vector2D piecePosition;

	private Service<SimplePiece[][]> chessBotCalculation = new ChessBotCalculationService();

	public GamePresenter() {
		initChessBotCalculation();
	}

	public void setBoard(SimplePiece[][] simplePieceBoard) {
		gameView.initSimplePieceBoard(simplePieceBoard);
	}

	public boolean moveRequest(Vector2D oldPos, Vector2D newPos) {
		boolean isValidMove = mainPresenter.moveRequest(oldPos, newPos);

		if (isValidMove) {
			gameView.loadSimpleBoard(mainPresenter.getGameBoard());
			markCheckedKing();
			markSquaresPieceMoved(oldPos, newPos);
		}

		return isValidMove;
	}

	public void userMoveSucceeded() {
		chessBotCalculation.restart();
	}

	public void startGame() {
		if (markedSquareOld != null && markedSquareNew != null) {
			gameView.unmarkSquare(markedSquareOld);
			gameView.unmarkSquare(markedSquareNew);
		}

		if (lastMarkedKingPos != null) {
			gameView.unmarkSquare(lastMarkedKingPos);
		}
	}

	private void markCheckedKing() {
		boolean inv = gameView.isBoardInverted();
		int len = settings.columns - 1;
		if (mainPresenter.kingInCheck()) {
			lastMarkedKingPos = mainPresenter.getKingCheckedPos();
			gameView.markSquare(inv ? lastMarkedKingPos.getInverted(len) : lastMarkedKingPos,
					settings.kingCheckMarked.get());
		} else if (lastMarkedKingPos != null) {
			gameView.unmarkSquare(inv ? lastMarkedKingPos.getInverted(len) : lastMarkedKingPos);
		}
	}

	private void markSquaresPieceMoved(Vector2D oldPos, Vector2D newPos) {
		if (markedSquareOld != null && markedSquareNew != null) {
			gameView.unmarkSquare(markedSquareOld);
			gameView.unmarkSquare(markedSquareNew);
		}

		gameView.markSquare(oldPos, settings.markedColorBright.get());
		gameView.markSquare(newPos, settings.markedColorDark.get());

		markedSquareOld = oldPos;
		markedSquareNew = newPos;
	}

	public void setPieceListenerDisabled(ChessPieceColor color, boolean disabled) {
		gameView.setPieceListenerDisabled(color, disabled);
	}

	public GameView getGameView() {
		return gameView;
	}

	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

	public void userPlaysAs(ChessPieceColor selectedColor) {
		gameView.userPlaysAs(selectedColor);
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void markMoveableSquares(Vector2D pos) {
		piecePosition = pos;
		final int len = settings.columns - 1; // either columns or rows, does not matter
		final boolean inv = gameView.isBoardInverted();

		moveablePositions = mainPresenter.getMoveablePositions(inv ? pos.getInverted(len) : pos);

		gameView.markSquare(pos, settings.markedColorBright.get());
		moveablePositions.forEach(position -> gameView.markSquare(inv ? position.getInverted(len) : position,
				settings.moveablePosMarked.get()));
	}

	public void unMarkMoveableSquares() {
		if (moveablePositions == null)
			return;

		final boolean inv = gameView.isBoardInverted();
		final int len = settings.columns - 1;
		gameView.unmarkSquare(inv ? piecePosition.getInverted(len) : piecePosition);
		moveablePositions.forEach(position -> gameView.unmarkSquare(inv ? position.getInverted(len) : position));

	}

	// when ChessBotCalculation finished the Executer draws the board
	private void initChessBotCalculation() {
		chessBotCalculation.setOnSucceeded(event -> {
			SimplePiece[][] b = chessBotCalculation.getValue();
			System.out.println("finished");
			System.out.println(b);
			Platform.runLater(() -> gameView.loadSimpleBoard(b));
		});
	}

	public void stopChessBotCalculation() throws InterruptedException {
		chessBotCalculation.cancel();
	}

	private class ChessBotCalculationService extends Service<SimplePiece[][]> {

		@Override
		protected Task<SimplePiece[][]> createTask() {
			return new ChessBotCalculationTask();
		}
	}

	private class ChessBotCalculationTask extends Task<SimplePiece[][]> {

		@Override
		protected SimplePiece[][] call() throws Exception {
			SimplePiece[][] board = null;
			try {
				board = mainPresenter.requestBotMove();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Move botMove = mainPresenter.getLastBotMove();
			Platform.runLater(() -> {
				markCheckedKing();
				final Vector2D oldPos = botMove.from(), newPos = botMove.to();
				boolean inv = gameView.isBoardInverted();
				int len = settings.columns - 1;
				markSquaresPieceMoved(inv ? oldPos.getInverted(len) : oldPos, inv ? newPos.getInverted(len) : newPos);
			});

			System.out.println("return board");
			System.out.println("current played Move: " + botMove);
			return board;
		}

	}

}
