package main.model;

import main.Settings;
import main.gui.MainPresenter;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;
import main.model.start.FENConverter;

public class Model {

	private Settings settings;
	private BoardRepresentation board;
	private MoveValidation moveValidation;
	private MainPresenter mainPresenter; 

	public void startGame() {
		startGame(settings.selectedFEN.get());
		moveValidation.setBoard(board);
	}

	public void startGame(String fen) {
		this.board = new BoardRepresentation(FENConverter.convertPieceBoard(fen));
	}

	public boolean move(int oldColumn, int oldRow, int newColumn, int newRow) {
		// FIX THIS !!!!!!!!!! new Vector2D(oldRow,oldColumn),new Vector2D(newColumn,newRow)
		return moveValidation.makeMove(new Vector2D(oldRow,oldColumn),new Vector2D(newColumn,newRow));
	}
	
	public void removePieceFromBoard(Vector2D pos) {
		mainPresenter.removePieceFromBoard(pos); 
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

	public BoardRepresentation getBoard() {
		return board;
	}

	public void setBoard(BoardRepresentation board) {
		this.board = board;
	}
	
	public MainPresenter getMainPresenter() {
		return mainPresenter;
	}

	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

}
