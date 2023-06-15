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
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.Settings;
import main.model.Vector2D;
import main.model.chessPieces.SimplePiece;

public class Board extends GridPane {

	private Settings settings;
	private GameView gameView;

	private static final String IMAGE_PATH = "file:resources/";
	private static final String FILE_FORMAT = ".png";

	private List<Piece> piecesOnBoard = new LinkedList<>();

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

		int oldX = draggedPiece.getColumn();
		int oldY = draggedPiece.getRow();
		int newX = (int) (event.getSceneX() / settings.squareWidth.get());
		int newY = (int) (event.getSceneY() / settings.squareHeight.get());

		boolean validMove = false;

		if (oldX != newX || oldY != newY)
			validMove = gameView.moveRequest(oldX, oldY, newX, newY);

		if (validMove) {
			movePiece(draggedPiece, newX, newY);

		}

	}

	private void movePiece(Piece piece, int x, int y) {
		piece.setRow(x);
		piece.setColumn(y);
		this.getChildren().remove(piece);
		this.add(piece, x, y);
	}

	public void removePiece(Vector2D pos) {
		
		for(Piece piece : piecesOnBoard) {
			System.out.println(piece); // ROWS AND COLUMNS ARE SWITCHED ??? NEED TO BE FIXED IT!!!!
			if(piece.getRow() == pos.getX() && piece.getColumn() == pos.getY()) {
				System.out.println("FOUND IT");
				this.getChildren().remove(piece); 
				
			}
		}
	}
	
	public void drawBoard() {

		drawBoard(false);
	}

	public void drawBoard(boolean inverted) {
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
		drawPiecesOnBoard(board, false);
	}

	public void drawPiecesOnBoard(SimplePiece[][] board, boolean inverted) {
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

				piecesOnBoard.add(piece);
				this.add(piece, column, row);
			}
		}
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

		public void setColumn(int column) {
			this.column.set(column);
		}

		public void setRow(int row) {
			this.row.set(row);
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


}
