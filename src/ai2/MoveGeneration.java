package ai2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Comparator;

import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.Check;
import main.model.gameLogic.MoveValidation;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class MoveGeneration {

	public static Map<Piece, Vector2D[]> getMoves(final BoardRepresentation boardR, final ChessPieceColor color) {

		Map<Piece, Vector2D[]> allMoves = new LinkedHashMap<>();
		Piece[][] board = boardR.getBoard().clone();
		boolean inCheck = Check.kingInCheck(boardR, color);
		
		
		
		for (Piece[] row : board) {
			for (Piece p : row) {

				if (p == null)
					continue;

				if (Check.isPiecePinned(boardR, p)) { // Pinned piece can be ignored
					continue;
				}

				if (p != null && p.getColor() == color) {
					List<List<Vector2D>> movesInDirections = p.calculateMoveablePositions();
					List<Vector2D> movesOfPiece = new ArrayList<>();

					for (List<Vector2D> moves : movesInDirections) {
						for (Vector2D move : moves) {

							if (inCheck) {
								if (Check.checkCanBeStoppedWithPieceMove(boardR, color, p.getPosition(), move)) {
									movesOfPiece.add(move);
								}
							} else {
								movesOfPiece.add(move);
							}
						}
					}
					if (movesOfPiece.size() > 0) {
						allMoves.put(p, movesOfPiece.toArray(new Vector2D[movesOfPiece.size()]));
					}
				}
			}
		}

		allMoves = sortPieceOrder(allMoves);
		System.out.println(allMoves);
		return allMoves;
	}

	private static Map<Piece, Vector2D[]> sortPieceOrder(Map<Piece, Vector2D[]> allMoves) {
		Comparator<Map.Entry<Piece, Vector2D[]>> comparator = (entry1, entry2) -> entry2.getKey().value
				- entry1.getKey().value;
		List<Map.Entry<Piece, Vector2D[]>> entries = new ArrayList<>(allMoves.entrySet());
		Collections.sort(entries, comparator);
		Map<Piece, Vector2D[]> sortedMap = new LinkedHashMap<>();
		entries.forEach(entrie -> sortedMap.put(entrie.getKey(), entrie.getValue()));
		return sortedMap;
	}

}
