package main.gui.game.board;

import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.Settings;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.SimplePiece;

public class Board extends GridPane {

	private Settings settings;
	private GameView gameView;

	private static final String IMAGE_PATH = "file:resources/";
	private static final String FILE_FORMAT = ".png";

	private List<Piece> piecesOnBoard = new LinkedList<>();
	private boolean inverted = false;

	private ChessPieceColor disabledSide = ChessPieceColor.BLACK;

	public Board() {
		this.setTranslateX(0);
		this.setTranslateY(0);
		this.setOnDragOver(this::onDragOver);
		this.setOnDragDropped(this::onDragDropped);

	}

	public void init() {

	}

	private void onDragOver(DragEvent event) {
		event.acceptTransferModes(TransferMode.MOVE);
	}

	private void onDragDropped(DragEvent event) {
		Piece draggedPiece = Move.getPiece();

		int oldX = draggedPiece.getRow();
		int oldY = draggedPiece.getColumn();

		int newX = (int) (event.getSceneX() / settings.squareWidth.get());
		int newY = (int) (event.getSceneY() / settings.squareHeight.get());

		boolean validMove = false;

		if (oldX != newX || oldY != newY) {
			Vector2D oldPos = new Vector2D(oldX, oldY);
			Vector2D newPos = new Vector2D(newX, newY);

			if (inverted) {
				oldPos.invertY(settings.rows - 1);
				oldPos.invertX(settings.rows - 1);
				newPos.invertX(settings.rows - 1);
				newPos.invertY(settings.rows - 1);
			}

			validMove = gameView.moveRequest(oldPos, newPos);
		}

		if (validMove) {
			gameView.userMoveSucceeded();
		}
	}

	public void drawBoard() {
		this.getChildren().clear();
		Color color1 = settings.brightColor.get();
		Color color2 = settings.darkColor.get();
		if (inverted) {
			color1 = settings.darkColor.get();
			color2 = settings.brightColor.get();
		}

		for (int row = 0; row < settings.rows; row++) {
			for (int column = 0; column < settings.columns; column++) {
				Color color = (row + column) % 2 == 0 ? color1 : color2;
				add(new Square(settings.squareWidth.get(), settings.squareHeight.get(), color), row, column);
			}

		}

	}

	public void drawPiecesOnBoard(SimplePiece[][] board) {
		clearPiecesOnBoard();

		if (inverted) {
			board = invertBoard(board);
		}

		for (int row = 0; row < settings.rows; row++) {
			for (int column = 0; column < settings.columns; column++) {
				SimplePiece simplePiece = board[row][column];
				if (simplePiece == null)
					continue;

				String imageName = simplePiece.toString();
				Piece piece = new Piece(row, column);
				piece.setImage(new Image(IMAGE_PATH + imageName + FILE_FORMAT, 150, 150, false, false));
				piece.setColor(simplePiece.getColor());

				piecesOnBoard.add(piece);
				this.add(piece, column, row);
			}
		}
		// TODO TEMP
		// disablePieceListener(disabledSide);
	}

	private SimplePiece[][] invertBoard(SimplePiece[][] board) {
		SimplePiece[][] invertedBoard = new SimplePiece[board.length][board[0].length];
		for (int i = 0; i < invertedBoard.length; i++) {
			SimplePiece[] invertedRow = new SimplePiece[invertedBoard[i].length];
			for (int j = 0; j < invertedRow.length; j++) {
				invertedRow[j] = board[board.length - i - 1][board[i].length - j - 1];
			}
			invertedBoard[i] = invertedRow;
		}
		return invertedBoard;
	}

	public void clearPiecesOnBoard() {
		if (piecesOnBoard == null)
			return;
		piecesOnBoard.forEach(piece -> this.getChildren().remove(piece));
		piecesOnBoard.clear();
	}

	public void disablePieceListener(ChessPieceColor color) {
		// TODO TEMP DIS- ENABLING BECAUSE PIECES ARE NEW GENERATED AFTER A MOVE
		if (disabledSide != color)
			this.disabledSide = color;

		piecesOnBoard.forEach(piece -> {
			if (piece.getColor() == color) {
				piece.setOnDragDetected(piece::onDisabled);
			}
		});
	}

	public void enablePieceListener(ChessPieceColor color) {
		piecesOnBoard.forEach(piece -> {
			if (piece.getColor() == color) {
				piece.setOnDragDetected(piece::onDragDetected);
			}
		});
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public GameView getGameView() {
		return gameView;
	}

	public void setGameView(GameView gameView) {
		this.gameView = gameView;
	}

	private class Piece extends Pane {

		private ImageView imageView;
		private IntegerProperty column;
		private IntegerProperty row;
		private ChessPieceColor color;

		public Piece(int column, int row) {
			this.setPrefWidth(settings.squareWidth.get());
			this.setPrefHeight(settings.squareHeight.get());

			this.column = new SimpleIntegerProperty(column);
			this.row = new SimpleIntegerProperty(row);
			this.setOnDragDetected(this::onDragDetected);
			this.setOnDragDone(this::onDragDone);
		}

		private void onDragDetected(MouseEvent event) {
			Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();
			Move.getMove(this);
			Image image = imageView.getImage();
			content.putImage(image);
			dragboard.setContent(content);
			this.setVisible(false);
		}

		private void onDisabled(MouseEvent event) {
			System.out.println("DISABLED!");
		}

		private void onDragDone(DragEvent event) {
			this.setVisible(true);
		}

		public int getColumn() {
			return column.get();
		}

		public int getRow() {
			return row.get();
		}

		public void setImage(Image image) {
			this.imageView = new ImageView(image);
			getChildren().add(imageView);
			initImageView();
		}

		private void initImageView() {
			imageView.setFitHeight(settings.squareHeight.get());
			imageView.setFitWidth(settings.squareWidth.get());
		}

		public String toString() {
			return "( " + column.get() + " | " + row.get() + " )";
		}

		public void setColor(ChessPieceColor color) {
			this.color = color;
		}

		public ChessPieceColor getColor() {
			return color;
		}
	}

	private static class Move {

		private static volatile Move move;
		private Piece figure;

		private Move(Piece figure) {
			this.figure = figure;
		}

		public static Move getMove(Piece figure) {
			Move lastMove = move;
			if (lastMove == null || figure != null) {
				synchronized (Move.class) {
					lastMove = move;
					if (lastMove == null || figure != null) {
						move = new Move(figure);
					}
				}
			}
			return lastMove;
		}

		public static Piece getPiece() {
			return move.figure;
		}
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

}
