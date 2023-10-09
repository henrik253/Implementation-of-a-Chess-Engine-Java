package chessAI.logic.boardEvaluation;

import main.model.chessPieces.concretePieces.Piece;
import main.model.gameLogic.BoardRepresentation;
import utils.ChessPieceColor;

public class Evaluate {

	public static int calc(BoardRepresentation boardR, ChessPieceColor color) {

		return calcMaterialDifference(boardR.getBoard(), color);
	}

	public static int calcMaterialDifference(Piece[][] board, ChessPieceColor color) {
		int sumBlack = 0;
		int sumWhite = 0;

		for (Piece[] row : board) {
			for (Piece p : row) {
				if (p != null && p.getColor().isWhite()) {
					sumWhite += p.getValue();
				} else if (p != null && p.getColor().isBlack()) {
					sumBlack += p.getValue();
				}
			}
		}

		if (color.isWhite())
			return sumWhite - sumBlack;

		return sumBlack - sumWhite;
	}

}
