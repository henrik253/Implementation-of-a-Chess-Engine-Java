package main.model.chessbots;

import main.model.pieces.Piece;
import utils.ChessPieceColor;
import utils.Move;

public interface ChessBot {

	ChessPieceColor getColor();

	void setColor(ChessPieceColor color);

	Move makeMove(Piece[][] board);

	Move getLastMove();
}
