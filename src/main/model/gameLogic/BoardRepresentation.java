package main.model.gameLogic;

import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

import java.util.LinkedList;
import java.util.List;

import main.model.pieces.King;
import main.model.pieces.Pawn;
import main.model.pieces.Piece;
import main.model.pieces.Queen;
import main.model.pieces.Rook;

public class BoardRepresentation {

	private Piece[][] board;
	private int[][] attackedSquaresByWhite;
	private int[][] attackedSquaresByBlack;

	private List<Piece> whitePieces;
	private List<Piece> blackPieces;
	private List<Piece> capturedPieces;

	private King whiteKing;
	private King blackKing;

	private List<Move> moveHistory;

	private Move lastMove; // not init. !
	private Move currentMove;

	private Piece[][] copy;

	public BoardRepresentation(Piece[][] board) {
		this.board = board;
		whitePieces = new LinkedList<>();
		blackPieces = new LinkedList<>();
		capturedPieces = new LinkedList<>();
		moveHistory = new LinkedList<>();
		initPieces();
		setBoardForPieces(); // before initPieces() !
		attackedSquaresByWhite = calcAttackedSquaresBy(ChessPieceColor.WHITE);
		attackedSquaresByBlack = calcAttackedSquaresBy(ChessPieceColor.BLACK);

	}

	public BoardRepresentation(Piece[][] board,
			final BoardRepresentation boardRepresentation) {
		this.board = board.clone();
		whitePieces = new LinkedList<>(boardRepresentation.whitePieces);
		blackPieces = new LinkedList<>(boardRepresentation.blackPieces);
		capturedPieces = new LinkedList<>(boardRepresentation.capturedPieces);
		whiteKing = (King) boardRepresentation.whiteKing.clone();
		blackKing = (King) boardRepresentation.blackKing.clone();
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
		this.attackedSquaresByWhite = calcAttackedSquaresBy(
				ChessPieceColor.WHITE);
		this.attackedSquaresByBlack = calcAttackedSquaresBy(
				ChessPieceColor.BLACK);
	}

	public int[][] calcAttackedSquaresBy(ChessPieceColor color) {
		int[][] attackedSquares = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] != null && board[i][j].getColor() == color) {
					markAttackedSquares(board[i][j], attackedSquares);
				}
			}
		}
		return attackedSquares;
	}

	private void markAttackedSquares(Piece piece, int[][] attackedSquares) {
		List<List<Vector2D>> moves = piece
				.calculateAttackableAndMoveablePositions();
		moves.forEach(movesInDir -> movesInDir
				.forEach(m -> attackedSquares[m.getY()][m.getX()]++));
	}

	public void makeMove(Vector2D oldPos, Vector2D newPos) {
		copy = getBoardClone();
		currentMove = new Move(oldPos, newPos);
		// order in which funcs are called is important!
		Piece movedPiece = getPiece(oldPos); // get the piece

		if (movedPiece == null) {
			throw new NullPointerException("\n couldnt find a piece at " + oldPos
					+ "\n Board: \n" + this.toBoardString());
		}

		currentMove.setMovedPiece(movedPiece);
		Piece removedPiece = removePiece(newPos); // remove the piece that is on
													// the pos the moving piece
													// want
													// to get
		movedPiece.executeMove(oldPos, newPos);

		lastMove = currentMove;
		lastMove.setCapturedPiece(removedPiece);
		moveHistory.add(lastMove);
	}

	public void undoLastMove() {
		// STEPS OF UNDO
		// 1. if piece captured get it
		// 2. bring moved piece back to pos
		// 3. (bring captured piece back to his pos or transform e.g.
		// Pawn->Queen to
		// Queen->Pawn)
		// if it was first move of movedPiece then it needs to be set back!
		// 4. refresh lists that manage pieces e.g whitePieces, blackPieces,
		// capturedPieces

		Vector2D oldPos = lastMove.getOldPos(), newPos = lastMove.getNewPos();
		Piece capturedPiece = lastMove.getCapturedPiece();
		Piece movedPiece = lastMove.getMovedPiece(); // getPiece(newPos); throws
														// Error !

		if (movedPiece == null) {
			throw new NullPointerException(" couldnt find a piece at " + oldPos
					+ "\n Board: \n" + this.toBoardString());
		}

		// UNDO the Move
		board[oldPos.getY()][oldPos.getX()] = movedPiece;
		board[newPos.getY()][newPos.getX()] = capturedPiece; // captured Piece or null 
		
		movedPiece.setPosition(oldPos);

		if (lastMove.pieceGotCaptured()) {
			capturedPiece.setPosition(newPos);
			capturedPieces.remove(capturedPiece);	
			getPieces(capturedPiece.getColor()).add(capturedPiece);
		}	

		if (lastMove.wasFirstMoveOfMovedPiece()) {
			movedPiece.setFirstMove(true);
		}
		
		if(lastMove.isCastlingMove()) {
			Vector2D oldRookPos = lastMove.getOldRookPos(), newRookPos = lastMove.getNewRookPos();
			Rook rook = lastMove.getRook();
			rook.setPosition(oldRookPos);
			board[newRookPos.getY()][newRookPos.getX()] = null;
			board[oldRookPos.getY()][oldRookPos.getX()] = rook;
		}

		if (lastMove.pieceTransformed()) {
			Piece to = lastMove.getTransformed(); // e.g. the Pawn
			Piece from = getPiece(lastMove.getOldPos()); // TODO null ptr exc.															// bc. from == null
			// e.g. the queen?
			to.setPosition(lastMove.getOldPos().clone());
			from.setPosition(lastMove.getOldPos().clone()); // important step
			replacePiece(from, to);
		}

		moveHistory.remove(moveHistory.size() - 1);

		if (moveHistory.size() > 0)
			lastMove = moveHistory.get(moveHistory.size() - 1);
	}

	// public void undoLastMove() {
	// board = copy;
	//
	// moveHistory.remove(moveHistory.size() - 1);
	// if (moveHistory.size() > 0)
	// lastMove = moveHistory.get(moveHistory.size() - 1);
	// }

	public Piece removePiece(Vector2D pos) {
		Piece piece = getPiece(pos);
		if (piece != null) {
			if (piece.getColor().isWhite()) {
				whitePieces.remove(piece);
			} else
				blackPieces.remove(piece);
			capturedPieces.add(piece);
		}
		return piece;
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

	public boolean isCheckOn(Piece king, Vector2D pos) {
		ChessPieceColor enemyPieceColor = king.getColor().getOpponentColor();
		int[][] attackedSquares = enemyPieceColor.isWhite()
				? attackedSquaresByBlack
				: attackedSquaresByWhite;
		return attackedSquares[pos.getY()][pos.getX()] > 0;
	}

	public List<Piece> getWhitePieces() {
		return whitePieces;
	}

	public List<Piece> getBlackPieces() {
		return blackPieces;
	}

	public List<Piece> getPieces(ChessPieceColor color) {
		return color.isWhite() ? whitePieces : blackPieces;
	}

	// TODO TEST THE BUGS!
	public void replacePiece(Piece from, Piece to) {
		ChessPieceColor color = from.getColor();
		int x = from.getPosition().getX();
		int y = from.getPosition().getY();

		removePiece(from.getPosition());
		board[y][x] = to;
		to.setBoard(this);
		currentMove.setTransformedPiece(from);
		if (color.isWhite())
			whitePieces.add(to);
		else
			blackPieces.add(to);
	}

	public Piece getPiece(Vector2D position) {
		Piece p = board[position.getY()][position.getX()];

		return p;
	}

	public Piece[][] getBoard() {
		return board;
	}

	@Override
	public BoardRepresentation clone() {
		BoardRepresentation clone = new BoardRepresentation(
				this.getBoardClone(), this);
		return clone;
	}

	public Piece[][] getBoardClone() {
		Piece[][] result = new Piece[board.length][board.length];
		for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board[row].length; column++) {
				Piece p = this.board[row][column];
				if (p != null) {
					result[row][column] = p; // cloning the p wont change the
												// relying board of p
				}
			}
		}
		return result;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	public List<Move> getMoveHistory() {
		return moveHistory;
	}

	public Move getLastMove() {
		return this.lastMove;
	}

	public String toBoardString() {
		String result = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j] instanceof Piece) {
					result += board[i][j].getShortName();
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

	public Move getCurrentMove() {
		return currentMove;
	}

	public String toString() {
		// String resultWhite = "";
		// String resultBlack = "";
		// for (int i = 0; i < board.length; i++) {
		// for (int j = 0; j < board[i].length; j++) {
		// resultWhite += attackedSquaresByWhite[i][j];
		//
		// resultBlack += attackedSquaresByBlack[i][j];
		//
		// resultWhite += " | ";
		// resultBlack += " | ";
		// }
		// resultWhite += "\n";
		// resultWhite += "_".repeat(40);
		// resultWhite += "\n";
		//
		// resultBlack += "\n";
		// resultBlack += "_".repeat(40);
		// resultBlack += "\n";
		// }
		// return "WHITE: \n" + resultWhite + " \n" + "\n \n" + "BLACK: \n" +
		// resultBlack + " \n"
		// + toBoardString() + "#".repeat(50);
		return toBoardString();
	}

	public King getKing(ChessPieceColor color) {
		return color.isWhite() ? whiteKing : blackKing;
	}

}
