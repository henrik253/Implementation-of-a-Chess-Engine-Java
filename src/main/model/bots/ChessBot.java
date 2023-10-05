package main.model.bots;

import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.Piece;
import utils.Move;

public interface ChessBot {

	ChessPieceColor getColor();

	void setColor(ChessPieceColor color);

	Move makeMove(Piece[][] board);

	Move getLastMove();
}
