package main.gui.game.board.view;

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
import utils.ChessPieceColor;
import utils.SimplePiece;
import utils.Vector2D;

public class Board extends GridPane {

	private Settings settings;
	private GameView gameView;

	private static final String IMAGE_PATH = "file:resources/";
	private static final String FILE_FORMAT = ".png";

	private List<Piece> piecesOnBoard = new LinkedList<>();
	private Square[][] squares;

	private boolean inverted = false;
	private ChessPieceColor disabledSide = ChessPieceColor.BLACK;

	public Board() {
		this.setTranslateX(0);
		this.setTranslateY(0);
		this.setOnDragOver(this::onDragOver);
		this.setOnDragDropped(this::onDragDropped);

	}

	public void init() {
		squares = new Square[settings.rows][settings.columns];
	}

	private void onDragOver(DragEvent event) {
		event.acceptTransferModes(TransferMode.MOVE);
	}

	private void onDragDropped(DragEvent event) {
		Piece draggedPiece = MovedPiece.get();

		int oldX = draggedPiece.getRow();
		int oldY = draggedPiece.getColumn();
		int newX = (int) (event.getSceneX() / settings.squareWidth.get());
		int newY = (int) (event.getSceneY() / settings.squareHeight.get());

		gameView.unmarkLastMovebleSquares();

		if (oldX != newX || oldY != newY) {
			Vector2D oldPos = new Vector2D(oldX, oldY);
			Vector2D newPos = new Vector2D(newX, newY);

			if (inverted) {
				oldPos.invertY(settings.rows - 1);
				oldPos.invertX(settings.rows - 1);
				newPos.invertX(settings.rows - 1);
				newPos.invertY(settings.rows - 1);
			}

			if (gameView.moveRequest(oldPos, newPos)) {
				gameView.userMoveSucceeded();
			}
		}

	}

	public void drawBoard() {
		this.getChildren().clear();
		clearSquares();

		Color color1 = settings.brightColor.get();
		Color color2 = settings.darkColor.get();
		if (inverted) {
			color1 = settings.darkColor.get();
			color2 = settings.brightColor.get();
		}

		for (int row = 0; row < settings.rows; row++) {
			for (int column = 0; column < settings.columns; column++) {
				Color color = (row + column) % 2 == 0 ? color1 : color2;
				Square square = new Square(settings.squareWidth.get(), settings.squareHeight.get(), color);
				squares[row][column] = square;
				add(square, column, row); // its actually row,column in the whole game!
				// but for gridPane its swapped!
			}
		}
	}

	public void markSquare(Vector2D pos, Color color) {
		Color color1 = settings.brightColor.get();
		Color color2 = settings.darkColor.get();

		Color c = (pos.getY() + pos.getX()) % 2 == 0 ? color1 : color2;
		Color finalColor = c.interpolate(color, 0.3);
		squares[pos.getY()][pos.getX()].setFill(finalColor);
	}

	public void unmarkSquare(Vector2D pos) { // auto Color
		Color color1 = settings.brightColor.get();
		Color color2 = settings.darkColor.get();
		Color color = (pos.getY() + pos.getX()) % 2 == 0 ? color1 : color2;

		squares[pos.getY()][pos.getX()].setFill(color);
	}

	private void clearSquares() {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares[i].length; j++) {
				squares[i][j] = null;
			}
		}
	}

	public void drawPiecesOnBoard(SimplePiece[][] board) {
		clearPiecesOnBoard();
		
		if(board == null) {
			System.err.println("drawPiecesOnBoard(board), board == null = true");
			return;
		}
		
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

		disablePieceListener(disabledSide);
	}

	private SimplePiece[][] invertBoard(SimplePiece[][] board) {
		if(board == null) {
			throw new IllegalArgumentException("board is null");
		}
		
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

	public ChessPieceColor getDisabledSide() {
		return disabledSide;
	}

	public void setDisabledSide(ChessPieceColor disabledSide) {
		this.disabledSide = disabledSide;
	}

	public boolean isInverted() {
		return inverted;
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
			MovedPiece.getMove(this);
			Image image = imageView.getImage();
			content.putImage(image);
			dragboard.setContent(content);
			this.setVisible(false);
			showMoveableSquares();
		}

		private void showMoveableSquares() {
			Vector2D currentPos = new Vector2D(row.get(), column.get());
			gameView.showMoveablePositions(currentPos);

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

	private static class MovedPiece {

		private static volatile MovedPiece move;
		private Piece figure;

		private MovedPiece(Piece figure) {
			this.figure = figure;
		}

		public static MovedPiece getMove(Piece figure) {
			MovedPiece lastMove = move;
			if (lastMove == null || figure != null) {
				synchronized (MovedPiece.class) {
					lastMove = move;
					if (lastMove == null || figure != null) {
						move = new MovedPiece(figure);
					}
				}
			}
			return lastMove;
		}

		public static Piece get() {
			return move.figure;
		}
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

}
