package utils.conversions.pgn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private static final String STARTING_SYMBOL = "1.";
	private static final String PGN_STRING_SEPERATOR = " "; // Space

	private static final char PGN_COMMENT_START = '{';
	private static final char PGN_COMMENT_END = '}';

	private static final List<String> endings = Arrays.asList(new String[] { "1-0", "0-1", "1/2-1/2" });

	public static Map<String, List<Piece[][]>> parsePGNFile(String path) {
		Map<String, List<Piece[][]>> result = new HashMap<>();
		// iterating through the the file and calling for every PGNString
		// parsePGNStringToSimpleBoard
		int count = 0;
		int lastEffort = -1;
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

					pgnBuild = removeCommentsFromPGN(pgnBuild);
					pgnBuild = removeSubvariations(pgnBuild);
					pgn = pgnBuild.toString();
					
					// the pgn contains double numbers for example 24. b3 24... Qf5
					pgn = pgn.replaceAll("\\d+\\.\\.\\.", "");
					pgn = pgn.replaceAll("\\$\\d+", "");
					// the builded pgn still contains double spaces so we change all
					// double,tripple... spaces to one space
					pgn = pgn.toString().replaceAll("\\s+", PGN_STRING_SEPERATOR);
					
					int c = (int) (((++count) / 14757.0) * 100);

					if (lastEffort != c) {
						lastEffort = c;
						System.err.println("Effort: " + c + "%");
					}
					List<Piece[][]> gameHistory = parsePGNStringToSimpleBoard(pgn);
					result.put(pgn, gameHistory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static StringBuilder removeCommentsFromPGN(StringBuilder s) {
		return removeBetweenSigns(new StringBuilder(s), '{', '}');
	}

	public static StringBuilder removeSubvariations(StringBuilder s) {
		return removeBetweenSigns(s, '(', ')');
	}

	public static StringBuilder removeBetweenSigns(StringBuilder pgnBuild, char signStart, char signEnd) {
		// a pgnBuild can max contain comments like this {[]}
		for (int index = 0; index < pgnBuild.length(); index++) {
			if (signStart == pgnBuild.charAt(index)) {
				removeBetweenSigns(pgnBuild, index, signStart, signEnd);
			}
		}
		return pgnBuild;
	}

	// removes Comment with all sub Commands inside of it for instance { ...
	// {...{ }} }}
	// start is the index where removeCommentsFromPGN found a '{'
	private static StringBuilder removeBetweenSigns(StringBuilder pgnBuild, int start, char signStart, char signEnd) {
		for (int index = start + 1; index < pgnBuild.length(); index++) {
			if (signStart == pgnBuild.charAt(index)) {
				removeBetweenSigns(pgnBuild, index, signStart, signEnd);
			}
			if (signEnd == pgnBuild.charAt(index)) {
				return pgnBuild.delete(start, index + 1); // substring goes from start - (end -1)
			}

		}

		return pgnBuild;
	}

	// pgn string has following endings either "numb. moveWhite end" or "numb.
	// moveWhite moveBlack end"
	public static List<Piece[][]> parsePGNStringToSimpleBoard(String pgn) {
		BoardRepresentation board = null;
		try {
			List<Piece[][]> result = new ArrayList<>();
			String[] expressions = pgn.split(PGN_STRING_SEPERATOR);
			// moveEntry is represented by 3 expression numb moveWhite moveBlack
			final int moveEntryLength = 3;
			board = new BoardRepresentation(FENConverter.convertToPieceBoard(DEFAULT_BOARD));

			for (int i = 0; i < expressions.length; i += moveEntryLength) {

				String moveNumber = expressions[i];
				String whiteMoveS = expressions[i + 1];

				Move whiteMove = AlgebraicNotationConverter.getMove(whiteMoveS, board.getBoard(),
						ChessPieceColor.WHITE);
				BoardRepresentation boardAfterWhiteMove = makeCopyAndMove(whiteMove, board);
				result.add(boardAfterWhiteMove.getBoard());
				board = boardAfterWhiteMove;

				if (isEnding(expressions[i + 2])) {
					break;
				}

				String blackMoveS = expressions[i + 2];
				Move blackMove = AlgebraicNotationConverter.getMove(blackMoveS, board.getBoard(),
						ChessPieceColor.BLACK);
				BoardRepresentation boardAfterBlackMove = makeCopyAndMove(blackMove, board);
				result.add(boardAfterBlackMove.getBoard());

				board = boardAfterBlackMove;

				if (i + 3 < expressions.length && isEnding(expressions[i + 3])) {
					break;
				}

				// DEBUG
			}
			return result;
		} catch (Exception e) {
			System.err.println(pgn);
			System.err.println(board);
			throw e;
		}
	}

	private static boolean isEnding(String string) {
		return endings.contains(string);
	}

	public static int countSign(char c, String s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	public static BoardRepresentation makeCopyAndMove(Move move, BoardRepresentation board) {
		BoardRepresentation clone = board.softClone();
		try {
			if (move.pawnWillPromote()) {
				clone.makeMove(move.getOldPos(), move.getNewPos(), move.getPromotingPiece());
			} else {
				clone.makeMove(move.getOldPos(), move.getNewPos());
			}
		} catch (Exception e) {
			System.err.println("problem with making move on board \n" + board + " \n move: " + move);
			throw e;
		}
		return clone;
	}

	
}
