package main.model;

import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;

public interface ChessBot {

	ChessPieceColor getColor();

	void setColor(ChessPieceColor color);

	Move makeMove(Piece[][] board);
}
