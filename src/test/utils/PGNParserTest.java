package test.utils;

import org.junit.Test;

import utils.conversions.pgn.PGNParser;

public class PGNParserTest {

	private static final String PATH = "file:resources/";
	private static final String FILE_NAME = "opening";
	private static final String FILE_FORMAT = ".pgn";
	private static final String PGN_FILE_PATH = "C:\\Users\\Genii\\Desktop\\Teamprojekt\\resources\\opening.pgn";

	
	@Test
	public void test_Print_PGN() {
		//PGNParser.parsePGNFile(PGN_FILE_PATH);
	}
	
	@Test
	public void test_filtering_Move_PGN() {
		String pgn = "1. d4 Nf6 2. c4 e6 3. Nf3 Bb4+ 4. Nbd2 b6 5. a3 Bxd2+ 6. Qxd2 Bb7 7. e3 d6 8.b4 Nbd7 9. Bb2 Ne4 10. Qc2 O-O 11. Be2 f5 12. O-O Rf6 13. d5 Rg6 14. Rad1 Nf815. g3 c5 16. bxc5 bxc5 17. Ne1 exd5 18. cxd5 Qg5 19. Ng2 Re8 20. Bb5 Rb8 21.Bc4 Rh6 22. Nf4 Ng6 23. Qe2 Ne5 24. Bxe5 dxe5 25. Ne6 Qe7 26. Qb2 Rxe6 27. dxe6Ng5 28. e4 Nh3+ 29. Kg2 g5 30. Bd5 1-0";
		System.out.println(pgn);
		PGNParser.parsePGNStringToSimpleBoard(pgn);
	}
}
