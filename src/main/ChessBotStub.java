package main;

import java.util.ArrayList;
import java.util.List;

import main.model.ChessBot;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;

public class ChessBotStub implements ChessBot {

	private Piece[][] board;
	private ChessPieceColor color = ChessPieceColor.WHITE;

	@Override
	public Piece[][] makeMove(Piece[][] board) {
		this.board = board;

		List<Piece> currentPieces = new ArrayList<>();
		// filter black pieces
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null && piece.getColor().isBlack())
					currentPieces.add(piece);
			}
		}

		while(true) {
			Piece randPiece = currentPieces.get((int) (currentPieces.size() * Math.random()));
			System.out.println(randPiece);
			List<Vector2D> dir = randPiece.getAttackableSquares()
					.get((int)((randPiece.getAttackableSquares().size() - 1)* Math.random()));
			
			if(dir.isEmpty())
				continue;
			
			int rand = (int) ((dir.size() - 1) *  Math.random());

			Vector2D move = dir.get(rand);
			
			if(board[move.getY()][move.getX()] != null) {
				continue;
			}
			board[move.getY()][move.getX()] = board[randPiece.getPosition().getY()][randPiece.getPosition().getX()];
			board[randPiece.getPosition().getY()][randPiece.getPosition().getX()] = null;
			break;
		}
	
		
		return this.board;
	}

}
