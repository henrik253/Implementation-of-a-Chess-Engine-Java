package utils.conversions.pgn;

import main.model.pieces.Piece;

import java.util.List;

import main.model.gameLogic.*;
import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.Move;
import utils.SimplePiece;
import utils.Vector2D;
import utils.conversions.FENConverter;

public class AlgebraicNotationConverter {

	private static final char CASTLING_SIGN = 'O';
	private static final String QUEEN_SIDE_CASTLE = "O-O-O";
	private static final String KING_SIDE_CASTLE = "O-O";

	/*
	 * Notation for moves for PGN source:
	 * https://en.wikipedia.org/wiki/Algebraic_notation_(chess) 1.Piece type is
	 * identified by an uppercase letter 1.1 K (King) ,Q (Queen) ,R (Rook) ,B
	 * (Bishop) ,N (Knight) 2.Notation for move 2.2 Piece Type followed with the
	 * target square. 2.3 When using a pawn only the target square is used
	 * 
	 * 3. Captures When a piece is captured an "x" is used
	 * 
	 * 4. Disambiguating moves when two or more pieces can move to the same square
	 * the piecePos is added 4.1 for example Qh4e1 means Queen from h4 to e1 4.2 if
	 * rooks are on the same row/column only the letter or the numb can be written
	 * down for instance: Rdf8
	 * 
	 * These rule can be combined with capturing.
	 * 
	 * 5. Special Rules Pawn promotion: example: e8=Q Draw offer: (=) Castling: O-O
	 * (kingside castling) O-O-O (queenside castling) Check: if a moves checks an +
	 * is appended Checkmate: # represents checkmate
	 */

	// differences at the beginning of a single move exd6 -> En Passant
	//
	public static Move getMove(String input, Piece[][] board, ChessPieceColor color) {
		Move move = null;

		if (input.isEmpty()) {
			throw new IllegalArgumentException("empty algebraicMove");
		}

		char starting = input.charAt(0);
		char column; // from a -> h
		char row; // from 1 - 8
		// if starting is a upperCaseLetter we know thats a moving piece or Castling, if
		// not its a pawn move
		if (Character.isUpperCase(starting)) {
			if (starting == CASTLING_SIGN) {
				return getCastlingMove(input, board, color);
			} else if (input.charAt(1) == 'x' && Character.isAlphabetic(input.charAt(2))
					&& Character.isDigit(input.charAt(3))) {
				// capturing a piece with the followed position, for example: Rxd5
				return getMove(starting, null, input.charAt(2) + "" + input.charAt(3), board, color);

			} else if (Character.isAlphabetic(input.charAt(1))) {
				// this case checks a algebraicMove if its Disambiguating move, with checking
				// for the columns, for example: Rdd5
				if (Character.isDigit(input.charAt(2))) {
					// normal move in this case like Rd5
					return getMove(starting, null, input.charAt(1) + "" + input.charAt(2), board, color);
				}  else if (input.charAt(2) == 'x') {
					// capturing move like Rdxd5
					return getMove(starting, input.charAt(1), input.charAt(3) + "" + input.charAt(4), board, color);
				}
				else if (Character.isAlphabetic(input.charAt(2))) {
					// move like Rdd5
					return getMove(starting, input.charAt(1), input.charAt(2) + "" + input.charAt(3), board, color);
				}

			} else if (Character.isDigit(input.charAt(1))) {
				// this case checks a algebraicMove if its Disambiguating (distinct) move, with
				// checking
				// for the rows
				// examples: R3d5 R2xd5
				if (input.charAt(2) == 'x') {
					return getMove(starting, input.charAt(1), input.charAt(3) + "" + input.charAt(4), board, color);
				} else if (Character.isAlphabetic(input.charAt(2))) {
					// move like R3d5
					return getMove(starting, input.charAt(1), input.charAt(2) + "" + input.charAt(3), board, color);
				} 
			}

		} else {
			// in this Case Pawn is moving, capturing (capturing or en passant) or promoting
			if (Character.isAlphabetic(starting) && input.charAt(1) == 'x' && input.charAt(2) >= 'a'
					&& input.charAt(2) <= 'h' && Character.isDigit(input.charAt(3))) {
				// in this case pawn is capturing for example: exd5 so the last two characters
				// are the position where he is capturing
				// starting as indistinct because we say Pawn on e captures d5
				return getMove('P', starting, input.charAt(2) + "" + input.charAt(3), board, color);

			} else if ((starting >= 'a' && starting <= 'h') && Character.isDigit(input.charAt(1))
					&& (input.length() == 2 || input.charAt(2) != '=')) { //  input.charAt(2) can be sth like # , + ... 
				// in this case pawn on starting line is moving to
				// Character.isDigit(algebraicMove.charAt(1)
				return getMove('P', null, input.charAt(0) + "" + input.charAt(1), board, color);
			} else if ((starting >= 'a' && starting <= 'h') && Character.isDigit(input.charAt(1))
					&& input.charAt(2) == '=') {
				// in this case its a promoting pawn
				char promoting = input.charAt(3);
				Move promotingPawnMove  = getMove('P', null, input.charAt(0) + "" + input.charAt(1), board, color);
				Piece promotingPawn = board[promotingPawnMove.from().getY()][promotingPawnMove.from().getX()];
				promotingPawnMove.setPromotingPiece(promotingPawn, getPieceName(promoting));
				return promotingPawnMove;
			}

		}
		return move;
	}

	// expression like Rd, R, d5, ...
	private static Move getMove(Character pieceSign, Character indistinct, String movingPos, Piece[][] board,
			ChessPieceColor color) {
		// a = 0 , ... , h = 7
		int newColumn = movingPos.charAt(0) - 'a';
		int newRow = board.length - Character.getNumericValue(movingPos.charAt(1));
		Vector2D to = new Vector2D(newColumn, newRow);
		ChessPieceName pieceName = getPieceName(pieceSign);

		Vector2D from = findPiecePosition(pieceName, color, to, board, indistinct);

		return new Move(from, to);
	}

	private static Move getCastlingMove(String move, Piece[][] board, ChessPieceColor color) {
		// king pos can be hard coded
		Vector2D from = color.isWhite() ? new Vector2D(4, 7) : new Vector2D(4, 0);
		// checkining Queen_Side_Castle for the case O-O+ and KingSideCastle contains O-O-O 
		Vector2D to = move.contains(QUEEN_SIDE_CASTLE) ?Vector2D.plus(from, new Vector2D(-2,0)):Vector2D.plus(from, new Vector2D(2 , 0)); // contains becuase sth. like this O-O+
		return new Move(from, to);
	}


	private static Vector2D findPiecePosition(ChessPieceName name, ChessPieceColor color, Vector2D to, Piece[][] board,
			Character indistinct) {
		// check if matching piece could move to position "to"
		
		if (indistinct == null) {
			// iterating though the whole board
			for (int row = 0; row < board.length; row++) {
				for (int column = 0; column < board[row].length; column++) {
					Piece p = board[row][column];
				
					if (p != null && p.getName().equals(name) && p.getColor().equals(color)) {
						Vector2D matchingMove = getMatchingMoveablePosition(p, to);
						if (matchingMove != null) {
							return matchingMove;
						}
					}
				}
			}
		} else {
			// iterating trough the desired column
			if (Character.isAlphabetic(indistinct)) {
				// looking the column
				int column = indistinct - 'a';
				for (int index = 0; index < board.length; index++) {
					Piece p = board[index][column];
					if (p != null && p.getName().equals(name) && p.getColor().equals(color)) {
						Vector2D matchingMove = getMatchingMoveablePosition(p, to);
						if (matchingMove != null) {
							return matchingMove;
						}
					}
				}
			} else 
				if (Character.isDigit(indistinct)) {
				// iterating trough the desired row
				int row = board.length - Character.getNumericValue(indistinct);
				for (int index = 0; index < board.length; index++) {
					Piece p = board[row][index];
					if (p != null && p.getName().equals(name) && p.getColor().equals(color)) {
						Vector2D matchingMove = getMatchingMoveablePosition(p, to);
						if (matchingMove != null) {
							return matchingMove;
						}
					}
				}
			}

		}
		return null;
	}

	private static Vector2D getMatchingMoveablePosition(Piece p, Vector2D to) {
		for (List<Vector2D> direction : p.calculateMoveablePositions()) {
			for (Vector2D pos : direction) {
				if (pos.equals(to)) {
					return p.getPosition().clone();
				}
			}
		}
		return null;
	}

	private static ChessPieceName getPieceName(Character c) {
		return switch (c) {
		case 'P' -> ChessPieceName.PAWN;
		case 'N' -> ChessPieceName.KNIGHT;
		case 'R' -> ChessPieceName.ROOK;
		case 'B' -> ChessPieceName.BISHOP;
		case 'Q' -> ChessPieceName.QUEEN;
		case 'K' -> ChessPieceName.KING;
		default -> {
			throw new IllegalArgumentException("Char c: " + c + " couldnt be assigned to a a piece");
		}
		};
	}

}
