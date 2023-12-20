package ai2.boardEvaluation;

import ai2.boardEvaluation.squaretables.PieceSquareTable;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.Check;
import main.model.pieces.King;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.conversions.FENConverter;

public class Evaluate {

	private static final int MAX_PIECE_COUNT = 32;

	private static final float MATERIAL_WEIGHT = 3.0f;
	private static final float POSITION_WEIGHT = 2.0f;

	// Evaluating the board for colors perspective
	public static int evaluate(BoardRepresentation boardR) {
		// 0 for start, 1 for end

		if (boardR.getBlackPieces().size() + boardR.getWhitePieces().size() > MAX_PIECE_COUNT) {
			throw new IllegalArgumentException("too many pieces in boardR");
		}
		float gameProgress = Math.abs(
				1 - ((boardR.getWhitePieces().size() + boardR.getBlackPieces().size() - 2) / (float) MAX_PIECE_COUNT));

		int evaluation = evaluate(boardR.getBoard(), gameProgress);

		return evaluation;
	}

	// The iteration includes MaterialDifference, Position of the pieces with
	// linear interpolation
	public static int evaluate(Piece[][] board, float gameProgress) {
		float blackPos = 0;
		float whitePos = 0;

		int materialWhite = 0, materialBlack = 0;

		for (Piece[] row : board) {
			for (Piece p : row) {

				if (p == null)
					continue;

				int pieceValue = p instanceof King ? 10 : p.getValue();

				if (p.getColor().isWhite()) {

					materialWhite += pieceValue;
					whitePos += pieceValue * getSquareTableValue(p, gameProgress);

				} else {
					materialBlack += pieceValue;
					blackPos += pieceValue * getSquareTableValue(p, gameProgress);
				}

			}
		}

		int position = ((int) ((whitePos - blackPos) * POSITION_WEIGHT));
		int material = (int) ((materialWhite - materialBlack) * MATERIAL_WEIGHT);

		return (int) (position + material);
	}

	// SquareTables store values ranging from 0.0f - 1.0f.
	public static float getSquareTableValue(Piece p, float gameProgress) {

		int x = p.getPosition().getX(), y = p.getPosition().getY();
		if (p.getColor().isBlack()) { // Invert the position because the square tables are from whites perspective.
			x = Math.abs(x - 7);
			y = Math.abs(y - 7);
		}

		return switch (p.getName()) {
		case PAWN -> linearInterpolation(PieceSquareTable.PawnTable.opening[y][x],
				PieceSquareTable.PawnTable.endGame[y][x], gameProgress);
		case ROOK -> linearInterpolation(PieceSquareTable.RookTable.opening[y][x],
				PieceSquareTable.RookTable.endGame[y][x], gameProgress);
		case KNIGHT -> linearInterpolation(PieceSquareTable.KnightTable.opening[y][x],
				PieceSquareTable.KnightTable.endGame[y][x], gameProgress);
		case BISHOP -> linearInterpolation(PieceSquareTable.BishopTable.opening[y][x],
				PieceSquareTable.BishopTable.endGame[y][x], gameProgress);
		case QUEEN -> linearInterpolation(PieceSquareTable.QueenTable.opening[y][x],
				PieceSquareTable.QueenTable.endGame[y][x], gameProgress);
		case KING -> linearInterpolation(PieceSquareTable.KingTable.opening[y][x],
				PieceSquareTable.KingTable.endGame[y][x], gameProgress);
		default -> {
			System.out.println("Unknown membership status. No discount applicable.");
			yield 0.0f;
		}
		};
	}

	// Interpolation between Start & End SquareTable
	private static float linearInterpolation(float openingVal, float endingVal, float gameProgress) {
		return openingVal + (endingVal - openingVal) * gameProgress;
	}

//	public static void main(String[] args) {
//		String fen1 = "rnbqkbnr/pppppppp/8/8/8/8/8/4K3";
//		BoardRepresentation boardR = new BoardRepresentation(
//				FENConverter.convertToPieceBoard("4nk2/8/8/3p4/3P4/2N5/8/7K"));
//		
//		System.out.println(evaluate(boardR));
//		
//		String fen2 = "1k6/rB6/8/8/8/2R5/RR6/K7";
//		BoardRepresentation boardR2 = new BoardRepresentation(
//				FENConverter.convertToPieceBoard(fen2));
//		System.out.println(evaluate(boardR2));
//	}

}
