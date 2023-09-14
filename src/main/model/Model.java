package main.model;

import main.ChessBotStub;
import main.Settings;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;
import main.model.convertions.FENConverter;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;

public class Model {

	private Settings settings;
	private BoardRepresentation boardRepresentation;
	private MoveValidation moveValidation;

	private ChessBot selectedChessBot;

	// GAME START 
	public void startGame() {
		startGame(settings.selectedFEN.get()); 
		selectedChessBot = new ChessBotStub();
		moveValidation.setBoard(boardRepresentation);
	}

	public void startGame(String fen) {
		this.boardRepresentation = new BoardRepresentation(FENConverter.convertPieceBoard(fen)); // <----
		this.moveValidation.setOnMove(ChessPieceColor.WHITE);
	}
	
	// GAME RUNNING
	public boolean movePiece(Vector2D oldPos, Vector2D newPos) { // <----
		return moveValidation.makeMove(oldPos, newPos);
	}

	public Piece[][] makeBotMove() {
		Piece[][] board = boardRepresentation.getBoard().clone();
		return selectedChessBot.makeMove(board);
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
