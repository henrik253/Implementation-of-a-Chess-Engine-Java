package main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.model.ChessBot;
import main.model.Move;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;

public class ChessBotStub implements ChessBot {

	private Piece[][] board;
	private ChessPieceColor color = ChessPieceColor.WHITE;
	private List<Piece> currentPieces = new ArrayList<>();

	private void init() {
		currentPieces.clear();
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null && piece.getColor() == color.getOpponentColor()) {
					currentPieces.add(piece);
				}
			}
		}
	}

	@Override
	public Move makeMove(Piece[][] board) {
		this.board = board;
		init();
		int counter = 0; 
		while (true) {
			int pieceNr = (int) (Math.random() * (currentPieces.size() - 1));
			Piece piece = currentPieces.get(pieceNr);
			Vector2D piecePos = piece.getPosition();

			List<Vector2D> moves = piece.getAttackableSquares().stream().flatMap(movesInDir -> movesInDir.stream())
					.collect(Collectors.toList());

			if (moves.size() == 0 && counter++ < 10000)
				continue;

			int moveNr = (int) (Math.random() * (moves.size() - 1));
			return new Move(piecePos, moves.get(moveNr));
		}
	}

}
