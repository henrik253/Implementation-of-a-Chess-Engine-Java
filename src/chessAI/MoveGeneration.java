package chessAI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.model.chessPieces.concretePieces.Piece;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class MoveGeneration {

	private static MoveValidation validation = new MoveValidation();

	public static Map<Piece, Vector2D[]> getMoves(final BoardRepresentation boardR, final ChessPieceColor color) {
		validation.setOnMove(color);
		validation.setBoard(boardR);

		Map<Piece, Vector2D[]> allMoves = new HashMap<>();
		Piece[][] board = boardR.getBoard();

		for (Piece[] row : board) {
			for (Piece p : row) {

				if (p != null && p.getColor() == color) {
					List<List<Vector2D>> movesInDirections = p.calculateMoveablePositions();
					List<Vector2D> tempResult = new ArrayList<>(); 

					for (List<Vector2D> moves : movesInDirections) {
						for (Vector2D move : moves) {
							if (validation.kingInCheckIfPieceMoves(p.getPosition(), move)) {
								continue;
							}
							tempResult.add(move); 
						}
					}
					// TODO CHECK IF KING WOULD BE IN CHECK IF PIECE MOVES ! NO PSEUDO LEGAL MOVES ?
					allMoves.put(p, tempResult.toArray(new Vector2D[tempResult.size()]));
				}
			}
		}
		return allMoves;
	}

}
