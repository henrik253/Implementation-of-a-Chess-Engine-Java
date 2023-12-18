package ai2.opening;

import java.util.List;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.model.gameLogic.BoardRepresentation;
import main.model.pieces.Piece;
import utils.Move;
import utils.Vector2D;
import utils.conversions.FENConverter;
import utils.conversions.pgn.PGNParser;

public class OpeningBook {
	
	public static final OpeningBook openingBook = new OpeningBook();
	
	private static boolean isInit; 
	
	private final static String FILE_PATH = "resources/";
	private final static String PGN_FILENAME = "opening.pgn";
	private final static String PATH = FILE_PATH + PGN_FILENAME;
	
	private String path;
	private final Piece[][] DEFAULT_BOARD = FENConverter.convertToPieceBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
	
	private Map<String, List<Piece[][]>> openingTable;
	private List<List<Piece[][]>> usableBoardHistorys;
	
	private OpeningBook(String path) {
		this.path = path;
		new Thread(() -> init()).start();
	}
	
	private OpeningBook() {
		this(PATH);
	}
	
	public void init() {
		System.out.println("STARTED PARSING PGN FILE");
		openingTable = PGNParser.parsePGNFile(Paths.get(path).toAbsolutePath().toString());
		System.out.println("FINISHED PARSING PGN FILE");
		updateUsableBoards(FENConverter.convertToPieceBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));
	}
	
	public boolean isInit() {
		return usableBoardHistorys != null;
	}

	public boolean hasNextMove() {
		return usableBoardHistorys.size() > 0;
	}

	public Move getNextMove(Piece[][] currentBoard) {
		//updateUsableBoards(currentBoard);
		Piece[][] board = takeBoardOfUsableBoardHistory(currentBoard);
		
		return filterOutMove(currentBoard, board);
	}

	public void updateUsableBoards(Piece[][] currentBoard) {
		if (usableBoardHistorys == null) {
			usableBoardHistorys = new ArrayList<>();
			Set<Entry<String, List<Piece[][]>>> entrySet = openingTable.entrySet();
			for (Map.Entry<String, List<Piece[][]>> gameHistoryEntry : entrySet) {
				usableBoardHistorys.add(gameHistoryEntry.getValue());
			}
		} else {
			List<List<Piece[][]>> updated = new ArrayList<>();
			usableBoardHistorys.forEach(gameHistory -> gameHistory.forEach(board -> {
				if (BoardRepresentation.isSameBoard(currentBoard, board)) {
					updated.add(gameHistory);
				}
			}));
			usableBoardHistorys = updated;
		}
	}

	private Piece[][] takeBoardOfUsableBoardHistory(Piece[][] currentBoard) {
		List<Piece[][]> possibleBoards = new ArrayList<>();
		
		for (List<Piece[][]> gameHistory : usableBoardHistorys) {
			int indexOfNextBoard = 0;
		
			for (Piece[][] board : gameHistory) {
				if (++indexOfNextBoard >= gameHistory.size()) {
					continue;
				}
				if (isSameBoard(board, currentBoard)) {
					possibleBoards.add(gameHistory.get(indexOfNextBoard));
					break;
				}
			}
		}
		if(possibleBoards.size() == 0) {
			throw new NoSuchElementException(" couldnt find a matching board ...");
		}
	//	System.out.println("All possible Moves");
	//	possibleBoards.forEach(board -> System.out.println(new BoardRepresentation(board)));
		return possibleBoards.get(((int) Math.random() * possibleBoards.size()));
	}

	public boolean isSameBoard(Piece[][] board1, Piece[][] board2) {
		for (int i = 0; i < board1.length; i++) {
			for (int j = 0; j < board1[i].length; j++) {
				Piece board1Piece = board1[i][j];
				Piece board2Piece = board2[i][j];
				if (board1Piece != null && board2Piece != null) { // are pieces
					if (board1Piece.getName() == board2Piece.getName()
							&& board1Piece.getColor() == board2Piece.getColor()) {
						continue;
					} else {
						return false;
					}
				} else if (board1Piece == null && board2Piece == null) {
					continue;
				} else {
					return false;
				}

			}
		}
		return true;
	}

	
	private Move filterOutMove(Piece[][] current, Piece[][] nextBoard) {
		Vector2D from = null, to = null;

		for (int row = 0; row < current.length; row++) {
			for (int col = 0; col < current[row].length; col++) {
				Piece currentPiece = current[row][col];
				Piece nextPiece = nextBoard[row][col];
				if (currentPiece == null && nextPiece != null) {
					to = new Vector2D(col, row);
				}
				if (nextPiece == null && currentPiece != null) {
					from = new Vector2D(col, row);
				}
			}
		}
		if(from == null || to == null) {
			System.err.println(new BoardRepresentation(current));
			System.err.println(new BoardRepresentation(nextBoard));
			throw new NoSuchElementException("couldnt find move");
		} 
		
		return new Move(from, to);
	}
	
	public boolean isInitalized() {
		return isInit;
	}
	
//	public static void main(String[] args) {
//	
//		OpeningBook openingBook = new OpeningBook();
//		System.out.println("Testing filterOutMove \n");
//		Piece[][] normal = FENConverter.convertToPieceBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
//		Piece[][] next =  FENConverter.convertToPieceBoard("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R");
//		System.out.println(openingBook.filterOutMove(normal,next));
//		Piece[][] next2 =  FENConverter.convertToPieceBoard("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKB1R");
//		System.out.println(openingBook.filterOutMove(next, next2));
//		System.out.println("\n Testing isSameBoard \n");
//		System.out.println(openingBook.isSameBoard(next,normal));
//		System.out.println(openingBook.isSameBoard(next, next2));
//		System.out.println(openingBook.isSameBoard(next,next));
//		System.out.println("\n Testing openingBook \n");
//		openingBook.init();
//		System.out.println(openingBook.getNextMove(next));
//	}

}
