package main.model.chessbots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import main.model.pieces.King;
import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;
import utils.Vector2D;

public class RandomChessBot implements ChessBot {

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

		int pieceNr = (int) ((Math.random()) * (currentPieces.size()));
		Piece piece = currentPieces.get(pieceNr);
		Vector2D piecePos = piece.getPosition();

		List<Vector2D> moves = piece.calculateMoveablePositions().stream().flatMap(movesInDir -> movesInDir.stream())
				.collect(Collectors.toList());

		List<List<Vector2D>> allMoves1 = new LinkedList<>();
		for (Piece piece1 : currentPieces) {
			List<Vector2D> moves1 = piece1.calculateMoveablePositions().stream()
					.flatMap(movesInDir -> movesInDir.stream()).collect(Collectors.toList());

			if (moves1.size() <= 1)
				continue;

			List<Vector2D> movesFromPiece = new LinkedList<>();
			movesFromPiece.add(piece1.getPosition());
			movesFromPiece.addAll(moves1);

			allMoves1.add(movesFromPiece);
		}

		int movesPiece = (int) (Math.random() * (allMoves1.size()) - 1) + 1;
		
		List<Vector2D> movesFromPiece = allMoves1.get(movesPiece);
		int moveIndex = (int) ((Math.random() * (movesFromPiece.size()) - 1) + 1);
		move = new Move(movesFromPiece.get(0),movesFromPiece.get(moveIndex));

		return move;

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
