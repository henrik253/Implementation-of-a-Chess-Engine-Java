package ai2.boardEvaluation;

import ai2.boardEvaluation.squaretables.PieceSquareTable;
import main.model.gameLogic.BoardRepresentation;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.ChessPieceName;

public class Evaluate {

	private static final int MAX_PIECE_COUNT = 32;

	// Evaluating the board for colors perspective
	public static float evaluate(BoardRepresentation boardR, ChessPieceColor color) {
		// 0 for start, 1 for end
		float gameProgress = ((boardR.getWhitePieces().size() + boardR.getBlackPieces().size()) / MAX_PIECE_COUNT) - 1; 

		return evaluate(boardR.getBoard(), color, gameProgress);
	}

	// The iteration includes MaterialDifference, Position of the pieces with
	// linear interpolation
	public static float evaluate(Piece[][] board, ChessPieceColor color, float gameProgress) {
		float blackPos = 0;
		float whitePos = 0;
		
		int materialWhite = 0,materialBlack = 0;
		
		for (Piece[] row : board) {
			for (Piece p : row) {

				if (p == null)
					continue;

				if (p.getColor().isWhite()) {
					materialWhite += p.value;
					whitePos += p.value * getSquareTableValue(p, gameProgress); // weighting the position with piece val
				} else {
					materialBlack += p.value;
					blackPos += p.value * getSquareTableValue(p, gameProgress);
				}

			}
		}

		// looking from colors perspective
		float position = color.isWhite() ? whitePos - blackPos : blackPos - whitePos;
		float material = color.isWhite() ? materialWhite - materialBlack : materialBlack - materialWhite;
		
		
		return position + material;
	}
	
	// SquareTables store values ranging from 0.0f - 1.0f.
	public static float getSquareTableValue(Piece p, float gameProgress) {
		int x = p.getPosition().getX(), y = p.getPosition().getY();
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

}
