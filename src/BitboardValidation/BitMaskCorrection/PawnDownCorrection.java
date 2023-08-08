package BitboardValidation.BitMaskCorrection;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;

public class PawnDownCorrection extends BitMaskCorrection{
    public PawnDownCorrection(Bitboard board, MovementBitBoardGenerator generator) {
        super(board, generator);
    }

    @Override
    public ULong correct(ULong inputMask, int row, int col) {
        ULong result = new ULong(inputMask);
        result.and(board.enemyPieces.combinedPieces);
        if(row < 7 && !(!board.enemyPieces.combinedPieces.at(row + 1, col) && !board.playerPieces.combinedPieces.at(row + 1, col))){
            result.unset(row + 1, col);
        } else if (row < 7) {
            result.set(row + 1, col);
        }
        return result;
    }
}