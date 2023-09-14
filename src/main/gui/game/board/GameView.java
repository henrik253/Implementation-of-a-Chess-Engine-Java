package main.gui.game.board;

import javafx.scene.layout.Pane;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.SimplePiece;

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

}
