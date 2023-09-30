package main.gui.game.board;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.SimplePiece;
import utils.Vector2D;

public class GameView extends Pane {

	private GamePresenter gamePresenter;

	private Board board;

	public GameView() {

	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public boolean moveRequest(Vector2D oldPos, Vector2D newPos) {
		return gamePresenter.moveRequest(oldPos, newPos);
	}

	public void initSimplePieceBoard(SimplePiece[][] simplePieceBoard) {
		this.board.drawPiecesOnBoard(simplePieceBoard);
	}

	public void loadSimpleBoard(SimplePiece[][] board) {
		this.board.drawPiecesOnBoard(board);
	}

	public void setBoard(Board board) {
		this.board = board;
		this.getChildren().add(board);
	}

	public void setPieceListenerDisabled(ChessPieceColor color, boolean disabled) {
		if (disabled)
			board.disablePieceListener(color);
		else
			board.enablePieceListener(color);
	}

	public GamePresenter getGamePresenter() {
		return gamePresenter;
	}

	public void setGamePresenter(GamePresenter gamePresenter) {
		this.gamePresenter = gamePresenter;
	}
//
//	public void removePieceFromBoard(Vector2D pos) {
//		board.removePiece(pos);
//		
//	}

	public void userPlaysAs(ChessPieceColor selectedColor) {
		board.setInverted(selectedColor.isBlack());
	}

	public void userMoveSucceeded() {
		gamePresenter.userMoveSucceeded();
	}

	public void markSquare(Vector2D oldPos, Color color) {
		board.markSquare(oldPos, color);
	}

	public void unmarkSquare(Vector2D oldPos) {
		board.unmarkSquare(oldPos);

	}

}
