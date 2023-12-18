package main.model.gameLogic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ai2.MoveGeneration;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;

public class Remis {
	// Remis happens when the same board appears three times in board history or
	// when the Player cant move any piece
	private static final int maxCountOfBoard = 3;
	
	public static boolean isRemis(final BoardRepresentation board,final List<Move> moveHistory,
			final ChessPieceColor nextPlayerMove, final BoardRepresentation startingBoard) {
		
		return checkBoardHistoryForMaxCount(moveHistory,startingBoard) || !playerCanMove(board,nextPlayerMove) || onlyTwoKingsOnBoard(board);
	}
	
	private static boolean onlyTwoKingsOnBoard(BoardRepresentation board) {
		return board.getBlackPieces().size() == 1 && board.getWhitePieces().size() == 1;
	}

	// generating all boards from the moveHistory and comparing them 
	public static boolean checkBoardHistoryForMaxCount(final List<Move> moveHistory,final BoardRepresentation startingBoard) {
		List<BoardRepresentation> boards = new LinkedList<>();
		BoardRepresentation start = startingBoard.clone();
		boards.add(start);

		BoardRepresentation last = start.clone();
		for(Move move : moveHistory) {
			BoardRepresentation next = last;
			next.makeMove(move.from(), move.to());
			boards.add(next);
			last = next.clone();
		}

		if(boards.size() <= maxCountOfBoard) {
			return false;
		}
		
		for(int i=0; i < boards.size() - 1; i++) {
			int sameBoardCount = 1;
			for(int j=i+1; j < boards.size(); j++) {
				BoardRepresentation iBoard = boards.get(i);
				BoardRepresentation jBoard = boards.get(j);
				if(iBoard.equals(jBoard)) {
					sameBoardCount++;
				}
				if(sameBoardCount == maxCountOfBoard) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean playerCanMove(BoardRepresentation board,ChessPieceColor movingPlayer) {
		return Check.kingCanMove(board, movingPlayer) || MoveGeneration.getMoves(board, movingPlayer).size() > 0;
	}
}
