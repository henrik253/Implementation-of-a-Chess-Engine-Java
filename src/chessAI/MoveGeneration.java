package chessAI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.Check;
import main.model.gameLogic.MoveValidation;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class MoveGeneration {

	public static Map<Piece, Vector2D[]> getMoves(final BoardRepresentation boardR, final ChessPieceColor color) {

		Map<Piece, Vector2D[]> allMoves = new HashMap<>();
		Piece[][] board = boardR.getBoard().clone();
		boolean inCheck = Check.kingInCheck(boardR, color);

		for (Piece[] row : board) {
			for (Piece p : row) {

				if (Check.isPiecePinned(boardR, p)) { // Pinned piece can be ignored
					continue;
				}

				if (p != null && p.getColor() == color) {
					List<List<Vector2D>> movesInDirections = p.calculateMoveablePositions();
					List<Vector2D> tempResult = new ArrayList<>();

					for (List<Vector2D> moves : movesInDirections) {
						for (Vector2D move : moves) {

							if (inCheck) {
								if(Check.checkCanBeStopped(boardR, color, p)) {
									tempResult.add(move);
								}
							} else {
								tempResult.add(move);
							}
						}
					}

					allMoves.put(p, tempResult.toArray(new Vector2D[tempResult.size()]));
				}
			}
		}
		return allMoves;
	}
	


}
