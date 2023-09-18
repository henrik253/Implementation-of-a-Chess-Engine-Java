package main.model.gameLogic;

import main.model.Move;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.King;
import main.model.chessPieces.concretePieces.Pawn;
import main.model.chessPieces.concretePieces.Piece;

import java.util.LinkedList;
import java.util.List;

public class BoardRepresentation {

	private Piece[][] board;
	private int[][] attackedSquaresByWhite;
	private int[][] attackedSquaresByBlack;

	private List<Piece> whitePieces;
	private List<Piece> blackPieces;

	private List<Piece> capturedPieces;

	private List<Move> moveHistory;

	private King whiteKing;
	private King blackKing;

	private Move lastMove; // not init. ! 

	public BoardRepresentation(Piece[][] board) {
		this.board = board;
		whitePieces = new LinkedList<>();
		blackPieces = new LinkedList<>();
		capturedPieces = new LinkedList<>();
		moveHistory = new LinkedList<>();
		initPieces();
		setBoardForPieces(); // before initPieces() !
		calcAttackedSquaresBy(ChessPieceColor.WHITE);
		calcAttackedSquaresBy(ChessPieceColor.BLACK);

	}

	public BoardRepresentation(Piece[][] board, BoardRepresentation boardRepresentation) {
		this.board = board;
		whitePieces = boardRepresentation.whitePieces;
		blackPieces = boardRepresentation.blackPieces;
		capturedPieces = boardRepresentation.capturedPieces;
		whiteKing = boardRepresentation.whiteKing;
		blackKing = boardRepresentation.blackKing;
		lastMove = boardRepresentation.lastMove;

		setBoardForPieces(); // before initPieces() !
		calcAttackedSquaresBy(ChessPieceColor.WHITE);
		calcAttackedSquaresBy(ChessPieceColor.BLACK);
	}

	private void initPieces() {
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				Piece piece = board[row][column];
				if (piece != null) {
					if (piece.getColor().isWhite()) {
						whitePieces.add(piece);

						if (piece instanceof King) {
							whiteKing = (King) piece;
						}
					} else {
						blackPieces.add(piece);

						if (piece instanceof King) {
							blackKing = (King) piece;
						}
					}
				}
			}
		}
	}

	private void setBoardForPieces() {
		whitePieces.forEach(piece -> piece.setBoard(this));
		blackPieces.forEach(piece -> piece.setBoard(this));
	}

	public BoardRepresentation(int length) {
		this.board = new Piece[length][length];
		this.attackedSquaresByWhite = calcAttackedSquaresBy(ChessPieceColor.WHITE);
		this.attackedSquaresByBlack = calcAttackedSquaresBy(ChessPieceColor.BLACK);
	}

	public int[][] calcAttackedSquaresBy(ChessPieceColor color) {
		int[][] attackedSquares = new int[board.length][board.length];
		if (color.isWhite()) {
			whitePieces.forEach(piece -> markAttackedSquares(piece, attackedSquares));
			attackedSquaresByWhite = attackedSquares; // Save for later use
		} else {
			blackPieces.forEach(piece -> markAttackedSquares(piece, attackedSquares));
			attackedSquaresByBlack = attackedSquares; // Save for later use
		}
		return attackedSquares;
	}

	private void markAttackedSquares(Piece piece, int[][] attackedSquares) {
		List<List<Vector2D>> moves = piece.calculateAttackablePositions(piece.getPosition());
		moves.forEach(movesInDir -> movesInDir.forEach(m -> attackedSquares[m.getY()][m.getX()]++));
	}

	public void makeMove(Vector2D oldPos, Vector2D newPos) {
		lastMove = new Move(oldPos, newPos);

		Piece movedPiece = this.getPiece(oldPos);

		removePiece(newPos);
		movedPiece.executeMove(oldPos, newPos);
		moveHistory.add(lastMove);
	}

	public void undoLastMove() {
		Vector2D oldPos = lastMove.getOldPos(), newPos = lastMove.getNewPos();
		Piece capturedPiece = lastMove.getCapturedPiece();

		makeMove(newPos, oldPos);
		board[newPos.getY()][newPos.getX()] = capturedPiece;
	}

	public void removePiece(Vector2D pos) {

		if (isPieceOn(pos)) {
			Piece piece = this.getPiece(pos);

			if (piece.getColor().isWhite()) {
				whitePieces.remove(piece);
			} else
				blackPieces.remove(piece);

			capturedPieces.add(piece);
			lastMove.setCapturedPiece(piece);
		}
	}

	public boolean isEnemyPieceOn(Vector2D pos, ChessPieceColor color) {
		Piece piece = getPiece(pos);
		return piece != null && piece.getColor() != color;
	}

	public boolean isNoPieceOn(Vector2D pos) {
		return getPiece(pos) == null;
	}

	public boolean isPieceOn(Vector2D pos) {
		return getPiece(pos) instanceof Piece;
	}

//	public void makeSpecialMove(Vector2D oldPos, Vector2D newPos) { // e.g. castling
//		Piece movedPiece = this.getPiece(oldPos);
//		Piece pieceOnNewPos = this.getPiece(newPos);
//
//		if (movedPiece instanceof King && ((King) movedPiece).isValidCastle(newPos)) {
//			executeCastling((King) movedPiece, oldPos, newPos);
//		} else if (movedPiece instanceof Pawn && getPiece(newPos) == null) {
//			executeEnPassant(oldPos, newPos); // == null to check if its really enPassant
//		} else { // in MoveValidation enPassant rules were checked so == null compare is enough
//			board[oldPos.getY()][oldPos.getX()] = null; // this case is for the Pawn Attack!
//			board[newPos.getY()][newPos.getX()] = movedPiece;
//		}
//
//		if (pieceOnNewPos != null) {
//			if (pieceOnNewPos.getColor().isWhite()) {
//				whitePieces.remove(pieceOnNewPos);
//			} else
//				blackPieces.remove(pieceOnNewPos);
//		}
//
//		movedPiece.setPosition(newPos);
//
//	}

	public void executeCastling(King king, Vector2D oldPos, Vector2D newPos) { // TODO in Game logic
		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;
		int xPosRook = isRightSideCastle ? this.board.length - 1 : 0;
		Vector2D rookPos = new Vector2D(xPosRook, king.getPosition().getY());

		Vector2D rookDirection = new Vector2D(isRightSideCastle ? -2 : 3, 0);
		Vector2D kingDirection = new Vector2D(isRightSideCastle ? 2 : -2, 0);
		makeMove(rookPos, Vector2D.add(rookPos, rookDirection));
		makeMove(king.getPosition(), Vector2D.add(king.getPosition(), kingDirection));
	}

	public void executeEnPassant(Vector2D oldPos, Vector2D newPos) { // TODO in Game logic
		boolean whiteOnMove = getPiece(oldPos).getColor().isWhite();
		Vector2D direction = new Vector2D(0, whiteOnMove ? 1 : -1);
		Piece attackedPiece = getPiece(Vector2D.add(newPos, direction));

		makeMove(oldPos, newPos);
		if (whiteOnMove)
			blackPieces.remove(attackedPiece);
		else
			whitePieces.remove(attackedPiece);

		Vector2D attackedPiecePos = attackedPiece.getPosition();
		board[attackedPiecePos.getY()][attackedPiecePos.getX()] = null; // or NoPiece!
	}

//	public boolean isCheckBetween(Vector2D kingPos, Vector2D rookPos) { // TODO in Game logic
//		int x = kingPos.getX() - rookPos.getX();
//		int y = kingPos.getY() - rookPos.getY();
//		int length = Math.max(Math.abs(x), Math.abs(y));
//
//		int countX = kingPos.getX();
//		int countY = kingPos.getY();
//
//		int incX = (x > 0 ? 1 : -1);
//		int incY = (y > 0 ? 1 : -1);
//
//		incX = x != 0 ? incX : 0;
//		incY = y != 0 ? incY : 0;
//
//		int[][] attackedSquares = getPiece(kingPos).getColor().isWhite() ? attackedSquaresByBlack
//				: attackedSquaresByWhite;
//
//		if (attackedSquares[kingPos.getX()][kingPos.getY()] > 0)
//			return true;
//
//		for (int step = 0; step < length - 1; step++) {
//			countX -= incX;
//			countY -= incY;
//			if (attackedSquares[countY][countX] > 0) {
//				return true;
//			}
//		}
//		return false;
//
//	}

	public int countFiguresBetween(Vector2D pos1, Vector2D pos2) {
		int x = pos1.getX() - pos2.getX();
		int y = pos1.getY() - pos2.getY();
		int length = Math.max(Math.abs(x), Math.abs(y));

		int countX = pos1.getX();
		int countY = pos1.getY();

		int incX = (x > 0 ? 1 : -1);
		int incY = (y > 0 ? 1 : -1);
		incX = x != 0 ? incX : 0;
		incY = y != 0 ? incY : 0;

		int count = 0;

		for (int step = 0; step < length - 1; step++) {
			countX -= incX;
			countY -= incY;
			if (this.board[countY][countX] != null) {
				count++;
			}
		}
		return count;
	}

	public Piece getPiece(Vector2D position) {
		return board[position.getY()][position.getX()];
	}

	public Piece[][] getBoard() {
		return board;
	}

	@Override
	public BoardRepresentation clone() { // !!!!!!!!!!!!!!

		BoardRepresentation clone = new BoardRepresentation(this.getBoardClone(), this);

		return clone;
	}

	public Piece[][] getBoardClone() {
		Piece[][] result = new Piece[board.length][board.length];
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				Piece p = this.board[row][column];
				if (p != null) {
					result[row][column] = p;
				}
			}
		}

		return result;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	public int[][] getAttackedSquaresByWhite() {
		return attackedSquaresByWhite;
	}

	public void setAttackedSquaresByWhite(int[][] attackedSquaresByWhite) {
		this.attackedSquaresByWhite = attackedSquaresByWhite;
	}

	public int[][] getAttackedSquaresByBlack() {
		return attackedSquaresByBlack;
	}

	public void setAttackedSquaresByBlack(int[][] attackedSquaresByBlack) {
		this.attackedSquaresByBlack = attackedSquaresByBlack;
	}

	public King getWhiteKing() {
		return whiteKing;
	}

	public King getBlackKing() {
		return blackKing;
	}

	public String toBoardString() {
		String result = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j] instanceof Piece) {
					result += board[i][j].toString().charAt(2);
				} else
					result += " ";

				result += " | ";
			}
			result += "\n";
			result += "_".repeat(40);
			result += "\n";
		}

		return result;
	}

	public Move getLastMove() {
		return lastMove;
	}

	public String toString() {
		String resultWhite = "";
		String resultBlack = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				resultWhite += attackedSquaresByWhite[i][j];

				resultBlack += attackedSquaresByBlack[i][j];

				resultWhite += " | ";
				resultBlack += " | ";
			}
			resultWhite += "\n";
			resultWhite += "_".repeat(40);
			resultWhite += "\n";

			resultBlack += "\n";
			resultBlack += "_".repeat(40);
			resultBlack += "\n";
		}

		return "WHITE: \n" + resultWhite + " \n" + "#".repeat(50) + "\n \n" + "BLACK: \n" + resultBlack + " \n"
				+ toBoardString();
	}

	public List<Piece> getWhitePieces() {
		return this.whitePieces;
	}

	public List<Piece> getBlackPieces() {
		return this.blackPieces;
	}
}
