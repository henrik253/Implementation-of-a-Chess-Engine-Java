package BitboardValidation.BitMaskCorrection;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;

public class PawnUpCorrection extends BitMaskCorrection{
    public PawnUpCorrection(Bitboard board, MovementBitBoardGenerator generator) {
        super(board, generator);
    }

    @Override
    public ULong correct(ULong inputMask, int row, int col) {
        ULong result = new ULong(inputMask);
        result.and(board.enemyPieces.combinedPieces);
        if(row > 0 && !(!board.enemyPieces.combinedPieces.at(row - 1, col) && !board.playerPieces.combinedPieces.at(row - 1, col))){
            result.unset(row - 1, col);
        } else if (row > 0 ) {
            result.set(row - 1, col);
        }
        return result;
    }

}
