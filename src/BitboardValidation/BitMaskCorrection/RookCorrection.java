package BitboardValidation.BitMaskCorrection;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;

public class RookCorrection extends BitMaskCorrection{

    public RookCorrection(Bitboard board, MovementBitBoardGenerator generator) {
        super(board, generator);
    }

    @Override
    public ULong correct(ULong inputMask, int row, int col) {
        return inputMask;
    }

}
