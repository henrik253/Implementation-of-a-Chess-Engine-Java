package main.model;

import main.model.chessPieces.concretePieces.Piece;

public interface ChessBot {

	Move makeMove(Piece[][] board);
}
