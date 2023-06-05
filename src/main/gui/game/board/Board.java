package main.gui.game.board;

import java.io.Serializable;

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

import main.gui.game.settings.Settings;
import main.model.chessPieces.SimplePiece;

public class Board extends GridPane {

	private Settings settings;
	private GameView gameView;
	private static final String IMAGES_PATH = "file:resources/";
	private static final String FILE_FORMAT = ".png";
	public Board() {
		this.setTranslateX(0);
		this.setTranslateY(0);
		this.setOnDragOver(this::onDragOver);
		this.setOnDragDropped(this::onDragDropped);

	}

	public void init() {

	}

	// "file:resources/BishopBlack.png"

	public void initSimplePieceBoard(SimplePiece[][] board) {
		for (int row = 0; row < settings.rows; row++) {
			for (int column = 0; column < settings.columns; column++) {
				SimplePiece simplePiece = board[row][column];
				if (simplePiece == null)
					continue;

				String imageName = simplePiece.toString();
				Piece piece = new Piece(row, column);
				piece.setImage(new Image(IMAGES_PATH + imageName + FILE_FORMAT));
				this.add(piece, column, row);
			}
		}
	}

	private void onDragOver(DragEvent event) {
		event.acceptTransferModes(TransferMode.MOVE);
	}

	private void onDragDropped(DragEvent event) {
		Piece draggedPiece = Move.getPiece();

		int oldX = draggedPiece.getColumn();
		int oldY = draggedPiece.getRow();
		int newX = (int) (event.getSceneX() / settings.squareWidth.get());
		int newY = (int) (event.getSceneY() / settings.squareHeight.get());

		this.getChildren().remove(draggedPiece);
		this.add(draggedPiece, newX, newY);
	}

	public void draw() {
		for (int row = 0; row < settings.rows; row++) {
			for (int column = 0; column < settings.columns; column++) {
				Color color = (row + column) % 2 == 0 ? settings.brightColor.get() : settings.darkColor.get();
				add(new Square(settings.squareWidth.get(), settings.squareHeight.get(), color), row, column);
			}
		}
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

		public Piece(int column, int row) {
			this.setPrefWidth(settings.squareWidth.get());
			this.setPrefHeight(settings.squareHeight.get());

			this.column = new SimpleIntegerProperty(column);
			this.row = new SimpleIntegerProperty(row);
			this.setOnDragDetected(this::onDragDetected);

		}

		private void onDragDetected(MouseEvent event) {
			Dragboard dragboard = this.startDragAndDrop(TransferMode.ANY);
			ClipboardContent content = new ClipboardContent();

			Move.getMove(this);
			Image image = imageView.getImage();
			content.putImage(image);
			dragboard.setContent(content);

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

	}

	public static class Move {

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

}
