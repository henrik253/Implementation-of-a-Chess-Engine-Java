package test.main.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import main.model.gameLogic.*;
import main.model.pieces.Piece;
import main.model.pieces.Queen;
import utils.ChessPieceColor;
import utils.Vector2D;
import utils.conversions.FENConverter;

public class CheckClassTest {

	private final String DoubleRookCheckMateByBlack = "rr6/8/8/8/8/8/8/K7";
	private final String DoubleRookNoCheckMate = "rr6/8/2K5/8/8/8/8/8";
	private final String DoubleRookCheckMateByWhite = "k7/8/8/8/8/8/RR6/K7";
	private final String QueenCheckingWhiteKing = "rr1k4/1q6/8/8/8/8/6K1/8";
	private final String DoubleRookCheckMateByWhiteKingMovesBehind = "8/8/k7/8/8/8/RR6/K7";
	private final String DoubleRookNoCheckMateByWhiteKingMovesToRight = "8/8/k7/8/8/8/R1R5/K7";
	private final String kingCantMoveSurroundedByWhiteRooks = "8/2R1R3/5R2/3k4/1R6/8/2R5/K7";
	private final String kingCanTakeSurroundedByWhiteRooks = "8/2R5/4R3/3k4/1R6/8/2R5/K7";

	final BoardRepresentation board1 = new BoardRepresentation(
			FENConverter.convertToPieceBoard(DoubleRookCheckMateByBlack));
	final BoardRepresentation board2 = new BoardRepresentation(FENConverter.convertToPieceBoard(DoubleRookNoCheckMate));
	final BoardRepresentation board3 = new BoardRepresentation(
			FENConverter.convertToPieceBoard(DoubleRookCheckMateByWhite));
	final BoardRepresentation board4 = new BoardRepresentation(FENConverter.convertToPieceBoard(QueenCheckingWhiteKing));

	@Test
	public void test_getCheckingPiece() {
		System.out.println("START test_getCheckingPiece");
		final BoardRepresentation board1 = new BoardRepresentation(
				FENConverter.convertToPieceBoard(DoubleRookCheckMateByBlack));
		System.out.println("board1 \n" +board1.toBoardString());
		Piece expected = board1.getPiece(new Vector2D(0, 0));
		Piece actual = Check.getCheckingPiece(board1, ChessPieceColor.WHITE);
		assertEquals(expected, actual);

		final BoardRepresentation board2 = new BoardRepresentation(
				FENConverter.convertToPieceBoard(DoubleRookNoCheckMate));
		System.out.println("board2 \n" + board2.toBoardString());
		assertEquals(Check.getCheckingPiece(board2, ChessPieceColor.WHITE), null);

		final BoardRepresentation board3 = new BoardRepresentation(
				FENConverter.convertToPieceBoard(DoubleRookCheckMateByWhite));
		System.out.println("board3 \n" +board3.toBoardString());
		expected = board3.getPiece(new Vector2D(0, 6));
		actual = Check.getCheckingPiece(board3, ChessPieceColor.BLACK);
		assertEquals(expected, actual);

		final BoardRepresentation board4 = new BoardRepresentation(
				FENConverter.convertToPieceBoard(QueenCheckingWhiteKing));

		System.out.println("board4 \n" + board4.toBoardString());
		assertTrue(Check.getCheckingPiece(board4, ChessPieceColor.WHITE) instanceof Queen);
		System.out.println("END test_getCheckingPiece");
	}

	final BoardRepresentation board5 = getBoard(DoubleRookCheckMateByWhiteKingMovesBehind);
	final BoardRepresentation board6 = getBoard(DoubleRookNoCheckMateByWhiteKingMovesToRight);
	final BoardRepresentation board7 = getBoard(kingCantMoveSurroundedByWhiteRooks);
	final BoardRepresentation board8 = getBoard(kingCanTakeSurroundedByWhiteRooks);
	final BoardRepresentation board9 = getBoard("8/8/2R1R3/1Q1k4/2R1R3/8/8/K7"); // cant be stopped
	final BoardRepresentation board10 = getBoard("k3R3/1Q6/8/8/8/R7/8/8"); // can be stopped by taking the Queen
	final BoardRepresentation board11 = getBoard("k3R3/1Q6/8/8/4B3/R7/8/8"); // cant be stopped, queen is protected by
																				// // bishop
	final BoardRepresentation board12 = getBoard("8/1q6/3K4/4q3/2n5/8/8/8"); // cant be stopped, black queen is //
																				// protected by knight
	final BoardRepresentation board13 = getBoard("8/1q6/3K4/4q3/8/8/8/8"); // can be stopped, black queen is not
																			// protected

	@Test
	public void test_kingCanMove() {
		System.out.println("START test_kingCanMove");
		System.out.println("board1 \n" + board1.toBoardString());
		assertFalse(Check.kingCanMove(board1, ChessPieceColor.WHITE));
		
		System.out.println("board3 \n" + board3.toBoardString());
		assertFalse(Check.kingCanMove(board3, ChessPieceColor.BLACK));

		System.out.println("board5 \n" + board5.toBoardString());
		assertFalse(Check.kingCanMove(board5, ChessPieceColor.BLACK));

		System.out.println("board6 \n" + board6.toBoardString());
		assertTrue(Check.kingCanMove(board6, ChessPieceColor.BLACK));

		System.out.println("board7 \n" + board7.toBoardString());
		assertFalse(Check.kingCanMove(board7, ChessPieceColor.BLACK));

		System.out.println("board8 \n" + board8.toBoardString());
		assertTrue(Check.kingCanMove(board8, ChessPieceColor.BLACK));

		System.out.println("board9 \n" + board9.toBoardString());
		assertFalse(Check.kingCanMove(board9, ChessPieceColor.BLACK));

		System.out.println("board10 \n" + board10.toBoardString());
		assertTrue(Check.kingCanMove(board10, ChessPieceColor.BLACK));

		System.out.println("board11 \n" + board11.toBoardString());
		assertFalse(Check.kingCanMove(board11, ChessPieceColor.BLACK));

		System.out.println("board12 \n" + board12.toBoardString());
		assertFalse(Check.kingCanMove(board12, ChessPieceColor.WHITE)); // white king 
		
		System.out.println("board13 \n" + board13.toBoardString());
		assertTrue(Check.kingCanMove(board13, ChessPieceColor.WHITE));
		
		System.out.println("END test_kingCanMove");
	}
	//

	final BoardRepresentation board14 = getBoard("K7/8/8/8/8/q7/1q6/4B3"); // Bishop can block the checking queen
	final BoardRepresentation board15 = getBoard("K7/8/3B4/8/8/q7/1q6/8"); // Bishop can take the checking queen
	final BoardRepresentation board16 = getBoard("K7/2n5/3B4/8/8/q6R/1q2N3/3k4"); // Double Check by Knight and Queen
	final BoardRepresentation board17 = getBoard("K7/2n5/3Bq3/8/8/7R/1q2N3/3k4"); // Bishop can take checking knight
	
	final BoardRepresentation boardFromGame1 = getBoard("1r4n1/p2Q2pr/7p/3RP3/1kp1P3/5N2/1P1n2PP/1K6"); // Black Knight checking king
	
	@Test
	public void test_checkCanBeStopped() {
		System.out.println("START test_checkCanBeStopped");
		System.out.println("board14 \n" +board14.toBoardString());
		Piece checkingPiece = Check.getCheckingPiece(board14, ChessPieceColor.WHITE);
		System.out.println("Checking Piece: " + checkingPiece);
		assertTrue(Check.checkCanBeStoppedByAnyPiece(board14, ChessPieceColor.WHITE, checkingPiece));

		System.out.println("board15 \n" +board15.toBoardString());
		checkingPiece = Check.getCheckingPiece(board15, ChessPieceColor.WHITE);
		System.out.println("Checking Piece: " + checkingPiece);
		assertTrue(Check.checkCanBeStoppedByAnyPiece(board15, ChessPieceColor.WHITE, checkingPiece));

		System.out.println("board16 \n" +board16.toBoardString());
		checkingPiece = Check.getCheckingPiece(board16, ChessPieceColor.WHITE);
		System.out.println("Checking Piece: " + checkingPiece);
		assertFalse(Check.checkCanBeStoppedByAnyPiece(board16, ChessPieceColor.WHITE, checkingPiece));

		System.out.println("board17 \n" +board17.toBoardString());
		checkingPiece = Check.getCheckingPiece(board17, ChessPieceColor.WHITE);
		System.out.println("Checking Piece: " + checkingPiece);
		assertTrue(Check.checkCanBeStoppedByAnyPiece(board17, ChessPieceColor.WHITE, checkingPiece));
		
		System.out.println("board from playing Game \n" + boardFromGame1.toBoardString());
		checkingPiece = Check.getCheckingPiece(boardFromGame1, ChessPieceColor.WHITE);
		assertTrue(Check.checkCanBeStoppedByAnyPiece(boardFromGame1, ChessPieceColor.WHITE, checkingPiece));
		
		System.out.println("END test_checkCanBeStopped");
	}

	private BoardRepresentation getBoard(String fen) {
		return new BoardRepresentation(FENConverter.convertToPieceBoard(fen));
	}
	
	final BoardRepresentation board18 = getBoard("k7/1Q6/8/2N5/8/8/8/8"); // Checkmate by white
	final BoardRepresentation board19 = getBoard("k7/2Q5/8/2N5/8/8/8/8"); // No Checkmate by white
	final BoardRepresentation board20 = getBoard("k7/3Q4/8/8/1R2B3/8/2N5/3K4"); //Checkmate by white
	final BoardRepresentation board21 = getBoard("8/1k6/4r3/8/8/8/3n2qr/5K2"); // Checkmate by black
	final BoardRepresentation board22 = getBoard("8/1k4q1/4r3/8/2n5/8/7r/5K2"); // No Checkmate by black
	final BoardRepresentation board23 = getBoard("rnbqkbnr/pppQpppp/8/1B6/4P3/8/PPPP1PPP/RNB1K1NR"); // No Checkmate by white,
	// queen can be taken. 
	final BoardRepresentation board24 = getBoard("r1bqkbnr/4pQpp/np2B3/p1p5/4P3/8/PPPP1PPP/RNB1K1NR"); // Checkmate by white
	
	
	
	@Test
	public void test_CheckMate() {
		System.out.println("board18 \n" + board18.toBoardString());
		assertTrue(Check.isMate(board18, ChessPieceColor.BLACK));
		System.out.println("board19 \n" + board18.toBoardString());
		assertFalse(Check.isMate(board19, ChessPieceColor.BLACK));// falsse .
		System.out.println("board20 \n" + board18.toBoardString());
		assertTrue(Check.isMate(board20, ChessPieceColor.BLACK));// false
		System.out.println("board21 \n" + board18.toBoardString());
		assertTrue(Check.isMate(board21, ChessPieceColor.WHITE));
		System.out.println("board22 \n" + board18.toBoardString());
		assertFalse(Check.isMate(board22, ChessPieceColor.WHITE));
		System.out.println("board23 \n" + board18.toBoardString());
		assertFalse(Check.isMate(board23, ChessPieceColor.BLACK));
		System.out.println("board23 \n" + board18.toBoardString());
		assertTrue(Check.isMate(board24, ChessPieceColor.BLACK));
	}
	
	final BoardRepresentation board25 = getBoard("k7/1q3r2/3n4/4q3/Bn6/8/1N6/K2B1R2"); // Knight on 2 b pinned
	final BoardRepresentation board26 = getBoard("k7/1q6/3n4/4q3/Bn6/8/1N6/K1rB1R2"); // Knight on 2 b pinned + king in Check!
	final BoardRepresentation board27 = getBoard("k7/1q6/3n4/1q6/Bn6/8/1N6/K1rB1R2"); // Knight on 2 b not pinned + king in Check!
	
	
	@After public void test_PiecePinned() {
		System.out.println("board 25 \n "  + board25);
		Piece knight25 =  board25.getPiece(new Vector2D(1,6));
		assertTrue(Check.isPiecePinned(board25, knight25));
		
		System.out.println("board 26 \n "  + board26);
		Piece knight26 = board26.getPiece(new Vector2D(1,6));
		assertTrue(Check.isPiecePinned(board26, knight26));
		
		System.out.println("board 27 \n "  + board27);
		Piece knight27 = board27.getPiece(new Vector2D(1,6));
		assertFalse(Check.isPiecePinned(board27, knight27));
	}
	
}
