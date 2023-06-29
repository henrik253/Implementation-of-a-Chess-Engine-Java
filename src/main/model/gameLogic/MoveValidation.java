package main.model.gameLogic;

import main.model.Model;
import main.model.Move;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.*;

import java.util.List;

public class MoveValidation {
	private Model model;

	private ChessPieceColor onMove;
	private BoardRepresentation board;

	private King whiteKing;
	private King blackKing;

	private int moveCounter = 0;

	private Move lastMove;

	public MoveValidation(ChessPieceColor startingColor) {
		this.onMove = startingColor;
	}

	public MoveValidation() {
		this(ChessPieceColor.WHITE);
	}

	public void initKingReferences() {
		whiteKing = (King) board.getKing(ChessPieceColor.WHITE);
		blackKing = (King) board.getKing(ChessPieceColor.BLACK);

	}

	public boolean makeMove(Vector2D oldPos, Vector2D newPos) {
		Piece piece = board.getPiece(oldPos);
		if(piece == null){
			return false;
		}
		boolean moveSucceed = false;

		if (!isOnMove(piece))
			return false;

		if (!piece.isValidMove(newPos) && !isSpecialMove(piece, newPos))
			return false;

		if (!noPiecesBetween(piece, newPos))
			return false;

		if (noPiece(newPos)) {
			moveSucceed = tryMove(oldPos, newPos);
		} else if (isEnemyPiece(piece, newPos)) {
			if (tryMove(oldPos, newPos)) {
				moveSucceed = true;
			}

		} else if (isAllyPiece(piece, newPos)) {
			moveSucceed = false;
		}

		if (moveSucceed) {
			if (enemyInCheck()) {
				System.out.println("IN CHECK!!!!");
			}
			setNextPlayerOnMove();
			moveCounter++;
			lastMove = new Move(oldPos, newPos);
		}
		return moveSucceed;
	}

	private boolean tryMove(Vector2D oldPos, Vector2D newPos) {
		BoardRepresentation boardClone = this.board.clone();

		if (isSpecialMove(boardClone.getPiece(oldPos), newPos)) {
			boardClone.makeSpecialMove(oldPos, newPos);
		} else
			boardClone.makeMove(oldPos, newPos);

		boardClone.calcAttackedSquaresBy(onMove.getOpponentColor());

		int[][] attackedSquares = onMove.isWhite() ? boardClone.getAttackedSquaresByBlack()
				: boardClone.getAttackedSquaresByWhite();

		King king = onMove.isWhite() ? (King) boardClone.getKing(ChessPieceColor.WHITE)
				: (King) boardClone.getKing(ChessPieceColor.BLACK);

		if (inCheck(attackedSquares, king)) {
			return false;
		}

		this.board.setBoard(boardClone.getBoard());
		this.board.calcAttackedSquaresBy(onMove);

		return true;
	}

	public boolean isSpecialMove(Piece piece, Vector2D newPos) {

		if (piece instanceof Pawn) {
			Pawn pawn = (Pawn) piece;

			if (isPawnAttack(pawn, newPos))
				return true;
			
			if(isEnPassantMove(pawn,newPos))
				return true;

		}

		if (piece instanceof King) {
			King king = (King) piece;
			if (isCastleMove(king, newPos)) {
				return true;
			}
		}

		return false;
	}

	public boolean isPawnAttack(Pawn pawn, Vector2D newPos) {
		Piece p = this.board.getPiece(newPos);
		return pawn.isValidAttack(newPos) && p != null && p.getColor() != onMove;

	}

	public boolean isEnPassantMove(Pawn pawn, Vector2D newPos) {
		Piece p = this.board.getPiece(newPos);
		Vector2D direction = new Vector2D(0, onMove.isWhite() ? 1 : -1);
		Vector2D attackedPieceCoords = Vector2D.add(newPos, direction);
		if(attackedPieceCoords.getX() >= 0 && attackedPieceCoords.getY() >= 0){
			Piece attackedPiece = board.getPiece(attackedPieceCoords);
			return pawn.isValidAttack(newPos) && p == null && attackedPiece instanceof Pawn
					&& ((Pawn) attackedPiece).getColor() != onMove && lastMoveIsDoublePawnMove();
		}
		return false;
	}

	private boolean lastMoveIsDoublePawnMove() {
		Piece piece = board.getPiece(lastMove.getNewPos());
		int yLength = (int) Math.abs(lastMove.getOldPos().getY() - lastMove.getNewPos().getY());
		return piece instanceof Pawn && !(((Pawn) piece).isFirstMove()) && yLength == 2;
	}

	public boolean isCastleMove(King king, Vector2D newPos) {
		if (!king.isFirstMove())
			return false;

		if (!king.isValidCastle(newPos))
			return false;

		// left or right side castle
		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;

		int xPosRook = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
		Vector2D rookPos = new Vector2D(xPosRook, king.getPosition().getY());

		if (board.countFiguresBetween(king.getPosition(), rookPos) > 0) {
			return false;
		}

		if (board.isCheckBetween(king.getPosition(), rookPos)) {
			return false;
		}

		Piece rook = board.getPiece(rookPos);

		if (!(rook instanceof Rook && ((Rook) rook).isFirstMove())) {
			return false;
		}

		return true;
	}

	public boolean tryCastling(Vector2D oldPos, Vector2D newPos) {
		Piece piece = board.getPiece(oldPos);
		if (!(piece instanceof King))
			return false;

		King king = (King) piece;

		if (!king.isFirstMove())
			return false;

		if (!king.isValidCastle(newPos))
			return false;

		// left or right side castle
		boolean isRightSideCastle = newPos.getX() - king.getPosition().getX() > 0;

		int xPos = isRightSideCastle ? this.board.getBoard().length - 1 : 0;
		Vector2D rookPos = new Vector2D(xPos, king.getPosition().getY());

		if (board.countFiguresBetween(king.getPosition(), rookPos) > 0) {
			return false;
		}

		if (board.isCheckBetween(king.getPosition(), rookPos)) {
			return false;
		}

		Piece rook = board.getPiece(rookPos);

		if (rook instanceof Rook && ((Rook) rook).isFirstMove()) {
			Vector2D direction = new Vector2D(isRightSideCastle ? -2 : 3, 0);
			makeMove(rookPos, Vector2D.add(rookPos, direction));
			return true;
		}

		return false;
	}

	private boolean inCheck(int[][] attackedSquares, King king) {
		return attackedSquares[king.getPosition().getY()][king.getPosition().getX()] > 0;
	}

	private boolean isCheckMate() {
		King k = onMove.isWhite() ? blackKing : whiteKing;
		int[][] attackableSqaures = onMove.isWhite() ? this.board.getAttackedSquaresByWhite()
				: this.board.getAttackedSquaresByBlack();
		List<Vector2D> moveablePositions = k.calculateMoveablePositions();

		for (Vector2D pos : moveablePositions) {

		}
		return false;
	}

	private boolean noPiecesBetween(Piece piece, Vector2D newPos) {
		return this.board.countFiguresBetween(piece.getPosition(), newPos) == 0 || piece instanceof Knight;
	}

	private boolean isOnMove(Piece piece) {
		return onMove == piece.getColor();
	}

	private void setNextPlayerOnMove() {
		onMove = onMove.isWhite() ? ChessPieceColor.BLACK : ChessPieceColor.WHITE;
	}

	private boolean noPiece(Vector2D pos) {
		return board.getPiece(pos) == null;
	}

	private boolean isPiece(Vector2D pos) {
		return !noPiece(pos);
	}

	private boolean isEnemyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() != piece.getColor();
	}

	private boolean isAllyPiece(Piece piece, Vector2D pos) {
		return board.getPiece(pos).getColor() == piece.getColor();
	}

	public boolean enemyInCheck() {
		int[][] attackedSquares = onMove.isWhite() ? board.getAttackedSquaresByWhite()
				: board.getAttackedSquaresByBlack();
		Vector2D pos = onMove.isWhite() ? blackKing.getPosition() : whiteKing.getPosition();
		return attackedSquares[pos.getY()][pos.getX()] > 0;
	}

	public ChessPieceColor getOnMove() {
		return onMove;
	}

	public void setOnMove(ChessPieceColor onMove) {
		this.onMove = onMove;
	}

	public BoardRepresentation getBoard() {
		return board;
	}

	public void setBoard(BoardRepresentation board) {
		this.board = board;
		initKingReferences();
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
