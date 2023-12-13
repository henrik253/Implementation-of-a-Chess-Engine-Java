package main.model.gameLogic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;

public class Remis {
	// Remis happens when the same board appears three times in board history or
	// when the Player cant move any piece
	private static final int maxCountOfBoard = 3;
	
	public static boolean isRemis(final BoardRepresentation boardRepresentation,final List<Move> moveHistory,
			final ChessPieceColor onMove, final BoardRepresentation startingBoard) {
		
		return checkBoardHistoryForMaxCount(moveHistory,startingBoard);
	}

	public static boolean checkBoardHistoryForMaxCount(final List<Move> moveHistory,final BoardRepresentation startingBoard) {
		Map<BoardRepresentation,Integer> boardCount = new HashMap<>();
		boardCount.put(startingBoard, 1);
		BoardRepresentation last = startingBoard;
		for(Move move : moveHistory) {
			System.out.println("gt");
			System.out.println(move);
			BoardRepresentation nextBoard = last; 
			System.out.println(last);
			nextBoard.makeMove(move.from(), move.to());
			boardCount.put(nextBoard, boardCount.getOrDefault(last, 1) + 1);		
		}
		
		for(int counts : boardCount.values()) {
			if(counts == maxCountOfBoard) {
				return true;
			}
		}
		
		return false;
	}
}
