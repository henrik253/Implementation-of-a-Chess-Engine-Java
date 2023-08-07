package BitboardValidation.BitMaskCorrection;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;
import BitboardValidation.MoveValidation.BitBoardMoveValidation;

public abstract class BitMaskCorrection {
    final MovementBitBoardGenerator generator;
    public Bitboard board;
    BitBoardMoveValidation validation;
    BitMaskCorrection(Bitboard board, MovementBitBoardGenerator generator){
        this.board = board;
        this.generator = generator;
    }
    public abstract ULong correct(ULong inputMask, int row, int col);
}
