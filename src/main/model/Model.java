package main.model;

import main.Settings;
import main.model.chessPieces.concretePieces.Piece;
import main.model.convertions.FENConverter;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;

public class Model {

	private Settings settings;
	private BoardRepresentation boardRepresentation;
	private MoveValidation moveValidation;

	private ChessBot selectedChessBot; 
	
	public void startGame() {
		startGame(settings.selectedFEN.get());
		moveValidation.setBoard(boardRepresentation);
		
//		if(chessBot == null)
//			throw new IllegalArgumentException("No ChessBot selected");
		

	}

	public void startGame(String fen) {
		this.boardRepresentation = new BoardRepresentation(FENConverter.convertPieceBoard(fen)); // <----
		
		// moveValidation needs to be started. depending on the inserted FEN
	}

	public boolean movePiece(int oldColumn, int oldRow, int newColumn, int newRow) { // <----
		return moveValidation.makeMove(new Vector2D(oldColumn, oldRow), new Vector2D(newColumn, newRow));
	}
	
	public Piece[][] makeBotMove() {
		return selectedChessBot.makeMove();
	}
	
	public void setChessBot(ChessBot chessBot) {
		this.selectedChessBot = chessBot;
	}

	public Piece[][] getBoard() {
		return boardRepresentation.getBoard();
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

	public BoardRepresentation getBoardRepresentation() {
		return boardRepresentation;
	}

	public void setBoardRepresentation(BoardRepresentation board) {
		this.boardRepresentation = board;
	}

}
