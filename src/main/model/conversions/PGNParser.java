package main.model.conversions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.SimpleBoard;
import utils.SimplePiece;

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
	private static SimplePiece[][] startingBoard = FENConverter.convertSimplePieceBoard(DEFAULT_BOARD);
	
	private static final String STARTING_SYMBOL = "1.";
	private static final char PGN_STRING_SEPERATOR = ' '; // Space 
	
	private static final char PGN_COMMENT_START = '{';
	private static final char PGN_COMMENT_END = '}';
	
	public static Map<String,List<SimpleBoard>> parsePGNFile(String path){
		Map<String,List<SimpleBoard>> result = new HashMap<>();
		
		// iterating through the the file and calling for every PGNString parsePGNStringToSimpleBoard
		try(BufferedReader reader = new BufferedReader(new FileReader(path)))
		{	
			// filtering the moves out of the PGN 
			
			String line = ""; 
			while((line = reader.readLine()) != null) {
				// if the first line contains a 1. we starting snipping out the PGN Move String
				String pgn = "";
				if(line.length() >= STARTING_SYMBOL.length() && line.substring(0,STARTING_SYMBOL.length()).equals(STARTING_SYMBOL)) {
					StringBuilder pgnBuild = new StringBuilder(line);
					// snipping out the pgn string
					while( (line = reader.readLine()) != null && !line.isEmpty()) {
						pgnBuild.append(line);
					}
					pgn = pgnBuild.toString();
				}
				
				if(!pgn.isEmpty()) {
					System.out.println(pgn);
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return result; 
	}

	public static List<SimpleBoard> parsePGNStringToSimpleBoard(String pgn) {
		List<SimpleBoard> result = new ArrayList<>();
		String word  = "";
		
		// iterating through the pgn string and creating boards
		for(int index = 0; index < pgn.length() ; index++) {
			StringBuilder whiteAndBlackMove = new StringBuilder("");
			if(Character.isDigit(pgn.charAt(index))) {
				// snipping out the move by black and white and setting new index; for example: 1. e4 e6 2.
				int tempIndex = index; 
				char c;
				while(!Character.isDigit((c = pgn.charAt(++tempIndex)) )) {
					whiteAndBlackMove.append(c);
				}
				index = tempIndex - 1; // -2 to set it behind the next numb, in the aboce example the index would be at  1. e4 e6_2. (marked with "_")
			}
			
			System.out.print(whiteAndBlackMove.toString() + "#");
			
		}
		return result;
	}
	

}
