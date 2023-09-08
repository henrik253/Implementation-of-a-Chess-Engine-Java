package main.model;

import main.model.chessPieces.concretePieces.Piece;

public interface ChessBot {

	Piece[][] makeMove(Piece[][] board);
}
