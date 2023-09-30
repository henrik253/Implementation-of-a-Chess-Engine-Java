package main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.model.ChessBot;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.King;
import main.model.chessPieces.concretePieces.Piece;
import utils.Move;
import utils.Vector2D;

public class ChessBotStub implements ChessBot {

	private Piece[][] board;
	private ChessPieceColor color = ChessPieceColor.BLACK;
	private List<Piece> currentPieces = new ArrayList<>();

	private Move move;

	private void init() {
		currentPieces.clear();
		for (Piece[] row : board) {
			for (Piece piece : row) {
				if (piece != null && piece.getColor() == color) {
					currentPieces.add(piece);
				}
			}
		}
	}

	@Override
	public Move makeMove(Piece[][] board) {
		this.board = board;
		init();

		while (true) {
			int pieceNr = (int) ((Math.random()) * (currentPieces.size()));
			Piece piece = currentPieces.get(pieceNr);
			Vector2D piecePos = piece.getPosition();

			List<Vector2D> moves = piece.calculateMoveablePositions().stream()
					.flatMap(movesInDir -> movesInDir.stream()).collect(Collectors.toList());

			if (piece instanceof King)
				System.out.println("ChessBotStub: " + piece + " " + moves);

			if (moves.size() == 0)
				continue;

			int moveNr = (int) (Math.random() * (moves.size()));
			move = new Move(piecePos, moves.get(moveNr));

			return move;
		}
	}

	@Override
	public ChessPieceColor getColor() {

		return color;
	}

	@Override
	public void setColor(ChessPieceColor color) {
		this.color = color;
	}

	@Override
	public Move getLastMove() {
		return move;
	}

}
