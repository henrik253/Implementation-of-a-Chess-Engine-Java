package main.model;

import main.Settings;
import main.gui.MainPresenter;
import main.model.chessPieces.SimplePiece;
import main.model.chessPieces.concretePieces.Piece;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;
import main.model.start.FENConverter;

public class Model {

	private Settings settings;
	private BoardRepresentation board;
	private MoveValidation moveValidation;

	public void startGame() {
		startGame(settings.selectedFEN.get());
		moveValidation.setBoard(board);
	}

	public void startGame(String fen) {
		this.board = new BoardRepresentation(FENConverter.convertPieceBoard(fen));
	}

	public boolean movePiece(int oldColumn, int oldRow, int newColumn, int newRow) {
		return moveValidation.makeMove(new Vector2D(oldColumn, oldRow), new Vector2D(newColumn, newRow));
	}

	public Piece[][] getBoard() {
		return board.getBoard();
	}

	public MoveValidation getMoveValidation() {
		return moveValidation;
	}

	public void setMoveValidation(MoveValidation moveValidation) {
		this.moveValidation = moveValidation;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public BoardRepresentation getBoardRepresentatio() {
		return board;
	}

	public void setBoardRepresentatio(BoardRepresentation board) {
		this.board = board;
	}



}
