package utils.conversions.pgn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.model.gameLogic.*;
import main.model.pieces.*;
import utils.ChessPieceColor;
import utils.Move;
import utils.SimplePiece;
import utils.Vector2D;
import utils.conversions.FENConverter;

/*
 * structure of PGN file Every Entry in the PGN has the following structure: 1.
 * information Text encapsulated in [] 2. the game itself 2.1 a move by white
 * and black is prefixed with the move numb. 2.2 moves are obviously represented
 * in chess notation
 * 
 * 
 * Parsing Algebraic notation into numbers
 * 
 * Notation for moves for PGN source:
 * https://en.wikipedia.org/wiki/Algebraic_notation_(chess) 1.Piece type is
 * identified by an uppercase letter 1.1 K (King) ,Q (Queen) ,R (Rook) ,B
 * (Bishop) ,N (Knight) 2.Notation for move 2.2 Piece Type followed with the
 * target square. 2.3 When using a pawn only the target square is used
 * 
 * 3. Captures When a piece is captured an "x" is used
 * 
 * 4. Disambiguating moves when two or more pieces can move to the same square
 * the piecePos is added 4.1 for example Qh4e1 means Queen from h4 to e1 4.2 if
 * rooks are on the same row/column only the letter or the numb can be written
 * down for instance: Rdf8
 * 
 * These rule can be combined with capturing.
 * 
 * 5. Special Rules Pawn promotion: example: e8=Q Draw offer: (=) Castling: O-O
 * (kingside castling) O-O-O (queenside castling) Check: if a moves checks an +
 * is appended Checkmate: # represents checkmate
 */
// Algorithm implementation: 
public class PGNParser {

	private static final String DEFAULT_BOARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	private static Piece[][] startingBoard =new BoardRepresentation(FENConverter.convertToPieceBoard(DEFAULT_BOARD)).getBoard();

	private static final String STARTING_SYMBOL = "1.";
	private static final String PGN_STRING_SEPERATOR = " "; // Space

	private static final char PGN_COMMENT_START = '{';
	private static final char PGN_COMMENT_END = '}';

	public static Map<String, List<Piece[][]>> parsePGNFile(String path) {
		Map<String, List<Piece[][]>> result = new HashMap<>();
		// iterating through the the file and calling for every PGNString
		// parsePGNStringToSimpleBoard
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			// filtering the moves out of the PGN

			String line = "";
			while ((line = reader.readLine()) != null) {
				// if the first line contains a 1. we starting snipping out the PGN Move String
				String pgn = "";
				if (line.length() >= STARTING_SYMBOL.length()
						&& line.substring(0, STARTING_SYMBOL.length()).equals(STARTING_SYMBOL)) {
					StringBuilder pgnBuild = new StringBuilder(line);
					// snipping out the pgn string
					while ((line = reader.readLine()) != null && !line.isEmpty()) {
						pgnBuild.append(" ");
						pgnBuild.append(line);
					}
					pgn = pgnBuild.toString();
				}
				// pgn still contains comments that are represented with "( ... )" or "( { } )"
				if (!pgn.isEmpty()) {
					System.out.println(pgn);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// pgn string has following endings either "numb. moveWhite end" or "numb.
	// moveWhite moveBlack end"
	public static List<Piece[][]> parsePGNStringToSimpleBoard(String pgn) {
		List<Piece[][]> result = new ArrayList<>();
		String[] expressions = pgn.split(PGN_STRING_SEPERATOR);
		// moveEntry is represented by 3 expression numb moveWhite moveBlack
		final int moveEntryLength = 3;
		Piece[][] board = startingBoard;
		
		for (int i = 0; i < expressions.length; i += moveEntryLength) {
			
			if (i + moveEntryLength + 1 == expressions.length) {
				// checking if we would be at end with next iteration like the following numb. moveWhite moveBlack end
				System.out.println(expressions[i] + " " + expressions[i + 1] + " " + expressions[i + 2] + " "+ expressions[i + 3]);
				break;
			}
			String moveNumber = expressions[i];
			String whiteMoveS = expressions[i + 1];
			String blackMoveS = expressions[i + 2];
			
			
			
			// DEBUG 
			System.out.println("String for white move " + whiteMoveS);
			Move whiteMove = AlgebraicNotationConverter.getMove(whiteMoveS,board,ChessPieceColor.WHITE); 
			
			
			BoardRepresentation boardRwhiteMoved = makeMoveAndCopy(whiteMove,board);
			
			System.out.println("Board after white moved: \n" + boardRwhiteMoved);
			board = boardRwhiteMoved.getBoard();
			System.out.println(whiteMove);
			
			System.out.println("String for black move : " + blackMoveS);
			Move blackMove = AlgebraicNotationConverter.getMove(blackMoveS,board,ChessPieceColor.BLACK); 
		
			BoardRepresentation boardRblackMoved = makeMoveAndCopy(blackMove,board);
			
			System.out.println("Board after black moved: \n" +boardRblackMoved);
			System.out.println(blackMove);
			
			board = boardRblackMoved.getBoard();
			
			// DEBUG
		}
		return result;
	}
	
	public static BoardRepresentation makeMoveAndCopy(Move move,Piece[][] board){
		Piece[][] copy = new Piece[board.length][];
		Vector2D from = move.getOldPos() , to = move.getNewPos();
		for(int i = 0; i < board.length ; i++) {
			copy[i] = Arrays.copyOf(board[i],board[i].length);
		}
		copy[to.getY()][to.getX()] = copy[from.getY()][from.getX()];
		copy[from.getY()][from.getX()] = null;
		
		return new BoardRepresentation(copy);
	}
	
	private static final String PGN_FILE_PATH = "C:\\Users\\Genii\\Desktop\\Teamprojekt\\resources\\opening.pgn";
	public static void main(String[] args) {
		parsePGNFile(PGN_FILE_PATH);
		String pgn = "1. e4 c5 2. c3 Nf6 3. e5 Nd5 4. Nf3 Nc6 5. d4 cxd4 6. cxd4 d6 7. Bc4 e6 8. O-O Be7 9. exd6 Qxd6 10. Nc3 O-O 11. Re1 Rd8 12. Bb3 a6 13. g3 b6 14. Nxd5 exd5 15. Bf4 Qd7 16. Rc1 Bb4 17. Re5 Nxe5 18. Nxe5 Qe6 19. Nc6 Be7 20. Nxd8 Bxd8 21. Qf1 Bb7 22. Re1 Qd7 23. Qh3 f5 24. Re5 Bf6 25. Qxf5 Qxf5 26. Rxf5 Rc8 27. Be5 Rc1+ 28. Kg2 Kf7 29. Rf3 Re1 30. Bxf6 gxf6 31. Re3 Rxe3 32. fxe3 f5 33. Ba4 a5 34. Kf3 Kf6 35. Kf4 Bc8 36. Bc6 Be6 37. a3 h6 38. Bb7 Bg8 39. Bc8 Be6 40. Bxe6 Kxe6 41. g4 fxg4 42. Kxg4 a4 43. Kf4 1-0";
		System.out.println(pgn);
		PGNParser.parsePGNStringToSimpleBoard(pgn);
	}
}
