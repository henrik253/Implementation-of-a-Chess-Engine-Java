package ai2.boardEvaluation;

import ai2.boardEvaluation.squaretables.PieceSquareTable;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.Check;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.ChessPieceName;
import utils.conversions.FENConverter;

public class Evaluate {

	private static final int MAX_PIECE_COUNT = 32;

	// Evaluating the board for colors perspective
	public static float evaluate(BoardRepresentation boardR, ChessPieceColor color, boolean maximazing,int depth) {
		// 0 for start, 1 for end
		float gameProgress = ((boardR.getWhitePieces().size() + boardR.getBlackPieces().size()) / MAX_PIECE_COUNT) - 1;
		float evaluation = evaluate(boardR.getBoard(), color, maximazing, gameProgress);

		return maximazing ? evaluation : -evaluation;
	}

	// The iteration includes MaterialDifference, Position of the pieces with
	// linear interpolation
	public static float evaluate(Piece[][] board, ChessPieceColor color, boolean maximazing, float gameProgress) {
		float blackPos = 0;
		float whitePos = 0;

		int materialWhite = 0, materialBlack = 0;

		for (Piece[] row : board) {
			for (Piece p : row) {

				if (p == null)
					continue;

				if (p.getColor().isWhite()) {
					materialWhite += p.getValue();
					whitePos += p.getValue() * getSquareTableValue(p, gameProgress); // weighting the position with
					// piece val
				} else {
					materialBlack += p.getValue();
					blackPos += p.getValue() * getSquareTableValue(p, gameProgress);
				}

			}
		}

		float position = whitePos - blackPos;
		float material = materialWhite - materialBlack;

		return position + material;
	}

	// SquareTables store values ranging from 0.0f - 1.0f.
	public static float getSquareTableValue(Piece p, float gameProgress) {

		int x = p.getPosition().getX(), y = p.getPosition().getY();
		if (p.getColor().isBlack()) {
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
//		String fen1 = "7K/k7/4q2N/8/8/8/8/6qr";
//		BoardRepresentation boardR = new BoardRepresentation(
//				FENConverter.convertToPieceBoard("7K/k7/4q2N/8/8/8/8/6qr"));
//
//		System.out.println(evaluate(boardR, ChessPieceColor.BLACK));
//	}

}
