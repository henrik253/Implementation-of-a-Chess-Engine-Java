package test.chessai;

import org.junit.Test;

import ai2.opening.OpeningBook;
import main.model.pieces.Piece;
import utils.Move;
import utils.Vector2D;
import utils.conversions.FENConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;

public class OpeningBookTest {

	private static final String DEFAULT_BAORD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	private OpeningBook openingBook = new OpeningBook();
	
	
	@Before
	public void init() {
		openingBook.init();
	}
	
	@Test
	public void test_updateUsableBoards() {
		
	}
	
	@Test
	public void test_takeBoardOfUsableBoardHistory() {}
	
	@Test
	public void test_isSameBoard() {}
	
	@Test
	public void test_filterOutMove() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Method filterOutMove = OpeningBook.class.getDeclaredMethod("filterOutMove", Piece[][].class,Piece[][].class);
		
		Piece[][] normal = FENConverter.convertToPieceBoard(DEFAULT_BAORD);
		Piece[][] board =  FENConverter.convertToPieceBoard("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R");
		
		assertEquals(new Move(new Vector2D(6,6),new Vector2D(5,5)),filterOutMove.invoke(normal, board));
	}
}
